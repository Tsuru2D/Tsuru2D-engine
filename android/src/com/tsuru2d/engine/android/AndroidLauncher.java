package com.tsuru2d.engine.android;

import android.os.Bundle;
import android.os.Environment;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.tsuru2d.engine.EngineMain;
import com.tsuru2d.engine.PlatformApi;
import com.tsuru2d.engine.loader.RawAssetLoader;
import com.tsuru2d.engine.loader.zip.ZipRawAssetLoader;

import java.io.File;

public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useImmersiveMode = true;
        File sdCard = Environment.getExternalStorageDirectory();
        File gameZipFile = new File(sdCard, "Tsuru2D/com.oxycode.myvisualnovel.vngame");
        RawAssetLoader rawAssetLoader = new ZipRawAssetLoader(gameZipFile, null);
        PlatformApi api = new PlatformApi(null, null, rawAssetLoader);
        initialize(new EngineMain(api), config);
    }
}
