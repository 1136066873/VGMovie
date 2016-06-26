package vgmovie.heguodong.customview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;

import vgmovie.heguodong.R;
import vgmovie.heguodong.utils.PhoneUtils;

/**
 * Created by heGuoDong_start  on 2016/6/26 18:21.
 */
public class MenuItemView extends RelativeLayout implements View.OnClickListener  {

    private int leftPadding = -1,rightPadding = -1;
    private float textSize = -1;
    private int textColor = -1;
    private int backgroundSourceId = -1,rightFlagImgSourceId = -1,leftFlagImgSourceId;
    private int topDeviderSourceId = -1,bottomDeviderSourceId = -1;
    private int deviderHeight = -1;
    private String textString;
    private boolean isDeviderAlignTextLeft;
    private int mRippleColor;
    private boolean mHover = true;
    private float mAlphaFactor;

    private TextView mTextView;
    private ImageView mRightImage,mLeftImage;
    private View bottom;
    private int leftImageSize,rightImageSize;

    private float mDownX;
    private float mDownY;
    private float mDensity;
    private float mRadius;
    private RadialGradient mRadialGradient;
    private Paint mPaint;
    private Path mPath;
    private Rect mRect;
    private ObjectAnimator mRadiusAnimator;
    private boolean mAnimationIsCancel;

    private RightFlagClickListener mListener;
    private ItemClickListener mListener2;
    private int height;

    private static final float defaultTextSize = 16;
    private static final int RIGHTFLAG_ID = 0x01;
    private static final int LEFTIMAGEID = 0x02;
    private static final int TEXTID = 0x03;



    public MenuItemView(Context context) {
        this(context,null,0);
    }

    public MenuItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MenuItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        addView();
        this.setOnClickListener(this);


    }




    /**
     * 初始化属性集合，得到属性值
     */
    private void init(AttributeSet attrs){

        setRippleColor(Color.BLACK, 0.2f);

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.MenuItemView);
        leftPadding = ta.getDimensionPixelSize(R.styleable.MenuItemView_leftPadding, 0);
        rightPadding = ta.getDimensionPixelSize(R.styleable.MenuItemView_rightPadding, 0);
        textSize =  ta.getDimensionPixelSize(R.styleable.MenuItemView_textSize, -1);
        textColor = ta.getColor(R.styleable.MenuItemView_textColor, -1);
        backgroundSourceId = ta.getResourceId(R.styleable.MenuItemView_mbackground, -1);
        rightFlagImgSourceId = ta.getResourceId(R.styleable.MenuItemView_rightFlagImg, -1);
        leftFlagImgSourceId = ta.getResourceId(R.styleable.MenuItemView_leftFlagImg, -1);
        topDeviderSourceId = ta.getResourceId(R.styleable.MenuItemView_topDevider, -1);
        bottomDeviderSourceId = ta.getResourceId(R.styleable.MenuItemView_bottomDevider, -1);
        deviderHeight = ta.getDimensionPixelSize(R.styleable.MenuItemView_deviderHight, -1);
        textString = ta.getString(R.styleable.MenuItemView_textString);
        isDeviderAlignTextLeft = ta.getBoolean(R.styleable.MenuItemView_isDeviderAlignTextLeft, false);
        leftImageSize = ta.getDimensionPixelSize(R.styleable.MenuItemView_leftImageSize, LayoutParams.WRAP_CONTENT);
        rightImageSize = ta.getDimensionPixelSize(R.styleable.MenuItemView_rightImageSize, LayoutParams.WRAP_CONTENT);
        mRippleColor = ta.getColor(R.styleable.MenuItemView_rippleColor,mRippleColor);
        mAlphaFactor = ta.getFloat(R.styleable.MenuItemView_alphaFactor,mAlphaFactor);
        mHover = ta.getBoolean(R.styleable.MenuItemView_hover, mHover);
        ta.recycle();
