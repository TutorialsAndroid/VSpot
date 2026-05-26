package com.developer.spoti.vspot;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.developer.spoti.vspoti.VSpotView;

public class MainActivity extends AppCompatActivity {

    private VSpotView vSpotTour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final View view1 = findViewById(R.id.view1);
        final View view2 = findViewById(R.id.view2);
        final View view3 = findViewById(R.id.view3);
        final View view4 = findViewById(R.id.view4);
        final View view5 = findViewById(R.id.view5);

        findViewById(R.id.btnStartTour).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVSpotTour(view1, view2, view3, view4, view5);
            }
        });

        // Start automatically after first layout pass.
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                startVSpotTour(view1, view2, view3, view4, view5);
            }
        });
    }

    private void startVSpotTour(View view1, View view2, View view3, View view4, View view5) {
        if (vSpotTour != null && vSpotTour.isShowing()) {
            vSpotTour.dismiss();
        }

        VSpotView.Style style = new VSpotView.Style();
        style.overlayColor = 0xE6000000;
        style.cardColor = Color.WHITE;
        style.accentColor = Color.parseColor("#6750A4");
        style.stepBadgeColor = Color.parseColor("#6750A4");
        style.targetStrokeColor = Color.WHITE;
        style.connectorColor = Color.WHITE;
        style.cardCornerRadiusDp = 24f;
        style.targetCornerRadiusDp = 18f;
        style.spotlightPaddingDp = 10f;
        style.maxMessageWidthDp = 360;
        style.showPulse = true;
        style.showConnector = true;
        style.showControls = true;
        style.previousButtonText = "Back";
        style.nextButtonText = "Next";
        style.doneButtonText = "Finish";
        style.skipButtonText = "Skip";

        vSpotTour = new VSpotView.Builder(this)
                .setStyle(style)
                .setGravity(VSpotView.Gravity.center)
                .setDismissType(VSpotView.DismissType.none)
                .addStep(new VSpotView.Step.Builder(view1)
                        .setTitle("Smart Text Highlight")
                        .setContentText("Highlight any TextView, Button, ImageView, or custom view with a polished overlay.")
                        .setSpotlightPaddingDp(12)
                        .build())
                .addStep(new VSpotView.Step.Builder(view2)
                        .setTitle("Image & Icon Support")
                        .setContentText("VSpot v4.0.0 works with images and dynamic layouts without manual x/y calculations.")
                        .setSpotlightShape(VSpotView.SpotlightShape.circle)
                        .setSpotlightPaddingDp(10)
                        .build())
                .addStep(new VSpotView.Step.Builder(view3)
                        .setTitle("Action Area")
                        .setContentText("Use this for CTA buttons, filters, checkout buttons, or important actions.")
                        .build())
                .addStep(new VSpotView.Step.Builder(view4)
                        .setTitle("Form Fields")
                        .setContentText("Great for onboarding users through profile forms, search boxes, and input fields.")
                        .build())
                .addStep(new VSpotView.Step.Builder(view5)
                        .setTitle("Grouped Views")
                        .setContentText("You can target an entire layout group, not only a single small child view.")
                        .build())
                .setCallback(new VSpotView.Callback() {
                    @Override
                    public void onDismiss(View lastTargetView, boolean completed, int lastStepIndex) {
                        Toast.makeText(
                                MainActivity.this,
                                completed ? "VSpot tour completed" : "VSpot tour dismissed",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .build();

        vSpotTour.show();
    }

    @Override
    protected void onDestroy() {
        if (vSpotTour != null && vSpotTour.isShowing()) {
            vSpotTour.dismiss();
        }
        super.onDestroy();
    }
}
