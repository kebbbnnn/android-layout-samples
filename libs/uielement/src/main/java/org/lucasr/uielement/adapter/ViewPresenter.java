package org.lucasr.uielement.adapter;

import org.lucasr.uielement.async.AsyncUIElementProvider;
import org.lucasr.uielement.cache.Hashable;

public interface ViewPresenter<O extends Hashable> extends ElementPresenter<O> {
    public boolean hasAsyncUIElementProvider();
    public void setAsyncUIElementProvider(AsyncUIElementProvider<O> provider);
}
