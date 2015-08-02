package cn.gdeveloper.mapchat.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import cn.gdeveloper.mapchat.R;
import cn.gdeveloper.mapchat.app.MapChatContext;
import cn.gdeveloper.mapchat.common.MapChatHttpService;
import cn.gdeveloper.mapchat.http.MapChatMessageID;
import cn.gdeveloper.mapchat.http.WebResponse;
import cn.gdeveloper.mapchat.ui.LoadingDialog;
import cn.gdeveloper.mapchat.utils.SharedPreferencesUtil;
import io.rong.imkit.RongIM;

public class LogoActivity extends BaseActivity {

    private ImageView mImgBackgroud;
    private LoadingDialog mDialog;

    @Override
    protected int setContentViewResId() {
        return R.layout.activity_logo;
    }

    @Override
    protected void initView() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();//隐藏ActionBar
        mImgBackgroud = (ImageView)findViewById(R.id.img_backgroud);
        Animation animation = AnimationUtils.loadAnimation(LogoActivity.this, R.anim.translate_anim);
        mImgBackgroud.startAnimation(animation);

//        String loginname = MapChatContext.getInstance().getSharedPreferences().getString(SharedPreferencesUtil.USER_LOGINNAME,"");
//        String pwd = MapChatContext.getInstance().getSharedPreferences().getString(SharedPreferencesUtil.USER_PASSWORD,"");
//        if(!loginname.isEmpty()&&!pwd.isEmpty()){
//            mDialog = new LoadingDialog(this);
//            MapChatHttpService.getInstance().login(loginname,pwd,new WebResponse(mHandler));
//        }else{
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    LogoActivity.this.startActivity(new Intent(LogoActivity.this,LoginActivity.class));
//                    LogoActivity.this.finish();
//                }
//            },2000);
//        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LogoActivity.this.startActivity(new Intent(LogoActivity.this,MapChatMainActivity.class));
                LogoActivity.this.finish();
            }
        },1000);
    }

    @Override
    protected void initData() {

    }

    private Handler mHandler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MapChatMessageID.MSG_MEMBER_LOGIN_SUCCESS:
                    hideDialog();
                    RongIM.getInstance().setUserInfoAttachedState(true);
//                    RongCloudEvent.getInstance().setOtherListener();

                    startActivity(new Intent(LogoActivity.this, MainActivity.class));
                    finish();
                    break;
                case MapChatMessageID.MSG_MEMBER_LOGIN_FAILED:
                    startActivity(new Intent(LogoActivity.this, LoginActivity.class));
                    break;
            }
        }
    };

    private void showDialog(){
        if (mDialog != null&&!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    private void hideDialog(){
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }
}
