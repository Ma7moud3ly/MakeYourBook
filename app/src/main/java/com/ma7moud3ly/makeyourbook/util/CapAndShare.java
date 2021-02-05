package com.ma7moud3ly.makeyourbook.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.ma7moud3ly.makeyourbook.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import androidx.annotation.NonNull;

/*
 * capture and share
 * capture a view as bitmap and share or store it locally*/
public class CapAndShare {
    private final Context context;
    private final View view;
    private final String IMAGES_FOLDER_NAME = "MakeYourBook";

    public CapAndShare(Context context, View view) {
        this.context = context;
        this.view = view;

    }

    public boolean save() {
        String name = "make-your-book-" + System.currentTimeMillis();
        File image = saveImage(name);
        return image != null && image.exists();
    }

    public void share(String subject, String text) {
        String name = "make-your-book";
        File image = saveImageLocally(name);
        if (image != null && image.exists())
            shareImage(image, subject, text);
    }


    private File saveImageLocally(@NonNull String name) {
        try {
            File image = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), name + ".png");
            boolean saved;
            OutputStream fos;
            fos = new FileOutputStream(image);
            view.setFocusable(false);
            view.setDrawingCacheEnabled(true);
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            Bitmap bitmap = view.getDrawingCache();
            saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            view.setDrawingCacheEnabled(false);
            fos.flush();
            fos.close();
            if (saved && image.exists()) {
                return image;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private File saveImage(@NonNull String name) {
        try {
            String imagesDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).toString() + File.separator + IMAGES_FOLDER_NAME;
            File image = new File(imagesDir, name + ".png");
            boolean saved;
            OutputStream fos;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentResolver resolver = context.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + IMAGES_FOLDER_NAME);
                Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                fos = resolver.openOutputStream(imageUri);
            } else {
                File dir = new File(imagesDir);
                if (!dir.exists())
                    dir.mkdir();
                fos = new FileOutputStream(image);
            }
            view.setFocusable(false);
            view.setDrawingCacheEnabled(true);
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            Bitmap bitmap = view.getDrawingCache();
            saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            view.setDrawingCacheEnabled(false);
            fos.flush();
            fos.close();
            if (saved && image.exists()) {
                addToGallery(image);
                return image;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void shareImage(File file, String subject, String text) {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.app_name)));
        } catch (Exception e) {
            Toast.makeText(context, "No App Available", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private void addToGallery(File pic) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(pic);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

}
