package com.tsuru2d.engine.gameapi;

import com.tsuru2d.engine.loader.LuaAssetIDLib;
import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.lua.ExposedJavaClass;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaThread;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

public class LuaTest {
    private boolean inccounter = false;
    private int counter = 0;
    private Token mToken;
    private LuaValue gamethread;
    private LuaThread mThread;

    private class Token extends ExposedJavaClass {
        private Globals mGlobals;

        public Token(Globals globals) {
            mGlobals = globals;
        }

        @ExposeToLua(name = "wait")
        public LuaValue myWait() {
            // coroutine.yield()
            System.out.println("wait() called");
            LuaValue value = mGlobals.get("coroutine").get("yield").call();
            return value;
        }
    }

    private synchronized void run() {
        final Globals globals = JsePlatform.standardGlobals();
        globals.load(new LuaAssetIDLib());
        globals.set("text", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                System.out.println("text() called");
                final Token token = new Token(globals);
                // final LuaUserdata userdata = new ExposedJavaClass(token);
                mToken = token;
                inccounter = true;
                return token;
            }
        });

        LuaThread thread = new LuaThread(globals, new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                System.out.println("Start");
                // text().wait() -> "nyaa!"
                LuaValue value = globals.get("text").call().invokemethod("wait").arg1();
                System.out.println(value.checkjstring());
                return LuaValue.NIL;
            }
        });
        thread.resume(LuaValue.NIL);
        mThread = thread;

        while (true) {
            if (inccounter) {
                counter++;
                try {
                    wait(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (counter == 100) {
                    System.out.println("resume() called");
                    // couroutine.resume(co, "nyaa!")
                    mThread.resume(LuaValue.valueOf("nyaa!"));
                    inccounter = false;
                    counter = 0;
                    return;
                }
            }
        }
    }

    public static void main(String[] args) {
        new LuaTest().run();
    }
}
