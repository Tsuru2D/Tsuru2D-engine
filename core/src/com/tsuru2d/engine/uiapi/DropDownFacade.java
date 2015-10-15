package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.tsuru2d.engine.BaseScreen;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaTable;

public class DropDownFacade<T> extends UIWrapper<SelectBox>{
    SelectBox<T>mSelectBox;

    public DropDownFacade(BaseScreen screen,LuaTable luaTable,SelectBox.SelectBoxStyle style){
        super(screen,luaTable);
        mSelectBox=new SelectBox<T>(style);
    }

    @Override
    SelectBox getActor() {
        return mSelectBox;
    }

    @Override
    public void dispose() {
        mSelectBox=null;
    }

    @ExposeToLua
    public T getSelected(){
        return mSelectBox.getSelected();
    }

    @ExposeToLua
    public void setSelected(T item){
        mSelectBox.setSelected(item);
    }

    @ExposeToLua
    public List<T> getList(){
        return mSelectBox.getList();
    }
}
