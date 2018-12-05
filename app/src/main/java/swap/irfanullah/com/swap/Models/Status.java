package swap.irfanullah.com.swap.Models;

import com.google.gson.annotations.SerializedName;

public class Status {
    private String USERNAME;
    private int USER_ID;
    private int STATUS_ID;
    private String STATUS;
    private String TIME;
    private String PROFILE_IMAGE;


    public Status() {

    }

    public Status(String USERNAME, int USER_ID, int STATUS_ID, String STATUS, String TIME, String PROFILE_IMAGE) {
        this.USERNAME = USERNAME;
        this.USER_ID = USER_ID;
        this.STATUS_ID = STATUS_ID;
        this.STATUS = STATUS;
        this.TIME = TIME;
        this.PROFILE_IMAGE = PROFILE_IMAGE;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public int getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(int USER_ID) {
        this.USER_ID = USER_ID;
    }

    public int getSTATUS_ID() {
        return STATUS_ID;
    }

    public void setSTATUS_ID(int STATUS_ID) {
        this.STATUS_ID = STATUS_ID;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public String getPROFILE_IMAGE() {
        return PROFILE_IMAGE;
    }

    public void setPROFILE_IMAGE(String PROFILE_IMAGE) {
        this.PROFILE_IMAGE = PROFILE_IMAGE;
    }
}
