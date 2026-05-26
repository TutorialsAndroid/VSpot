package com.developer.spoti.vspoti;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Internal card view used by VSpotView v4.0.0.
 *
 * This class intentionally has package visibility because the public API should stay
 * focused on VSpotView.Builder, VSpotView.Step and VSpotView.Style.
 */
class VSpotMessageView extends LinearLayout {

    interface ActionListener {
        void onNext();

        void onPrevious();

        void onSkip();
    }

    private final Paint cardPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF cardRect = new RectF();

    private final TextView stepTextView;
    private final TextView titleTextView;
    private final TextView contentTextView;
    private final LinearLayout actionRow;
    private final TextView previousButton;
    private final TextView skipButton;
    private final TextView nextButton;

    private ActionListener actionListener;
    private VSpotView.Style style;
    private int maxTextWidthPx = Integer.MAX_VALUE;

    VSpotMessageView(Context context) {
        super(context);

        setWillNotDraw(false);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        setClickable(true);
        setFocusable(true);
        setClipToPadding(false);
        setClipChildren(false);
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        stepTextView = new TextView(context);
        stepTextView.setGravity(Gravity.CENTER);
        stepTextView.setSingleLine(true);
        stepTextView.setEllipsize(TextUtils.TruncateAt.END);
        addView(stepTextView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        titleTextView = new TextView(context);
        titleTextView.setGravity(Gravity.CENTER);
        titleTextView.setIncludeFontPadding(true);
        titleTextView.setMaxLines(3);
        titleTextView.setEllipsize(TextUtils.TruncateAt.END);
        addView(titleTextView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        contentTextView = new TextView(context);
        contentTextView.setGravity(Gravity.CENTER);
        contentTextView.setIncludeFontPadding(true);
        contentTextView.setLineSpacing(dp(2), 1.05f);
        addView(contentTextView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        actionRow = new LinearLayout(context);
        actionRow.setOrientation(HORIZONTAL);
        actionRow.setGravity(Gravity.CENTER);
        addView(actionRow, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        previousButton = createActionButton(context);
        skipButton = createActionButton(context);
        nextButton = createActionButton(context);

        actionRow.addView(previousButton);
        actionRow.addView(skipButton);
        actionRow.addView(nextButton);

        previousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionListener != null) {
                    actionListener.onPrevious();
                }
            }
        });

        skipButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionListener != null) {
                    actionListener.onSkip();
                }
            }
        });

        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionListener != null) {
                    actionListener.onNext();
                }
            }
        });
    }

    void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    void setMaxTextWidth(int maxTextWidthPx) {
        this.maxTextWidthPx = Math.max(dp(160), maxTextWidthPx);
        titleTextView.setMaxWidth(this.maxTextWidthPx);
        contentTextView.setMaxWidth(this.maxTextWidthPx);
    }

    void bind(VSpotView.Step step,
              int stepIndex,
              int totalSteps,
              boolean isFirstStep,
              boolean isLastStep,
              VSpotView.Style style) {

        if (step == null || style == null) {
            return;
        }

        this.style = style;

        int shadowInset = dp(Math.max(6f, style.cardElevationDp));
        int horizontalPadding = dp(18);
        int topPadding = dp(16);
        int bottomPadding = dp(style.showControls ? 14 : 18);

        setPadding(
                shadowInset + horizontalPadding,
                shadowInset + topPadding,
                shadowInset + horizontalPadding,
                shadowInset + bottomPadding
        );

        cardPaint.setColor(style.cardColor);
        cardPaint.setStyle(Paint.Style.FILL);
        cardPaint.setShadowLayer(
                dp(style.cardElevationDp),
                0,
                dp(style.cardShadowDyDp),
                0x55000000
        );

        bindStepIndicator(stepIndex, totalSteps, style);
        bindTitle(step.getTitle(), style);
        bindContent(step.getContent(), style);
        bindActions(totalSteps, isFirstStep, isLastStep, style);

        setContentDescription(buildContentDescription(step, stepIndex, totalSteps, style));
        invalidate();
        requestLayout();
    }

    private void bindStepIndicator(int stepIndex, int totalSteps, VSpotView.Style style) {
        if (!style.showStepIndicator || totalSteps <= 1) {
            stepTextView.setVisibility(GONE);
            return;
        }

        stepTextView.setVisibility(VISIBLE);
        stepTextView.setText((stepIndex + 1) + " / " + totalSteps);
        stepTextView.setTextColor(style.stepBadgeTextColor);
        stepTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, style.stepTextSizeSp);
        stepTextView.setTypeface(Typeface.DEFAULT_BOLD);
        stepTextView.setPadding(dp(10), dp(4), dp(10), dp(4));
        stepTextView.setBackground(createRoundedDrawable(style.stepBadgeColor, 0x00000000, 0, dp(999)));

        LayoutParams params = (LayoutParams) stepTextView.getLayoutParams();
        params.bottomMargin = dp(8);
        stepTextView.setLayoutParams(params);
    }

    private void bindTitle(String title, VSpotView.Style style) {
        if (TextUtils.isEmpty(title)) {
            titleTextView.setVisibility(GONE);
            return;
        }

        titleTextView.setVisibility(VISIBLE);
        titleTextView.setText(title);
        titleTextView.setTextColor(style.titleColor);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, style.titleTextSizeSp);
        titleTextView.setTypeface(style.titleTypeface != null ? style.titleTypeface : Typeface.DEFAULT_BOLD);
        titleTextView.setMaxWidth(maxTextWidthPx);

        LayoutParams params = (LayoutParams) titleTextView.getLayoutParams();
        params.bottomMargin = dp(5);
        titleTextView.setLayoutParams(params);
    }

    private void bindContent(CharSequence content, VSpotView.Style style) {
        if (TextUtils.isEmpty(content)) {
            contentTextView.setVisibility(GONE);
            return;
        }

        contentTextView.setVisibility(VISIBLE);
        contentTextView.setText(content);
        contentTextView.setTextColor(style.contentColor);
        contentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, style.contentTextSizeSp);
        contentTextView.setTypeface(style.contentTypeface != null ? style.contentTypeface : Typeface.DEFAULT);
        contentTextView.setMaxWidth(maxTextWidthPx);

        LayoutParams params = (LayoutParams) contentTextView.getLayoutParams();
        params.bottomMargin = style.showControls ? dp(14) : 0;
        contentTextView.setLayoutParams(params);
    }

    private void bindActions(int totalSteps, boolean isFirstStep, boolean isLastStep, VSpotView.Style style) {
        if (!style.showControls) {
            actionRow.setVisibility(GONE);
            return;
        }

        actionRow.setVisibility(VISIBLE);

        previousButton.setText(style.previousButtonText);
        skipButton.setText(style.skipButtonText);
        nextButton.setText(isLastStep ? style.doneButtonText : style.nextButtonText);

        styleActionButton(previousButton, false, style);
        styleActionButton(skipButton, false, style);
        styleActionButton(nextButton, true, style);

        previousButton.setVisibility(style.showPreviousButton && !isFirstStep ? VISIBLE : GONE);
        skipButton.setVisibility(style.showSkipButton && totalSteps > 1 && !isLastStep ? VISIBLE : GONE);
        nextButton.setVisibility(VISIBLE);

        LayoutParams rowParams = (LayoutParams) actionRow.getLayoutParams();
        rowParams.topMargin = dp(2);
        actionRow.setLayoutParams(rowParams);
    }

    private TextView createActionButton(Context context) {
        TextView button = new TextView(context);
        button.setGravity(Gravity.CENTER);
        button.setSingleLine(true);
        button.setClickable(true);
        button.setFocusable(true);
        button.setMinWidth(dp(72));
        button.setPadding(dp(12), dp(8), dp(12), dp(8));

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.leftMargin = dp(4);
        params.rightMargin = dp(4);
        button.setLayoutParams(params);

        return button;
    }

    private void styleActionButton(TextView button, boolean filled, VSpotView.Style style) {
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, style.buttonTextSizeSp);
        button.setTypeface(Typeface.DEFAULT_BOLD);
        button.setTextColor(filled ? style.cardColor : style.accentColor);
        button.setBackground(createRoundedDrawable(
                filled ? style.accentColor : 0x00000000,
                style.accentColor,
                dp(1),
                dp(999)
        ));
    }

    private CharSequence buildContentDescription(VSpotView.Step step, int stepIndex, int totalSteps, VSpotView.Style style) {
        StringBuilder builder = new StringBuilder();
        if (style.showStepIndicator && totalSteps > 1) {
            builder.append("Step ").append(stepIndex + 1).append(" of ").append(totalSteps).append(". ");
        }
        if (!TextUtils.isEmpty(step.getTitle())) {
            builder.append(step.getTitle()).append(". ");
        }
        if (!TextUtils.isEmpty(step.getContent())) {
            builder.append(step.getContent());
        }
        return builder.toString();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int shadowInset = style != null ? dp(Math.max(6f, style.cardElevationDp)) : dp(8);

        cardRect.set(
                shadowInset,
                shadowInset,
                getWidth() - shadowInset,
                getHeight() - shadowInset
        );

        float radius = style != null ? dp(style.cardCornerRadiusDp) : dp(18);
        canvas.drawRoundRect(cardRect, radius, radius, cardPaint);

        if (style != null && style.cardStrokeWidthDp > 0) {
            Paint strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            strokePaint.setStyle(Paint.Style.STROKE);
            strokePaint.setStrokeWidth(dp(style.cardStrokeWidthDp));
            strokePaint.setColor(style.cardStrokeColor);
            canvas.drawRoundRect(cardRect, radius, radius, strokePaint);
        }

        super.onDraw(canvas);
    }

    private GradientDrawable createRoundedDrawable(int fillColor, int strokeColor, int strokeWidth, int radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(fillColor);
        drawable.setCornerRadius(radius);
        if (strokeWidth > 0) {
            drawable.setStroke(strokeWidth, strokeColor);
        }
        return drawable;
    }

    private int dp(float value) {
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value,
                getResources().getDisplayMetrics()
        ));
    }
}
