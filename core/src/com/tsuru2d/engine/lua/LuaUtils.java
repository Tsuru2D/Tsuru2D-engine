package com.tsuru2d.engine.lua;

import org.luaj.vm2.*;

/* package */ class LuaUtils {
    /**
     * Converts a Java object to its Lua equivalent. The logic
     * is as follows:
     * <p>
     * <ol>
     *     <li>{@code null} -> {@link LuaNil}</li>
     *     <li>{@link String} -> {@link LuaString}</li>
     *     <li>{@link Integer} -> {@link LuaInteger}</li>
     *     <li>(other primitive wrapper classes...)</li>
     *     <li>{@link LuaValue} -> {@link LuaValue}</li>
     *     <li>{@link Object} -> {@link LuaUserdata}</li>
     * </ol>
     * <p>
     * This method does *not* handle {@link Varargs} objects,
     * use {@link #bridgeJavaToLuaOut(Object)} if you need to handle
     * them. This is intended to be used when calling Lua functions
     * from Java.
     * @param javaValue The value to convert.
     */
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

    /**
     * The same as {@link #bridgeJavaToLuaIn(Object)}, but also
     * handles {@link Varargs} objects. This is intended to be used
     * when returning values from a Java method to Lua.
     * @param javaValue The value to convert.
     */
    public static Varargs bridgeJavaToLuaOut(Object javaValue) {
        if (javaValue instanceof Varargs) {
            return (Varargs)javaValue;
        } else {
            return bridgeJavaToLuaIn(javaValue);
        }
    }

    /**
     * Converts a Lua object to its Java equivalent. This method
     * performs type checking and unboxing of primitive wrappers.
     *
     * <p>
     * If {@code luaValue} is nil and {@code expectedType} does not
     * refer to a primitive type, {@code null} will be returned.
     *
     * <p>
     * If {@code expectedType} refers to a Java primitive type or its
     * corresponding wrapper type, {@code luaValue} will be converted
     * to the wrapper type.
     *
     * <p>
     * If {@code expectedType} refers to a subclass of {@link LuaValue},
     * the Lua object will be directly returned.
     *
     * <p>
     * If {@code expectedType} refers to any other Java class,
     * {@code luaValue} must be an instance of {@link LuaUserdata} that
     * wraps that type.
     *
     * @param luaValue The value to convert.
     * @param expectedType The Java class to convert the value to.
     */
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
