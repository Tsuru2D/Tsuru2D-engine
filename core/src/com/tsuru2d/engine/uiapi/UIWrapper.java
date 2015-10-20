package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.util.DrawableLoader;

public abstract class UIWrapper<T extends Actor> implements Disposable {
    protected final BaseScreen mScreen;
    protected DrawableLoader mDrawableLoader;

    abstract T getActor();

    @ExposeToLua
    public void setPosition(float x, float y) {
        getActor().setPosition(x, y);
    }

    @ExposeToLua
    public void setZipPath(AssetID zipPath) {
        try {
            mDrawableLoader =
                    DrawableLoader.getDrawableLoader(mScreen.getAssetLoader().getText(zipPath).get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UIWrapper(BaseScreen screen) {
        mScreen = screen;
    }

}
