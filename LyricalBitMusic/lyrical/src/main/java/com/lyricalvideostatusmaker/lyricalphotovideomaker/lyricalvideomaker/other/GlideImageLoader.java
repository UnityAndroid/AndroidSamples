package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class GlideImageLoader {

    public static boolean isValidContextForGlide(final Context context) {

        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isFinishing()) {
                return false;
            }
        }
        return true;
    }

    public static void endGlideProcess(Context context) {
        Glide.with(context).pauseRequests();
    }

    public static void SetImageResource(Context context, ImageView view, int Resource, DiskCacheStrategy diskCacheStrategy) {
        if (isValidContextForGlide(context)) {
            if (diskCacheStrategy != null) {
                Glide.with(context).load(Resource).diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
            } else {
                Glide.with(context).load(Resource).diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
            }
        }
    }

    public static void SetImageUrl(Context context, ImageView view, String Resource, DiskCacheStrategy diskCacheStrategy) {
        if (isValidContextForGlide(context)) {
            if (diskCacheStrategy != null) {
                Glide.with(context).load(Resource).diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
            } else {
                Glide.with(context).load(Resource).diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
            }
        }
    }

    public static void SetImageUrl(Context context, ImageView view, String Resource,int PlaceHolder, DiskCacheStrategy diskCacheStrategy) {
        if (isValidContextForGlide(context)) {
            if (diskCacheStrategy != null) {
                Glide.with(context).load(Resource).placeholder(PlaceHolder).diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
            } else {
                Glide.with(context).load(Resource).placeholder(PlaceHolder).diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
            }
        }
    }



}
