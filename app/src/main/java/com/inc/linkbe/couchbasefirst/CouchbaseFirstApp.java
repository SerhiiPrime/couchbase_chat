package com.inc.linkbe.couchbasefirst;

import android.app.Application;
import android.content.Context;

/**
 * Created by linkbe on 7/21/14.
 */
public class CouchbaseFirstApp extends Application {

    private static Context mContext;


    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        CBHelper.INSTANCE.init();
    }


    public static Context getAppContext() {
        return mContext;
    }



    @Override
    public void onTrimMemory (int level) {
        CBHelper.INSTANCE.manager.close();
    }
}
