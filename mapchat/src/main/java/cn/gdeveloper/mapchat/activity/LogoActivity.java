package cn.gdeveloper.mapchat.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import cn.gdeveloper.mapchat.R;

public class LogoActivity extends BaseActivity {

    private ImageView mImgBackgroud;

    @Override
    protected int setContentViewResId() {
        return R.layout.activity_logo;
    }

    @Override
    protected void initView() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mImgBackgroud = (ImageView)findViewById(R.id.img_backgroud);
        Animation animation = AnimationUtils.loadAnimation(LogoActivity.this, R.anim.translate_anim);
        mImgBackgroud.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LogoActivity.this.startActivity(new Intent(LogoActivity.this,LoginActivity.class));
                LogoActivity.this.finish();
            }
        },2000);
    }

    @Override
    protected void initData() {

    }
}
