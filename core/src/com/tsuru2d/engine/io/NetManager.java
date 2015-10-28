package com.tsuru2d.engine.io;

import org.luaj.vm2.LuaTable;

public interface NetManager {
    interface Callback {
        void onResult(NetResult result);
    }

    boolean isLoggedIn();

    void login(String username, String password, Callback callback);

    void logout();

    void enumerateSaves(int startIndex, int endIndex, Callback callback);

    void writeSave(GameSaveData data, boolean forceOverwrite, Callback callback);

    void deleteSave(long saveID);

    void readGameSettings(Callback callback);

    void writeGameSettings(LuaTable data, Callback callback);
}
