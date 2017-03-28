package com.example.yyz.news121;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.yyz.news121.activity.HomeActivity;
import com.example.yyz.news121.fragment.LeftmenuFragment;
import com.example.yyz.news121.utils.CacheUtils;

public class MainActivity extends Activity {
    private RelativeLayout activity_main_logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity_main_logo= (RelativeLayout) findViewById(R.id.activity_main_logo);

//旋转动画
        RotateAnimation ra=new RotateAnimation(0,360,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        ra.setFillAfter(true);
//渐变动画
        AlphaAnimation aa=new AlphaAnimation(0,1);
        aa.setFillAfter(true);
//缩放动画
        ScaleAnimation sa=new ScaleAnimation(0,1,0,1,ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);
        sa.setFillAfter(true);

        //添加动画，没有先后顺序
        AnimationSet as=new AnimationSet(false);
        as.addAnimation(ra);
        as.addAnimation(sa);
        as.addAnimation(aa);
        as.setDuration(2000);

        activity_main_logo.setAnimation(as);
        //监听动画播放完成
        as.setAnimationListener(new MyAnimationListener());
    }


    class MyAnimationListener implements Animation.AnimationListener {

        /**
         * 当动画开始播放的时候回调
         * @param animation
         */
        @Override
        public void onAnimationStart(Animation animation) {

        }
        /**
         * 当动画播放完成的时候回调
         * @param animation
         */
        @Override
        public void onAnimationEnd(Animation animation) {
            boolean isStartMain = CacheUtils.getBoolean(MainActivity.this,GuideActivity.START_MAIN);
            if(isStartMain){
                //直接进入主页面
                Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                startActivity(intent);
            }else
            {
                Intent intent = new Intent(MainActivity.this,GuideActivity.class);
                startActivity(intent);
            }
            //Toast.makeText(MainActivity.this, "动画播放完成", Toast.LENGTH_SHORT).show();

            //关闭当前页面
            finish();

        }
        /**
         * 当动画重复播放的时候回调
         * @param animation
         */
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

}
