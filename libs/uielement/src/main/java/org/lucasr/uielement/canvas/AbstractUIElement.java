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

package org.lucasr.uielement.canvas;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import org.lucasr.uielement.R;

public abstract class AbstractUIElement implements UIElement {
    protected UIElementHost mHost;

    private int mId;

    private int mMeasuredWidth;
    private int mMeasuredHeight;

    private Rect mBounds = new Rect();
    private Rect mPadding = new Rect();
    private Rect mTouchBounds = new Rect();

    private Paint mBackgroundColorPaint;
    private int mBackgroundColor = 0x00000000;

    private LayoutParams mLayoutParams;

    private UIElement.OnClickListener clickListener;

    private int mVisibility = View.VISIBLE;

    public AbstractUIElement(UIElementHost host) {
        this(host, null);
    }

    public AbstractUIElement(UIElementHost host, AttributeSet attrs) {
        swapHost(host);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.UIElement, 0, 0);

        final int indexCount = a.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            final int attr = a.getIndex(i);

            if (attr == R.styleable.UIElement_android_padding) {
                final int padding = a.getDimensionPixelSize(attr, 0);
                mPadding.left = mPadding.top = mPadding.right = mPadding.bottom = padding;
            } else if (attr == R.styleable.UIElement_android_paddingLeft) {
                mPadding.left = a.getDimensionPixelSize(attr, 0);
            } else if (attr == R.styleable.UIElement_android_paddingTop) {
                mPadding.top = a.getDimensionPixelSize(attr, 0);
            } else if (attr == R.styleable.UIElement_android_paddingRight) {
                mPadding.right = a.getDimensionPixelSize(attr, 0);
            } else if (attr == R.styleable.UIElement_android_paddingBottom) {
                mPadding.bottom = a.getDimensionPixelSize(attr, 0);
            } else if (attr == R.styleable.UIElement_android_id) {
                mId = a.getResourceId(attr, -1);
            } else if (attr == R.styleable.UIElement_android_visibility) {
                mVisibility = a.getInt(attr, View.VISIBLE);
            }
        }

        a.recycle();
    }

    protected void onAttachedToHost() {
    }

    protected void onDetachedFromHost() {
    }

    @Override
    public boolean swapHost(UIElementHost host) {
        if (mHost == host) {
            return false;
        }

        if (mHost != null) {
            onDetachedFromHost();
        }

        mHost = host;

        if (mHost != null) {
            onAttachedToHost();
        }

        return true;
    }

    @Override
    public boolean isAttachedToHost() {
        return (mHost != null);
    }

    @Override
    public int getId() {
        return mId;
    }

    protected void setMeasuredDimension(int width, int height) {
        mMeasuredWidth = width;
        mMeasuredHeight = height;
    }

    @Override
    public int getMeasuredWidth() {
        return mMeasuredWidth;
    }

    @Override
    public int getMeasuredHeight() {
        return mMeasuredHeight;
    }

    @Override
    public int getPaddingLeft() {
        return mPadding.left;
    }

    @Override
    public int getPaddingTop() {
        return mPadding.top;
    }

    @Override
    public int getPaddingRight() {
        return mPadding.right;
    }

    @Override
    public int getPaddingBottom() {
        return mPadding.bottom;
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        mPadding.left = left;
        mPadding.top = top;
        mPadding.right = right;
        mPadding.bottom = bottom;

        requestLayout();
    }

    @Override
    public int getVisibility() {
        return mVisibility;
    }

    @Override
    public void setVisibility(int visibility) {
        if (mVisibility == visibility) {
            return;
        }

        mVisibility = visibility;

        requestLayout();
        invalidate();
    }

    @Override
    public void setOnClickListener(UIElement.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (this instanceof UIElementGroup) {
            UIElementGroup elementGroup = (UIElementGroup) this;
            for (int i = 0, size = elementGroup.getChildCount(); i < size; i++) {
                UIElement element = elementGroup.getChildAt(i);

                if (!element.dispatchTouchEvent(event)) {
                    return true;
                }
            }
        } else {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                // Construct a rect of the view's bounds
                mTouchBounds = new Rect(mBounds.left, mBounds.top, mBounds.right, mBounds.bottom);
            }

            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(mTouchBounds != null && mTouchBounds.contains((int) event.getX(), (int) event.getY())) {
                    // User moved inside bounds
                    if (this.clickListener != null) {
                        this.clickListener.onClick(this);
                    }
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public final void draw(Canvas canvas) {
        final int saveCount = canvas.getSaveCount();
        canvas.save();

        if (mBackgroundColorPaint != null) {
            canvas.drawRect(mBounds.left, mBounds.top, mBounds.right, mBounds.bottom, mBackgroundColorPaint);
        }

        canvas.clipRect(mBounds);
        canvas.translate(mBounds.left, mBounds.top);

        onDraw(canvas);

        canvas.restoreToCount(saveCount);
    }


    @Override
    public final void measure(int widthMeasureSpec, int heightMeasureSpec) {
        onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public final void layout(int left, int top, int right, int bottom) {
        mBounds.left = left;
        mBounds.top = top;
        mBounds.right = right;
        mBounds.bottom = bottom;

        onLayout(left, top, right, bottom);
    }

    @Override
    public int getLeft() {
        return mBounds.left;
    }

    @Override
    public int getTop() {
        return mBounds.top;
    }

    @Override
    public int getRight() {
        return mBounds.right;
    }

    @Override
    public int getBottom() {
        return mBounds.bottom;
    }

    @Override
    public int getWidth() {
        return mBounds.right - mBounds.left;
    }

    @Override
    public int getHeight() {
        return mBounds.bottom - mBounds.top;
    }

    @Override
    public void setLayoutParams(LayoutParams lp) {
        if (mLayoutParams == lp) {
            return;
        }

        mLayoutParams = lp;
        requestLayout();
    }

    @Override
    public LayoutParams getLayoutParams() {
        return mLayoutParams;
    }

    @Override
    public void onFinishInflate() {
    }

    @Override
    public Context getContext() {
        return mHost.getContext();
    }

    @Override
    public Resources getResources() {
        return mHost.getResources();
    }

    @Override
    public void setBackgroundColor(int color) {
        if (mBackgroundColor == color) {
            return;
        }
        mBackgroundColor = color;

        if (mBackgroundColorPaint == null) {
            mBackgroundColorPaint = new Paint();
        }

        mBackgroundColorPaint.setColor(mBackgroundColor);

        requestLayout();
    }

    @Override
    public void requestLayout() {
        mHost.requestLayout();
    }

    @Override
    public void invalidate() {
        mHost.invalidate(mBounds.left, mBounds.top, mBounds.right, mBounds.bottom);
    }

    protected abstract void onDraw(Canvas canvas);

    protected abstract void onMeasure(int widthMeasureSpec, int heightMeasureSpec);

    protected abstract void onLayout(int left, int top, int right, int bottom);

    public abstract void drawableStateChanged();
}
