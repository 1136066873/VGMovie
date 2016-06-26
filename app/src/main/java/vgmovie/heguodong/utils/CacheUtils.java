package vgmovie.heguodong.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by heGuoDong_start  on 2016/6/25 00:04.
 *
 *作用：缓存的工具类
 *
 */
public class CacheUtils  {

    /**
     *把第一次联网下载下来的 jsonString 缓存一下//一开始想的不对，不能缓存，不然得到的图片老是一样的
     */
    public static void putString(Context context, String url, String jsonString) {
        SharedPreferences heguodong = context.getSharedPreferences("heguodong", Context.MODE_PRIVATE);
        heguodong.edit().putString(url,jsonString).commit();
    }

    /**
     *得到上一次联网下载下来的 jsonString ，就不再联网再去解析。//一开始想的不对，不能缓存，不然得到的老是一样的
     */
    public static String getString(Context context, String key) {
        SharedPreferences heguodong = context.getSharedPreferences("heguodong", Context.MODE_PRIVATE);
        return heguodong.getString(key,"");//第二个是返回的数据
    }


}
