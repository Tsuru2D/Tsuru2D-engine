package com.tsuru2d.engine.gameapi;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.tsuru2d.engine.loader.LuaAssetID;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.lua.ExposedJavaClass;
import com.tsuru2d.engine.lua.LuaMapIterator;
import com.tsuru2d.engine.util.Xlog;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.util.HashMap;
import java.util.Iterator;

public class GameActor extends ExposedJavaClass implements Disposable {
    private static final HashMap<String, Interpolation> sInterpolationMap;
    static {
        sInterpolationMap = new HashMap<String, Interpolation>();
        sInterpolationMap.put("linear", null);
        sInterpolationMap.put("pow2", Interpolation.pow2);
        sInterpolationMap.put("pow2In", Interpolation.pow2In);
        sInterpolationMap.put("pow2Out", Interpolation.pow2Out);
        sInterpolationMap.put("pow3", Interpolation.pow3);
        sInterpolationMap.put("pow3In", Interpolation.pow3In);
        sInterpolationMap.put("pow3Out", Interpolation.pow3Out);
        sInterpolationMap.put("pow4", Interpolation.pow4);
        sInterpolationMap.put("pow4In", Interpolation.pow4In);
        sInterpolationMap.put("pow4Out", Interpolation.pow4Out);
        sInterpolationMap.put("pow5", Interpolation.pow3);
        sInterpolationMap.put("pow5In", Interpolation.pow5In);
        sInterpolationMap.put("pow5Out", Interpolation.pow5Out);
        sInterpolationMap.put("exp10", Interpolation.exp10);
        sInterpolationMap.put("exp10In", Interpolation.exp10In);
        sInterpolationMap.put("exp10Out", Interpolation.exp10Out);
        sInterpolationMap.put("exp5", Interpolation.exp5);
        sInterpolationMap.put("exp5In", Interpolation.exp5In);
        sInterpolationMap.put("exp5Out", Interpolation.exp5Out);
        sInterpolationMap.put("elastic", Interpolation.elastic);
        sInterpolationMap.put("elasticIn", Interpolation.elasticIn);
        sInterpolationMap.put("elasticOut", Interpolation.elasticOut);
        sInterpolationMap.put("swing", Interpolation.swing);
        sInterpolationMap.put("swingIn", Interpolation.swingIn);
        sInterpolationMap.put("swingOut", Interpolation.swingOut);
        sInterpolationMap.put("bounce", Interpolation.bounce);
        sInterpolationMap.put("bounceIn", Interpolation.bounceIn);
        sInterpolationMap.put("bounceOut", Interpolation.bounceOut);
        sInterpolationMap.put("sine", Interpolation.sine);
        sInterpolationMap.put("sineIn", Interpolation.sineIn);
        sInterpolationMap.put("sineOut", Interpolation.sineOut);
        sInterpolationMap.put("circle", Interpolation.circle);
        sInterpolationMap.put("circleIn", Interpolation.circleIn);
        sInterpolationMap.put("circleOut", Interpolation.circleOut);
    }

    private final GameScreen mScreen;
    private final LuaTable mActorScript;
    private final Image mActor;
    private ManagedAsset<Texture> mTexture;

    public GameActor(GameScreen screen, LuaTable actorScript) {
        mScreen = screen;
        mActorScript = actorScript;
        mActor = new Image();
        mActor.setZIndex(1);
        mActor.setDebug(true);
        mActorScript.invokemethod("onCreate", this);
    }

    public Actor getActor() {
        return mActor;
    }

    @ExposeToLua
    public void setTexture(LuaAssetID imageID) {
        ManagedAsset<Texture> newTexture = mScreen.getAssetLoader().getImage(imageID.userdata());
        dispose();
        mTexture = newTexture;
        mActor.setDrawable(new TextureRegionDrawable(new TextureRegion(mTexture.get())));
        mActor.setSize(mActor.getPrefWidth(), mActor.getPrefHeight());
        mActor.setAlign(Align.center);
        mActor.setOrigin(Align.center);
        mActor.layout();
    }

    public void transform(LuaTable transformation) {
        Iterator<Varargs> it = new LuaMapIterator(transformation);
        while (it.hasNext()) {
            Varargs transformKvp = it.next();
            String key = transformKvp.arg(1).checkjstring();
            LuaValue rawValue = transformKvp.arg(2);

            if (key.equals("x")) {
                moveToActionX(rawValue);
            } else if (key.equals("y")) {
                moveToActionY(rawValue);
            } else if (key.equals("alpha")) {
                alphaAction(rawValue);
            } else if (key.equals("rotation")) {
                rotateToAction(rawValue);
            } else if (key.equals("scale")) {
                scaleToAction(rawValue);
            } else if (key.equals("color")) {
                colorAction(rawValue);
            } else {
                Xlog.e("Unknown transform key: %s", key);
            }
        }
    }

