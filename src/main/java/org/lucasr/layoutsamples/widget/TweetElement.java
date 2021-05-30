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

package org.lucasr.layoutsamples.widget;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.lucasr.layoutsamples.adapter.Action;
import org.lucasr.layoutsamples.adapter.Tweet;
import org.lucasr.layoutsamples.canvas.TappableImageElement;
import org.lucasr.layoutsamples.util.ImageUtils;
import org.lucasr.uielement.adapter.ElementPresenter;
import org.lucasr.uielement.canvas.ImageElement;
import org.lucasr.uielement.canvas.TextElement;
import org.lucasr.uielement.canvas.UIElement;
import org.lucasr.uielement.canvas.UIElementGroup;
import org.lucasr.uielement.canvas.UIElementHost;

import java.util.EnumMap;
import java.util.EnumSet;

public class TweetElement extends UIElementGroup implements ElementPresenter<Tweet> {
    private final ImageElement mProfileImage;
    private final TextElement mAuthorText;
    private final TextElement mMessageText;
    private final ImageElement mPostImage;
    private final EnumMap<Action, UIElement> mActionIcons;

    private final ImageElementTarget mProfileImageTarget;
    private final ImageElementTarget mPostImageTarget;

    public TweetElement(UIElementHost host) {
        this(host, null);
    }

    public TweetElement(UIElementHost host, AttributeSet attrs) {
        super(host, attrs);

        final Resources res = getResources();

        //int padding = Shared.getDimensionPixelOffset(R.dimen.tweet_padding);
        int padding = (int) TweetElementLayout.shared.get(TweetElementLayout.TweetElementResources.PADDING);
        setPadding(padding, padding, padding, padding);

        long startTime = System.nanoTime();
//        UIElementInflater.from(getContext()).inflate(R.layout.tweet_element_view, host, this);

        //mProfileImage = (ImageElement) findElementById(R.id.profile_image);
        mProfileImage = new ImageElement(host);
        mProfileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addElement(mProfileImage, (ViewGroup.MarginLayoutParams) TweetElementLayout.shared.get(TweetElementLayout.TweetElementParams.PROFILE_IMAGE));

        //mAuthorText = (TextElement) findElementById(R.id.author_text);
        mAuthorText = new TextElement(host);
        mAuthorText.setRawTextSize((float) TweetElementLayout.shared.get(TweetElementLayout.TweetElementResources.AUTHOR_TEXT_SIZE));
        mAuthorText.setEllipsize(TextUtils.TruncateAt.END);
        mAuthorText.setMaxLines(1);
        addElement(mAuthorText, TweetElementLayout.shared.get(TweetElementLayout.TweetElementParams.AUTHOR_TEXT));

        //mMessageText = (TextElement) findElementById(R.id.message_text);
        mMessageText = new TextElement(host);
        mMessageText.setRawTextSize((float) TweetElementLayout.shared.get(TweetElementLayout.TweetElementResources.MESSAGE_TEXT_SIZE));
        mMessageText.setTextColor((int) TweetElementLayout.shared.get(TweetElementLayout.TweetElementResources.MESSAGE_TEXT_COLOR));
        addElement(mMessageText, (ViewGroup.MarginLayoutParams) TweetElementLayout.shared.get(TweetElementLayout.TweetElementParams.MESSAGE_TEXT));

        //mPostImage = (ImageElement) findElementById(R.id.post_image);
        mPostImage = new ImageElement(host);
        mPostImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addElement(mPostImage, (ViewGroup.MarginLayoutParams) TweetElementLayout.shared.get(TweetElementLayout.TweetElementParams.POST_IMAGE));

        mProfileImageTarget = new ImageElementTarget(res, mProfileImage, true);
        mPostImageTarget = new ImageElementTarget(res, mPostImage, false);

        //mMessageText.setBackgroundColor(Color.RED);

        mActionIcons = new EnumMap(Action.class);
        for (final Action action : Action.values()) {
            //final int elementId;
            final TweetElementLayout.TweetElementResources drawableKey;
            switch (action) {
                case REPLY:
                    //elementId = R.id.reply_action;
                    drawableKey = TweetElementLayout.TweetElementResources.DRAWABLE_REPLY;
                    break;

                case RETWEET:
                    //elementId = R.id.retweet_action;
                    drawableKey = TweetElementLayout.TweetElementResources.DRAWABLE_RETWEET;
                    break;

                case FAVOURITE:
                    //elementId = R.id.favourite_action;
                    drawableKey = TweetElementLayout.TweetElementResources.DRAWABLE_FAVOURITE;
                    break;

                default:
                    throw new IllegalArgumentException("Unrecognized tweet action");
            }
            //UIElement actionElement = findElementById(elementId);
            TappableImageElement actionElement = new TappableImageElement(host);
            actionElement.setScaleType(ImageView.ScaleType.FIT_START);
            actionElement.setImageDrawable((Drawable) TweetElementLayout.shared.get(drawableKey));
            addElement(actionElement, TweetElementLayout.shared.get(TweetElementLayout.TweetElementParams.ACTION_ELEMENT));

            //Random rnd = new Random();
            //int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            //actionElement.setBackgroundColor(color);
            actionElement.setOnClickListener(new UIElement.OnClickListener() {
                @Override
                public void onClick(UIElement element) {
                    Log.d(TweetElement.class.getName(), "Action clicked: " + action);
                    ((Activity) getContext()).getWindow().getDecorView().performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
                }
            });

            mActionIcons.put(action, actionElement);
        }
        long endTime = System.nanoTime();
        double duration = ((double) (endTime - startTime)) / 1000000;
        Log.e(TweetElement.class.getName(), "Finished at " + duration + "ms");
    }

