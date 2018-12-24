package swap.irfanullah.com.swap.Models;

import android.net.Uri;

public class Media {
    private Uri uri;
    private int type;

    public Media(Uri uri, int type) {
        this.uri = uri;
        this.type = type;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