    private void moveToActionX(LuaValue rawValue) {
        MoveToAction action = Actions.action(MoveToAction.class);
        action.setDuration(0);
        action.setY(mActor.getY());
        if (rawValue.istable()) {
            LuaTable table = rawValue.checktable();
            action.setX(getfloat(table, "value"));
            consumeFullParams(action, table);
        } else {
            action.setX(checkfloat(rawValue));
        }
        mActor.addAction(action);
    }

    private void moveToActionY(LuaValue rawValue) {
        MoveToAction action = Actions.action(MoveToAction.class);
        action.setDuration(0);
        action.setX(mActor.getX());
        if (rawValue.istable()) {
            LuaTable table = rawValue.checktable();
            action.setY(getfloat(table, "value"));
            consumeFullParams(action, table);
        } else {
            action.setY(checkfloat(rawValue));
        }
        mActor.addAction(action);
    }

    private void alphaAction(LuaValue rawValue) {
        AlphaAction action = Actions.action(AlphaAction.class);
        action.setDuration(0);
        if (rawValue.istable()) {
            LuaTable table = rawValue.checktable();
            action.setAlpha(getfloat(table, "value"));
            consumeFullParams(action, table);
        } else {
            action.setAlpha(checkfloat(rawValue));
        }
        mActor.addAction(action);
    }

    private void rotateToAction(LuaValue rawValue) {
        RotateToAction action = Actions.action(RotateToAction.class);
        action.setDuration(0);
        if (rawValue.istable()) {
            LuaTable table = rawValue.checktable();
            action.setRotation(getfloat(table, "value"));
            consumeFullParams(action, table);
        } else {
            action.setRotation(checkfloat(rawValue));
        }
        mActor.addAction(action);
    }

    private void scaleToAction(LuaValue rawValue) {
        ScaleToAction action = Actions.action(ScaleToAction.class);
        action.setDuration(0);
        if (rawValue.istable()) {
            LuaTable table = rawValue.checktable();
            if (table.get("value").istable()) {
                LuaTable xyTable = table.get("value").checktable();
                action.setX(getfloat(xyTable, 1));
                action.setY(getfloat(xyTable, 2));
                consumeFullParams(action, table);
            } else if (table.get("value").isnumber()) {
                action.setScale(getfloat(table, "value"));
                consumeFullParams(action, table);
            } else {
                action.setX(getfloat(table, 1));
                action.setY(getfloat(table, 2));
            }
        } else {
            action.setScale(checkfloat(rawValue));
        }
        mActor.addAction(action);
    }

    private void colorAction(LuaValue rawValue) {
        ColorAction action = Actions.action(ColorAction.class);
        action.setDuration(0);
        LuaTable table = rawValue.checktable();
        if (table.get("value").istable()) {
            LuaTable colorTable = table.get("value").checktable();
            float r = getfloat(colorTable, 1);
            float g = getfloat(colorTable, 1);
            float b = getfloat(colorTable, 1);
            float a = getActor().getColor().a;
            Color color = new Color(r, g, b, a);
            action.setColor(color);
            consumeFullParams(action, table);
        } else {
            float r = getfloat(table, 1);
            float g = getfloat(table, 1);
            float b = getfloat(table, 1);
            float a = getActor().getColor().a;
            Color color = new Color(r, g, b, a);
            action.setColor(color);
        }
        mActor.addAction(action);
    }

    private void consumeFullParams(TemporalAction action, LuaTable table) {
        LuaValue interpolationRaw = table.get("interpolation");
        if (!interpolationRaw.isnil()) {
            String interpolationStr = interpolationRaw.checkjstring();
            Interpolation interpolation = sInterpolationMap.get(interpolationStr);
            action.setInterpolation(interpolation);
        }

        LuaValue durationRaw = table.get("duration");
        if (!durationRaw.isnil()) {
            action.setDuration(checkfloat(durationRaw));
        }
    }

    private static float checkfloat(LuaValue value) {
        return (float)value.checkdouble();
    }

    private static float getfloat(LuaTable table, String key) {
        LuaValue value = table.get(key);
        return checkfloat(value);
    }

    private static float getfloat(LuaTable table, int index) {
        LuaValue value = table.get(index);
        return checkfloat(value);
    }

    @Override
    public void dispose() {
        if (mTexture != null) {
            mScreen.getAssetLoader().freeAsset(mTexture);
        }
    }
}
