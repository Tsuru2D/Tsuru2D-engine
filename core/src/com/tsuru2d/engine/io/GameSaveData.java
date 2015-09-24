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
     * The ID of the scene that the player was in for this
     * save file.
     */
    private String mSceneId;

    /**
     * The base ID of the frame that the player was on.
     * Since not all frames have IDs, the actual frame
     * will be determined by this value and the value
     * of {@link #mFrameOffset}.
     */
    private String mFrameId;

    /**
     * An offset from {@link #mFrameId} that determines the
     * exact frame that the player was on. For example, if
     * this value is 0, the player will start on the frame
     * with ID {@link #mFrameId}. If this value is two, the
     * player will start two frames after that frame, and so on.
     */
    private int mFrameOffset;

    /**
     * Game-defined save data. Make sure that all non-array
     * keys are strings and that you do not store circular
     * references in this table.
     */
    public LuaTable mCustomData;
}
