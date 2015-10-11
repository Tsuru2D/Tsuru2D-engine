package com.tsuru2d.engine.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tsuru2d.engine.EngineMain;
import com.tsuru2d.engine.loader.RawAssetLoader;
import com.tsuru2d.engine.loader.zip.ZipRawAssetLoader;
import com.tsuru2d.engine.PlatformApi;

import java.io.IOException;
import java.util.zip.ZipFile;

public class DesktopLauncher {
    public static void main(String[] args) {
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(args[0]);
        } catch (IOException e) {
            throw new RuntimeException("Could not load game data");
        }
        final RawAssetLoader rawAssetLoader = new ZipRawAssetLoader(zipFile);
        PlatformApi api = new PlatformApi(null, null, rawAssetLoader);
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new EngineMain(api), config);
    }
}
