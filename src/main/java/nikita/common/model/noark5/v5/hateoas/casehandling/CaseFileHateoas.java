package nikita.common.model.noark5.v5.hateoas.casehandling;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.casehandling.CaseFileHateoasSerializer;

import static nikita.common.config.N5ResourceMappings.CASE_FILE;

@JsonSerialize(using = CaseFileHateoasSerializer.class)
public class CaseFileHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public CaseFileHateoas(INoarkEntity entity) {
        super(entity);
    }

    public CaseFileHateoas(NikitaPage page) {
        super(page, CASE_FILE);
    }
}
