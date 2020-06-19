package nikita.webapp.odata;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static nikita.common.config.Constants.DM_OWNED_BY;
import static nikita.common.config.ESConstants.*;

/**
 * Extending ODataWalker to handle events so we can convert OData filter
 * command to an ElasticSearch query.
 * <p>
 * We will just attempt to keep this code in sync with SQL/HQL. We're not even
 * using ES at the moment so I won't be testing the ES query JSON syntax.
 * <p>
 * NOTE. THIS CODE HAS NOT BEEN TESTED TO SEE IF IT RUNS! IT COMPILES BUT
 * MAY NOT DO ANYTHING.
 */

public class ODataToES {
    // There is not time right now to get this code also in place. The
    // concept is definitely nice, to convert OData to HQL, SQL and ES, but HQL
    // is all we require at the moment. Leaving it in the repo commented out
    // so the we may try and piece it together when we have some extra time
    // now and again
    private static final Logger logger =
            LoggerFactory.getLogger(ODataToES.class);


    private JSONObject query;

    public void processEnterTop() {
        query.put(QUERY_SIZE, 10);
    }

    public void processEnterSkip() {
        query.put(QUERY_FROM, 10);
    }

    /**
     * processEnterResource
     * <p>
     * When dealing with the following example URL:
     * <p>
     * [contextPath][api]/arkivstruktur/arkiv?$filter=startsWith(tittel,'hello')
     * <p>
     * It is assumed that the resource part 'arkiv' is external to the query,
     * that a query to the es instance will identify the resource and just
     * upload the query part that we return. This can be seen with the
     * following GET request:
     * <p>
     * GET /index/arkiv/_search
     * <p>
     * This method creates the basic template for a ES query, and should
     * create JSON that looks something like:
     * <p>
     * {
     * "query": {
     * }
     * }
     * <p>
     * We always add to the where clause to filter out rows that actually
     * belong to the user first and then can add extra filtering as
     * the walker progresses.
     * <p>
     * Note this will cause some problems when dealing with ownership of objects
     * via groups. Probably have to some lookup or something. But we are
     * currently just dealing with getting OData2ES to work.
     */

    public void processResource(String entity, String loggedInUser) {
        query = new JSONObject("{\n\t \"" + QUERY_QUERY +
                "\" : {\n\t}\n}\n");
        // Create a "sub JSON object" to hold the ownedBy
        JSONObject where = new JSONObject();
        where.put(DM_OWNED_BY, loggedInUser);
        query.put(QUERY_QUERY, where);
    }


    public void processNikitaObjects(
            String parentResource, String resource, String systemId, String loggedInUser) {
        logger.error("processNikitaObjects ES not implemented");
    }
    /**
     * processStartsWith
     * <p>
     * Convert 'startsWith' to a ES LIKE query in SQL. In the following
     * example:
     * <p>
     * [contextPath][api]/arkivstruktur/arkiv?$filter=startsWith(tittel,'hello')
     * <p>
     * startsWith(tittel, 'hello') becomes the following JSON:
     * <p>
     * GET /index/arkiv/_search
     * {
     * "query": {
     * "prefix": {
     * "tittel": "hello"
     * }
     * }
     * }
     * <p>
     * Our approach is to check to see if a prefix object exists. If it does
     * just add the attribute and value pair. If not create a prefix object,
     * add the attribute and value pair, then add the prefix object to the
     * query. In essence, we are just taking care of the following part.
     * <p>
     * "prefix": {
     * "tittel": "hello"
     * }
     *
     * @param attribute The attribute/column you wish to filter on
     * @param value     The value you wish to filter on
     */

    public void processStartsWith(String attribute, String value) {
        JSONObject prefix = (JSONObject) query.get(QUERY_PREFIX);
        if (prefix == null) {
            prefix = new JSONObject();
            //prefix.put(getInternalNameObject(attribute), value);
            query.put(QUERY_PREFIX, prefix);
        } else {
            // prefix.put(getInternalNameObject(attribute), value);
        }
    }

    /**
     * processContains
     * <p>
     * Convert a OData 'contains' to a ES LIKE query (a prefix query). In the
     * following example:
     * <p>
     * [contextPath][api]/arkivstruktur/arkiv?$filter=contains(tittel, 'hello')
     * <p>
     * contains(tittel, 'hello') becomes the following JSON:
     * <p>
     * GET /index/arkiv/_search
     * {
     * "query": {
     * "match_phrase": {
     * "tittel": "hello"
     * }
     * }
     * }
     * <p>
     * Our approach is to check to see if a matchPhrase object exists. If it
     * does just add the attribute and value pair. If not create a
     * matchPhrase object, add the attribute and value pair, then add the
     * matchPhrase object to the query. In essence, we are just taking care
     * of the following part.
     * <p>
     * "match_phrase": {
     * "tittel": "hello"
     * }
     *
     * @param attribute The attribute/column you wish to filter on
     * @param value     The value you wish to filter on
     */

    public void processContains(String attribute, String value) {

        JSONObject matchPhrase = (JSONObject) query.get(QUERY_MATCH_PHRASE);
        if (matchPhrase == null) {
            matchPhrase = new JSONObject();
            //matchPhrase.put(getInternalNameObject(attribute), value);
            query.put(QUERY_MATCH_PHRASE, matchPhrase);
        } else {
            //matchPhrase.put(getInternalNameObject(attribute), value);
        }
    }

    /**
     * processComparatorCommand
     * <p>
     * Convert a general Odata attribute comparator value command to ES JSON. In
     * the following example:
     * <p>
     * [contextPath][api]/arkivstruktur/arkiv?$filter=tittel eq 'hello'
     * <p>
     * tittel eq 'hello' becomes the following ES JSON:
     * <p>
     * <p>
     * This is achieved by looking at the children and picking out the
     * attribute (in this case 'tittel'), the comparator (in this case eq) and
     * value (in this case 'hello').
     *
     * @param attribute  The attribute/column you wish to filter on
     * @param comparator The type of comparison you want to undertake e.g eq,
     *                   lt etc
     * @param value      The value you wish to filter on
     */

    public void processComparatorCommand(String attribute, String comparator,
                                         String value) {

    }


    public void processSkipCommand(Integer skip) {

    }


    public void processTopCommand(Integer top) {

    }


    public void processOrderByCommand(String attribute, String sortOrder) {

    }

    public JSONObject getQuery() {
        return query;
    }
}
