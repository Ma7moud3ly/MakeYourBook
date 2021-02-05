package com.ma7moud3ly.makeyourbook.data;

import com.ma7moud3ly.makeyourbook.util.CONSTANTS;

public class Story extends MyData {
    public String id, title, author, author_id, ref;

    public Story(String json) {
        this.json = json;
    }

    public Story(Book book) {
        this.id = book.id;
        this.title = book.name;
        this.author = book.author;
        this.author_id = book.author_id;
        this.ref = CONSTANTS.TXT_BOOKS_DIR;
    }

    public Story(Article article) {
        this.id = article.id;
        this.title = article.title;
        this.author = article.author;
        this.author_id = article.author_id;
        this.ref = CONSTANTS.ARTICLES_DIR;
    }
}