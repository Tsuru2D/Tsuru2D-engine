package com.tsuru2d.engine.lua;

import com.tsuru2d.engine.util.Xlog;
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
    private static final Object[] EMPTY_PARAMS = new Object[0];

    private final Method mJavaMethod;
    private final String mMethodName;
    private final Class<?>[] mParameterTypes;
    private final Object[] mParameters;

    public ExposedJavaMethod(Method method, String methodName) {
        mJavaMethod = method;
        mMethodName = methodName;
        mParameterTypes = method.getParameterTypes();
        if (mParameterTypes.length == 0) {
            mParameters = EMPTY_PARAMS;
        } else {
            mParameters = new Object[mParameterTypes.length];
        }
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
                String errMsg = String.format(
                    "First argument is not a Java object, " +
                    "did you mean :%1$s() instead of .%1$s()?", mMethodName);
                throw new LuaError(errMsg);
            }
            thisObject = arg1.touserdata();
            srcIndex++;
            argCount--;
        }

        // Convert arguments to native Java objects
        while (destIndex < argCount && destIndex < mParameters.length) {
            Class<?> expectedType = mParameterTypes[destIndex];
            // Allow the usage of varargs, if and only if the Java method
            // has Varargs as its last argument
            if (expectedType.equals(Varargs.class)) {
                if (destIndex == mParameters.length - 1) {
                    mParameters[destIndex++] = args.subargs(destIndex);
                } else {
                    throw new LuaError("Varargs must be the last argument in target Java method");
                }
            } else {
                mParameters[destIndex++] = LuaUtils.bridgeLuaToJava(
                    args.arg(srcIndex++), expectedType);
            }
        }

        if (argCount != mParameters.length) {
            Xlog.d("Lua->Java parameter count mismatch (expected %d, got %d)",
                mParameters.length, argCount);
        }

        // Invoke the method using reflection
        Object returnValue;
        try {
            returnValue = mJavaMethod.invoke(thisObject, mParameters);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            throw new LuaError(e);
        }

        // Clear argument buffer for next use
        while (--destIndex >= 0) {
            mParameters[destIndex] = null;
        }

        // Wrap return value if there is one.
        // If the method returns void, the return value
        // from invoke() will be null, which will be wrapped
        // to nil anyways, so we don't have to check the
        // method return type.
        return LuaUtils.bridgeJavaToLuaOut(returnValue);
    }
}
