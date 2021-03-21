package com.ma7moud3ly.makeyourbook.repositories;
/**
 * اصنع كتابك Make your Book
 *
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */

import android.content.Context;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ma7moud3ly.makeyourbook.App;
import com.ma7moud3ly.makeyourbook.util.CONSTANTS;
import com.ma7moud3ly.makeyourbook.util.CheckInternet;
import com.ma7moud3ly.makeyourbook.util.FilesHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;


public class EbookReaderRepository {

    public MutableLiveData<File> data = new MutableLiveData<>();

    @Inject
    public EbookReaderRepository() {

    }


    public void downloadEbook(String id) {

        File srcFile = new File(App.DATA_DIR + "/" + CONSTANTS.E_BOOKS_DIR + "/" + id + ".zip");
        File parent = srcFile.getParentFile();
        File dstFile = new File(parent, id);
        if(dstFile.exists()){
           // App.l("alerady downloaded");
            data.setValue(dstFile);
            return;
        }

        if (!parent.exists()) parent.mkdirs();
        StorageReference storeRef = FirebaseStorage.getInstance().getReference(CONSTANTS.E_BOOKS_DIR).child(id + ".zip");
        storeRef.getFile(srcFile).addOnSuccessListener(taskSnapshot -> {
            if (!dstFile.exists()) dstFile.mkdir();
            boolean extracted = unzipBook(srcFile, dstFile);
            if (extracted) {
                data.setValue(dstFile);
            } else
                data.setValue(null);
            if (srcFile.exists()) srcFile.delete();
        }).addOnFailureListener(exception -> {
            data.setValue(null);
        });
    }

    private boolean unzipBook(File src, File dst) {
        ZipInputStream zis = null;
        try {
            zis = new ZipInputStream(new FileInputStream(src));
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(dst, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " + dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (zis != null) zis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}