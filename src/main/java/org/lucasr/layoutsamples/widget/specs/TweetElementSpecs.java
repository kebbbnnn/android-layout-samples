package org.lucasr.layoutsamples.widget.specs;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;

import org.lucasr.layoutsamples.app.R;
import org.lucasr.layoutsamples.util.Shared;

import java.util.HashMap;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public final class TweetElementSpecs {
    public static final int PADDING = 15;
    public static final int PROFILE_IMAGE_SIZE = 90;
    public static final int CONTENT_MARGIN = 15;
    public static final int POST_IMAGE_HEIGHT = 195;
    public static final int ICON_IMAGE_SIZE = 30;

    public static final float AUTHOR_TEXT_SIZE = 24.0f;
    public static final float MESSAGE_TEXT_SIZE = 24.0f;

    public static final int MESSAGE_TEXT_COLOR = Shared.shared.getColor(R.color.tweet_message_text_color);

    public static final Drawable DRAWABLE_REPLY = Shared.shared.getDrawable(R.drawable.tweet_reply);
    public static final Drawable DRAWABLE_RETWEET = Shared.shared.getDrawable(R.drawable.tweet_retweet);
    public static final Drawable DRAWABLE_FAVOURITE = Shared.shared.getDrawable(R.drawable.tweet_favourite);

    private static final HashMap<Integer, ViewGroup.LayoutParams> paramsMap = new HashMap<>();

    public static MarginLayoutParams getProfileImageParams() {
        ViewGroup.MarginLayoutParams params = (MarginLayoutParams) paramsMap.get(0);
        if (null == params) {
            int size = PROFILE_IMAGE_SIZE;
            params = new ViewGroup.MarginLayoutParams(size, size);
            params.rightMargin = CONTENT_MARGIN;
            paramsMap.put(0, params);
        }
        return params;
    }

    public static LayoutParams getAuthorTextParams() {
        ViewGroup.LayoutParams params = paramsMap.get(1);
        if (null == params) {
            params = new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            paramsMap.put(1, params);
        }
        return params;
    }

    public static MarginLayoutParams getMessageTextParams() {
        ViewGroup.MarginLayoutParams params = (MarginLayoutParams) paramsMap.get(2);
        if (null == params) {
            params = new ViewGroup.MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT);
            params.bottomMargin = CONTENT_MARGIN;
            paramsMap.put(2, params);
        }
        return params;
    }

    public static MarginLayoutParams getPostImageParams() {
        ViewGroup.MarginLayoutParams params = (MarginLayoutParams) paramsMap.get(3);
        if (null == params) {
            params = new ViewGroup.MarginLayoutParams(MATCH_PARENT, POST_IMAGE_HEIGHT);
            params.bottomMargin = CONTENT_MARGIN;
            paramsMap.put(3, params);
        }
        return params;
    }

    public static LayoutParams getActionElementParams() {
        ViewGroup.LayoutParams params = paramsMap.get(4);
        if (null == params) {
            params = new ViewGroup.LayoutParams(ICON_IMAGE_SIZE, ICON_IMAGE_SIZE);
            paramsMap.put(4, params);
        }
        return params;
    }
}
