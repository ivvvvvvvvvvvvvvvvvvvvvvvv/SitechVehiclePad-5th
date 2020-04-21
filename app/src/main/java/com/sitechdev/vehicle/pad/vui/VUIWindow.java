package com.sitechdev.vehicle.pad.vui;

/**
 * @author zhubaoqiang
 * @date 2019/8/15
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppApplication;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 语音交互窗口
 */
public class VUIWindow implements View.OnClickListener {

    private Context context;

    private WindowManager windowManager;

    private View rootView;

    private WindowManager.LayoutParams params;

    private ChatHolder chatHolder;

    private VUIHolder currentHolder;

    private FrameLayout mHolderContainer;

    private OnWindowHideListener onWindowHideListener;

    private AnimationDrawable teddy;

    private VUIWindow(){
        context = AppApplication.getContext();
        if (null != context){
            windowManager = (WindowManager) context.getSystemService(
                    Context.WINDOW_SERVICE);
            if (null != windowManager){
                params = createParams();
                rootView = createView();
            }
        }
    }

    private View createView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        rootView = inflater.inflate(R.layout.fragment_diaog_vui,
                null, false);
        rootView.findViewById(R.id.root_view_vui).setOnClickListener(this);
        mHolderContainer = rootView.findViewById(R.id.vui_dialog_holder_container);
        ImageView teddyView = rootView.findViewById(R.id.vui_dialog_teddy);
        teddy = (AnimationDrawable) teddyView.getDrawable();
        return rootView;
    }

    private WindowManager.LayoutParams createParams() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        int screenWidth = point.x;
        int screenHeight = point.y;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else {
            params.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
//        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        params.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        params.format = PixelFormat.TRANSPARENT;
        params.x = screenWidth;
        params.y = screenHeight;
        return params;
    }

    public void show(){
        if (null != windowManager && null != rootView && !rootView.isShown()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                if (!Settings.canDrawOverlays(context)){
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + context.getPackageName()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    return;
                }
            }
            windowManager.addView(rootView, params);
        }
        if (null == chatHolder){
            chatHolder = new ChatHolder(context);
        }
        if (null != mHolderContainer && currentHolder != chatHolder){
            currentHolder = chatHolder.added(mHolderContainer, currentHolder);
        }
        if (null != teddy && !teddy.isRunning()){
            teddy.start();
        }
    }

    public void hide(){
        if (null != windowManager && null != rootView && rootView.isShown()){
            if (null != teddy && teddy.isRunning()){
                teddy.stop();
            }
            windowManager.removeViewImmediate(rootView);
            showText(context.getResources().getString(R.string.vui_welcome_text));
            if (null != onWindowHideListener){
                onWindowHideListener.onWindowHide();
            }
        }
    }

    public void showWeather(JSONObject weather){
        WeatherHolder holder = new WeatherHolder(context);
        currentHolder = holder.added(mHolderContainer, currentHolder);
        holder.adapter(weather);
    }

    public void showStock(JSONObject stock){
        StockHolder holder = new StockHolder(context);
        currentHolder = holder.added(mHolderContainer, currentHolder);
        holder.adapter(stock);
    }

    public void showText(String text){
        if (null != chatHolder){
            if (chatHolder.isadded()){
                chatHolder.getTextView().setText(text);
            }
        }
    }

    public void appendText(int percent, String text){
        if (null != chatHolder){
            if (chatHolder.isadded()){
                TextView textView = chatHolder.getTextView();
                int iTag = -1;
                Object tag = textView.getTag();
                if (null != tag){
                    iTag = (int) tag;
                }
                if (percent > iTag){
                    textView.setText(text);
                    textView.setTag(percent);
                }
            }
        }
    }

    public void clearText(){
        if (null != chatHolder){
            if (chatHolder.isadded()){
                TextView textView = chatHolder.getTextView();
                textView.setText("");
                textView.setTag(null);
            }
        }
    }

    public void showContacts(JSONArray array){
        ContactsHolder holder = new ContactsHolder(context);
        currentHolder = holder.added(mHolderContainer, currentHolder);
        holder.adapter(array);
    }

    public <T extends VUIHolder> T getCurrentHolder() {
        return (T) currentHolder;
    }

    public void setOnWindowHideListener(OnWindowHideListener onWindowHideListener) {
        this.onWindowHideListener = onWindowHideListener;
    }

    public boolean isShowing(){
        return null != windowManager && null != rootView && rootView.isShown();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.root_view_vui:
                VUI.log("click root_view_vui");
                hide();
                break;
        }
    }

    private static class VUIWindowHolder{
        private static final VUIWindow INSTANCE = new VUIWindow();
    }

    public interface OnWindowHideListener{
        void onWindowHide();
    }

    public static VUIWindow getInstance() {
        return VUIWindowHolder.INSTANCE;
    }
}
