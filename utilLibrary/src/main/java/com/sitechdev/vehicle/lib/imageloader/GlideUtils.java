package com.sitechdev.vehicle.lib.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sitechdev.vehicle.lib.util.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片加载工具类
 *
 * @author liuhe
 * @date 2018/1/28
 */
public class GlideUtils {

    private static final GlideUtils INSTANCE = new GlideUtils();

    private GlideUtils() {
    }

    public static GlideUtils getInstance() {
        return INSTANCE;
    }

    public void loadImage(Object object, ImageView view) {
        loadImage(getDefaultOptions(object, view));
    }

    public void loadImage(@NonNull final ImageLoaderOptions options) {
        RequestOptions requestOptions = new RequestOptions();
        /*
         * 1）placeholder 正在请求图片的时候展示的图片
         * 2）error  如果请求失败的时候展示的图片 （如果没有设置，还是展示placeholder的占位符）
         * 3）fallback  如果请求的url/model为 null 的时候展示的图片 （如果没有设置，还是展示placeholder的占位符）
         */
        if (null == options.getSource()) {
            requestOptions.fallback(R.drawable.emptyview);
        }
        if (options.getHolderDrawable() != -1) {
            requestOptions.placeholder(options.getHolderDrawable());
        } else {
//            requestOptions.placeholder(R.drawable.emptyview);
        }
        if (options.getErrorDrawable() != -1) {
            requestOptions.error(options.getErrorDrawable());
        } else {
            requestOptions.error(R.drawable.emptyview);
        }
        if (options.getImageSize() != null) {
            requestOptions.override(options.getImageSize().getWidth(), options.getImageSize().getHeight());
        }
        if (options.isSkipMemoryCache()) {
            requestOptions.skipMemoryCache(true);
        }
        if (options.getDiskCacheStrategy() != ImageLoaderOptions.DiskCacheStrategy.DEFAULT) {
            if (ImageLoaderOptions.DiskCacheStrategy.NONE == options.getDiskCacheStrategy()) {
                requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            } else if (ImageLoaderOptions.DiskCacheStrategy.All == options.getDiskCacheStrategy()) {
                requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
            } else if (ImageLoaderOptions.DiskCacheStrategy.SOURCE == options.getDiskCacheStrategy()) {
                requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            } else if (ImageLoaderOptions.DiskCacheStrategy.RESULT == options.getDiskCacheStrategy()) {
                requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA);
            }
        }
        List<Transformation> list = new ArrayList<Transformation>();
        if (options.needImageRadius()) {
            list.add(new RoundedCorners(options.getImageRadius()));
        }
        if (options.isCircle()) {
            list.add(new CircleTransformation());
        }
        if (list.size() > 0) {
            Transformation[] transformations = list.toArray(new Transformation[list.size()]);
            requestOptions.transform(transformations);
        }
        RequestBuilder builder = getRequestBuilder(options);
        builder.apply(requestOptions).into((ImageView) options.getViewContainer());
    }

    public void cleanMemory(Context context) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            GlideApp.get(context).clearMemory();
        }
    }

    public void clearView(View view) {
        GlideApp.with(view).clear(view);
    }

    public void pause(Context context) {
        GlideApp.with(context).pauseRequests();
    }

    public void resume(Context context) {
        GlideApp.with(context).resumeRequests();
    }

    private RequestManager getRequestManager(View view) {
        return GlideApp.with(view);
    }

    private ImageLoaderOptions getDefaultOptions(@NonNull Object object, @NonNull View container) {
        return new ImageLoaderOptions.Builder(object, container).build();
    }

    private RequestBuilder getRequestBuilder(ImageLoaderOptions options) {
        RequestBuilder builder = null;
        if (options.isAsGif()) {
            builder = getRequestManager(options.getViewContainer()).asGif();
        } else {
            builder = getRequestManager(options.getViewContainer()).asBitmap();
        }
        builder.load(options.getSource());
        return builder;
    }

    public void loadImgIntoLayout(Context context, String imgUrl, View view) {
        GlideApp.with(context)
                .asBitmap()
                .load(imgUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Drawable drawable = new BitmapDrawable(resource);
                        view.setBackground(drawable);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    public void loadImgIntoLayout(Context context, int imgResId, View view) {
        GlideApp.with(context)
                .asBitmap()
                .load(imgResId)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Drawable drawable = new BitmapDrawable(resource);
                        view.setBackground(drawable);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }
}
