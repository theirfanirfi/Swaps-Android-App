package swap.irfanullah.com.swap.Models;

public class Swap {
    private int SWAP_ID;
    private int POSTER_USER_ID;
    private int SWAPED_WITH_USER_ID;
    private int STATUS_ID;
    private String CREATED_AT;
    private String UPDATED_AT;

    public int getSWAP_ID() {
        return SWAP_ID;
    }

    public void setSWAP_ID(int SWAP_ID) {
        this.SWAP_ID = SWAP_ID;
    }

    public int getPOSTER_USER_ID() {
        return POSTER_USER_ID;
    }

    public void setPOSTER_USER_ID(int POSTER_USER_ID) {
        this.POSTER_USER_ID = POSTER_USER_ID;
    }

    public int getSWAPED_WITH_USER_ID() {
        return SWAPED_WITH_USER_ID;
    }

    public void setSWAPED_WITH_USER_ID(int SWAPED_WITH_USER_ID) {
        this.SWAPED_WITH_USER_ID = SWAPED_WITH_USER_ID;
    }

    public int getSTATUS_ID() {
        return STATUS_ID;
    }

    public void setSTATUS_ID(int STATUS_ID) {
        this.STATUS_ID = STATUS_ID;
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
