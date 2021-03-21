package com.ma7moud3ly.makeyourbook.data;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class EBook extends MyData {
    public Map<String, String> contents;
    public Map<String, String> chapters;
    public Map<String, Map<Integer, String>> images;
    public int pages, wordPerPage, readingTime;
    public File dir;

    public EBook(String json) {
        this.json = json;
    }

    public EBook() {
    }
}