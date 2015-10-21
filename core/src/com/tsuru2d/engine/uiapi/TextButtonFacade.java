package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaFunction;

public class TextButtonFacade extends ActorFacade<TextButton> {
    private final AssetUpdatedObserver mAssetUpdatedObserver;
    private ManagedAsset<String> mText;
    private LuaFunction mOnClickCallback;

    public TextButtonFacade(BaseScreen screen) {
        super(screen, new TextButton(null, screen.getSkin()));
        mActor.addListener(new OnClickHandler());
        mAssetUpdatedObserver = new AssetUpdatedObserver();
    }

    @ExposeToLua
    public void setText(AssetID text) {
        ManagedAsset<String> oldText = mText;
        mText = mScreen.getAssetLoader().getText(text);
        if (oldText != null) {
            // In the case that oldText == newText, this won't be
            // a problem since the reference count will be at least
            // 2 anyways, and we just remove then re-add the observer
            oldText.removeObserver(mAssetUpdatedObserver);
            mScreen.getAssetLoader().freeAsset(oldText);
        }
        mText.addObserver(mAssetUpdatedObserver);
        mActor.setText(mText.get());
    }

    @ExposeToLua
    public void setOnClick(LuaFunction callback) {
        mOnClickCallback = callback;
    }

    @Override
    public void dispose() {
        if (mText != null) {
            mText.removeObserver(mAssetUpdatedObserver);
            mScreen.getAssetLoader().freeAsset(mText);
        }
    }

    private class OnClickHandler extends ChangeListener {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            if (mOnClickCallback != null) {
                mOnClickCallback.call();
            }
        }
    }

    private class AssetUpdatedObserver implements AssetObserver<String> {
        @Override
        public void onAssetUpdated(ManagedAsset<String> asset) {
            mActor.setText(asset.get());
        }
    }
}