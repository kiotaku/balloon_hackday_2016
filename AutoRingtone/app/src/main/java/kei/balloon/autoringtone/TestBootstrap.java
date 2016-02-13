package kei.balloon.autoringtone;


import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;

/**
 * Created by kei on 2016/02/13.
 */
public class TestBootstrap extends Application {
    @Override public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();
    }
}
