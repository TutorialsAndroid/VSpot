package com.developer.spoti.vspoti;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.text.Spannable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.drawable.GradientDrawable;

class VSpotMessageView2 extends LinearLayout {

    private TextView mTitleTextView;
    private TextView mContentTextView;
    private float density;

    VSpotMessageView2(Context context) {
        super(context);

        density = context.getResources().getDisplayMetrics().density;

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        setWillNotDraw(false);

        int padding = (int) (10 * density);
        int paddingBetween = (int) (3 * density);
        int maxWidth = (int) (280 * density);

        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.WHITE);
        background.setCornerRadius(15 * density);
        background.setStroke(1, Color.LTGRAY);
        setBackground(background);
        setPadding(padding, padding, padding, padding);

        mTitleTextView = new TextView(context);
        mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        mTitleTextView.setTextColor(Color.BLACK);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setPadding(0, 0, 0, paddingBetween);
        mTitleTextView.setMaxWidth(maxWidth);
        addView(mTitleTextView, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mContentTextView = new TextView(context);
        mContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        mContentTextView.setTextColor(Color.BLACK);
        mContentTextView.setGravity(Gravity.CENTER);
        mContentTextView.setMaxWidth(maxWidth);
        addView(mContentTextView, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            mTitleTextView.setVisibility(GONE);
        } else {
            mTitleTextView.setVisibility(VISIBLE);
            mTitleTextView.setText(title);
        }
    }

    public void setContentText(String content) {
        mContentTextView.setText(content);
    }

    public void setContentSpan(Spannable content) {
        mContentTextView.setText(content);
    }

    public void setContentTypeFace(Typeface typeFace) {
        mContentTextView.setTypeface(typeFace);
    }

    public void setTitleTypeFace(Typeface typeFace) {
        mTitleTextView.setTypeface(typeFace);
    }

    public void setTitleTextSize(int size) {
        mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setContentTextSize(int size) {
        mContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setColor(int color) {
        GradientDrawable background = (GradientDrawable) getBackground();
        background.setColor(color);
    }
}

