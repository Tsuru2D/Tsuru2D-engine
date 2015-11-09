package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaTable;

public class ToggleButtonFacade extends ButtonFacade {
    private ManagedAsset<Texture> mChecked, mCheckedHover;

    public ToggleButtonFacade(BaseScreen screen, AssetID styleID) {
        super(screen, styleID);
    }

    @Override
    protected void populateStyle(Button.ButtonStyle style, LuaTable styleTable) {
        super.populateStyle(style, styleTable);

        mChecked = swapStyleImage(styleTable, "checked", mChecked);
        mCheckedHover = swapStyleImage(styleTable, "checkedHover", mCheckedHover);

        style.checked = toDrawable(mChecked);
        style.checkedOver = toDrawable(mCheckedHover);

    }

    @Override
    public void dispose() {
        mChecked = freeAsset(mChecked);
        mCheckedHover = freeAsset(mCheckedHover);
        super.dispose();
    }

    @ExposeToLua
    public void setChecked(boolean checked) {
        getActor().setProgrammaticChangeEvents(false);
        getActor().setChecked(checked);
        getActor().setProgrammaticChangeEvents(true);
    }

}
