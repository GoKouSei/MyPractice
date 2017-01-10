package com.gokousei.mypractice.SpecialEffects.Immersive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gokousei.mypractice.R;

public class ImmersiveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immersive);
    }
    public void one(View view){
        startActivity(new Intent(this,OneActivity.class));
    }
    public void two(View view){
        startActivity(new Intent(this,TwoActivity.class));
    }
}
