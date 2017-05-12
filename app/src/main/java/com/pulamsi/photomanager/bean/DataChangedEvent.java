package com.pulamsi.photomanager.bean;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-30
 * Time: 10:48
 * FIXME
 */
public class DataChangedEvent {

    private String flag;

    private int position;

    private Object object;

    public DataChangedEvent(String flag, Object object) {
        this.flag = flag;
        this.object = object;
    }

    public DataChangedEvent(int position, Object object) {
        this.position = position;
        this.object = object;
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public DataChangedEvent(Object object) {
        this.object = object;
    }

    public DataChangedEvent(String flag) {
        this.flag = flag;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

}
