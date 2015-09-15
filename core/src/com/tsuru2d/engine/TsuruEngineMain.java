package com.tsuru2d.engine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tsuru2d.engine.loader.LuaFileLoader;
import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.lua.ExposedJavaClass;
import com.tsuru2d.engine.lua.JavaVarargs;
import com.tsuru2d.engine.util.Xlog;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.util.HashMap;
import java.util.Map;

public class TsuruEngineMain extends ApplicationAdapter {
    private FileHandleResolver mLoader;
    private SpriteBatch batch;
    private Texture img;
    private Globals globals;
    private OrthographicCamera camera;
    private Map<String, Sound> soundMap;

    public TsuruEngineMain(FileHandleResolver fileHandleResolver) {
        mLoader = fileHandleResolver;
    }

    @ExposeToLua
    public void playSound(String name) {
        Sound sound = soundMap.get(name);
        if (sound != null) {
            sound.play();
        } else {
            System.out.println("Error: sound not found: " + name);
        }
    }

    @ExposeToLua(name = "getDimensions")
    public Varargs getDim() {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        return JavaVarargs.of(width, height);
    }

    @ExposeToLua
    public Varargs test(String a, LuaTable b, int c, float d, double e, Double s) {
        Xlog.d("a=%s, b=%s, c=%d, d=%f, e=%f, s=%s", a, b, c, d, e, s);
        return JavaVarargs.of(a, b, c, d, e, s);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        globals = JsePlatform.standardGlobals();
        AssetManager assetManager = new AssetManager(mLoader);
        assetManager.setLoader(LuaValue.class, new LuaFileLoader(mLoader));
        globals.set("engine", new ExposedJavaClass(this));

        assetManager.load("main.lua", LuaValue.class, new LuaFileLoader.LuaFileParameter(globals));
        assetManager.load("badlogic.jpg", Texture.class);
        assetManager.load("sfx.ogg", Sound.class);
        System.out.println(Gdx.graphics.getWidth() + " x " + Gdx.graphics.getHeight());
        assetManager.finishLoading();
        LuaValue res = assetManager.get("main.lua");
        img = assetManager.get("badlogic.jpg");
        soundMap = new HashMap<String, Sound>();
        soundMap.put("sfx", (Sound)assetManager.get("sfx.ogg"));
        globals.get("init").invoke(JavaVarargs.of(img.getWidth(), img.getHeight()));
        camera = new OrthographicCamera();
    }

    @Override
    public void resize(int width, int height) {
        globals.get("onDimensionsChanged").invoke(JavaVarargs.of(width, height));
        camera.setToOrtho(true, width, height);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
    }

    @Override
    public void render() {
        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Buttons.LEFT)) {
            int x = Gdx.input.getX();
            int y = Gdx.input.getY();
            globals.get("onClick").invoke(JavaVarargs.of(x, y));
        }
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        Varargs pos = globals.get("move").invoke();
        float x = pos.arg(1).tofloat();
        float y = pos.arg(2).tofloat();
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, x, y);
        batch.end();
    }
}
