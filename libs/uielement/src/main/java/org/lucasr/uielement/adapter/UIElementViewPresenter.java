package org.lucasr.uielement.adapter;

import org.lucasr.uielement.async.AsyncUIElementProvider;
import org.lucasr.uielement.cache.Hashable;

public interface UIElementViewPresenter<O extends Hashable> extends UIElementPresenter<O> {
    public boolean hasAsyncUIElementProvider();
    public void setAsyncUIElementProvider(AsyncUIElementProvider<O> provider);
}
