package realtech.api.front.model;

import java.util.List;

import lombok.Data;

@Data
public class CommentListResponse {
    private List<CommentDetail> comments;
    private int totalComments;
}
