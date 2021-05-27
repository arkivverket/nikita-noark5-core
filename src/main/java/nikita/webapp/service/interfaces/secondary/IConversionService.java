package nikita.webapp.service.interfaces.secondary;

import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.hateoas.secondary.ConversionHateoas;
import nikita.common.model.noark5.v5.secondary.Conversion;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface IConversionService {

    void deleteConversion(@NotNull final UUID systemId);

    Conversion findConversionBySystemId(@NotNull final UUID systemId);

    ConversionHateoas findBySystemId(@NotNull final UUID systemId);

    ConversionHateoas generateDefaultConversion(
            @NotNull final UUID systemId,
            @NotNull final DocumentObject documentObject);

    ConversionHateoas packAsHateoas(@NotNull final Conversion conversion);

    ConversionHateoas save(@NotNull final Conversion conversion);
}
