////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)
////
//
//package com.common.view;
//
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.os.Build;
//import android.util.AttributeSet;
//import android.widget.ImageView;
//
//import androidx.annotation.RequiresApi;
//
//import com.common.util.ResourceUtil;
//import com.sitech.testmo.R;
//
//public class KeyButtonView extends ImageView {
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public KeyButtonView(Context var1, AttributeSet var2) {
//        this(var1, var2, 0);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public KeyButtonView(Context var1, AttributeSet var2, int var3) {
//        super(var1, var2,var3);
//        var3 = R.drawable.ic_launcher;//ResourceUtil.getDrawableId(var1, "button_common_keyview");
//        Drawable var4 = null;
//        if (var3 != 0) {
//            var4 = var1.getDrawable(var3);
//        }
//
//        if (var4 != null) {
//            this.setBackground(var4);
//        } else {
////            this.setBackground(new KeyButtonRipple(var1, this));
//        }
//    }
//}
