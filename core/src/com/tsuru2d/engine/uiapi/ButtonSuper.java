package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.tsuru2d.engine.BaseScreen;
import org.luaj.vm2.LuaTable;

/*
 * Only the class extends this class can be added to the ButtonGroupFacade.
 */

public abstract class ButtonSuper extends UIWrapper<Button>{
    public ButtonSuper(BaseScreen screen, LuaTable luaTable) {
        super(screen, luaTable);
    }
}
