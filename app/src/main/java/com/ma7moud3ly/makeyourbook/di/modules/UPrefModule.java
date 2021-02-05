package com.ma7moud3ly.makeyourbook.di.modules;

import android.app.Activity;
import android.content.Context;

import com.ma7moud3ly.ustore.UPref;

import dagger.Module;
import dagger.Provides;

@Module
public class UPrefModule {
    @Provides
    public UPref provideIt(Context context) {
        return new UPref((Activity) context);
    }
}
