package com.sitechdev.net;

import android.util.Log;

import com.lzy.okgo.model.Progress;
import com.lzy.okserver.download.DownloadListener;

public abstract class SimpleDownloadListener extends DownloadListener {

    private static final String TAG = SimpleDownloadListener.class.getSimpleName();

    public SimpleDownloadListener(Object tag) {
        super(tag);
    }

    @Override
    public void onStart(Progress progress) {

    }

    @Override
    public void onProgress(Progress progress) {
    }

    @Override
    public void onError(Progress progress) {
        Log.e(TAG, "download error" + progress);
    }

    @Override
    public void onRemove(Progress progress) {
    }
}
