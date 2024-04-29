package com.example.captainb;
import android.content.Context;
import android.content.SharedPreferences;
import java.util.UUID;

public class UuidFactory {

    protected static final String PREFS_FILE = "captainb_user_id.xml";
    protected static final String PREFS_USER_ID = "user_id";

    public String getUUID(Context context) {
        final SharedPreferences sharedPref = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        String user_id = sharedPref.getString(PREFS_USER_ID, "");

        if (user_id.isEmpty()){
            user_id = genUUID(sharedPref);
        }

        return user_id;
    }

    public String genUUID(SharedPreferences sharedPref) {
        String uniqueID = UUID.randomUUID().toString();
        sharedPref.edit().putString(PREFS_USER_ID, uniqueID).commit();
        return uniqueID;
    }
}
