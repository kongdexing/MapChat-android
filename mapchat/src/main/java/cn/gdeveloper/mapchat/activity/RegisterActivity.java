package cn.gdeveloper.mapchat.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import cn.gdeveloper.mapchat.app.DemoContext;
import cn.gdeveloper.mapchat.R;
import cn.gdeveloper.mapchat.common.MapChatHttpService;
import cn.gdeveloper.mapchat.http.MapChatMessageID;
import cn.gdeveloper.mapchat.http.WebResponse;
import cn.gdeveloper.mapchat.model.Status;
import cn.gdeveloper.mapchat.ui.DeEditTextHolder;
import cn.gdeveloper.mapchat.ui.LoadingDialog;
import cn.gdeveloper.mapchat.ui.WinToast;
import cn.gdeveloper.mapchat.utils.CommonUtils;
import cn.gdeveloper.mapchat.utils.NetUtils;
import me.add1.exception.BaseException;
import me.add1.exception.InternalException;
import me.add1.network.AbstractHttpRequest;

/**
 * Created by Bob on 2015/2/6.
 */
public class RegisterActivity extends BaseApiActivity implements View.OnClickListener, DeEditTextHolder.OnEditTextFocusChangeListener, Handler.Callback {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private static final int HANDLER_REGIST_HAS_NO_FOCUS = 1;
    private static final int HANDLER_REGIST_HAS_FOCUS = 2;

    private EditText mRegistEmail;
    private EditText mRegistPhone;
    private EditText mRegistPassword;
    private EditText mRegistNickName;
    private Button mRegisteButton;
    private TextView mRegisteUserAgreement;
    private FrameLayout mEmailDeleteFramelayout;
    private FrameLayout mPhoneDeleteFramelayout;
    private FrameLayout mPasswordDeleteFramelayout;
    private FrameLayout mNickNameDeleteFramelayout;

    private ImageView mImgBackgroud;
    DeEditTextHolder mEditUserNameEt;
    DeEditTextHolder mEditPhoneEt;
    DeEditTextHolder mEditPassWordEt;
    DeEditTextHolder mEditNickNameEt;
    /**
     * 软键盘的控制
     */
    private InputMethodManager mSoftManager;
    private AbstractHttpRequest<Status> httpRequest;
    /**
     * 是否展示title
     */
    private LinearLayout mIsShowTitle;
    private Handler mHandler;
    private LoadingDialog mDialog;

