package com.ascendcorp.androidtechpoc.screen.fr;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.TypedValue;

import com.baoyz.widget.PullRefreshLayout;
import com.baoyz.widget.RefreshDrawable;

class CustomRingDrawable extends RefreshDrawable {
    private static final int MAX_LEVEL = 200;
    private boolean isRunning;
    private RectF mBounds;
    private int mWidth;
    private int mHeight;
    private Paint mPaint = new Paint(1);
    private Path mPath;
    private float mAngle;
    private int[] mColorSchemeColors;
    private Handler mHandler = new Handler();
    private int mLevel;
    private float mDegress;

    CustomRingDrawable(Context context, PullRefreshLayout layout) {
        super(context, layout);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth((float) this.dp2px(3));
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mPath = new Path();
    }

    public void setPercent(float percent) {
        this.mPaint.setColor(this.evaluate(percent, this.mColorSchemeColors[0], this.mColorSchemeColors[1]));
        this.mAngle = 340.0F * percent;
    }

    public void setColorSchemeColors(int[] colorSchemeColors) {
        this.mColorSchemeColors = colorSchemeColors;
    }

    public void offsetTopAndBottom(int offset) {
        this.invalidateSelf();
    }

    public void start() {
        this.mLevel = 50;
        this.isRunning = true;
        this.invalidateSelf();
    }

    private void updateLevel(int level) {
        int animationLevel = level == 200 ? 0 : level;
        int stateForLevel = animationLevel / 50;
        float percent = (float) (level % 50) / 50.0F;
        int startColor = this.mColorSchemeColors[stateForLevel];
        int endColor = this.mColorSchemeColors[(stateForLevel + 1) % this.mColorSchemeColors.length];
        this.mPaint.setColor(this.evaluate(percent, startColor, endColor));
        this.mDegress = 360.0F * percent;
    }

    public void stop() {
        this.isRunning = false;
        this.mDegress = 0.0F;
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        this.mWidth = this.getRefreshLayout().getFinalOffset() - dp2px(8);
        this.mHeight = this.mWidth;
        this.mBounds = new RectF((float) (bounds.width() / 2 - this.mWidth / 2), (float) bounds.top, (float) (bounds.width() / 2 + this.mWidth / 2), (float) (bounds.top + this.mHeight));
        this.mBounds.inset((float) this.dp2px(15), (float) this.dp2px(15));
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.rotate(this.mDegress, this.mBounds.centerX(), this.mBounds.centerY());
        this.drawRing(canvas);
        canvas.restore();
        if (this.isRunning) {
            this.mLevel = this.mLevel >= 200 ? 0 : this.mLevel + 1;
            this.updateLevel(this.mLevel);
            this.invalidateSelf();
        }

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setTextSize(48f);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Hello World!", mBounds.centerX(), this.mHeight + dp2px(6), paint);
    }

    private void drawRing(Canvas canvas) {
        this.mPath.reset();
        this.mPath.arcTo(this.mBounds, 270.0F, this.mAngle, true);
        canvas.drawPath(this.mPath, this.mPaint);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) dp, this.getContext().getResources().getDisplayMetrics());
    }

    private int evaluate(float fraction, int startValue, int endValue) {
        int startInt = startValue;
        int startA = startInt >> 24 & 255;
        int startR = startInt >> 16 & 255;
        int startG = startInt >> 8 & 255;
        int startB = startInt & 255;
        int endInt = endValue;
        int endA = endInt >> 24 & 255;
        int endR = endInt >> 16 & 255;
        int endG = endInt >> 8 & 255;
        int endB = endInt & 255;
        return startA + (int) (fraction * (float) (endA - startA)) << 24 | startR + (int) (fraction * (float) (endR - startR)) << 16 | startG + (int) (fraction * (float) (endG - startG)) << 8 | startB + (int) (fraction * (float) (endB - startB));
    }
}
