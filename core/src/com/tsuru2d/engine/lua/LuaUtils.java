package com.tsuru2d.engine.lua;

import org.luaj.vm2.*;

/* package */ class LuaUtils {
    public static Varargs bridgeJavaToLuaOut(Object javaValue) {
        if (javaValue instanceof Varargs) {
            return (Varargs)javaValue;
        } else {
            return bridgeJavaToLuaIn(javaValue);
        }
    }

    public static LuaValue bridgeJavaToLuaIn(Object javaValue) {
        if (javaValue == null) {
            return LuaValue.NIL;
        } else if (javaValue instanceof String) {
            return LuaString.valueOf((String)javaValue);
        } else if (javaValue instanceof Integer) {
            return LuaInteger.valueOf((Integer)javaValue);
        } else if (javaValue instanceof Float) {
            return LuaDouble.valueOf((Float)javaValue);
        } else if (javaValue instanceof Boolean) {
            return LuaBoolean.valueOf((Boolean)javaValue);
        } else if (javaValue instanceof Double) {
            return LuaDouble.valueOf((Double)javaValue);
        } else if (javaValue instanceof Long) {
            return LuaInteger.valueOf((Long)javaValue);
        } else if (javaValue instanceof Short) {
            return LuaInteger.valueOf((Short)javaValue);
        } else if (javaValue instanceof Byte) {
            return LuaInteger.valueOf((Byte)javaValue);
        } else if (javaValue instanceof Character) {
            return LuaInteger.valueOf((Character)javaValue);
        } else if (javaValue instanceof LuaValue) {
            return (LuaValue)javaValue;
        } else {
            return new LuaUserdata(javaValue);
        }
    }

    public static Object bridgeLuaToJava(LuaValue luaValue, Class<?> expectedType) {
        if (luaValue.isnil() && !expectedType.isPrimitive()) {
            return null;
        } else if (String.class.equals(expectedType)) {
            return luaValue.checkjstring();
        } else if (int.class.equals(expectedType) || Integer.class.equals(expectedType)) {
            return luaValue.checkint();
        } else if (float.class.equals(expectedType) || Float.class.equals(expectedType)) {
            return (float)luaValue.checkdouble();
        } else if (boolean.class.equals(expectedType) || Boolean.class.equals(expectedType)) {
            return luaValue.checkboolean();
        } else if (double.class.equals(expectedType) || Double.class.equals(expectedType)) {
            return luaValue.checkdouble();
        } else if (long.class.equals(expectedType) || Long.class.equals(expectedType)) {
            return luaValue.checklong();
        } else if (short.class.equals(expectedType) || Short.class.equals(expectedType)) {
            return (short)luaValue.checkint();
        } else if (byte.class.equals(expectedType) || Byte.class.equals(expectedType)) {
            return (byte)luaValue.checkint();
        } else if (char.class.equals(expectedType) || Character.class.equals(expectedType)) {
            return (char)luaValue.checkint();
        } else if (LuaValue.class.isAssignableFrom(expectedType)) {
            if (!expectedType.isAssignableFrom(luaValue.getClass())) {
                throw new LuaError("Cannot convert " + luaValue.getClass().getName() +
                    " to " + expectedType.getName());
            }
            return luaValue;
        } else {
            return luaValue.checkuserdata(expectedType);
        }
    }
}
