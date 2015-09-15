package com.tsuru2d.engine.util;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

public final class Xlog {
    private static final String TAG = "Tsuru2D";
    private static final int LOG_LEVEL = Application.LOG_DEBUG;

    static {
        Gdx.app.setLogLevel(LOG_LEVEL);
    }

    private Xlog() { }

    public static void d(String message, Object... args) {
        if (args.length == 0) {
            Gdx.app.debug(TAG, message);
        } else if (args.length == 1 && args[0] instanceof Throwable) {
            Gdx.app.debug(TAG, message, (Throwable)args[0]);
        } else if (args[args.length - 1] instanceof Throwable) {
            Gdx.app.debug(TAG, String.format(message, args), (Throwable)args[args.length - 1]);
        } else {
            Gdx.app.debug(TAG, String.format(message, args));
        }
    }

    public static void i(String message, Object... args) {
        if (args.length == 0) {
            Gdx.app.log(TAG, message);
        } else if (args.length == 1 && args[0] instanceof Throwable) {
            Gdx.app.log(TAG, message, (Throwable)args[0]);
        } else if (args[args.length - 1] instanceof Throwable) {
            Gdx.app.log(TAG, String.format(message, args), (Throwable)args[args.length - 1]);
        } else {
            Gdx.app.log(TAG, String.format(message, args));
        }
    }

    public static void e(String message, Object... args) {
        if (args.length == 0) {
            Gdx.app.error(TAG, message);
        } else if (args.length == 1 && args[0] instanceof Throwable) {
            Gdx.app.error(TAG, message, (Throwable)args[0]);
        } else if (args[args.length - 1] instanceof Throwable) {
            Gdx.app.error(TAG, String.format(message, args), (Throwable)args[args.length - 1]);
        } else {
            Gdx.app.error(TAG, String.format(message, args));
        }
    }
}
