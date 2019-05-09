package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v4.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v4.metadata.FileType;

/**
 * Created by tsodring on 03/03/18.
 */

public interface IFileTypeService {

    MetadataHateoas createNewFileType(FileType fileType,
                                      String outgoingAddress);

    MetadataHateoas find(String systemId, String outgoingAddress);

    MetadataHateoas findAll(String outgoingAddress);

    MetadataHateoas findByDescription(String description,
                                      String outgoingAddress);

    MetadataHateoas findByCode(String code, String outgoingAddress);

    MetadataHateoas handleUpdate(String systemId, Long version,
                                 FileType fileType, String outgoingAddress);

    FileType generateDefaultFileType();
}
