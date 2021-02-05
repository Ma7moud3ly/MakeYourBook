package com.ma7moud3ly.makeyourbook.util;

import com.ma7moud3ly.makeyourbook.App;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class FilesHelper {

    public static String read(String name) {
        File file = new File(name);
        if (!file.exists())
            return "";
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(file));
            byte[] byt = new byte[dis.available()];
            dis.readFully(byt);
            dis.close();
            String content = new String(byt, 0, byt.length);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
            return e.getStackTrace().toString();
        }
    }

    public static String readJsonString(int resource) {
        InputStream inputStream = App.getAppContext().getResources().openRawResource(resource);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            int pointer;
            while ((pointer = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, pointer);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        String json = writer.toString();
        return json;
    }
}
