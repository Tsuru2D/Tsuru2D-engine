package com.tsuru2d.engine.io;

import org.luaj.vm2.LuaTable;

public interface NetManager {
    interface Callback {
        void onResult(Result result, Object data);
    }

    enum Result {
        // Common
        SUCCESS("success"),
        ERROR_CONNECTION_FAILED("connection_failed"),
        ERROR_NOT_AUTHENTICATED("not_authenticated"),
        ERROR_INVALID_GAME_PACKAGE("invalid_game_package"),

        // Account creation
        ERROR_INVALID_EMAIL("invalid_email"),
        ERROR_INVALID_PASSWORD("invalid_password"),
        ERROR_USER_ALREADY_EXISTS("user_already_exists"),

        // Account login
        ERROR_USER_NOT_FOUND("user_not_found"),
        ERROR_INCORRECT_PASSWORD("incorrect_password"),

        // Write save
        ERROR_SAVE_DATA_ALREADY_EXISTS("save_data_already_exists"),

        // Delete save
        ERROR_INVALID_SAVE_ID("invalid_save_id"),
        ERROR_DELETE_FAILED("delete_failed");

        private final String mStrValue;

        Result(String strValue) {
            mStrValue = strValue;
        }

        public String strValue() {
            return mStrValue;
        }
    }

    boolean isLoggedIn();
    void login(String username, String password, Callback callback);
    void logout();
    void enumerateSaveGames(int startIndex, int endIndex, Callback callback);
    void writeSaveGame(GameSaveData data, boolean forceOverwrite, Callback callback);
    void getConfig(Callback callback);
    void writeConfig(LuaTable data, Callback callback);
}
