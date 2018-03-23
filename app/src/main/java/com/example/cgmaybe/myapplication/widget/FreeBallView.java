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

    private Bitmap mBallBmp;
    private Paint mPaint;
    private Matrix mMatrix;

    private int mBallRadius;
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
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FreeBallView);
        mBallRadius = typedArray.getDimensionPixelSize(R.styleable.FreeBallView_freeBallRadius, 50);
        int ballSrc = typedArray.getResourceId(R.styleable.FreeBallView_freeBallSrc, 0);

        mShadowWidth = mBallRadius;
        mShadowHeight = mBallRadius / 3;

        mShadowMaxWidth = mShadowWidth;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), ballSrc);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) mBallRadius) / width;
        float scaleHeight = ((float) mBallRadius) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        mBallBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        mPaint = new Paint();
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
//        mMatrix.setTranslate((getWidth() - mBallBmp.getWidth()) / 2, (getHeight() - mBallBmp.getWidth()));
        canvas.drawBitmap(mBallBmp, mMatrix, mPaint);
    }

    private void drawOval(Canvas canvas) {
        canvas.save();

        canvas.translate((getWidth() - mShadowWidth) / 2, getHeight() - mShadowHeight);
        Paint oval = new Paint();
        oval.setColor(Color.GRAY);
        RectF rectF = new RectF(0, 0, mShadowWidth, mShadowHeight);
        canvas.drawOval(rectF, oval);

        canvas.restore();
    }

    public void setRotate(float rotate) {
        mMatrix.setTranslate((getWidth() - mBallBmp.getWidth()) / 2, 0f);
        mMatrix.postRotate(180 * rotate, (getWidth()) / 2, mBallBmp.getWidth() / 2);
        invalidate();
    }

    public void setDownTranslate(float translateY) {
        mShadowWidth = (int) (mShadowMaxWidth * (1 - translateY));
        mShadowWidth = Math.max(mShadowWidth, 20);
        mShadowHeight = mShadowWidth / 3;

        mMatrix.setTranslate((getWidth() - mBallBmp.getWidth()) / 2, (getHeight() - mBallBmp.getWidth()) * translateY);
        invalidate();
    }

    public void setUpTranslate(float upTranslateY) {
        mShadowWidth = (int) (mShadowMaxWidth * (1 - upTranslateY));
        mShadowWidth = Math.max(mShadowWidth, 20);
        mShadowHeight = mShadowWidth / 3;

        mMatrix.setTranslate((getWidth() - mBallBmp.getWidth()) / 2, (getHeight() - mBallBmp.getWidth()) * upTranslateY);
        invalidate();
    }

    public void setScale(float scaleY) {
        mMatrix.setTranslate((getWidth() - mBallBmp.getWidth()) / 2, (getHeight() - mBallBmp.getWidth()));
        mMatrix.preScale(1, scaleY, mBallBmp.getWidth() / 2, mBallBmp.getHeight());
        invalidate();
    }
}
