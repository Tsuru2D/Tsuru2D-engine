package com.tsuru2d.engine.loader;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.tsuru2d.engine.LuaContext;
import org.luaj.vm2.LuaTable;

/**
 * An asset loader for Lua scripts. Will execute the script
 * in the given environment, and return the return value
 * of the script itself.
 */
public class LuaFileLoader extends AsynchronousAssetLoader<LuaTable, LuaFileLoader.LuaFileParameter> {
    public static class LuaFileParameter extends AssetLoaderParameters<LuaTable> {
        public LuaTable mEnvironment;

        public LuaFileParameter() {
            this(null);
        }

        public LuaFileParameter(LuaTable environment) {
            mEnvironment = environment;
        }
    }

    private String mLuaFileContents;

    public LuaFileLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, LuaFileParameter parameter) {
        mLuaFileContents = file.readString();
    }

    @Override
    public LuaTable loadSync(AssetManager manager, String fileName, FileHandle file, LuaFileParameter parameter) {
        LuaTable environment = parameter.mEnvironment;
        if (environment == null) {
            environment = new LuaTable();
        }
        LuaContext.load(mLuaFileContents, fileName, environment);
        return environment;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, LuaFileParameter parameter) {
        return null;
    }
}
