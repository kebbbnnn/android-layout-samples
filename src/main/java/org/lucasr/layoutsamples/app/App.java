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

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import org.lucasr.uielement.cache.UIElementCache;

import jp.wasabeef.takt.Takt;

public class App extends Application {
    private UIElementCache mElementCache;

    @Override
    public void onCreate() {
        super.onCreate();

        Takt.stock(this).color(Color.BLACK);

        /*UIElementInflater.from(this).preloadElementConstructors(new HashMap<String, Constructor<? extends UIElement>>() {{
            try {
                put(TappableImageElement.class.getName(), TappableImageElement.class.getConstructor(UIElementHost.class, AttributeSet.class));
            } catch (NoSuchMethodException ignore) {}
        }});*/

        mElementCache = new UIElementCache();
    }

    public UIElementCache getElementCache() {
        return mElementCache;
    }

    public static App getInstance(Context context) {
        return (App) context.getApplicationContext();
    }

    public static String getViewHierarchy(@NonNull View v) {
        StringBuilder desc = new StringBuilder();
        getViewHierarchy(v, desc, 0);
        return desc.toString();
    }

    private static void getViewHierarchy(View v, StringBuilder desc, int margin) {
        desc.append(getViewMessage(v, margin));
        if (v instanceof ViewGroup) {
            margin++;
            ViewGroup vg = (ViewGroup) v;
            for (int i = 0; i < vg.getChildCount(); i++) {
                getViewHierarchy(vg.getChildAt(i), desc, margin);
            }
        }
    }

    @SuppressLint("ResourceType")
    private static String getViewMessage(View v, int marginOffset) {
        String repeated = new String(new char[marginOffset]).replace("\0", "  ");
        try {
            String resourceId = v.getResources() != null ? (v.getId() > 0 ? v.getResources().getResourceName(v.getId()) : "no_id") : "no_resources";
            return repeated + "[" + v.getClass().getSimpleName() + "] " + resourceId + "\n";
        } catch (Resources.NotFoundException e) {
            return repeated + "[" + v.getClass().getSimpleName() + "] name_not_found\n";
        }
    }
}
