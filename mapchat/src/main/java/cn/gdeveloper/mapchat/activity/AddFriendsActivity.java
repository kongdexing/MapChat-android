package cn.gdeveloper.mapchat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import cn.gdeveloper.mapchat.R;
import cn.gdeveloper.mapchat.http.impl.MapChatHttpService;
import cn.gdeveloper.mapchat.http.request.IResponseListener;
import cn.gdeveloper.mapchat.http.request.MapChatMessageID;
import cn.gdeveloper.mapchat.model.Friend;
import cn.gdeveloper.mapchat.ui.LoadingDialog;
import cn.gdeveloper.mapchat.ui.WinToast;
import cn.gdeveloper.mapchat.view.adapter.SearchFriendAdapter;

/**
 * 搜索添加好友
 */
public class AddFriendsActivity extends BaseActionBarActivity implements View.OnClickListener {

    private EditText edt_input;
    private Button btn_search;
    private InputMethodManager imm;

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

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        layoutManager.scrollToPosition(0);

//        adapter = new SearchFriendAdapter();
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

        Intent intent = new Intent(AddFriendsActivity.this, SearchResultActivity.class);
        intent.putExtra("searchKey", edt_input.getText().toString().trim());
        startActivity(intent);
    }

}
