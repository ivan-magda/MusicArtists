package com.ivanmagda.musicartists.model;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

// TODO: http://developer.android.com/intl/ru/reference/android/net/http/HttpResponseCache.html
public final class Persistence {

    // Properties.

    public static final String ARTISTS_PERSISTENCE_KEY = "artists";

    // Init.

    private Persistence() {
    }

    // Saving support.

    public static void writeObject(Context context, String key, Object object) throws IOException {
        FileOutputStream fileOutputStream = context.openFileOutput(key, Context.MODE_PRIVATE);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
        fileOutputStream.close();
    }

    public static Object readObject(Context context, String key) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = context.openFileInput(key);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        return objectInputStream.readObject();
    }

}
