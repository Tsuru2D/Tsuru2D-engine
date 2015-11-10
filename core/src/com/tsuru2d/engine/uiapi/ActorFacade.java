package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.AssetType;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposedJavaClass;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public abstract class ActorFacade<TActor extends Actor, TStyle> extends ExposedJavaClass implements Disposable, StyleNames {
    private final BaseScreen mScreen;
    private final ManagedAsset<LuaTable> mStyleTable;
    private TActor mActor;

    public ActorFacade(BaseScreen screen, AssetID styleID) {
        mScreen = screen;
        if (styleID != null) {
            mStyleTable = screen.getAssetLoader().getSkin(styleID);
        } else {
            mStyleTable = null;
        }
    }

    public void initialize() {
        TStyle style = createStyle();
        if (style != null) {
            populateStyle(style, getStyleTable());
        }
        TActor actor = createActor(style);
        initializeActor(actor);
        mActor = actor;
    }

    protected void initializeActor(TActor actor) {
        // Override this method if you need to perform
        // additional initialization of the actor, such as
        // adding observers or setting default properties.
    }

    public TActor getActor() {
        return mActor;
    }

    protected LuaTable getStyleTable() {
        return mStyleTable.get();
    }

    protected <U> ManagedAsset<U> getAsset(AssetType assetType, AssetID id) {
        if (id == null) {
            return null;
        }
        return mScreen.getAssetLoader().getAsset(assetType, id);
    }

    protected BaseScreen getScreen() {
        return mScreen;
    }

    protected <U> ManagedAsset<U> swapAsset(AssetType assetType, AssetID id,
                                            ManagedAsset<U> oldAsset, AssetObserver<U> observer) {
        ManagedAsset<U> newAsset = getAsset(assetType, id);
        if (oldAsset != null) {
            freeAsset(oldAsset, observer);
        }
        if (newAsset != null && observer != null) {
            newAsset.addObserver(observer);
        }
        return newAsset;
    }

    protected <U> ManagedAsset<U> swapAsset(AssetType assetType, AssetID id, ManagedAsset<U> oldAsset) {
        return swapAsset(assetType, id, oldAsset, null);
    }

    protected ManagedAsset<Texture> swapStyleImage(LuaTable styleTable, String key, ManagedAsset<Texture> oldAsset) {
        return swapAsset(AssetType.IMAGE, getAssetID(styleTable, key), oldAsset);
    }

    protected <U> ManagedAsset<U> freeAsset(ManagedAsset<U> asset, AssetObserver<U> observer) {
        if (asset != null && observer != null) {
            asset.removeObserver(observer);
        }
        if (asset != null) {
            mScreen.getAssetLoader().freeAsset(asset);
        }
        return null;
    }

    protected <U> ManagedAsset<U> freeAsset(ManagedAsset<U> asset) {
        return freeAsset(asset, null);
    }

    @Override
    public void dispose() {
        freeAsset(mStyleTable);
    }

    protected abstract TStyle createStyle();
    protected abstract void populateStyle(TStyle style, LuaTable styleTable);
    protected abstract TActor createActor(TStyle style);

    protected static String getText(ManagedAsset<String> text, Object[] formatParams) {
        if (text == null) {
            return null;
        }
        String textStr = text.get();
        if (formatParams != null) {
            textStr = String.format(textStr, formatParams);
        }
        return textStr;
    }

    protected static AssetID getAssetID(LuaTable styleTable, String key) {
        LuaValue value = styleTable.get(key);
        if (value.isnil()) {
            return null;
        } else {
            return (AssetID)value.checkuserdata(AssetID.class);
        }
    }

    protected static TextureRegionDrawable toDrawable(ManagedAsset<Texture> image) {
        // TODO: Once we have support for transparently loading 9patches, this
        // should be removed and getImage() should directly return a managed Drawable.
        if (image == null) {
            return null;
        }
        return new TextureRegionDrawable(new TextureRegion(image.get()));
    }

    protected static Color tableToColor(LuaValue colorValue) {
        if (colorValue.isnil()) {
            return null;
        }
        LuaTable colorTable = colorValue.checktable();
        float r = getfloat(colorTable, 1);
        float g = getfloat(colorTable, 2);
        float b = getfloat(colorTable, 3);
        float a = getoptfloat(colorTable, 4, 1);
        return new Color(r, g, b, a);
    }

    private static float checkfloat(LuaValue value) {
        return (float)value.checkdouble();
    }

    private static float getfloat(LuaTable table, int index) {
        LuaValue value = table.get(index);
        return checkfloat(value);
    }

    private static float getoptfloat(LuaTable table, int index, float defaultValue) {
        LuaValue value = table.get(index);
        if (value.isnil()) {
            return defaultValue;
        }
        return checkfloat(value);
    }
}
