package nikita.webapp.service.interfaces.odata;

import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;

public interface IODataService {

    /**
     * Process an OData Query that corresponds to a GET request.
     *
     * @return a HateoasNoarkObject with the correct type
     * @throws Exception is something goes wrong
     */
    HateoasNoarkObject processODataQueryGet(String odataAppend);

    HateoasNoarkObject processODataQueryGet();

    String processODataQueryDelete();

    HateoasNoarkObject processODataSearchQuery(String search, int fetchCount, int skip);
}
