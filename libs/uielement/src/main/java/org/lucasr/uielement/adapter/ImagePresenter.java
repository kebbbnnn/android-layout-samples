package org.lucasr.uielement.adapter;

import java.util.EnumSet;

public interface ImagePresenter<T> {
    public void load(T t, EnumSet<UpdateFlags> flags);
}
