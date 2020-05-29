package com.sitechdev.vehicle.pad.vui;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author zhubaoqiang
 * @date 2019/8/19
 */
public abstract class VUIHolder {

    protected Context context;
    private View root;
    private boolean isadded;

    protected VUIHolder(@NonNull Context context, @LayoutRes int layoutID) {
        this(LayoutInflater.from(context), layoutID);
    }

    protected VUIHolder(LayoutInflater inflater, @LayoutRes int layoutID){
        this.context = inflater.getContext();
        root = inflater.inflate(layoutID, null, false);
        getView();
    }

    protected <T extends View> T findViewById(@IdRes int id){
        return root.findViewById(id);
    }

    public VUIHolder added(@NonNull ViewGroup container, VUIHolder currentHolder){
        if (null != currentHolder){
            currentHolder.removed();
        }
        if (container.getChildCount() > 0) {
            container.removeAllViews();
        }
        container.addView(root);
        isadded = true;
        return this;
    }

    protected void removed(){
        isadded = false;
    }

    public boolean isadded() {
        return isadded;
    }

    protected abstract void getView();
}
