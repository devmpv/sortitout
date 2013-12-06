package com.me.sortitout;


import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.me.sortitout.ApplicationHandler;

public class MainActivity extends AndroidApplication{
	protected AdView adView;

    // This is the callback that posts a message for the handler

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create the layout
        RelativeLayout layout = new RelativeLayout(this);
        
        //Params
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        cfg.useAccelerometer = false;
        cfg.maxSimultaneousSounds = 1;
        cfg.useWakelock = false;
        cfg.numSamples = 2;
        
        // Create the libgdx View
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
     
        // Create and setup the AdMob view
        adView = new AdView(this, AdSize.SMART_BANNER, "a1522ef2f38ba91");
        AdRequest adRequest = new AdRequest();
        adRequest.addTestDevice(AdRequest.TEST_EMULATOR); 
        adRequest.addTestDevice("79E92E554ACEC25E7C0744DB62D560B7");
        adView.loadAd(adRequest);
        // Add the libgdx view
        View gameView = initializeForView(new ApplicationHandler(new RequestHandler(adView)), false);
        layout.addView(gameView);
        // Add the AdMob view
        RelativeLayout.LayoutParams adParams = 
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        /* UNCOMMENT TO ENABLE ADS!
        layout.addView(adView, adParams);
        */
        // Hook it all up
        setContentView(layout);
    }
}