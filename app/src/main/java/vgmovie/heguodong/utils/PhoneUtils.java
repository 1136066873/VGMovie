package vgmovie.heguodong.utils;

import android.content.Context;

/**
 * Created by heGuoDong_start  on 2016/6/25 22:27.
 *
 * 作用：
 */
public class PhoneUtils {
    public static int dp2px(Context context,float dpValue){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale +0.5f);
    }


}
