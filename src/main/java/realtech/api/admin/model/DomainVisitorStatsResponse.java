package realtech.api.admin.model;

import java.util.List;

import lombok.Data;

@Data
public class DomainVisitorStatsResponse {
    private List<DomainVisitorStats> data;
    private int total;
}
