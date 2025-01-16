package com.example.graphicsmaker.create;

public class StickerInfo {
    private int id;
    private int primaryColor;
    private String resID;
    private int secondaryColor;

    public StickerInfo(int id, String resID, int primaryColor, int secondaryColor) {
        this.id = id;
        this.resID = resID;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setResID(String resID) {
        this.resID = resID;
    }

    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
    }

    public void setSecondaryColor(int secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public String getResID() {
        return this.resID;
    }

    public int getPrimaryColor() {
        return this.primaryColor;
    }

    public int getSecondaryColor() {
        return this.secondaryColor;
    }
}
