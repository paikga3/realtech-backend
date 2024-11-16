package realtech.api.common.model;

import lombok.Data;

@Data
public class PageParams {
    private int page; // 현재 페이지 번호
    private int pageSize; // 페이지당 항목 수
}
