package com.ma7moud3ly.makeyourbook;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatDelegate;

public class App extends android.app.Application {
    private static Context appContext;
    public static final String DEBUG_TAG = "HINT";
    public static String DATA_DIR = "";
    public static boolean isLoggedIn = false;

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


    }

    public static void toast(Object o) {
        Toast.makeText(getAppContext(), o.toString(), Toast.LENGTH_SHORT).show();
    }

    public static void l(Object o) {
        Log.i(DEBUG_TAG, o.toString());
    }
}