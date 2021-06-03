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
import android.view.View;

import org.lucasr.layoutsamples.adapter.Tweet;
import org.lucasr.layoutsamples.app.App;
import org.lucasr.layoutsamples.widget.TweetElement;
import org.lucasr.uielement.adapter.UpdateFlags;
import org.lucasr.uielement.async.AsyncUIElement;
import org.lucasr.uielement.async.AsyncUIElementProvider;
import org.lucasr.uielement.cache.UIElementCache;

import java.util.EnumSet;

public class AsyncTweetElementProvider implements AsyncUIElementProvider<Tweet> {

    private AsyncTweetElementProvider() {}

    private static class SingletonHelper {
        private static final AsyncTweetElementProvider INSTANCE = new AsyncTweetElementProvider();
    }

    public static final AsyncTweetElementProvider shared = SingletonHelper.INSTANCE;

    private int mTargetWidth;

    public synchronized boolean setTargetWidth(Context context, int targetWidth) {
        final boolean notEqual = mTargetWidth != targetWidth;
        if (notEqual) {
            mTargetWidth = targetWidth;
        }
        return notEqual;
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized AsyncUIElement<TweetElement, Tweet> create(Context context, Tweet tweet) {
        UIElementCache elementCache = App.getInstance(context).getElementCache();

        AsyncUIElement<TweetElement, Tweet> asyncElement = (AsyncUIElement<TweetElement, Tweet>) elementCache.get(tweet);
        if (asyncElement != null) {
            if (!asyncElement.isAttachedToHost()) {
                final HeadlessElementHost headlessHost = SafeHeadlessElementHost.getInstance(context).getHeadlessHost();
                asyncElement.swapHost(headlessHost);
            }
            return asyncElement;
        }

        final int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(mTargetWidth,
                View.MeasureSpec.EXACTLY);
        final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);

        HeadlessElementHost headlessHost = SafeHeadlessElementHost.getInstance(context).getHeadlessHost();

        final TweetElement element = new TweetElement(headlessHost);
        element.update(tweet, EnumSet.of(UpdateFlags.NO_IMAGE_LOADING));
        element.measure(widthMeasureSpec, heightMeasureSpec);
        element.layout(0, 0, element.getMeasuredWidth(), element.getMeasuredHeight());

        asyncElement = new AsyncUIElement<TweetElement, Tweet>(element);
        elementCache.put(tweet, asyncElement);

        return asyncElement;
    }
}
