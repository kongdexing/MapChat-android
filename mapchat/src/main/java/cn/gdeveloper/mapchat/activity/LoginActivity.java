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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.gdeveloper.mapchat.R;
import cn.gdeveloper.mapchat.app.DemoContext;
import cn.gdeveloper.mapchat.app.RongCloudEvent;
import cn.gdeveloper.mapchat.common.MapChatHttpService;
import cn.gdeveloper.mapchat.http.MapChatMessageID;
import cn.gdeveloper.mapchat.http.WebResponse;
import cn.gdeveloper.mapchat.model.ApiResult;
import cn.gdeveloper.mapchat.model.Friends;
import cn.gdeveloper.mapchat.model.Groups;
import cn.gdeveloper.mapchat.model.User;
import cn.gdeveloper.mapchat.ui.DeEditTextHolder;
import cn.gdeveloper.mapchat.ui.LoadingDialog;
import cn.gdeveloper.mapchat.ui.WinToast;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import me.add1.network.AbstractHttpRequest;

/**
 * Created by Bob on 2015/1/30.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, Handler.Callback {

    private static final String TAG = "LoginActivity";
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
    public static final String INTENT_IMAIL = "intent_email";
    public static final String INTENT_PASSWORD = "intent_password";
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
    private ImageView mImgBackgroud;
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
        mImgBackgroud = (ImageView) findViewById(R.id.de_img_backgroud);
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

        rl_login_parent = (RelativeLayout)findViewById(R.id.rl_login_parent);
        rl_login_parent.getRootView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int rootHeight = rl_login_parent.getRootView().getHeight();
                int height  = rl_login_parent.getHeight();
                int inerval = rootHeight-height;
                if(inerval>100){
                    mHandler.sendEmptyMessage(HANDLER_LOGIN_HAS_FOCUS);
                }else{
                    mHandler.sendEmptyMessage(HANDLER_LOGIN_HAS_NO_FOCUS);
                }
            }
        });

        mEditUserNameEt = new DeEditTextHolder(mUserNameEt, mFrUserNameDelete, null);
        mEditPassWordEt = new DeEditTextHolder(mPassWordEt, mFrPasswordDelete, null);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.translate_anim);
                mImgBackgroud.startAnimation(animation);
            }
        });
    }

    @Override
    protected void initData() {
        if (DemoContext.getInstance() != null) {
            String email = DemoContext.getInstance().getSharedPreferences().getString(INTENT_IMAIL, "");
            String password = DemoContext.getInstance().getSharedPreferences().getString(INTENT_PASSWORD, "");
            mUserNameEt.setText(email);
            mPassWordEt.setText(password);
        }

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
        switch (msg.what){
            case MapChatMessageID.MSG_MEMBER_LOGIN_SUCCESS:
                hideDialog();
                WinToast.toast(LoginActivity.this, R.string.login_success);

                break;
            case MapChatMessageID.MSG_MEMBER_LOGIN_FAILED:
                hideDialog();
                WinToast.toast(LoginActivity.this, R.string.login_failure);
                break;
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
                showDialog();
                MapChatHttpService.getInstance().login(userName,passWord,new WebResponse(mHandler));
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
                mUserNameEt.setText(data.getStringExtra(INTENT_IMAIL));
                mPassWordEt.setText(data.getStringExtra(INTENT_PASSWORD));
            }
        }
    }

    private void httpLoginSuccess(User user, boolean isFirst) {
        if (user.getCode() == 200) {
            Log.e(TAG, "-----get token----");
//            getTokenHttpRequest = DemoContext.getInstance().getDemoApi().getToken(this);
        }
    }

    private void httpGetTokenSuccess(String token) {
        try {
            /**
             * IMKit SDK调用第二步
             * 建立与服务器的连接
             * 详见API
             * http://docs.rongcloud.cn/api/android/imkit/index.html
             */
            Log.e("LoginActivity", "---------onSuccess gettoken----------:" + token);
            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onSuccess(String userId) {
                    Log.e("LoginActivity", "---------onSuccess userId----------:" + userId);
//                    getUserInfoHttpRequest = DemoContext.getInstance().getDemoApi().getFriends(LoginActivity.this);
                    SharedPreferences.Editor edit = DemoContext.getInstance().getSharedPreferences().edit();
                    edit.putString("DEMO_USERID", userId);
                    edit.apply();
                    RongIM.getInstance().setUserInfoAttachedState(true);
                    RongCloudEvent.getInstance().setOtherListener();
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
//                    mHandler.obtainMessage(HANDLER_LOGIN_FAILURE).sendToTarget();
                    Log.e("LoginActivity", "---------onError ----------:" + errorCode);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        //发起获取好友列表的http请求  (注：非融云SDK接口，是demo接口)
        if (DemoContext.getInstance() != null) {
//                getFriendsHttpRequest = DemoContext.getInstance().getDemoApi().getNewFriendlist(LoginActivity.this);
//            mGetMyGroupsRequest = DemoContext.getInstance().getDemoApi().getMyGroups(LoginActivity.this);
        }

        if (DemoContext.getInstance() != null) {
            SharedPreferences.Editor editor = DemoContext.getInstance().getSharedPreferences().edit();
            editor.putString(INTENT_PASSWORD, mPassWordEt.getText().toString());
            editor.putString(INTENT_IMAIL, mUserNameEt.getText().toString());
            editor.apply();
        }
    }

