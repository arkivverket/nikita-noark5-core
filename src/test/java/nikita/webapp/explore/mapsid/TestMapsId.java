package nikita.webapp.explore.mapsid;

import nikita.common.repository.n5v5.secondary.ICorrespondencePartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import java.util.UUID;

/**
 * When working on the domain model, we are experiencing problems tidying
 * up the @OneToOne associations. We have tried a few different approaches but
 * are now creating a test to see if we can figure out why our model with UUID
 * is not working.
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TestMapsId {

    private final Logger logger =
            LoggerFactory.getLogger(TestMapsId.class);

    @Autowired
    EntityManager entityManager;

    @Autowired
    ParentRepo parentRepo;

    @Autowired
    ChildRepo childRepo;

    @Autowired
    ICorrespondencePartRepository correspondencePartRepository;

    @Test
    /**
     * Make a working example that shows how the approach should work
     */
    public void testCreateParentChildSpring() {
        Parent parent = new Parent();
        Child child = new Child();
        parent.setChild(child);
        parentRepo.save(parent);

        UUID primaryKey = parent.getCode();
        Parent persistedParent = entityManager
                .find(Parent.class, primaryKey);
        logger.info("Parent object is " + persistedParent.toString());

        Child persistedChild = entityManager
                .find(Child.class, primaryKey);
        logger.info("Child object is " + persistedChild.toString());
    }
}
