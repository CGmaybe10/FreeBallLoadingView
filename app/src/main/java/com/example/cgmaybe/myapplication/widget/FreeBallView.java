package com.example.cgmaybe.myapplication.widget;

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

import com.example.cgmaybe.myapplication.R;


public class FreeBallView extends View {
    private static final String TAG = "moubiaob";

    private Context mContext;
    private Bitmap mBallBmp;
    private Paint mBallPaint, mShadowPaint;
    private Matrix mMatrix;
    private int mShadowWidth, mShadowHeight;
    private int mShadowMaxWidth;

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
        mMatrix.setTranslate((getWidth() - mBallBmp.getWidth()) / 2, 0);

        typedArray.recycle();
    }

    public FreeBallView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

    public void setRotate(float rotate) {
        mMatrix.setTranslate((getWidth() - mBallBmp.getWidth()) / 2, 0f);
        mMatrix.postRotate(360 * rotate, (getWidth()) / 2, mBallBmp.getWidth() / 2);
        invalidate();
    }

    public void setDownTranslate(float translateY) {
        mShadowWidth = (int) (mShadowMaxWidth * (1 - translateY));
        mShadowWidth = Math.max(mShadowWidth, dip2px(mContext, 8f));
        mShadowHeight = mShadowWidth / 4;

        mMatrix.setTranslate((getWidth() - mBallBmp.getWidth()) / 2,
                (getHeight() - mBallBmp.getWidth() - dip2px(mContext, 1.5f)) * translateY);
        invalidate();
    }

    public void setUpTranslate(float upTranslateY) {
        mShadowWidth = (int) (mShadowMaxWidth * (1 - upTranslateY));
        mShadowWidth = Math.max(mShadowWidth, dip2px(mContext, 8f));
        mShadowHeight = mShadowWidth / 4;

        mMatrix.setTranslate((getWidth() - mBallBmp.getWidth()) / 2,
                (getHeight() - mBallBmp.getWidth() - dip2px(mContext, 1.5f)) * upTranslateY);
        invalidate();
    }

    public void setScale(float scaleY) {
        mMatrix.setTranslate((getWidth() - mBallBmp.getWidth()) / 2,
                (getHeight() - mBallBmp.getWidth() - dip2px(mContext, 1.5f)));
        mMatrix.preScale(1, scaleY, mBallBmp.getWidth() / 2, mBallBmp.getHeight());
        invalidate();
    }

    private int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
