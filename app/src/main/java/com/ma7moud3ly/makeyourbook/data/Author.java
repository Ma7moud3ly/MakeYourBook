package com.ma7moud3ly.makeyourbook.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ma7moud3ly.makeyourbook.util.FilesHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Author extends MyData {
    public String id, name, img;
    public List<Book> txt_books;
    public List<Book> pdf_books;
    public List<Book> e_books;

    public Author() {
    }

    public Author(String id, String name, String img) {
        this.id = id;
        this.name = name;
        this.img = img;
    }

    public Author(String json) {
        this.json = json;
    }
}