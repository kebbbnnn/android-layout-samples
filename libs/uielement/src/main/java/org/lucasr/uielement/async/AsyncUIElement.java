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

package org.lucasr.uielement.async;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Looper;

import org.lucasr.uielement.adapter.ElementPresenter;
import org.lucasr.uielement.adapter.ImagePresenter;
import org.lucasr.uielement.adapter.UpdateFlags;
import org.lucasr.uielement.cache.Hashable;
import org.lucasr.uielement.canvas.UIElementGroup;
import org.lucasr.uielement.canvas.UIElementWrapper;

import java.util.EnumSet;

public class AsyncUIElement<E extends UIElementGroup, O extends Hashable> extends UIElementWrapper implements ElementPresenter<O> {
    private final Paint mIndicatorPaint;
    private final static int INDICATOR_SIZE = 15;

    public AsyncUIElement(E element) {
        super(element);

        mIndicatorPaint = new Paint();

        boolean onMainThread = (Looper.myLooper() == Looper.getMainLooper());
        final int indicatorColor = onMainThread ? 0xFFFF0000 : 0xFF66CD00;

        mIndicatorPaint.setColor(indicatorColor);
    }

    @Override
    public void measure(int widthMeasureSpec, int heightMeasureSpec) {
        // Do nothing, the wrapped UIElement is already measured.
    }

    @Override
    public void layout(int left, int top, int right, int bottom) {
        // Do nothing, the wrapped UIElement is already sized and positioned.
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawRect(0, 0, INDICATOR_SIZE, INDICATOR_SIZE, mIndicatorPaint);
    }

    @Override
    public void requestLayout() {
        // Do nothing, we never change the wrapped element's layout.
    }

    @SuppressWarnings("unchecked")
    @Override
    public void update(O object, EnumSet<UpdateFlags> flags) {
        ImagePresenter<O> presenter = (ImagePresenter<O>) getWrappedElement();
        presenter.load(object, flags);
    }
}
