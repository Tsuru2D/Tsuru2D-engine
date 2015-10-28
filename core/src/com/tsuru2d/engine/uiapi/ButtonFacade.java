package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;

public class ButtonFacade extends ActorFacade<Button> {
    private LuaFunction mOnClickCallback;
    private ManagedAsset<Texture> mUp, mDown, mHover;

    public ButtonFacade(BaseScreen screen) {
        super(screen);
        Button button = createActor();
        setActor(button);
        button.addListener(new OnClickHandler());
    }

    protected Button createActor() {
        return new Button(createStyle());
    }

    protected Button.ButtonStyle createStyle() {
        return new Button.ButtonStyle();
    }

    protected void populateStyle(Button.ButtonStyle style, LuaTable styleTable) {
        mUp = getAsset(styleTable, "up");
        mDown = getAsset(styleTable, "down");
        mHover = getAsset(styleTable, "hover");

        style.up = getDrawable(mUp);
        style.down = getDrawable(mDown);
        style.over = getDrawable(mHover);
    }

    @ExposeToLua
    public void setStyle(LuaTable styleTable) {
        dispose();
        Button.ButtonStyle newStyle = createStyle();
        populateStyle(newStyle, styleTable);
        mActor.setStyle(newStyle);
    }

    @ExposeToLua
    public void setOnClick(LuaFunction callback) {
        mOnClickCallback = callback;
    }

    @Override
    public void dispose() {
        if (mUp != null) {
            mScreen.getAssetLoader().freeAsset(mUp);
            mUp = null;
        }
        if (mDown != null) {
            mScreen.getAssetLoader().freeAsset(mDown);
            mDown = null;
        }
        if (mHover != null) {
            mScreen.getAssetLoader().freeAsset(mHover);
            mHover = null;
        }
    }

    private ManagedAsset<Texture> getAsset(LuaTable style, String key) {
        if (style.get(key).isnil()) {
            return null;
        } else {
            AssetID assetID = (AssetID)style.get(key).checkuserdata(AssetID.class);
            return mScreen.getAssetLoader().getImage(assetID);
        }
    }

    private TextureRegionDrawable getDrawable(ManagedAsset<Texture> texture) {
        if (texture == null) {
            return null;
        }
        return new TextureRegionDrawable(new TextureRegion(texture.get()));
    }

    private class OnClickHandler extends ChangeListener {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            if (mOnClickCallback != null) {
                mOnClickCallback.call();
            }
        }
    }
}
