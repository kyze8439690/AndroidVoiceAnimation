package com.yugy.github.voiceanimation;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by yugy on 2014/3/24.
 */
public class ScreenUtils {

    public static int dp2px(Context context, int dp){
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return px;
    }

}
