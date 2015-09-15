package com.tsuru2d.engine.lua;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

/**
 * Helper class for creating {@link Varargs} objects from
 * Java objects and primitives.
 */
public final class JavaVarargs {
    private JavaVarargs() { }

    public static Varargs of(int value1, int value2) {
        return LuaValue.varargsOf(LuaValue.valueOf(value1), LuaValue.valueOf(value2));
    }

    public static Varargs of(double value1, double value2) {
        return LuaValue.varargsOf(LuaValue.valueOf(value1), LuaValue.valueOf(value2));
    }

    public static Varargs of(int... values) {
        LuaValue[] luaValues = new LuaValue[values.length];
        for (int i = 0; i < values.length; ++i) {
            luaValues[i] = LuaValue.valueOf(values[i]);
        }
        return LuaValue.varargsOf(luaValues);
    }

    public static Varargs of(double... values) {
        LuaValue[] luaValues = new LuaValue[values.length];
        for (int i = 0; i < values.length; ++i) {
            luaValues[i] = LuaValue.valueOf(values[i]);
        }
        return LuaValue.varargsOf(luaValues);
    }

    public static Varargs of(float... values) {
        LuaValue[] luaValues = new LuaValue[values.length];
        for (int i = 0; i < values.length; ++i) {
            luaValues[i] = LuaValue.valueOf(values[i]);
        }
        return LuaValue.varargsOf(luaValues);
    }

    public static Varargs of(Object... values) {
        LuaValue[] luaValues = new LuaValue[values.length];
        for (int i = 0; i < values.length; ++i) {
            luaValues[i] = LuaUtils.bridgeJavaToLuaIn(values[i]);
        }
        return LuaValue.varargsOf(luaValues);
    }
}

