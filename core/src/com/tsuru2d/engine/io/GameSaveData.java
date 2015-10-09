package com.tsuru2d.engine.io;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import org.luaj.vm2.LuaTable;

/**
 * Container class for save data.
 */
public class GameSaveData implements Json.Serializable {
    /**
     * The ID number of the save file. This number must be unique
     * for all (game, user) pairs. Never re-use an existing ID
     * number; if you want to replace an existing save file, see
     * {@link #mIndex}.
     */
    public long mId;

    /**
     * The index of the save file. The client may request a
     * subset of save files by specifying an index range;
     * this will speed up loading on slow connections.
     */
    public int mIndex;

    /**
     * The version of the game which created the save file.
     * This is necessary so that games may update their
     * table schema and retain backwards compatibility with
     * save data created in a previous version of the game.
     */
    public int mVersion;

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
    public String mSceneId;

    /**
     * The base ID of the frame that the player was on.
     * Since not all frames have IDs, the actual frame
     * will be determined by this value and the value
     * of {@link #mFrameOffset}.
     */
    public String mFrameId;

    /**
     * An offset from {@link #mFrameId} that determines the
     * exact frame that the player was on. For example, if
     * this value is 0, the player will start on the frame
     * with ID {@link #mFrameId}. If this value is 2, the
     * player will start two frames after that frame, and so on.
     */
    public int mFrameOffset;

    /**
     * Game-defined save data. Make sure that all non-array
     * keys are strings and that you do not store circular
     * references in this table.
     */
    public LuaTable mCustomState;

    @Override
    public void write(Json json) {
        json.writeValue("id", mId);
        json.writeValue("index", mIndex);
        json.writeValue("version", mVersion);
        json.writeValue("time", mCreationTime);
        json.writeValue("scene", mSceneId);
        json.writeValue("frame", mFrameId);
        json.writeValue("frameOffset", mFrameOffset);
        json.writeValue("state", mCustomState);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        mId = json.readValue("id", long.class, jsonData);
        mIndex = json.readValue("index", int.class, jsonData);
        mVersion = json.readValue("version", int.class, jsonData);
        mCreationTime = json.readValue("time", long.class, jsonData);
        mSceneId = json.readValue("scene", String.class, jsonData);
        mFrameId = json.readValue("frame", String.class, jsonData);
        mFrameOffset = json.readValue("frameOffset", int.class, jsonData);
        mCustomState = json.readValue("state", LuaTable.class, jsonData);
    }
}
