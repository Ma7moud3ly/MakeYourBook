package com.ma7moud3ly.makeyourbook.data;

public class Book extends MyData {
    public String id, name, author, author_id, category, img, author_img, size, format, download_link;
    public long type;

    public Book(String json) {
        this.json = json;
    }

    public Book() {
    }
}