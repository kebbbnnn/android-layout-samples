package org.lucasr.uielement.async;

import android.content.Context;

import java.lang.ref.WeakReference;

public final class SafeHeadlessElementHost {
    private WeakReference<HeadlessElementHost> mHeadlessHost;

    private SafeHeadlessElementHost() {}

    private static class SingletonHelper {
        private static final SafeHeadlessElementHost INSTANCE = new SafeHeadlessElementHost();
    }

    public static SafeHeadlessElementHost getInstance(Context context) {
        SafeHeadlessElementHost instance = SingletonHelper.INSTANCE;
        if (null == instance.mHeadlessHost || null == instance.mHeadlessHost.get()) {
            instance.mHeadlessHost = new WeakReference<>(new HeadlessElementHost(context));
        } else if (null == instance.mHeadlessHost.get().getContext()) {
            instance.mHeadlessHost.get().mContext = context;
        }
        return instance;
    }

    public HeadlessElementHost getHeadlessHost() {
        return mHeadlessHost.get();
    }
}
