package nikita.webapp;

import nikita.common.model.noark5.v5.hateoas.Link;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkGeneralEntity;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static nikita.common.config.Constants.TEST_DESCRIPTION;
import static nikita.common.config.Constants.TEST_TITLE;
import static nikita.common.config.HATEOASConstants.SELF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Base class with common functionality for various service level tests.
 * <p>
 * Created by tsodring 2019/04/08
 */
public class Base {

    protected Set<String> requiredRels = new TreeSet<>();

    protected void initRelList() {
        requiredRels.add(SELF);
    }

    /**
     * Check that the title and description values set prior to sending the
     * object to the service layer still have the correct values
     *
     * @param noarkEntity The Noark object to check
     */
    protected void checkSetValuesCorrect(INoarkGeneralEntity noarkEntity) {
        assertEquals(noarkEntity.getTitle(), TEST_TITLE);
        assertEquals(noarkEntity.getDescription(), TEST_DESCRIPTION);
    }

    /**
     * Check that systemId, createdBy, createdDate are not null
     *
     * @param noarkEntity The Noark object to check
     */
    protected void checkCreatedVariablesNotNull(
            INoarkGeneralEntity noarkEntity) {
        assertNotNull(noarkEntity.getSystemId());
        assertNotNull(noarkEntity.getCreatedBy());
        assertNotNull(noarkEntity.getCreatedDate());
    }

    /**
     * Check that the ownedBy field is set to tha value you expect it should
     * be set to.
     *
     * @param noarkEntity The Noark object to check
     * @param user        The value of user to check against
     */
    protected void checkOwnedBySet(INoarkGeneralEntity noarkEntity,
                                   String user) {
        assertEquals(noarkEntity.getOwnedBy(), user);
    }

    /**
     * There are a set of required links defined for each Hateaoas object type.
     * When a set of links are added to a Hateoas* object, it should be possible
     * to make sure the links are in the required set. This method checks that
     * all links that should be there are there and should detect if there are
     * any superfluous links.
     * <p>
     *
     * @param links List of links to traverse and check against requiredRels
     */
    protected void checkLinks(List<Link> links) {
        // Make sure both sets are the same size. If both sets are not the
        // same size there is a problem somewhere
        assertEquals(requiredRels.size(), links.size());

        // Go through each Links.link and check that it has a corresponding rel
        // in requiredRels.
        int countLinksRemoved = links.size();
        int countRelsRemoved = requiredRels.size();
        for (Link link : links) {
            if (requiredRels.contains(link.getRel())) {
                countLinksRemoved--;
                countRelsRemoved--;
            }
        }
        // countRemoved should be 0
        assertEquals(countLinksRemoved, 0);

        // requiredRels shold be empty
        assertEquals(countRelsRemoved, 0);
    }
}
