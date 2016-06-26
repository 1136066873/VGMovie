package vgmovie.heguodong.adapter;

import android.content.Context;

import java.util.List;

import vgmovie.heguodong.R;

/**
 * Created by heGuoDong_start  on 2016/6/26 03:06.
 *
 * 热映的适配器
 */
public class HotAdapter extends CommonAdapter<String>{

    public HotAdapter(Context context, List<String> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder viewHolder, int position, String item,int itemViewType) {
        viewHolder.setText(R.id.text, item);
    }
}
