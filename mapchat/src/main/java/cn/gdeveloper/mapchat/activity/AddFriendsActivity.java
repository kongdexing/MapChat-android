package cn.gdeveloper.mapchat.activity;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
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
import cn.gdeveloper.mapchat.common.MapChatHttpService;
import cn.gdeveloper.mapchat.http.MapChatMessageID;
import cn.gdeveloper.mapchat.http.WebResponse;
import cn.gdeveloper.mapchat.model.Friend;
import cn.gdeveloper.mapchat.ui.LoadingDialog;
import cn.gdeveloper.mapchat.ui.WinToast;
import cn.gdeveloper.mapchat.view.adapter.SearchFriendAdapter;

/**
 *
 */
public class AddFriendsActivity extends BaseActivity implements View.OnClickListener {

    private EditText edt_input;
    private Button btn_search;
    private InputMethodManager imm;
    private RecyclerView recycler_SearchFriend;
    private SearchFriendAdapter adapter;
    private LoadingDialog mDialog;

    @Override
    protected int setContentViewResId() {
        return R.layout.activity_add_friends;
    }

    @Override
    protected void initView() {
        recycler_SearchFriend = (RecyclerView) findViewById(R.id.recycler_SearchFriend);
        edt_input = (EditText) findViewById(R.id.inputEdit);
        btn_search = (Button) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
        mDialog = new LoadingDialog(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        layoutManager.scrollToPosition(0);
        recycler_SearchFriend.setLayoutManager(layoutManager);
        recycler_SearchFriend.setHasFixedSize(true);

        adapter = new SearchFriendAdapter();
        recycler_SearchFriend.setAdapter(adapter);
        recycler_SearchFriend.setItemAnimator(new DefaultItemAnimator());

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

        edt_input.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    btn_search.setText(R.string.public_account_cancel);
                } else {
                    btn_search.setText(R.string.public_account_search);
                }
            }
        });
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onClick(View v) {
        String searchKey = edt_input.getText().toString().trim();
        if (searchKey.isEmpty()) {
            finish();
            return;
        }
        imm.hideSoftInputFromWindow(edt_input.getWindowToken(), 0); //强制隐藏键盘
        MapChatHttpService.getInstance().search(searchKey, new WebResponse(handler));
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WebResponse.HttpRequestMessage.MSG_REQUEST_START:
                    showDialog();
                    break;
                case WebResponse.HttpRequestMessage.MSG_REQUEST_END:
                    hideDialog();
                    break;
                case MapChatMessageID.MSG_SEARCH_SUCCESS:
                    Log.i("AddFriend","MSG_SEARCH_SUCCESS");
                    adapter.loadData((ArrayList<Friend>) msg.obj);
                    recycler_SearchFriend.setAdapter(adapter);
                    break;
                case MapChatMessageID.MSG_SEARCH_FAILED:
                    WinToast.toast(AddFriendsActivity.this, (String) msg.obj);
                    break;
            }
            super.handleMessage(msg);
        }
    };

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
