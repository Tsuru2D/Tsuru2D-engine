package com.tsuru2d.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetLoader;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.lua.ExposedJavaClass;
import org.luaj.vm2.LuaTable;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseScreen extends ExposedJavaClass implements Screen {
    protected final EngineMain mGame;
    protected final Stage mStage;
    protected final LuaTable mLuaEnvironment;
    private final List<ManagedAsset<?>> mLoadedAssets;

    public BaseScreen(EngineMain game) {
        mGame = game;
        mStage = new Stage(game.getViewport(), game.getSpriteBatch());
        mLuaEnvironment = new LuaTable();
        mLoadedAssets = new ArrayList<ManagedAsset<?>>();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(mStage);
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

    }

    @Override
    public void dispose() {

    }

    public float getWidth() {
        return mStage.getWidth();
    }

    public AssetLoader getAssetLoader() {
        return mGame.getAssetLoader();
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
    private void setScreen(AssetID id) {
        mGame.setScreen(id);
    }

    @ExposeToLua
    private void pushScreen(AssetID id) {
        mGame.pushScreen(id);
    }

    @ExposeToLua
    private void popScreen() {
        mGame.popScreen();
    }
}
