package nikita.webapp.service;

import nikita.common.model.noark5.v5.Fonds;
import nikita.common.model.noark5.v5.hateoas.FondsHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkGeneralEntity;
import nikita.common.repository.n5v5.IFondsRepository;
import nikita.webapp.Base;
import nikita.webapp.hateoas.FondsHateoasHandler;
import nikita.webapp.service.impl.FondsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.FONDS_STATUS_OPEN_CODE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Service layer tests related to Fonds.
 * <p>
 * Checks properties on creation and hateoas links
 * <p>
 * Created by tsodring 2019/04/08
 */
@RunWith(SpringRunner.class)
public class FondsTest
        extends Base {

    @InjectMocks
    private FondsService fondsService;

    @Mock
    private IFondsRepository fondsRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Spy
    private FondsHateoasHandler fondsHateoasHandler;

    @Before
    public void init() {
        fondsHateoasHandler = new FondsHateoasHandler();
        initMocks(this);
        initRelList();
    }

    /**
     * 1. Create fonds object
     * 1.1. Check required set values returned not null
     * 1.2. Check values already set remain the same
     * 1.3. Check owner of fonds is set correctly
     * 1.4. Check that the returned Hateoas object contains correct class type
     * 1.5. Check that fondsStatus is set to "Opprettet"
     */
    @Test
    @WithMockUser(username = TEST_USER_ADMIN,
            authorities = {ROLE_RECORDS_MANAGER})
    public void createFondsCheckProperties() {
        Fonds fonds = new Fonds();
        fonds.setDescription(TEST_DESCRIPTION);
        fonds.setTitle(TEST_TITLE);
        when(fondsRepository.save(fonds)).thenReturn(fonds);
        FondsHateoas fondsHateoas = fondsService.createNewFonds(fonds);
        INoarkGeneralEntity noarkEntity =
                (INoarkGeneralEntity) fondsHateoas.getList().get(0);
        checkCreatedVariablesNotNull(noarkEntity);
        checkSetValuesCorrect(noarkEntity);
        checkOwnedBySet(noarkEntity, TEST_USER_ADMIN);
        // Make sure that there is an actual Fonds object embedded in the result
        // of a createNewFonds
        assertTrue(noarkEntity instanceof Fonds);
        assertEquals(((Fonds) noarkEntity).getFondsStatus().getCode(),
                     FONDS_STATUS_OPEN_CODE);
    }

    /**
     * 2. Create Fonds check links
     * 2.1 Check required links are there
     * 2.2 Check no extra links are in returned list
     */
    @Test
    @WithMockUser(username = TEST_USER_ADMIN,
            authorities = {ROLE_RECORDS_MANAGER})
    public void createFondsCheckLinks() {
        Fonds fonds = new Fonds();
        fonds.setDescription(TEST_DESCRIPTION);
        fonds.setTitle(TEST_TITLE);
        when(fondsRepository.save(fonds)).thenReturn(fonds);
        FondsHateoas fondsHateoas = fondsService.createNewFonds(fonds);
        checkLinks(fondsHateoas.getLinks(fonds));
    }

    @Override
    protected void initRelList() {
        super.initRelList();
        requiredRels.add(REL_METADATA_DOCUMENT_MEDIUM);
        requiredRels.add(REL_FONDS_STRUCTURE_FONDS_CREATOR);
        requiredRels.add(REL_FONDS_STRUCTURE_SERIES);
        requiredRels.add(REL_FONDS_STRUCTURE_NEW_FONDS_CREATOR);
        requiredRels.add(REL_FONDS_STRUCTURE_SUB_FONDS);
        requiredRels.add(REL_FONDS_STRUCTURE_NEW_FONDS);
        requiredRels.add(REL_METADATA_FONDS_STATUS);
        requiredRels.add(REL_FONDS_STRUCTURE_NEW_SERIES);
    }
}
