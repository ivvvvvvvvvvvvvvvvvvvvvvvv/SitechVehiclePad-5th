package com.sitechdev.vehicle.lib.imageloader;

import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * @author liuhe
 * @date 2017/3/20 0020
 */
public class ImageLoaderOptions {

    /**
     * 图片容器
     */
    private View viewContainer;
    /**
     * 图片地址
     */
    private Object source;

    /**
     * 直接加载bitmap
     */
    private Bitmap bitmap;
    /**
     * 设置展位图
     */
    private int holderDrawable;
    /**
     * 设置图片的大小
     */
    private ImageSize imageSize;
    /**
     * 加载错误的图片
     */
    private int errorDrawable;
    /**
     * 是否跳过内存缓存
     */
    private boolean skipMemoryCache = false;
    /**
     * 磁盘缓存策略
     */
    private DiskCacheStrategy mDiskCacheStrategy = DiskCacheStrategy.DEFAULT;
    /**
     * 圆角
     */
    private int imageRadius = 0;
    /**
     * 是否是圆形图片
     */
    private boolean isCircle = false;
    /**
     * 是否作为gif展示
     */
    private boolean asGif = false;

    private ImageLoaderOptions(Builder builder) {
        this.asGif = builder.asGif;
        this.errorDrawable = builder.errorDrawable;
        this.holderDrawable = builder.holderDrawable;
        this.imageSize = builder.mImageSize;
        this.skipMemoryCache = builder.isSkipMemoryCache;
        this.mDiskCacheStrategy = builder.mDiskCacheStrategy;
        this.source = builder.source;
        this.viewContainer = builder.mViewContainer;
        this.isCircle = builder.isCircle;
        this.imageRadius = builder.imageRadius;
    }

    public boolean needImageRadius() {
        return imageRadius > 0;
    }

    public int getImageRadius() {
        return imageRadius;
    }

    public Object getSource() {
        return source;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public View getViewContainer() {
        return viewContainer;
    }

    public boolean isCircle() {
        return isCircle;
    }

    public int getHolderDrawable() {
        return holderDrawable;
    }

    public ImageSize getImageSize() {
        return imageSize;
    }

    public int getErrorDrawable() {
        return errorDrawable;
    }

    public boolean isAsGif() {
        return asGif;
    }

    public boolean isSkipMemoryCache() {
        return skipMemoryCache;
    }

    public DiskCacheStrategy getDiskCacheStrategy() {
        return mDiskCacheStrategy;
    }

    public void show() {
        GlideUtils.getInstance().loadImage(this);
    }


    public final static class Builder {

        private int holderDrawable = -1;
        private View mViewContainer;
        private Object source;
        private int resource = -1;
        private ImageSize mImageSize;
        private int errorDrawable = -1;
        private boolean asGif = false;
        private boolean isSkipMemoryCache = false;
        private DiskCacheStrategy mDiskCacheStrategy = DiskCacheStrategy.DEFAULT;
        private int imageRadius = 0;
        private boolean isCircle = false;

        public Builder(@NonNull Object source, @NonNull View v) {
            this.source = source;
            this.mViewContainer = v;
        }

        public Builder placeholder(@DrawableRes int holderDrawable) {
            this.holderDrawable = holderDrawable;
            return this;
        }

        public Builder isCircle() {
            this.isCircle = true;
            return this;
        }

        public Builder radius(int radius) {
            this.imageRadius = radius;
            return this;
        }

        public Builder skipMemoryCache(boolean isSkipMemoryCache) {
            this.isSkipMemoryCache = isSkipMemoryCache;
            return this;
        }

        public Builder override(int width, int height) {
            this.mImageSize = new ImageSize(width, height);
            return this;
        }

        public Builder asGif(boolean asGif) {
            this.asGif = asGif;
            return this;
        }

        public Builder error(@DrawableRes int errorDrawable) {
            this.errorDrawable = errorDrawable;
            return this;
        }

        public Builder diskCacheStrategy(DiskCacheStrategy mDiskCacheStrategy) {
            this.mDiskCacheStrategy = mDiskCacheStrategy;
            return this;
        }

        public ImageLoaderOptions build() {
            return new ImageLoaderOptions(this);
        }
    }

    /**
     * 对应重写图片size
     */
    public final static class ImageSize {
        private int width = 0;
        private int height = 0;

        public ImageSize(int width, int heigh) {
            this.width = width;
            this.height = heigh;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }
    }

    /**
     * 磁盘缓存策略
     * DiskCacheStrategy.NONE 什么都不缓存，就像刚讨论的那样
     * DiskCacheStrategy.SOURCE 仅仅只缓存原来的全分辨率的图像。在我们上面的例子中，将会只有一个 1000x1000 像素的图片
     * DiskCacheStrategy.RESULT 仅仅缓存最终的图像，即，降低分辨率后的（或者是转换后的）
     * DiskCacheStrategy.ALL 缓存所有版本的图像（默认行为）
     */
    public enum DiskCacheStrategy {
        All, NONE, SOURCE, RESULT, DEFAULT
    }
}
