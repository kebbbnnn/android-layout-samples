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

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import org.lucasr.layoutsamples.adapter.Tweet;
import org.lucasr.layoutsamples.adapter.TweetsAdapter;
import org.lucasr.layoutsamples.async.AsyncTweetElementProvider;
import org.lucasr.smoothie.AsyncListView;
import org.lucasr.smoothie.ItemManager;
import org.lucasr.uielement.async.AsyncLayoutLoader;
import org.lucasr.uielement.async.AsyncUIElementProvider;
import org.lucasr.uielement.cache.UIElementCache;
import org.lucasr.uielement.canvas.UIElement;

public class TweetsListView extends AsyncListView {
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int POOL_SIZE = CPU_COUNT * 2 + 1;

    private TweetsAdapter mTweetsAdapter;
    private int mPresenterId;

    public TweetsListView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.listViewStyle);
    }

    public TweetsListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPresenterId = R.layout.tweet_composite_row;
    }

    private void updateTargetWidth() {
        if (mPresenterId != R.layout.tweet_async_row) {
            return;
        }

        final Context context = getContext();

        final int targetWidth = getWidth() - getPaddingLeft() + getPaddingRight();
        if (AsyncTweetElementProvider.SHARED.setTargetWidth(context, targetWidth)) {
            App.getInstance(context).getElementCache().evictAll();

            TweetsAdapter adapter = (TweetsAdapter) getAdapter();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void updateItemLoader() {
        Context context = getContext();

        if (mPresenterId == R.layout.tweet_async_row && !hasItemManager()) {
            final UIElementCache elementCache = App.getInstance(context).getElementCache();
            final AsyncUIElementProvider<Tweet> provider = AsyncTweetElementProvider.SHARED;

            AsyncLayoutLoader<Tweet, UIElement> loader = new AsyncLayoutLoader<>(context, elementCache, provider);

            ItemManager.Builder builder = new ItemManager.Builder(loader);
            builder.setPreloadItemsEnabled(true).setPreloadItemsCount(30);
            builder.setThreadPoolSize(POOL_SIZE); // Source: https://stackoverflow.com/a/30297237/2687048
            setItemManager(builder.build());
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            @SuppressWarnings("deprecation")
            public void onGlobalLayout() {
                updateTargetWidth();
                updateItemLoader();

                final AsyncUIElementProvider<Tweet> provider = AsyncTweetElementProvider.SHARED;
                mTweetsAdapter = new TweetsAdapter(provider, getContext(), mPresenterId);
                setAdapter(mTweetsAdapter);

                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (changed) {
            updateTargetWidth();
        }
    }

    public void setPresenter(int id) {
        if (mPresenterId == id) {
            return;
        }

        mPresenterId = id;
        if (mTweetsAdapter != null) {
            mTweetsAdapter.setPresenter(id);
        }

        updateItemLoader();
    }
}
