package org.lucasr.layoutsamples.widget;

import android.view.ViewGroup;

import org.lucasr.layoutsamples.app.R;
import org.lucasr.layoutsamples.util.Shared;

import java.util.HashMap;

public class TweetElementLayout {
    private static class SingletonHelper {
        private static final TweetElementLayout INSTANCE = new TweetElementLayout();
    }
    public enum TweetElementParams {
        PROFILE_IMAGE, AUTHOR_TEXT, MESSAGE_TEXT, POST_IMAGE, ACTION_ELEMENT;
    }
    public enum TweetElementResources {
        PADDING, PROFILE_IMAGE_SIZE, CONTENT_MARGIN, AUTHOR_TEXT_SIZE, MESSAGE_TEXT_SIZE, MESSAGE_TEXT_COLOR, POST_IMAGE_HEIGHT, ICON_IMAGE_SIZE, DRAWABLE_REPLY, DRAWABLE_RETWEET, DRAWABLE_FAVOURITE
    }

    public static TweetElementLayout shared = SingletonHelper.INSTANCE;
    //public static TweetElementLayout shared = new TweetElementLayout();

    private final HashMap<TweetElementParams, ViewGroup.LayoutParams> mLayoutParams = new HashMap<>();
    private final HashMap<TweetElementResources, Object> mResourcesMap = new HashMap<>();

    private TweetElementLayout() {}

    public void warmup() {
        mResourcesMap.put(TweetElementResources.PADDING, Shared.shared.getDimensionPixelOffset(R.dimen.tweet_padding));
        mResourcesMap.put(TweetElementResources.PROFILE_IMAGE_SIZE, Shared.shared.getDimensionPixelOffset(R.dimen.tweet_profile_image_size));
        mResourcesMap.put(TweetElementResources.CONTENT_MARGIN, Shared.shared.getDimensionPixelOffset(R.dimen.tweet_content_margin));
        mResourcesMap.put(TweetElementResources.AUTHOR_TEXT_SIZE, (float) Shared.shared.getDimensionPixelSize(R.dimen.tweet_author_text_size));
        mResourcesMap.put(TweetElementResources.MESSAGE_TEXT_SIZE, (float) Shared.shared.getDimensionPixelSize(R.dimen.tweet_message_text_size));
        mResourcesMap.put(TweetElementResources.MESSAGE_TEXT_COLOR, Shared.shared.getColor(R.color.tweet_message_text_color));
        mResourcesMap.put(TweetElementResources.POST_IMAGE_HEIGHT, Shared.shared.getDimensionPixelOffset(R.dimen.tweet_post_image_height));
        mResourcesMap.put(TweetElementResources.ICON_IMAGE_SIZE, Shared.shared.getDimensionPixelOffset(R.dimen.tweet_icon_image_size));
        mResourcesMap.put(TweetElementResources.DRAWABLE_REPLY, Shared.shared.getDrawable(R.drawable.tweet_reply));
        mResourcesMap.put(TweetElementResources.DRAWABLE_RETWEET, Shared.shared.getDrawable(R.drawable.tweet_retweet));
        mResourcesMap.put(TweetElementResources.DRAWABLE_FAVOURITE, Shared.shared.getDrawable(R.drawable.tweet_favourite));

        ViewGroup.MarginLayoutParams profileImageParams = new ViewGroup.MarginLayoutParams(
                (int) get(TweetElementResources.PROFILE_IMAGE_SIZE),
                (int) get(TweetElementResources.PROFILE_IMAGE_SIZE)
        );
        profileImageParams.rightMargin = (int) get(TweetElementResources.CONTENT_MARGIN);
        mLayoutParams.put(TweetElementParams.PROFILE_IMAGE, profileImageParams);

        ViewGroup.LayoutParams authorTextParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        mLayoutParams.put(TweetElementParams.AUTHOR_TEXT, authorTextParams);

        ViewGroup.MarginLayoutParams messageTextParams = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        messageTextParams.bottomMargin = (int) get(TweetElementResources.CONTENT_MARGIN);
        mLayoutParams.put(TweetElementParams.MESSAGE_TEXT, messageTextParams);

        ViewGroup.MarginLayoutParams postImageParams = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int) get(TweetElementResources.POST_IMAGE_HEIGHT)
        );
        postImageParams.bottomMargin = (int) get(TweetElementResources.CONTENT_MARGIN);
        mLayoutParams.put(TweetElementParams.POST_IMAGE, postImageParams);

        ViewGroup.LayoutParams actionElementParams = new ViewGroup.LayoutParams(
                (int) get(TweetElementResources.ICON_IMAGE_SIZE),
                (int) get(TweetElementResources.ICON_IMAGE_SIZE)
        );
        mLayoutParams.put(TweetElementParams.ACTION_ELEMENT, actionElementParams);
    }

    public ViewGroup.LayoutParams get(TweetElementParams key) {
        return shared.mLayoutParams.get(key);
    }

    public Object get(TweetElementResources key) {
        return shared.mResourcesMap.get(key);
    }
}
