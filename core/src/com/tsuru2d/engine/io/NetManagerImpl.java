package com.tsuru2d.engine.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.tsuru2d.engine.lua.ExposedJavaClass;
import org.luaj.vm2.LuaTable;

import java.util.HashMap;

public class NetManagerImpl extends ExposedJavaClass implements NetManager {
    private static class TsuruApi {
        private static final String BASE_URL = "http://127.0.0.1:8080/apiv1/";
        public static final String CREATE_USER = BASE_URL + "create_user";
        public static final String LOGIN = BASE_URL + "login";
        public static final String LOGOUT = BASE_URL + "logout";
        public static final String WRITE_SAVE = BASE_URL + "write_save";
        public static final String DELETE_SAVE = BASE_URL + "delete_save";
        public static final String ENUMERATE_SAVES = BASE_URL + "enumerate_saves";
        public static final String READ_GAME_SETTINGS = BASE_URL + "read_game_settings";
        public static final String WRITE_GAME_SETTINGS = BASE_URL + "write_game_settings";
    }

    private final String mGamePackage;
    private final Json mJson;
    private String mAuthToken;

    public NetManagerImpl(String gamePackage) {
        mGamePackage = gamePackage;
        mJson = new Json(JsonWriter.OutputType.json);
        mJson.setSerializer(LuaTable.class, new JsonLuaTableSerializer());
    }

    private void storeAuthToken(JsonValue responseJson) {
        mAuthToken = responseJson.getString("auth_token");
    }

    private void getJson(String url, HashMap<String, Object> requestJson, JsonResponseListener listener) {
        String jsonStr = mJson.toJson(requestJson);
        HttpRequest request = new HttpRequest("POST");
        request.setUrl(url);
        request.setContent(jsonStr);
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Accept", "application/json");
        Gdx.net.sendHttpRequest(request, listener);
    }

    @Override
    public boolean isLoggedIn() {
        return mAuthToken != null;
    }

    @Override
    public void login(String email, String password, Callback callback) {
        HashMap<String, Object> json = new HashMap<String, Object>();
        json.put("email", email);
        json.put("password", password);
        getJson(TsuruApi.LOGIN, json, new JsonResponseListener(mJson, callback) {
            @Override
            protected void handleJson(JsonValue responseJson) {
                storeAuthToken(responseJson);
            }
        });
    }

    @Override
    public void logout() {
        mAuthToken = null;
        // TODO: tell server to invalidate auth token
    }

    @Override
    public void enumerateSaves(int startIndex, int endIndex, Callback callback) {
        HashMap<String, Object> json = new HashMap<String, Object>();
        json.put("auth_token", mAuthToken);
        json.put("game_package", mGamePackage);
        json.put("from_index", startIndex);
        json.put("to_index", endIndex);
        getJson(TsuruApi.ENUMERATE_SAVES, json, new JsonResponseListener(mJson, callback) {
            @Override
            protected void handleJson(JsonValue responseJson) {
                JsonValue saves = responseJson.get("saves");
                GameSaveData[] saveDatas = new GameSaveData[saves.size];
                for (int i = 0; i < saves.size; ++i) {
                    JsonValue save = saves.get(i);
                    GameSaveData saveData = mJson.readValue(GameSaveData.class, save);
                    saveDatas[i] = saveData;
                }
                callbackSuccess(saveDatas);
            }
        });

    }

    @Override
    public void writeSave(GameSaveData data, boolean forceOverwrite, Callback callback) {

    }

    @Override
    public void deleteSave(long saveID) {

    }

    @Override
    public void readGameSettings(Callback callback) {

    }

    @Override
    public void writeGameSettings(LuaTable data, Callback callback) {

    }
}
