package vgmovie.heguodong.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import vgmovie.heguodong.base.BaseFragment;

/**
 * Created by heGuoDong_start  on 2016/6/26 03:32.
 */
public class MainFragmentPagerAdapter  extends FragmentPagerAdapter {

    private List<BaseFragment> fragments;

    public MainFragmentPagerAdapter(FragmentManager manager,List<BaseFragment> fragments) {
        super(manager);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        if(fragments != null)
            return fragments.get(position);
        return null;
    }

    @Override
    public int getCount() {
        if(fragments != null)
            return fragments.size();
        return 0;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
