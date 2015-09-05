package cn.gdeveloper.mapchat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.util.ArrayList;

import cn.gdeveloper.mapchat.R;
import cn.gdeveloper.mapchat.common.MapChatHandler;
import cn.gdeveloper.mapchat.common.MapChatHttpService;
import cn.gdeveloper.mapchat.http.MapChatMessageID;
import cn.gdeveloper.mapchat.http.WebResponse;
import cn.gdeveloper.mapchat.model.Friend;
import cn.gdeveloper.mapchat.ui.LoadingDialog;
import cn.gdeveloper.mapchat.ui.WinToast;
import cn.gdeveloper.mapchat.view.BackActionBar;
import cn.gdeveloper.mapchat.view.adapter.SearchFriendAdapter;

/**
 * 搜索添加好友
 */
public class AddFriendsActivity extends BaseActionBarActivity implements View.OnClickListener {

    private EditText edt_input;
    private Button btn_search;
    private InputMethodManager imm;
    private SearchFriendAdapter adapter;
    private LoadingDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        initView();
    }

    private void initView() {
        edt_input = (EditText) findViewById(R.id.inputEdit);
        btn_search = (Button) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
        mDialog = new LoadingDialog(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        layoutManager.scrollToPosition(0);

        adapter = new SearchFriendAdapter();
//        recycler_SearchFriend.setAdapter(adapter);
//        recycler_SearchFriend.setItemAnimator(new DefaultItemAnimator());

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        edt_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onClick(btn_search);
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        String searchKey = edt_input.getText().toString().trim();
        if (searchKey.isEmpty()) {
            WinToast.toast(AddFriendsActivity.this, R.string.search_empty_hint);
            return;
        }

        imm.hideSoftInputFromWindow(edt_input.getWindowToken(), 0); //强制隐藏键盘
        MapChatHttpService.getInstance().search(searchKey, new WebResponse(mHandler));
    }

    @Override
    protected MapChatHandler getMessageHandler() {
        return new MapChatHandler(this) {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case WebResponse.HttpRequestMessage.MSG_REQUEST_START:
                        showDialog();
                        break;
                    case WebResponse.HttpRequestMessage.MSG_REQUEST_END:
                        hideDialog();
                        break;
                    case MapChatMessageID.MSG_SEARCH_SUCCESS:
                        Log.i("AddFriend", "MSG_SEARCH_SUCCESS");
                        adapter.loadData((ArrayList<Friend>) msg.obj);
                        Intent intent = new Intent(AddFriendsActivity.this, SearchResultActivity.class);
                        intent.putExtra("searchKey", edt_input.getText().toString().trim());
                        intent.putExtra("searchVal", (ArrayList<Friend>) msg.obj);
                        startActivity(intent);
//                    recycler_SearchFriend.setAdapter(adapter);
                        break;
                    case MapChatMessageID.MSG_SEARCH_FAILED:
                        WinToast.toast(AddFriendsActivity.this, (String) msg.obj);

                        ArrayList<Friend> friends = new ArrayList<Friend>();
                        Friend friend = new Friend();
                        friend.setSex(0);
                        friend.setBirthday("2000-10-01");
                        friend.setPortrait("http://v1.qzone.cc/avatar/201408/22/21/13/53f741f95904e103.jpg%21200x200.jpg");
                        friend.setUserName("小四");
                        friends.add(friend);

                        intent = new Intent(AddFriendsActivity.this, SearchResultActivity.class);
                        intent.putExtra("searchKey", edt_input.getText().toString().trim());
                        intent.putExtra("searchVal", friends);
                        startActivity(intent);
                        break;
                }
                super.handleMessage(msg);
            }
        };
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
