package com.tsuru2d.engine.loader;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StreamUtils;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;

import java.io.InputStream;

/**
 * An asset loader for Lua scripts. Will execute the script
 * in the given environment, and return the return value
 * of the script itself.
 */
public class LuaFileLoader extends SynchronousAssetLoader<LuaValue, LuaFileLoader.LuaFileParameter> {
    public static class LuaFileParameter extends AssetLoaderParameters<LuaValue> {
        public final Globals mGlobals;
        public final LuaValue mEnvironment;

        public LuaFileParameter(Globals globals) {
            this(globals, globals);
        }

        public LuaFileParameter(Globals globals, LuaValue environment) {
            mGlobals = globals;
            mEnvironment = environment;
        }
    }

    public LuaFileLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public LuaValue load(AssetManager assetManager, String fileName, FileHandle file, LuaFileParameter parameter) {
        Globals globals = parameter.mGlobals;
        LuaValue environment = parameter.mEnvironment;
        InputStream scriptStream = file.read();
        try {
            return globals.load(scriptStream, fileName, "t", environment).call();
        } finally {
            StreamUtils.closeQuietly(scriptStream);
        }
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, LuaFileParameter parameter) {
        return null;
    }
}
