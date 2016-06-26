package vgmovie.heguodong.fragmentsinmoviefragment;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import vgmovie.heguodong.R;
import vgmovie.heguodong.adapter.HotAdapter;
import vgmovie.heguodong.base.BaseFragment;
import vgmovie.heguodong.updateview.UpdateListView;

/**
 * Created by heGuoDong_start  on 2016/6/26 02:23.
 *
 * 作用：这个是在 MovieFragment 中默认加载的第一个页面！！！太特码不容易了。。。
 */
public class HotFragment  extends BaseFragment  implements UpdateListView.UpdateDataListener {

    private HotAdapter mAdapter;
    private UpdateListView mListView;
    private List<String> datas = new ArrayList<String>();

    private static final int REFRESH_OK = 0;
    private static final int LOAD_OK = 1;

    @Override
    public void setContentView(ViewGroup container) {
        rootView = Inflater.inflate(R.layout.fragment_movie_hot_layout, container,false);
    }

    @Override
    public void initView() {
        mListView = (UpdateListView) rootView.findViewById(R.id.update_listview);
        mAdapter = new HotAdapter(context, datas, R.layout.fragment_movie_hot_item_layout);
        mListView.setAdapter(mAdapter);
        mListView.setOnUpdateListener(this);

    }

    @Override
    public void initData() {

        if(!isDestroyView){
            for(int i = 0;i<15;i++){
                datas.add("测试数据:"+i);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshing() {
        new Thread() {

            @Override
            public void run() {
                try {
                    sleep(3000);
                    mHandler.sendEmptyMessage(REFRESH_OK);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }.start();


    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case REFRESH_OK:
                    datas.clear();
                    for(int i = 0;i<15;i++){
                        datas.add("I am new refresh data:"+i);
                    }
                    mAdapter.notifyDataSetChanged();
                    mListView.setCurrentHeaderState(UpdateListView.UpdateViewState.REFRESH_DONE);
                    mListView.refreshViewByRefreshingState();
                    break;
                case LOAD_OK:
                    break;
            }
        };
    };

    @Override
    public void loading() {



    }
}
