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
    private final int mParameterCount;

    /**
     * Constructs a new Java method wrapper.
     * @param method The Java method to expose to Lua.
     */
    public ExposedJavaMethod(Method method) {
        mJavaMethod = method;
        int parameterCount;
        try {
            parameterCount = method.getParameterCount();
        } catch (NoSuchMethodError e) {
            parameterCount = method.getParameterTypes().length;
        }
        mParameterCount = parameterCount;
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
        Object[] javaArgs = new Object[mParameterCount];

        // Convert arguments to native Java objects
        while (destIndex < argCount && destIndex < javaArgs.length) {
            javaArgs[destIndex++] = bridgeLuaToJava(args.arg(srcIndex++));
        }

        // Fill in missing arguments with null
        while (destIndex < javaArgs.length) {
            javaArgs[destIndex++] = null;
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

        // Wrap return value if there is one
        if (mJavaMethod.getReturnType() != Void.TYPE) {
            return bridgeJavaToLua(returnValue);
        } else {
            return LuaValue.NIL;
        }
    }

    private static Varargs bridgeJavaToLua(Object javaValue) {
        if (javaValue == null) {
            return LuaValue.NIL;
        } else if (javaValue instanceof String) {
            return LuaValue.valueOf((String)javaValue);
        } else if (javaValue instanceof Boolean) {
            return LuaValue.valueOf((Boolean)javaValue);
        } else if (javaValue instanceof Integer) {
            return LuaValue.valueOf((Integer)javaValue);
        } else if (javaValue instanceof Double) {
            return LuaValue.valueOf((Double)javaValue);
        } else if (javaValue instanceof Float) {
            return LuaValue.valueOf((Float)javaValue);
        } else if (javaValue instanceof Varargs) {
            return (Varargs)javaValue;
        } else {
            return new LuaUserdata(javaValue);
        }
    }

    private static Object bridgeLuaToJava(LuaValue luaValue) {
        switch (luaValue.type()) {
        case LuaValue.TNIL:
            return null;
        case LuaValue.TSTRING:
            return luaValue.tojstring();
        case LuaValue.TBOOLEAN:
            return luaValue.toboolean();
        case LuaValue.TINT:
            return luaValue.toint();
        case LuaValue.TNUMBER:
            return luaValue.todouble();
        case LuaValue.TUSERDATA:
            return luaValue.touserdata();
        default:
            return luaValue;
        }
    }
}
