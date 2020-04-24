package nikita.webapp.web.soapendpoints.geointegrasjon;

import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.metadata.CaseStatus;
import nikita.common.model.noark5.v5.metadata.DocumentMedium;
import nikita.common.rep.*;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import nikita.webapp.service.interfaces.ISeriesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.UUID;

@Endpoint
public class GIArkivOppdatering {

    private static final Logger logger =
            LoggerFactory.getLogger(GIArkivOppdatering.class);
    private static final String NAMESPACE_URI =
            "http://rep.geointegrasjon.no/Arkiv/Oppdatering/xml.wsdl/2012.01.31";
    private final ISeriesService seriesService;

    public GIArkivOppdatering(ISeriesService seriesService) {
        this.seriesService = seriesService;
        logger.debug("GIArkivOppdatering construction");
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "NySaksmappe")
    @ResponsePayload
    @SuppressWarnings("unused")
    public NySaksmappeResponse createCaseFile(
            @RequestPayload NySaksmappe incomingCaseFile) {
        return toNySaksmappeResponse(seriesService
                .createCaseFileAssociatedWithSeries(
                        getSeriesUUIDOrThrow(incomingCaseFile),
                        getSaksmappeFromNySaksmappe(incomingCaseFile)));
    }

    private CaseFile getSaksmappeFromNySaksmappe(NySaksmappe incomingCaseFile) {
        Saksmappe saksmappe = getSaksmappe(incomingCaseFile);
        CaseFile caseFile = new CaseFile();
        caseFile.setTitle(saksmappe.getTittel());
        caseFile.setPublicTitle(saksmappe.getOffentligTittel());
        caseFile.setCaseStatus(getCaseStatus(saksmappe));
        caseFile.setDocumentMedium(getDocumentMedium(saksmappe));
        return caseFile;
    }

    private NySaksmappeResponse toNySaksmappeResponse(CaseFile caseFile) {
        NySaksmappeResponse saksmappeResponse = new NySaksmappeResponse();
        Saksmappe saksmappe = new Saksmappe();
        saksmappe.setSystemID(caseFile.getSystemId());
        saksmappe.setTittel(caseFile.getTitle());
        saksmappe.setOffentligTittel(caseFile.getPublicTitle());
        saksmappe.setAdministrativEnhet(caseFile.getRecordsManagementUnit());
        saksmappe.setSaksansvarlig(caseFile.getCaseResponsible());
        saksmappe.setSaksstatus(getSaksstatus(caseFile));
        saksmappe.setDokumentmedium(getDokumentMedium(caseFile));
        saksmappeResponse.setReturn(saksmappe);
        return saksmappeResponse;
    }

    private Saksmappe getSaksmappe(NySaksmappe incomingCaseFile) {
        if (null != incomingCaseFile) {
            return incomingCaseFile.getMappe();
        } else {
            throw new NikitaMalformedInputDataException(
                    "Problem with incoming payload. Not " +
                            "possible to find a UUID for Series");
        }
    }

    private CaseStatus getCaseStatus(Saksmappe saksmappe) {
        CaseStatus caseStatus = new CaseStatus();
        Saksstatus saksstatus = saksmappe.getSaksstatus();
        if (null != saksstatus) {
            caseStatus.setCode(saksstatus.getKodeverdi());
            caseStatus.setCodeName(saksstatus.getKodebeskrivelse());
        }
        return caseStatus;
    }

    private Dokumentmedium getDokumentMedium(CaseFile caseFile) {
        Dokumentmedium dokumentmedium = new Dokumentmedium();
        DocumentMedium documentMedium = caseFile.getDocumentMedium();
        if (null != documentMedium) {
            dokumentmedium.setKodeverdi(documentMedium.getCode());
            dokumentmedium.setKodebeskrivelse(documentMedium.getCodeName());
        }
        return dokumentmedium;
    }

    private Saksstatus getSaksstatus(CaseFile caseFile) {
        Saksstatus saksstatus = new Saksstatus();
        CaseStatus caseStatus = caseFile.getCaseStatus();
        if (null != caseStatus) {
            saksstatus.setKodeverdi(caseStatus.getCode());
            saksstatus.setKodebeskrivelse(caseStatus.getCodeName());
        }
        return saksstatus;
    }

    private DocumentMedium getDocumentMedium(Saksmappe saksmappe) {
        Dokumentmedium dokumentmedium = saksmappe.getDokumentmedium();
        DocumentMedium documentMedium = new DocumentMedium();
        if (null != dokumentmedium) {
            documentMedium.setCode(dokumentmedium.getKodeverdi());
            documentMedium.setCodeName(dokumentmedium.getKodebeskrivelse());
        }
        return documentMedium;
    }

    private String getSeriesUUIDOrThrow(NySaksmappe incomingCaseFile) {
        Saksmappe caseFile = getSaksmappe(incomingCaseFile);
        if (null != caseFile) {
            Arkivdel refSeries = caseFile.getReferanseArkivdel();
            if (null != refSeries) {
                String seriesId = refSeries.getKodeverdi();
                if (null != seriesId) {
                    try {
                        // Allows us to test that it is in fact a valid UUID
                        // or throw an exception
                        return UUID.fromString(seriesId).toString();
                    } catch (IllegalArgumentException exception) {
                        throw new NikitaMalformedInputDataException(
                                "Problem with incoming payload. Not " +
                                        "possible to find a UUID for Series");
                    }
                }
            }
        }
        throw new NikitaMalformedInputDataException(
                "Problem with incoming payload. Missing " +
                        "referanseArkivdel with UUID value");
    }
}
