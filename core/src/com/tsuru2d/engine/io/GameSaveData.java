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
     * The time at which the save file was created, in seconds
     * from midnight, January 1, 1970 UTC. This is the value
     * returned by {@link System#currentTimeMillis()} divided
     * by 1000.
     */
    public long mCreationTime;

    /**
     * The ID of the scene that the player was on.
     */
    public String mSceneId;

    /**
     * The ID of the frame that the player was on. This ID
     * is only unique within a scene.
     */
    public String mFrameId;

    /**
     * Game-defined save data. Make sure that all non-array
     * keys are strings and that you do not store circular
     * references in this table.
     */
    public LuaTable mCustomState;

    @Override
    public void write(Json json) {
        json.writeValue("save_id", mId);
        json.writeValue("index", mIndex);
        json.writeValue("version", mVersion);
        json.writeValue("time", mCreationTime);
        json.writeValue("scene_id", mSceneId);
        json.writeValue("frame_id", mFrameId);
        json.writeValue("custom_state", mCustomState);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        mId = json.readValue("save_id", long.class, jsonData);
        mIndex = json.readValue("index", int.class, jsonData);
        mVersion = json.readValue("version", int.class, jsonData);
        mCreationTime = json.readValue("time", long.class, jsonData);
        mSceneId = json.readValue("scene_id", String.class, jsonData);
        mFrameId = json.readValue("frame_id", String.class, jsonData);
        mCustomState = json.readValue("custom_state", LuaTable.class, jsonData);
    }
}
