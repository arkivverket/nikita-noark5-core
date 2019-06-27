package nikita.webapp.odata.model;

public class RefBuilder {
    private String fromEntity;
    private String toEntity;
    private String toSystemId;
    private String fromSystemId;
    private String entity;

    public RefBuilder setFromEntity(String fromEntity) {
        this.fromEntity = fromEntity;
        return this;
    }

    public RefBuilder setToEntity(String toEntity) {
        this.toEntity = toEntity;
        return this;
    }

    public RefBuilder setToSystemId(String toSystemId) {
        this.toSystemId = toSystemId;
        return this;
    }

    public RefBuilder setFromSystemId(String fromSystemId) {
        this.fromSystemId = fromSystemId;
        return this;
    }

    public RefBuilder setEntity(String entity) {
        this.entity = entity;
        return this;
    }

    public Ref createRef() {
        return new Ref(fromEntity, toEntity, toSystemId, fromSystemId, entity);
    }
}
