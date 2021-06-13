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

import android.content.Context;
import android.util.AttributeSet;

import org.lucasr.layoutsamples.adapter.Tweet;
import org.lucasr.uielement.adapter.UpdateFlags;
import org.lucasr.uielement.adapter.UIElementViewPresenter;
import org.lucasr.uielement.async.AsyncUIElementProvider;
import org.lucasr.uielement.canvas.UIElementView;

import java.util.EnumSet;

public class TweetElementView extends UIElementView implements UIElementViewPresenter<Tweet> {
    public TweetElementView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TweetElementView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUIElement(new TweetElement(this));
    }

    @Override
    public void update(Tweet tweet, EnumSet<UpdateFlags> flags) {
        TweetElement element = (TweetElement) getUIElement();
        element.update(tweet, flags);
    }

    @Override
    public boolean hasAsyncUIElementProvider() {
        return false;
    }

    @Override
    public void setAsyncUIElementProvider(AsyncUIElementProvider<Tweet> provider) {
        // We don't need async ui element provider here
    }
}
