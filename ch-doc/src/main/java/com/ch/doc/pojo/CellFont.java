package com.ch.doc.pojo;

/**
 * decs:字体
 *
 * @author 01370603
 * @date 2019/8/14
 */
public class CellFont {

    private String name;
    private String fontName;
    private String color;
    private boolean bold = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }
}
