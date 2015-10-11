package com.tsuru2d.engine;

import com.tsuru2d.engine.loader.LuaAssetIDLib;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.Bit32Lib;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseMathLib;

public final class LuaContext {
    private static Globals sGlobals;
    static {
        Globals globals = new Globals();
        globals.load(new JseBaseLib());
        globals.load(new PackageLib());
        globals.load(new Bit32Lib());
        globals.load(new TableLib());
        globals.load(new StringLib());
        globals.load(new JseMathLib());
        globals.load(new LuaAssetIDLib());
        LoadState.install(globals);
        LuaC.install(globals);
        sGlobals = globals;
    }

    private LuaContext() { }

    public static LuaValue load(String luaScript, String chunkName, LuaTable environment) {
        return sGlobals.load(luaScript, chunkName, environment).call();
    }
}
