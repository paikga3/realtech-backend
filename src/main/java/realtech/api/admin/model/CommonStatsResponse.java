package realtech.api.admin.model;

import java.util.List;

import lombok.Data;

@Data
public class CommonStatsResponse {
    private int total;
    private List<CommonStats> data;
}
