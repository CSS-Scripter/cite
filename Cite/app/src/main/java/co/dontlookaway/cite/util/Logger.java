package co.dontlookaway.cite.util;

import android.util.Log;

public class Logger<T> {
    Class<T> clazz;

    public Logger(Class<T> clazz) {
        this.clazz = clazz;
    }

    public void info(String message) {
        Log.i(this.clazz.getName(), message + "");
    }

    public void verbose(String message) {
        Log.v(this.clazz.getName(), message + "");
    }

    public void debug(String message) {
        Log.d(this.clazz.getName(), message + "");
    }

    public void error(String message) {
        Log.e(this.clazz.getName(), message + "");
    }

    public void warn(String message) {
        Log.w(this.clazz.getName(), message + "");
    }

    public void wtf(String message) {
        Log.wtf(this.clazz.getName(), message + "");
    }
}
