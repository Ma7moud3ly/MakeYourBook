package com.ma7moud3ly.makeyourbook.util;

public class SearchHelper {
    public static boolean in(String s1, String s2) {
        s1 = edit(s1);
        s2 = edit(s2);
        return s1.contains(s2);

    }

    private static String edit(String s) {
        return s.replace("أ", "ا")
                .replace("إ", "ا")
                .replace("ى", "ي")
                .replace("رواية", "")
                .replace("كتاب", "")
                .trim();
    }
}
