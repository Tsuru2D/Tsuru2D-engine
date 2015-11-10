package com.tsuru2d.engine.io;

import org.luaj.vm2.LuaTable;

public interface NetManager {
    interface Callback {
        void onResult(NetResult result);
    }

    boolean isLoggedIn();

    void register(String email, String password, Callback callback);

    void login(String email, String password, Callback callback);

    void logout();

    void enumerateSaves(int startIndex, int endIndex, Callback callback);

    void writeSave(boolean overwrite, GameSaveData data, Callback callback);

    void deleteSave(long saveID, Callback callback);

    void readGameSettings(Callback callback);

    void writeGameSettings(LuaTable data, Callback callback);
}
