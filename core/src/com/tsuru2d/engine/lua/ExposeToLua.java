package com.tsuru2d.engine.lua;

import java.lang.annotation.*;

/**
 * Marks a Java method to be exposed to Lua. Only methods
 * marked with this annotation will be callable from
 * Lua code. Only public methods will be exposed - this
 * annotation has no effect on private methods.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExposeToLua {
    /**
     * The name of the method when imported into Lua.
     * If this is not provided, the name of the Java method
     * will be used.
     */
    String name() default "";

    /**
     * Whether an error will be thrown if the incorrect
     * number of arguments are passed from Lua. If false,
     * all missing object arguments will be passed as
     * {@code null}, and any extra arguments will be
     * ignored.
     */
    boolean strictArgs() default false;
}
