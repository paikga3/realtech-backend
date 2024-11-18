package realtech.api.admin.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import realtech.api.common.model.PageParams;

@Data
@EqualsAndHashCode(callSuper = false)
public class FetchVisitorAggregationParams extends PageParams {
    private String startDate;
    private String endDate;
}
