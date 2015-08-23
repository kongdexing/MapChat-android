package cn.gdeveloper.mapchat.activity;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.gdeveloper.mapchat.R;
import cn.gdeveloper.mapchat.model.Friends;
import cn.gdeveloper.mapchat.ui.LoadingDialog;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;
import me.add1.network.AbstractHttpRequest;

public class MainActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, ActionBar.OnMenuVisibilityListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String ACTION_DMEO_RECEIVE_MESSAGE = "action_demo_receive_message";
    public static final String ACTION_DMEO_AGREE_REQUEST = "action_demo_agree_request";
    private RelativeLayout mMainConversationLiner;
    private RelativeLayout mMainGroupLiner;
    private RelativeLayout mMainChatroomLiner;
    private RelativeLayout mMainCustomerLiner;

    /**
     * 聊天室的fragment
     */
    private Fragment mChatroomFragment = null;

    /**
     * 客服的fragment
     */
    private Fragment mCustomerFragment = null;
    /**
     * 会话列表的fragment
     */
    private Fragment mConversationFragment = null;
    /**
     * 群组的fragment
     */
    private Fragment mGroupListFragment = null;
    /**
     * 会话TextView
     */
    private TextView mMainConversationTv;
    /**
     * 群组TextView
     */
    private TextView mMainGroupTv;

    private TextView mUnreadNumView;
    /**
     * 聊天室TextView
     */
    private TextView mMainChatroomTv;
    /**
     * 客服TextView
     */
    private TextView mMainCustomerTv;

    private FragmentManager mFragmentManager;


    private ViewPager mViewPager;
    /**
     * 下划线
     */
    private ImageView mMainSelectImg;

    private DemoFragmentPagerAdapter mDemoFragmentPagerAdapter;

    private LayoutInflater mInflater;
    /**
     * 下划线长度
     */
    int indicatorWidth;
    private RelativeLayout mMainShow;

    private boolean hasNewFriends = false;
    private Menu mMenu;
    private ReceiveMessageBroadcastReceiver mBroadcastReciver;
    private LoadingDialog mDialog;
    //    private AbstractHttpRequest<Friends> getUserInfoHttpRequest;
    private AbstractHttpRequest<Friends> getFriendsHttpRequest;
    private int mNetNum = 0;
    ActivityManager activityManager;
    @Override
    protected int setContentViewResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mFragmentManager = getSupportFragmentManager();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm); // 获取屏幕信息
        indicatorWidth = dm.widthPixels / 4;// 指示器宽度为屏幕宽度的4/1

        mMainShow = (RelativeLayout) findViewById(R.id.main_show);
        mMainConversationLiner = (RelativeLayout) findViewById(R.id.main_conversation_liner);
        mMainGroupLiner = (RelativeLayout) findViewById(R.id.main_group_liner);
        mMainChatroomLiner = (RelativeLayout) findViewById(R.id.main_chatroom_liner);
        mMainCustomerLiner = (RelativeLayout) findViewById(R.id.main_customer_liner);
        mMainConversationTv = (TextView) findViewById(R.id.main_conversation_tv);
        mMainGroupTv = (TextView) findViewById(R.id.main_group_tv);
        mMainChatroomTv = (TextView) findViewById(R.id.main_chatroom_tv);
        mMainCustomerTv = (TextView) findViewById(R.id.main_customer_tv);
        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
        mMainSelectImg = (ImageView) findViewById(R.id.main_switch_img);
        mUnreadNumView = (TextView) findViewById(R.id.de_num);

        ViewGroup.LayoutParams cursor_Params = mMainSelectImg.getLayoutParams();
        cursor_Params.width = indicatorWidth;// 初始化滑动下标的宽
        mMainSelectImg.setLayoutParams(cursor_Params);
        // 获取布局填充器
        mInflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);

    }

    @Override
    protected void initData() {
        activityManager = (ActivityManager)this.getSystemService(ACTIVITY_SERVICE);
        mMainChatroomLiner.setOnClickListener(this);
        mMainConversationLiner.setOnClickListener(this);
        mMainGroupLiner.setOnClickListener(this);
        mMainCustomerLiner.setOnClickListener(this);
        mDemoFragmentPagerAdapter = new DemoFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mDemoFragmentPagerAdapter);
        mViewPager.setOnPageChangeListener(this);
        mDialog = new LoadingDialog(this);
        //发起获取好友列表的http请求  (注：非融云SDK接口，是demo接口)
