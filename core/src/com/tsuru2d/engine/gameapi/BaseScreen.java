package com.tsuru2d.engine.gameapi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.tsuru2d.engine.EngineMain;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetLoader;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.lua.ExposedJavaClass;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseScreen extends ExposedJavaClass implements Screen {
    protected final EngineMain mGame;
    protected final Stage mStage;
    private final LuaTable mScreenScript;
    private final Table mTable;
    private final Skin mSkin;
    private final List<ManagedAsset<?>> mLoadedAssets;

    public BaseScreen(EngineMain game, LuaTable screenScript) {
        mGame = game;
        mStage = new Stage(game.getViewport(), game.getSpriteBatch());
        mScreenScript = screenScript;
        mLoadedAssets = new ArrayList<ManagedAsset<?>>();

        Skin skin = new Skin();
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        skin.add("default", new BitmapFont());
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);
        mSkin = skin;

        Table table = new Table();
        table.setFillParent(true);
        mStage.addActor(table);
        mTable = table;
        mScreenScript.invokemethod("onCreate", this);
    }

    public void show(LuaValue params) {
        if (params == null) {
            params = LuaValue.NIL;
        }
        Gdx.input.setInputProcessor(mStage);
        mScreenScript.invokemethod("onResume", params);
    }

    @Override
    public void show() {
        show(LuaValue.NIL);
    }

    @Override
    public void render(float delta) {
        mStage.act(delta);
        mStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        mStage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        mScreenScript.invokemethod("onPause");
    }

    @Override
    public void dispose() {
        mScreenScript.invokemethod("onDestroy");
    }

    public float getWidth() {
        return mStage.getWidth();
    }

    public AssetLoader getAssetLoader() {
        return mGame.getAssetLoader();
    }

    @ExposeToLua
    public void newButton(String text, final LuaFunction clickCallback) {
        TextButton button = new TextButton(text, mSkin);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                clickCallback.call();
            }
        });
        mTable.add(button);
        mTable.row();
    }

    @ExposeToLua
    public void playMusic(AssetID musicID) {
        mGame.playMusic(musicID);
    }

    @ExposeToLua
    public void setLanguage(String languageCode) {
        mGame.setLanguage(languageCode);
    }

    @ExposeToLua
    public void setScreen(AssetID id, LuaValue params) {
        mGame.setScreen(id, params);
    }

    @ExposeToLua
    public void pushScreen(AssetID id, LuaValue params) {
        mGame.pushScreen(id, params);
    }

    @ExposeToLua
    public void popScreen(LuaValue params) {
        mGame.popScreen(params);
    }
}
