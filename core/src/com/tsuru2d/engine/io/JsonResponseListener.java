package com.tsuru2d.engine.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.io.IOException;

public class JsonResponseListener implements Net.HttpResponseListener {
    private final NetManager.Callback mCallback;

    public JsonResponseListener(NetManager.Callback callback) {
        mCallback = callback;
    }

    @Override
    public void handleHttpResponse(Net.HttpResponse httpResponse) {
        if (httpResponse.getStatus().getStatusCode() != HttpStatus.SC_OK) {
            callbackError("connection_failed", null);
            return;
        }

        JsonReader jsonReader = new JsonReader();
        JsonValue responseJson = jsonReader.parse(httpResponse.getResultAsStream());
        if (responseJson.getBoolean("success")) {
            onSuccess(responseJson);
        } else {
            onError(responseJson);
        }
    }

    @Override
    public void failed(Throwable t) {
        if (t instanceof IOException) {
            callbackError("connection_failed", t);
        } else {
            t.printStackTrace();
            callbackError("unknown_error", t);
        }
    }

    @Override
    public void cancelled() {
        callbackError("request_cancelled", null);
    }

    protected void callbackError(String errorCode, Throwable e) {
        NetResult result = new NetResult();
        result.mSuccess = false;
        result.mErrorCode = errorCode;
        result.mData = e;
        runCallback(result);
    }

    protected void callbackSuccess(Object data) {
        NetResult result = new NetResult();
        result.mSuccess = true;
        result.mData = data;
        runCallback(result);
    }

    protected void runCallback(final NetResult result) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                mCallback.onResult(result);
            }
        });
    }

    protected void onSuccess(JsonValue responseJson) {
        callbackSuccess(null);
    }

    protected void onError(JsonValue responseJson) {
        callbackError(responseJson.getString("error"), null);
    }
}
