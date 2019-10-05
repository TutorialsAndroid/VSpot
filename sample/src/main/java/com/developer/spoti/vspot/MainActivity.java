package com.developer.spoti.vspot;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.developer.spoti.vspoti.VSpotView;

public class MainActivity extends AppCompatActivity {

    private VSpotView mVSpotView;
    private VSpotView.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final View view1 = findViewById(R.id.view1);
        final View view2 = findViewById(R.id.view2);
        final View view3 = findViewById(R.id.view3);
        final View view4 = findViewById(R.id.view4);
        final View view5 = findViewById(R.id.view5);


        builder = new VSpotView.Builder(MainActivity.this)
                .setTitle("Guide Title Text")
                .setContentText("Spoti Description Text\n .....Spoti Description Text\n .....Spoti Description Text .....")
                .setGravity(VSpotView.Gravity.center)
                .setDismissType(VSpotView.DismissType.outside)
                .setTargetView(view1)
                .setVSpotListener(view -> {
                    switch (view.getId()){
                        case R.id.view1:
                            builder.setTargetView(view2).build();
                            break;
                        case R.id.view2:
                            builder.setTargetView(view3).build();
                            break;
                        case R.id.view3:
                            builder.setTargetView(view4).build();
                            break;
                        case R.id.view4:
                            builder.setTargetView(view5).build();
                            break;
                        case R.id.view5:
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