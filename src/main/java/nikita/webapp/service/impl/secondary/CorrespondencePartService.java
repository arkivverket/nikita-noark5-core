package nikita.webapp.service.impl.secondary;

import nikita.common.model.noark5.v4.casehandling.secondary.*;
import nikita.common.model.noark5.v4.interfaces.entities.casehandling.IContactInformationEntity;
import nikita.common.repository.n5v4.secondary.ICorrespondencePartRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.service.interfaces.secondary.ICorrespondencePartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;

@Service
@Transactional
public class CorrespondencePartService
        implements ICorrespondencePartService {

    private static final Logger logger =
            LoggerFactory.getLogger(CorrespondencePartService.class);

    private ICorrespondencePartRepository correspondencePartRepository;

    public CorrespondencePartService(
            ICorrespondencePartRepository correspondencePartRepository) {
        this.correspondencePartRepository = correspondencePartRepository;
    }

    @Override
    public CorrespondencePart findBySystemId(@NotNull String systemId) {
        return getCorrespondencePartOrThrow(systemId);
    }

    /**
     * Update the CorrespondencePartPerson identified by systemId. Retrieve a
     * copy of the CorrespondencePartPerson from the database. If it does not
     * exist throw a not found exception. If it exists, call sub methods that
     * copy the values. Save the updated object back to the database.
     *
     * @param systemId                   systemId of the
     *                                   CorrespondencePartPerson to update
     * @param version                    ETAG version
     * @param incomingCorrespondencePart incoming CorrespondencePartPerson with
     *                                   values to copy from
     * @return the updated CorrespondencePartPerson
     */
    @Override
    public CorrespondencePartPerson
    updateCorrespondencePartPerson(String systemId, Long version,
                                   CorrespondencePartPerson
                                           incomingCorrespondencePart) {
        CorrespondencePartPerson existingCorrespondencePart =
                (CorrespondencePartPerson)
                        getCorrespondencePartOrThrow(systemId);

        // Copy all the values you are allowed to copy ....
        // First the values
        existingCorrespondencePart.setdNumber(
                incomingCorrespondencePart.getdNumber());
        existingCorrespondencePart.setName(
                incomingCorrespondencePart.getName());
        existingCorrespondencePart.setSocialSecurityNumber(
                incomingCorrespondencePart.getSocialSecurityNumber());

        // Then secondary objects
        updateContactInformation(existingCorrespondencePart.
                        getContactInformation(),
                incomingCorrespondencePart.
                        getContactInformation());
        // Residing address
        updateAddress(existingCorrespondencePart.
                        getResidingAddress().getSimpleAddress(),
                incomingCorrespondencePart.
                        getResidingAddress().getSimpleAddress());
        // Postal address
        updateAddress(existingCorrespondencePart.
                        getPostalAddress().getSimpleAddress(),
                incomingCorrespondencePart.
                        getPostalAddress().getSimpleAddress());
        
        // Check the ETAG
        existingCorrespondencePart.setVersion(version);
        correspondencePartRepository.save(existingCorrespondencePart);
        return existingCorrespondencePart;
    }

    @Override
    public CorrespondencePartInternal updateCorrespondencePartInternal(
            String systemId, Long version,
            CorrespondencePartInternal incomingCorrespondencePart) {
        CorrespondencePartInternal existingCorrespondencePart =
                (CorrespondencePartInternal)
                        getCorrespondencePartOrThrow(systemId);
        // Copy all the values you are allowed to copy ....

        existingCorrespondencePart.setAdministrativeUnit(
                incomingCorrespondencePart.getAdministrativeUnit());
        existingCorrespondencePart.setCaseHandler(
                incomingCorrespondencePart.getCaseHandler());
        //existingCorrespondencePart.setReferenceAdministrativeUnit
        //      (incomingCorrespondencePart.getReferenceAdministrativeUnit());
//        existingCorrespondencePart.setReferenceUser(incomingCorrespondencePart
//                .getReferenceUser());
        existingCorrespondencePart.setVersion(version);
        correspondencePartRepository.save(existingCorrespondencePart);
        return existingCorrespondencePart;
    }

    @Override
    public CorrespondencePartUnit updateCorrespondencePartUnit(
            String systemId, Long version,
            CorrespondencePartUnit incomingCorrespondencePart) {
        CorrespondencePartUnit existingCorrespondencePart =
                (CorrespondencePartUnit) getCorrespondencePartOrThrow(systemId);

        // Copy all the values you are allowed to copy ....
        // First the values
        existingCorrespondencePart.setName(
                incomingCorrespondencePart.getName());
        existingCorrespondencePart.setOrganisationNumber(
                incomingCorrespondencePart.getOrganisationNumber());
        existingCorrespondencePart.setContactPerson(
                incomingCorrespondencePart.getContactPerson());

        // Then secondary objects
        // Contact information
        updateContactInformation(existingCorrespondencePart.
                        getContactInformation(),
                incomingCorrespondencePart.
                        getContactInformation());
        // Business address
        updateAddress(existingCorrespondencePart.
                        getBusinessAddress().getSimpleAddress(),
                incomingCorrespondencePart.
                        getBusinessAddress().getSimpleAddress());
        // Postal address
        updateAddress(existingCorrespondencePart.
                        getPostalAddress().getSimpleAddress(),
                incomingCorrespondencePart.
                        getPostalAddress().getSimpleAddress());

        // Check the ETAG
        existingCorrespondencePart.setVersion(version);
        correspondencePartRepository.save(existingCorrespondencePart);
        return existingCorrespondencePart;
    }


    @Override
    public CorrespondencePartPerson
    createNewCorrespondencePartPerson(
            CorrespondencePartPerson correspondencePartPerson) {
        return correspondencePartRepository.save(correspondencePartPerson);
    }

    @Override
    public CorrespondencePartUnit createNewCorrespondencePartUnit(
            CorrespondencePartUnit correspondencePartUnit) {
        return correspondencePartRepository.save(correspondencePartUnit);
    }

    @Override
    public CorrespondencePartInternal createNewCorrespondencePartInternal(
            CorrespondencePartInternal correspondencePartInternal) {
        return correspondencePartRepository.save(correspondencePartInternal);
    }

    @Override
    public void deleteCorrespondencePartUnit(@NotNull String code) {
        CorrespondencePartUnit correspondencePartUnit =
                (CorrespondencePartUnit) getCorrespondencePartOrThrow(code);
        correspondencePartRepository.delete(correspondencePartUnit);
    }

    @Override
    public void deleteCorrespondencePartPerson(@NotNull String code) {
        /*
        TODO: Temp disabled!
        CorrespondencePartPerson correspondencePartPerson =
        (CorrespondencePartPerson) getCorrespondencePartOrThrow(code);
        correspondencePartRepository.delete(correspondencePartPerson);
        */
    }

    @Override
    public void deleteCorrespondencePartInternal(@NotNull String code) {
        CorrespondencePartInternal correspondencePartInternal =
                (CorrespondencePartInternal) getCorrespondencePartOrThrow(code);

/*
        // Disassociate the link between Fonds and FondsCreator
        // https://gitlab.com/OsloMet-ABI/nikita-noark5-core/issues/82
        Query q = entityManager.createNativeQuery("DELETE FROM
        fonds_fonds_creator WHERE f_pk_fonds_id  = :id ;");
        q.setParameter("id", fonds.getId());
        q.executeUpdate();
        entityManager.remove(fonds);
        entityManager.flush();
        entityManager.clear();*/
        correspondencePartRepository.delete(correspondencePartInternal);
    }

    // Internal helper methods

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid CorrespondencePart back. If there
     * is no valid CorrespondencePart, an exception is thrown
     *
     * @param correspondencePartSystemId systemId of correspondencePart to
     *                                   retrieve
     * @return the retrieved CorrespondencePart
     */
    protected CorrespondencePart getCorrespondencePartOrThrow(
            @NotNull String correspondencePartSystemId) {
        CorrespondencePart correspondencePart =
                correspondencePartRepository.findBySystemId(
                        correspondencePartSystemId);
        if (correspondencePart == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " CorrespondencePart, " +
                    "using systemId " + correspondencePartSystemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return correspondencePart;
    }

    /**
     * Copy the values you are allowed to copy from the incoming
     * contactInformation object to the existing contactInformation object
     * retrieved from the database.
     *
     * @param existingContactInformation An existing contactInformation object
     *                                   retrieved from the database
     * @param incomingContactInformation Incoming contactInformation object
     */
    public void updateContactInformation(
            IContactInformationEntity existingContactInformation,
            IContactInformationEntity incomingContactInformation) {

        existingContactInformation.setEmailAddress(
                incomingContactInformation.getEmailAddress());
        existingContactInformation.setMobileTelephoneNumber(
                incomingContactInformation.getMobileTelephoneNumber());
        existingContactInformation.setTelephoneNumber(
                incomingContactInformation.getTelephoneNumber());
    }

    /**
     * Copy the values you are allowed to copy from the incoming address object
     * to the existing address object retrieved from the database.
     *
     * @param existingAddress An existing address object retrieved from the
     *                        database
     * @param incomingAddress Incoming address object
     */
    private void updateAddress(SimpleAddress existingAddress,
                               SimpleAddress incomingAddress) {

        existingAddress.setAddressType(incomingAddress.getAddressType());
        existingAddress.setAddressLine1(incomingAddress.getAddressLine1());
        existingAddress.setAddressLine2(incomingAddress.getAddressLine2());
        existingAddress.setAddressLine3(incomingAddress.getAddressLine3());
        existingAddress.setPostalNumber(incomingAddress.getPostalNumber());
        existingAddress.setPostalTown(incomingAddress.getPostalTown());
        existingAddress.setCountryCode(incomingAddress.getCountryCode());
    }
}
