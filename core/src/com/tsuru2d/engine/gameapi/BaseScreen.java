package com.tsuru2d.engine.gameapi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tsuru2d.engine.EngineMain;
import com.tsuru2d.engine.io.LuaNetManager;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetLoader;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.lua.ExposedJavaClass;
import com.tsuru2d.engine.uiapi.LuaUIManager;
import com.tsuru2d.engine.uiapi.*;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public abstract class BaseScreen extends ExposedJavaClass implements Screen {
    protected final EngineMain mGame;
    protected final LuaTable mScreenScript;
    protected final Stage mStage;
    private final Skin mSkin;
    private final Table mTable;
    private final TextureRegionDrawable mBackgroundDrawable;
    private ManagedAsset<Texture> mBackgroundTexture;

    public BaseScreen(EngineMain game, LuaTable screenScript) {
        mGame = game;
        mScreenScript = screenScript;
        mStage = new Stage(game.getViewport(), game.getSpriteBatch());
        mBackgroundDrawable = new TextureRegionDrawable(new TextureRegion());
        mSkin = createSkin();
        Table table = new Table();
        table.setDebug(true);
        table.setFillParent(true);
        mStage.addActor(table);
        mTable = table;
        mScreenScript.invokemethod("onCreate", this);
    }

    private static Skin createSkin() {
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
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.background = skin.newDrawable("white", Color.DARK_GRAY);
        labelStyle.font = skin.getFont("default");
        skin.add("default", labelStyle);
        return skin;
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
            mGame.getAssetLoader().freeAsset(mBackgroundTexture);
            mBackgroundTexture = null;
        }
    }

    public AssetLoader getAssetLoader() {
        return mGame.getAssetLoader();
    }

    @Deprecated
    public Skin getSkin() {
        // This method is for TESTING ONLY!
        return mSkin;
    }

    @ExposeToLua
    public LuaNetManager getNetManager() {
        return new LuaNetManager(mGame.getNetManager());
    }

    @ExposeToLua
    public LuaUIManager getUIManager() {
        return new LuaUIManager(this);
    }

    @ExposeToLua
    public CellFacade add(ActorFacade<?> actor) {
        Cell<?> cell = mTable.add(actor.getActor());
        return new CellFacade(cell);
    }

    @ExposeToLua
    public void setBackground(AssetID imageID) {
        ManagedAsset<Texture> texture = mGame.getAssetLoader().getImage(imageID);
        if (mBackgroundTexture != null) {
            // TODO: background asset update handler
            mGame.getAssetLoader().freeAsset(mBackgroundTexture);
        }
        mBackgroundTexture = texture;
        mBackgroundDrawable.getRegion().setRegion(texture.get());
        mTable.background(mBackgroundDrawable);
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
