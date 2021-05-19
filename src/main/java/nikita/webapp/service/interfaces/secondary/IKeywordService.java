package nikita.webapp.service.interfaces.secondary;

import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.hateoas.secondary.KeywordHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.secondary.Keyword;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public interface IKeywordService {

    KeywordHateoas createKeywordAssociatedWithFile(
            @NotNull final Keyword keyword, @NotNull final File file);

    KeywordHateoas createKeywordAssociatedWithRecord(
            @NotNull final Keyword keyword, @NotNull final Record record);

    KeywordHateoas createKeywordAssociatedWithClass(
            @NotNull final Keyword keyword, @NotNull final Class klass);

    KeywordHateoas findBySystemId(@NotNull final UUID systemId);

    KeywordHateoas findAllByOwner();

    KeywordHateoas updateKeywordBySystemId(
            @NotNull final UUID systemId,
            @NotNull final Long version,
            @NotNull final Keyword incomingKeyword);

    void deleteKeywordBySystemId(@NotNull final UUID systemId);

    KeywordHateoas generateDefaultKeyword();

    KeywordHateoas packKeywordsAsHateaos(
            @NotNull final List<INoarkEntity> keywords);
}
