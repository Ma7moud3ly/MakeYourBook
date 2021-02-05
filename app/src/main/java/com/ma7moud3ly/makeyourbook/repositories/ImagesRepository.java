package com.ma7moud3ly.makeyourbook.repositories;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ma7moud3ly.makeyourbook.App;
import com.ma7moud3ly.makeyourbook.R;
import com.ma7moud3ly.makeyourbook.util.CheckInternet;

import java.io.File;

import androidx.databinding.BindingAdapter;

public class ImagesRepository {

    @BindingAdapter(value = {"loadImage"})
    public static void loadImage(ImageView imageView, String path) {
        if (path == null) return;
        File file = new File(path);
        load(imageView, file.getParent(), file.getName());
    }

    private static File localFile(String dir, String name) {
        return new File(App.DATA_DIR + "/" + dir + "/" + name);
    }

    private static void load(ImageView imageView, String imgDir, String imgName) {
        Glide.with(imageView.getContext()).load(imageView.getContext().getResources().getDrawable(R.drawable.icon2)).into(imageView);
        File img = localFile(imgDir, imgName);
        if (img.exists()) {
            RequestOptions options = new RequestOptions().centerCrop().placeholder(R.drawable.icon2).error(R.drawable.icon2);
            Glide.with(App.getAppContext()).load(img.getAbsolutePath()).diskCacheStrategy(DiskCacheStrategy.NONE).apply(options).into(imageView);
        } else if (CheckInternet.isConnected(imageView.getContext())) {
            Glide.with(imageView.getContext()).load(imageView.getContext().getResources().getDrawable(R.drawable.icon2)).into(imageView);
            loadRemoteImage(imageView, imgDir, imgName);
        }
    }

    private static void loadRemoteImage(ImageView imageView, String imgDir, String imgName) {
        StorageReference storeRef = FirebaseStorage.getInstance().getReference().child(imgDir).child(imgName );
        File dir = new File(App.DATA_DIR + "/" + imgDir);
        if (!dir.exists()) dir.mkdirs();
        File imgFile = new File(dir, imgName);
        final String path = imgFile.getAbsolutePath();
        storeRef.getFile(imgFile).addOnSuccessListener(taskSnapshot -> {
            RequestOptions options = new RequestOptions().centerCrop().placeholder(R.drawable.icon2).error(R.drawable.icon2);
            Glide.with(App.getAppContext()).load(path).diskCacheStrategy(DiskCacheStrategy.NONE).apply(options).into(imageView);
        }).addOnFailureListener(exception -> {
            exception.printStackTrace();
        });
    }


}


