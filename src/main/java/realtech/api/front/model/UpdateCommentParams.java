package realtech.api.front.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UpdateCommentParams extends BaseCommentRequest {
    private int commentId;
    private String content;
}
