package nikita.webapp.persistence;

import nikita.common.model.noark5.v4.Fonds;
import nikita.common.model.noark5.v4.hateoas.FondsHateoas;
import nikita.common.repository.n5v4.IFondsRepository;
import nikita.webapp.hateoas.FondsHateoasHandler;
import nikita.webapp.service.impl.FondsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Testing how to create tests at the service layer. These will be simple tests
 * that can be run during CD/CI to capture the basic functionality of the
 * service layer.
 * <p>
 * I am still missing a basic understanding of mocking as I am expecting
 * the service layer objects repository etc actually are working, when in
 * reality I am mocking them up. I just don't have time to work on this now.
 * <p>
 * The problem here is that fondsHateoasHandler is not being instantiated
 * properly so the List coming back is empty.
 */
@RunWith(SpringRunner.class)
public class ApplicationTest {

    @InjectMocks
    FondsService fondsService;

    @Mock
    IFondsRepository fondsRepository;

    @Mock
    ApplicationEventPublisher applicationEventPublisher;

    @Mock
    FondsHateoasHandler fondsHateoasHandler;

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN", "USER"})
    public void createFonds() {
        Fonds fonds = new Fonds();
        fonds.setDescription("Hello");
        fonds.setTitle("Hello");
        FondsHateoas fondsHateoas = fondsService.createNewFonds(fonds);

        // Check systemId has a value
        // This fails as the list is empty. However the fonds object above
        // has values set from interacting with FondsService.
        assertNotNull(fondsHateoas.getList().get(0).getSystemId());
        assertTrue(fondsHateoas.getList().get(0).getClass().
                isInstance(Fonds.class));
        System.out.println(fondsHateoas);
    }

}

