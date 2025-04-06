package com.developer.spoti.vspoti;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.view.*;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class VSpotView2 extends FrameLayout {

    private static final float INDICATOR_HEIGHT = 30;
    private float density;
    private List<View> targetViews;
    private List<RectF> targetRects;
    private VSpotMessageView2 mMessageView;
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

    final int ANIMATION_DURATION = 400;

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
        mMessageView = new VSpotMessageView2(context);
        addView(mMessageView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        // Wait for layout to complete before positioning
        mMessageView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            setMessageLocation(resolveMessageViewLocation(currentTargetIndex));
        });
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        if (!targetRects.isEmpty()) {
//            Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
//            Canvas tempCanvas = new Canvas(bitmap);
//
//            mPaint.setColor(0xdd000000);
//            mPaint.setStyle(Paint.Style.FILL);
//            mPaint.setAntiAlias(true);
//            tempCanvas.drawRect(canvas.getClipBounds(), mPaint);
//
//            targetPaint.setXfermode(XFERMODE_CLEAR);
//            for (RectF rect : targetRects) {
//                tempCanvas.drawRoundRect(rect, 15 * density, 15 * density, targetPaint);
//            }
//
//            canvas.drawBitmap(bitmap, 0, 0, emptyPaint);
//        }
//    }

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
                tempCanvas.drawRoundRect(rect, 15 * density, 15 * density, targetPaint);
            }

            canvas.drawBitmap(bitmap, 0, 0, emptyPaint);

            // Draw pointer/arrow to current target
            drawArrowToTarget(canvas);
        }
    }

    private void drawArrowToTarget(Canvas canvas) {
        RectF target = targetRects.get(currentTargetIndex);
        float targetCenterX = target.centerX();
        float targetTopY = target.top;
        float targetBottomY = target.bottom;

        float messageX = mMessageView.getX();
        float messageY = mMessageView.getY();
        float messageWidth = mMessageView.getWidth();
        float messageHeight = mMessageView.getHeight();

        float arrowCenterX = Math.max(messageX, Math.min(targetCenterX, messageX + messageWidth));
        float arrowTipX = arrowCenterX;
        float arrowTipY;
        boolean drawArrowDownward;

        if (messageY > targetBottomY) {
            // Message is below target – arrow points up
            arrowTipY = messageY;
            drawArrowDownward = false;
        } else {
            // Message is above target – arrow points down
            arrowTipY = messageY + messageHeight;
            drawArrowDownward = true;
        }

        float halfWidth = ARROW_WIDTH / 2f;
        float height = ARROW_HEIGHT;

        arrowPath.reset();

        if (drawArrowDownward) {
            // Pointing downward
            arrowPath.moveTo(arrowTipX, arrowTipY);
            arrowPath.lineTo(arrowTipX - halfWidth, arrowTipY - height);
            arrowPath.lineTo(arrowTipX + halfWidth, arrowTipY - height);
        } else {
            // Pointing upward
            arrowPath.moveTo(arrowTipX, arrowTipY);
            arrowPath.lineTo(arrowTipX - halfWidth, arrowTipY + height);
            arrowPath.lineTo(arrowTipX + halfWidth, arrowTipY + height);
        }

        arrowPath.close();

        arrowPaint.setColor(Color.WHITE); // same as message background
        arrowPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(arrowPath, arrowPaint);
    }

    private void showMessageWithAnimation() {
        mMessageView.setAlpha(0f);
        mMessageView.setScaleX(0.8f);
        mMessageView.setScaleY(0.8f);

        mMessageView.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(300)
                .setInterpolator(new android.view.animation.DecelerateInterpolator())
                .start();
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

    public void show() {
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        this.setClickable(false);

        ((ViewGroup) ((Activity) getContext()).getWindow().getDecorView()).addView(this);
        AlphaAnimation startAnimation = new AlphaAnimation(0.0f, 1.0f);
        startAnimation.setDuration(ANIMATION_DURATION);
        startAnimation.setFillAfter(true);
        this.startAnimation(startAnimation);
        mIsShowing = true;
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

        showMessageWithAnimation();
    }

    public static class Builder {
        private final List<View> targetViews = new ArrayList<>();
        private final Context context;
        private String title, contentText;
        private Gravity gravity;
        private VSpotListener vSpotListener;
        private DismissType dismissType = DismissType.outside;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder addTargetView(View view) {
            this.targetViews.add(view);
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentText(String contentText) {
            this.contentText = contentText;
            return this;
        }

        public Builder setGravity(Gravity gravity) {
            this.gravity = gravity;
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
