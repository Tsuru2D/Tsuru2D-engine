package com.tsuru2d.engine.io;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.Pixmap;
import org.apache.oltu.oauth2.client.HttpClient;

import java.util.List;

public class DataLoader {
    public interface EnumerateCallback {
        void onResult(List<GameSaveData> data, boolean online);
    }

    public interface CreateCallback {
        void onResult(boolean online);
    }

    public interface ThumbnailCallback {
        void onResult(Pixmap thumbnail, boolean online);
    }

    public interface DeleteCallback {
        void onResult(boolean online);
    }

    private final FileHandleResolver mHandleResolver;
    private final HttpClient mHttpClient;

    public DataLoader(FileHandleResolver resolver, HttpClient client) {
        mHandleResolver = resolver;
        mHttpClient = client;
    }

    public void create(GameSaveData data, CreateCallback callback) {

    }

    public void delete(long saveID, DeleteCallback callback) {

    }

    public void getThumbnail(long saveID, ThumbnailCallback callback) {

    }

    public void enumerate(EnumerateCallback callback) {

    }
}
