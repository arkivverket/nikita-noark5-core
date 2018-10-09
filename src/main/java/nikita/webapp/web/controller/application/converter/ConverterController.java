package nikita.webapp.web.controller.application.converter;

import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.noark5.v4.hateoas.DocumentObjectHateoas;
import nikita.webapp.service.interfaces.IDocumentObjectService;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;


/**
 * ConverterController
 * <p>
 * This code is to test out the ability to handle conversions via LibreOffice
 * and will be used for teaching purposes. It's mainly a proof of concept
 * implementation and it's expected we swap it with a queue of sorts later.
 */
//@RestController
//@RequestMapping(value = "application/converter/")
public class ConverterController {

    private IDocumentObjectService documentObjectService;

    public ConverterController(IDocumentObjectService documentObjectService) {
        this.documentObjectService = documentObjectService;
    }

    @ApiOperation(value = "Uploads a file and converts it to PDF",
            response = DocumentObjectHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "File uploaded successfully",
                    response = String.class),
            @ApiResponse(code = 401, message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403, message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500, message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Timed
    @RequestMapping(value = "convert",
            method = RequestMethod.POST,
            headers = "Accept=*/*",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public HttpEntity<byte[]> handleFileUpload(
            HttpServletRequest request) {
/*
commenting this out as work on documentobject leaves this not running
This only a temporary class while testing something anyway.
        try {
            String filename = "returned.pdf";
  //          byte[] documentBody = documentObjectService.convertDocumentToPDF(
   //                 request.getInputStream());

            //byte[] documentBody = new byte[(int)Files.size(file)];
            //IOUtils.readFully(resource.getInputStream(),
            //        documentBody);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Content-Type", "application/pdf");
            String header = "Content-Disposition";
            String value = "\"attachment; filename=\"" + filename + "\"";
            responseHeaders.add(header, value);

            return new HttpEntity<>(documentBody, responseHeaders);
        } catch (Exception e) {
            throw new StorageException(e.toString());
        } */
        return null;
    }


}
