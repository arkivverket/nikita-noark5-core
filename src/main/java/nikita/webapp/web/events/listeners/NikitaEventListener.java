package nikita.webapp.web.events.listeners;

import nikita.common.model.noark5.v4.DocumentDescription;
import nikita.common.model.noark5.v4.DocumentObject;
import nikita.common.model.noark5.v4.Fonds;
import nikita.common.model.noark5.v4.Series;
import nikita.common.model.noark5.v4.casehandling.CaseFile;
import nikita.common.model.noark5.v4.casehandling.RegistryEntry;
import nikita.common.model.noark5.v4.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v4.interfaces.entities.INoarkGeneralEntity;
import nikita.webapp.web.events.AfterNoarkEntityCreatedEvent;
import nikita.webapp.web.events.AfterNoarkEntityDeletedEvent;
import nikita.webapp.web.events.AfterNoarkEntityEvent;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by tsodring on 4/4/17.
 * <p>
 * Event listeners to be able to respond when CRUD that operations have
 * occurred. Currently this is blockchain integration or to republish
 * information in another Noark core.
 */

@Async
@Component
@Profile("eventhandling")
public class NikitaEventListener {

    protected static final Logger logger =
            LoggerFactory.getLogger(NikitaEventListener.class);
    @Value("${nikita.simplechain.address}")
    private String urlSimpleChain;

    @EventListener
    public void handleCreationEvent(AfterNoarkEntityCreatedEvent event) {
        createBlockAndPost(event, "create");
        logger.info("Nikita created an object of type [" +
                event.getEntity().getClass().getSimpleName() + "], " +
                event.toString());
    }

    @EventListener
    public void handleUpdateEvent(AfterNoarkEntityUpdatedEvent event) {
        createBlockAndPost(event, "update");
        logger.info("Nikita updated an object of type [" +
                event.getEntity().getClass().getSimpleName() + "], " +
                event.toString());
    }

    @EventListener
    public void handleDeletionEvent(AfterNoarkEntityDeletedEvent event) {
        createBlockAndPost(event, "delete");
        logger.info("Nikita deleted an object of type [" +
                event.getEntity().getClass().getSimpleName() + "], " +
                event.toString());
    }

    private void createBlockAndPost(AfterNoarkEntityEvent event,
                                    String eventType) {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(urlSimpleChain);

        if (client == null) {
            logger.error("could not create CloseableHttpClient");
            return;
        }

        try {

            JSONObject block = new JSONObject();

            block.put("header", event.getEntity().getSystemId());
            JSONObject body = new JSONObject();
            String entityType = event.getEntity().getBaseTypeName();
            body.put("object-type", entityType);
            body.put("event-type", eventType);

            INikitaEntity entity = event.getEntity();

            if (entity instanceof INoarkGeneralEntity) {

                INoarkGeneralEntity noarkEntity = (INoarkGeneralEntity) entity;

                body.put(CREATED_DATE, noarkEntity.getCreatedDate());

                if (((INoarkGeneralEntity) entity).getFinalisedDate() != null) {
                    body.put(TITLE, noarkEntity.getTitle());
                    body.put(FINALISED_DATE, noarkEntity.getFinalisedDate());
                }
                switch (entityType) {
                    case FONDS:
                        body.put(TITLE, noarkEntity.getTitle());
                        body.put(FONDS_STATUS, ((Fonds) entity).getFondsStatus());
                        break;
                    case SERIES:
                        body.put(SERIES_STATUS, ((Series) entity).getSeriesStatus());
                        break;
                    case CASE_FILE:
                        body.put(FILE_ID, ((CaseFile) entity).getFileId());
                        body.put(CASE_STATUS, ((CaseFile) entity).getCaseStatus());
                        break;
                }
            } else if (entityType.equals(REGISTRY_ENTRY)) {
                body.put(REGISTRY_ENTRY_NUMBER, ((RegistryEntry) entity).
                        getRegistryEntryNumber());
                body.put(REGISTRY_ENTRY_TYPE, ((RegistryEntry) entity).
                        getRegistryEntryType());
                body.put(REGISTRY_ENTRY_STATUS, ((RegistryEntry) entity).
                        getRecordStatus());
                body.put(REGISTRY_ENTRY_DATE, ((RegistryEntry) entity).
                        getDocumentDate());
            } else if (entityType.equals(DOCUMENT_DESCRIPTION)) {
                body.put(ASSOCIATED_WITH_RECORD_AS,
                        ((DocumentDescription) entity).
                                getAssociatedWithRecordAs());
                body.put(DOCUMENT_TYPE,
                        ((DocumentDescription) entity).
                                getDocumentType());
                body.put(DOCUMENT_DESCRIPTION_DOCUMENT_NUMBER,
                        ((DocumentDescription) entity).
                                getDocumentNumber());
            } else if (entityType.equals(DOCUMENT_OBJECT)) {
                DocumentObject documentObject = (DocumentObject) entity;
                body.put(DOCUMENT_OBJECT_CHECKSUM,
                        documentObject.getChecksum());
                body.put(DOCUMENT_OBJECT_CHECKSUM_ALGORITHM,
                        documentObject.getChecksumAlgorithm());
                body.put(DOCUMENT_OBJECT_MIME_TYPE,
                        documentObject.getMimeType());
                body.put(DOCUMENT_OBJECT_FILE_SIZE,
                        documentObject.getFileSize());
            }

            block.put("body", body);

            post.setEntity(new StringEntity(block.toString()));
            post.setHeader("Accept", APPLICATION_JSON_VALUE);
            post.setHeader("Content-type", APPLICATION_JSON_VALUE);

            HttpResponse response = client.execute(post);

            int status = response.getStatusLine().getStatusCode();
            String message = response.getStatusLine().getReasonPhrase();
            if (status != 201) {
                logger.error("SimpleChain integration problem (" + status +
                        ") message " + message);
            }
            client.close();
        } catch (IOException | JSONException e) {
            logger.error("Could not create JSON object during " +
                    event.toString());
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
