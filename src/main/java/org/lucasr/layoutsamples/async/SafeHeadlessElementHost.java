package org.lucasr.layoutsamples.async;

import android.content.Context;
import java.lang.ref.WeakReference;

public class SafeHeadlessElementHost {
    private WeakReference<HeadlessElementHost> mHeadlessHost;

    private SafeHeadlessElementHost() {}

    private static class SingletonHelper {
        private static final SafeHeadlessElementHost INSTANCE = new SafeHeadlessElementHost();
    }

    public static SafeHeadlessElementHost getInstance(Context context) {
        SafeHeadlessElementHost instance = SingletonHelper.INSTANCE;
        if (instance.mHeadlessHost == null || instance.mHeadlessHost.get() == null) {
            instance.mHeadlessHost = new WeakReference<>(new HeadlessElementHost(context));
        }
        return instance;
    }

    public HeadlessElementHost getHeadlessHost() {
        return mHeadlessHost.get();
    }
}