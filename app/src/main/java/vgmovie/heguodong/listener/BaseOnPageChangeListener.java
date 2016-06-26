package vgmovie.heguodong.listener;

import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import vgmovie.heguodong.customview.CustomTitleTabBar;

/**
 * Created by heGuoDong_start  on 2016/6/26 02:04.
 */
public abstract class BaseOnPageChangeListener implements ViewPager.OnPageChangeListener {

    public ViewPager pager;
    public CustomTitleTabBar titleTabBar;
    public ImageView mBar;
    public float itemWidth;
    public boolean isDragging;
    public float endPosition;
    public float beginPosition;
    public int lastFragmentIndex;
    public int currentFragmentIndex;
    private float lastOffset = 0;
    private int lastValue = 0;
    private boolean right = false;
    private boolean left = false;


    /**
     * 构造器
     */
    public BaseOnPageChangeListener(ViewPager pager,CustomTitleTabBar tabBar) {
        this.pager = pager;
        this.titleTabBar = tabBar;
        this.mBar = tabBar.getTabBar();
        this.itemWidth = titleTabBar.getItemWidth();
    }

    /**
     * 当页面滑动的时候回调
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(isDragging){
            if(lastValue > positionOffsetPixels){//向右滑动
                if(right){//上次滑动方向是否是右
                    endPosition = beginPosition - itemWidth * (Math.abs(lastOffset - (1- positionOffset)));
                }else if(left){
                    endPosition = beginPosition - itemWidth * (Math.abs(positionOffset - lastOffset));
                }
                right = true;
                left = false;
            }else if(lastValue < positionOffsetPixels){//向左滑动
                if(left){//上次滑动方向是否是左
                    endPosition = beginPosition + itemWidth * (Math.abs(lastOffset - positionOffset));
                }else if(right){
                    endPosition = beginPosition + itemWidth * (Math.abs(1 - lastOffset - positionOffset));
                }
                right = false;
                left = true;
            }else{//两边
                endPosition = beginPosition;
                right = left = false;
            }
            moveing();
        }

        if(right){
            lastOffset = 1 -positionOffset;
        }
        if(left){
            lastOffset = positionOffset;
        }
        lastValue = positionOffsetPixels;
        beginPosition = endPosition;


    }

    /**
     * 当页面被选中的时候回调
     */
    @Override
    public void onPageSelected(int position) {
        lastFragmentIndex = currentFragmentIndex;
        currentFragmentIndex = position;
        beginPosition = endPosition;
        endPosition = titleTabBar.getSelectedTitleView(position).getX();
        moveNextTrue();

    }

    /**
     * 当页面滑动状态改变的时候回调
     */
    @Override
    public void onPageScrollStateChanged(int state) {

        if (state == ViewPager.SCROLL_STATE_DRAGGING){
            isDragging = true;
        }else if(state == ViewPager.SCROLL_STATE_SETTLING){
            isDragging = false;
            beginPosition = endPosition;
            if(pager.getCurrentItem() == currentFragmentIndex){
                endPosition = titleTabBar.getSelectedTitleView(currentFragmentIndex).getX();
                moveNextFalse();
            }
        }else if(state == ViewPager.SCROLL_STATE_IDLE){
            isDragging = false;
            right = left = false;
            lastOffset = 0;
        }

    }


    public abstract void moveNextFalse();//未进入下一个页面
    public abstract void moveing();//滑动中
    public abstract void moveNextTrue();//进入下一个页面


}
