package com.tsuru2d.engine.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tsuru2d.engine.EngineMain;
import com.tsuru2d.engine.PlatformApi;
import com.tsuru2d.engine.loader.RawAssetLoader;
import com.tsuru2d.engine.loader.zip.ZipRawAssetLoader;

import java.io.File;

public class DesktopLauncher {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No game file specified");
        }
        File gameZipFile = new File(args[0]);
        RawAssetLoader rawAssetLoader = new ZipRawAssetLoader(gameZipFile, null);
        PlatformApi api = new PlatformApi(null, null, rawAssetLoader);
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new EngineMain(api), config);
    }
}
