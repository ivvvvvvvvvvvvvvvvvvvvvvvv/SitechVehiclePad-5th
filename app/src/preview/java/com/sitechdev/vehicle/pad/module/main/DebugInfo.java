package com.sitechdev.vehicle.pad.module.main;

import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseActivity;

/**
 * <pre>
 *      author : zyf
 *      time   : 2020/6/9
 * </pre>
 */
public class DebugInfo extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_debuginfo;
    }

    @Override
    protected void initData() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        String density = "屏幕密度： " + displayMetrics.density;

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        String fenbianlv = "\n分辨率： " + width+"*"+height;

        ((TextView)findViewById(R.id.text)).setText(density+fenbianlv);
        ((TextView)findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
