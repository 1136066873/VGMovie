package vgmovie.heguodong.fragmentsinmaimactivity;

import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import vgmovie.heguodong.R;
import vgmovie.heguodong.base.BaseFragment;

/**
 * Created by heGuoDong_start  on 2016/6/26 16:56.
 *
 * 作用：发现页面
 */
public class DiscoverFragment extends BaseFragment {


    private FragmentManager manager;

    @Override
    public void setContentView(ViewGroup container) {
        rootView = Inflater.inflate(R.layout.fragment_community_layout, container, false);
        manager = this.getChildFragmentManager();

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
