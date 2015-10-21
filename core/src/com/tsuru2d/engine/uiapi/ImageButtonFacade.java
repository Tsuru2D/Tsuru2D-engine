package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaFunction;

public class ImageButtonFacade extends UIWrapper<Button> {
    private ClickHandler mClickHandler;
    private LuaFunction mCallBack;
    private final ImageButton mImageButton;
    private ImageButton.ImageButtonStyle mImageButtonStyle;
    public ImageButtonFacade(BaseScreen screen){
        super(screen);
        mClickHandler = new ClickHandler();
        mImageButtonStyle=new ImageButton.ImageButtonStyle();
        mImageButton=new ImageButton(mImageButtonStyle);
    }

    @Override
    public void dispose(){

    }

    @Override
    public ImageButton getActor(){
        return mImageButton;
    }

    public void setPosition(float x, float y){
        mImageButton.setPosition(x,y);
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
