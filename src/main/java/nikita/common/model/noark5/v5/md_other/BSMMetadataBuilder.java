package nikita.common.model.noark5.v5.md_other;

public class BSMMetadataBuilder {
    private String name;
    private String type;
    private Boolean outdated;
    private String description;
    private String source;

    public BSMMetadataBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public BSMMetadataBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public BSMMetadataBuilder setOutdated(Boolean outdated) {
        this.outdated = outdated;
        return this;
    }

    public BSMMetadataBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public BSMMetadataBuilder setSource(String source) {
        this.source = source;
        return this;
    }
}
