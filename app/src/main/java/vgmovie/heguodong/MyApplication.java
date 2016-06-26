package vgmovie.heguodong;

import android.app.Application;

import org.xutils.x;

/**
 * Created by heGuoDong_start  on 2016/6/22 22:55.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);

    }
}
