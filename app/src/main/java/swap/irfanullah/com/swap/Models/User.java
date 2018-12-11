package swap.irfanullah.com.swap.Models;

import com.google.gson.annotations.SerializedName;

import swap.irfanullah.com.swap.R;

public class User {
    @SerializedName("user_id")
    private int USER_ID;
    @SerializedName("username")
    private String USERNAME;
    @SerializedName("name")
    private String FULL_NAME;
    @SerializedName("email")
    private String EMAIL;
    @SerializedName("password")
    private String PASSWORD;
    @SerializedName("profile_image")
    private String PROFILE_IMAGE;
    @SerializedName("created_at")
    private String CREATED_AT;
    @SerializedName("updated_at")
    private String UPDATED_AT;
    @SerializedName("token")
    private String TOKEN;
    @SerializedName("ratting")
    private float RATING;

    public float getRATING() {
        return RATING;
    }

    public String getTOKEN() {
        return TOKEN;
    }

    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }

    public String getFULL_NAME() {
        return FULL_NAME;
    }

    public void setFULL_NAME(String FULL_NAME) {
        this.FULL_NAME = FULL_NAME;
    }

    public int getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(int USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getPROFILE_IMAGE() {
        return PROFILE_IMAGE;
    }

    public void setPROFILE_IMAGE(String PROFILE_IMAGE) {
        this.PROFILE_IMAGE = PROFILE_IMAGE;
    }

    public String getCREATED_AT() {
        return CREATED_AT;
    }

    public void setCREATED_AT(String CREATED_AT) {
        this.CREATED_AT = CREATED_AT;
    }

    public String getUPDATED_AT() {
        return UPDATED_AT;
    }

    public void setUPDATED_AT(String UPDATED_AT) {
        this.UPDATED_AT = UPDATED_AT;
    }
}