//        if (MapChatContext.getInstance() != null) {
////            getUserInfoHttpRequest = MapChatContext.getInstance().getDemoApi().getFriends(MainActivity.this);
//
//            getFriendsHttpRequest = MapChatContext.getInstance().getDemoApi().getNewFriendlist(MainActivity.this);
//            if (mDialog != null && !mDialog.isShowing()) {
//                mDialog.show();
//            }
//        }

        final Conversation.ConversationType[] conversationTypes = {Conversation.ConversationType.PRIVATE, Conversation.ConversationType.DISCUSSION,
                Conversation.ConversationType.GROUP, Conversation.ConversationType.SYSTEM,
                Conversation.ConversationType.PUBLICSERVICE, Conversation.ConversationType.APPSERVICE};

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RongIM.getInstance().setOnReceiveUnreadMessageCountListener(mCountListener, conversationTypes);
            }
        }, 500);


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_DMEO_RECEIVE_MESSAGE);
        if (mBroadcastReciver == null) {
            mBroadcastReciver = new ReceiveMessageBroadcastReceiver();
        }
        this.registerReceiver(mBroadcastReciver, intentFilter);
    }

    private class ReceiveMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //收到好友添加的邀请，需要更新 Actionbar
            if (action.equals(ACTION_DMEO_RECEIVE_MESSAGE)) {
                hasNewFriends = intent.getBooleanExtra("has_message", false);
                supportInvalidateOptionsMenu();
            }
        }
    }

    public RongIM.OnReceiveUnreadMessageCountListener mCountListener = new RongIM.OnReceiveUnreadMessageCountListener() {
        @Override
        public void onMessageIncreased(int count) {
            if (count == 0) {
                mUnreadNumView.setVisibility(View.GONE);
            } else if (count > 0 && count < 100) {
                mUnreadNumView.setVisibility(View.VISIBLE);
                mUnreadNumView.setText(count + "");
            } else {
                mUnreadNumView.setVisibility(View.VISIBLE);
                mUnreadNumView.setText(R.string.no_read_message);
            }
        }
    };

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onMenuVisibilityChanged(boolean isVisible) {

    }

    private class DemoFragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

        public DemoFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;
            switch (i) {
                case 0:
                    mMainConversationTv.setTextColor(getResources().getColor(R.color.de_title_bg));
                    if (mConversationFragment == null) {
                        ConversationListFragment listFragment = ConversationListFragment.getInstance();
//                        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
//                                .appendPath("conversationlist")
//                                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
//                                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")
//                                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")
//                                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")
//                                .appendQueryParameter(Conversation.ConversationType.PUBLICSERVICE.getName(), "false")
//                                .appendQueryParameter(Conversation.ConversationType.APPSERVICE.getName(), "false")
//                                .build();
//                        listFragment.initFragment(uri);
                        fragment = listFragment;
                    } else {
                        fragment = mConversationFragment;
                    }
                    break;
                case 1:
//                    if (mGroupListFragment == null) {
//                        mGroupListFragment = new DeGroupListFragment();
//                    }
//                    fragment = mGroupListFragment;
                    break;
                case 2:
//                    if (mChatroomFragment == null) {
//                        fragment = new DeChatroomListFragment();
//                    } else {
//                        fragment = mChatroomFragment;
//                    }
                    break;
                case 3:
//                    if (mCustomerFragment == null) {
//                        fragment = new DeCustomerFragment();
//                    } else {
//                        fragment = mCustomerFragment;
//                    }
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