    private void layoutElement(UIElement element, int left, int top, int width, int height) {
        MarginLayoutParams margins = (MarginLayoutParams) element.getLayoutParams();
        final int leftWithMargins = left + margins.leftMargin;
        final int topWithMargins = top + margins.topMargin;

        element.layout(leftWithMargins, topWithMargins,
                       leftWithMargins + width, topWithMargins + height);
    }

    private int getWidthWithMargins(UIElement element) {
        final MarginLayoutParams lp = (MarginLayoutParams) element.getLayoutParams();
        return element.getWidth() + lp.leftMargin + lp.rightMargin;
    }

    private int getHeightWithMargins(UIElement element) {
        final MarginLayoutParams lp = (MarginLayoutParams) element.getLayoutParams();
        return element.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
    }

    private int getMeasuredWidthWithMargins(UIElement element) {
        final MarginLayoutParams lp = (MarginLayoutParams) element.getLayoutParams();
        return element.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
    }

    private int getMeasuredHeightWithMargins(UIElement element) {
        final MarginLayoutParams lp = (MarginLayoutParams) element.getLayoutParams();
        return element.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
    }

    private void cancelImageRequest(Target target) {
        if (!isAttachedToHost() || target == null) {
            return;
        }

        Picasso.get().cancelRequest(target);
        //Picasso.with(getContext()).cancelRequest(target);
    }

    @Override
    public boolean swapHost(UIElementHost host) {
        if (host == null) {
            cancelImageRequest(mProfileImageTarget);
            cancelImageRequest(mPostImageTarget);
        }

        return super.swapHost(host);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int widthUsed = 0;
        int heightUsed = 0;

        measureElementWithMargins(mProfileImage,
                                  widthMeasureSpec, widthUsed,
                                  heightMeasureSpec, heightUsed);
        widthUsed += getMeasuredWidthWithMargins(mProfileImage);

        measureElementWithMargins(mAuthorText,
                                  widthMeasureSpec, widthUsed,
                                  heightMeasureSpec, heightUsed);
        heightUsed += getMeasuredHeightWithMargins(mAuthorText);

        measureElementWithMargins(mMessageText,
                                  widthMeasureSpec, widthUsed,
                                  heightMeasureSpec, heightUsed);
        heightUsed += getMeasuredHeightWithMargins(mMessageText);

        if (mPostImage.getVisibility() != View.GONE) {
            measureElementWithMargins(mPostImage,
                                      widthMeasureSpec, widthUsed,
                                      heightMeasureSpec, heightUsed);
            heightUsed += getMeasuredHeightWithMargins(mPostImage);
        }

        int maxIconHeight = 0;
        for (Action action : Action.values()) {
            final UIElement icon = mActionIcons.get(action);
            measureElementWithMargins(icon,
                                      widthMeasureSpec, widthUsed,
                                      heightMeasureSpec, heightUsed);

            final int height = getMeasuredHeightWithMargins(icon);
            if (height > maxIconHeight) {
                maxIconHeight = height;
            }

            widthUsed += getMeasuredWidthWithMargins(icon);
        }
        heightUsed += maxIconHeight;

        int heightSize = heightUsed + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    public void onLayout(int l, int t, int r, int b) {
        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();

        int currentTop = paddingTop;

        layoutElement(mProfileImage, paddingLeft, currentTop,
                      mProfileImage.getMeasuredWidth(),
                      mProfileImage.getMeasuredHeight());

        final int contentLeft = getWidthWithMargins(mProfileImage) + paddingLeft;
        final int contentWidth = r - l - contentLeft - getPaddingRight();

        layoutElement(mAuthorText, contentLeft, currentTop,
                      contentWidth, mAuthorText.getMeasuredHeight());
        currentTop += getHeightWithMargins(mAuthorText);

        layoutElement(mMessageText, contentLeft, currentTop,
                      contentWidth, mMessageText.getMeasuredHeight());
        currentTop += getHeightWithMargins(mMessageText);

        if (mPostImage.getVisibility() != View.GONE) {
            layoutElement(mPostImage, contentLeft, currentTop,
                    contentWidth, mPostImage.getMeasuredHeight());

            currentTop += getHeightWithMargins(mPostImage);
        }

        final int iconsWidth = contentWidth / mActionIcons.size();
        int iconsLeft = contentLeft;

        for (Action action : Action.values()) {
            final UIElement icon = mActionIcons.get(action);

            layoutElement(icon, iconsLeft, currentTop,
                          iconsWidth, icon.getMeasuredHeight());
            iconsLeft += iconsWidth;
        }
    }

    public void loadProfileImage(Tweet tweet, EnumSet<UpdateFlags> flags) {
        ImageUtils.loadImage(getContext(), mProfileImage, mProfileImageTarget,
                tweet.getProfileImageUrl(), flags, true);
    }

    public void loadPostImage(Tweet tweet, EnumSet<UpdateFlags> flags) {
        ImageUtils.loadImage(getContext(), mPostImage, mPostImageTarget,
                tweet.getPostImageUrl(), flags, false);
    }

    @Override
    public void update(Tweet tweet, EnumSet<UpdateFlags> flags) {
        mAuthorText.setText(tweet.getAuthorName());
        mMessageText.setText(tweet.getMessage());

        loadProfileImage(tweet, flags);

        final boolean hasPostImage = !TextUtils.isEmpty(tweet.getPostImageUrl());
        mPostImage.setVisibility(hasPostImage ? View.VISIBLE : View.GONE);
        if (hasPostImage) {
            loadPostImage(tweet, flags);
        }
    }
}
