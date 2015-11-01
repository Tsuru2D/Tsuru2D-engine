package com.tsuru2d.engine.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import org.luaj.vm2.LuaTable;

import java.io.IOException;
import java.io.StringWriter;

public class NetManagerImpl implements NetManager {
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

    private JsonWriter newJsonWriter() {
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(writer);
        jsonWriter.setOutputType(JsonWriter.OutputType.json);
        return jsonWriter;
    }

    private void getJson(String url, JsonWriter requestJson, JsonResponseListener listener) {
        String jsonStr = requestJson.getWriter().toString();
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
        if (isLoggedIn()) {
            NetResult result = new NetResult();
            result.mSuccess = false;
            result.mErrorCode = "already_logged_in";
            callback.onResult(result);
            return;
        }

        JsonWriter jsonWriter = newJsonWriter();
        try {
            jsonWriter.object();
            jsonWriter.name("email").value(email);
            jsonWriter.name("password").value(password);
            jsonWriter.pop();
            jsonWriter.close();
        } catch (IOException e) {
            throw new AssertionError(e);
        }

        getJson(TsuruApi.LOGIN, jsonWriter, new JsonResponseListener(callback) {
            @Override
            protected void onSuccess(JsonValue responseJson) {
                storeAuthToken(responseJson);
                callbackSuccess(null);
            }
        });
    }

    @Override
    public void register(String email, String password, Callback callback) {
        JsonWriter jsonWriter = newJsonWriter();
        try {
            jsonWriter.object();
            jsonWriter.name("email").value(email);
            jsonWriter.name("password").value(password);
            jsonWriter.pop();
            jsonWriter.close();
        } catch (IOException e) {
            throw new AssertionError(e);
        }

        getJson(TsuruApi.CREATE_USER, jsonWriter, new JsonResponseListener(callback) {
            @Override
            protected void onSuccess(JsonValue responseJson) {
                storeAuthToken(responseJson);
                callbackSuccess(null);
            }
        });
    }

    @Override
    public void logout() {
        mAuthToken = null;
        // TODO: tell server to invalidate auth token
    }

    @Override
    public void enumerateSaves(int startIndex, final int endIndex, Callback callback) {
        JsonWriter jsonWriter = newJsonWriter();
        try {
            jsonWriter.object();
            jsonWriter.name("auth_token").value(mAuthToken);
            jsonWriter.name("game_package").value(mGamePackage);
            jsonWriter.name("from_index").value(startIndex);
            jsonWriter.name("to_index").value(endIndex);
            jsonWriter.pop();
            jsonWriter.close();
        } catch (IOException e) {
            throw new AssertionError(e);
        }

        getJson(TsuruApi.ENUMERATE_SAVES, jsonWriter, new JsonResponseListener(callback) {
            @Override
            protected void onSuccess(JsonValue responseJson) {
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
    public void writeSave(GameSaveData data, boolean overwrite, Callback callback) {
        JsonWriter jsonWriter = newJsonWriter();
        try {
            jsonWriter.object();
            jsonWriter.name("auth_token").value(mAuthToken);
            jsonWriter.name("game_package").value(mGamePackage);
            jsonWriter.name("overwrite").value(overwrite);
            jsonWriter.name("index").value(data.mIndex);
            jsonWriter.name("version").value(data.mVersion);
            jsonWriter.name("time").value(data.mCreationTime);
            jsonWriter.name("scene_id").value(data.mSceneId);
            jsonWriter.name("frame_id").value(data.mFrameId);
            mJson.setWriter(jsonWriter);
            mJson.writeValue("custom_state", data.mCustomState);
            // mJson.setWriter(null);
            // jsonWriter.name("custom_state").object().pop();
            jsonWriter.pop();
            jsonWriter.close();
        } catch (IOException e) {
            throw new AssertionError(e);
        }

        getJson(TsuruApi.WRITE_SAVE, jsonWriter, new JsonResponseListener(callback));
    }

    @Override
    public void deleteSave(long saveID, Callback callback) {
        JsonWriter jsonWriter = newJsonWriter();
        try {
            jsonWriter.object();
            jsonWriter.name("auth_token").value(mAuthToken);
            jsonWriter.name("save_id").value(saveID);
            jsonWriter.pop();
            jsonWriter.close();
        } catch (IOException e) {
            throw new AssertionError(e);
        }

        getJson(TsuruApi.DELETE_SAVE, jsonWriter, new JsonResponseListener(callback));
    }

    @Override
    public void readGameSettings(Callback callback) {
        JsonWriter jsonWriter = newJsonWriter();
        try {
            jsonWriter.object();
            jsonWriter.name("auth_token").value(mAuthToken);
            jsonWriter.name("game_package").value(mGamePackage);
            jsonWriter.pop();
            jsonWriter.close();
        } catch (IOException e) {
            throw new AssertionError(e);
        }

        getJson(TsuruApi.READ_GAME_SETTINGS, jsonWriter, new JsonResponseListener(callback) {
            @Override
            protected void onSuccess(JsonValue responseJson) {
                JsonValue settingsRaw = responseJson.get("settings");
                LuaTable settings = mJson.readValue(LuaTable.class, settingsRaw);
                callbackSuccess(settings);
            }
        });
    }

    @Override
    public void writeGameSettings(LuaTable data, Callback callback) {
        JsonWriter jsonWriter = newJsonWriter();
        try {
            jsonWriter.object();
            jsonWriter.name("auth_token").value(mAuthToken);
            jsonWriter.name("game_package").value(mGamePackage);
            mJson.setWriter(jsonWriter);
            mJson.writeValue("settings", data);
            // mJson.setWriter(null);
            jsonWriter.pop();
            jsonWriter.close();
        } catch (IOException e) {
            throw new AssertionError(e);
        }

        getJson(TsuruApi.WRITE_GAME_SETTINGS, jsonWriter, new JsonResponseListener(callback));
    }
}
