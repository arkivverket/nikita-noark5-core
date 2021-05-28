package nikita.common.model.nikita;

import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;

import java.util.ArrayList;
import java.util.List;

public class NikitaPage {

    private List<INoarkEntity> entityList = new ArrayList<>();
    private long count = 0;
    private int top;
    private int skip;
    private boolean isEmpty;

    public NikitaPage(List<INoarkEntity> entityList, long count,
                      int top, int skip) {
        this.entityList = entityList;
        this.count = count;
        this.top = top;
        this.skip = skip;
    }

    public NikitaPage(List<INoarkEntity> entityList, long count) {
        this.entityList = entityList;
        this.count = count;
    }

    public NikitaPage(List<INoarkEntity> entityList) {
        this.entityList = entityList;
        this.count = entityList.size();
    }

    public NikitaPage() {
        this.isEmpty = true;
    }

    public List<INoarkEntity> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<INoarkEntity> entityList) {
        this.entityList = entityList;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public long getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
