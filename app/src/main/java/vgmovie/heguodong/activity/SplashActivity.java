package vgmovie.heguodong.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import vgmovie.heguodong.MainActivity;
import vgmovie.heguodong.R;
import vgmovie.heguodong.utils.Url;
import vgmovie.heguodong.domain.GuidePagerBean;

public class SplashActivity extends Activity {

    private TextView tv_main ;
    private ImageView iv_icon_location ;
    private ImageView iv_icon_net ;
    private boolean netConnectYesOrNo = false;//记录第一次联网请求jsonString 时的联网状态
    private boolean hasAnyNetPicture  = false;//记录得到的jsonString 中到底有没有图片
    private Handler handler = new Handler();
    private ScaleAnimation scaleAnimation;//网络图片的动画模式


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 使屏幕不显示标题栏(必须要在setContentView方法执行前执行)
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏，使内容全屏显示(必须要在setContentView方法执行前执行)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        overridePendingTransition(Animation.INFINITE, Animation.INFINITE);
        findViews();
        initAnimation();
        scaleAnimation.setAnimationListener(new MyAnimationListener());

        getPictureDataFromNet();

    }



    /**
     * 初始化视图显示
     */
    private void findViews() {
//        tv_main = (TextView)findViewById(R.id.tv_main);
        iv_icon_location = (ImageView)findViewById(R.id.iv_icon_location);
        iv_icon_net = (ImageView)findViewById(R.id.iv_icon_net);
    }

    /**
     * 进来就联网请求数据
     */
    private void getPictureDataFromNet() {
        RequestParams requestParams = new RequestParams(Url.SPLASHPICTURE_URL);
//        requestParams.setConnectTimeout(1000 );//1秒的连接超时
        Log.e("TAG", "联网地址是" + Url.SPLASHPICTURE_URL);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG","联网成功");
                //缓存得到的String 类型的数据
                netConnectYesOrNo = true ;
                processData(result);//解析数据

                sendMessage();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG","联网错误By" + ex.toString()  );
                netConnectYesOrNo = false;
                sendMessage();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("TAG","联网取消By" + cex.toString()  );
            }

            @Override
            public void onFinished() {
                Log.e("TAG","联网请求结束");
            }
        });

    }

    /**
     * 解析得到的 jsonString
     */
    private void processData(String jsonString) {

        //①Gson解析数据，得到图片的地址
        GuidePagerBean newsCenterBean = new Gson().fromJson(jsonString, GuidePagerBean.class) ;

        int size = newsCenterBean.getPosters().size();

        if(size != 0 ) {
            hasAnyNetPicture = true ;
            GuidePagerBean.PostersBean postersBean = newsCenterBean.getPosters().get(size - 1);

            String picString = postersBean.getPic();

//            iv_icon_location.setVisibility(View.GONE);
            //②用Glide 加载图片
            Glide.with(this).load(picString)
                    .error(R.drawable.bg_vg_movies)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iv_icon_net);

        }else {
            hasAnyNetPicture = false ;
            return;

        }




    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        //缩放动画：大小从0~1变大,缩放中心：界面中心
        scaleAnimation = new ScaleAnimation(1f, 1.18f, 1f, 1.18f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        Log.e("TAG", "测试动画是不是等于null-----1----- " + (scaleAnimation == null));
        scaleAnimation.setDuration(3000);
        scaleAnimation.setFillAfter(false);


    }


    /**
     * 发送一个延迟消息
     */
    private void sendMessage() {
        if(netConnectYesOrNo && hasAnyNetPicture) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           //让第一个本地的图片消失
                           iv_icon_location.setVisibility(View.GONE);
                           //让网络图片呈现出来
                           iv_icon_net.startAnimation(scaleAnimation);
                       }
                   });

                }
            }, 3000);



        }else {//包含-------((!netConnectYesOrNo) || (!hasAnyNetPicture))
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //让第一个本地的图片消失
                    iv_icon_location.setVisibility(View.GONE);
                    //让第二个图片也消失
                    iv_icon_net.setVisibility(View.GONE);

                    Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            },6000);

        }

    }

    /**
     * 自定义的动画播放监听类
     */
    class MyAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                            scaleAnimation.cancel();
                            iv_icon_net.setVisibility(View.GONE);

                        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();



                }
            });

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }



}
