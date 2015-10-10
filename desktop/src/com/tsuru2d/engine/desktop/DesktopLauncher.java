package com.tsuru2d.engine.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tsuru2d.engine.EngineMain;
import com.tsuru2d.engine.loader.zip.ZipAssetFinder;

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
        ZipAssetFinder finder = new ZipAssetFinder(zipFile);
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new EngineMain(finder), config);
    }
}
