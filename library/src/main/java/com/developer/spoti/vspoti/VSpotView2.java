package com.developer.spoti.vspoti;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class VSpotView2 extends FrameLayout {

    private static final float INDICATOR_HEIGHT = 30;
    private float density;
    private List<View> targetViews;
    private List<RectF> targetRects;
    private VSpotMessageView mMessageView;
    private boolean isTop;
    private Gravity mGravity;
    private DismissType dismissType;
    int marginGuide;
    private boolean mIsShowing;
    private VSpotListener mVSpotListener;
    private int currentTargetIndex = 0;

    final int ANIMATION_DURATION = 400;
    final Paint emptyPaint = new Paint();
    final Paint paintLine = new Paint();
    final Paint paintCircle = new Paint();
    final Paint paintCircleInner = new Paint();
    final Paint mPaint = new Paint();
    final Paint targetPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    final Xfermode XFERMODE_CLEAR = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

    public interface VSpotListener {
        void onDismiss(View view);
    }

    public enum Gravity {
        auto, center
    }

    public enum DismissType {
        outside, anywhere, targetView
    }

    private VSpotView2(Context context, List<View> views) {
        super(context);
        setWillNotDraw(false);

        this.targetViews = views;
        this.targetRects = new ArrayList<>();

        density = context.getResources().getDisplayMetrics().density;

        for (View target : targetViews) {
            int[] locationTarget = new int[2];
            target.getLocationOnScreen(locationTarget);
            targetRects.add(new RectF(locationTarget[0], locationTarget[1],
                    locationTarget[0] + target.getWidth(),
                    locationTarget[1] + target.getHeight()));
        }

        mMessageView = new VSpotMessageView(getContext());
        final int padding = (int) (5 * density);
        mMessageView.setPadding(padding, padding, padding, padding);
        mMessageView.setColor(Color.WHITE);

        addView(mMessageView, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        setMessageLocation(resolveMessageViewLocation(currentTargetIndex));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!targetRects.isEmpty()) {
            Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas tempCanvas = new Canvas(bitmap);

            mPaint.setColor(0xdd000000);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setAntiAlias(true);
            tempCanvas.drawRect(canvas.getClipBounds(), mPaint);

            targetPaint.setXfermode(XFERMODE_CLEAR);
            targetPaint.setAntiAlias(true);

            for (RectF rect : targetRects) {
                tempCanvas.drawRoundRect(rect, 15, 15, targetPaint);
            }
            canvas.drawBitmap(bitmap, 0, 0, emptyPaint);
        }
    }

    public void nextTarget() {
        if (currentTargetIndex < targetViews.size() - 1) {
            currentTargetIndex++;
            setMessageLocation(resolveMessageViewLocation(currentTargetIndex));
            invalidate();
        }
    }

    public void previousTarget() {
        if (currentTargetIndex > 0) {
            currentTargetIndex--;
            setMessageLocation(resolveMessageViewLocation(currentTargetIndex));
            invalidate();
        }
    }

    public boolean isShowing() {
        return mIsShowing;
    }

    public void dismiss() {
        ((ViewGroup) ((Activity) getContext()).getWindow().getDecorView()).removeView(this);
        mIsShowing = false;
        if (mVSpotListener != null) {
            mVSpotListener.onDismiss(targetViews.get(currentTargetIndex));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (dismissType) {
                case outside:
                    if (!isViewContains(mMessageView, x, y)) dismiss();
                    break;
                case anywhere:
                    dismiss();
                    break;
                case targetView:
                    if (targetRects.get(currentTargetIndex).contains(x, y)) {
                        targetViews.get(currentTargetIndex).performClick();
                        dismiss();
                    }
                    break;
            }
            return true;
        }
        return false;
    }

    private boolean isViewContains(View view, float rx, float ry) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        int w = view.getWidth();
        int h = view.getHeight();

        return !(rx < x || rx > x + w || ry < y || ry > y + h);
    }


    private Point resolveMessageViewLocation(int index) {
        RectF rect = targetRects.get(index);
        int xMessageView = (int) (rect.right) - mMessageView.getWidth();
        int yMessageView = (int) (rect.top + targetViews.get(index).getHeight() + INDICATOR_HEIGHT * density);
        return new Point(xMessageView, yMessageView);
    }

    void setMessageLocation(Point p) {
        mMessageView.setX(p.x);
        mMessageView.setY(p.y);
        requestLayout();
    }

    public static class Builder {
        private List<View> targetViews = new ArrayList<>();
        private Context context;
        private VSpotListener vSpotListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder addTargetView(View view) {
            this.targetViews.add(view);
            return this;
        }

        public Builder setVSpotListener(VSpotListener listener) {
            this.vSpotListener = listener;
            return this;
        }

        public VSpotView2 build() {
            VSpotView2 vSpotView = new VSpotView2(context, targetViews);
            vSpotView.mVSpotListener = vSpotListener;
            return vSpotView;
        }
    }
}


