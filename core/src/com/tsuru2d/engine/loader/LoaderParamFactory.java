package com.tsuru2d.engine.loader;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public final class LoaderParamFactory {
    private LoaderParamFactory() { }

    @SuppressWarnings("unchecked")
    public static <T> AssetLoaderParameters<T> get(Class<T> rawAssetType) {
        if (rawAssetType.equals(Sound.class)) {
            return (AssetLoaderParameters<T>)new SoundLoader.SoundParameter();
        } else if (rawAssetType.equals(Music.class)) {
            return (AssetLoaderParameters<T>)new MusicLoader.MusicParameter();
        } else if (rawAssetType.equals(Texture.class)) {
            return (AssetLoaderParameters<T>)new TextureLoader.TextureParameter();
        } else if (rawAssetType.equals(LuaTable.class)) {
            return (AssetLoaderParameters<T>)new LuaTableLoader.LuaTableParameter();
        } else if (rawAssetType.equals(LuaValue.class)) {
            return (AssetLoaderParameters<T>)new LuaValueLoader.LuaValueParameter();
        } else {
            throw new IllegalArgumentException("Unknown class type: " + rawAssetType.getName());
        }
    }
}
