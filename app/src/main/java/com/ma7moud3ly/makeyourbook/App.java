package com.ma7moud3ly.makeyourbook;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.ma7moud3ly.makeyourbook.util.CONSTANTS;
import com.ma7moud3ly.makeyourbook.util.CacheHelper;
import com.ma7moud3ly.makeyourbook.util.RootHelper;

import java.io.File;

public class App extends android.app.Application {
    private static Context appContext;
    public static final String DEBUG_TAG = "HINT";
    public static String DATA_DIR = "";
    public static boolean isLoggedIn = false;
    public static boolean newVersion = false;

    public static Context getAppContext() {
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        DATA_DIR = getApplicationInfo().dataDir;
        appContext = getApplicationContext();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        firstLaunch();


    }

    public void firstLaunch() {
        SharedPreferences prefs = getSharedPreferences("version", 0);
        int current_version;
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            current_version = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return;
        }
        int saved_version = prefs.getInt("version", 0);
        App.l("versions", current_version, saved_version);
        if (current_version != saved_version) {
            App.newVersion = true;
            CacheHelper.deleteCache(getAppContext());
            CacheHelper.deleteDir(new File(DATA_DIR));
            prefs.edit().putInt("version", current_version).commit();
        }
    }

    public static void toast(Object o) {
        Toast.makeText(getAppContext(), o.toString(), Toast.LENGTH_SHORT).show();
    }

    public static void l(Object... o) {
        String s = "";
        for (Object oo : o) s += oo.toString() + " ";
        Log.i(DEBUG_TAG, s);
    }
}