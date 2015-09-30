package com.tsuru2d.engine.android;

import android.os.Bundle;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.tsuru2d.engine.TsuruEngineMain;

public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useImmersiveMode = true;
        InternalFileHandleResolver resolver = new InternalFileHandleResolver();
        // AssetFinder assetFinder = new AndroidInternalFileFinder(getAssets());
        initialize(new TsuruEngineMain(resolver, null), config);
    }
}
