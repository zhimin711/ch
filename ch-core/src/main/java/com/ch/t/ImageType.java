package com.ch.t;

/**
 * ImageType
 * <p>
 * Created by 80002023 on 2017/4/5.
 */
public enum ImageType {
    JPG("jpg", "image/jpeg"), //image/jpeg
    PNG("png", "image/png"), //image/png
    GIF("gif", "image/gif"), //image/gif
    UNKNOWN("-", "");

    private final String type;
    private final String contentType;

    ImageType(String type, String contentType) {
        this.type = type;
        this.contentType = contentType;
    }

    public String getType() {
        return type;
    }

    public String getFileSuffix() {
        return "." + type;
    }

    public static ImageType fromType(String type) {
        if (type == null) {
            return UNKNOWN;
        }
        type = type.toLowerCase();
        if (type.startsWith("image/")) {
            type = type.replaceFirst("image/", "");
        }
        switch (type) {
            case "jpg":
            case "jpeg":
                return JPG;
            case "png":
                return PNG;
            case "gif":
                return GIF;
            default:
                return UNKNOWN;
        }
    }

    public String getContentType() {
        return contentType;
    }
}
