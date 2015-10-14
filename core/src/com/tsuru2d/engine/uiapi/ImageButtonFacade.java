package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tsuru2d.engine.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;

public class ImageButtonFacade extends ButtonSuper {
    private ManagedAsset<String> mText;
    private ClickHandler mClickHandler;
    private LuaFunction mCallBack;
    private TextButton.TextButtonStyle mTextButtonStyle;
    private BitmapFont mFont;
    private final ImageButton mImageButton;
    public ImageButtonFacade(BaseScreen screen, LuaTable luaTable,ImageButton.ImageButtonStyle style){
        super(screen, luaTable);
        mFont = new BitmapFont();
        mTextButtonStyle = new TextButton.TextButtonStyle();
        mTextButtonStyle.font = mFont;
        mClickHandler = new ClickHandler();
        mImageButton=new ImageButton(style);
    }

    @Override
    public void dispose(){
        if (mText != null) {
            mScreen.getAssetLoader().freeAsset(mText);
        }
    }

    @Override
    public ImageButton getActor(){
        return mImageButton;
    }

    public void setPosition(float x, float y){
        mImageButton.setPosition(x,y);
    }

    @ExposeToLua
    public void setText(AssetID text) {
        dispose();
        mText = mScreen.getAssetLoader().getText(text);
    }

    @ExposeToLua
    public void setOnClick(LuaFunction callBack) {
        mCallBack = callBack;
        if(mCallBack != null) {
            mImageButton.addListener(mClickHandler);
        }else {
            mImageButton.removeListener(mClickHandler);
        }

    }

    @ExposeToLua
    public void setSize(float width, float height) {
        mImageButton.setSize(width, height);
    }

    private class ClickHandler extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            if(mCallBack != null) {
                mCallBack.call();
            }
        }
    }

}
