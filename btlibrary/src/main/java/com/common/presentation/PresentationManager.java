//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.presentation;

import android.annotation.SuppressLint;
import android.app.Presentation;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager.InvalidDisplayException;


public class PresentationManager {
    private static final String TAG = "PresentationManager";
    private static PresentationManager mPresentationManager;
    private Context mContext;
    private Display[] mDisplay;
    private Presentation mPresentation = null;

    public PresentationManager() {
    }

    public static PresentationManager getInstanse(Context var0) {
        if (mPresentationManager == null) {
            mPresentationManager = new PresentationManager();
            mPresentationManager.mContext = var0;
            mPresentationManager.init();
        }

        return mPresentationManager;
    }

    @SuppressLint("WrongConstant")
    private void init() {
        this.mDisplay = ((DisplayManager)this.mContext.getSystemService("display")).getDisplays();
        StringBuilder var1 = new StringBuilder();
        var1.append("get ");
        var1.append(this.mDisplay.length);
        var1.append(" displays!");
        Log.i("PresentationManager", var1.toString());
    }

    public Display getDisplay(int var1) {
        return var1 < this.mDisplay.length ? this.mDisplay[var1] : null;
    }

    public void updatePresentation(int var1, Presentation var2) {
        if (var1 < this.mDisplay.length) {
            Display var3 = this.mDisplay[var1];
            if (this.mPresentation != null && var2.getDisplay() != var3) {
                Log.w("PresentationManager", "Dismissing presentation because the current route no longer has a presentation display.");
            }

            if (this.mPresentation == null && this.mDisplay != null) {
                StringBuilder var5 = new StringBuilder();
                var5.append("Showing presentation on display: ");
                var5.append(this.mDisplay);
                Log.w("PresentationManager", var5.toString());

                try {
                    this.mPresentation.getWindow().getAttributes().type = 2003;
                    this.mPresentation.show();
                    return;
                } catch (InvalidDisplayException var4) {
                    Log.w("PresentationManager", "Couldn't show presentation!  Display was removed in the meantime.", var4);
                    this.mPresentation = null;
                }
            }
        }

    }
}
