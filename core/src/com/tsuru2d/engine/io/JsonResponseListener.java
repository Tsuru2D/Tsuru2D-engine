package com.tsuru2d.engine.io;


import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public abstract class JsonResponseListener implements Net.HttpResponseListener {
    private final Json mJson;
    private final NetManager.Callback mCallback;

    public JsonResponseListener(Json json, NetManager.Callback callback) {
        mJson = json;
        mCallback = callback;
    }

    @Override
    public void handleHttpResponse(Net.HttpResponse httpResponse) {
        if (httpResponse.getStatus().getStatusCode() != HttpStatus.SC_OK) {
            callbackError("connection_failed");
            return;
        }

        JsonValue responseJson = mJson.fromJson(JsonValue.class, httpResponse.getResultAsStream());
        if (!responseJson.getBoolean("success")) {
            callbackError(responseJson.getString("error"));
        }

        handleJson(responseJson);
    }

    @Override
    public void failed(Throwable t) {
        callbackError("connection_failed");
    }

    @Override
    public void cancelled() {
        callbackError("request_cancelled");
    }

    protected void callbackError(String errorCode) {
        NetResult result = new NetResult();
        result.mSuccess = false;
        result.mErrorCode = errorCode;
        runCallback(result);
    }

    protected void callbackSuccess(Object data) {
        NetResult result = new NetResult();
        result.mSuccess = false;
        result.mData = data;
        runCallback(result);
    }

    protected void runCallback(NetResult result) {
        mCallback.onResult(result);
    }

    protected abstract void handleJson(JsonValue responseJson);
}
