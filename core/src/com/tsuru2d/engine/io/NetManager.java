package com.tsuru2d.engine.io;

import org.luaj.vm2.LuaTable;

public interface NetManager {
    interface Callback {
        void onResult(Result result, Object data);
    }

    enum Result {
        SUCCESS("success"),
        ERROR_CONNECTION_FAILED("connection_failed"),
        ERROR_NOT_AUTHENTICATED("not_authenticated"),
        ERROR_INVALID_SLOT("invalid_slot"),
        ERROR_SLOT_IN_USE("slot_in_use");

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
