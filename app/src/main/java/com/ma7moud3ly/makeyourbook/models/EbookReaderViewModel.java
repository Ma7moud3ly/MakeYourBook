package com.ma7moud3ly.makeyourbook.models;
/**
 * اصنع كتابك Make your Book
 *
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.ma7moud3ly.makeyourbook.App;
import com.ma7moud3ly.makeyourbook.data.EBook;
import com.ma7moud3ly.makeyourbook.repositories.EbookReaderRepository;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EbookReaderViewModel extends ViewModel {
    public MutableLiveData<File> bookPath;
    public MutableLiveData<EBook> ebook = new MutableLiveData<>();
    public MutableLiveData<String> chapterContent = new MutableLiveData<>();
    private EbookReaderRepository repo;
    public String[] allChapters;

    @Inject
    public EbookReaderViewModel(EbookReaderRepository repo) {
        this.repo = repo;
        bookPath = repo.data;
    }

    public void read(String id) {
        repo.downloadEbook(id);
    }

    public void openChapter(String chapter) {
        EBook ebook = this.ebook.getValue();
        String content = ebook.contents.get(chapter);
        int i = 0;
        Map<String, String> images = new HashMap<>();
        if (ebook.images != null && ebook.images.containsKey(chapter)) {
            for (Map.Entry<Integer, String> entry : ebook.images.get(chapter).entrySet()) {
                File img = new File(this.bookPath.getValue(), entry.getValue());
                if (!img.exists()) continue;
                int pos = entry.getKey() + i;
                String part1 = "", part2 = "";
                if (content.trim().length() > 0 && pos < content.length()) {
                    part1 = content.substring(0, pos);
                    part2 = content.substring(pos);
                }
                String image = "<img style='max-width:100%' src='" + loadImage(img) + "' />";
                String part = "{{IMAGE" + entry.getKey() + "}}";
                images.put(part, image);
                i += part.length();
                content = part1 + part + part2;
            }
        }

        content = content
                .replace("\n", "<br>")
                .replace("\u2060\u202B", " ");
        if (images.size() > 0) {
            for (Map.Entry<String, String> entry : images.entrySet()) {
                String key = entry.getKey();
                String val = entry.getValue();
                content = content.replace(key, val);
            }
        }
        chapterContent.setValue(content);

    }

    private String loadImage(File file) {
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap.CompressFormat compress;
        if (file.getName().endsWith("png")) compress = Bitmap.CompressFormat.PNG;
        else compress = Bitmap.CompressFormat.JPEG;
        bitmap.compress(compress, 75, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imgageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return "data:image/png;base64," + imgageBase64;
    }

    public int chapterIndex(String chapter) {
        for (int i = 0; i < allChapters.length; i++) {
            String ch = allChapters[i];
            if (ch.equals(chapter)) return i;
        }
        return -1;
    }

}
