package nikita.common.util.patch;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class TempPatch {


    private EntityManager em;

    public TempPatch(
            EntityManager em) {
        this.em = em;
    }

    public void updateProjectRequirement(
            PatchObjects patchObjects, Long requirementNumber)
            throws Exception {

        for (PatchObject patchObject : patchObjects.getPatchObjects()) {
            if ("replace".equalsIgnoreCase(patchObject.getOp())
                    && null != patchObject.getPath()
                    && null != patchObject.getValue()) {
                String path = patchObject.getPath();
                if ("/".equals(path.substring(0, 1))) {
                    path = path.substring(1);
                }

                String updateQuery = "update ProjectRequirement set "
                        + path + " = :value where id = :id";
                Query query = em.createQuery(updateQuery);
                query.setParameter("value", patchObject.getValue());
                query.setParameter("id", requirementNumber);
                query.executeUpdate();
            } else {
                throw new Exception("Cannot handle this PatchObject " +
                        patchObject.toString());
            }
        }
    }
}
