package com.ma7moud3ly.makeyourbook.di;

import android.content.Context;

import com.ma7moud3ly.makeyourbook.activities.BaseActivity;
import com.ma7moud3ly.makeyourbook.di.modules.UPrefModule;
import com.ma7moud3ly.makeyourbook.di.modules.ViewModelModule;
import com.ma7moud3ly.makeyourbook.fragments.BaseFragment;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {UPrefModule.class, ViewModelModule.class})
public interface ActivityGraph {
    void inject(BaseActivity baseActivity);

    void inject(BaseFragment fragment);

    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        ActivityGraph create(@BindsInstance Context context);
    }
}
