package com.sitechdev.vehicle.pad.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;

public class ReflectImageView {
    private static boolean isGetHeigth = true;
    private static int width;
    private static int height;

    /**
     * 获得带倒影的图片方法
     */
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = -60;
        width = bitmap.getWidth();
        height = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, 0,
                width, height, matrix, false);


        Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
                (height + height / 2), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();

        deafalutPaint.setAlpha(100);
        //canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, deafalutPaint);

        Paint mPaint = new Paint();            //阴影的效果可以自己根据需要设定
        LinearGradient shader = new LinearGradient(0, 0, 0, 250, 0x70ffffff, 0x00ffffff, Shader.TileMode.MIRROR);

        //LinearGradient shader = new LinearGradient(0, 0, 0, height, 0x00ffffff, 0x70ffffff, Shader.TileMode.CLAMP);
        mPaint.setShader(shader);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        canvas.drawRect(0, height - 30, width, bitmapWithReflection.getHeight(), mPaint);

        //canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
        // + reflectionGap, mPaint);

//        Paint paint = new Paint();
//        LinearGradient shader = new LinearGradient(0, 0, 0,
//                200, 0x70ffffff,
//                0x00ffffff, Shader.TileMode.MIRROR);
////        LinearGradient shader = new LinearGradient(0, 0, 0, height, 0x00ffffff,
////                0x70ffffff, Shader.TileMode.MIRROR);
//        paint.setShader(shader);
//        // Set the Transfer mode to be porter duff and destination in
//        //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
//        // Draw a rectangle using the paint with our linear gradient
//        canvas.drawRect(0, height-30, width, bitmapWithReflection.getHeight()
//                , paint);

        return bitmapWithReflection;
    }
}
