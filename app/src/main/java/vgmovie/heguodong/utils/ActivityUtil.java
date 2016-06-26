package vgmovie.heguodong.utils;

/**
 * Created by heGuoDong_start  on 2016/6/25 20:46.
 *
 * 作用：用来放置刚刚 打开的Activity 。
 */


import android.app.Activity;
import android.content.Context;

import java.util.Stack;

/**
 * Activity管理工具类 每次启动新的Activity，都会将此Activity压入到Activity Stack，当用户执行返回操作时，
 * 移除Activity Stack 最顶上的 Activity
 */
public class ActivityUtil {

    /**
     * 私有Util工具类构造函数
     * 生成私有的activityStack实例
     */
    private Stack<Activity> activityStack;

    private ActivityUtil() {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
    }

    /**
     * 单一实例
     */
    private static ActivityUtil activityUtil;

    public static ActivityUtil getInstance() {
        if (activityUtil == null) {
            synchronized (ActivityUtil.class) {
                if (activityUtil == null) {
                    activityUtil = new ActivityUtil();
                }
            }
        }
        return activityUtil;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity getCurrentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）????????????
     */
    public void finishCurrentActivity() {
        finishThisActivity(getCurrentActivity());
    }

    /**
     * 結束Activity???????????
     */
    public void finishThisActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishThisActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                Activity activity = activityStack.get(i);
                activityStack.remove(activity);
                activity.finish();
            }
        }
        activityStack.clear();
        activityStack = null;
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            System.exit(0);
        } catch (Exception e) {
        }
    }
}