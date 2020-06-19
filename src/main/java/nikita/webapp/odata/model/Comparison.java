package nikita.webapp.odata.model;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Comparison {

    Object left;
    List<String> leftMethods = new ArrayList();
    StringJoiner concatValues = new StringJoiner(",", "concat(", ")");
    String method = "";
    String comparator = "";
    Object right;
    List<String> rightMethods = new ArrayList();
    Boolean processConcat = false;

    public Object getLeft() {
        return left;
    }

    public void setLeft(Object left) {
        if (processConcat) {
            concatValues.add(left.toString());
        } else if (null == this.left) {
            this.left = left;
        }
    }

    public List getLeftMethods() {
        return leftMethods;
    }

    public void addLeftMethod(String leftMethod) {
        this.leftMethods.add(leftMethod);
    }

    public String getComparator() {
        return comparator;
    }

    public void setComparator(String comparator) {
        this.comparator = comparator;
    }

    public void setComparator(String comparator, String method) {
        this.method = method;
        this.comparator = comparator;
    }

    public Object getRight() {
        return right;
    }

    public void setRight(Object right) {
        if (null == this.right) {
            this.right = right;
        }
    }

    public List getRightMethods() {
        return rightMethods;
    }

    public void addRightMethod(String rightMethod) {
        this.rightMethods.add(rightMethod);
    }

    public void addConcatValue(String concatValue) {
        concatValues.add(concatValue);
    }

    public StringJoiner getConcatValues() {
        return concatValues;
    }

    public void startConcat() {
        processConcat = true;
    }

    public void endConcat() {
        processConcat = false;
    }

    public Boolean getProcessConcat() {
        return processConcat;
    }
}
