package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.FileType;

import javax.validation.constraints.NotNull;

/**
 * Created by tsodring on 03/03/18.
 */

public interface IFileTypeService {

    MetadataHateoas createNewFileType(FileType fileType);

    MetadataHateoas findAll();

    MetadataHateoas findByCode(String code);

    MetadataHateoas handleUpdate(
            @NotNull final String systemId,
            @NotNull final Long version,
            @NotNull final FileType fileType);

    FileType generateDefaultFileType();
}
