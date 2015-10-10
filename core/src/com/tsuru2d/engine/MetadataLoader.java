package com.tsuru2d.engine;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.StreamUtils;
import com.tsuru2d.engine.loader.AssetFinder;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetType;
import com.tsuru2d.engine.lua.LuaArrayIterator;
import com.tsuru2d.engine.model.MetadataInfo;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public final class MetadataLoader {
    private MetadataLoader() { }

    public static MetadataInfo getMetadata(Globals globals, AssetFinder assetFinder) {
        LuaTable env = loadMetadataFile(globals, assetFinder);
        return parseMetadataFile(env);
    }

    private static MetadataInfo parseMetadataFile(LuaTable env) {
        MetadataInfo info = new MetadataInfo();
        info.mPackageName = env.get("package").checkjstring();
        info.mVersionCode = env.get("versionCode").checkint();
        info.mVersionName = env.get("versionName").checkjstring();
        info.mLanguages = parseLanguagesTable(env.get("languages"));
        info.mLocalizationDir = env.get("localizationPath").checkjstring();
        info.mResolution = env.get("resolution").checkjstring(); // TODO
        info.mAssetDirs = parseAssetDirs(env.get("assetPaths"));
        info.mTitle = (AssetID)env.get("title").checkuserdata();
        info.mAuthor = (AssetID)env.get("author").checkuserdata();
        // TODO: Others
        return info;
    }

    private static Map<AssetType, String> parseAssetDirs(LuaValue value) {
        LuaTable table = value.checktable();
        HashMap<AssetType, String> output = new HashMap<AssetType, String>();
        output.put(AssetType.SOUND, table.get("sound").checkjstring());
        output.put(AssetType.MUSIC, table.get("music").checkjstring());
        output.put(AssetType.VOICE, table.get("voice").checkjstring());
        output.put(AssetType.TEXT, table.get("text").checkjstring());
        output.put(AssetType.SCREEN, table.get("screen").checkjstring());
        output.put(AssetType.SCENE, table.get("scene").checkjstring());
        output.put(AssetType.CHARACTER, table.get("character").checkjstring());
        return output;
    }

    private static String[] parseLanguagesTable(LuaValue value) {
        if (value.isstring()) {
            return new String[] {value.tojstring()};
        }
        LuaTable table = value.checktable();
        String[] languages = new String[table.length()];
        LuaArrayIterator iterator = new LuaArrayIterator(table);
        int i = 0;
        while (iterator.hasNext()) {
            languages[i++] = iterator.next().checkjstring(2);
        }
        return languages;
    }

    private static LuaTable loadMetadataFile(Globals globals, AssetFinder assetFinder) {
        FileHandle metadataHandle = assetFinder.getMetadataHandle();
        LuaTable env = new LuaTable();
        env.set(LuaValue.INDEX, globals);
        env.setmetatable(env);
        InputStream metadataFileStream = metadataHandle.read();
        try {
            globals.load(metadataHandle.read(), "metadata", "t", env).invoke();
        } finally {
            StreamUtils.closeQuietly(metadataFileStream);
        }
        return env;
    }
}
