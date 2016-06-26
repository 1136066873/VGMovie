package vgmovie.heguodong.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import vgmovie.heguodong.R;

/**
 * Created by heGuoDong_start  on 2016/6/26 17:41.
 */
public class RoundedImageView extends ImageView {

    public   Context context;
    private int mBorderThickness = 0;
    private int mBorderColor = 0xFFFFFFFF;

    public RoundedImageView(Context context) {
        this(context, null, 0);
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setCustomAttributes(attrs);//包含两个步骤，得到属性，画出属性

    }

    /**
     * 包含两个过程，这是第一个过程：得到属性，下面重写的 onDraw 方法是画出属性
     */
    private void setCustomAttributes(AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Roundedimageview);
        mBorderThickness = a.getDimensionPixelSize(R.styleable.Roundedimageview_border_thickness,0);
        mBorderColor = a.getColor(R.styleable.Roundedimageview_border_color,mBorderColor);
        a.recycle();
    }


    /**
     * 重写 onDraw 方法，画出图片属性，不画的话是一片空白，不能显示任何东西
     */
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        this.measure(0, 0);
        if(drawable.getClass() == NinePatchDrawable.class)
            return;
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

        int w = getWidth(), h = getHeight();

        int radius = (w < h ? w : h) / 2 - mBorderThickness;
        Bitmap roundBitmap = getCroppedBitmap(bitmap, radius);//写成单独方法，在这里调用(而且有get就有set)
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(mBorderColor);
        canvas.drawCircle(w / 2, h / 2, radius + mBorderThickness, paint);
        canvas.drawBitmap(roundBitmap, w / 2 - radius, h /2 - radius, null);

    }


    /**
     *得到属性图片
     */
    private Bitmap getCroppedBitmap(Bitmap bmp, int radius) {

        Bitmap scaledSrcBmp;
        int diameter = radius * 2;
        if (bmp.getWidth() != diameter || bmp.getHeight() != diameter)
            scaledSrcBmp = Bitmap.createScaledBitmap(bmp, diameter, diameter, false);
        else
            scaledSrcBmp = bmp;
        Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(), scaledSrcBmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(), scaledSrcBmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(scaledSrcBmp.getWidth() / 2, scaledSrcBmp.getHeight() / 2, scaledSrcBmp.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);

        return output;

    }


    @Override
    public void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(background);
    }
}
