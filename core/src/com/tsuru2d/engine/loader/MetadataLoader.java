package com.tsuru2d.engine.loader;

import com.tsuru2d.engine.model.GameMetadataInfo;
import com.tsuru2d.engine.model.LangMetadataInfo;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MetadataLoader {
    public static class Resolution {
        private final int mWidth;
        private final int mHeight;

        public Resolution(int width, int height) {
            mWidth = width;
            mHeight = height;
        }

        public int getWidth() {
            return mWidth;
        }

        public int getHeight() {
            return mHeight;
        }
    }

    private MetadataLoader() { }

    public static LangMetadataInfo parseLangMetadata(LuaTable env) {
        LangMetadataInfo info = new LangMetadataInfo();
        info.mTargetPackageName = env.get("targetPackage").checkjstring();
        info.mTargetVersionCode = env.get("targetVersionCode").checkint();
        info.mLanguageCode = env.get("languageCode").checkjstring();
        info.mLanguageName = env.get("languageName").checkjstring();
        return info;
    }

    public static GameMetadataInfo parseGameMetadata(LuaTable env) {
        GameMetadataInfo info = new GameMetadataInfo();
        info.mPackageName = env.get("package").checkjstring();
        info.mVersionCode = env.get("versionCode").checkint();
        info.mVersionName = env.get("versionName").checkjstring();
        info.mResolution = parseResolution(env.get("resolution"));
        info.mLanguageCode = env.get("languageCode").optjstring(null);
        info.mLanguageName = env.get("languageName").optjstring(null);
        info.mAssetDirs = parseAssetDirs(env.get("assetPaths"));
        info.mTitle = (AssetID)env.get("title").checkuserdata();
        info.mAuthor = (AssetID)env.get("author").checkuserdata();
        info.mMainScreen = (AssetID)env.get("mainScreen").checkuserdata();
        info.mGameScreen = (AssetID)env.get("gameScreen").checkuserdata();
        return info;
    }

    private static Resolution parseResolution(LuaValue value) {
        String resolution = value.checkjstring();
        Pattern pattern = Pattern.compile("(\\d+)x(\\d+)");
        Matcher matcher = pattern.matcher(resolution);
        if (matcher.matches()) {
            int width = Integer.parseInt(matcher.group(1));
            int height = Integer.parseInt(matcher.group(2));
            return new Resolution(width, height);
        } else {
            throw new IllegalArgumentException("Invalid resolution string: " + resolution);
        }
    }

    private static Map<AssetType, String> parseAssetDirs(LuaValue value) {
        LuaTable table = value.checktable();
        HashMap<AssetType, String> output = new HashMap<AssetType, String>();
        output.put(AssetType.SOUND, table.get("sound").checkjstring());
        output.put(AssetType.MUSIC, table.get("music").checkjstring());
        output.put(AssetType.VOICE, table.get("voice").checkjstring());
        output.put(AssetType.IMAGE, table.get("image").checkjstring());
        output.put(AssetType.TEXT, table.get("text").checkjstring());
        output.put(AssetType.SCREEN, table.get("screen").checkjstring());
        output.put(AssetType.SCENE, table.get("scene").checkjstring());
        output.put(AssetType.CHARACTER, table.get("character").checkjstring());
        return output;
    }
}
