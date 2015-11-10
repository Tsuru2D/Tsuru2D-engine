package com.tsuru2d.engine.gameapi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tsuru2d.engine.EngineMain;
import com.tsuru2d.engine.io.GameSaveData;
import com.tsuru2d.engine.io.LuaNetManager;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetLoader;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.lua.ExposedJavaClass;
import com.tsuru2d.engine.uiapi.LuaUIManager;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public abstract class BaseScreen extends ExposedJavaClass implements Screen {
    protected final EngineMain mGame;
    protected final LuaTable mScreenScript;
    protected final Stage mStage;
    protected Table mRootTable;
    protected Table mUITable;
    private LuaNetManager mNetManager;
    private LuaUIManager mUIManager;
    private TextureRegionDrawable mBackground;
    private ManagedAsset<Texture> mBackgroundTexture;

    public BaseScreen(EngineMain game, LuaTable screenScript) {
        mGame = game;
        mScreenScript = screenScript;
        mStage = new Stage(game.getViewport(), game.getSpriteBatch());
        mNetManager = new LuaNetManager(this, game.getNetManager());
        mBackground = new TextureRegionDrawable(new TextureRegion());
    }

    public void inititialize() {
        mUIManager = new LuaUIManager(this, mUITable);
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
        if (mBackgroundTexture != null) {
            getAssetLoader().freeAsset(mBackgroundTexture);
            mBackgroundTexture = null;
        }
        mUIManager.dispose();
    }

    public AssetLoader getAssetLoader() {
        return mGame.getAssetLoader();
    }

    public GameSaveData buildSaveData() {
        return mGame.buildSaveData();
    }

    @ExposeToLua
    public LuaNetManager getNetManager() {
        return mNetManager;
    }

    @ExposeToLua
    public LuaUIManager getUIManager() {
        return mUIManager;
    }

    @ExposeToLua
    public void setBackground(AssetID imageID) {
        ManagedAsset<Texture> texture = getAssetLoader().getImage(imageID);
        if (mBackgroundTexture != null) {
            // TODO: background asset update handler
            getAssetLoader().freeAsset(mBackgroundTexture);
        }
        mBackgroundTexture = texture;
        mBackground.getRegion().setRegion(texture.get());
        mRootTable.background(mBackground);
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
    public void setMenuScreen(AssetID id, LuaValue params) {
        mGame.setMenuScreen(id, params);
    }

    @ExposeToLua
    public void pushMenuScreen(AssetID id, LuaValue params) {
        mGame.pushMenuScreen(id, params);
    }

    @ExposeToLua
    public void popScreen(LuaValue params) {
        mGame.popScreen(params);
    }
}
