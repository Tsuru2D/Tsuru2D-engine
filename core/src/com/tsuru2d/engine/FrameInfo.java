package com.tsuru2d.engine;

import com.tsuru2d.engine.loader.AssetID;
import org.luaj.vm2.LuaTable;

public class FrameInfo {
    public String mFrameId;
    public LuaTable mSetupInfo;
    public LuaTable mTransformInfo;
    public AssetID mMusicId;
    public AssetID mSoundId;
    public AssetID mVoiceId;
    public LuaTable mCameraInfo;
    public LuaTable mPromptInfo;

    public void fromTable(LuaTable table) {
        mFrameId = table.get("id").checkjstring();
        // TODO
    }
}
