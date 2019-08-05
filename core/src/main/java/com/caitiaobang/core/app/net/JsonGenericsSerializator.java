package com.caitiaobang.core.app.net;

import com.google.gson.Gson;

public class JsonGenericsSerializator implements IGenericsSerializator {
    Gson mGson = new Gson();
    @Override
    public <T> T transform(String response, Class<T> classOfT) {
        return mGson.fromJson(response, classOfT);
    }
}
