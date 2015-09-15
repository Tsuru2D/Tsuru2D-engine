package com.tsuru2d.engine.lua;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.VarArgFunction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Wraps a Java method as a {@link LuaFunction} object, so
 * that it can be called from a Lua script.
 */
/* package */ class ExposedJavaMethod extends VarArgFunction {
    private final Method mJavaMethod;
    private final Class<?>[] mParameterTypes;

    /**
     * Constructs a new Java method wrapper.
     * @param method The Java method to expose to Lua.
     */
    public ExposedJavaMethod(Method method) {
        mJavaMethod = method;
        mParameterTypes = method.getParameterTypes();
    }

    @Override
    public Varargs invoke(Varargs args) {
        int argCount = args.narg();
        Object thisObject = null;
        int destIndex = 0, srcIndex = 1;

        // Pop off the first argument if the target is an instance method
        if (!Modifier.isStatic(mJavaMethod.getModifiers())) {
            LuaValue arg1 = args.arg1();
            if (!(arg1 instanceof ExposedJavaClass)) {
                throw new LuaError("First argument is not a Java object, " +
                    "did you mean :method() instead of .method()?");
            }
            thisObject = arg1.touserdata();
            srcIndex++;
            argCount--;
        }

        // TODO: Change this to a field if the excessive memory allocations
        // and deallocations prove to be a problem
        Object[] javaArgs = new Object[mParameterTypes.length];

        // Convert arguments to native Java objects
        while (destIndex < argCount && destIndex < javaArgs.length) {
            Class<?> expectedType = mParameterTypes[destIndex];
            javaArgs[destIndex++] = LuaUtils.bridgeLuaToJava(args.arg(srcIndex++), expectedType);
        }

        // Invoke the method using reflection
        Object returnValue;
        try {
            returnValue = mJavaMethod.invoke(thisObject, javaArgs);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            throw new LuaError(e);
        }

        // Wrap return value if there is one.
        // If the method returns void, the return value
        // from invoke() will be null, which will be wrapped
        // to nil anyways, so we don't have to check the
        // method return type.
        return LuaUtils.bridgeJavaToLuaOut(returnValue);
    }
}
