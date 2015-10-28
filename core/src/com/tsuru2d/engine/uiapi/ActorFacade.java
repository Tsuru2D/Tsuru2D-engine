package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposedJavaClass;
import org.luaj.vm2.LuaTable;

public abstract class ActorFacade<T extends Actor> extends ExposedJavaClass implements Disposable {
    protected final BaseScreen mScreen;
    protected T mActor;

    public ActorFacade(BaseScreen screen) {
        mScreen = screen;
    }

    protected void setActor(T actor) {
        mActor = actor;
    }

    public T getActor() {
        return mActor;
    }

    protected ManagedAsset<Texture> getStyleAsset(LuaTable style, String key) {
        if (style.get(key).isnil()) {
            return null;
        } else {
            AssetID assetID = (AssetID)style.get(key).checkuserdata(AssetID.class);
            return mScreen.getAssetLoader().getImage(assetID);
        }
    }

    @Override
    public void dispose() {

    }
}
