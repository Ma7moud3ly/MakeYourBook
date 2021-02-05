package com.ma7moud3ly.makeyourbook.data;

import java.util.List;

public class Collection extends MyData {
    public String id, title;
    public List<Book> books;

    public Collection() {
    }

    public Collection(String id, String title, List<Book> books) {
        this.id = id;
        this.title = title;
        this.books = books;
    }
    public Collection(String json) {
        this.json = json;
    }
}