package com.tsuru2d.engine.lua;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaUserdata;
import org.luaj.vm2.LuaValue;

import java.lang.reflect.Method;

/**
 * Wraps a Java object as a {@link LuaUserdata} object, and exposes
 * methods annotated with {@link ExposeToLua} as functions
 * in Lua. Static methods should be called using {@code obj.method()},
 * while instance methods should be called using {@code obj:method()}.
 */
public class ExposedJavaClass extends LuaUserdata {
    /**
     * Creates a Java object wrapper class from {@code this}. This
     * constructor should be used if you subclass this class. This
     * is logically equivalent to passing {@code this} to the
     * {@link #ExposedJavaClass(Object)} constructor.
     */
    protected ExposedJavaClass() {
        super(null);
        m_instance = this;
        m_metatable = createMetatable(this);
    }

    /**
     * Creates a Java object wrapper class from {@code obj} that
     * delegates all method calls to it. This constructor should
     * be used if you are wrapping a separate class.
     * @param obj The object to delegate method calls to.
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
            ExposedJavaMethod luaFunction = new ExposedJavaMethod(method, exposedName);
            metatable.set(exposedName, luaFunction);
        }

        metatable.set(LuaValue.INDEX, metatable);
        return metatable;
    }
}
