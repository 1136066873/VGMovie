package vgmovie.heguodong.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Spanned;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by heGuoDong_start  on 2016/6/26 03:09.
 */
public class  ViewHolder{
    private SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;
    private int itemViewType;

    private ViewHolder(Context context, ViewGroup parent, int layoutId,int position,int itemViewType) {
        this.mPosition = position;
        this.itemViewType = itemViewType;
        this.mViews = new SparseArray<View>();
        this.mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,false);
        // setTag
        this.mConvertView.setTag(this);
    }

    /**
     * 拿到一个ViewHolder对象
     */
    public static ViewHolder get(Context context, View convertView,ViewGroup parent, int layoutId, int position,int itemViewType) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position,itemViewType);
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        if(viewHolder != null && viewHolder.getItemViewType() == itemViewType){
            return viewHolder;
        }else{
            viewHolder = null;
        }
        return new ViewHolder(context, parent, layoutId, position,itemViewType);
    }

    public int getItemViewType() {
        return itemViewType;
    }

    public View getConvertView() {
        return this.mConvertView;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     *通过控件的Id控制控件是否显示
     */
    public void setVisibility(int viewId,int visibility){
        View view = getView(viewId);
        view.setVisibility(visibility);
    }

    /**
     *通过控件的Id控制控件是否可点击
     */
    public void setClickable(int viewId,boolean clickable){
        View view = getView(viewId);
        view.setClickable(clickable);
    }

    /**
     * 为TextView设置字符串
     */
    public void setText(int viewId, String text) {
        TextView view = getView(viewId);
        if(view != null && text != null){
            view.setText(text);
        }
    }

    public void setText(int viewId,Spanned spanned){
        TextView view = getView(viewId);
        if(view != null && spanned != null){
            view.setText(spanned);
        }
    }

    public void setTextColor(int viewId,int color){
        TextView view = getView(viewId);
        if(view != null){
            view.setTextColor(color);
        }
    }

    /**
     * 为ImageView设置图片
     */
    public void setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        if(view != null){
            view.setImageResource(drawableId);
        }
    }

    /**
     * 为ImageView设置图片
     */
    public void setImageBitmap(int viewId, Bitmap bm){
        ImageView view = getView(viewId);
        if(view != null){
            view.setImageBitmap(bm);
        }
    }

    public int getPosition() {
        return mPosition;
    }

    /**
     *清空 mViews
     */
    public void clearViews(){
        mViews.clear();
        mViews = null;
    }
}
