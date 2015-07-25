package com.hackathon.cardboardsense;

import android.os.Bundle;
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
        UnityPlayer.UnitySendMessage("LeftHand", "Move", "blaaa");
    }
}