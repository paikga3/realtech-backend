package realtech.api.front.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import realtech.api.common.model.PageParams;

@Data
@EqualsAndHashCode(callSuper = false)
public class FetchReservationInquiriesParams extends PageParams {
    private String searchType;
    private String searchKeyword;
}
