package com.tsuru2d.engine.gameapi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
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
    private static HashMap<String, Interpolation> interpolationMap;
    static{
        interpolationMap=new HashMap<String, Interpolation>();
        interpolationMap.put("pow2",Interpolation.pow2);
        interpolationMap.put("pow2In",Interpolation.pow2In);
        interpolationMap.put("pow2Out",Interpolation.pow2Out);
        interpolationMap.put("pow3",Interpolation.pow3);
        interpolationMap.put("pow3In",Interpolation.pow3In);
        interpolationMap.put("pow3Out",Interpolation.pow3Out);
        interpolationMap.put("pow4",Interpolation.pow4);
        interpolationMap.put("pow4In",Interpolation.pow4In);
        interpolationMap.put("pow4Out",Interpolation.pow4Out);
        interpolationMap.put("pow5",Interpolation.pow3);
        interpolationMap.put("pow5In",Interpolation.pow5In);
        interpolationMap.put("pow5Out",Interpolation.pow5Out);
        interpolationMap.put("exp10",Interpolation.exp10);
        interpolationMap.put("exp10In",Interpolation.exp10In);
        interpolationMap.put("exp10Out",Interpolation.exp10Out);
        interpolationMap.put("exp5",Interpolation.exp5);
        interpolationMap.put("exp5In",Interpolation.exp5In);
        interpolationMap.put("exp5Out",Interpolation.exp5Out);
        interpolationMap.put("elastic",Interpolation.elastic);
        interpolationMap.put("elasticIn",Interpolation.elasticIn);
        interpolationMap.put("elasticOut",Interpolation.elasticOut);
        interpolationMap.put("swing",Interpolation.swing);
        interpolationMap.put("swingIn",Interpolation.swingIn);
        interpolationMap.put("swingOut",Interpolation.swingOut);
        interpolationMap.put("bounce",Interpolation.bounce);
        interpolationMap.put("bounceIn",Interpolation.bounceIn);
        interpolationMap.put("bounceOut",Interpolation.bounceOut);
        interpolationMap.put("sine",Interpolation.sine);
        interpolationMap.put("sineIn",Interpolation.sineIn);
        interpolationMap.put("sineOut",Interpolation.sineOut);
        interpolationMap.put("circle",Interpolation.circle);
        interpolationMap.put("circleIn",Interpolation.circleIn);
        interpolationMap.put("circleOut",Interpolation.circleOut);
    }
    public void setTexture(Texture texture) {
        mTexture = texture;
    }

    //transformation can have three values {x,y,alpha}, each having three properties {value,interpolation,duration}

    public void transform(LuaTable transformation) {
        Iterator<Varargs> it = new LuaMapIterator(transformation);
        while (it.hasNext()) {
            Varargs transformKvp = it.next();

            String key = transformKvp.arg(1).checkjstring();
            LuaValue rawValue = transformKvp.arg(2);

            if (key.equals("x")) {  //x={value=..., interpolation=..., duration=...} or x=...
                MoveToAction action = Actions.action(MoveToAction.class);
                if(rawValue.istable()){  //x={value=..., interpolation=..., duration=...}
                    Iterator<Varargs> table=new LuaMapIterator((LuaTable)rawValue);
                    while(table.hasNext()){
                        Varargs tableKvp= table.next();
                        String tableKey=tableKvp.arg(1).checkjstring();
                        LuaValue tableValue=tableKvp.arg(2);
                        if(tableKey.equals("value")){
                            action.setX((float)tableValue.checkdouble());
                        }else if(tableKey.equals("interpolation")){
                            action.setInterpolation(interpolationMap.get(tableValue.checkjstring()));
                        }else if(tableKey.equals("duration")){
                            action.setDuration((float)tableValue.checkdouble());
                        }
                    }
                }else{  //x=...
                    action.setX((float)rawValue.checkdouble());
                }
                mActor.addAction(action);
            } else if (key.equals("y")) {  //y={value=..., interpolation=..., duration=...} or y=...
                MoveToAction action = Actions.action(MoveToAction.class);
                if(rawValue.istable()){  //y={value=..., interpolation=..., duration=...}
                    Iterator<Varargs> table=new LuaMapIterator((LuaTable)rawValue);
                    while(table.hasNext()){
                        Varargs tableKvp= table.next();
                        String tableKey=tableKvp.arg(1).checkjstring();
                        LuaValue tableValue=tableKvp.arg(2);
                        if(tableKey.equals("value")){
                            action.setY((float)tableValue.checkdouble());
                        }else if(tableKey.equals("interpolation")){
                            action.setInterpolation(interpolationMap.get(tableValue.checkjstring()));
                        }else if(tableKey.equals("duration")){
                            action.setDuration((float)tableValue.checkdouble());
                        }
                    }
                }else{  //y=...
                    action.setY((float)rawValue.checkdouble());
                }
                mActor.addAction(action);
            } else if (key.equals("alpha")){  //alpha={value=..., interpolation=..., duration=...} or alpha=...
                AlphaAction action = Actions.action(AlphaAction.class);
                if(rawValue.istable()){
                    Iterator<Varargs> table=new LuaMapIterator((LuaTable)rawValue);
                    while(table.hasNext()){  //alpha={value=..., interpolation=..., duration=...}
                        Varargs tableKvp= table.next();
                        String tableKey=tableKvp.arg(1).checkjstring();
                        LuaValue tableValue=tableKvp.arg(2);
                        if(tableKey.equals("value")) {
                            action.setAlpha((float)tableValue.checkdouble());
                        }else if(tableKey.equals("interpolation")){
                                action.setInterpolation(interpolationMap.get(tableValue.checkjstring()));
                        }else if(tableKey.equals("duration")){
                            action.setDuration((float)tableValue.checkdouble());
                        }
                    }
                }else{  //alpha=...
                    action.setAlpha((float)rawValue.checkdouble());
                }
                mActor.addAction(action);
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
}
