package com.gokousei.mypractice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gokousei.mypractice.Explorer.ExplorerActivity;
import com.gokousei.mypractice.SpecialEffects.Immersive.ImmersiveActivity;
import com.gokousei.mypractice.SpecialEffects.Search.BodyActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showExplorer(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            startActivity(new Intent(this, ExplorerActivity.class));
        }
    }
    public void showSearchSE(View view) {
            startActivity(new Intent(this, BodyActivity.class));
    }
    public void showImmersive(View view) {
        startActivity(new Intent(this, ImmersiveActivity.class));
    }
}
