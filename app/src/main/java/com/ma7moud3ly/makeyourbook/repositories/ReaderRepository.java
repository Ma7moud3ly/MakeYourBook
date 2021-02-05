package com.ma7moud3ly.makeyourbook.repositories;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */
import android.content.Context;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ma7moud3ly.makeyourbook.App;
import com.ma7moud3ly.makeyourbook.util.CheckInternet;
import com.ma7moud3ly.makeyourbook.util.FilesHelper;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;


public class ReaderRepository {

    private Context context;
    public MutableLiveData<ArrayList<String>> data = new MutableLiveData<>();
    public final static int maxPageLength = 5000;


    @Inject
    public ReaderRepository(Context context) {
        this.context = context;
    }

    private ArrayList<String> splitStoryToPages(String story) {
        ArrayList<String> temp = new ArrayList<>();
        if (story.length() <= maxPageLength) temp.add(story);
        else {
            int pages = (int) Math.ceil(1.0 * story.length() / maxPageLength);
            for (int i = 0; i < pages; i++) {
                int pageIndex1 = i * maxPageLength;
                int pageIndex2 = pageIndex1 + maxPageLength;
                if (pageIndex2 > story.length()) pageIndex2 = story.length();
                temp.add(story.substring(pageIndex1, pageIndex2));
            }
        }
        return temp;
    }

    public void read(String ref, String name) {
        File file = new File(App.DATA_DIR + "/" + ref + "/" + name);
        if (file.exists()) {
            String story = FilesHelper.read(file.getAbsolutePath());
            data.setValue(splitStoryToPages(story));
        } else if (CheckInternet.isConnected(context)) {
            StorageReference storeRef = FirebaseStorage.getInstance().getReference().child(ref).child(name);
            File parent = file.getParentFile();
            if (!parent.exists()) parent.mkdirs();
            final String path = file.getAbsolutePath();
            storeRef.getFile(file).addOnSuccessListener(taskSnapshot -> {
                String story = FilesHelper.read(path);
                data.setValue(splitStoryToPages(story));
            }).addOnFailureListener(exception -> {
                data.setValue(new ArrayList<>());
            });
        } else {
            data.setValue(new ArrayList<>());
        }
    }


}