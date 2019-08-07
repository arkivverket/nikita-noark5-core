package nikita.webapp.application;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.webapp.util.serialisers.ApplicationDetailsSerializer;

import javax.validation.constraints.NotNull;
import java.util.List;

@JsonSerialize(using = ApplicationDetailsSerializer.class)
public class ApplicationDetails {

    private String selfHref;
    protected List<ConformityLevel> conformityLevels;
    
    public ApplicationDetails(@NotNull List<ConformityLevel> conformityLevels) {
        this.conformityLevels =  conformityLevels;
    }

    public List<ConformityLevel> getConformityLevels() {
        return conformityLevels;
    }

    public void setConformityLevels(List<ConformityLevel> conformanceLevel) {
        this.conformityLevels = conformanceLevel;
    }

    public String getSelfHref() {
        return selfHref;
    }

    public void setSelfHref(String selfHref) {
        this.selfHref = selfHref;
    }
}
