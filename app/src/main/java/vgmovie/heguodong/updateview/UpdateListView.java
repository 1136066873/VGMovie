package vgmovie.heguodong.updateview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by heGuoDong_start  on 2016/6/26 02:33.
 *
 * 作用：用于下拉刷新，底部更多
 */
public class UpdateListView extends ListView implements AbsListView.OnScrollListener {

    private float mDownY;
    private float mMoveY;

    private int mCurrentScrollState;

    // 头部显示高度
    private static enum HeadDisPlayHeight {HIDE_DISPLAY,SOME_DISPLAY,TOTAL_DISPLAY};



    private static enum RefreshState {
        HIDE_PULL_NOREFRESH,// 未刷新，隐藏全部头部
        PART_PULL_NOREFRESH,// 未刷新，隐藏部分头部
        SHOW_PULL_NOREFRESH,// 未刷新，滑开头部(显示)
        HIDE_PULL_REFRESHING,// 刷新状态中，下拉隐藏全部头部
        PART_PULL_REFRESHING,// 刷新状态中，下拉隐藏部分头部
        SHOW_PULL_REFRESHING,// 刷新状态中，下拉滑开头部(显示)
        HIDE_PULL_REFRESHED,// 刷新完成，隐藏全部头部
        PART_PULL_REFRESHED,// 刷新完成，隐藏部分头部
        SHOW_PULL_REFRESHED// 刷新完成，滑开头部(显示)
    };


    private RefreshState currentRefreshState = RefreshState.HIDE_PULL_NOREFRESH;



    private static enum LoadState{
        HIDE_DRAG_NOLOAD,// 未加载，隐藏全部头部
        PART_DRAG_NOLOAD,// 未加载，隐藏部分头部
        SHOW_DRAG_NOLOAD,// 未加载，滑开头部(显示)
        HIDE_DRAG_LOADING,// 加载状态中，下拉隐藏全部头部
        PART_DRAG_LOADING,// 加载状态中，下拉隐藏部分头部
        SHOW_DRAG_LOADING,// 加载状态中，下拉滑开头部(显示)
        HIDE_DRAG_LOADED,// 加载完成，隐藏全部头部
        PART_DRAG_LOADED,// 加载完成，隐藏部分头部
        SHOW_DRAG_LOADED// 加载完成，滑开头部(显示)
    }


    private LoadState currentLoadState = LoadState.HIDE_DRAG_NOLOAD;

    // 界面加载刷新状态
    public static enum UpdateViewState{
        REFRESH_ORIGINAL,// 未刷新状态
        REFRESH_RETURNING,// 界面反弹中
        REFRESH_REFRESHING,// 刷新数据中
        REFRESH_DONE,// 刷新数据完成
        LOAD_ORIGINAL,// 未加载状态
        LOAD_RETURNING,// 界面反弹中
        LOAD_LOADING,// 加载数据中
        LOAD_DONE// 加载数据完成
    };


    private UpdateViewState currentUpdateState = UpdateViewState.REFRESH_ORIGINAL; // 记录当前数据加载状态

    private boolean headVisible = false;
    private boolean screenTouched = false;
    private boolean headBegin = false;

    private HeadViewWeight mHeadViewWeight = null;
    private RefreshView mHeadRefreshView = null;
    private int mHeaderHeight;
    private int realHeaderPadding;
    private UpdateDataListener mUpdateListener = null;

    private FootViewWeight mFootViewWeight = null;
    private int mFooterHeight;
    private int realFooterPadding;

    private Handler handler = new Handler();

    private float lastDelatY = 0;
    private boolean smoothUp = false;//手势滑动方向

    public void setOnUpdateListener(UpdateDataListener refreshListener) {
        this.mUpdateListener = refreshListener;
    }

    public UpdateListView(Context context) {
        this(context, null);
    }

