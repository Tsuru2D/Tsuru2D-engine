package com.tsuru2d.engine.loader;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public final class LoaderParamFactory {
    private LoaderParamFactory() { }

    @SuppressWarnings("unchecked")
    public static <T> AssetLoaderParameters<T> get(Class<T> rawAssetType) {
        if (rawAssetType.equals(Sound.class)) {
            return (AssetLoaderParameters<T>)new SoundLoader.SoundParameter();
        } else if (rawAssetType.equals(Music.class)) {
            return (AssetLoaderParameters<T>)new MusicLoader.MusicParameter();
        } else {
            throw new IllegalArgumentException("Unknown class type: " + rawAssetType.getName());
        }
    }
}
