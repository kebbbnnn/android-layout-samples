package org.lucasr.layoutsamples.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import org.lucasr.layoutsamples.app.R;

import java.util.HashMap;

public class Shared {

    @SuppressLint("StaticFieldLeak")
    private static Shared sInstance;

    private final Context mContext;

    private final HashMap<Integer, Object> mValues =  new HashMap<>();

    private Shared(Context context) {
        this.mContext = context;
    }

    public static void init(Context context) {
        sInstance = new Shared(context.getApplicationContext());

        sInstance.mValues.put(R.dimen.tweet_padding, Shared.getDimensionPixelOffset(R.dimen.tweet_padding));
        sInstance.mValues.put(R.dimen.tweet_profile_image_size, Shared.getDimensionPixelOffset(R.dimen.tweet_profile_image_size));
        sInstance.mValues.put(R.dimen.tweet_content_margin, Shared.getDimensionPixelOffset(R.dimen.tweet_content_margin));
        sInstance.mValues.put(R.dimen.tweet_author_text_size, (float) Shared.getDimensionPixelSize(R.dimen.tweet_author_text_size));
        sInstance.mValues.put(R.dimen.tweet_message_text_size, (float) Shared.getDimensionPixelSize(R.dimen.tweet_message_text_size));
        sInstance.mValues.put(R.color.tweet_message_text_color, Shared.getColor(R.color.tweet_message_text_color));
        sInstance.mValues.put(R.dimen.tweet_post_image_height, Shared.getDimensionPixelOffset(R.dimen.tweet_post_image_height));
        sInstance.mValues.put(R.dimen.tweet_icon_image_size, Shared.getDimensionPixelOffset(R.dimen.tweet_icon_image_size));
        sInstance.mValues.put(R.drawable.tweet_reply, Shared.getDrawable(R.drawable.tweet_reply));
        sInstance.mValues.put(R.drawable.tweet_retweet, Shared.getDrawable(R.drawable.tweet_retweet));
        sInstance.mValues.put(R.drawable.tweet_favourite, Shared.getDrawable(R.drawable.tweet_favourite));
    }

    private static void checkInit() {
        if (sInstance == null) {
            String thisClassName = Shared.class.getSimpleName();
            throw new RuntimeException("Instance of " + thisClassName + " not initialized." + " Make sure to call "
                    + thisClassName + ".init(context) method in you Application class.");
        }
    }

    public static <T> T get(int id) {
        checkInit();
        return (T) sInstance.mValues.get(id);
    }

    private String getStringRes(@StringRes int id) {
        return mContext.getString(id);
    }

    private Drawable getDrawableRes(@DrawableRes int id) {
        return ContextCompat.getDrawable(mContext, id);
    }

    private int getColorRes(@ColorRes int id){
        return ContextCompat.getColor(mContext, id);
    }

    public int getDimensionPixelOffsetRes(@DimenRes int id) {
        return mContext.getResources().getDimensionPixelOffset(id);
    }

    public int getDimensionPixelSizeRes(@DimenRes int id) {
        return mContext.getResources().getDimensionPixelSize(id);
    }

    public static String getString(@StringRes int id) {
        checkInit();
        return sInstance.getStringRes(id);
    }

    public static Drawable getDrawable(@DrawableRes int id) {
        checkInit();
        return sInstance.getDrawableRes(id);
    }

    public static int getColor(@ColorRes int id) {
        checkInit();
        return sInstance.getColorRes(id);
    }

    public static int getDimensionPixelOffset(@DimenRes int id) {
        checkInit();
        return sInstance.getDimensionPixelOffsetRes(id);
    }

    public static int getDimensionPixelSize(@DimenRes int id) {
        checkInit();
        return sInstance.getDimensionPixelSizeRes(id);
    }
}
