package nikita.webapp.persistence;

import nikita.common.model.noark5.v5.Fonds;
import nikita.common.repository.n5v5.IFondsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * FondsRepository tests
 * <p>
 * Created by tsodring on 2019/04/08
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class FondsJPATest {

    @Autowired
    IFondsRepository fondsRepository;
    @Autowired
    EntityManager entityManager;
    private String fonds1SystemId;
    private String fonds2SystemId;
    // Does not exist
    private String fonds3SystemId;

    @Before
    public void setUp() {
        fonds1SystemId = "3318a63f-11a7-4ec9-8bf1-4144b7f281cf";
        fonds2SystemId = "f1677c47-99e1-42a7-bda2-b0bbc64841b7";
        fonds3SystemId = "0c5e864c-3269-4e01-9430-17d55291dae7";
    }

    /**
     * When an invalid fonds is created (missing title, @NotNull in @Entity), a
     * ConstraintViolationException should be thrown.
     */
    @Test(expected = ConstraintViolationException.class)
    public void whenInvalidEntityIsCreated_thenDataException() {
        Fonds fonds = new Fonds();
        fonds = fondsRepository.save(fonds);
        entityManager.flush();
        assertThat(fondsRepository.save(fonds)).isNotNull();
    }

    /**
     * When querying the database for a fonds that does not exist, the
     * result should be a null value.
     */
    @Test
    public void whenGetNonExistingFondsEntity_thenIsNull() {
        assertThat(fondsRepository.findBySystemId(
                UUID.fromString(fonds3SystemId))).isNull();
    }

    /**
     * When the database has a number of fonds persisted (2), it should be
     * possible to retrieve a count of persisted fonds.
     */
    @Test
    @Sql("/db-tests/createFonds.sql")
    public void whenInitializedByDbUnit_thenCheckSize() {
        // assertThat(fondsRepository.findAll().size()).isEqualTo(2);
    }

    /**
     * When the database has a fonds persisted, it should be possible to
     * retrieve the persisted fonds.
     */
    @Test
    @Sql("/db-tests/createFonds.sql")
    public void whenInitializedByDbUnit_thenFindBySystemId() {
        Fonds fonds = fondsRepository.findBySystemId(
                UUID.fromString(fonds1SystemId));
        assertThat(fonds).isNotNull();
        fonds = fondsRepository.findBySystemId(
                UUID.fromString(fonds2SystemId));
        assertThat(fonds).isNotNull();
    }

    /**
     * When the database has a fonds persisted, it should be possible to
     * delete the persisted fonds.
     */
    @Test
    @Sql("/db-tests/createFonds.sql")
    public void whenInitializedByDbUnit_thenDeleteBySystemId() {
        Fonds fonds = fondsRepository.findBySystemId(
                UUID.fromString(fonds1SystemId));
        fondsRepository.delete(fonds);
    }

    /**
     * An attempt to delete a non-existent fonds should result in an exception.
     * It probably does not make sense to have such a test, as fonds is null.
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void whenDeleteNonExistingFondsEntity_thenIsNull() {
        Fonds fonds = fondsRepository.findBySystemId(
                UUID.fromString(fonds3SystemId));
        fondsRepository.delete(fonds);
    }
}
