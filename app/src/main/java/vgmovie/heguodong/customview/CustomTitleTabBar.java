package vgmovie.heguodong.customview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import vgmovie.heguodong.R;

/**
 * Created by heGuoDong_start  on 2016/6/26 01:30.
 *
 * 作用：自定义的顶部 TitleTabBar
 */
public class CustomTitleTabBar extends FrameLayout implements View.OnClickListener {


    private final Context context;
    private final Resources mResources;
    private int titleSourceId;
    private int whiteColor;
    private int redColor;
    private int itemHeight;
    private int sumWidth;
    private String[] titles;
    private int count;
    private int itemWidth;
    private ImageView tabBar;
    private int currentSelectIndex = 0;
    private SparseArray<TextView> titleArray = new SparseArray<TextView>();
    private TitleTabClickListener mListener;

    public CustomTitleTabBar(Context context) {
        this(context, null, 0);
    }

    public CustomTitleTabBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTitleTabBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.mResources = context.getResources();
        initAttrs(attrs);

        addBar();
        addTitle();


    }

    /**
     * 第一步：得到属性
     */
    private void initAttrs(AttributeSet attrs) {
        this.whiteColor = mResources.getColor(R.color.textcolor_white_a);
        this.redColor = mResources.getColor(R.color.textcolor_red_d);
        this.itemHeight = mResources.getDimensionPixelSize(R.dimen.top_title_bar_height);
        this.sumWidth = mResources.getDimensionPixelSize(R.dimen.top_title_bar_sum_width);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopTitleBar);
        titleSourceId = ta.getResourceId(R.styleable.TopTitleBar_titleSourceId, -1);
        ta.recycle();
        if(titleSourceId != -1){
            titles = mResources.getStringArray(titleSourceId);
            this.count = titles.length;
            this.itemWidth = sumWidth / count;
        }
        ImageView back = new ImageView(context);
        back.setAlpha(0.5f);
        back.setBackgroundResource(R.drawable.shape_grey_back);
        this.addView(back, new LayoutParams(sumWidth, itemHeight));

    }

    /**
     * 第二步之一：画出来Bar
     */
    private void addBar() {
        tabBar = new ImageView(context);
        LayoutParams params = new LayoutParams(itemWidth, itemHeight);
        tabBar.setBackgroundResource(R.drawable.shape_white_back);
        tabBar.setLayoutParams(params);
        this.addView(tabBar);
    }

    /**
     * 第二步之二：
     */
    private void addTitle() {
        LinearLayout content = new LinearLayout(context);
        content.setBackgroundColor(mResources.getColor(R.color.transparent));
        this.addView(content, new LayoutParams(sumWidth, itemHeight));
        for(int i = 0;i<count;i++){
            content.addView(singleTitle(i));
        }

    }

    /**
     * 具体的根据位置添加 Title
     */
    private View singleTitle(int index) {
        TextView title = new TextView(context);
        if(index == currentSelectIndex){
            title.setTextColor(redColor);
        }else{
            title.setTextColor(whiteColor);
        }
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX,mResources.getDimensionPixelSize(R.dimen.title_text_size));
        title.setOnClickListener(this);//监听类在下面
        title.setTag(index);
        title.setText(titles[index]);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemWidth, itemHeight);
        params.weight = 1;
        title.setGravity(Gravity.CENTER);
        title.setLayoutParams(params);
        titleArray.put(index, title);
        return title;
    }


    @Override
    public void onClick(View v) {
        if(v instanceof TextView){
            setTitleState((Integer) v.getTag());
            if(mListener != null){
                mListener.callback(currentSelectIndex);
            }
        }

    }

    /**
     *设置标题栏点击效果
     *@param  selectedIndex:被点击的标题下标
     */
    public void setTitleState(int selectedIndex){
        TextView last = titleArray.get(currentSelectIndex);
        if(last != null){
            last.setTextColor(whiteColor);
            last.setAlpha(1.0f);
        }
        currentSelectIndex = selectedIndex;
        TextView title = titleArray.get(currentSelectIndex);
        if(title != null){
            title.setAlpha(1.0f);
            title.setTextColor(redColor);
        }
    }

    /**
     *标题点击回调
     */
    public interface TitleTabClickListener{
        void callback(int index);
    }

    public void scrollBar(float startX,float endX,int duration){
        ObjectAnimator oa = ObjectAnimator.ofFloat(tabBar, "x", startX,endX);
        oa.setDuration(duration);
        oa.start();
    }

    public void setTitleTabClickListener(TitleTabClickListener listener){
        this.mListener = listener;
    }


    public String[] getTitles(){
        return titles;
    }

    public int getTitleCount(){
        return count;
    }

    public float getItemWidth(){
        return itemWidth;
    }

    public ImageView getTabBar(){
        return tabBar;
    }

    public View getSelectedTitleView(int index){
        return titleArray.get(index);
    }

    public void setTitleAlpha(int index,float alpha){
        titleArray.get(index).setAlpha(alpha);
    }


}
