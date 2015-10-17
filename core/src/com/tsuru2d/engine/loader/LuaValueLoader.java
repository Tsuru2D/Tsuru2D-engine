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
import org.luaj.vm2.LuaValue;

/**
 * An asset loader for Lua scripts. Will execute the script
 * in the given environment, and return the return value
 * of the script itself.
 */
public class LuaValueLoader extends AsynchronousAssetLoader<LuaValue, LuaValueLoader.LuaValueParameter> {
    public static class LuaValueParameter extends AssetLoaderParameters<LuaValue> {
        public LuaTable mEnvironment;

        public LuaValueParameter() {
            this(null);
        }

        public LuaValueParameter(LuaTable environment) {
            mEnvironment = environment;
        }
    }

    private String mLuaFileContents;

    public LuaValueLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, LuaValueParameter parameter) {
        mLuaFileContents = file.readString();
    }

    @Override
    public LuaValue loadSync(AssetManager manager, String fileName, FileHandle file, LuaValueParameter parameter) {
        LuaTable environment = parameter.mEnvironment;
        if (environment == null) {
            environment = new LuaTable();
        }
        return LuaContext.load(mLuaFileContents, fileName, environment);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, LuaValueParameter parameter) {
        return null;
    }
}
