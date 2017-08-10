package com.lvruheng.floatingdragbutton;

import android.graphics.Rect;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout mFloatBtnWrapper;
    private AbsoluteLayout.LayoutParams mFloatBtnWindowParams;
    private AbsoluteLayout mFloatRootView;
    private ConstraintLayout mMainLayout;
    private FloatTouchListener mFloatTouchListener;
    private Rect mFloatViewBoundsInScreens;
    private int mEdgePadding;
    private ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainLayout = (ConstraintLayout) findViewById(R.id.main_layout);
        addFloatBtn();
        setTouchListener();
    }

    /**
     * 添加浮动按钮
     */
    private void addFloatBtn() {
        mFloatBtnWrapper = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.float_btn,null,false);
        mImageView = (ImageView) mFloatBtnWrapper.findViewById(R.id.iv_shine);
        mFloatBtnWindowParams = new AbsoluteLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0, 0);
        mFloatRootView = new AbsoluteLayout(this);
        mMainLayout.addView(mFloatRootView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFloatRootView.addView(mFloatBtnWrapper, mFloatBtnWindowParams);
    }

    /**
     * 设置触摸监听
     */
    private void setTouchListener() {
        final float scale = MainActivity.this.getResources().getDisplayMetrics().density;
        mEdgePadding = (int) (10 * scale + 0.5);
        mFloatRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mFloatRootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mFloatViewBoundsInScreens = new Rect();
                int[] mainLocation = new int[2];
                int marginTop = Math.max(mainLocation[1],mFloatBtnWrapper.getTop());
                mMainLayout.getLocationOnScreen(mainLocation);
                mFloatViewBoundsInScreens.set(
                        mainLocation[0],
                        mainLocation[1],
                        mainLocation[0] + mMainLayout.getWidth(),
                        mMainLayout.getHeight() + mainLocation[1]);
                mFloatTouchListener = new FloatTouchListener(MainActivity.this,mFloatViewBoundsInScreens,mFloatBtnWrapper,
                        mFloatBtnWindowParams,mainLocation[1],mEdgePadding);
                mFloatTouchListener.setFloatButtonCallback(new FloatTouchListener.FloatButtonCallback() {
                    @Override
                    public void onPositionChanged(int x, int y, int gravityX, float percentY) {

                    }

                    @Override
                    public void onTouch() {

                    }
                });
                mFloatRootView.setOnTouchListener(mFloatTouchListener);
                mFloatRootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RotateAnimation rotateAnimation = new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        rotateAnimation.setDuration(1000);
                        rotateAnimation.setRepeatCount(3);
                        mImageView.startAnimation(rotateAnimation);
                        Toast.makeText(MainActivity.this,"点击展现效果",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}
