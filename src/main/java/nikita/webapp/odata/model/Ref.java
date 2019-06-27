package nikita.webapp.odata.model;

public class Ref {

    private String fromEntity;
    private String toEntity;
    private String toSystemId;
    private String fromSystemId;
    private String entity;

    Ref(String fromEntity, String toEntity, String toSystemId, String fromSystemId, String entity) {
        this.fromEntity = fromEntity;
        this.toEntity = toEntity;
        this.toSystemId = toSystemId;
        this.fromSystemId = fromSystemId;
        this.entity = entity;
    }

    public String getFromEntity() {
        return fromEntity;
    }

    public String getToEntity() {
        return toEntity;
    }

    public String getToSystemId() {
        return toSystemId;
    }

    public String getFromSystemId() {
        return fromSystemId;
    }

    public String getEntity() {
        return entity;
    }

    @Override
    public String toString() {
        return "Ref{" +
                "fromEntity='" + fromEntity + '\'' +
                ", toEntity='" + toEntity + '\'' +
                ", toSystemId='" + toSystemId + '\'' +
                ", fromSystemId='" + fromSystemId + '\'' +
                ", entity='" + entity + '\'' +
                '}';
    }
}
