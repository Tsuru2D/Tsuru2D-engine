package com.tsuru2d.engine.uiapi;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tsuru2d.engine.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public class Label extends com.badlogic.gdx.scenes.scene2d.ui.Label implements Disposable{
    private BaseScreen mScreen;
    private ManagedAsset<String> mText;
    private TextObserver mObserver;
    private final LuaTable mLuaTable;

    public Label(BaseScreen screen, LuaTable data) {
        super("", new Skin()); // new Skin() gets a place holder Skin object
        this.mLuaTable = data;
        mScreen = screen;
        mObserver = new TextObserver();
        addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                LuaValue mScript = mLuaTable.get("onClick");
                if(mScript != null){
                    mScript.call();
                }
            }
        });
    }

    @ExposeToLua
    public void setText(AssetID text) {
        dispose();
        mText = mScreen.getAssetLoader().getText(text);
        mText.addObserver(mObserver);
    }

    @Override
    public void dispose() {
        if (mText != null && mObserver != null) {
            mText.removeObserver(mObserver);
            mScreen.getAssetLoader().freeAsset(mText);
        }
    }

    private class TextObserver implements AssetObserver<String> {

        @Override
        public void onAssetUpdated(ManagedAsset<String> asset) {
            setText(asset.get());
        }
    }
}
