package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaFunction;

public class DropDownFacade extends ActorFacade<SelectBox>{
    private final SelectBox mSelectBox;
    private SelectBox.SelectBoxStyle mSelectBoxStyle;
    private LuaFunction mCallBack;
    private ChangeHandler mChangeHandler;
    private ManagedAsset<String> atLeastSomething;

    public DropDownFacade(BaseScreen screen,String ZipfileLocation,String pathInZip){
        super(screen);
        mChangeHandler=new ChangeHandler();
        //mSelectBoxStyle=new SelectBox.SelectBoxStyle();
        //mSelectBox=new SelectBox(mSelectBoxStyle);
        BitmapFont font = new BitmapFont();
        Drawable selection;
        try{
            selection=null;
            List.ListStyle mListStyle= new List.ListStyle(font, Color.BLUE, Color.RED, selection);
            mSelectBoxStyle = new SelectBox.SelectBoxStyle(new BitmapFont(), Color.BLUE,selection, new ScrollPane.ScrollPaneStyle(), mListStyle);
        } catch (Exception e){
            e.printStackTrace();
        }
        mSelectBox=new SelectBox(mSelectBoxStyle);
    }

    @ExposeToLua
    public void setPosition(float x, float y) {
        mSelectBox.setPosition(x, y);
    }

    @ExposeToLua
    public void setOnChange(LuaFunction callBack) {
        mCallBack = callBack;
        if(mCallBack != null) {
            mSelectBox.addListener(mChangeHandler);
        }else {
            mSelectBox.removeListener(mChangeHandler);
        }
    }


    @Override
    public void dispose() {

    }

    @ExposeToLua
    public String getSelected(){
        if(mSelectBox.getSelected() instanceof String)
            return (String)mSelectBox.getSelected();
        else
            return null;
    }

    @ExposeToLua
    public void setSelected(String item){
        mSelectBox.setSelected(item);
    }

    @ExposeToLua
    public Array getItems(){
        return mSelectBox.getItems();
    }

    private class ChangeHandler extends ChangeListener {
        @Override
        public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            if (mCallBack != null) {
                mCallBack.call();
            }
        }
    }
}
