package nikita.webapp.service.impl.secondary;

import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.hateoas.secondary.ConversionHateoas;
import nikita.common.model.noark5.v5.secondary.Conversion;
import nikita.common.repository.n5v5.secondary.IConversionRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.secondary.IConversionHateoasHandler;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.odata.IODataService;
import nikita.webapp.service.interfaces.secondary.IConversionService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;

/**
 * Note, ConversionService is already an object in Spring so I need to name
 * this class differently to avoid a name conflict and subsequent exceptions.
 */
@Service
public class NikitaConversionService
        extends NoarkService
        implements IConversionService {

    private final IConversionRepository conversionRepository;
    private final IConversionHateoasHandler conversionHateoasHandler;

    // All CREATE methods

    public NikitaConversionService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IODataService odataService,
            IPatchService patchService,
            IConversionRepository conversionRepository,
            IConversionHateoasHandler conversionHateoasHandler) {
        super(entityManager, applicationEventPublisher, patchService, odataService);
        this.conversionRepository = conversionRepository;
        this.conversionHateoasHandler = conversionHateoasHandler;
    }

    // All READ methods

    @Override
    public ConversionHateoas save(Conversion conversion) {
        return packAsHateoas(conversionRepository.save(conversion));
    }

    // All DELETE methods

    @Override
    public ConversionHateoas findBySystemId(@NotNull final UUID systemId) {
        return packAsHateoas(getConversionOrThrow(systemId));
    }

    // All template methods

    @Override
    public Conversion findConversionBySystemId(@NotNull final UUID systemId) {
        return getConversionOrThrow(systemId);
    }

    // All helper methods

    @Override
    @Transactional
    public void deleteConversion(@NotNull final UUID systemId) {
        conversionRepository.delete(getConversionOrThrow(systemId));
    }

    public ConversionHateoas generateDefaultConversion(
            @NotNull final UUID systemId,
            @NotNull final DocumentObject documentObject) {
        Conversion defaultConversion = new Conversion();
        /* Propose conversion done now by logged in user */
        defaultConversion.setConvertedDate(OffsetDateTime.now());
        defaultConversion.setConvertedBy(getUser());
        defaultConversion.setConvertedToFormat(documentObject.getFormat());
        defaultConversion.setVersion(-1L, true);
        return packAsHateoas(defaultConversion);
    }

    public ConversionHateoas packAsHateoas(@NotNull final Conversion conversion) {
        ConversionHateoas conversionHateoas = new ConversionHateoas(conversion);
        applyLinksAndHeader(conversionHateoas, conversionHateoasHandler);
        return conversionHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. Note. If you call this, you
     * will only ever get a valid Conversion back. If there is no valid
     * Conversion, an exception is thrown
     *
     * @param systemId systemId of the Conversion object to retrieve
     * @return the Conversion object
     */
    protected Conversion getConversionOrThrow(@NotNull final UUID systemId) {
        Conversion conversion = conversionRepository.findBySystemId(systemId);
        if (conversion == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " Conversion, using systemId " + systemId;
            throw new NoarkEntityNotFoundException(info);
        }
        return conversion;
    }
}
