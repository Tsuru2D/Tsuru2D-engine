package com.tsuru2d.engine.io;

import com.tsuru2d.engine.io.GameSaveData;
import org.luaj.vm2.LuaTable;

public interface FileManager {
    void enumerateSaveGames(int startIndex, int endIndex);
    void writeSaveGame(GameSaveData data);
    void getConfig();
    void writeConfig(LuaTable data);
}
