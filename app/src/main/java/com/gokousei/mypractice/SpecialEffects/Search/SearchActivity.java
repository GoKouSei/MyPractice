package com.gokousei.mypractice.SpecialEffects.Search;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gokousei.mypractice.R;

public class SearchActivity extends AppCompatActivity {
    private TextView mSearchBGText;
    private TextView mHintTxt;
    private TextView mSearchTxt;
    private FrameLayout mContentFrame;
    private ImageView mArrowImg;
    private boolean finishing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        mSearchBGText = (TextView) findViewById(R.id.tv_search_bg);
        mHintTxt = (TextView) findViewById(R.id.tv_hint);
        mContentFrame = (FrameLayout) findViewById(R.id.frame_content_bg);
        mSearchTxt = (TextView) findViewById(R.id.tv_search);
        mArrowImg = (ImageView) findViewById(R.id.iv_arrow);

        mSearchBGText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                    mSearchBGText.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mSearchBGText.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                performEnterAnimation();
            }
        });

    }

    private void performEnterAnimation() {
        float originY = getIntent().getIntExtra("y", 0);

        int location[] = new int[2];
        mSearchBGText.getLocationOnScreen(location);

        final float translateY = originY - (float) location[1];

        //放到前一个页面的位置
        mSearchBGText.setY(mSearchBGText.getY() + translateY);
        mHintTxt.setY(mSearchBGText.getY() + (mSearchBGText.getHeight() - mHintTxt.getHeight()) / 2);
        mSearchTxt.setY(mSearchBGText.getY() + (mSearchBGText.getHeight() - mSearchTxt.getHeight()) / 2);
        float top = getResources().getDisplayMetrics().density * 20;
        final ValueAnimator translateVa = ValueAnimator.ofFloat(mSearchBGText.getY(), top);
        translateVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mSearchBGText.setY((Float) valueAnimator.getAnimatedValue());

                mArrowImg.setY(mSearchBGText.getY() + (mSearchBGText.getHeight() - mArrowImg.getHeight()) / 2);
                mHintTxt.setY(mSearchBGText.getY() + (mSearchBGText.getHeight() - mHintTxt.getHeight()) / 2);
                mSearchTxt.setY(mSearchBGText.getY() + (mSearchBGText.getHeight() - mSearchTxt.getHeight()) / 2);
            }
        });

        ValueAnimator scaleVa = ValueAnimator.ofFloat(1, 0.8f);
        scaleVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mSearchBGText.setScaleX((Float) valueAnimator.getAnimatedValue());
            }
        });

        ValueAnimator alphaVa = ValueAnimator.ofFloat(0, 1f);
        alphaVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mContentFrame.setAlpha((Float) valueAnimator.getAnimatedValue());
                mSearchTxt.setAlpha((Float) valueAnimator.getAnimatedValue());
                mArrowImg.setAlpha((Float) valueAnimator.getAnimatedValue());
            }
        });

        alphaVa.setDuration(500);
        translateVa.setDuration(500);
        scaleVa.setDuration(500);

        alphaVa.start();
        translateVa.start();
        scaleVa.start();

    }

    @Override
    public void onBackPressed() {
        if (!finishing) {
            finishing = true;
            performExitAnimation();
        }
    }

    private void performExitAnimation() {
        float originY = getIntent().getIntExtra("y", 0);

        int location[] = new int[2];
        mSearchBGText.getLocationOnScreen(location);

        final float translateY = originY - (float) location[1];


        final ValueAnimator translateVa = ValueAnimator.ofFloat(mSearchBGText.getY(), mSearchBGText.getY() + translateY);
        translateVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mSearchBGText.setY((Float) valueAnimator.getAnimatedValue());
                mArrowImg.setY(mSearchBGText.getY() + (mSearchBGText.getHeight() - mArrowImg.getHeight()) / 2);
                mHintTxt.setY(mSearchBGText.getY() + (mSearchBGText.getHeight() - mHintTxt.getHeight()) / 2);
                mSearchTxt.setY(mSearchBGText.getY() + (mSearchBGText.getHeight() - mSearchTxt.getHeight()) / 2);
            }
        });
        translateVa.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                finish();
                overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        ValueAnimator scaleVa = ValueAnimator.ofFloat(0.8f, 1f);
        scaleVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mSearchBGText.setScaleX((Float) valueAnimator.getAnimatedValue());
            }
        });

        ValueAnimator alphaVa = ValueAnimator.ofFloat(1, 0f);
        alphaVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mContentFrame.setAlpha((Float) valueAnimator.getAnimatedValue());

                mArrowImg.setAlpha((Float) valueAnimator.getAnimatedValue());
                mSearchTxt.setAlpha((Float) valueAnimator.getAnimatedValue());
            }
        });


        alphaVa.setDuration(500);
        translateVa.setDuration(500);
        scaleVa.setDuration(500);

        alphaVa.start();
        translateVa.start();
        scaleVa.start();

    }

}