//        this.setBackgroundResource(backgroundSourceId != -1 ? backgroundSourceId:getResources().getColor(android.R.color.white));

        mDensity = getContext().getResources().getDisplayMetrics().density;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAlpha(100);
        mPath = new Path();
    }

    /**
     * 把得到的属性值画出来
     */
    private void addView(){

        mLeftImage = new ImageView(getContext());
        mLeftImage.setId(LEFTIMAGEID);
        LayoutParams leftImageParams = new LayoutParams(leftImageSize,leftImageSize);
        leftImageParams.leftMargin = leftPadding;
        leftImageParams.addRule(RelativeLayout.CENTER_VERTICAL | RelativeLayout.ALIGN_PARENT_LEFT);
        if(leftFlagImgSourceId != -1){
            mLeftImage.setBackgroundResource(leftFlagImgSourceId);
        }
        mLeftImage.setLayoutParams(leftImageParams);
        this.addView(mLeftImage);

        mTextView = new TextView(getContext());
        mTextView.setId(TEXTID);
        mTextView.setGravity(Gravity.CENTER_VERTICAL);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize != -1 ? textSize: PhoneUtils.dp2px(getContext(), defaultTextSize));
        mTextView.setTextColor(textColor != -1 ?textColor:getResources().getColor(android.R.color.black));
        mTextView.setText(textString);
        LayoutParams textParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        textParams.leftMargin = leftPadding;
        textParams.addRule(RelativeLayout.RIGHT_OF, LEFTIMAGEID);
        mTextView.setLayoutParams(textParams);
        this.addView(mTextView);

        if(rightFlagImgSourceId != -1){
            mRightImage = new ImageView(getContext());
            LayoutParams rightImgParams = new LayoutParams(rightImageSize,rightImageSize);
            rightImgParams.rightMargin = rightPadding;
            rightImgParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
            rightImgParams.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);
            mRightImage.setBackgroundResource(rightFlagImgSourceId);
            mRightImage.setOnClickListener(this);
            mRightImage.setFocusable(false);
            mRightImage.setId(RIGHTFLAG_ID);
            this.addView(mRightImage, rightImgParams);
        }

        if(topDeviderSourceId != -1){
            View top = new View(getContext());
            LayoutParams topParams = new LayoutParams(LayoutParams.MATCH_PARENT, deviderHeight != -1 ?deviderHeight:2);
            topParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            top.setBackgroundResource(topDeviderSourceId);
            this.addView(top, topParams);
        }

        if(bottomDeviderSourceId != -1){
            bottom = new View(getContext());
            LayoutParams bottomParams = new LayoutParams(LayoutParams.MATCH_PARENT, deviderHeight != -1 ?deviderHeight:2);
            bottomParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            if(isDeviderAlignTextLeft){
                bottomParams.addRule(RelativeLayout.ALIGN_START, TEXTID);
            }
            bottom.setBackgroundResource(bottomDeviderSourceId);
            this.addView(bottom, bottomParams);
        }
    }


    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode()) {
            return;
        }
        canvas.save(Canvas.CLIP_SAVE_FLAG);
        mPath.reset();
        mPath.addCircle(mDownX, mDownY, mRadius, Path.Direction.CW);
        canvas.clipPath(mPath);
        canvas.restore();
        canvas.drawCircle(mDownX, mDownY, mRadius, mPaint);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == RIGHTFLAG_ID){
            if(mListener != null){
                mListener.rightFlagclicked();
            }
        }else{
            if(mListener2 != null){
                mListener2.itemClicked();
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//		mMaxRadius = (float) Math.sqrt(w * w + h * h);
        height = getHeight();
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean superResult = super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(this.isEnabled() && mHover){
                    mRect = new Rect(getLeft(), getTop(), getRight(), getBottom());
                    mAnimationIsCancel = false;
                    mDownX = event.getX();
                    mDownY = event.getY();
                    mRadiusAnimator = ObjectAnimator.ofFloat(this, "radius", 0, dp(height/4)).setDuration(400);
                    mRadiusAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                    mRadiusAnimator.start();
                    if (!superResult) {
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                mDownX = event.getX();
                mDownY = event.getY();
                if (mAnimationIsCancel = !mRect.contains(getLeft() + (int) event.getX(), getTop() + (int) event.getY())) {
                    setRadius(0);
                } else {
                    setRadius(dp(height/4));
                }
                if (!superResult) {
                    return true;
                }
                break;
            default:
                mRadiusAnimator = ObjectAnimator.ofFloat(this, "radius", dp(height/4),0);
                mRadiusAnimator.setDuration(400);
                mRadiusAnimator .setInterpolator(new AccelerateDecelerateInterpolator());
                mRadiusAnimator.start();
                mRadiusAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        setRadius(0);
                        ViewHelper.setAlpha(MenuItemView.this, 1);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }
                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                });

                if (!superResult) {
                    return true;
                }
                break;
        }
        return superResult;
    }

    /**
     * 设置圆角图形--最终目的是造水波纹
     */
    private void setRadius(final float radius) {
        mRadius = radius;
        if (mRadius > 0) {
            mRadialGradient = new RadialGradient(mDownX, mDownY, mRadius,
                    adjustAlpha(mRippleColor, mAlphaFactor), mRippleColor,//添加透明度动画--抽取出一个方法用来调用
                    Shader.TileMode.MIRROR);
            mPaint.setShader(mRadialGradient);
        }
        invalidate();
    }

    /**
     * 把int 类型的数据转成 dp
     */
    private int dp(int dp) {
        return (int) (dp * mDensity + 0.5f);
    }

    /**
     * 在圆圈的基础上添加水波纹动画效果
     */
    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }



    public void setRightClickListener(RightFlagClickListener listener){
        this.mListener = listener;
    }

    public void setItemClickListener(ItemClickListener listener){
        this.mListener2 = listener;
    }

    public void setRightFlagBack(int resId){
        if(mRightImage != null){
            mRightImage.setBackgroundResource(resId);
        }
    }

    public void setBottomVisivility(int visibility){
        bottom.setVisibility(visibility);
    }


    public void setHover(boolean enabled) {
        mHover = enabled;
    }

    public interface RightFlagClickListener{
        void rightFlagclicked();
    }

    public interface ItemClickListener{
        void itemClicked();
    }

    public void setRippleColor(int rippleColor, float alphaFactor) {
        mRippleColor = rippleColor;
        mAlphaFactor = alphaFactor;
    }


}
