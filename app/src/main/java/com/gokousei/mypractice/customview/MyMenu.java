package com.gokousei.mypractice.customview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Created by z on 2016/11/3.
 */

public class MyMenu extends PopupWindow {
    private LinearLayout mLayout;
    private ImageView mImageView;
    private TextView mTextView;
    public MyMenu(Context context, View.OnClickListener onClickListener, int resID, String text, int fontSize,
                  int fontColor, int colorBgTabMenu, int aniTabMenu){
        super(context);

        mLayout=new LinearLayout(context);
        mLayout.setOrientation(LinearLayout.VERTICAL);
        mLayout.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        mLayout.setPadding(9, 9, 9, 9);

        mTextView = new TextView(context);
        mTextView.setTextSize(fontSize);
//        mTextView.setTextColor((context.getResources().getColor(fontColor)));
        mTextView.setTextColor((ContextCompat.getColor(context, android.R.color.black)));
        mTextView.setText(text);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setPadding(5, 5, 5, 5);

        mImageView=new ImageView(context);
        mImageView.setBackgroundResource(resID);

        mLayout.addView(mImageView,new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)));
        mLayout.addView(mTextView);
        mLayout.setOnClickListener(onClickListener);

        this.setContentView(mLayout);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, android.R.color.darker_gray)));
        this.setAnimationStyle(aniTabMenu);
        this.setFocusable(true);
    }

}
