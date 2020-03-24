package nikita.webapp.structure;

import nikita.common.repository.nikita.IUserRepository;
import nikita.webapp.service.impl.odata.ODataService;
import nikita.webapp.util.InternalNameTranslator;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import static nikita.common.config.Constants.NOARK_FONDS_STRUCTURE_PATH;
import static nikita.common.config.Constants.SLASH;
import static nikita.common.config.N5ResourceMappings.FONDS;
import static org.junit.Assert.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TestODataService {

    @Autowired
    private DataSource dataSource;
    @InjectMocks
    private ODataService oDataService;
    @Mock
    private EntityManager entityManager;
    @Autowired
    private IUserRepository userRepository;

    private InternalNameTranslator internalNameTranslator =
            new InternalNameTranslator();

    @BeforeClass
    public void init() {
        internalNameTranslator.populateTranslatedNames();
    }

    @Test
    void injectedComponentsAreNotNull() {
        assertNotNull(dataSource);
        assertNotNull(entityManager);
        assertNotNull(userRepository);
        assertNotNull(oDataService);
    }

    public void testThis() {
        internalNameTranslator.populateTranslatedNames();
        String contains = "$filter=contains(tittel, 'bravo')";
        String baseAddress = NOARK_FONDS_STRUCTURE_PATH + SLASH + FONDS;
        String address = baseAddress + "?" + contains;
        oDataService.convertODataToHQL(address, null);
    }
}
