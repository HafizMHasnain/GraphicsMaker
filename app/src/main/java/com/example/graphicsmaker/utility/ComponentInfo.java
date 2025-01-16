package com.example.graphicsmaker.utility;

public class ComponentInfo {
    private int COMP_ID;
    private int HEIGHT;
    private int ORDER;
    private float POS_X;
    private float POS_Y;
    private String RES_ID;
    private float ROTATION;
    private int STC_COLOR;
    private int STC_OPACITY;
    private int TEMPLATE_ID;
    private String TYPE = "";
    private int WIDTH;
    private float Y_ROTATION;

    public ComponentInfo(int TEMPLATE_ID, float POS_X, float POS_Y, int WIDTH, int HEIGHT, float ROTATION, float Y_ROTATION, String RES_ID, String TYPE, int ORDER, int STC_COLOR, int STC_OPACITY) {
        this.TEMPLATE_ID = TEMPLATE_ID;
        this.POS_X = POS_X;
        this.POS_Y = POS_Y;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.ROTATION = ROTATION;
        this.Y_ROTATION = Y_ROTATION;
        this.RES_ID = RES_ID;
        this.TYPE = TYPE;
        this.ORDER = ORDER;
        this.STC_COLOR = STC_COLOR;
        this.STC_OPACITY = STC_OPACITY;
    }

    public int getCOMP_ID() {
        return this.COMP_ID;
    }

    public void setCOMP_ID(int COMP_ID) {
        this.COMP_ID = COMP_ID;
    }

    public int getTEMPLATE_ID() {
        return this.TEMPLATE_ID;
    }

    public void setTEMPLATE_ID(int TEMPLATE_ID) {
        this.TEMPLATE_ID = TEMPLATE_ID;
    }

    public float getPOS_X() {
        return this.POS_X;
    }

    public void setPOS_X(float POS_X) {
        this.POS_X = POS_X;
    }

    public float getPOS_Y() {
        return this.POS_Y;
    }

    public void setPOS_Y(float POS_Y) {
        this.POS_Y = POS_Y;
    }

    public String getRES_ID() {
        return this.RES_ID;
    }

    public void setRES_ID(String RES_ID) {
        this.RES_ID = RES_ID;
    }

    public String getTYPE() {
        return this.TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public int getORDER() {
        return this.ORDER;
    }

    public void setORDER(int ORDER) {
        this.ORDER = ORDER;
    }

    public float getROTATION() {
        return this.ROTATION;
    }

    public void setROTATION(float ROTATION) {
        this.ROTATION = ROTATION;
    }

    public int getWIDTH() {
        return this.WIDTH;
    }

    public void setWIDTH(int WIDTH) {
        this.WIDTH = WIDTH;
    }

    public int getHEIGHT() {
        return this.HEIGHT;
    }

    public void setHEIGHT(int HEIGHT) {
        this.HEIGHT = HEIGHT;
    }

    public float getY_ROTATION() {
        return this.Y_ROTATION;
    }

    public void setY_ROTATION(float y_ROTATION) {
        this.Y_ROTATION = y_ROTATION;
    }

    public int getSTC_COLOR() {
        return this.STC_COLOR;
    }

    public void setSTC_COLOR(int STC_COLOR) {
        this.STC_COLOR = STC_COLOR;
    }

    public int getSTC_OPACITY() {
        return this.STC_OPACITY;
    }

    public void setSTC_OPACITY(int STC_OPACITY) {
        this.STC_OPACITY = STC_OPACITY;
    }
}
