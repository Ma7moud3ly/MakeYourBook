package com.ma7moud3ly.makeyourbook.data;

public class Article extends MyData {
    public String id, img, title, author_id, author, author_img, publish;

    public Article(String json) {
        this.json = json;
    }

    public Article() {
    }
}