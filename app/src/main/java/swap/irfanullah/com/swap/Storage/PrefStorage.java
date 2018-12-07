package swap.irfanullah.com.swap.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import swap.irfanullah.com.swap.Models.User;

public class PrefStorage {
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static String PREF_STORAGE_FILE = "users";
    public static String USER_PREF_DETAILS = "user";

    public static SharedPreferences getSharedPreference(Context context)
    {
        return context.getSharedPreferences(PREF_STORAGE_FILE, context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getEditor(Context context)
    {
        if(sharedPreferences != null)
        {
            return sharedPreferences.edit();
        }
        else
        {
            sharedPreferences = getSharedPreference(context);
            return sharedPreferences.edit();
        }
    }

    public static String getUserData(Context context)
    {
        String userDetails = "";
        userDetails = getSharedPreference(context).getString(USER_PREF_DETAILS,"");
        return userDetails;
    }

    public static User getUser(Context context)
    {
        Gson gson = new Gson();
        User user = gson.fromJson(PrefStorage.getUserData(context),User.class);
        return user;
    }
}
