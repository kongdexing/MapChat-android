package cn.gdeveloper.mapchat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.gdeveloper.mapchat.R;
import cn.gdeveloper.mapchat.app.MapChatContext;
import cn.gdeveloper.mapchat.app.RongCloudEvent;
import cn.gdeveloper.mapchat.http.impl.MapChatHttpService;
import cn.gdeveloper.mapchat.http.request.IResponseListener;
import cn.gdeveloper.mapchat.http.request.MapChatMessageID;
import cn.gdeveloper.mapchat.model.ApiResult;
import cn.gdeveloper.mapchat.model.Friends;
import cn.gdeveloper.mapchat.model.Groups;
import cn.gdeveloper.mapchat.model.User;
import cn.gdeveloper.mapchat.ui.DeEditTextHolder;
import cn.gdeveloper.mapchat.ui.LoadingDialog;
import cn.gdeveloper.mapchat.ui.WinToast;
import cn.gdeveloper.mapchat.utils.SharedPreferencesUtil;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import me.add1.network.AbstractHttpRequest;

/**
 * Created by Bob on 2015/1/30.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, Handler.Callback {

    private static final String TAG = LoginActivity.class.getSimpleName();
    /**
     * 用户账户
     */
    private EditText mUserNameEt;
    /**
     * 密码
     */
    private EditText mPassWordEt;
    /**
     * 登录button
     */
    private Button mSignInBt;
    /**
     * 设备id
     */
    private String mDeviceId;
    /**
     * 忘记密码
     */
    private TextView mFogotPassWord;
    /**
     * 注册
     */
    private TextView mRegister;
    /**
     * 输入用户名删除按钮
     */
    private FrameLayout mFrUserNameDelete;
    /**
     * 输入密码删除按钮
     */
    private FrameLayout mFrPasswordDelete;
    /**
     * logo
     */
    private ImageView mLoginImg;
    /**
     * 是否展示title
     */
    private RelativeLayout mIsShowTitle;
    /**
     * 左侧title
     */
    private TextView mLeftTitle;
    /**
     * 右侧title
     */
    private TextView mRightTitle;
    private static final int REQUEST_CODE_REGISTER = 200;
    private static final int HANDLER_LOGIN_HAS_FOCUS = 3;
    private static final int HANDLER_LOGIN_HAS_NO_FOCUS = 4;

    private LoadingDialog mDialog;
    private AbstractHttpRequest<User> loginHttpRequest;
    private AbstractHttpRequest<User> getTokenHttpRequest;
    private AbstractHttpRequest<Friends> getUserInfoHttpRequest;
    private AbstractHttpRequest<Groups> mGetMyGroupsRequest;

    private Handler mHandler;
    private List<User> mUserList;
    private List<ApiResult> mResultList;
    DeEditTextHolder mEditUserNameEt;
    DeEditTextHolder mEditPassWordEt;
    private RelativeLayout rl_login_parent;

    @Override
    protected int setContentViewResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();//隐藏ActionBar
        mLoginImg = (ImageView) findViewById(R.id.de_login_logo);
        mUserNameEt = (EditText) findViewById(R.id.app_username_et);
        mPassWordEt = (EditText) findViewById(R.id.app_password_et);
        mSignInBt = (Button) findViewById(R.id.app_sign_in_bt);
        mRegister = (TextView) findViewById(R.id.de_login_register);
        mFogotPassWord = (TextView) findViewById(R.id.de_login_forgot);
        mFrUserNameDelete = (FrameLayout) findViewById(R.id.fr_username_delete);
        mFrPasswordDelete = (FrameLayout) findViewById(R.id.fr_pass_delete);
        mIsShowTitle = (RelativeLayout) findViewById(R.id.de_merge_rel);
        mLeftTitle = (TextView) findViewById(R.id.de_left);
        mRightTitle = (TextView) findViewById(R.id.de_right);
        mUserList = new ArrayList<User>();
        mResultList = new ArrayList<ApiResult>();

        mSignInBt.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mLeftTitle.setOnClickListener(this);
        mRightTitle.setOnClickListener(this);
        mHandler = new Handler(this);
        mDialog = new LoadingDialog(this);

        rl_login_parent = (RelativeLayout) findViewById(R.id.rl_login_parent);
        rl_login_parent.getRootView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int rootHeight = rl_login_parent.getRootView().getHeight();
                int height = rl_login_parent.getHeight();
                int inerval = rootHeight - height;
                if (inerval > 100) {
                    mHandler.sendEmptyMessage(HANDLER_LOGIN_HAS_FOCUS);
                } else {
                    mHandler.sendEmptyMessage(HANDLER_LOGIN_HAS_NO_FOCUS);
                }
            }
        });

        mEditUserNameEt = new DeEditTextHolder(mUserNameEt, mFrUserNameDelete, null);
        mEditPassWordEt = new DeEditTextHolder(mPassWordEt, mFrPasswordDelete, null);

    }

    @Override
    protected void initData() {
        mUserNameEt.setText(User.getInstance().getLoginName());
        mPassWordEt.setText(User.getInstance().getPassword());

        TelephonyManager mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        mDeviceId = mTelephonyManager.getDeviceId();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mUserNameEt.setOnClickListener(LoginActivity.this);
                mPassWordEt.setOnClickListener(LoginActivity.this);
            }
        }, 200);
    }

    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case HANDLER_LOGIN_HAS_FOCUS:
                mLoginImg.setVisibility(View.GONE);
                mRegister.setVisibility(View.GONE);
                mFogotPassWord.setVisibility(View.GONE);
                mIsShowTitle.setVisibility(View.VISIBLE);
                mLeftTitle.setText(R.string.app_sign_up);
                mRightTitle.setText(R.string.app_fogot_password);
                break;
            case HANDLER_LOGIN_HAS_NO_FOCUS:
                mLoginImg.setVisibility(View.VISIBLE);
                mRegister.setVisibility(View.VISIBLE);
                mFogotPassWord.setVisibility(View.VISIBLE);
                mIsShowTitle.setVisibility(View.GONE);
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_sign_in_bt://登录
                String userName = mUserNameEt.getEditableText().toString();
                String passWord = mPassWordEt.getEditableText().toString();
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(passWord)) {
                    WinToast.toast(this, R.string.login_erro_is_null);
                    return;
                }
                User.getInstance().setLoginName(userName);
                User.getInstance().setPassword(passWord);
                User.getInstance().setLoginState(false);
                MapChatHttpService.getInstance().login(userName, passWord, new ResponseListener());
                break;
            case R.id.de_left://注册
            case R.id.de_login_register://注册
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivityForResult(intent, REQUEST_CODE_REGISTER);
                break;
            case R.id.de_login_forgot://忘记密码
                WinToast.toast(this, "忘记密码");
                break;
            case R.id.de_right://忘记密码
                Intent intent1 = new Intent(this, RegisterActivity.class);
                startActivityForResult(intent1, REQUEST_CODE_REGISTER);
                break;
            case R.id.app_username_et:
            case R.id.app_password_et:
                Message mess = Message.obtain();
                mess.what = HANDLER_LOGIN_HAS_FOCUS;
                mHandler.sendMessage(mess);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_REGISTER && resultCode == RESULT_OK) {
            if (data != null) {
                mUserNameEt.setText(data.getStringExtra(SharedPreferencesUtil.USER_EMAIL));
                mPassWordEt.setText(data.getStringExtra(SharedPreferencesUtil.USER_PASSWORD));
            }
        }
    }

    private class ResponseListener implements IResponseListener {

        @Override
        public void onStart() {
            Log.i(TAG, "login start");
            showDialog();
        }

        @Override
        public void onRequestResponse(int code, Object value) {
            switch (code) {
                case MapChatMessageID.MSG_MEMBER_LOGIN_SUCCESS:
                    Log.i(TAG, "login success userid :" + User.getInstance().getUserId());
                    httpGetTokenSuccess(User.getInstance().getToken());
                    break;
                case MapChatMessageID.MSG_MEMBER_LOGIN_FAILED:
                    hideDialog();
                    WinToast.toast(LoginActivity.this, R.string.login_failure);
                    break;
                default:
                    WinToast.toast(LoginActivity.this, value.toString());
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
                WinToast.toast(LoginActivity.this, R.string.login_success);

                RongIM.getInstance().setUserInfoAttachedState(true);
                RongCloudEvent.getInstance().setOtherListener();

                startActivity(new Intent(LoginActivity.this, MyActivity.class));
                finish();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e(TAG, "onError :" + errorCode);
                WinToast.toast(LoginActivity.this, R.string.login_failure);
            }
        });

        //发起获取好友列表的http请求  (注：非融云SDK接口，是demo接口)
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
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
