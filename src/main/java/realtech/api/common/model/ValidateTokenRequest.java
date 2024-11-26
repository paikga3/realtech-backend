package realtech.api.common.model;

import lombok.Data;

@Data
public class ValidateTokenRequest {
    private String token;
    private String boardType;
    private int postId;
}
