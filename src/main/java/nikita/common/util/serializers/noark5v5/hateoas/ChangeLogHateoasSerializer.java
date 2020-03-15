package nikita.common.util.serializers.noark5v5.hateoas;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.ChangeLogHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.IChangeLogEntity;
import nikita.common.model.noark5.v5.interfaces.entities.IEventLogEntity;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;
import nikita.webapp.hateoas.ChangeLogHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;

@HateoasPacker(using = ChangeLogHateoasHandler.class)
@HateoasObject(using = ChangeLogHateoas.class)
public class ChangeLogHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {
    public static void serialiseChangeLog(IChangeLogEntity changeLog,
                                          JsonGenerator jgen)
            throws IOException {
            printSystemIdEntity(jgen, changeLog);
            printNullable(jgen, REFERENCE_ARCHIVE_UNIT,
                          changeLog.getReferenceArchiveUnitSystemId());
            printNullable(jgen, REFERENCE_METADATA, changeLog.getReferenceMetadata());
            printDateTime(jgen, CHANGED_DATE, changeLog.getChangedDate());
            printNullable(jgen, CHANGED_BY, changeLog.getChangedBy());
            printNullable(jgen, REFERENCE_CHANGED_BY,
                          changeLog.getReferenceChangedBy());
            printNullable(jgen, OLD_VALUE, changeLog.getOldValue());
            printNullable(jgen, NEW_VALUE, changeLog.getNewValue());
    }

    @Override
    public void serializeNoarkEntity(INoarkEntity noarkEntity,
                                     HateoasNoarkObject changeLogHateoas,
                                     JsonGenerator jgen)
            throws IOException {
        IChangeLogEntity changeLog = (IChangeLogEntity)noarkEntity;
        jgen.writeStartObject();

        if (changeLog != null) {
            serialiseChangeLog(changeLog, jgen);
            if (changeLog instanceof IEventLogEntity) {
                EventLogHateoasSerializer
                    .serialiseEventLog((IEventLogEntity)changeLog, jgen);
            }
        }
        printHateoasLinks(jgen, changeLogHateoas.getLinks(changeLog));
        jgen.writeEndObject();
    }
}
