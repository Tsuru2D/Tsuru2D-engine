package com.tsuru2d.engine.io;

import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.lua.ExposedJavaClass;
import org.luaj.vm2.*;

public class LuaNetManager extends ExposedJavaClass {
    private final NetManager mNetManager;

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
                // GameSaveData[] data = (GameSaveData[])result.mData;
                // TODO
                runCallback(result.mSuccess, result.mErrorCode);
            }
        });
    }

    @ExposeToLua
    public void writeSave() {
        // TODO
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
