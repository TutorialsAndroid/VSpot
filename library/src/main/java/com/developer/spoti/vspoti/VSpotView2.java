package com.developer.spoti.vspoti;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.view.*;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class VSpotView2 extends FrameLayout {

    private static final float INDICATOR_HEIGHT = 30;
    private float density;
    private List<View> targetViews;
    private List<RectF> targetRects;
    private VSpotMessageView mMessageView;
    private Gravity mGravity = Gravity.auto;
    private DismissType dismissType = DismissType.outside; // Default value
    private boolean mIsShowing = false;
    private VSpotListener mVSpotListener;
    private int currentTargetIndex = 0;

    private final Paint mPaint = new Paint();
    private final Paint targetPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint emptyPaint = new Paint();
    private final Xfermode XFERMODE_CLEAR = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

    private final Paint arrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path arrowPath = new Path();
    private static final float ARROW_WIDTH = 20;
    private static final float ARROW_HEIGHT = 12;

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
        this.density = context.getResources().getDisplayMetrics().density;

        // Prepare target bounds
        for (View target : targetViews) {
            int[] location = new int[2];
            target.getLocationOnScreen(location);
            targetRects.add(new RectF(location[0], location[1],
                    location[0] + target.getWidth(),
                    location[1] + target.getHeight()));
        }

        // Message view
        mMessageView = new VSpotMessageView(context);
        addView(mMessageView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        // Wait for layout to complete before positioning
        mMessageView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            setMessageLocation(resolveMessageViewLocation(currentTargetIndex));
        });
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
            for (RectF rect : targetRects) {
                tempCanvas.drawRoundRect(rect, 15 * density, 15 * density, targetPaint);
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
        if (getContext() instanceof Activity) {
            ViewGroup decor = (ViewGroup) ((Activity) getContext()).getWindow().getDecorView();
            decor.removeView(this);
            mIsShowing = false;
            if (mVSpotListener != null) {
                mVSpotListener.onDismiss(targetViews.get(currentTargetIndex));
            }
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
        int x = (int) (rect.right - mMessageView.getWidth());
        int y = (int) (rect.top + rect.height() + INDICATOR_HEIGHT * density);
        return new Point(Math.max(x, 0), y);
    }

    void setMessageLocation(Point p) {
        mMessageView.setX(p.x);
        mMessageView.setY(p.y);
        requestLayout();
    }

    public static class Builder {
        private final List<View> targetViews = new ArrayList<>();
        private final Context context;
        private VSpotListener vSpotListener;
        private DismissType dismissType = DismissType.outside;

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

        public Builder setDismissType(DismissType type) {
            this.dismissType = type;
            return this;
        }

        public VSpotView2 build() {
            VSpotView2 vSpotView = new VSpotView2(context, targetViews);
            vSpotView.mVSpotListener = vSpotListener;
            vSpotView.dismissType = dismissType;
            return vSpotView;
        }
    }
}
