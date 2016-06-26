package vgmovie.heguodong.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by heGuoDong_start  on 2016/6/26 01:13.
 *
 * 作用：此类是填充在MainActivity 的帧布局中的四个xxxxFragment 的基类
 */
public abstract class BaseFragment extends Fragment{
    public Context context;
    public LayoutInflater Inflater;
    public View rootView;
    public boolean isDestroyView = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
        this.Inflater = LayoutInflater.from(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        setContentView(container);
        initView();
        initData();
        setOnClickListener();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        isDestroyView = true;
        super.onDestroyView();
    }
    public abstract void setContentView(ViewGroup container);
    public abstract void initView();
    public abstract void initData();
    public void setOnClickListener(){};


}
