package com.tsuru2d.engine.io;

import org.luaj.vm2.LuaTable;

/**
 * Container class for save data.
 */
public class GameSaveData {
    /**
     * The ID number of the save file. This number must be unique
     * for all (game, user) pairs.
     */
    public long mId;

    /**
     * The version of the game which created the save file.
     * This is necessary so that games may update their
     * table schema and retain backwards compatibility with
     * save data created in a previous version of the game.
     */
    public long mVersion;

    /**
     * The time at which the save file was created, in milliseconds
     * from midnight, January 1, 1970 UTC. This is the value
     * returned by {@link System#currentTimeMillis()}.
     */
    public long mCreationTime;

    /**
     * Game-defined save data. Make sure that all non-array
     * keys are strings and that you do not store circular
     * references in this table.
     */
    public LuaTable mRawData;
}
