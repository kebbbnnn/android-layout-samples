package org.lucasr.layoutsamples.canvas;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;

import org.lucasr.uielement.canvas.ImageElement;
import org.lucasr.uielement.canvas.UIElementHost;

public class TappableImageElement extends ImageElement {

    private final ValueAnimator mValueAnimator = ValueAnimator.ofInt(100, 0);
    private final float[] mDrawMatrixValues = new float[9];
    private boolean mIsMatrixSaved = false;
    private float mScalePercentage = 1.0f;
    private final long DURATION = 500;
    private long mCurrentPlayTime = 0;

    public TappableImageElement(UIElementHost host) {
        this(host, null);
    }
    public TappableImageElement(UIElementHost host, AttributeSet attrs) {
        super(host, attrs);

        mValueAnimator.setDuration(200);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentPlayTime = animation.getCurrentPlayTime();
                int value = (int) animation.getAnimatedValue();
                if (value < 50) { value = 100 - value; }
                mScalePercentage = (float) value / 100;
                if (mCurrentPlayTime < DURATION && mScalePercentage > 0.0f) {
                    invalidate();
                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mCurrentPlayTime > 0.0f && mCurrentPlayTime < DURATION && mScalePercentage > 0.0f) {
            if (!mIsMatrixSaved) {
                mDrawMatrix.getValues(mDrawMatrixValues);
                mIsMatrixSaved = true;
            }

            float scaleX = mDrawMatrixValues[Matrix.MSCALE_X] * mScalePercentage;
            float scaleY = mDrawMatrixValues[Matrix.MSCALE_Y] * mScalePercentage;

            final int drawableWidth = mDrawableWidth;
            final int drawableHeight = mDrawableHeight;

            final int viewWidth = getWidth() - getPaddingLeft() - getPaddingRight();
            final int viewHeight = getHeight() - getPaddingTop() - getPaddingBottom();

            float dx = ((Math.min(viewWidth, viewHeight) - drawableWidth * scaleX) * 0.5f + 0.5f);
            float dy = ((Math.min(viewWidth, viewHeight) - drawableHeight * scaleY) * 0.5f + 0.5f);

            if (mScalePercentage == 1.0f) {
                mDrawMatrix.setScale(mDrawMatrixValues[Matrix.MSCALE_X], mDrawMatrixValues[Matrix.MSCALE_Y]);
                mDrawMatrix.postTranslate(mDrawMatrixValues[Matrix.MTRANS_X], mDrawMatrixValues[Matrix.MTRANS_Y]);
                mScalePercentage = 0.0f;
            } else {
                mDrawMatrix.setScale(scaleX, scaleY);
                mDrawMatrix.postTranslate(dx, dy);
            }
        }

        super.onDraw(canvas);
    }

    @Override
    public boolean callOnClick() {
        mValueAnimator.start();
        return super.callOnClick();
    }
}
