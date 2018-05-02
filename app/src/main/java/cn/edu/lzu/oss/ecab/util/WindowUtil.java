package cn.edu.lzu.oss.ecab.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class WindowUtil {
    private static int HEIGHT = 0;
    private static int WIDTH = 0;
    private static float DENSITY = 0.0f;

    public static void initWindows(Context context){
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        DENSITY = dm.density;
        WIDTH = dm.widthPixels;
        HEIGHT = dm.heightPixels;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static float getDENSITY() {
        return DENSITY;
    }
}
