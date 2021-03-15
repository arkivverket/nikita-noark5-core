package nikita.common.model.noark5.v5.interfaces;

import nikita.common.model.noark5.v5.secondary.Keyword;

import java.util.List;

public interface IKeyword {
    List<Keyword> getReferenceKeyword();

    void setReferenceKeyword(List<Keyword> keywords);

    void addKeyword(Keyword keyword);

    void removeKeyword(Keyword keyword);
}
