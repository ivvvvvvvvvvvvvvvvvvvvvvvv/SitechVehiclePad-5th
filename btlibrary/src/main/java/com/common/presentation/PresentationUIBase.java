//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.presentation;

import android.app.Activity;
import android.app.Presentation;
import android.content.Context;
import android.os.Build;
import android.view.Display;


public class PresentationUIBase extends Presentation {
    public static final int SCREEN0 = 0;
    public static final int SCREEN1 = 1;
    private static final String TAG = "PresentationUIBase";
    protected Context mContext;
    protected int mDisplayIndex;
    public boolean mPause = false;

    public PresentationUIBase(Context var1, Display var2, int var3) {
        super(var1, var2, var3);
        this.mContext = var1;
        if (!(var1 instanceof Activity)) {
            this.getWindow().setType(2003);
        }

    }

    public void onPuase() {
        this.mPause = true;
    }

    public void onResume() {
        this.mPause = false;
    }

    @Override
    public void show() {
        super.show();
        this.mPause = false;
    }
}
