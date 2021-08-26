package nikita.webapp.service.interfaces.secondary;

import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.RecordEntity;
import nikita.common.model.noark5.v5.hateoas.secondary.KeywordHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.KeywordTemplateHateoas;
import nikita.common.model.noark5.v5.secondary.Keyword;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface IKeywordService {

    KeywordHateoas createKeywordAssociatedWithFile(
            @NotNull final Keyword keyword, @NotNull final File file);

    KeywordHateoas createKeywordAssociatedWithRecord(
            @NotNull final Keyword keyword, @NotNull final RecordEntity record);

    KeywordHateoas createKeywordAssociatedWithClass(
            @NotNull final Keyword keyword, @NotNull final Class klass);

    KeywordHateoas findBySystemId(@NotNull final UUID systemId);

    KeywordHateoas findAll();

    KeywordHateoas updateKeywordBySystemId(
            @NotNull final UUID systemId,
            @NotNull final Long version,
            @NotNull final Keyword incomingKeyword);

    void deleteKeywordBySystemId(@NotNull final UUID systemId);

    KeywordTemplateHateoas generateDefaultKeyword(@NotNull final UUID systemId);
}
