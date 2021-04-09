package org.lucasr.layoutsamples.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import org.lucasr.layoutsamples.app.R;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class Shared {

    private Shared() {
        Log.e(Shared.class.getName(), "Point X");
        if (application == null) {
            application = getApplicationUsingReflection();
        }
    }

    private static class SingletonHelper {
        private static final Shared INSTANCE = new Shared();
    }

    private Application application;

    public static Shared shared = SingletonHelper.INSTANCE;

    private String getStringRes(@StringRes int id) {
        return application.getString(id);
    }

    private Drawable getDrawableRes(@DrawableRes int id) {
        return ContextCompat.getDrawable(application, id);
    }

    private int getColorRes(@ColorRes int id){
        return ContextCompat.getColor(application, id);
    }

    public int getDimensionPixelOffsetRes(@DimenRes int id) {
        return application.getResources().getDimensionPixelOffset(id);
    }

    public int getDimensionPixelSizeRes(@DimenRes int id) {
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
