package vgmovie.heguodong.updateview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import vgmovie.heguodong.utils.PhoneUtils;

/**
 * Created by heGuoDong_start  on 2016/6/26 02:41.
 *
 * 作用：当下拉刷新的时候，用他来实现盛放小猫头像的那一块布局。
 */
public class HeadViewWeight extends LinearLayout  {

    //=========================
    private RefreshView refreshView;
    private static final float defaultSize = 30f;
    private static final float defaultMargin = 10f;
    //=========================
    public HeadViewWeight(Context context) {
        this(context, null, 0);
    }

    public HeadViewWeight(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeadViewWeight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOrientation(LinearLayout.VERTICAL);
        this.setGravity(Gravity.CENTER);
        addCatRefreshView();

    }

    /**
     * 把小猫头像刷新的东西加进来
     */
    private void addCatRefreshView() {
        refreshView = new RefreshView(getContext());
        int size = PhoneUtils.dp2px(getContext(), defaultSize);
        LayoutParams params = new LayoutParams(size, size);
        int margin = PhoneUtils.dp2px(getContext(), defaultMargin);
        params.bottomMargin = margin;
        params.topMargin = margin;
        refreshView.setLayoutParams(params);
        this.addView(refreshView);


    }

    public RefreshView getRefreshView() {
        return refreshView;
    }




}
