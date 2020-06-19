//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

import com.common.util.ResourceUtil;

public class KeyButton extends Button {
    public KeyButton(Context var1, AttributeSet var2) {
        this(var1, var2, 0);
    }

    public KeyButton(Context var1, AttributeSet var2, int var3) {
        super(var1, var2);
        Drawable var4 = var1.getDrawable(ResourceUtil.getDrawableId(var1, "button_common_keyview"));
        if (var4 != null) {
            this.setBackground(var4);
        } else {
//            this.setBackground(new KeyButtonRipple(var1, this));
        }
    }
}
