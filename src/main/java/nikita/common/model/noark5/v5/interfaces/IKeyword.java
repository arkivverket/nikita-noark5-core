package nikita.common.model.noark5.v5.interfaces;

import nikita.common.model.noark5.v5.secondary.Keyword;

import java.util.List;

/**
 * Created by tsodring on 12/7/16.
 */

public interface IKeyword {
    List<Keyword> getReferenceKeyword();

    void setReferenceKeyword(List<Keyword> keywords);

    void addReferenceKeyword(Keyword keyword);
}
