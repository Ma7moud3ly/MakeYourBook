package com.ma7moud3ly.makeyourbook.activities;
/**
 * اصنع كتابك Make your Book
 *
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ma7moud3ly.makeyourbook.App;
import com.ma7moud3ly.makeyourbook.R;
import com.ma7moud3ly.makeyourbook.di.ActivityGraph;
import com.ma7moud3ly.makeyourbook.di.ViewModelFactory;
import com.ma7moud3ly.makeyourbook.interfaces.ActivityCallbacks;
import com.ma7moud3ly.makeyourbook.models.AboutViewModel;
import com.ma7moud3ly.makeyourbook.observables.UiState;
import com.ma7moud3ly.makeyourbook.util.CONSTANTS;
import com.ma7moud3ly.makeyourbook.util.RootHelper;
import com.ma7moud3ly.ustore.UPref;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class BaseActivity extends AppCompatActivity implements ActivityCallbacks {
    //public UiState uiState;
    @Inject
    public ViewModelFactory viewModelFactory;
    @Inject
    public UiState uiState;
    @Inject
    public UPref pref;

    public ActivityGraph activityGraph;
    public FirebaseAuth firebaseAuth;
    public View retry;

    public static String[] fontNames;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fontNames = getResources().getStringArray(R.array.font_names);
    }

    @Override
    public void navigateTo(Fragment fragment, Bundle bundle, boolean addToBackStack) {
        if (!App.isLoggedIn) return;
        uiState.showMenu.set(false);
        if (bundle != null) fragment.setArguments(bundle);
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    public void onLogin(FirebaseUser user) {

    }


    //sign in anonymously to firebase
    public void login() {
        uiState.state.set(CONSTANTS.LOADING);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInAnonymously()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        uiState.state.set(CONSTANTS.LOADED);
                        App.isLoggedIn = true;
                        onLogin(firebaseAuth.getCurrentUser());
                    } else {
                        //App.l("signInAnonymously:failure" + task.getException());
                        App.toast(getString(R.string.no_internet));
                        uiState.state.set(CONSTANTS.RETRY);
                        App.isLoggedIn = false;
                    }
                });
    }

    public boolean checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            App.toast(getString(R.string.save_available));
        }
    }

    public void back(View v) {
        try {
            uiState.showMenu.set(false);
            int c = getSupportFragmentManager().getBackStackEntryCount();
            if (c == 0) finish();
            getSupportFragmentManager().popBackStack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void back() {
        try {
            uiState.showMenu.set(false);
            int c = getSupportFragmentManager().getBackStackEntryCount();
            if (c == 0) finish();
            getSupportFragmentManager().popBackStack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void subscribeToTopics(String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(task -> {
                    pref.put("notify-" + topic, true);
                    //App.l(topic + " sub : " + task.isSuccessful());
                });
    }

    public void unsubscribeToTopics(String topic) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
                .addOnCompleteListener(task -> {
                    pref.put("notify-" + topic, false);
                    //App.l(topic + " un-sub : " + task.isSuccessful());
                });
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(getString(R.string.channel_id), name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
