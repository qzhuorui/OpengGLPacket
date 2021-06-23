package com.choryan.opengglpacket.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * @author: ChoRyan Quan
 * @date: 2021/6/23
 */
public class BitmapUtils {

    public static Bitmap adjustBitmapRotation(Bitmap bm, int orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        try {
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            return bm1;
        } catch (OutOfMemoryError ex) {
        }
        return null;
    }
}
