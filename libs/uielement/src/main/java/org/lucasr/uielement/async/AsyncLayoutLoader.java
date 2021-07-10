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
import android.view.View;
import android.widget.Adapter;

import org.lucasr.smoothie.SimpleItemLoader;
import org.lucasr.uielement.cache.Hashable;
import org.lucasr.uielement.cache.UIElementCache;
import org.lucasr.uielement.canvas.UIElement;

public class AsyncLayoutLoader<O extends Hashable, E extends UIElement> extends SimpleItemLoader<O, E> {
    private final Context mContext;
    private final UIElementCache mElementCache;
    private final AsyncUIElementProvider<O> mProvider;

    public AsyncLayoutLoader(Context context, UIElementCache elementCache, AsyncUIElementProvider<O> provider) {
        mContext = context;
        mElementCache = elementCache;
        mProvider = provider;
    }

    @SuppressWarnings("unchecked")
    @Override
    public O getItemParams(Adapter adapter, int position) {
        return (O) adapter.getItem(position);
    }

    @SuppressWarnings("unchecked")
    @Override
    public E loadItem(O object) {
        final HeadlessElementHost headlessHost = SafeHeadlessElementHost.getInstance(mContext).getHeadlessHost();
        return (E) mProvider.create(mContext, object, headlessHost);
    }

    @SuppressWarnings("unchecked")
    @Override
    public E loadItemFromMemory(O object) {
        return (E) mElementCache.get(object.id());
    }

    @Override
    public void displayItem(View itemView, E result, boolean fromMemory) {
        // Do nothing as we're only using this loader to pre-measure/layout
        // UIElements that are off screen.
    }
}
