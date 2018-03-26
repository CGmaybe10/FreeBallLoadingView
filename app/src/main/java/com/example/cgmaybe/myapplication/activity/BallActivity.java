package com.example.cgmaybe.myapplication.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import com.example.cgmaybe.myapplication.R;
import com.example.cgmaybe.myapplication.widget.FreeBallView;

public class BallActivity extends AppCompatActivity implements View.OnClickListener {
    static final String TAG = "moubiao";
    private FreeBallView mFreeBallView;
    private Button mStartBT, mStopBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ball_layout);

        initData();
        initView();
    }

    private void initData() {
    }

    private void initView() {
        mFreeBallView = findViewById(R.id.logo_free_ball);
        mStartBT = findViewById(R.id.start_ani_bt);
        mStopBT = findViewById(R.id.stop_ani_bt);


        mStartBT.setOnClickListener(this);
        mStopBT.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_ani_bt:
                mFreeBallView.startAnimation();
                break;
            case R.id.stop_ani_bt:
                mFreeBallView.stopAnimation();
                break;
            default:
                break;
        }
    }
}
