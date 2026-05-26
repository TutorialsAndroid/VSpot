package com.developer.spoti.vspoti;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * VSpotView v4.0.0
 *
 * A production-ready spotlight/onboarding overlay for Android views.
 *
 * Key upgrades:
 * - Multi-step product tour support.
 * - Safe layout tracking with listener cleanup.
 * - No bitmap allocation inside onDraw().
 * - Correct target/message touch detection using overlay-local coordinates.
 * - Custom theme, overlay, card, connector, action buttons, spotlight padding and radius.
 * - Backward-compatible single-step Builder API.
 */
public class VSpotView extends FrameLayout {

    private static final float DEFAULT_INDICATOR_GAP_DP = 30f;

    private final Activity activity;
    private final ArrayList<Step> steps;
    private final Style style;

    private final Paint overlayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint clearPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint connectorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint targetStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pulsePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final RectF rawTargetRect = new RectF();
    private final RectF spotlightRect = new RectF();

    private final int[] overlayLocation = new int[2];
    private final int[] targetLocation = new int[2];

    private final VSpotMessageView messageView;

    private Gravity gravity;
    private DismissType dismissType;
    private VSpotListener legacyListener;
    private Callback callback;

    private boolean isShowing;
    private boolean isDismissing;
    private boolean isMessageBelowTarget = true;
    private int currentIndex = 0;

