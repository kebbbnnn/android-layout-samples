package org.lucasr.uielement.async;

import android.content.Context;

import org.lucasr.uielement.cache.Hashable;
import org.lucasr.uielement.canvas.UIElementGroup;
import org.lucasr.uielement.canvas.UIElementHost;

public interface AsyncUIElementProvider<O extends Hashable> {
    public AsyncUIElement<? extends UIElementGroup, ? extends Hashable> create(Context context, O object, UIElementHost host);
}