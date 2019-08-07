package nikita.webapp.hateoas.application;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import nikita.webapp.application.OIDCConfiguration;

import java.io.IOException;
import java.util.List;

import static nikita.common.config.OIDCConstants.*;

public class OIDCConfigurationSerializer
        extends StdSerializer<OIDCConfiguration> {

    public OIDCConfigurationSerializer() {
        super(OIDCConfiguration.class);
    }

    @Override
    public void serialize(
            OIDCConfiguration configuration, JsonGenerator jgen,
            SerializerProvider provider)
            throws IOException {
        jgen.writeStartObject();
        writeString(configuration.getIssuer(), OIDC_ISSUER, jgen);
        writeString(configuration.getAuthorizationEndpoint(),
                OIDC_AUTHORIZATION_ENDPOINT, jgen);
        writeString(configuration.getTokenEndpoint(),
                OIDC_TOKEN_ENDPOINT, jgen);
        writeString(configuration.getUserinfoEndpoint(),
                OIDC_USERINFO_ENDPOINT, jgen);
        writeString(configuration.getRegistrationEndpoint(),
                OIDC_REGISTRATION_ENDPOINT, jgen);
        writeString(configuration.getJwksUri(), OIDC_JWKS_URI, jgen);
        writeArray(configuration.getResponseModesSupported(),
                OIDC_RESPONSE_TYPES_SUPPORTED, jgen);
        writeArray(configuration.getResponseModesSupported(),
                OIDC_RESPONSE_MODES_SUPPORTED, jgen);
        writeArray(configuration.getGrantTypesSupported(),
                OIDC_GRANT_TYPES_SUPPORTED, jgen);
        writeArray(configuration.getSubjectTypesSupported(),
                OIDC_SUBJECT_TYPES_SUPPORTED, jgen);
        writeArray(configuration.getIdTokenSigningAlgValuesSupported(),
                OIDC_ID_TOKEN_SIGNING_ALG_VALUES_SUPPORTED, jgen);
        writeArray(configuration.getScopesSupported(),
                OIDC_SCOPES_SUPPORTED, jgen);
        writeArray(configuration.getTokenEndpointAuthMethodsSupported(),
                OIDC_TOKEN_ENDPOINT_AUTH_METHODS_SUPPORTED, jgen);
        writeArray(configuration.getClaimsSupported(),
                OIDC_CLAIMS_SUPPORTED, jgen);
        writeString(configuration.getIntrospectionEndpoint(),
                OIDC_INTROSPECTION_ENDPOINT, jgen);
        writeArray(configuration.getIntrospectionEndpointAuthMethodsSupported(),
                OIDC_INTROSPECTION_ENDPOINT_AUTH_METHODS_SUPPORTED, jgen);
        writeString(configuration.getRevocationEndpoint(),
                OIDC_REVOCATION_ENDPOINT, jgen);
        writeString(configuration.getEndSessionEndpoint(),
                OIDC_END_SESSION_ENDPOINT, jgen);
        writeBoolean(configuration.getRequestParameterSupported(),
                OIDC_REQUEST_PARAMETER_SUPPORTED, jgen);
        writeArray(configuration.getRequestObjectSigningAlgValuesSupported(),
                OIDC_REQUEST_OBJECT_SIGNING_ALG_VALUES_SUPPORTED, jgen);
        jgen.writeEndObject();
    }

    private void writeString(String value, String fieldName,
                             JsonGenerator jgen) throws IOException {
        if (null != value) {
            jgen.writeStringField(fieldName, value);
        }
    }

    private void writeBoolean(Boolean value, String fieldName,
                              JsonGenerator jgen) throws IOException {
        if (null != value) {
            jgen.writeBooleanField(fieldName, value);
        }
    }

    private void writeArray(List<String> array, String arrayFieldName,
                            JsonGenerator jgen) throws IOException {
        if (array.size() > 0) {
            jgen.writeArrayFieldStart(arrayFieldName);
            for (String value : array) {
                jgen.writeString(value);
            }
            jgen.writeEndArray();
        }
    }
}
