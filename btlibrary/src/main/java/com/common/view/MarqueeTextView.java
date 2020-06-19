//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.view;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.TextView;

public class MarqueeTextView extends TextView {
    public MarqueeTextView(Context var1) {
        super(var1);
    }

    public MarqueeTextView(Context var1, AttributeSet var2) {
        super(var1, var2);
        this.setEllipsize(TruncateAt.MARQUEE);
        this.setSingleLine(true);
        this.setMarqueeRepeatLimit(-1);
    }

    public MarqueeTextView(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
    }

    public boolean isFocused() {
        return true;
    }
}