//    @Override
//    public void onCallApiSuccess(AbstractHttpRequest request, Object obj) {
//        //登录成功  返回数据
//        if (loginHttpRequest.equals(request)) {
//            if (obj instanceof User) {
//                final User user = (User) obj;
//                if (user.getCode() == 200) {
//                    if (DemoContext.getInstance() != null && user.getResult() != null) {
//                        SharedPreferences.Editor edit = DemoContext.getInstance().getSharedPreferences().edit();
//                        edit.putString("DEMO_USER_ID", user.getResult().getId());
//                        edit.putString("DEMO_USER_NAME", user.getResult().getUsername());
//                        edit.putString("DEMO_USER_PORTRAIT", user.getResult().getPortrait());
//                        edit.apply();
//                        Log.e(TAG, "-------login success------");
//                        httpLoginSuccess(user, true);
//                    }
//                } else if (user.getCode() == 103) {
//                    hideDialog();
//                    WinToast.toast(LoginActivity.this, "密码错误");
//                } else if (user.getCode() == 104) {
//                    if (mDialog != null)
//                        mDialog.dismiss();
//                    WinToast.toast(LoginActivity.this, "账号错误");
//                }
//            }
//        } else if (getTokenHttpRequest.equals(request)) {
//            if (obj instanceof User) {
//                final User user = (User) obj;
//                if (user.getCode() == 200) {
//                    httpGetTokenSuccess(user.getResult().getToken());
//                    SharedPreferences.Editor edit = DemoContext.getInstance().getSharedPreferences().edit();
//                    edit.putString("DEMO_TOKEN", user.getResult().getToken());
//                    edit.apply();
//                    Log.e(TAG, "------getTokenHttpRequest -success--" + user.getResult().getToken());
//                } else if (user.getCode() == 110) {
//                    WinToast.toast(LoginActivity.this, user.getMessage());
//                } else if (user.getCode() == 111) {
//                    WinToast.toast(LoginActivity.this, user.getMessage());
//                }
//            }
//        } else if (mGetMyGroupsRequest.equals(request)) {
//            if (obj instanceof Groups) {
//                final Groups groups = (Groups) obj;
//                if (groups.getCode() == 200) {
//                    List<Group> grouplist = new ArrayList<>();
//                    if (groups.getResult() != null) {
//                        for (int i = 0; i < groups.getResult().size(); i++) {
//                            String id = groups.getResult().get(i).getId();
//                            String name = groups.getResult().get(i).getName();
//                            if (groups.getResult().get(i).getPortrait() != null) {
//                                Uri uri = Uri.parse(groups.getResult().get(i).getPortrait());
//                                grouplist.add(new Group(id, name, uri));
//                            } else {
//                                grouplist.add(new Group(id, name, null));
//                            }
//                        }
//                        HashMap<String, Group> groupM = new HashMap<String, Group>();
//                        for (int i = 0; i < grouplist.size(); i++) {
//                            groupM.put(groups.getResult().get(i).getId(), grouplist.get(i));
//                            Log.e("login", "------get Group id---------" + groups.getResult().get(i).getId());
//                        }
//                        if (DemoContext.getInstance() != null)
//                            DemoContext.getInstance().setGroupMap(groupM);
//                    }
//                } else {
////                    WinToast.toast(this, groups.getCode());
//                }
//            }
//        } else if (getUserInfoHttpRequest.equals(request)) {
//            //获取好友列表接口  返回好友数据  (注：非融云SDK接口，是demo接口)
//            if (obj instanceof Friends) {
//                final Friends friends = (Friends) obj;
//                if (friends.getCode() == 200) {
//                    ArrayList<UserInfo> friendResults = new ArrayList<UserInfo>();
//                    for (int i = 0; i < friends.getResult().size(); i++) {
//                        UserInfo info = new UserInfo(String.valueOf(friends.getResult().get(i).getId()), friends.getResult().get(i).getUsername(), friends.getResult().get(i).getPortrait() == null ? null : Uri.parse(friends.getResult().get(i).getPortrait()));
//                        friendResults.add(info);
//                    }
//                    friendResults.add(new UserInfo("10000", "新好友消息", Uri.parse("test")));
//                    friendResults.add(new UserInfo("kefu114", "客服11", Uri.parse("http://jdd.kefu.rongcloud.cn/image/service_80x80.png")));
//                    if (DemoContext.getInstance() != null)
//                        //将数据提供给用户信息提供者
//                        DemoContext.getInstance().setUserInfos(friendResults);
////                    mHandler.obtainMessage(HANDLER_LOGIN_SUCCESS).sendToTarget();
//                }
//            }
//        }
//    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

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
