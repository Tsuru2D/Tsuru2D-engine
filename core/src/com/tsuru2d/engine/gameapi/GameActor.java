package com.tsuru2d.engine.gameapi;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.tsuru2d.engine.lua.ExposedJavaClass;
import com.tsuru2d.engine.lua.LuaMapIterator;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.util.HashMap;
import java.util.Iterator;

public class GameActor extends ExposedJavaClass {
    private Texture mTexture;
    private Actor mActor;
    private static final HashMap<String, Interpolation> sInterpolationMap;

    static {
        sInterpolationMap = new HashMap<String, Interpolation>();
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

    public void setTexture(Texture texture) {
        mTexture = texture;
    }

    //transformation can have three values {x,y,alpha,rotation,scale,color}, each having three properties {value(or {values}),interpolation,duration}

    public void transform(LuaTable transformation) {
        Iterator<Varargs> it = new LuaMapIterator(transformation);
        while (it.hasNext()) {
            Varargs transformKvp = it.next();
            String key = transformKvp.arg(1).checkjstring();
            LuaValue rawValue = transformKvp.arg(2);

            if (key.equals("x")) {              //x={value=..., interpolation=..., duration=...} or x=...
                moveToActionX(rawValue);
            } else if (key.equals("y")) {       //y={value=..., interpolation=..., duration=...} or y=...
                moveToActionY(rawValue);
            } else if (key.equals("alpha")) {   //alpha={value=..., interpolation=..., duration=...} or alpha=...
                alphaAction(rawValue);
            } else if (key.equals("rotation")) {   //rotation={value=..., interpolation=.., duration=..} or rotation=...
                rotateToAction(rawValue);
            } else if (key.equals("scale")) {     //scale={0.5,0.6} or scale=0.5 or scale={{0.5,0.6}[,interpolation=[, duration=]} or scale={0.5,[,0.6[,interpolation=[, duration=]]]}
                scaleToAction(rawValue);
            } else if (key.equals("color")) {     //color={r,g,b,a[,interpolation=..[,duration=..]]} or color={r,g,b,a}
                colorAction(rawValue);
            }
        }
    }

    public void draw(Batch batch, float parentAlpha) {
        batch.draw(
            mTexture,
            mActor.getX(),
            mActor.getY(),
            mActor.getOriginX(),
            mActor.getOriginY(),
            mActor.getHeight(),
            mActor.getWidth(),
            mActor.getScaleX(),
            mActor.getScaleY(),
            mActor.getRotation(),
            0,
            0,
            mTexture.getWidth(),
            mTexture.getHeight(),
            false,
            false);
    }


    private void moveToActionX(LuaValue rawValue) {
        MoveToAction action = Actions.action(MoveToAction.class);
        if (rawValue.istable()) {  //x={value=..., interpolation=..., duration=...}
            LuaTable table = rawValue.checktable();
            action.setX((float)table.get("value").checkdouble());
            parseTable(action, table);
        } else {  //x=...
            action.setX((float)rawValue.checkdouble());
        }
        mActor.addAction(action);
    }

    private void moveToActionY(LuaValue rawValue) {
        MoveToAction action = Actions.action(MoveToAction.class);
        if (rawValue.istable()) {  //y={value=...[, interpolation=...[, duration=...]]}
            LuaTable table = rawValue.checktable();
            action.setY((float)table.get("value").checkdouble());
            parseTable(action, table);
        } else {  //y=...
            action.setY((float)rawValue.checkdouble());
        }
        mActor.addAction(action);
    }

    private void alphaAction(LuaValue rawValue) {
        AlphaAction action = Actions.action(AlphaAction.class);
        if (rawValue.istable()) {   //alpha={value=..[,interpolation=..[,duration=..]]}
            LuaTable table = rawValue.checktable();
            action.setAlpha((float)table.get("value").checkdouble());
            parseTable(action, table);
        } else {  //alpha=...
            action.setAlpha((float)rawValue.checkdouble());
        }
        mActor.addAction(action);
    }

    private void rotateToAction(LuaValue rawValue) {
        RotateToAction action = Actions.action(RotateToAction.class);
        if (rawValue.istable()) {       //rotation={value[,interpolation=..[,duration=..]]}
            LuaTable table = rawValue.checktable();
            action.setRotation((float)table.get("value").checkdouble());
            parseTable(action, table);
        } else {                          //rotation=..
            action.setRotation((float)rawValue.checkdouble());
        }
        mActor.addAction(action);
    }

    private void scaleToAction(LuaValue rawValue) {
        ScaleToAction action = Actions.action(ScaleToAction.class);
        if (rawValue.istable()) {
            LuaTable table = rawValue.checktable();
            if (table.get("value").istable()) {             //scale={value={0.5,0.6}[,interpolation=..[,duration=..]]}
                LuaTable xyTable = table.get("value").checktable();
                action.setX((float)xyTable.get(1).checkdouble());
                action.setY((float)xyTable.get(2).checkdouble());
            } else if (table.get("value").isnumber()) {     //scale={value=0.5[,interpolation=..[,duration=..]]}
                action.setScale((float)table.get("value").checkdouble());
            } else {                                        //scale={0.5,0.6[,interpolation=..[,duration=..]]} normally people do not have interpolation or duration append in this syntax
                action.setX((float)table.get(1).checkdouble());
                action.setY((float)table.get(2).checkdouble());
            }
            parseTable(action, table);
        } else {                                            //scale=0.5
            action.setScale((float)rawValue.checkdouble());
        }
        mActor.addAction(action);
    }

    private void colorAction(LuaValue rawValue) {
        ColorAction action = Actions.action(ColorAction.class);
        LuaTable table = rawValue.checktable();
        if (table.get("value").istable()) {     //color={value={r,g,b[,a]},interpolation=..,duration=..}
            LuaTable colorTable = table.get("value").checktable();
            float r = (float)colorTable.get(1).checkdouble();
            float g = (float)colorTable.get(2).checkdouble();
            float b = (float)colorTable.get(3).checkdouble();
            float a = (float)(colorTable.get(4).isnil() ? 1 : colorTable.get(4).checkdouble());
            Color color = new Color(r, g, b, a);
            action.setColor(color);
            parseTable(action, table);
        } else {                         //color={r,g,b[,a]}
            float r = (float)table.get(1).checkdouble();
            float g = (float)table.get(2).checkdouble();
            float b = (float)table.get(3).checkdouble();
            float a = (float)(table.get(4).isnil() ? 1 : table.get(4).checkdouble());
            Color color = new Color(r, g, b, a);
            action.setColor(color);
        }
        mActor.addAction(action);
    }

    private void parseTable(TemporalAction action, LuaTable table) {
        if (!table.get("interpolation").isnil()) {
            String strInterpolation = table.get("interpolation").checkjstring();
            Interpolation interpolation=sInterpolationMap.get(strInterpolation);
            action.setInterpolation(interpolation);
        }
        if (!table.get("duration").isnil())
            action.setDuration((float)table.get("duration").checkdouble());
    }
}
