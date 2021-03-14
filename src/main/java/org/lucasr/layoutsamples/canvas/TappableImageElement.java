package org.lucasr.layoutsamples.canvas;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import org.lucasr.uielement.canvas.ImageElement;
import org.lucasr.uielement.canvas.UIElementHost;

public class TappableImageElement extends ImageElement {
    private static final Interpolator ACCELERATE_DECELERATE_INTERPOLATOR = new AccelerateDecelerateInterpolator();
    private static final long ANIMATION_DURATION = 200;

    private final ValueAnimator mValueAnimator = ValueAnimator.ofInt(100, 0);
    private final float[] mDrawMatrixValues = new float[9];

    private boolean isMatrixSaved = false;

    private float mScalePercentage = 1.0f;
    private long mCurrentPlayTime = 0;

    public TappableImageElement(UIElementHost host) {
        this(host, null);
    }
    public TappableImageElement(UIElementHost host, AttributeSet attrs) {
        super(host, attrs);

        mValueAnimator.setDuration(ANIMATION_DURATION);
        mValueAnimator.setInterpolator(ACCELERATE_DECELERATE_INTERPOLATOR);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentPlayTime = animation.getCurrentPlayTime();
                int value = (int) animation.getAnimatedValue();
                if (value < 50) { value = 100 - value; }
                mScalePercentage = (float) value / 100;

                if (mCurrentPlayTime < ANIMATION_DURATION && mScalePercentage > 0.0f) {
                    invalidate();
                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if ((mCurrentPlayTime > 0.0f && mCurrentPlayTime < ANIMATION_DURATION) && mScalePercentage > 0.0f) {
            if (!isMatrixSaved) {
                mDrawMatrix.getValues(mDrawMatrixValues);
                isMatrixSaved = true;
            }
            if (((Math.round(mScalePercentage * 10.0f) / 10.0f) == 1.0f) && (((Math.round((mCurrentPlayTime / (float) ANIMATION_DURATION) * 10.0f) / 10.0f) + 0.1f) == 1.0f)) {
                mDrawMatrix.setScale(mDrawMatrixValues[Matrix.MSCALE_X], mDrawMatrixValues[Matrix.MSCALE_Y]);
                mDrawMatrix.postTranslate(mDrawMatrixValues[Matrix.MTRANS_X], mDrawMatrixValues[Matrix.MTRANS_Y]);
            } else {
                float scaleX = mDrawMatrixValues[Matrix.MSCALE_X] * mScalePercentage;
                float scaleY = mDrawMatrixValues[Matrix.MSCALE_Y] * mScalePercentage;

                final int drawableWidth = mDrawableWidth;
                final int drawableHeight = mDrawableHeight;

                final int viewWidth = getWidth() - getPaddingLeft() - getPaddingRight();
                final int viewHeight = getHeight() - getPaddingTop() - getPaddingBottom();

                float dx = ((Math.min(viewWidth, viewHeight) - drawableWidth * scaleX) * 0.5f + 0.5f);
                float dy = ((Math.min(viewWidth, viewHeight) - drawableHeight * scaleY) * 0.5f + 0.5f);

                mDrawMatrix.setScale(scaleX, scaleY);
                mDrawMatrix.postTranslate(dx, dy);
            }
        }

        super.onDraw(canvas);
    }

    @Override
    public boolean callOnClick() {
        mValueAnimator.start();

        boolean clicked = super.callOnClick();
        View grandParentView = grandParentView();
        Log.e(TappableImageElement.class.getName(), "clicked: " + clicked + ", grandParentView: " + grandParentView);
        if (clicked && grandParentView != null) {
            grandParentView.getRootView().performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
        }
        return clicked;
    }

    private View grandParentView() {
        Window window = ((Activity) getContext()).getWindow();
        if (window != null) {
            return window.getDecorView();
        }
        return null;
    }

}
