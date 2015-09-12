package com.tsuru2d.engine.lua;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaUserdata;

import java.lang.reflect.Method;

/**
 * Wraps a Java object as a {@link LuaUserdata} object, and exposes
 * methods annotated with {@link ExposeToLua} as functions
 * in Lua. Static methods should be called using {@code obj.method()},
 * while instance methods should be called using {@code obj:method()}.
 */
public class ExposedJavaClass extends LuaUserdata {
    /**
     * Constructs a new Java object wrapper.
     * @param obj The Java object to expose to Lua.
     */
    public ExposedJavaClass(Object obj) {
        super(obj, createMetatable(obj));
    }

    private static LuaTable createMetatable(Object javaObject) {
        Class<?> cls = javaObject.getClass();
        LuaTable metatable = new LuaTable();

        for (Method method : cls.getMethods()) {
            ExposeToLua annotation = method.getAnnotation(ExposeToLua.class);
            if (annotation == null) {
                continue;
            }

            // Use the name provided in the annotation if specified,
            // or the Java method name otherwise
            String exposedName = annotation.name();
            if (exposedName == null || exposedName.isEmpty()) {
                exposedName = method.getName();
            }

            // Make sure our method is callable via reflection
            method.setAccessible(true);

            // Add our wrapped function into the metatable
            ExposedJavaMethod luaFunction = new ExposedJavaMethod(method);
            metatable.set(exposedName, luaFunction);
        }

        metatable.set("__index", metatable);
        return metatable;
    }
}
