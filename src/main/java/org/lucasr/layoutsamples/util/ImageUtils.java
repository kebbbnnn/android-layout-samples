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

package org.lucasr.layoutsamples.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import org.lucasr.layoutsamples.adapter.ElementPresenter.UpdateFlags;
import org.lucasr.layoutsamples.app.R;
import org.lucasr.layoutsamples.widget.ImageElementTarget;
import org.lucasr.uielement.canvas.ImageElement;

import java.util.EnumSet;

public class ImageUtils {
    public static final Drawable PLACEHOLDER_DRAWABLE_CIRCLE = Shared.shared.getDrawable(R.drawable.circle_shape);
    public static final Drawable PLACEHOLDER_DRAWABLE_RECTANGLE = Shared.shared.getDrawable(R.drawable.tweet_placeholder_image);

    private static final Transformation CIRCLE_TRANSFORMATION = new CircleTransform();

    private ImageUtils() {}

    public static void loadImage(Context context, ImageView view, String url,
                                 EnumSet<UpdateFlags> flags) {
        if (!flags.contains(UpdateFlags.NO_IMAGE_LOADING)) {
            //Picasso.with(context)
            Picasso.get()
                    .load(url)
                    .transform(CIRCLE_TRANSFORMATION)
                    .placeholder(R.drawable.tweet_placeholder_image)
                    .error(R.drawable.tweet_placeholder_image)
                    .into(view);
        } else {
            view.setImageResource(R.drawable.tweet_placeholder_image);
        }
    }

    public static void loadImage(Context context, ImageElement element,
                                 ImageElementTarget target, String url,
                                 EnumSet<UpdateFlags> flags, boolean circular) {
        if (!flags.contains(UpdateFlags.NO_IMAGE_LOADING)) {
            //Picasso.with(context)
            RequestCreator requestCreator = Picasso.get().load(url);
            if (circular) {
                requestCreator.transform(CIRCLE_TRANSFORMATION);
            }
            if (circular) {
                requestCreator
                        .placeholder(PLACEHOLDER_DRAWABLE_CIRCLE)
                        .error(PLACEHOLDER_DRAWABLE_CIRCLE);
            } else {
                requestCreator
                        .placeholder(PLACEHOLDER_DRAWABLE_RECTANGLE)
                        .error(PLACEHOLDER_DRAWABLE_RECTANGLE);
            }

            requestCreator.into(target);
        } else {
            if (circular) {
                //element.setImageResource(R.drawable.circle_shape);
                element.setImageDrawable(PLACEHOLDER_DRAWABLE_CIRCLE);
            } else {
                //element.setImageResource(R.drawable.tweet_placeholder_image);
                element.setImageDrawable(PLACEHOLDER_DRAWABLE_RECTANGLE);
            }
        }
    }

    public static class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}
