package vgmovie.heguodong.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.InflateException;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import vgmovie.heguodong.R;
import vgmovie.heguodong.utils.PhoneUtils;

/**
 * Created by heGuoDong_start  on 2016/6/25 21:15.
 *
 * 作用：这是自定义底部的 Tab 类用以代替本来是用的 RadioGroup
 */
public class CustomTabItemView extends LinearLayout implements View.OnClickListener {

    //=====================


    private  Context context;

    private ImageView contentLogo;
    private TextView contentText;

    //背景图片，字体
    private int logoBackResourceId;
    private String textString;

    //style里面的属性--共有的--图片大小、字体颜色、字体大小（呵呵都是共有的）
    private int textColor;
    private float textSize;
    private int contentLogoSize;

    //设置下字体的默认大小吧（如果在获取属性失败的前提下哦）
    private static final float defaultTextSize = 16;
    //默认的字体颜色，选中的字体颜色（进行切换时候转换使用）
    private int defaultColor,selectedColor;
    private TabClickListner mClickListner;
    //=====================



    public CustomTabItemView(Context context) {
        this(context, null);
    }

    public CustomTabItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTabItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;


        initAttrs(attrs);//得到属性值

        drawView();//把得到的属性值画出来


    }



    /**
     * 这里是是用系统的工具得到属性，好处是能得到图片之类的东西。
     */
    private void initAttrs(AttributeSet attrs) {
        /**设置点击监听事件*/
        this.setOnClickListener(this);
        //从attrs.xml中获取自定义属性     (AttributeSet set, int[] attrs)
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomTabItemView);
        //图片资源
        logoBackResourceId = ta.getResourceId(R.styleable.CustomTabItemView_contentLogoBack, -1);
        //字体颜色
        textColor = ta.getColor(R.styleable.CustomTabItemView_contentTextColor, getResources().getColor(android.R.color.black));
        //字体大小
        textSize = ta.getDimensionPixelSize(R.styleable.CustomTabItemView_contentTextSize, PhoneUtils.dp2px(context, defaultTextSize));
        //显示字体
        textString = ta.getString(R.styleable.CustomTabItemView_contentTextString);
        //图片大小
        contentLogoSize = ta.getDimensionPixelSize(R.styleable.CustomTabItemView_contentLogoSize, LayoutParams.WRAP_CONTENT);

        ta.recycle();
        defaultColor = context.getResources().getColor(R.color.textcolor_black_b3);
        selectedColor = context.getResources().getColor(R.color.textcolor_red_d);

    }


    /**对底部自定义控件的点击监听事件*/
    @Override
    public void onClick(View v) {
        setTabSelected(true);
        if(mClickListner != null){
            //让外部去实现点击事件的回调处理
            mClickListner.onTabClick(this);
        }
    }

    /**
     * 把得到的属性值画出来
     */
    private void drawView() {
        /**
         * 需要自定义属性里面的logoBackResourceId、contentLogoSize
         */
        contentLogo = new ImageView(context);
        contentLogo.setFocusable(false);
        contentLogo.setClickable(false);

        //设置图片的参数--制定宽高
        LayoutParams logoParams = new LayoutParams(contentLogoSize,contentLogoSize);
        contentLogo.setLayoutParams(logoParams);

        //创建布局完毕，需要设置背景图片
        if(logoBackResourceId != -1){
            contentLogo.setBackgroundResource(logoBackResourceId);
        }else{
            throw new InflateException("未设置填充图片资源");
        }

        this.addView(contentLogo);


        /**
         * 需要自定义属性里面的textString、textColor、textSize
         */
        if(!TextUtils.isEmpty(textString)){
            contentText = new TextView(context);
            contentText.setFocusable(false);
            contentText.setClickable(false);

            //设置字体的参数
            LayoutParams textParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            textParams.topMargin = PhoneUtils.dp2px(context,3);
            contentText.setLayoutParams(textParams);
            contentText.setTextColor(textColor);
            contentText.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
            contentText.setText(textString);
            this.addView(contentText);
        }


    }

    /**
     *设置选中状态
     */
    public void setTabSelected(boolean enable){
        //当图标不为空的时候才可选
        if(contentLogo != null){
            contentLogo.setSelected(enable);
        }
        if(contentText != null){
            if(enable){
                contentText.setTextColor(selectedColor);
            }else{
                contentText.setTextColor(defaultColor);
            }
        }
    }




    /**
     * 设置一个接口
     */
    public interface TabClickListner{
        void onTabClick(View view);
    }

    /**
     *设置点击监听事件
     */
    public void setTabClickListener(TabClickListner listner){
        this.mClickListner = listner;
    }


}
