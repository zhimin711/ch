package com.ch.type;

/**
 * ImageType
 * <p>
 * Created by 80002023 on 2017/4/5.
 */
public enum ImageType {
    JPG("jpg"), //image/jpeg
    PNG("png"), //image/gif
    GIF("gif"), //image/gif
    UNKNOWN("-");

    private final String type;

    ImageType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static ImageType fromType(String type) {
        if (type != null) {
            type = type.toLowerCase();
            switch (type) {
                case "jpg":
                    return JPG;
                case "png":
                    return PNG;
                case "gif":
                    return GIF;
            }
        }
        return UNKNOWN;
    }
}
