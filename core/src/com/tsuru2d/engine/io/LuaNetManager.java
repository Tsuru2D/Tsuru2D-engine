package com.tsuru2d.engine.io;

import com.tsuru2d.engine.EngineMain;
import com.tsuru2d.engine.gameapi.GameScene;
import com.tsuru2d.engine.gameapi.GameScreen;
import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.lua.ExposedJavaClass;
import org.luaj.vm2.*;

import java.util.ArrayList;

public class LuaNetManager extends ExposedJavaClass {
    private final NetManager mNetManager;
    private EngineMain mGame;
    private ArrayList<GameSaveData> mSaveDatas;

    public void setEngineMain(EngineMain game){
        mGame=game;
    }

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

    public LuaNetManager(NetManager netManager) {
        mNetManager = netManager;
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
                if (result.mSuccess){
                    GameSaveData[] data = (GameSaveData[])result.mData;
                    for(GameSaveData save : data){
                        mSaveDatas.add(save);
                    }
                }
                runCallback(result.mSuccess, result.mErrorCode);
            }
        });
    }

    @ExposeToLua
    public void writeSave(int index, boolean isOverWrite, final LuaFunction callback) {
        final GameSaveData newSave = new GameSaveData();
        int version = mGame.getMetadata().mVersionCode;
        GameScreen gameScreen = mGame.getGameScreen();
        LuaTable globals = gameScreen.getGlobalsTable();
        GameScene scene = gameScreen.getScene();
        newSave.mCreationTime=System.currentTimeMillis();
        newSave.mVersion = version;
        newSave.mCustomState = globals;
        newSave.mSceneId = scene.getSceneID().toString();
        newSave.mFrameId = scene.getFrameID();
        newSave.mIndex = index;
        mNetManager.writeSave(newSave, isOverWrite, new LuaCallback(callback) {
            @Override
            public void onResult(NetResult result) {
                if (result.mSuccess){
                    newSave.mId=((Number)result.mData).longValue();
                    mSaveDatas.add(newSave);
                }
                runCallback(result.mSuccess,result.mErrorCode);
            }
        });
    }

    @ExposeToLua
    public void deleteSave() {
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
