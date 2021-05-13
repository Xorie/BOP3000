package application.bop3000.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceConfig {
    private SharedPreferences sharedPreferences;
    private Context context;
    private static final String PREFS_NAME = "preferenceName";

    public SharedPreferenceConfig(Context applicationContext) {
        this.context = applicationContext;
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void login_status(boolean status) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("LOGIN_STATUS", status).apply();
    }

    public boolean read_login_status() {
        boolean status = false;
        status = sharedPreferences.getBoolean("LOGIN_STATUS", false);
        return status;
    }


    public boolean setPreference(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public String getPreference(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, "defaultValue");
    }
}
