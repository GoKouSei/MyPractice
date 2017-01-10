package com.gokousei.mypractice.SpecialEffects.Search;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.gokousei.mypractice.R;

public class BodyActivity extends AppCompatActivity {
private TextView mSearchBGText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body);
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        mSearchBGText= (TextView) findViewById(R.id.tv_search_bg);
    }
    public void showSearch(View view){
        Intent intent=new Intent(BodyActivity.this,SearchActivity.class);
        int location[]=new int[2];
        mSearchBGText.getLocationOnScreen(location);
        intent.putExtra("x",location[0]);
        intent.putExtra("y",location[1]);
        startActivity(intent);
        overridePendingTransition(0,0);
    }
}
