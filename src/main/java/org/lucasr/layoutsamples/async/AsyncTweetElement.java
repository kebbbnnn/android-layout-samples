package org.lucasr.layoutsamples.async;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;

import com.squareup.picasso.Picasso;

import org.lucasr.layoutsamples.adapter.Tweet;
import org.lucasr.layoutsamples.app.R;
import org.lucasr.layoutsamples.widget.TweetElement;
import org.lucasr.layoutsamples.adapter.TweetPresenter;
import org.lucasr.layoutsamples.canvas.UIElementHost;
import org.lucasr.layoutsamples.canvas.UIElementWrapper;

import java.util.EnumSet;

public class AsyncTweetElement extends UIElementWrapper implements TweetPresenter {
    private final UIElementHost mDefaultHost;

    private final Paint mIndicatorPaint;
    private final int mIndicatorSize;

    public AsyncTweetElement(TweetElement element, UIElementHost defaultHost) {
        super(element);
        mDefaultHost = defaultHost;

        final Resources res = mDefaultHost.getResources();

        mIndicatorPaint = new Paint();
        mIndicatorSize = res.getDimensionPixelSize(R.dimen.tweet_padding);

        boolean onMainThread = (Looper.myLooper() == Looper.getMainLooper());
        final int indicatorColor = onMainThread ? R.color.tweet_on_main_thread :
                                                  R.color.tweet_off_main_thread;

        mIndicatorPaint.setColor(res.getColor(indicatorColor));
    }

    @Override
    public boolean swapHost(UIElementHost host) {
        if (host == null) {
            host = mDefaultHost;
        }

        return super.swapHost(host);
    }

    @Override
    public void measure(int widthMeasureSpec, int heightMeasureSpec) {
        // Do nothing, assume the wrapped UIElement is already measured.
    }

    @Override
    public void layout(int left, int top, int right, int bottom) {
        // Do nothing, assume the wrapped UIElement is already sized and positioned.
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawRect(0, 0, mIndicatorSize, mIndicatorSize, mIndicatorPaint);
    }

    @Override
    public void requestLayout() {
        // Do nothing, we never change the wrapped element's layout.
    }

    @Override
    public void update(Tweet tweet, EnumSet<UpdateFlags> flags) {
        TweetElement element = (TweetElement) getWrappedElement();
        element.loadProfileImage(tweet, flags);

        final boolean hasPostImage = !TextUtils.isEmpty(tweet.getPostImageUrl());
        if (hasPostImage) {
            element.loadPostImage(tweet, flags);
        }
    }
}