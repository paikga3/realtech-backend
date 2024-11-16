package realtech.api.front.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class GalleryPost {
    private int id;
    private String title;
    private String thumbnailUrl;
}
