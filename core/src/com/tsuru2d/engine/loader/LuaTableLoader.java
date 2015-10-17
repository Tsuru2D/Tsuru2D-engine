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
 * in the given environment, and return the *environment*
 * in which the script was executed.
 */
public class LuaTableLoader extends AsynchronousAssetLoader<LuaTable, LuaTableLoader.LuaTableParameter> {
    public static class LuaTableParameter extends AssetLoaderParameters<LuaTable> {
        public LuaTable mEnvironment;

        public LuaTableParameter() {
            this(null);
        }

        public LuaTableParameter(LuaTable environment) {
            mEnvironment = environment;
        }
    }

    private String mLuaFileContents;

    public LuaTableLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, LuaTableParameter parameter) {
        mLuaFileContents = file.readString();
    }

    @Override
    public LuaTable loadSync(AssetManager manager, String fileName, FileHandle file, LuaTableParameter parameter) {
        LuaTable environment = parameter.mEnvironment;
        if (environment == null) {
            environment = new LuaTable();
        }
        LuaContext.load(mLuaFileContents, fileName, environment);
        return environment;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, LuaTableParameter parameter) {
        return null;
    }
}
