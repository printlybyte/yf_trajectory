package com.caitiaobang.core.app.app;

import android.content.Context;

public class Latte {
    private static Context mcontext;


    public static void init(Context context) {
        mcontext = context;
    }

    public static Context getApplicationContext() {
        return mcontext;
    }


}
