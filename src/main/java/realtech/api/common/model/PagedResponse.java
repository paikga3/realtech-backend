package realtech.api.common.model;

import java.util.List;

import lombok.Data;

@Data
public class PagedResponse<T> {
    private int total; // 전체 게시물 수
    private int currentPage; // 현재 페이지
    private int pageSize; // 페이지 크기
    private List<T> data; // 응답 데이터 리스트
}
