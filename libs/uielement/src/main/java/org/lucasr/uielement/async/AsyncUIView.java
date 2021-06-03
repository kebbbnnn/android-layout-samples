/*
 * Copyright (C) 2014 Lucas Rocha
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lucasr.uielement.async;

import android.content.Context;
import android.util.AttributeSet;

import org.lucasr.uielement.adapter.UpdateFlags;
import org.lucasr.uielement.adapter.ViewPresenter;
import org.lucasr.uielement.cache.Hashable;
import org.lucasr.uielement.canvas.UIElementGroup;
import org.lucasr.uielement.canvas.UIElementView;

import java.util.EnumSet;

public class AsyncUIView<O extends Hashable> extends UIElementView implements ViewPresenter<O> {
    private AsyncUIElementProvider<O> mProvider;

    public AsyncUIView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AsyncUIView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean hasAsyncUIElementProvider() {
        return null != mProvider;
    }

    @Override
    public void setAsyncUIElementProvider(AsyncUIElementProvider<O> provider) {
        mProvider = provider;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void update(O object, EnumSet<UpdateFlags> flags) {
        final AsyncUIElement<? extends UIElementGroup, O> element = (AsyncUIElement<? extends UIElementGroup, O>) mProvider.create(getContext(), object);
        setUIElement(element);

        element.update(object, flags);
    }
}
