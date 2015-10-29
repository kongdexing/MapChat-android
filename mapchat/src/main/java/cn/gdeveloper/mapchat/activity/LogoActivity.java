package cn.gdeveloper.mapchat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import cn.gdeveloper.mapchat.R;
import cn.gdeveloper.mapchat.app.RongCloudEvent;
import cn.gdeveloper.mapchat.http.impl.MapChatHttpService;
import cn.gdeveloper.mapchat.http.request.IResponseListener;
import cn.gdeveloper.mapchat.http.request.MapChatMessageID;
import cn.gdeveloper.mapchat.model.User;
import cn.gdeveloper.mapchat.ui.LoadingDialog;
import cn.gdeveloper.mapchat.ui.WinToast;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class LogoActivity extends Activity {

    private String TAG = LogoActivity.class.getSimpleName();
    private LoadingDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        initView();
    }

    private void initView() {
        String loginName = User.getInstance().getLoginName();
        String pwd = User.getInstance().getPassword();
        if (!loginName.isEmpty() && !pwd.isEmpty() && User.getInstance().getLoginState()) {
            mDialog = new LoadingDialog(this);
            MapChatHttpService.getInstance().login(loginName, pwd, new ResponseListener());
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    LogoActivity.this.startActivity(new Intent(LogoActivity.this, LoginActivity.class));
                    LogoActivity.this.finish();
                }
            }, 2000);
        }

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                LogoActivity.this.startActivity(new Intent(LogoActivity.this, MyActivity.class));
//                LogoActivity.this.finish();
//            }
//        }, 1000);
    }

    private class ResponseListener implements IResponseListener {

        @Override
        public void onStart() {
            showDialog();
        }

        @Override
        public void onRequestResponse(int code, Object value) {
            hideDialog();
            switch (code) {
                case MapChatMessageID.MSG_MEMBER_LOGIN_SUCCESS:
                    Log.i(TAG, "login success userid :" + User.getInstance().getUserId());
                    httpGetTokenSuccess(User.getInstance().getToken());
                    break;
                case MapChatMessageID.MSG_MEMBER_LOGIN_FAILED:
                    WinToast.toast(LogoActivity.this, R.string.login_failure);
                    startActivity(new Intent(LogoActivity.this, LoginActivity.class));
                    finish();
                    break;
                default:
                    WinToast.toast(LogoActivity.this, value.toString());
                    break;
            }
        }
    }

    private void httpGetTokenSuccess(String token) {
        /**
         * IMKit SDK调用第二步
         * 建立与服务器的连接
         * 详见API
         * http://docs.rongcloud.cn/api/android/imkit/index.html
         */
        Log.i(TAG, "token:" + token);
        RongIM.connect(token, new RongIMClient.ConnectCallback() {

            @Override
            public void onSuccess(String userId) {
                Log.e(TAG, "onSuccess userId:" + userId);

                RongIM.getInstance().setUserInfoAttachedState(true);
                RongCloudEvent.getInstance().setOtherListener();

                startActivity(new Intent(LogoActivity.this, MyActivity.class));
                finish();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e(TAG, "onError :" + errorCode);
                WinToast.toast(LogoActivity.this, R.string.login_failure);
                startActivity(new Intent(LogoActivity.this, LoginActivity.class));
                finish();
            }
        });

        //发起获取好友列表的http请求  (注：非融云SDK接口，是demo接口)
    }

    private void showDialog() {
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
        }
    }

    private void hideDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }
}
