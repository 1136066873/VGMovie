package vgmovie.heguodong.fragmentsinmaimactivity;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import vgmovie.heguodong.R;
import vgmovie.heguodong.adapter.MainFragmentPagerAdapter;
import vgmovie.heguodong.base.BaseFragment;
import vgmovie.heguodong.customview.CustomTitleTabBar;
import vgmovie.heguodong.fragmentsinmoviefragment.HotFragment;
import vgmovie.heguodong.fragmentsinmoviefragment.OutSeaFragment;
import vgmovie.heguodong.fragmentsinmoviefragment.WaitFragment;
import vgmovie.heguodong.listener.DandyPagerChangeListener;

/**
 * Created by heGuoDong_start  on 2016/6/26 01:18.
 */
public class MovieFragment extends BaseFragment implements CustomTitleTabBar.TitleTabClickListener {

    private FragmentManager manager;
    private CustomTitleTabBar titleTabBar;
    private ViewPager contentPager;
    private List<BaseFragment> fragments = new ArrayList<BaseFragment>();
    private MainFragmentPagerAdapter mAdapter;

    /**
     * 加载布局
     */
    @Override
    public void setContentView(ViewGroup container) {
        rootView = Inflater.inflate(R.layout.fragment_movie_layout, container,false);
        manager = this.getChildFragmentManager();
    }

    /**
     * 初始化显示
     */
    @Override
    public void initView() {
        titleTabBar = (CustomTitleTabBar) rootView.findViewById(R.id.movie_topBar);
        titleTabBar.setTitleTabClickListener(this);
        contentPager = (ViewPager) rootView.findViewById(R.id.contentPager);
        contentPager.setOnPageChangeListener(new DandyPagerChangeListener(contentPager,titleTabBar){
            @Override
            public void focusedFragment(int selectPosition, int lastPosition) {
                super.focusedFragment(selectPosition, lastPosition);
                titleTabBar.setTitleState(selectPosition);
            }
        });

    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        fragments.add(new HotFragment());
        fragments.add(new WaitFragment());
        fragments.add(new OutSeaFragment());

        mAdapter = new MainFragmentPagerAdapter(manager, fragments);
        contentPager.setAdapter(mAdapter);
        contentPager.setCurrentItem(0);
        contentPager.setOffscreenPageLimit(0);

    }

    @Override
    public void callback(int index) {
        contentPager.setCurrentItem(index);
    }

}
