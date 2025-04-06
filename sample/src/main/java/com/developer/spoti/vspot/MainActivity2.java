package com.developer.spoti.vspot;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.developer.spoti.vspoti.VSpotView2;

public class MainActivity2 extends AppCompatActivity {

    private VSpotView2 mVSpotView;
    private VSpotView2.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final View view1 = findViewById(R.id.view1);
        final View view2 = findViewById(R.id.view2);
        final View view3 = findViewById(R.id.view3);
        final View view4 = findViewById(R.id.view4);
        final View view5 = findViewById(R.id.view5);

        builder = new VSpotView2.Builder(MainActivity2.this)
                .setTitle("Guide Title Text")
                .setContentText("Spoti Description Text\n .....Spoti Description Text\n .....Spoti Description Text .....")

                .setGravity(VSpotView2.Gravity.center)
                .setDismissType(VSpotView2.DismissType.outside)
                .addTargetView(view1)
                .setVSpotListener(view -> {
                    int id = view.getId();
                    if (id == R.id.view1) {
                        builder.addTargetView(view2).build();
                    } else if (id == R.id.view2) {
                        builder.addTargetView(view3).build();
                    } else if (id == R.id.view3) {
                        builder.addTargetView(view4).build();
                    } else if (id == R.id.view4) {
                        builder.addTargetView(view5).build();
                    } else if (id == R.id.view5) {
                        return;
                    }
                    mVSpotView = builder.build();
                    mVSpotView.show();
                });

        mVSpotView = builder.build();
        mVSpotView.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
