package cn.edu.lzu.oss.ecab.javabean;

public class ItemDetail {
    private String location;
    private Times time;
    private int height;
    private int width;
    private int State;
    private String UID;

    public ItemDetail(String location, Times time, int height, int width, int state, String UID) {
        this.location = location;
        this.time = time;
        this.height = height;
        this.width = width;
        State = state;
        this.UID = UID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Times getTime() {
        return time;
    }

    public void setTime(Times time) {
        this.time = time;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
