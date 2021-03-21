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

package org.lucasr.layoutsamples.async;

import android.content.Context;
import android.util.Log;
import android.view.View;

import org.lucasr.layoutsamples.adapter.Tweet;
import org.lucasr.layoutsamples.adapter.TweetPresenter;
import org.lucasr.layoutsamples.app.App;
import org.lucasr.layoutsamples.widget.TweetElement;

import java.lang.ref.WeakReference;
import java.util.EnumSet;

public class AsyncTweetElementFactory {
    private AsyncTweetElementFactory() {}

    private static int sTargetWidth;
    //private static HeadlessElementHost sHeadlessHost;

    public synchronized static boolean setTargetWidth(Context context, int targetWidth) {
        if (sTargetWidth == targetWidth) {
            return false;
        }

        sTargetWidth = targetWidth;
        return true;
    }

    public synchronized static AsyncTweetElement create(Context context, Tweet tweet) {
        UIElementCache elementCache = App.getInstance(context).getElementCache();

        AsyncTweetElement asyncElement = (AsyncTweetElement) elementCache.get(tweet.getId());
        if (asyncElement != null) {
            return asyncElement;
        }

        final int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(sTargetWidth,
                View.MeasureSpec.EXACTLY);
        final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);

        /*if (sHeadlessHost == null) {
            sHeadlessHost = new HeadlessElementHost(context);
        }*/

        HeadlessElementHost headlessHost = SafeHeadlessElementHost.getInstance(context).getHeadlessHost();

        final TweetElement element = new TweetElement(headlessHost);
        element.update(tweet, EnumSet.of(TweetPresenter.UpdateFlags.NO_IMAGE_LOADING));
        element.measure(widthMeasureSpec, heightMeasureSpec);
        element.layout(0, 0, element.getMeasuredWidth(), element.getMeasuredHeight());

        asyncElement = new AsyncTweetElement(element);
        elementCache.put(tweet.getId(), asyncElement);

        return asyncElement;
    }
}

class SafeHeadlessElementHost {
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
