package realtech.util;

import java.util.HashMap;
import java.util.Map;

public class MimeTypeDetector {
    private static final Map<String, String> MIME_TYPES = new HashMap<>();

    static {
        MIME_TYPES.put("jpg", "image/jpeg");
        MIME_TYPES.put("jpeg", "image/jpeg");
        MIME_TYPES.put("png", "image/png");
        MIME_TYPES.put("gif", "image/gif");
    }

    public static String getMimeType(String key) {
        String extension = key.substring(key.lastIndexOf('.') + 1).toLowerCase();
        return MIME_TYPES.getOrDefault(extension, "binary/octet-stream");
    }
}
