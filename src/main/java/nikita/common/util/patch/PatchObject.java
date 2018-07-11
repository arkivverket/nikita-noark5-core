package nikita.common.util.patch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tsodring on 29/03/18.
 * <p>
 * Testing code on how to handle PATCH
 * <p>
 */
public class PatchObject {

    private String op;
    private String path;
    private String value;
    private List<String> from = new ArrayList<>();

    public PatchObject() {
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<String> getFrom() {
        return from;
    }

    public void setFrom(List<String> from) {
        this.from = from;
    }

    @Override
    public String toString() {
        return "PatchObject{" +
                "op='" + op + '\'' +
                ", path='" + path + '\'' +
                ", value='" + value + '\'' +
                ", from=" + from.toString() +
                '}';
    }
}
