package vgmovie.heguodong.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import vgmovie.heguodong.utils.ActivityUtil;

/**
 * Created by heGuoDong_start  on 2016/6/25 20:42.
 *
 * 作用：这个是ContentFragmentActivity 的基类！！！
 *
 * 借鉴网上的资源，以初始化放在栈中
 *
 */
public abstract class BaseFragmentActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //一旦启动，就添加到Stack中
        ActivityUtil.getInstance().addActivity(this);


        //让子类去加载各自的布局文件
        setContentView();


        //让子类去初始化控件
        initView();

        //让子类去实现自己的初始化数据
        initData();


        //让子类自己去实现自己的点击事件
        setOnClickListener();

    }

    /**
     *设置 ContentView布局
     */
    public abstract void setContentView();

    /**
     *初始化控件
     */
    public abstract void initView();

    /**
     *初始化数据源
     */
    public abstract void initData();


    /**
     *设置监听事件
     */
    public abstract void setOnClickListener();


    /**
     * 捕捉 android 中的后退键事件--这样用以对应栈中的操作
     */
    @Override
    public void onBackPressed() {
        //从Stack中结束本界面
        ActivityUtil.getInstance().finishThisActivity(this);
        super.onBackPressed();
    }


}
