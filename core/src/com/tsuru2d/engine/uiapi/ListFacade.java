package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Array;
import com.tsuru2d.engine.BaseScreen;
import org.luaj.vm2.LuaTable;

public class ListFacade extends UIWrapper<List>{
    private List.ListStyle mListStyle;
    private Array mArray;
    private final List mList;

    public ListFacade(BaseScreen screen, LuaTable luaTable) {
        super(screen, luaTable);
        mListStyle = new List.ListStyle();
        mList = new List(mListStyle);
    }

    @Override
    public List getActor() {
        return mList;
    }

    @Override
    public void setPosition(float x, float y) {
        mList.setPosition(x, y);
    }

    @Override
    public void dispose() {

    }

}
