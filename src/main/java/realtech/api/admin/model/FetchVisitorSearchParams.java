package realtech.api.admin.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import realtech.api.common.model.PageParams;

@Data
@EqualsAndHashCode(callSuper = false)
public class FetchVisitorSearchParams extends PageParams {
    private String searchType; // ip | referer 검색유형
    private String searchKeyword;
}
