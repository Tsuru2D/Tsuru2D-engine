package com.tsuru2d.engine.model;

import com.tsuru2d.engine.loader.AssetID;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;

public class FrameInfo {
    public LuaString mFrameId;
    public LuaTable mSetupInfo;
    public LuaTable mTransformInfo;
    public AssetID mMusicId;
    public AssetID mSoundId;
    public AssetID mVoiceId;
    public LuaTable mCameraInfo;
    public LuaTable mPromptInfo;
}
