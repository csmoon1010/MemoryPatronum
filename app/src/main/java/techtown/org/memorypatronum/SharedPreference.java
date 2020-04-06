package techtown.org.memorypatronum;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreference {
    static SharedPreferences getSharedPreferences(Context ctx){
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setAccount(Context ctx, String id){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString("id", id);
        //editor.putString("password", password);
        editor.commit();
    }

    public static String getId(Context ctx){
        return getSharedPreferences(ctx).getString("id", "");
    }

    /*public static String getPassword(Context ctx){
        return getSharedPreferences(ctx).getString("password", "");
    }*/

    public static void Logout(Context ctx){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }
}
