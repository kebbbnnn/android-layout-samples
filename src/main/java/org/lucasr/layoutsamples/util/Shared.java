package org.lucasr.layoutsamples.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class Shared {

    private Shared() {
        if (application == null) {
            application = getApplicationUsingReflection();
        }
    }

    private static class SingletonHelper {
        private static final Shared INSTANCE = new Shared();
    }

    public static Shared shared = SingletonHelper.INSTANCE;

    private Application application;
    private final HashMap<Integer, Object> mValuesMap = new HashMap<>();

    private String getStringRes(@StringRes int id) {
        Object stringObject = mValuesMap.get(id);
        if (null != stringObject) {
            return (String) stringObject;
        }
        stringObject = application.getString(id);
        mValuesMap.put(id, stringObject);
        return (String) stringObject;
    }

    private Drawable getDrawableRes(@DrawableRes int id) {
        Object drawableObject = mValuesMap.get(id);
        if (null != drawableObject) {
            return (Drawable) drawableObject;
        }
        drawableObject = ContextCompat.getDrawable(application, id);
        mValuesMap.put(id, drawableObject);
        return (Drawable) drawableObject;
    }

    private int getColorRes(@ColorRes int id) {
        Object intObject = mValuesMap.get(id);
        if (null != intObject) {
            return (int) intObject;
        }
        intObject = ContextCompat.getColor(application, id);
        mValuesMap.put(id, intObject);
        return (int) intObject;
    }

    public int getDimensionPixelOffsetRes(@DimenRes int id) {
        Object intObject = mValuesMap.get(id);
        if (null != intObject) {
            return (int) intObject;
        }
        intObject = application.getResources().getDimensionPixelOffset(id);
        mValuesMap.put(id, intObject);
        return (int) intObject;
    }

    public int getDimensionPixelSizeRes(@DimenRes int id) {
        Object intObject = mValuesMap.get(id);
        if (null != intObject) {
            return (int) intObject;
        }
        intObject = application.getResources().getDimensionPixelSize(id);
        mValuesMap.put(id, intObject);
        return application.getResources().getDimensionPixelSize(id);
    }

    public String getString(@StringRes int id) {
        return getStringRes(id);
    }

    public Drawable getDrawable(@DrawableRes int id) {
        return getDrawableRes(id);
    }

    public int getColor(@ColorRes int id) {
        return getColorRes(id);
    }

    public int getDimensionPixelOffset(@DimenRes int id) {
        return getDimensionPixelOffsetRes(id);
    }

    public int getDimensionPixelSize(@DimenRes int id) {
        return getDimensionPixelSizeRes(id);
    }

    //TODO: Optimize this
    @SuppressLint("PrivateApi")
    public Application getApplicationUsingReflection() {
        if (null != application) {
            return application;
        }

        try {
            application = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null, (Object[]) null);
        } catch (IllegalAccessException ignore) {
        } catch (InvocationTargetException ignore) {
        } catch (NoSuchMethodException ignore) {
        } catch (ClassNotFoundException ignore) {
        }

        if (null != application) {
            return application;
        }

        try {
            application = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null, (Object[]) null);
        } catch (IllegalAccessException ignore) {
        } catch (InvocationTargetException ignore) {
        } catch (NoSuchMethodException ignore) {
        } catch (ClassNotFoundException ignore) {
        }

        return application;
    }
}
