package cn.gdeveloper.mapchat.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.gdeveloper.mapchat.R;
import cn.gdeveloper.mapchat.common.MapChatHttpService;
import cn.gdeveloper.mapchat.http.MapChatMessageID;
import cn.gdeveloper.mapchat.http.WebResponse;
import cn.gdeveloper.mapchat.model.Status;
import cn.gdeveloper.mapchat.ui.DeEditTextHolder;
import cn.gdeveloper.mapchat.ui.LoadingDialog;
import cn.gdeveloper.mapchat.ui.WinToast;
import cn.gdeveloper.mapchat.utils.CommonUtils;
import me.add1.network.AbstractHttpRequest;

/**
 * Created by Bob on 2015/2/6.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener, Handler.Callback {

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
    private AbstractHttpRequest<Status> httpRequest;
    /**
     * 是否展示title
     */
    private LinearLayout mIsShowTitle;
    private Handler mHandler;
    private LoadingDialog mDialog;
    private RelativeLayout rl_root_parent;

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
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.translate_anim);
                mImgBackgroud.startAnimation(animation);
            }
        });

        rl_root_parent = (RelativeLayout)findViewById(R.id.rl_root_parent);
        rl_root_parent.getRootView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int rootHeight = rl_root_parent.getRootView().getHeight();
                int height = rl_root_parent.getHeight();
                int inerval = rootHeight - height;
                if (inerval > 100) {
                    mHandler.sendEmptyMessage(HANDLER_REGIST_HAS_FOCUS);
                } else {
                    mHandler.sendEmptyMessage(HANDLER_REGIST_HAS_NO_FOCUS);
                }
            }
        });
    }

    @Override
    protected void initData() {
        mRegisteButton.setOnClickListener(this);
        mRegisteUserAgreement.setOnClickListener(this);
        mEditUserNameEt = new DeEditTextHolder(mRegistEmail, mEmailDeleteFramelayout, null);
        mEditPhoneEt = new DeEditTextHolder(mRegistPhone,mPhoneDeleteFramelayout,null);
        mEditNickNameEt = new DeEditTextHolder(mRegistNickName, mNickNameDeleteFramelayout, null);
        mEditPassWordEt = new DeEditTextHolder(mRegistPassword, mPasswordDeleteFramelayout, null);

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
//                if (MapChatContext.getInstance() != null)
//                    httpRequest = MapChatContext.getInstance().getDemoApi().register(email, nickName, phone, password, this);
                if (mDialog != null && !mDialog.isShowing()) {
                    mDialog.show();
                }
                break;
            case R.id.register_user_agreement://用户协议

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
    }

}