    @Override
    protected int setContentViewResId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();//隐藏ActionBar
        mRegistEmail = (EditText) findViewById(R.id.et_register_mail);
        mRegistPhone = (EditText)findViewById(R.id.et_register_phone);
        mRegistPassword = (EditText) findViewById(R.id.et_register_password);
        mRegistNickName = (EditText) findViewById(R.id.et_register_nickname);
        mRegisteUserAgreement = (TextView) findViewById(R.id.register_user_agreement);
        mRegisteButton = (Button) findViewById(R.id.register_agree_button);
        mImgBackgroud = (ImageView) findViewById(R.id.de_img_backgroud);
        mIsShowTitle = (LinearLayout) findViewById(R.id.de_regist_title);
        mEmailDeleteFramelayout = (FrameLayout) findViewById(R.id.et_register_delete);
        mPhoneDeleteFramelayout = (FrameLayout) findViewById(R.id.et_phone_delete);
        mPasswordDeleteFramelayout = (FrameLayout) findViewById(R.id.et_password_delete);
        mNickNameDeleteFramelayout = (FrameLayout) findViewById(R.id.et_nickname_delete);
        mDialog = new LoadingDialog(this);
        mHandler = new Handler(this);
        mSoftManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.translate_anim);
                mImgBackgroud.startAnimation(animation);
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        event.getKeyCode();
        switch (event.getKeyCode()){
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_ESCAPE:
                Message mess = Message.obtain();
                mess.what = HANDLER_REGIST_HAS_NO_FOCUS;
                mHandler.sendMessage(mess);
                break;
            default:
                Log.i(TAG,"getKeyCode : "+event.getKeyCode());
                break;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void initData() {
        mRegisteButton.setOnClickListener(this);
        mRegisteUserAgreement.setOnClickListener(this);
        mEditUserNameEt = new DeEditTextHolder(mRegistEmail, mEmailDeleteFramelayout, null);
        mEditPhoneEt = new DeEditTextHolder(mRegistPhone,mPhoneDeleteFramelayout,null);
        mEditNickNameEt = new DeEditTextHolder(mRegistNickName, mNickNameDeleteFramelayout, null);
        mEditPassWordEt = new DeEditTextHolder(mRegistPassword, mPasswordDeleteFramelayout, null);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRegistEmail.setOnClickListener(RegisterActivity.this);
                mRegistPhone.setOnClickListener(RegisterActivity.this);
                mRegistNickName.setOnClickListener(RegisterActivity.this);
                mRegistPassword.setOnClickListener(RegisterActivity.this);
                mEditUserNameEt.setmOnEditTextFocusChangeListener(RegisterActivity.this);
                mEditPhoneEt.setmOnEditTextFocusChangeListener(RegisterActivity.this);
                mEditNickNameEt.setmOnEditTextFocusChangeListener(RegisterActivity.this);
                mEditPassWordEt.setmOnEditTextFocusChangeListener(RegisterActivity.this);
            }
        }, 200);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                mSoftManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                Message mess = Message.obtain();
                mess.what = HANDLER_REGIST_HAS_NO_FOCUS;
                mHandler.sendMessage(mess);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case HANDLER_REGIST_HAS_NO_FOCUS:
                mIsShowTitle.setVisibility(View.VISIBLE);
                break;
            case HANDLER_REGIST_HAS_FOCUS:
                mIsShowTitle.setVisibility(View.GONE);
                break;
            case MapChatMessageID.MSG_MEMBER_REGISTER_SUCCESS:

                break;
            case MapChatMessageID.MSG_MEMBER_REGISTER_FAILED:
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.hide();
                }
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_agree_button://注册button
                String email = mRegistEmail.getText().toString().trim();
                String password = mRegistPassword.getText().toString().trim();
                String phone = mRegistPhone.getText().toString().trim();
                String nickName = mRegistNickName.getText().toString().trim();

                if (TextUtils.isEmpty(email)||TextUtils.isEmpty(phone) || TextUtils.isEmpty(password) || TextUtils.isEmpty(nickName)) {
                    WinToast.toast(this, R.string.register_is_null);
                    return;
                } else if (!CommonUtils.isEmail(email)) {
                    WinToast.toast(this, R.string.register_email_error);
                    return;
                } else if(!CommonUtils.isPhone(phone)){
                    WinToast.toast(this, R.string.register_phone_error);
                    return;
                }
                MapChatHttpService.getInstance().register(email,nickName,phone,password,new WebResponse(mHandler));
//                if (DemoContext.getInstance() != null)
//                    httpRequest = DemoContext.getInstance().getDemoApi().register(email, nickName, phone, password, this);
                if (mDialog != null && !mDialog.isShowing()) {
                    mDialog.show();
                }
                break;
            case R.id.register_user_agreement://用户协议

                break;
            case R.id.et_register_mail:
            case R.id.et_register_password:
            case R.id.et_register_nickname:
                Message mess = Message.obtain();
                mess.what = HANDLER_REGIST_HAS_FOCUS;
                mHandler.sendMessage(mess);
                break;
            case R.id.de_left://登录
                finish();
                break;
            case R.id.de_right://忘记密码
                WinToast.toast(this,"忘记密码");
                break;
        }
    }

    protected void onPause() {
        super.onPause();
        if (mSoftManager == null) {
            mSoftManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        if (getCurrentFocus() != null) {
            mSoftManager.hideSoftInputFromWindow(getCurrentFocus()
                    .getWindowToken(), 0);// 隐藏软键盘
        }
    }

    @Override
    public void onCallApiSuccess(AbstractHttpRequest request, Object obj) {
    }

    @Override
    public void onCallApiFailure(AbstractHttpRequest request, BaseException e) {
        Log.d(TAG + "--onCallApiFailure:", "onCallApiFailure");
    }

    @Override
    public void onEditTextFocusChange(View v, boolean hasFocus) {

        switch (v.getId()) {

            case R.id.et_register_mail:
            case R.id.et_register_phone:
            case R.id.et_register_password:
            case R.id.et_register_nickname:
                Message mess = Message.obtain();
                if (hasFocus) {
                    mess.what = HANDLER_REGIST_HAS_FOCUS;
                }
                mHandler.sendMessage(mess);
                break;
        }
    }
}
