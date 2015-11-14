package com.tsuru2d.engine.io;

import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.lua.ExposedJavaClass;
import org.luaj.vm2.*;

import java.util.ArrayList;
import java.util.Iterator;

public class LuaNetManager extends ExposedJavaClass {
    private final BaseScreen mScreen;
    private final NetManager mNetManager;
    private final GameLocalSaves mLocalSaves;

    private static class LuaCallback implements NetManager.Callback {
        private final LuaFunction mCallback;

        public LuaCallback(LuaFunction callback) {
            mCallback = callback;
        }

        public void runCallback(LuaBoolean success, LuaValue errorCode, LuaValue data) {
            mCallback.call(success, errorCode, data);
        }

        public void runCallback(boolean success, String errorCode, LuaValue data) {
            LuaBoolean luaSuccess = LuaValue.valueOf(success);
            LuaValue luaErrorCode = LuaValue.NIL;
            if (errorCode != null) {
                luaErrorCode = LuaValue.valueOf(errorCode);
            }
            runCallback(luaSuccess, luaErrorCode, data);
        }

        public void runCallback(boolean success, String errorCode) {
            runCallback(success, errorCode, LuaValue.NIL);
        }

        @Override
        public void onResult(NetResult result) {
            runCallback(result.mSuccess, result.mErrorCode);
        }
    }

    public LuaNetManager(BaseScreen screen, NetManager netManager, GameLocalSaves localSaves) {
        mScreen = screen;
        mNetManager = netManager;
        mLocalSaves = localSaves;
    }

    @ExposeToLua
    public boolean isLoggedIn() {
        return mNetManager.isLoggedIn();
    }

    @ExposeToLua
    public void register(String email, String password, LuaFunction callback) {
        mNetManager.register(email, password, new LuaCallback(callback));
    }

    @ExposeToLua
    public void login(String email, String password, LuaFunction callback) {
        mNetManager.login(email, password, new LuaCallback(callback));
    }

    @ExposeToLua
    public void logout() {
        mNetManager.logout();
    }

    @ExposeToLua
    public void enumerateSaves(int startIndex, int endIndex, LuaFunction callback) {
        mNetManager.enumerateSaves(startIndex, endIndex, new LuaCallback(callback) {
            @Override
            public void onResult(NetResult result) {
                if (result.mSuccess) {
                    GameSaveData[] saves = (GameSaveData[])result.mData;
                    LuaTable luaSaves = new LuaTable(saves.length, 0);
                    for (int i = 0; i < saves.length; ++i) {
                        luaSaves.set(i + 1, new LuaUserdata(saves[i]));
                    }
                    mLocalSaves.syncSaveLocal(saves);
                    runCallback(true, result.mErrorCode, luaSaves);
                } else {
                    super.onResult(result);
                }
            }
        });
    }

    @ExposeToLua
    public void writeSave(boolean overwrite, LuaFunction callback) {
//        GameSaveData saveData = mScreen.buildSaveData();
//        saveData.mIndex = index;
        ArrayList<GameSaveData> UnsyncSaves = mLocalSaves.getUnsyncSaves();
        Iterator<GameSaveData> it = UnsyncSaves.iterator();
        while (it.hasNext()) {
            mNetManager.writeSave(overwrite, it.next(), new LuaCallback(callback) {
                @Override
                public void onResult(NetResult result) {
                    if (result.mSuccess) {
                        mLocalSaves.onSyncSuccess();
                    }
                }
            });
        }
    }

    @ExposeToLua
    public void createTempSave() {
        GameSaveData tempSave = mScreen.buildSaveData();
        mLocalSaves.setTempSave(tempSave);
    }

    @ExposeToLua
    public LuaUserdata getTempSave() {
        return new LuaUserdata(mLocalSaves.getTempSave());
    }

    @ExposeToLua
    public void loadSave(int index, LuaFunction callback) {
        GameSaveData saveData = mLocalSaves.load(index);
        if (saveData != null) {
            LuaUserdata save = new LuaUserdata(saveData);
            callback.call(save);
        }
    }

    @ExposeToLua
    public void saveLocal(int index) {
        mLocalSaves.getTempSave().mIndex = index;
        mLocalSaves.unsyncSaveLocal();
    }

    @ExposeToLua
    public void deleteSave(long saveID, LuaFunction callback) {
        // TODO
    }

    @ExposeToLua
    public void readGameSettings(LuaFunction callback) {
        mNetManager.readGameSettings(new LuaCallback(callback) {
            @Override
            public void onResult(NetResult result) {
                if (!result.mSuccess) {
                    runCallback(false, result.mErrorCode);
                } else {
                    LuaTable settings = (LuaTable)result.mData;
                    runCallback(true, result.mErrorCode, settings);
                }
            }
        });
    }

    @ExposeToLua
    public void writeGameSettings(LuaTable settings, final LuaFunction callback) {
        mNetManager.writeGameSettings(settings, new LuaCallback(callback));
    }
}
