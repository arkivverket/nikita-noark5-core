package nikita.webapp.explore.mapsid;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;

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

    @Test
    public void testCreateParentChildSpring() {
        Parent parent = new Parent();
        parent.setCode("12345");
        Child child = new Child();
        parent.setChild(child);
        parentRepo.save(parent);

        Parent persistedParent = entityManager
                .find(Parent.class, "12345");
        logger.info("Parent object is " + persistedParent.toString());
        Child persistedChild = entityManager
                .find(Child.class, "12345");
        logger.info("Child object is " + persistedChild.toString());
    }
}