    private final ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            refreshLayout();
        }
    };

    public interface VSpotListener {
        void onDismiss(View view);
    }

    /**
     * Optional advanced lifecycle callbacks.
     * Extend this class and override only the methods you need.
     */
    public static abstract class Callback {
        public void onShow(VSpotView spotView) {
        }

        public void onStepChanged(View targetView, int stepIndex, Step step) {
        }

        public void onTargetClick(View targetView, int stepIndex, Step step) {
        }

        public void onDismiss(View lastTargetView, boolean completed, int lastStepIndex) {
        }
    }

    /**
     * Kept lowercase names for old users:
     * VSpotView.Gravity.center / auto still works.
     * Uppercase names are provided for modern Java style.
     */
    public enum Gravity {
        auto,
        center,
        left,
        right,
        AUTO,
        CENTER,
        LEFT,
        RIGHT
    }

    /**
     * Kept old names for compatibility:
     * outside / anywhere / targetView still work.
     */
    public enum DismissType {
        outside,
        anywhere,
        targetView,
        none,
        OUTSIDE,
        ANYWHERE,
        TARGET_VIEW,
        NONE
    }

    public enum SpotlightShape {
        roundedRectangle,
        circle,
        ROUNDED_RECTANGLE,
        CIRCLE
    }

    private VSpotView(Builder builder) {
        super(builder.context);

        this.activity = requireActivity(builder.context);
        this.steps = new ArrayList<>(builder.resolvedSteps != null ? builder.resolvedSteps : builder.steps);
        this.style = new Style(builder.style);
        this.gravity = builder.gravity != null ? builder.gravity : Gravity.auto;
        this.dismissType = builder.dismissType != null ? builder.dismissType : DismissType.targetView;
        this.legacyListener = builder.legacyListener;
        this.callback = builder.callback;

        if (steps.isEmpty()) {
            throw new IllegalStateException("VSpotView requires at least one target step. Use setTargetView(...) or addStep(...).");
        }

        setWillNotDraw(false);
        setClickable(true);
        setFocusable(true);
        setClipChildren(false);
        setClipToPadding(false);
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        messageView = new VSpotMessageView(getContext());
        messageView.setActionListener(new VSpotMessageView.ActionListener() {
            @Override
            public void onNext() {
                next();
            }

            @Override
            public void onPrevious() {
                previous();
            }

            @Override
            public void onSkip() {
                dismiss(false);
            }
        });

        addView(messageView, new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
    }

    /**
     * Shows the overlay on the current Activity decor view.
     */
    public void show() {
        if (isShowing || isDismissing) {
            return;
        }

        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();

        if (getParent() instanceof ViewGroup) {
            ((ViewGroup) getParent()).removeView(this);
        }

        decorView.addView(this, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        isShowing = true;
        setAlpha(0f);
        setScaleX(style.startScale);
        setScaleY(style.startScale);

        post(new Runnable() {
            @Override
            public void run() {
                showStepInternal(currentIndex, false);

                animate()
                        .alpha(1f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(style.animationDuration)
                        .setInterpolator(new DecelerateInterpolator())
                        .start();

                if (callback != null) {
                    callback.onShow(VSpotView.this);
                }
            }
        });
    }

    public boolean isShowing() {
        return isShowing && getParent() != null;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int getStepCount() {
        return steps.size();
    }

    public Step getCurrentStep() {
        if (steps.isEmpty()) {
            return null;
        }
        return steps.get(currentIndex);
    }

    public View getCurrentTargetView() {
        Step step = getCurrentStep();
        return step != null ? step.targetView : null;
    }

    public List<Step> getSteps() {
        return Collections.unmodifiableList(steps);
    }

    public void next() {
        if (currentIndex < steps.size() - 1) {
            showStepInternal(currentIndex + 1, true);
        } else {
            dismiss(true);
        }
    }

    public void previous() {
        if (currentIndex > 0) {
            showStepInternal(currentIndex - 1, true);
        }
    }

    public void goToStep(int index) {
        if (index < 0 || index >= steps.size()) {
            throw new IndexOutOfBoundsException("Invalid VSpot step index: " + index);
        }
        showStepInternal(index, true);
    }

    public void finish() {
        dismiss(true);
    }

    public void dismiss() {
        dismiss(false);
    }

    private void dismiss(final boolean completed) {
        if (!isShowing || isDismissing) {
            return;
        }

        isDismissing = true;
        final View lastTarget = getCurrentTargetView();
        final int lastIndex = currentIndex;

        animate()
                .alpha(0f)
                .scaleX(style.endScale)
                .scaleY(style.endScale)
                .setDuration(style.animationDuration)
                .setInterpolator(new DecelerateInterpolator())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        ViewGroup parent = (ViewGroup) getParent();
                        if (parent != null) {
                            parent.removeView(VSpotView.this);
                        }

                        isShowing = false;
                        isDismissing = false;

                        if (legacyListener != null) {
                            legacyListener.onDismiss(lastTarget);
                        }

                        if (callback != null) {
                            callback.onDismiss(lastTarget, completed, lastIndex);
                        }
                    }
                })
                .start();
    }

    private void showStepInternal(final int index, final boolean animateStep) {
        if (index < 0 || index >= steps.size()) {
            return;
        }

        currentIndex = index;
        final Step step = steps.get(index);

        messageView.bind(
                step,
                index,
                steps.size(),
                index == 0,
                index == steps.size() - 1,
                style
        );

        refreshLayout();

        if (animateStep) {
            messageView.setAlpha(0f);
            messageView.setTranslationY(dp(isMessageBelowTarget ? 8f : -8f));
            messageView.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(Math.max(140, style.animationDuration / 2))
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        } else {
            messageView.setAlpha(1f);
            messageView.setTranslationY(0f);
        }

        CharSequence announcement = buildAccessibilityAnnouncement(step, index);
        if (!TextUtils.isEmpty(announcement)) {
            messageView.announceForAccessibility(announcement);
        }

        if (callback != null) {
            callback.onStepChanged(step.targetView, index, step);
        }
    }

    private CharSequence buildAccessibilityAnnouncement(Step step, int index) {
        StringBuilder builder = new StringBuilder();
        if (steps.size() > 1 && style.showStepIndicator) {
            builder.append("Step ").append(index + 1).append(" of ").append(steps.size()).append(". ");
        }
        if (!TextUtils.isEmpty(step.title)) {
            builder.append(step.title).append(". ");
        }
        if (!TextUtils.isEmpty(step.content)) {
            builder.append(step.content);
        }
        return builder.toString();
    }

    private void refreshLayout() {
        if (getWidth() == 0 || getHeight() == 0 || steps.isEmpty()) {
            return;
        }

        boolean targetReady = updateTargetRects();
        positionMessageView(targetReady);
        invalidate();
    }

    private boolean updateTargetRects() {
        Step step = getCurrentStep();
        View target = step != null ? step.targetView : null;

        if (target == null || !target.isShown() || target.getWidth() <= 0 || target.getHeight() <= 0) {
            rawTargetRect.setEmpty();
            spotlightRect.setEmpty();
            return false;
        }

        getLocationInWindow(overlayLocation);
        target.getLocationInWindow(targetLocation);

        float left = targetLocation[0] - overlayLocation[0];
        float top = targetLocation[1] - overlayLocation[1];

        rawTargetRect.set(left, top, left + target.getWidth(), top + target.getHeight());

        int padding = dp(step.spotlightPaddingDp >= 0 ? step.spotlightPaddingDp : style.spotlightPaddingDp);
        spotlightRect.set(rawTargetRect);
        spotlightRect.inset(-padding, -padding);

        // Keep the spotlight inside the overlay bounds to avoid drawing issues.
        spotlightRect.left = Math.max(0, spotlightRect.left);
        spotlightRect.top = Math.max(0, spotlightRect.top);
        spotlightRect.right = Math.min(getWidth(), spotlightRect.right);
        spotlightRect.bottom = Math.min(getHeight(), spotlightRect.bottom);

        return true;
    }

    private void positionMessageView(boolean targetReady) {
        Step step = getCurrentStep();

        int horizontalMargin = dp(style.screenHorizontalMarginDp);
        int verticalMargin = dp(style.screenVerticalMarginDp);
        int maxCardWidth = Math.max(1, Math.min(dp(style.maxMessageWidthDp), getWidth() - (horizontalMargin * 2)));

        messageView.setMaxTextWidth(maxCardWidth - dp(32));
        messageView.measure(
                MeasureSpec.makeMeasureSpec(maxCardWidth, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(Math.max(1, getHeight() - verticalMargin * 2), MeasureSpec.AT_MOST)
        );

        int messageWidth = Math.min(messageView.getMeasuredWidth(), maxCardWidth);
        int messageHeight = messageView.getMeasuredHeight();

        ViewGroup.LayoutParams params = messageView.getLayoutParams();
        if (params.width != messageWidth) {
            params.width = messageWidth;
            messageView.setLayoutParams(params);
        }

        float x;
        float y;

        if (!targetReady || spotlightRect.isEmpty()) {
            x = (getWidth() - messageWidth) / 2f;
            y = (getHeight() - messageHeight) / 2f;
            isMessageBelowTarget = true;
        } else {
            Gravity resolvedGravity = step.gravity != null ? step.gravity : gravity;

            if (isGravity(resolvedGravity, "left")) {
                x = spotlightRect.left;
            } else if (isGravity(resolvedGravity, "right")) {
                x = spotlightRect.right - messageWidth;
            } else {
                x = spotlightRect.centerX() - messageWidth / 2f;
            }

            float indicatorGap = dp(style.indicatorGapDp);
            float belowY = spotlightRect.bottom + indicatorGap;
            float aboveY = spotlightRect.top - indicatorGap - messageHeight;

            boolean hasSpaceBelow = belowY + messageHeight <= getHeight() - verticalMargin;
            boolean hasSpaceAbove = aboveY >= verticalMargin;

            if (hasSpaceBelow || (!hasSpaceAbove && spotlightRect.centerY() < getHeight() / 2f)) {
                y = belowY;
                isMessageBelowTarget = true;
            } else {
                y = Math.max(verticalMargin, aboveY);
                isMessageBelowTarget = false;
            }
        }

        x = clamp(x, horizontalMargin, getWidth() - messageWidth - horizontalMargin);
        y = clamp(y, verticalMargin, getHeight() - messageHeight - verticalMargin);

        messageView.setX(x);
        messageView.setY(y);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
    }

    @Override
    protected void onDetachedFromWindow() {
        ViewTreeObserver observer = getViewTreeObserver();
        if (observer.isAlive()) {
            observer.removeOnGlobalLayoutListener(globalLayoutListener);
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        overlayPaint.setColor(style.overlayColor);
        overlayPaint.setStyle(Paint.Style.FILL);

        int save = canvas.saveLayer(0, 0, getWidth(), getHeight(), null);
        canvas.drawRect(0, 0, getWidth(), getHeight(), overlayPaint);

        if (!spotlightRect.isEmpty()) {
            drawSpotlightCutout(canvas);
        }

        canvas.restoreToCount(save);

        if (!spotlightRect.isEmpty()) {
            drawSpotlightStroke(canvas);
            drawConnector(canvas);
        }
    }

    private void drawSpotlightCutout(Canvas canvas) {
        SpotlightShape shape = getCurrentStep().spotlightShape != null
                ? getCurrentStep().spotlightShape
                : style.spotlightShape;

        if (isShape(shape, "circle")) {
            float radius = Math.max(spotlightRect.width(), spotlightRect.height()) / 2f;
            canvas.drawCircle(spotlightRect.centerX(), spotlightRect.centerY(), radius, clearPaint);
        } else {
            float radius = dp(style.targetCornerRadiusDp);
            canvas.drawRoundRect(spotlightRect, radius, radius, clearPaint);
        }
    }

    private void drawSpotlightStroke(Canvas canvas) {
        if (!style.showTargetStroke) {
            return;
        }

        targetStrokePaint.setStyle(Paint.Style.STROKE);
        targetStrokePaint.setStrokeWidth(dp(style.targetStrokeWidthDp));
        targetStrokePaint.setColor(style.targetStrokeColor);

        pulsePaint.setStyle(Paint.Style.STROKE);
        pulsePaint.setStrokeWidth(dp(1.5f));
        pulsePaint.setColor(style.targetPulseColor);

        SpotlightShape shape = getCurrentStep().spotlightShape != null
                ? getCurrentStep().spotlightShape
                : style.spotlightShape;

        if (isShape(shape, "circle")) {
            float radius = Math.max(spotlightRect.width(), spotlightRect.height()) / 2f;
            canvas.drawCircle(spotlightRect.centerX(), spotlightRect.centerY(), radius, targetStrokePaint);
            if (style.showPulse) {
                canvas.drawCircle(spotlightRect.centerX(), spotlightRect.centerY(), radius + dp(5), pulsePaint);
            }
        } else {
            float radius = dp(style.targetCornerRadiusDp);
            canvas.drawRoundRect(spotlightRect, radius, radius, targetStrokePaint);
            if (style.showPulse) {
                RectF pulseRect = new RectF(spotlightRect);
                pulseRect.inset(-dp(5), -dp(5));
                canvas.drawRoundRect(pulseRect, radius + dp(5), radius + dp(5), pulsePaint);
            }
        }
    }

    private void drawConnector(Canvas canvas) {
        if (!style.showConnector || messageView.getWidth() <= 0 || messageView.getHeight() <= 0) {
            return;
        }

        float startX = spotlightRect.centerX();
        float startY = isMessageBelowTarget ? spotlightRect.bottom : spotlightRect.top;

        float messageLeft = messageView.getX();
        float messageRight = messageView.getX() + messageView.getWidth();
        float endX = clamp(startX, messageLeft + dp(24), messageRight - dp(24));
        float endY = isMessageBelowTarget ? messageView.getY() : messageView.getY() + messageView.getHeight();

        connectorPaint.setColor(style.connectorColor);
        connectorPaint.setStrokeWidth(dp(style.connectorWidthDp));
        connectorPaint.setStrokeCap(Paint.Cap.ROUND);
        connectorPaint.setStyle(Paint.Style.STROKE);

        canvas.drawLine(startX, startY, endX, endY, connectorPaint);

        connectorPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(startX, startY, dp(style.connectorDotRadiusDp), connectorPaint);
        canvas.drawCircle(endX, endY, dp(Math.max(2f, style.connectorDotRadiusDp - 1f)), connectorPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_UP) {
            return true;
        }

        float x = event.getX();
        float y = event.getY();

        if (isPointInsideView(messageView, x, y)) {
            return true;
        }

        DismissType resolvedDismissType = getResolvedDismissType();

        if (isDismissType(resolvedDismissType, "none")) {
            return true;
        }

        boolean isTargetClick = rawTargetRect.contains(x, y);

        if (isDismissType(resolvedDismissType, "targetView")) {
            if (isTargetClick) {
                View target = getCurrentTargetView();
                if (target != null) {
                    target.performClick();
                    if (callback != null) {
                        callback.onTargetClick(target, currentIndex, getCurrentStep());
                    }
                }
                dismiss(false);
            }
            return true;
        }

        if (isDismissType(resolvedDismissType, "anywhere")) {
            dismiss(false);
            return true;
        }

        if (isDismissType(resolvedDismissType, "outside")) {
            dismiss(false);
            return true;
        }

        return true;
    }

    private DismissType getResolvedDismissType() {
        Step step = getCurrentStep();
        if (step != null && step.dismissType != null) {
            return step.dismissType;
        }
        return dismissType != null ? dismissType : DismissType.targetView;
    }

    private boolean isPointInsideView(View view, float x, float y) {
        return x >= view.getX()
                && x <= view.getX() + view.getWidth()
                && y >= view.getY()
                && y <= view.getY() + view.getHeight();
    }

    public void setTitle(String title) {
        Step current = getCurrentStep();
        if (current != null) {
            current.title = title;
            messageView.bind(current, currentIndex, steps.size(), currentIndex == 0, currentIndex == steps.size() - 1, style);
            refreshLayout();
        }
    }

    public void setContentText(String contentText) {
        setContent(contentText);
    }

    public void setContentSpan(Spannable span) {
        setContent(span);
    }

    public void setContent(CharSequence content) {
        Step current = getCurrentStep();
        if (current != null) {
            current.content = content;
            messageView.bind(current, currentIndex, steps.size(), currentIndex == 0, currentIndex == steps.size() - 1, style);
            refreshLayout();
        }
    }

    public void setTitleTypeFace(Typeface typeface) {
        style.titleTypeface = typeface;
        messageView.bind(getCurrentStep(), currentIndex, steps.size(), currentIndex == 0, currentIndex == steps.size() - 1, style);
    }

    public void setContentTypeFace(Typeface typeface) {
        style.contentTypeface = typeface;
        messageView.bind(getCurrentStep(), currentIndex, steps.size(), currentIndex == 0, currentIndex == steps.size() - 1, style);
    }

    public void setTitleTextSize(int sizeSp) {
        style.titleTextSizeSp = sizeSp;
        messageView.bind(getCurrentStep(), currentIndex, steps.size(), currentIndex == 0, currentIndex == steps.size() - 1, style);
        refreshLayout();
    }

    public void setContentTextSize(int sizeSp) {
        style.contentTextSizeSp = sizeSp;
        messageView.bind(getCurrentStep(), currentIndex, steps.size(), currentIndex == 0, currentIndex == steps.size() - 1, style);
        refreshLayout();
    }

    private static Activity requireActivity(Context context) {
        Activity activity = findActivity(context);
        if (activity == null) {
            throw new IllegalStateException("VSpotView requires an Activity context.");
        }
        return activity;
    }

    private static Activity findActivity(Context context) {
        Context current = context;
        while (current instanceof ContextWrapper) {
            if (current instanceof Activity) {
                return (Activity) current;
            }
            current = ((ContextWrapper) current).getBaseContext();
        }
        return null;
    }

    private int dp(float value) {
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value,
                getResources().getDisplayMetrics()
        ));
    }

    private float clamp(float value, float min, float max) {
        if (max < min) {
            return min;
        }
        return Math.max(min, Math.min(value, max));
    }

    private boolean isGravity(Gravity value, String expected) {
        return value != null && normalizeEnum(value.name()).equals(normalizeEnum(expected));
    }

    private boolean isDismissType(DismissType value, String expected) {
        return value != null && normalizeEnum(value.name()).equals(normalizeEnum(expected));
    }

    private boolean isShape(SpotlightShape value, String expected) {
        return value != null && normalizeEnum(value.name()).equals(normalizeEnum(expected));
    }

    private String normalizeEnum(String value) {
        return value == null ? "" : value.replace("_", "").toLowerCase();
    }

    /**
     * One spotlight item in a tour.
     */
    public static class Step {
        private View targetView;
        private String title;
        private CharSequence content;
        private Gravity gravity;
        private DismissType dismissType;
        private SpotlightShape spotlightShape;
        private int spotlightPaddingDp = -1;

        private Step(Builder builder) {
            this.targetView = builder.targetView;
            this.title = builder.title;
            this.content = builder.content;
            this.gravity = builder.gravity;
            this.dismissType = builder.dismissType;
            this.spotlightShape = builder.spotlightShape;
            this.spotlightPaddingDp = builder.spotlightPaddingDp;
        }

        public View getTargetView() {
            return targetView;
        }

        public String getTitle() {
            return title;
        }

        public CharSequence getContent() {
            return content;
        }

        public Gravity getGravity() {
            return gravity;
        }

        public DismissType getDismissType() {
            return dismissType;
        }

        public SpotlightShape getSpotlightShape() {
            return spotlightShape;
        }

        public int getSpotlightPaddingDp() {
            return spotlightPaddingDp;
        }

        public static class Builder {
            private View targetView;
            private String title;
            private CharSequence content;
            private Gravity gravity;
            private DismissType dismissType;
            private SpotlightShape spotlightShape;
            private int spotlightPaddingDp = -1;

            public Builder() {
            }

            public Builder(View targetView) {
                this.targetView = targetView;
            }

            public Builder setTargetView(View targetView) {
                this.targetView = targetView;
                return this;
            }

            public Builder setTitle(String title) {
                this.title = title;
                return this;
            }

            public Builder setContentText(String content) {
                this.content = content;
                return this;
            }

            public Builder setContentSpan(Spannable content) {
                this.content = content;
                return this;
            }

            public Builder setContent(CharSequence content) {
                this.content = content;
                return this;
            }

            public Builder setGravity(Gravity gravity) {
                this.gravity = gravity;
                return this;
            }

            public Builder setDismissType(DismissType dismissType) {
                this.dismissType = dismissType;
                return this;
            }

            public Builder setSpotlightShape(SpotlightShape spotlightShape) {
                this.spotlightShape = spotlightShape;
                return this;
            }

            public Builder setSpotlightPaddingDp(int spotlightPaddingDp) {
                this.spotlightPaddingDp = spotlightPaddingDp;
                return this;
            }

            public Step build() {
                if (targetView == null) {
                    throw new IllegalStateException("VSpot step targetView cannot be null.");
                }
                return new Step(this);
            }
        }
    }

    /**
     * Visual configuration for VSpot.
     */
    public static class Style {
        public int overlayColor = 0xDD000000;
        public int cardColor = Color.WHITE;
        public int cardStrokeColor = 0x1F000000;
        public int titleColor = 0xFF111827;
        public int contentColor = 0xFF4B5563;
        public int accentColor = 0xFF6750A4;
        public int connectorColor = Color.WHITE;
        public int targetStrokeColor = Color.WHITE;
        public int targetPulseColor = 0x80FFFFFF;
        public int stepBadgeColor = 0xFF6750A4;
        public int stepBadgeTextColor = Color.WHITE;

        public float cardCornerRadiusDp = 22f;
        public float cardStrokeWidthDp = 1f;
        public float cardElevationDp = 12f;
        public float cardShadowDyDp = 4f;

        public float targetCornerRadiusDp = 16f;
        public float targetStrokeWidthDp = 2f;
        public float spotlightPaddingDp = 8f;
        public SpotlightShape spotlightShape = SpotlightShape.roundedRectangle;

        public float connectorWidthDp = 2f;
        public float connectorDotRadiusDp = 4f;
        public float indicatorGapDp = DEFAULT_INDICATOR_GAP_DP;

        public int screenHorizontalMarginDp = 16;
        public int screenVerticalMarginDp = 24;
        public int maxMessageWidthDp = 340;

        public int titleTextSizeSp = 18;
        public int contentTextSizeSp = 14;
        public int buttonTextSizeSp = 14;
        public int stepTextSizeSp = 12;

        public Typeface titleTypeface = Typeface.DEFAULT_BOLD;
        public Typeface contentTypeface = Typeface.DEFAULT;

        public boolean showControls = true;
        public boolean showPreviousButton = true;
        public boolean showSkipButton = true;
        public boolean showStepIndicator = true;
        public boolean showConnector = true;
        public boolean showTargetStroke = true;
        public boolean showPulse = true;

        public String previousButtonText = "Back";
        public String nextButtonText = "Next";
        public String doneButtonText = "Done";
        public String skipButtonText = "Skip";

        public long animationDuration = 260L;
        public float startScale = 0.98f;
        public float endScale = 0.98f;

        public Style() {
        }

        public Style(Style other) {
            this.overlayColor = other.overlayColor;
            this.cardColor = other.cardColor;
            this.cardStrokeColor = other.cardStrokeColor;
            this.titleColor = other.titleColor;
            this.contentColor = other.contentColor;
            this.accentColor = other.accentColor;
            this.connectorColor = other.connectorColor;
            this.targetStrokeColor = other.targetStrokeColor;
            this.targetPulseColor = other.targetPulseColor;
            this.stepBadgeColor = other.stepBadgeColor;
            this.stepBadgeTextColor = other.stepBadgeTextColor;

            this.cardCornerRadiusDp = other.cardCornerRadiusDp;
            this.cardStrokeWidthDp = other.cardStrokeWidthDp;
            this.cardElevationDp = other.cardElevationDp;
            this.cardShadowDyDp = other.cardShadowDyDp;

            this.targetCornerRadiusDp = other.targetCornerRadiusDp;
            this.targetStrokeWidthDp = other.targetStrokeWidthDp;
            this.spotlightPaddingDp = other.spotlightPaddingDp;
            this.spotlightShape = other.spotlightShape;

            this.connectorWidthDp = other.connectorWidthDp;
            this.connectorDotRadiusDp = other.connectorDotRadiusDp;
            this.indicatorGapDp = other.indicatorGapDp;

            this.screenHorizontalMarginDp = other.screenHorizontalMarginDp;
            this.screenVerticalMarginDp = other.screenVerticalMarginDp;
            this.maxMessageWidthDp = other.maxMessageWidthDp;

            this.titleTextSizeSp = other.titleTextSizeSp;
            this.contentTextSizeSp = other.contentTextSizeSp;
            this.buttonTextSizeSp = other.buttonTextSizeSp;
            this.stepTextSizeSp = other.stepTextSizeSp;

            this.titleTypeface = other.titleTypeface;
            this.contentTypeface = other.contentTypeface;

            this.showControls = other.showControls;
            this.showPreviousButton = other.showPreviousButton;
            this.showSkipButton = other.showSkipButton;
            this.showStepIndicator = other.showStepIndicator;
            this.showConnector = other.showConnector;
            this.showTargetStroke = other.showTargetStroke;
            this.showPulse = other.showPulse;

            this.previousButtonText = other.previousButtonText;
            this.nextButtonText = other.nextButtonText;
            this.doneButtonText = other.doneButtonText;
            this.skipButtonText = other.skipButtonText;

            this.animationDuration = other.animationDuration;
            this.startScale = other.startScale;
            this.endScale = other.endScale;
        }
    }

    public static class Builder {
        private final Context context;
        private final ArrayList<Step> steps = new ArrayList<>();
        private ArrayList<Step> resolvedSteps;
        private final Style style = new Style();

        private View targetView;
        private String title;
        private CharSequence contentText;
        private Gravity gravity;
        private DismissType dismissType;
        private VSpotListener legacyListener;
        private Callback callback;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTargetView(View view) {
            this.targetView = view;
            return this;
        }

        public Builder addStep(View targetView, String title, String contentText) {
            return addStep(new Step.Builder(targetView)
                    .setTitle(title)
                    .setContentText(contentText)
                    .build());
        }

        public Builder addStep(View targetView, String title, CharSequence content) {
            return addStep(new Step.Builder(targetView)
                    .setTitle(title)
                    .setContent(content)
                    .build());
        }

        public Builder addStep(Step step) {
            if (step == null) {
                throw new IllegalArgumentException("VSpot step cannot be null.");
            }
            this.steps.add(step);
            return this;
        }

        public Builder setGravity(Gravity gravity) {
            this.gravity = gravity;
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

        public Builder setContentSpan(Spannable span) {
            this.contentText = span;
            return this;
        }

        public Builder setContent(CharSequence content) {
            this.contentText = content;
            return this;
        }

        public Builder setContentTypeFace(Typeface typeFace) {
            this.style.contentTypeface = typeFace;
            return this;
        }

        public Builder setTitleTypeFace(Typeface typeFace) {
            this.style.titleTypeface = typeFace;
            return this;
        }

        public Builder setContentTextSize(int sizeSp) {
            this.style.contentTextSizeSp = sizeSp;
            return this;
        }

        public Builder setTitleTextSize(int sizeSp) {
            this.style.titleTextSizeSp = sizeSp;
            return this;
        }

        public Builder setDismissType(DismissType dismissType) {
            this.dismissType = dismissType;
            return this;
        }

        public Builder setVSpotListener(VSpotListener listener) {
            this.legacyListener = listener;
            return this;
        }

        public Builder setCallback(Callback callback) {
            this.callback = callback;
            return this;
        }

        public Builder setStyle(Style style) {
            if (style != null) {
                copyStyle(style, this.style);
            }
            return this;
        }

        public Builder setOverlayColor(int color) {
            this.style.overlayColor = color;
            return this;
        }

        public Builder setCardColor(int color) {
            this.style.cardColor = color;
            return this;
        }

        public Builder setTitleColor(int color) {
            this.style.titleColor = color;
            return this;
        }

        public Builder setContentColor(int color) {
            this.style.contentColor = color;
            return this;
        }

        public Builder setAccentColor(int color) {
            this.style.accentColor = color;
            this.style.stepBadgeColor = color;
            return this;
        }

        public Builder setConnectorColor(int color) {
            this.style.connectorColor = color;
            return this;
        }

        public Builder setTargetStrokeColor(int color) {
            this.style.targetStrokeColor = color;
            return this;
        }

        public Builder setCardCornerRadiusDp(float radiusDp) {
            this.style.cardCornerRadiusDp = radiusDp;
            return this;
        }

        public Builder setTargetCornerRadiusDp(float radiusDp) {
            this.style.targetCornerRadiusDp = radiusDp;
            return this;
        }

        public Builder setSpotlightPaddingDp(float paddingDp) {
            this.style.spotlightPaddingDp = paddingDp;
            return this;
        }

        public Builder setSpotlightShape(SpotlightShape shape) {
            this.style.spotlightShape = shape;
            return this;
        }

        public Builder setMaxMessageWidthDp(int widthDp) {
            this.style.maxMessageWidthDp = widthDp;
            return this;
        }

        public Builder setShowControls(boolean showControls) {
            this.style.showControls = showControls;
            return this;
        }

        public Builder setShowConnector(boolean showConnector) {
            this.style.showConnector = showConnector;
            return this;
        }

        public Builder setShowStepIndicator(boolean showStepIndicator) {
            this.style.showStepIndicator = showStepIndicator;
            return this;
        }

        public Builder setShowSkipButton(boolean showSkipButton) {
            this.style.showSkipButton = showSkipButton;
            return this;
        }

        public Builder setShowPreviousButton(boolean showPreviousButton) {
            this.style.showPreviousButton = showPreviousButton;
            return this;
        }

        public Builder setButtonTexts(String previous, String next, String done, String skip) {
            if (previous != null) {
                this.style.previousButtonText = previous;
            }
            if (next != null) {
                this.style.nextButtonText = next;
            }
            if (done != null) {
                this.style.doneButtonText = done;
            }
            if (skip != null) {
                this.style.skipButtonText = skip;
            }
            return this;
        }

        public Builder setAnimationDuration(long durationMillis) {
            this.style.animationDuration = Math.max(0L, durationMillis);
            return this;
        }

        public VSpotView build() {
            resolvedSteps = new ArrayList<>();

            if (steps.isEmpty()) {
                if (targetView == null) {
                    throw new IllegalStateException("VSpotView targetView is required. Call setTargetView(...) or addStep(...).");
                }
                resolvedSteps.add(new Step.Builder(targetView)
                        .setTitle(title)
                        .setContent(contentText)
                        .build());
            } else {
                resolvedSteps.addAll(steps);
            }

            return new VSpotView(this);
        }

        public VSpotView show() {
            VSpotView view = build();
            view.show();
            return view;
        }

        private static void copyStyle(Style from, Style to) {
            Style copy = new Style(from);
            to.overlayColor = copy.overlayColor;
            to.cardColor = copy.cardColor;
            to.cardStrokeColor = copy.cardStrokeColor;
            to.titleColor = copy.titleColor;
            to.contentColor = copy.contentColor;
            to.accentColor = copy.accentColor;
            to.connectorColor = copy.connectorColor;
            to.targetStrokeColor = copy.targetStrokeColor;
            to.targetPulseColor = copy.targetPulseColor;
            to.stepBadgeColor = copy.stepBadgeColor;
            to.stepBadgeTextColor = copy.stepBadgeTextColor;

            to.cardCornerRadiusDp = copy.cardCornerRadiusDp;
            to.cardStrokeWidthDp = copy.cardStrokeWidthDp;
            to.cardElevationDp = copy.cardElevationDp;
            to.cardShadowDyDp = copy.cardShadowDyDp;

            to.targetCornerRadiusDp = copy.targetCornerRadiusDp;
            to.targetStrokeWidthDp = copy.targetStrokeWidthDp;
            to.spotlightPaddingDp = copy.spotlightPaddingDp;
            to.spotlightShape = copy.spotlightShape;

            to.connectorWidthDp = copy.connectorWidthDp;
            to.connectorDotRadiusDp = copy.connectorDotRadiusDp;
            to.indicatorGapDp = copy.indicatorGapDp;

            to.screenHorizontalMarginDp = copy.screenHorizontalMarginDp;
            to.screenVerticalMarginDp = copy.screenVerticalMarginDp;
            to.maxMessageWidthDp = copy.maxMessageWidthDp;

            to.titleTextSizeSp = copy.titleTextSizeSp;
            to.contentTextSizeSp = copy.contentTextSizeSp;
            to.buttonTextSizeSp = copy.buttonTextSizeSp;
            to.stepTextSizeSp = copy.stepTextSizeSp;

            to.titleTypeface = copy.titleTypeface;
            to.contentTypeface = copy.contentTypeface;

            to.showControls = copy.showControls;
            to.showPreviousButton = copy.showPreviousButton;
            to.showSkipButton = copy.showSkipButton;
            to.showStepIndicator = copy.showStepIndicator;
            to.showConnector = copy.showConnector;
            to.showTargetStroke = copy.showTargetStroke;
            to.showPulse = copy.showPulse;

            to.previousButtonText = copy.previousButtonText;
            to.nextButtonText = copy.nextButtonText;
            to.doneButtonText = copy.doneButtonText;
            to.skipButtonText = copy.skipButtonText;

            to.animationDuration = copy.animationDuration;
            to.startScale = copy.startScale;
            to.endScale = copy.endScale;
        }
    }
}
