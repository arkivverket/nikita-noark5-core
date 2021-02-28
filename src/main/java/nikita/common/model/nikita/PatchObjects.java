package nikita.common.model.nikita;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.util.patch.PatchObjectsDeserializer;

import java.util.ArrayList;
import java.util.List;

@JsonDeserialize(using = PatchObjectsDeserializer.class)
public class PatchObjects {

    private List<PatchObject> patchObjects = new ArrayList<>();

    public PatchObjects() {
    }

    public PatchObjects(PatchObject patchObject) {
        patchObjects.add(patchObject);
    }

    public PatchObjects(List<PatchObject> patchObjects) {
        this.patchObjects.addAll(patchObjects);
    }

    public void add(PatchObject patchObject) {
        patchObjects.add(patchObject);
    }

    public List<PatchObject> getPatchObjects() {
        return patchObjects;
    }
}
