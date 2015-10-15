package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.tsuru2d.engine.BaseScreen;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;

public class DropDownFacade extends UIWrapper<SelectBox>{
    private final SelectBox mSelectBox;
    private SelectBox.SelectBoxStyle mSelectBoxStyle;
    private LuaFunction mCallBack;
    private ChangeHandler mChangeHandler;

    public DropDownFacade(BaseScreen screen,LuaTable luaTable){
        super(screen,luaTable);
        mChangeHandler=new ChangeHandler();
        mSelectBoxStyle=new SelectBox.SelectBoxStyle();
        mSelectBox=new SelectBox(mSelectBoxStyle);
    }
    @Override
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
    SelectBox getActor() {
        return mSelectBox;
    }

    @Override
    public void dispose() {

    }

    @ExposeToLua
    public Actor getSelected(){
        if(mSelectBox.getSelected() instanceof Actor)
            return (Actor)mSelectBox.getSelected();
        else
            return null;
    }

    @ExposeToLua
    public void setSelected(Actor item){
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
