package com.example.cgmaybe.myapplication.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.example.cgmaybe.myapplication.R;
import com.example.cgmaybe.myapplication.widget.FreeBallView;

public class BallActivity extends AppCompatActivity {
    static final String TAG = "moubiao";
    private FreeBallView mFreeBallView;
    private AnimatorSet mAnimatorSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ball_layout);

        initData();
        initView();
        startAnimation();
    }

    private void initData() {
    }

    private void initView() {
        mFreeBallView = findViewById(R.id.logo_free_ball);
    }

    private void startAnimation() {
        mAnimatorSet = new AnimatorSet();
        ValueAnimator rotateAnimator = ValueAnimator.ofFloat(0f, 1f);
        rotateAnimator.setDuration(1200);
        rotateAnimator.setInterpolator(new LinearInterpolator());
        rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float rotate = (float) valueAnimator.getAnimatedValue();
                mFreeBallView.setRotate(rotate);
            }
        });

        ValueAnimator downTranslateAnimator = ValueAnimator.ofFloat(0f, 1f);
        downTranslateAnimator.setDuration(600);
        downTranslateAnimator.setInterpolator(new AccelerateInterpolator());
        downTranslateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float rotate = (float) valueAnimator.getAnimatedValue();
                Log.d(TAG, "onAnimationUpdate: value = " + rotate);
                mFreeBallView.setDownTranslate(rotate);
            }
        });

        ValueAnimator scaleDownValueAnimator = ValueAnimator.ofFloat(0.999f, 0.6f);
//        scaleDownValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scaleDownValueAnimator.setInterpolator(new DecelerateInterpolator());
        scaleDownValueAnimator.setDuration(600);
        scaleDownValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float temp = (float) valueAnimator.getAnimatedValue();
                mFreeBallView.setScale(temp);
            }
        });
//        scaleDownValueAnimator.start();

        ValueAnimator scaleUpValueAnimator = ValueAnimator.ofFloat(0.6f, 1f);
        scaleUpValueAnimator.setInterpolator(new DecelerateInterpolator());
        scaleUpValueAnimator.setDuration(600);
        scaleUpValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float temp = (float) valueAnimator.getAnimatedValue();
                mFreeBallView.setScale(temp);
            }
        });
//        scaleUpValueAnimator.start();

        ValueAnimator upTranslateAnimator = ValueAnimator.ofFloat(1f, 0f);
        upTranslateAnimator.setInterpolator(new DecelerateInterpolator());
        upTranslateAnimator.setDuration(1000);
        upTranslateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                final float rotate = (float) valueAnimator.getAnimatedValue();
                mFreeBallView.setUpTranslate(rotate);
            }
        });
        upTranslateAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAnimatorSet.start();
            }
        });

        mAnimatorSet.play(downTranslateAnimator).after(rotateAnimator);
        mAnimatorSet.play(scaleDownValueAnimator).after(downTranslateAnimator);
        mAnimatorSet.play(scaleUpValueAnimator).after(scaleDownValueAnimator);
        mAnimatorSet.play(upTranslateAnimator).after(scaleDownValueAnimator);
        mAnimatorSet.start();
    }
}
