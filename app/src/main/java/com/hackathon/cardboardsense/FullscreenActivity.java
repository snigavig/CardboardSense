package com.hackathon.cardboardsense;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.unity3d.player.UnityPlayer;


public class FullscreenActivity
        extends UnityPlayerActivity {

    UnityPlayer mUnityPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(this, "hahahahaha!", Toast.LENGTH_LONG).show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String xxx = "1.1 1.2 1.3 1.1 1.3 1.4 1.1 1.4 1.3 1.2 1.1 1.3 1.4 1.2 1.1 1.3 1.4 1.5 1.1 1.2 1.3 1.1 1.3 1.4 1.1 1.4 1.3 1.2 1.1 1.3 1.4 1.2 1.1 1.3 1.4 1.5 1.1 1.2 1.3 1.1 1.3 1.4 1.1 1.4 1.3 1.2 1.1 1.3 1.4 1.2 1.1 1.3 1.4 1.5 1.1 1.2 1.3 1.1 1.3 1.4 1.1 1.4 1.3 1.2 1.1 1.3 1.4 1.2 1.1 1.3 1.4 1.5";
                UnityPlayer.UnitySendMessage("hand_right", "SetCoordinates", xxx);
            }
        }, 10000);
    }
}