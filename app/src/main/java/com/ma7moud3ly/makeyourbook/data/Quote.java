package com.ma7moud3ly.makeyourbook.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ma7moud3ly.makeyourbook.util.FilesHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Quote extends MyData {


    public String id;
    public String text;
    public String source;
    public String author;
    public String author_id;
    public String book_id;
    public String author_img;

    public Quote() {
    }

    public Quote(String json) {
        this.json = json;
    }


}