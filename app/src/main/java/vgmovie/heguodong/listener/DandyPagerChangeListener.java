package vgmovie.heguodong.listener;

import android.support.v4.view.ViewPager;

import vgmovie.heguodong.customview.CustomTitleTabBar;

/**
 * Created by heGuoDong_start  on 2016/6/26 02:03.
 *
 *作用：这是对主页滑动进行的监听类，根据监听的结果，实现滑动页面
 *
 */
public class DandyPagerChangeListener extends BaseOnPageChangeListener {


    /**
     * 构造器
     */
    public DandyPagerChangeListener(ViewPager pager, CustomTitleTabBar tabBar) {
        super(pager, tabBar);
    }

    /**
     * 未进入下一个
     */
    @Override
    public void moveNextFalse() {
        titleTabBar.scrollBar(beginPosition, endPosition, 100);

    }

    /**
     * 滑动中的
     */
    @Override
    public void moveing() {
        titleTabBar.scrollBar(beginPosition, endPosition, 0);

    }

    /**
     * 滑到下一个了
     */
    @Override
    public void moveNextTrue() {
        focusedFragment(currentFragmentIndex,lastFragmentIndex);
        titleTabBar.scrollBar(beginPosition, endPosition, 200);
    }

    public void focusedFragment(int selectPosition,int lastPosition){};
}
