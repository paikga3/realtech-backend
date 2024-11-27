package realtech.api.front.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CreateCommentParams extends BaseCommentRequest {
    private String authorName;
    private String content;
    private int parentCommentId;
}
