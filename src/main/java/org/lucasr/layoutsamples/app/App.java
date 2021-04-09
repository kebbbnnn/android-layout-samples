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

package org.lucasr.layoutsamples.app;

import android.app.Application;
import android.content.Context;
import android.util.AttributeSet;

import org.lucasr.layoutsamples.async.UIElementCache;
import org.lucasr.layoutsamples.canvas.TappableImageElement;
import org.lucasr.layoutsamples.util.Shared;
import org.lucasr.uielement.canvas.UIElement;
import org.lucasr.uielement.canvas.UIElementHost;
import org.lucasr.uielement.canvas.UIElementInflater;

import java.lang.reflect.Constructor;
import java.util.HashMap;

public class App extends Application {
    private UIElementCache mElementCache;

    @Override
    public void onCreate() {
        super.onCreate();

        Shared.init(this);

        UIElementInflater.from(this).preloadElementConstructors(new HashMap<String, Constructor<? extends UIElement>>() {{
            try {
                put(TappableImageElement.class.getName(), TappableImageElement.class.getConstructor(UIElementHost.class, AttributeSet.class));
            } catch (NoSuchMethodException ignore) {}
        }});

        mElementCache = new UIElementCache();
    }

    public UIElementCache getElementCache() {
        return mElementCache;
    }

    public static App getInstance(Context context) {
        return (App) context.getApplicationContext();
    }
}
