package nikita.common.model.noark5.v5.interfaces;

import nikita.common.model.noark5.v5.secondary.Keyword;

import java.util.Set;

public interface IKeyword {
    Set<Keyword> getReferenceKeyword();

    void addKeyword(Keyword keyword);
}
