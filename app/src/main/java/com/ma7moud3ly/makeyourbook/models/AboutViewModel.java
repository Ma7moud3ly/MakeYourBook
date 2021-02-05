package com.ma7moud3ly.makeyourbook.models;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;

import com.ma7moud3ly.makeyourbook.R;


public class AboutViewModel {
    public String version = "1.0";

    public void appVersion(Activity a) {
        PackageManager pm = a.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(a.getPackageName(), 0);
            this.version = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void share(View view) {
        Context context = view.getContext();
        String play_url = "http://play.google.com/store/apps/details?id=" + context.getPackageName();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
        sendIntent.putExtra(Intent.EXTRA_TEXT, play_url);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, context.getResources().getString(R.string.app_name)));
    }

    public void rate(View view) {
        Context context = view.getContext();
        Uri uri = Uri.parse("FBD://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }

    }

    public void contact(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        Uri d = Uri.parse("http://www.facebook.com/engma7moud3ly");
        i.setData(d);
        view.getContext().startActivity(i);
    }


}
