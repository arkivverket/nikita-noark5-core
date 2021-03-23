package utils;

import nikita.common.model.noark5.v5.casehandling.secondary.*;
import nikita.common.model.noark5.v5.metadata.CorrespondencePartType;
import nikita.common.model.noark5.v5.secondary.PartPerson;

import static nikita.common.config.MetadataConstants.CORRESPONDENCE_PART_CODE_EA;
import static nikita.common.config.MetadataConstants.CORRESPONDENCE_PART_DESCRIPTION_EA;
import static nikita.common.config.N5ResourceMappings.*;
import static utils.TestConstants.*;


public final class CorrespondencePartCreator {

    /**
     * Create a default CorrespondencePartUnit for testing purposes
     *
     * @return a CorrespondencePartUnit with all values set
     */
    public static CorrespondencePartUnit createCorrespondencePartUnit() {
        CorrespondencePartUnit correspondencePart =
                new CorrespondencePartUnit();
        correspondencePart.setName(NAME_TEST);
        correspondencePart.setContactPerson(CONTACT_NAME_TEST);
        correspondencePart.setUnitIdentifier(UNIT_NUMBER_TEST);
        correspondencePart.setCorrespondencePartType(
                createCorrespondencePartType());
        correspondencePart.setPostalAddress(createPostalAddress());
        correspondencePart.setBusinessAddress(createBusinessAddress());
        correspondencePart.setContactInformation(createContactInformation());
        return correspondencePart;
    }

    /**
     * Create a default CorrespondencePartPerson for testing purposes
     *
     * @return a CorrespondencePartPerson with all values set
     */
    public static CorrespondencePartPerson
    createCorrespondencePartPerson() {
        CorrespondencePartPerson correspondencePart =
                new CorrespondencePartPerson();
        correspondencePart.setName(NAME_TEST);
        correspondencePart.setdNumber(D_NUMBER_TEST);
        correspondencePart.setSocialSecurityNumber(SOCIAL_SECURITY_NUMBER_TEST);
        correspondencePart.setCorrespondencePartType(
                createCorrespondencePartType());
        correspondencePart.setPostalAddress(createPostalAddress());
        correspondencePart.setResidingAddress(createResidingAddress());
        correspondencePart.setContactInformation(createContactInformation());
        return correspondencePart;
    }

    /**
     * Create a default CorrespondencePartType for testing purposes
     *
     * @return a CorrespondencePartType with both values set
     */
    public static CorrespondencePartType createCorrespondencePartType() {
        CorrespondencePartType correspondencePartType =
                new CorrespondencePartType();
        correspondencePartType.setCode(CORRESPONDENCE_PART_CODE_EA);
        correspondencePartType.setCodeName(CORRESPONDENCE_PART_DESCRIPTION_EA);
        return correspondencePartType;
    }

    /**
     * Create a default ContactInformation for testing purposes
     *
     * @return a ContactInformation with all values set
     */
    public static ContactInformation createContactInformation() {
        ContactInformation contactInformation = new ContactInformation();
        contactInformation.setEmailAddress(EMAIL_ADDRESS_TEST);
        contactInformation.setMobileTelephoneNumber(MOBILE_NUMBER_TEST);
        contactInformation.setTelephoneNumber(TELEPHONE_NUMBER_TEST);
        PartPerson person = new PartPerson();
        contactInformation.setPartPerson(person);
        return contactInformation;
    }

    /**
     * Create a default ResidingAddress for testing purposes
     *
     * @return a ResidingAddress with all values set
     */
    public static ResidingAddress createResidingAddress() {
        ResidingAddress residingAddress = new ResidingAddress();
        SimpleAddress simpleAddress = new SimpleAddress();
        simpleAddress.setAddressLine1(RESIDING_ADDRESS_LINE_1_TEST);
        simpleAddress.setAddressLine2(RESIDING_ADDRESS_LINE_2_TEST);
        simpleAddress.setAddressLine3(RESIDING_ADDRESS_LINE_3_TEST);
        PostalNumber postalNumber = new PostalNumber();
        postalNumber.setPostalNumber(RESIDING_POSTAL_CODE_TEST);
        simpleAddress.setPostalNumber(postalNumber);
        simpleAddress.setPostalTown(RESIDING_POSTAL_TOWN_TEST);
        simpleAddress.setCountryCode(COUNTRY_CODE_TEST);
        simpleAddress.setAddressType(RESIDING_ADDRESS);
        residingAddress.setSimpleAddress(simpleAddress);
        return residingAddress;
    }

    /**
     * Create a default BusinessAddress for testing purposes
     *
     * @return a BusinessAddress with all values set
     */
    public static BusinessAddress createBusinessAddress() {
        BusinessAddress businessAddress = new BusinessAddress();
        SimpleAddress simpleAddress = new SimpleAddress();
        simpleAddress.setAddressLine1(BUSINESS_ADDRESS_LINE_1_TEST);
        simpleAddress.setAddressLine2(BUSINESS_ADDRESS_LINE_2_TEST);
        simpleAddress.setAddressLine3(BUSINESS_ADDRESS_LINE_3_TEST);
        PostalNumber postalNumber = new PostalNumber();
        postalNumber.setPostalNumber(BUSINESS_POSTAL_CODE_TEST);
        simpleAddress.setPostalNumber(postalNumber);
        simpleAddress.setPostalTown(BUSINESS_POSTAL_TOWN_TEST);
        simpleAddress.setCountryCode(COUNTRY_CODE_TEST);
        simpleAddress.setAddressType(BUSINESS_ADDRESS);
        businessAddress.setSimpleAddress(simpleAddress);
        return businessAddress;
    }

    /**
     * Create a default PostalAddress for testing purposes
     *
     * @return a PostalAddress with all values set
     */
    public static PostalAddress createPostalAddress() {
        PostalAddress postalAddress = new PostalAddress();
        SimpleAddress simpleAddress = new SimpleAddress();
        simpleAddress.setAddressLine1(POSTAL_ADDRESS_LINE_1_TEST);
        simpleAddress.setAddressLine2(POSTAL_ADDRESS_LINE_2_TEST);
        simpleAddress.setAddressLine3(POSTAL_ADDRESS_LINE_3_TEST);
        PostalNumber postalNumber = new PostalNumber();
        postalNumber.setPostalNumber(POSTAL_POSTAL_CODE_TEST);
        simpleAddress.setPostalNumber(postalNumber);
        simpleAddress.setPostalTown(POSTAL_POSTAL_TOWN_TEST);
        simpleAddress.setAddressType(POSTAL_ADDRESS);
        simpleAddress.setCountryCode(COUNTRY_CODE_TEST);
        postalAddress.setSimpleAddress(simpleAddress);
        return postalAddress;
    }
}
