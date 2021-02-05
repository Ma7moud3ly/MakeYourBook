package com.ma7moud3ly.makeyourbook.data;

import java.util.List;


public class Home extends MyData {
    public List<Collection> collections;
    public List<Author> authors, quotes;

    public Home(String json) {
        this.json = json;
    }

    public Home() {
    }
}