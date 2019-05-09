package nikita.webapp.hateoas;

import nikita.common.model.noark5.v4.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v4.hateoas.Link;
import nikita.common.model.noark5.v4.interfaces.entities.INikitaEntity;
import nikita.webapp.hateoas.interfaces.IClassificationSystemHateoasHandler;
import org.springframework.stereotype.Component;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

/**
 * Created by tsodring on 2/6/17.
 * <p>
 * Used to add ClassificationSystemHateoas links with ClassificationSystem specific information
 * <p>
 * Not sure if there is a difference in what should be returned of links for various CRUD operations so keeping them
 * separate calls at the moment.
 *
 * Note: SecondaryClassification seems to have a different form in the interface than in the standard. Leaving
 * it for the moment, but must bre visited. Perhaps a mangelmelding to get a better description of it
 *
 * The problem I see is that you now declare a classificationsystem as a secondary classificationsystem.
 * Does this preclude classes belonging to a primary classification system from being assigned as a
 * sekundaerklassifikasjon?
 *
 * A define secondary clasification system is useful in that it might allow arkivskapere to create activity
 * descriptions increasing the overall use of "tagging" within the system.
 *
 * The following should be added to Classificationtype in db
 Gårds- og bruksnummer GBN
 Funksjonsbasert, hierarkisk FH
 Emnebasert, hierarkisk arkivnøkkel EH
 Emnebasert, ett nivå E1
 K-koder KK Mangefasettert, ikke hierarki MF
 Objektbasert UO
 FødselsnummerPNR

 ny-klassifikasjon and ny-sekundaerklassifiikasjon should be visible of the root

 What is the purpose of this REL?
 http://rel.arkivverket.no/noark5/v4/api/arkivstruktur/ny-arkivdel/
 It must be a 'helper'. Normally you create a arkivdel and assign classificationsystem
 Here ,it must just be a shortcut, but how do you assign the arkivdel to its arkiv? This must be wrong!!
 */
@Component("classificationSystemHateoasHandler")
public class ClassificationSystemHateoasHandler extends HateoasHandler implements IClassificationSystemHateoasHandler {

    public ClassificationSystemHateoasHandler(String contextPath) {
        super(contextPath);
    }

    public ClassificationSystemHateoasHandler() {
    }

    @Override
    public void addEntityLinks(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {

        // links for primary entities
        addNewClassificationSystem(entity, hateoasNoarkObject, outgoingAddress);
        addNewSecondaryClassificationSystem(entity, hateoasNoarkObject, outgoingAddress);
        addSecondaryClassificationSystem(entity, hateoasNoarkObject, outgoingAddress);
        addClass(entity, hateoasNoarkObject, outgoingAddress);
        addNewClass(entity, hateoasNoarkObject, outgoingAddress);
        addSeries(entity, hateoasNoarkObject, outgoingAddress);
        // links for secondary entities
        // No secondary entities
        // links for metadata entities
        addClassificationType(entity, hateoasNoarkObject, outgoingAddress);
    }

    @Override
    public void addClass(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + CLASSIFICATION_SYSTEM + SLASH + entity.getSystemId() + SLASH + CLASS
                + SLASH, REL_FONDS_STRUCTURE_CLASS, false));
    }

    @Override
    public void addNewClass(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + CLASSIFICATION_SYSTEM + SLASH + entity.getSystemId() + SLASH + NEW_CLASS
                + SLASH, REL_FONDS_STRUCTURE_NEW_CLASS, false));
    }

    @Override
    public void addSeries(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + CLASSIFICATION_SYSTEM + SLASH + entity.getSystemId() + SLASH + SERIES
                + SLASH, REL_FONDS_STRUCTURE_SERIES, false));
    }

    @Override
    public void addNewClassificationSystem(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + CLASSIFICATION_SYSTEM + SLASH + entity.getSystemId() + SLASH +
                NEW_CLASSIFICATION_SYSTEM + SLASH, REL_FONDS_STRUCTURE_NEW_CLASSIFICATION_SYSTEM, false));
    }

    @Override
    public void addNewSecondaryClassificationSystem(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + CLASSIFICATION_SYSTEM + SLASH + entity.getSystemId() + SLASH +
                NEW_SECONDARY_CLASSIFICATION + SLASH, REL_FONDS_STRUCTURE_NEW_SECONDARY_CLASSIFICATION, false));
    }

    @Override
    public void addSecondaryClassificationSystem(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_FONDS_STRUCTURE_PATH + SLASH + CLASSIFICATION_SYSTEM + SLASH + entity.getSystemId() + SLASH +
                SECONDARY_CLASSIFICATION + SLASH, REL_FONDS_STRUCTURE_SECONDARY_CLASSIFICATION, false));
    }

    @Override
    public void addClassificationType(INikitaEntity entity, IHateoasNoarkObject hateoasNoarkObject, String outgoingAddress) {
        hateoasNoarkObject.addLink(entity, new Link(outgoingAddress + HATEOAS_API_PATH + SLASH +
                NOARK_METADATA_PATH + SLASH + CLASSIFICATION_SYSTEM + SLASH + entity.getSystemId() + SLASH + CLASSIFICATION_SYSTEM_TYPE
                + SLASH, REL_METADATA_CLASSIFICATION_SYSTEM_TYPE, false));
    }
}
