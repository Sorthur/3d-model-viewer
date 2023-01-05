package com.example.pwta_proj2;


import android.app.usage.UsageEvents;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.dinuscxj.gesture.MultiTouchGestureDetector;

public class MultiTouchGestureView extends View {

    private final MultiTouchGestureDetector mMultiTouchGestureDetector;
    //private final Drawable mIcon;
    private Drawable mIcon;
    private Canvas can;

    private float mScaleFactor = 1.0f;
    private float mOffsetX = 0.0f;
    private float mOffsetY = 0.0f;
    private float mRotation = 0.0f;

    public MultiTouchGestureView(Context context) {
        this(context, null);
    }

    public MultiTouchGestureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mMultiTouchGestureDetector = new MultiTouchGestureDetector(context, new MultiTouchGestureDetectorListener());

        mIcon = context.getResources().getDrawable(R.mipmap.ic_launcher);
        mIcon.setBounds(0, 0, mIcon.getIntrinsicWidth(), mIcon.getIntrinsicHeight());
    }

    public int setImage(Drawable icon) {
        mIcon = icon;
        mIcon.setBounds(0, 0, mIcon.getIntrinsicWidth(), mIcon.getIntrinsicHeight());
        invalidate();

        return 1;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mMultiTouchGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        can = canvas;
        canvas.save();
        // move to center
        canvas.translate((getMeasuredWidth() - mIcon.getIntrinsicWidth()) / 2,
                (getMeasuredHeight() - mIcon.getIntrinsicHeight()) / 2);

        // transform
        canvas.save();
        canvas.translate(mOffsetX, mOffsetY);
        canvas.scale(mScaleFactor, mScaleFactor, mIcon.getIntrinsicWidth() / 2, mIcon.getIntrinsicHeight() / 2);
        canvas.rotate(mRotation, mIcon.getIntrinsicWidth() / 2, mIcon.getIntrinsicHeight() / 2);
        mIcon.draw(canvas);
        canvas.restore();

    }

    private final class MultiTouchGestureDetectorListener extends MultiTouchGestureDetector.SimpleOnMultiTouchGestureListener {

        @Override
        public void onScale(MultiTouchGestureDetector detector) {
            mScaleFactor *= detector.getScale();
            mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, 5.0f));

            invalidate();
        }

        @Override
        public void onMove(MultiTouchGestureDetector detector) {
            mOffsetX += detector.getMoveX();
            mOffsetY += detector.getMoveY();

            invalidate();
        }

        @Override
        public void onRotate(MultiTouchGestureDetector detector) {
            mRotation += detector.getRotation();

            invalidate();
        }
    }
}