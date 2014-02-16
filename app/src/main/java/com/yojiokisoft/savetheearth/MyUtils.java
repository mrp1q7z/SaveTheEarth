package com.yojiokisoft.savetheearth;

/**
 * Created by taoka on 14/02/08.
 */
public class MyUtils {
    /**
     * dpiをpxに変換
     *
     * @param dpi
     * @return px
     */
    public static int dpi2Px(int dpi) {
        float density = getDensity();
        int px = (int) (dpi * density + 0.5f);
        return px;
    }

    /**
     * @return ピクセル密度
     */
    public static float getDensity() {
        return App.getInstance().getResources().getDisplayMetrics().density;
    }
}
