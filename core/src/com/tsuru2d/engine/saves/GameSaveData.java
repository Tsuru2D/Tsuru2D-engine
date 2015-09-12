package com.tsuru2d.engine.saves;

import org.luaj.vm2.LuaTable;

public class GameSaveData {
    /**
     * The unique ID number of the save file.
     */
    private long mId;

    /**
     * The version of the game which created the save file.
     */
    private long mVersion;

    /**
     * Game-defined save data.
     */
    private LuaTable mRawData;
}
