package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.tsuru2d.engine.BaseScreen;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public class TableLayout extends Table implements Disposable{
    private BaseScreen mScreen;
    private LuaTable mLuaTable;

    public TableLayout(BaseScreen screen, LuaTable data) {
        super();
        this.mLuaTable = data;
        mScreen = screen;
        setDebug(false);
    }

    public TableLayout(BaseScreen screen, LuaTable data, boolean debug) {
        super();
        this.mLuaTable = data;
        mScreen = screen;
        setDebug(debug);
    }

    private void addClickListener() {
        this.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                LuaValue mScript = mLuaTable.get("onClick");
                if(mScript != null){
                    String parameters = x + " " + y;
                    mScript.call(parameters);
                }
            }
        });
    }

    @ExposeToLua
    public void addCell(Disposable actor) {
        this.add((Actor)actor);
    }

    @ExposeToLua
    public void newRow() {
        this.row();
    }

    @Override
    public void dispose() {
        Array<Cell> mCells = getCells();
        for(Cell mC : mCells) {
            Disposable mUI = (Disposable) mC;
            mUI.dispose();
        }
    }
}
