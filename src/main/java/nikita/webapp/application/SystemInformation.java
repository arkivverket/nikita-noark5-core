package nikita.webapp.application;

import com.fasterxml.jackson.annotation.JsonProperty;

import static nikita.common.config.SystemConstants.*;

public class SystemInformation {

    @JsonProperty(value = VENDOR)
    private String vendor;

    @JsonProperty(value = VERSION)
    private String version;

    @JsonProperty(value = VERSION_DATE)
    private String versionDate;

    @JsonProperty(value = PRODUCT)
    private String product;

    @JsonProperty(value = PROTOCOL_VERSION)
    private String protocolVersion;

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersionDate() {
        return versionDate;
    }

    public void setVersionDate(String versionDate) {
        this.versionDate = versionDate;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    @Override
    public String toString() {
        return "SystemInformation{" +
                "vendor='" + vendor + '\'' +
                ", product='" + product + '\'' +
                ", version='" + version + '\'' +
                ", versionDate='" + versionDate + '\'' +
                ", protocolVersion='" + protocolVersion + '\'' +
                '}';
    }
}
