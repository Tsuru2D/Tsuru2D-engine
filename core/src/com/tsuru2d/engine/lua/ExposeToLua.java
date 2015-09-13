package com.tsuru2d.engine.lua;

import java.lang.annotation.*;

/**
 * Marks a Java method to be exposed to Lua. Only methods
 * marked with this annotation will be callable from
 * Lua code. This annotation ignores Java visibility -
 * all methods, even private ones, will be exposed to Lua
 * if marked with this annotation.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExposeToLua {
    /**
     * The name of the method when imported into Lua.
     * If this is not provided, the name of the Java method
     * will be used.
     */
    String name() default "";
}
