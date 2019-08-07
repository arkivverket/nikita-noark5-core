package nikita.webapp.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.webapp.hateoas.application.OIDCConfigurationSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@JsonSerialize(using = OIDCConfigurationSerializer.class)
public class OIDCConfiguration {

    @JsonProperty("issuer")
    private String issuer;

    @JsonProperty("authorization_endpoint")
    private String authorizationEndpoint;

    @JsonProperty("token_endpoint")
    private String tokenEndpoint;

    @JsonProperty("userinfo_endpoint")
    private String userinfoEndpoint;

    @JsonProperty("registration_endpoint")
    private String registrationEndpoint;

    @JsonProperty("jwks_uri")
    private String jwksUri;

    @JsonProperty("response_types_supported")
    private List<String> responseTypesSupported = new ArrayList();

    @JsonProperty("response_modes_supported")
    private List<String> responseModesSupported = new ArrayList();

    @JsonProperty("grant_types_supported")
    private List<String> grantTypesSupported = new ArrayList();

    @JsonProperty("subject_types_supported")
    private List<String> subjectTypesSupported = new ArrayList();

    @JsonProperty("id_token_signing_alg_values_supported")
    private List<String> idTokenSigningAlgValuesSupported = new ArrayList();

    @JsonProperty("scopes_supported")
    private List<String> scopesSupported = new ArrayList();

    @JsonProperty("token_endpoint_auth_methods_supported")
    private List<String> tokenEndpointAuthMethodsSupported = new ArrayList();

    @JsonProperty("claims_supported")
    private List<String> claimsSupported = new ArrayList();

    @JsonProperty("introspection_endpoint")
    private String introspectionEndpoint;

    @JsonProperty("introspection_endpoint_auth_methods_supported")
    private List<String> introspectionEndpointAuthMethodsSupported = new ArrayList();

    @JsonProperty("revocation_endpoint")
    private String revocationEndpoint;

    @JsonProperty("revocation_endpoint_auth_methods_supported")
    private List<String> revocationEndpointAuthMethodsSupported = new ArrayList();

    @JsonProperty("end_session_endpoint")
    private String endSessionEndpoint;

    @JsonProperty("request_parameter_supported")
    private Boolean requestParameterSupported;

    @JsonProperty("request_object_signing_alg_values_supported")
    private List<String> requestObjectSigningAlgValuesSupported = new ArrayList();

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getAuthorizationEndpoint() {
        return authorizationEndpoint;
    }

    public void setAuthorizationEndpoint(String authorizationEndpoint) {
        this.authorizationEndpoint = authorizationEndpoint;
    }

    public String getTokenEndpoint() {
        return tokenEndpoint;
    }

    public void setTokenEndpoint(String tokenEndpoint) {
        this.tokenEndpoint = tokenEndpoint;
    }

    public String getUserinfoEndpoint() {
        return userinfoEndpoint;
    }

    public void setUserinfoEndpoint(String userinfoEndpoint) {
        this.userinfoEndpoint = userinfoEndpoint;
    }

    public String getRegistrationEndpoint() {
        return registrationEndpoint;
    }

    public void setRegistrationEndpoint(String registrationEndpoint) {
        this.registrationEndpoint = registrationEndpoint;
    }

    public String getJwksUri() {
        return jwksUri;
    }

    public void setJwksUri(String jwksUri) {
        this.jwksUri = jwksUri;
    }

    public List<String> getResponseTypesSupported() {
        return responseTypesSupported;
    }

    public void setResponseTypesSupported(List<String> responseTypesSupported) {
        this.responseTypesSupported = responseTypesSupported;
    }

    public List<String> getResponseModesSupported() {
        return responseModesSupported;
    }

    public void setResponseModesSupported(List<String> responseModesSupported) {
        this.responseModesSupported = responseModesSupported;
    }

    public List<String> getGrantTypesSupported() {
        return grantTypesSupported;
    }

    public void setGrantTypesSupported(List<String> grantTypesSupported) {
        this.grantTypesSupported = grantTypesSupported;
    }

    public List<String> getSubjectTypesSupported() {
        return subjectTypesSupported;
    }

    public void setSubjectTypesSupported(List<String> subjectTypesSupported) {
        this.subjectTypesSupported = subjectTypesSupported;
    }

    public List<String> getIdTokenSigningAlgValuesSupported() {
        return idTokenSigningAlgValuesSupported;
    }

    public void setIdTokenSigningAlgValuesSupported(List<String> idTokenSigningAlgValuesSupported) {
        this.idTokenSigningAlgValuesSupported = idTokenSigningAlgValuesSupported;
    }

    public List<String> getScopesSupported() {
        return scopesSupported;
    }

    public void setScopesSupported(List<String> scopesSupported) {
        this.scopesSupported = scopesSupported;
    }

    public List<String> getTokenEndpointAuthMethodsSupported() {
        return tokenEndpointAuthMethodsSupported;
    }

    public void setTokenEndpointAuthMethodsSupported(List<String> tokenEndpointAuthMethodsSupported) {
        this.tokenEndpointAuthMethodsSupported = tokenEndpointAuthMethodsSupported;
    }

    public List<String> getClaimsSupported() {
        return claimsSupported;
    }

    public void setClaimsSupported(List<String> claimsSupported) {
        this.claimsSupported = claimsSupported;
    }

    public String getIntrospectionEndpoint() {
        return introspectionEndpoint;
    }

    public void setIntrospectionEndpoint(String introspectionEndpoint) {
        this.introspectionEndpoint = introspectionEndpoint;
    }

    public List<String> getIntrospectionEndpointAuthMethodsSupported() {
        return introspectionEndpointAuthMethodsSupported;
    }

    public void setIntrospectionEndpointAuthMethodsSupported(List<String> introspectionEndpointAuthMethodsSupported) {
        this.introspectionEndpointAuthMethodsSupported = introspectionEndpointAuthMethodsSupported;
    }

    public String getRevocationEndpoint() {
        return revocationEndpoint;
    }

    public void setRevocationEndpoint(String revocationEndpoint) {
        this.revocationEndpoint = revocationEndpoint;
    }

    public List<String> getRevocationEndpointAuthMethodsSupported() {
        return revocationEndpointAuthMethodsSupported;
    }

    public void setRevocationEndpointAuthMethodsSupported(List<String> revocationEndpointAuthMethodsSupported) {
        this.revocationEndpointAuthMethodsSupported = revocationEndpointAuthMethodsSupported;
    }

    public String getEndSessionEndpoint() {
        return endSessionEndpoint;
    }

    public void setEndSessionEndpoint(String endSessionEndpoint) {
        this.endSessionEndpoint = endSessionEndpoint;
    }

    public Boolean getRequestParameterSupported() {
        return requestParameterSupported;
    }

    public void setRequestParameterSupported(Boolean requestParameterSupported) {
        this.requestParameterSupported = requestParameterSupported;
    }

    public List<String> getRequestObjectSigningAlgValuesSupported() {
        return requestObjectSigningAlgValuesSupported;
    }

    public void setRequestObjectSigningAlgValuesSupported(List<String> requestObjectSigningAlgValuesSupported) {
        this.requestObjectSigningAlgValuesSupported = requestObjectSigningAlgValuesSupported;
    }
}
    
