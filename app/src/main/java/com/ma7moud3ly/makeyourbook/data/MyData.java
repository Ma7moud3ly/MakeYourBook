package com.ma7moud3ly.makeyourbook.data;
/**
 * اصنع كتابك Make your Book
 * @author Mahmoud Aly
 * engma7moud3ly@gmail.com
 * @since sep 2020
 */
import com.google.gson.Gson;

public class MyData {

    public String toString() {
        return new Gson().toJson(this);
    }

    public String json;

    public <M> M init() {
        return deserialize(json, this.getClass());
    }

    public <DATA> DATA deserialize(String str, Class cls) {
        return (DATA) cls.cast(new Gson().fromJson(str, cls));
    }

}
