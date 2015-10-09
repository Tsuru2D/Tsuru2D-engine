package com.tsuru2d.engine;

import com.tsuru2d.engine.io.GameSaveData;
import org.luaj.vm2.LuaTable;

public interface NetManager {
    void login();
    void login(String username, String password);
    void logout();
    void enumerateSaveGames(int startIndex, int endIndex);
    void writeSaveGame(GameSaveData data);
    void getConfig();
    void writeConfig(LuaTable data);
}
