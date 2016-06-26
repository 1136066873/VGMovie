package vgmovie.heguodong.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import vgmovie.heguodong.R;


/**
 * Created by heGuoDong_start  on 2016/6/26 18:01.
 */
public class ImageDoubleTextView  extends RelativeLayout {

    public Context context;
    private int imageSource;
    private int imageSize;
    private String topDefaultText,bottomDefaultText;
    private int topTextColor,bottomTextColor;
    private int topTextSize,bottomTextSize;
    private int textMarginLeft;
    private int defaultColor;
    private int defaultSize;

    private ImageView imageView;
    private TextView topText,bottomText;

    private static final int IMGID = 0x01;
    private static final int TOPTEXTID = 0x02;



    public ImageDoubleTextView(Context context) {
        this(context, null, 0);
    }

    public ImageDoubleTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageDoubleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        init(attrs);//初始化属性集合
        addImageView();//画图
        addTopTextView();//画顶部的字
        addBottomTextiew();//画底部的字

    }

    /**
     * 初始化属性集合
     */
    private void init(AttributeSet attrs){
        this.setGravity(Gravity.CENTER);
        defaultColor = context.getResources().getColor(R.color.textcolor_black_b3);
        defaultSize = context.getResources().getDimensionPixelSize(R.dimen.tab_text_size);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ImageDoubleTextView);
        imageSource = ta.getResourceId(R.styleable.ImageDoubleTextView_imageSource, -1);
        imageSize = ta.getDimensionPixelSize(R.styleable.ImageDoubleTextView_imageSize, LayoutParams.WRAP_CONTENT);
        topDefaultText = ta.getString(R.styleable.ImageDoubleTextView_top_default_text);
        bottomDefaultText = ta.getString(R.styleable.ImageDoubleTextView_bottom_default_text);
        topTextColor = ta.getResourceId(R.styleable.ImageDoubleTextView_top_text_color, defaultColor);
        bottomTextColor = ta.getResourceId(R.styleable.ImageDoubleTextView_bottom_text_color, defaultColor);
        topTextSize = ta.getDimensionPixelSize(R.styleable.ImageDoubleTextView_top_text_size, defaultSize);
        bottomTextSize = ta.getDimensionPixelSize(R.styleable.ImageDoubleTextView_bottom_text_size, defaultSize);
        textMarginLeft = ta.getDimensionPixelSize(R.styleable.ImageDoubleTextView_text_margin_left, 0);
        ta.recycle();
    }

    /**
     * 画图
     */
    private void addImageView(){
        imageView = new ImageView(context);
        imageView.setId(IMGID);
        LayoutParams params = new LayoutParams(imageSize,imageSize);
        params.leftMargin = textMarginLeft;
        params.rightMargin = textMarginLeft;
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        if(imageSource != -1){
            imageView.setBackgroundResource(imageSource);
        }
        imageView.setLayoutParams(params);
        this.addView(imageView);
    }


    /**
     * 画顶部的字
     */
    private void addTopTextView(){
        topText = new TextView(context);
        topText.setId(TOPTEXTID);
        topText.setText(topDefaultText);
        topText.setSingleLine(true);
        topText.setTextColor(topTextColor);
        topText.setGravity(Gravity.CENTER);
        topText.setTextSize(TypedValue.COMPLEX_UNIT_PX,topTextSize);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.RIGHT_OF, IMGID);
        topText.setLayoutParams(params);
        this.addView(topText);
    }


    /**
     * 画底部的字
     */
    private void addBottomTextiew(){
        bottomText = new TextView(context);
        bottomText.setText(bottomDefaultText);
        bottomText.setTextColor(bottomTextColor);
        bottomText.setGravity(Gravity.CENTER);
        bottomText.setSingleLine(true);
        bottomText.setTextSize(TypedValue.COMPLEX_UNIT_PX,bottomTextSize);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.RIGHT_OF,IMGID);
        params.addRule(RelativeLayout.BELOW, TOPTEXTID);
        bottomText.setLayoutParams(params);
        this.addView(bottomText);
    }


}
