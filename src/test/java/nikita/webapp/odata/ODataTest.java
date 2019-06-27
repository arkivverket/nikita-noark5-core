package nikita.webapp.odata;

import nikita.webapp.service.impl.odata.ODataService;
import nikita.webapp.util.InternalNameTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static nikita.common.config.Constants.NOARK_FONDS_STRUCTURE_PATH;
import static nikita.common.config.Constants.SLASH;
import static nikita.common.config.N5ResourceMappings.FONDS;

@RunWith(MockitoJUnitRunner.class)
public class ODataTest {

    @InjectMocks
    ODataService oDataService;

    @InjectMocks
    InternalNameTranslator nameTranslator;

    @PersistenceContext
    private EntityManager entityManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void setup() {
        nameTranslator.populateTranslatedNames();
    }

    @Test
    public void testThis() {
        String contains = "$filter=contains(tittel, 'bravo')";
        String baseAddress = NOARK_FONDS_STRUCTURE_PATH + SLASH + FONDS;
        String address = baseAddress + "?" + contains;
        oDataService.convertODataToHQL(address, null);
    }
}
