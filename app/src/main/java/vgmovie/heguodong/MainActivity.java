package vgmovie.heguodong;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;

import vgmovie.heguodong.base.BaseFragmentActivity;
import vgmovie.heguodong.customview.CustomTabItemView;
import vgmovie.heguodong.fragmentsinmaimactivity.CinemaFragment;
import vgmovie.heguodong.fragmentsinmaimactivity.DiscoverFragment;
import vgmovie.heguodong.fragmentsinmaimactivity.MineFragment;
import vgmovie.heguodong.fragmentsinmaimactivity.MovieFragment;

public class MainActivity extends BaseFragmentActivity   implements CustomTabItemView.TabClickListner {

    //===================================
    /***顶部Tab栏目*/
    private CustomTabItemView movie, cinema, community, mine, lastSelectedTab;
    /**整体界面包含电影。影院。发现。我的4个切换界面*/
    private MovieFragment movieFragment;
    private CinemaFragment cinemaFragment;
    private DiscoverFragment communityFragment;
    private MineFragment mineFragment;

    private FragmentManager fragmentManager;//得到Fragment 的管理器
    private CustomTabItemView.TabClickListner mClickListner;


    //===================================

    /**
     * 加载布局
     */
    @Override
    public void setContentView() {
        setTopBarColor();
        //一句话消除Activity 之间的转场动画
        overridePendingTransition(Animation.INFINITE, Animation.INFINITE);

        //整体的app分为2部分--除去底部的Tab栏目是一部分，剩余的是一部分（FrameLayout填充即可）
        setContentView(R.layout.activity_main);

        //获取fragment管理器
        fragmentManager = getSupportFragmentManager();

    }

    /**
     * 初始化视图显示
     */
    @Override
    public void initView() {
        //底部Tab栏视图获取
        Log.e("TAG","能不能找到 --------------------------------" + ((findViewById(R.id.tabLayout)) == null));
        View tabLayout = findViewById(R.id.tabLayout);

        Log.e("TAG","tabLayout --------------------------------" + (tabLayout == null));
        /**
         * 因为底部Tag栏目是include进来的
         * 需要在tabLayout布局里面进行获取Tab栏目对应的按钮选项
         */
        movie = (CustomTabItemView) tabLayout.findViewById(R.id.movie);
        cinema = (CustomTabItemView) tabLayout.findViewById(R.id.cinema);
        community = (CustomTabItemView) tabLayout.findViewById(R.id.community);
        mine = (CustomTabItemView) tabLayout.findViewById(R.id.mine);
        //初始化电影Tab处于选中状态
        movie.setTabSelected(true);
        lastSelectedTab = movie;
        //默认显示电影fragment界面
        setTabSelection(0);

    }

    /**
     *根据传入的index参数来设置选中的tab页
     */
    @SuppressLint("CommitTransaction")
    private void setTabSelection(int index){
        //开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideAllFragment(transaction);
        switch (index) {
            case 0:
                if(movieFragment == null){
                    movieFragment = new MovieFragment();
                    transaction.add(R.id.content,movieFragment);
                }else{
                    transaction.show(movieFragment);
                }
                break;
            case 1:
                if(cinemaFragment == null){
                    cinemaFragment = new CinemaFragment();
                    transaction.add(R.id.content,cinemaFragment);
                }else{
                    transaction.show(cinemaFragment);
                }
                break;
            case 2:
                if(communityFragment == null){
                    communityFragment = new DiscoverFragment();
                    transaction.add(R.id.content,communityFragment);
                }else{
                    transaction.show(communityFragment);
                }
                break;
            case 3:
                if(mineFragment == null){
                    mineFragment = new MineFragment();
                    transaction.add(R.id.content,mineFragment);
                }else{
                    transaction.show(mineFragment);
                }
                break;
        }
        transaction.commit();
    }


    /**
     *隐藏掉所有的fragment，防止多个fragment显示
     */
    private void hideAllFragment(FragmentTransaction transaction){
        if(movieFragment != null){
            transaction.hide(movieFragment);
        }
        if(cinemaFragment != null){
            transaction.hide(cinemaFragment);
        }
        if(communityFragment != null){
            transaction.hide(communityFragment);
        }
        if(mineFragment != null){
            transaction.hide(mineFragment);
        }
    }

    /**
     * 初始化数据，在这里不用立刻初始化
     */
    @Override
    public void initData() {
        // TODO Auto-generated method stub


    }



    @Override
    public void setOnClickListener() {
        movie.setTabClickListener(this);
        cinema.setTabClickListener(this);
        community.setTabClickListener(this);
        mine.setTabClickListener(this);

    }




    /**
     * 设置顶部栏的颜色--之着色模式
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setTopBarColor() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//必须在调用父类的onCreate 方法之前设置没标题，不然会报错。
        Window window = getWindow();
        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(Color.RED);
        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 预留出系统 View 的空间.
            ViewCompat.setFitsSystemWindows(mChildView, true);
        }
    }

    @Override
    public void onTabClick(View view) {
        switch (view.getId()) {
            case R.id.movie://index = 0;
                judgeTabSame(movie);
                setTabSelection(0);
                break;
            case R.id.cinema://index = 1;
                judgeTabSame(cinema);
                setTabSelection(1);
                break;
            case R.id.community://index = 2;
                judgeTabSame(community);
                setTabSelection(2);
                break;
            case R.id.mine://index = 3;
                judgeTabSame(mine);
                setTabSelection(3);
                break;
        }

    }


    /**
     *判断是否是同一个tab标签,并设置状态
     */
    private void judgeTabSame(CustomTabItemView clickTab){
        if(lastSelectedTab != clickTab){
            lastSelectedTab.setTabSelected(false);
            lastSelectedTab = clickTab;
            lastSelectedTab.setTabSelected(true);
        }
    }





















}
