package nikita.common.util.serializers.noark5v5.hateoas;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.EventLogHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IChangeLogEntity;
import nikita.common.model.noark5.v5.interfaces.entities.IEventLogEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;
import nikita.webapp.hateoas.EventLogHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;

@HateoasPacker(using = EventLogHateoasHandler.class)
@HateoasObject(using = EventLogHateoas.class)
public class EventLogHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    public static void serialiseEventLog(IEventLogEntity eventLog,
                                         JsonGenerator jgen)
            throws IOException {
            printNullableMetadata(jgen, EVENT_TYPE, eventLog.getEventType());
            printNullable(jgen, DESCRIPTION, eventLog.getDescription());
            printDateTime(jgen, EVENT_DATE, eventLog.getEventDate());
    }

    @Override
    public void serializeNoarkEntity(INoarkEntity noarkEntity,
                                     HateoasNoarkObject eventLogHateoas,
                                     JsonGenerator jgen)
            throws IOException {
        IEventLogEntity eventLog = (IEventLogEntity)noarkEntity;
        jgen.writeStartObject();

        if (eventLog != null) {
            // First those from ChangeLog
            ChangeLogHateoasSerializer
                .serialiseChangeLog((IChangeLogEntity)eventLog, jgen);

            // and those from EventLog
            serialiseEventLog(eventLog, jgen);
        }
        printHateoasLinks(jgen, eventLogHateoas.getLinks(eventLog));
        jgen.writeEndObject();
    }
}