    public UpdateListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(final Context context) {
        setOverScrollMode(OVER_SCROLL_NEVER);
        mHeadViewWeight = new HeadViewWeight(getContext());
        mHeadViewWeight.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,AbsListView.LayoutParams.WRAP_CONTENT));
        addHeaderView(mHeadViewWeight);
        mHeadRefreshView = mHeadViewWeight.getRefreshView();
        setSelection(1);
        setOnScrollListener(this);
        measureView(mHeadViewWeight);
        mHeaderHeight = mHeadViewWeight.getMeasuredHeight();
//		measureView(mHeadRefreshView);
        mHeadViewWeight.setPadding(mHeadViewWeight.getPaddingLeft(), -mHeaderHeight, mHeadViewWeight.getPaddingRight(),
                mHeadViewWeight.getPaddingBottom());
        realHeaderPadding = -mHeaderHeight;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL) {
            switch (currentUpdateState) {
                case REFRESH_ORIGINAL:
                    if (firstVisibleItem == 0 && getCurrentHeaderDisplayState() == HeadDisPlayHeight.SOME_DISPLAY) {
                        switch (currentRefreshState) {
                            case HIDE_PULL_NOREFRESH:
                                currentRefreshState = RefreshState.PART_PULL_NOREFRESH;
                                headBegin = true;
                                mDownY = mMoveY;
                                break;
                            case SHOW_PULL_NOREFRESH:
                                currentRefreshState = RefreshState.PART_PULL_NOREFRESH;
                                headBegin = true;
                                break;
                            default:
                                break;
                        }
                    } else if (firstVisibleItem == 0 && getCurrentHeaderDisplayState() == HeadDisPlayHeight.TOTAL_DISPLAY) {
                        if (currentRefreshState == RefreshState.PART_PULL_NOREFRESH || currentRefreshState == RefreshState.HIDE_PULL_NOREFRESH) {
                            currentRefreshState = RefreshState.SHOW_PULL_NOREFRESH;
                        }
                    } else if (firstVisibleItem != 0) {
                        resetHeaderPadding();
                        currentRefreshState = RefreshState.HIDE_PULL_NOREFRESH;
                    }
                    break;
                case LOAD_ORIGINAL:

                    break;
                default:
                    break;
            }
        }

        /**
         *当前状态为刷新时，不做处理
         */
        if (mCurrentScrollState == SCROLL_STATE_FLING && firstVisibleItem == 0
                && currentUpdateState != UpdateViewState.REFRESH_REFRESHING
                && currentRefreshState != RefreshState.SHOW_PULL_REFRESHING) {
            setSelection(1);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        mCurrentScrollState = scrollState;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 根据界面当前状态，改变mDownY
                mDownY = ev.getRawY();
                screenTouched = true;
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveY = ev.getRawY();
                float deltaY = (mMoveY - mDownY) / 3;
                if(lastDelatY > deltaY){
                    smoothUp = true;
                }else{
                    smoothUp = false;
                }
                lastDelatY = deltaY;
                if (mCurrentScrollState == SCROLL_STATE_IDLE) {
                    mCurrentScrollState = SCROLL_STATE_TOUCH_SCROLL;
                }
                if (headBegin) {
                    switch (currentUpdateState) {
                        case REFRESH_ORIGINAL:
                            mHeadViewWeight.setPadding(mHeadViewWeight.getPaddingLeft(), -mHeaderHeight + (int)deltaY,
                                    mHeadViewWeight.getPaddingRight(), mHeadViewWeight.getPaddingBottom());
                            break;
                        case REFRESH_REFRESHING:
                            if(headVisible){
                                mHeadViewWeight.setPadding(mHeadViewWeight.getPaddingLeft(), (int) deltaY,
                                        mHeadViewWeight.getPaddingRight(), mHeadViewWeight.getPaddingBottom());
                            }else{
                                mHeadViewWeight.setPadding(mHeadViewWeight.getPaddingLeft(), -mHeaderHeight + (int)deltaY,
                                        mHeadViewWeight.getPaddingRight(), mHeadViewWeight.getPaddingBottom());
                            }
                            break;
                        case REFRESH_DONE:
                            mHeadViewWeight.setPadding(mHeadViewWeight.getPaddingLeft(), realHeaderPadding + (int)deltaY,
                                    mHeadViewWeight.getPaddingRight(), mHeadViewWeight.getPaddingBottom());
                            break;
                        default:
                            break;
                    }
                }
                //当前为刷新状态
                if(currentUpdateState == UpdateViewState.REFRESH_REFRESHING
                        && currentRefreshState == RefreshState.SHOW_PULL_REFRESHING){
                    mHeadRefreshView.setProgress(1.0f);
                }else{
                    //当getPaddingTop >= 0时，表示头部完全显示
                    int paddingTop = mHeadViewWeight.getPaddingTop();
                    if(paddingTop < 0){//开始对红色小圆圈操作，显示情况
                        if(smoothUp){
                            float scale = 1 - (float)2*Math.abs(paddingTop)/mHeaderHeight;
                            mHeadRefreshView.setProgress(scale < 0f?0f:scale);
                        }else{
                            float scale = 1 - (float)2*Math.abs(paddingTop)/mHeaderHeight;
                            mHeadRefreshView.setProgress(scale >= 1.0f?1.0f:scale);
                        }
                    }else{
                        if(smoothUp){

                        }else{
                            mHeadRefreshView.setProgress(1.0f);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                screenTouched = false;
                refreshViewByPullState();
                break;
        }
        return super.onTouchEvent(ev);
    }

    private HeadDisPlayHeight getCurrentHeaderDisplayState() {
        if (mHeadViewWeight.getBottom() >= 0 && mHeadViewWeight.getBottom() <= mHeaderHeight) {
            return HeadDisPlayHeight.SOME_DISPLAY;
        }else if (mHeadViewWeight.getBottom() >= mHeaderHeight) {
            return HeadDisPlayHeight.TOTAL_DISPLAY;
        } else {
            return HeadDisPlayHeight.HIDE_DISPLAY;
        }
    }

    public void refreshViewByPullState() {
        switch (currentRefreshState) {
            case HIDE_PULL_NOREFRESH:
                resetHeaderPadding();
                break;
            case PART_PULL_NOREFRESH:
                handler.postDelayed(headHideAnimation, 5);
                break;
            case SHOW_PULL_NOREFRESH:
                changeHeaderToRefreshing();
                break;
            case HIDE_PULL_REFRESHING:
                resetHeaderPadding();
                break;
            case PART_PULL_REFRESHING:
                handler.postDelayed(headHideAnimation, 5);
                break;
            case SHOW_PULL_REFRESHING:
                handler.postDelayed(headBackAnimation, 5);
                break;
            case HIDE_PULL_REFRESHED:
                resetHeaderPadding();
                changeHeaderToOriginal();
                currentRefreshState = RefreshState.HIDE_PULL_NOREFRESH;
                break;
            case PART_PULL_REFRESHED:
                handler.postDelayed(headHideAnimation, 5);
                break;
            case SHOW_PULL_REFRESHED:
                handler.postDelayed(headHideAnimation, 5);
                break;
            default:
                break;
        }
    }

    private void resetHeaderPadding() {
        headVisible = false;
        headBegin = false;
        mHeadViewWeight.setPadding(mHeadViewWeight.getPaddingLeft(), -mHeaderHeight, mHeadViewWeight.getPaddingRight(),
                mHeadViewWeight.getPaddingBottom());
    }

    private void changeHeaderToRefreshing() {
        currentUpdateState = UpdateViewState.REFRESH_REFRESHING;
        currentRefreshState = RefreshState.SHOW_PULL_REFRESHING;
        if (mUpdateListener != null) {
            mUpdateListener.refreshing();
        }
        if(!screenTouched){
            handler.postDelayed(headBackAnimation, 5);
            mHeadRefreshView.startAnimate();
        }
    }

    private void changeHeaderToRefershed() {
        currentRefreshState = RefreshState.SHOW_PULL_REFRESHED;
        // 重新设置开始滑动位置
        mDownY = mMoveY;
        realHeaderPadding = mHeadViewWeight.getPaddingTop();
        if (!screenTouched) {
            handler.postDelayed(headHideAnimation, 500);
        }
    }

    private void changeHeaderToOriginal() {
        currentUpdateState = UpdateViewState.REFRESH_ORIGINAL;
        currentRefreshState = RefreshState.HIDE_PULL_NOREFRESH;
        if(!(currentRefreshState == RefreshState.SHOW_PULL_REFRESHING
                && currentUpdateState == UpdateViewState.REFRESH_REFRESHING)){
            mHeadRefreshView.stopAnimate();
        }
    }

    public void refreshViewByRefreshingState() {
        switch (currentUpdateState) {
            case REFRESH_ORIGINAL:
                break;
            case REFRESH_RETURNING:
                break;
            case REFRESH_REFRESHING:
                break;
            case REFRESH_DONE:
                changeHeaderToRefershed();
                break;
            default:
                break;
        }
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        setSelection(1);
        initFooterView();
    }

    private void initFooterView(){
        mFootViewWeight = new FootViewWeight(getContext());
        mFootViewWeight.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,AbsListView.LayoutParams.WRAP_CONTENT));
        addFooterView(mFootViewWeight);
        measureView(mFootViewWeight);
        mFooterHeight = mFootViewWeight.getMeasuredHeight();
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }


    private Runnable headHideAnimation = new Runnable() {
        public void run() {

            if (mHeadViewWeight.getBottom() > 0 && getFirstVisiblePosition() == 0) {
                int paddingTop = (int) (-mHeaderHeight * 0.25f + mHeadViewWeight.getPaddingTop() * 0.75f) - 1;
                if (paddingTop < -mHeaderHeight) {
                    paddingTop = -mHeaderHeight;
                }
                mHeadViewWeight.setPadding(mHeadViewWeight.getPaddingLeft(), paddingTop, mHeadViewWeight.getPaddingRight(),
                        mHeadViewWeight.getPaddingBottom());
                handler.postDelayed(headHideAnimation, 5);
            } else {
                resetHeaderPadding();
                if (currentRefreshState != RefreshState.HIDE_PULL_NOREFRESH && currentRefreshState != RefreshState.HIDE_PULL_REFRESHING
                        && currentRefreshState != RefreshState.HIDE_PULL_REFRESHED) {
                    setSelection(1);
                }
                if (currentUpdateState == UpdateViewState.REFRESH_DONE) {
                    changeHeaderToOriginal();
                } else if (currentUpdateState == UpdateViewState.REFRESH_REFRESHING) {
                    currentRefreshState = RefreshState.HIDE_PULL_REFRESHING;
                } else if (currentUpdateState == UpdateViewState.REFRESH_ORIGINAL) {
                    currentRefreshState = RefreshState.HIDE_PULL_NOREFRESH;
                }
                handler.removeCallbacks(headHideAnimation);
            }
        }
    };

    private Runnable headBackAnimation = new Runnable() {
        public void run() {

            if (mHeadViewWeight.getPaddingTop() > 0) {
                mHeadViewWeight.setPadding(mHeadViewWeight.getPaddingLeft(), (int) (mHeadViewWeight.getPaddingTop() * 0.75f),
                        mHeadViewWeight.getPaddingRight(), mHeadViewWeight.getPaddingBottom());
                handler.postDelayed(headBackAnimation, 5);
            } else {
                headVisible = true;
                handler.removeCallbacks(headBackAnimation);
            }
        }
    };

    public interface UpdateDataListener {
        //下拉刷新
        void refreshing();
        //上拉加载
        void loading();
    }

    public void setCurrentHeaderState(UpdateViewState currentHeaderState) {
        this.currentUpdateState = currentHeaderState;
    }
}
