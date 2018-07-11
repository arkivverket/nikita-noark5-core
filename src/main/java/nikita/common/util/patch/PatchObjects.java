package nikita.common.util.patch;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

@JsonDeserialize(using = PatchObjectsDeserializer.class)
public class PatchObjects {

    private List<PatchObject> patchObjects = new ArrayList <>();

    public void add(PatchObject patchObject) {
        patchObjects.add(patchObject);
    }

    public List<PatchObject> getPatchObjects() {
        return patchObjects;
    }
}
