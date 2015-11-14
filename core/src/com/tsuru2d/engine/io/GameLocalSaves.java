package com.tsuru2d.engine.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import org.luaj.vm2.LuaTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameLocalSaves {
    private static int MAXSAVESLOTS = 200;
    protected Preferences mSyncSaveList;
    protected Preferences mUnsyncSavesList;
    protected GameSaveData mTempSave;
    protected String mTitle;

    public GameLocalSaves(String title) {
        mSyncSaveList = Gdx.app.getPreferences("syncSavesList");
        mUnsyncSavesList = Gdx.app.getPreferences("unsyncSavesList");
        mTitle = title;
    }

    public void syncSaveLocal(GameSaveData... saves) {
        saveLocal(mSyncSaveList, saves);
    }

    public void unsyncSaveLocal(GameSaveData save) {
        saveLocal(mUnsyncSavesList, save);
    }

    public void unsyncSaveLocal() {
        saveLocal(mUnsyncSavesList, mTempSave);
    }

    private void saveLocal(Preferences saveList, GameSaveData... saves) {
        for (GameSaveData save : saves) {
            int index = createPreference(save);
            saveList.putBoolean("" + index, true);
        }
        saveList.flush();
    }

    public void setTempSave(GameSaveData temp) {
        mTempSave = temp;
    }

    public GameSaveData getTempSave() {
        return mTempSave;
    }

    private int createPreference(GameSaveData save) {
        Preferences newPreference = Gdx.app.getPreferences(mTitle + save.mIndex);
        int index = save.mIndex;
        newPreference.putLong("id", save.mId);
        newPreference.putInteger("index", save.mIndex);
        newPreference.putLong("time", save.mCreationTime);
        newPreference.putInteger("version", save.mVersion);
        newPreference.putString("scene_id", save.mSceneId);
        newPreference.putString("frame_id", save.mFrameId);
        Map<String, LuaTable> customState = new HashMap<String, LuaTable>();
        customState.put("custom_state", save.mCustomState); //TODO this is wrong, but I don't know how to fix it
        newPreference.put(customState);
        newPreference.flush();
        return index;
    }

    public ArrayList<GameSaveData> getUnsyncSaves() {
        ArrayList<GameSaveData> saves = new ArrayList<GameSaveData>();
        for (int i = 0; i < MAXSAVESLOTS; ++i) {
            if (mUnsyncSavesList.contains("" + i)) {
                saves.add(load(i));
            }
        }
        return saves;
    }

    public void onSyncSuccess() {
        for (int index = 0; index < MAXSAVESLOTS; ++index) {
            if (mUnsyncSavesList.contains("" + index)) {
                mSyncSaveList.putBoolean("" + index, true);
            }
        }
        mSyncSaveList.flush();
        mUnsyncSavesList.clear();
        mUnsyncSavesList.flush();
    }

    public GameSaveData load(int index) {
        Preferences pref = Gdx.app.getPreferences(mTitle + index);

        if (pref.contains("index")) {
            GameSaveData save = new GameSaveData();
            save.mCreationTime = pref.getLong("time");
            Map<String, ?> customStateMap = pref.get();
            LuaTable customState = (LuaTable)customStateMap.get("custom_state");
            save.mCustomState = customState;
            save.mFrameId = pref.getString("frame_id");
            save.mIndex = index;
            save.mId = pref.getLong("id");
            save.mSceneId = pref.getString("scene_id");
            save.mVersion = pref.getInteger("version");
            return save;
        } else {
            return null;
        }
    }
}
