package com.thermocouple.typek.activity;

import android.os.Bundle;
import android.os.Handler;

import com.thermocouple.typek.R;

public class SplashScreenActivity extends MasterActivity {
    private static int SPLASH_TIME_OUT = 3000;
    private boolean isFirstInstall = true;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
    }

    public void onStart() {
        super.onStart();
        handler = new Handler();

        if (functionHelper.getSession("FIRST_INSTALL") != null)
            isFirstInstall = Boolean.parseBoolean(functionHelper.getSession("FIRST_INSTALL"));

        if (isFirstInstall) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    functionHelper.saveSession("FIRST_INSTALL", String.valueOf(false));
                    functionHelper.startIntent(DashboardActivity.class, true, true, null);
                }
            }, SPLASH_TIME_OUT);
        } else {
//            if (functionHelper.getSession("id") != null) {
                functionHelper.startIntent(DashboardActivity.class, true, true, null);
//            } else {
//                if (functionHelper.getSession("URL_API") != null)
//                    functionHelper.startIntent(ActivityLogin.class, true, true, null);
//                else
//                    functionHelper.startIntent(ActivityLanding.class, true, true, null);
//            }
        }
    }

    public void onStop() {
        super.onStop();
        handler.removeCallbacksAndMessages(null);
    }

    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
