package com.example.cgmaybe.myapplication.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.example.cgmaybe.myapplication.R;


public class FreeBallView extends View {
    private static final String TAG = "moubiaob";

    private Context mContext;
    private Bitmap mBallBmp;
    private Paint mBallPaint, mShadowPaint;
    private Matrix mMatrix;
    private int mShadowWidth, mShadowHeight;
    private int mShadowMaxWidth;
    private AnimatorSet mAnimatorSet;

    public FreeBallView(Context context) {
        super(context);
    }

    public FreeBallView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData(context, attrs);
    }

    private void initData(Context context, AttributeSet attrs) {
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FreeBallView);
        int ballRadius = typedArray.getDimensionPixelSize(R.styleable.FreeBallView_freeBallRadius, 50);
        int ballSrc = typedArray.getResourceId(R.styleable.FreeBallView_freeBallSrc, 0);

        mShadowWidth = ballRadius;
        mShadowHeight = ballRadius / 3;

        mShadowMaxWidth = mShadowWidth;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), ballSrc);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) ballRadius) / width;
        float scaleHeight = ((float) ballRadius) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        mBallBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        mBallPaint = new Paint();
        mBallPaint.setAntiAlias(true);
        mShadowPaint = new Paint();
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setColor(Color.GRAY);
        mMatrix = new Matrix();
        mAnimatorSet = new AnimatorSet();

        typedArray.recycle();
    }

    public FreeBallView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mMatrix.setTranslate((getMeasuredWidth() - mBallBmp.getWidth()) / 2, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBall(canvas);
        drawOval(canvas);
    }

    private void drawBall(Canvas canvas) {
        canvas.drawBitmap(mBallBmp, mMatrix, mBallPaint);
    }

    private void drawOval(Canvas canvas) {
        canvas.save();
        canvas.translate((getWidth() - mShadowWidth) / 2, getHeight() - mShadowHeight);
        RectF rectF = new RectF(0, 0, mShadowWidth, mShadowHeight);
        canvas.drawOval(rectF, mShadowPaint);
        canvas.restore();
    }

    private void setRotate(float rotate) {
        mMatrix.setTranslate((getWidth() - mBallBmp.getWidth()) / 2, 0f);
        mMatrix.postRotate(360 * rotate, (getWidth()) / 2, mBallBmp.getWidth() / 2);
        invalidate();
    }

    private void setDownTranslate(float translateY) {
        mShadowWidth = (int) (mShadowMaxWidth * (1 - translateY));
        mShadowWidth = Math.max(mShadowWidth, dip2px(mContext, 8f));
        mShadowHeight = mShadowWidth / 4;

        mMatrix.setTranslate((getWidth() - mBallBmp.getWidth()) / 2,
                (getHeight() - mBallBmp.getWidth() - dip2px(mContext, 1.5f)) * translateY);
        invalidate();
    }

    private void setUpTranslate(float upTranslateY) {
        mShadowWidth = (int) (mShadowMaxWidth * (1 - upTranslateY));
        mShadowWidth = Math.max(mShadowWidth, dip2px(mContext, 8f));
        mShadowHeight = mShadowWidth / 4;

        mMatrix.setTranslate((getWidth() - mBallBmp.getWidth()) / 2,
                (getHeight() - mBallBmp.getWidth() - dip2px(mContext, 1.5f)) * upTranslateY);
        invalidate();
    }

    private void setScale(float scaleY) {
        mMatrix.setTranslate((getWidth() - mBallBmp.getWidth()) / 2,
                (getHeight() - mBallBmp.getWidth() - dip2px(mContext, 1.5f)));
        mMatrix.preScale(1, scaleY, mBallBmp.getWidth() / 2, mBallBmp.getHeight());
        invalidate();
    }

    public void startAnimation() {
        ValueAnimator downTranslateAnimator = ValueAnimator.ofFloat(0f, 1f);
        downTranslateAnimator.setDuration(600);
        downTranslateAnimator.setInterpolator(new AccelerateInterpolator());
        downTranslateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float rotate = (float) valueAnimator.getAnimatedValue();
                setDownTranslate(rotate);
            }
        });

        ValueAnimator scaleDownValueAnimator = ValueAnimator.ofFloat(0.999f, 0.6f);
        scaleDownValueAnimator.setInterpolator(new DecelerateInterpolator());
        scaleDownValueAnimator.setDuration(600);
        scaleDownValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float temp = (float) valueAnimator.getAnimatedValue();
                setScale(temp);
            }
        });

        ValueAnimator scaleUpValueAnimator = ValueAnimator.ofFloat(0.6f, 1f);
        scaleUpValueAnimator.setInterpolator(new DecelerateInterpolator());
        scaleUpValueAnimator.setDuration(600);
        scaleUpValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float temp = (float) valueAnimator.getAnimatedValue();
                setScale(temp);
            }
        });

        ValueAnimator upTranslateAnimator = ValueAnimator.ofFloat(1f, 0f);
        upTranslateAnimator.setInterpolator(new DecelerateInterpolator());
        upTranslateAnimator.setDuration(1000);
        upTranslateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                final float rotate = (float) valueAnimator.getAnimatedValue();
                setUpTranslate(rotate);
            }
        });

        ValueAnimator rotateAnimator = ValueAnimator.ofFloat(0f, 1f);
        rotateAnimator.setDuration(1200);
        rotateAnimator.setInterpolator(new LinearInterpolator());
        rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float rotate = (float) valueAnimator.getAnimatedValue();
                setRotate(rotate);
            }
        });
        rotateAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAnimatorSet.start();
            }
        });

        mAnimatorSet.play(scaleDownValueAnimator).after(downTranslateAnimator);
        mAnimatorSet.play(scaleUpValueAnimator).after(scaleDownValueAnimator);
        mAnimatorSet.play(upTranslateAnimator).after(scaleDownValueAnimator);
        mAnimatorSet.play(rotateAnimator).after(upTranslateAnimator);
        mAnimatorSet.start();
    }

    public void stopAnimation() {
        mAnimatorSet.cancel();
        reset();
    }

    private void reset() {
        mMatrix.setTranslate((getWidth() - mBallBmp.getWidth()) / 2, 0);
        invalidate();
    }

    private int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
