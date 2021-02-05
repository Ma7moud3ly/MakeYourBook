package com.ma7moud3ly.makeyourbook.interfaces;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseUser;

import androidx.fragment.app.Fragment;

public interface ActivityCallbacks {
    void onLogin(FirebaseUser user);
    void navigateTo(Fragment fragment, Bundle bundle, boolean addToBackStack);
}


