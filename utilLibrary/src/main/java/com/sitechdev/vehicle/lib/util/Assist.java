package com.sitechdev.vehicle.lib.util;

import android.os.Handler;
import android.view.View;

/**
 * Created by lishaozong on 2018/9/21.
 */

public class Assist {
    private final String TAG = Assist.class.getSimpleName();
    private static Assist assist;
    private static final long TIME1 = 500;

    public static Assist with() {
        if (assist == null) {
            synchronized (Assist.class) {
                if (assist == null) {
                    assist = new Assist();
                }
            }
        }
        return assist;
    }

    public void limits(Handler handler, View...views){
        limits(handler,TIME1,views);
    }

    /**
     * 限制时间内不可点击
     * @param views 需要限制的view
     */
    public void limits(Handler handler,long delayed, View...views){
        if (views != null && handler != null){
            for (int i = 0; i < views.length; i++) {
                if (views[i] != null){
                    views[i].setEnabled(false);
                    LimitRun runnable = new LimitRun();
                    runnable.setView(views[i]);
                    handler.postDelayed(runnable,delayed);
                }
            }
        }
    }

    private class LimitRun implements Runnable{
        private View mView;
        public void setView(View view){
            this.mView = view;
        }
        public void run(){
            if (mView != null){
                mView.setEnabled(true);
            }
        }
    }

    public static void destroy(){
        if (assist != null){
            assist = null;
        }
    }

}
