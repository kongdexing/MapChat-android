package cn.gdeveloper.mapchat.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import cn.gdeveloper.mapchat.R;
import cn.gdeveloper.mapchat.http.impl.MapChatHttpService;
import cn.gdeveloper.mapchat.http.request.IResponseListener;
import cn.gdeveloper.mapchat.http.request.MapChatMessageID;
import cn.gdeveloper.mapchat.model.Friend;
import cn.gdeveloper.mapchat.ui.WinToast;
import cn.gdeveloper.mapchat.view.BackActionBar;
import cn.gdeveloper.mapchat.view.listview.PullToRefreshListView;
import cn.gdeveloper.mapchat.view.listview.SearchResultListView;

public class SearchResultActivity extends BaseActionBarActivity {

    private BackActionBar titleBar;
    private String searchKey = "";
    private ArrayList<Friend> list_searchFriends = new ArrayList<Friend>();
    private SearchResultListView listview_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            searchKey = bundle.getString("searchKey");
//            list_searchFriends = (ArrayList<Friend>) bundle.get("searchVal");
        } else {
//            WinToast.toast(SearchResultActivity.this, R.string.search_result_empty);
            finish();
            return;
        }

        titleBar = (BackActionBar) findViewById(R.id.actionbar_result);
        titleBar.setTitle(searchKey);

        listview_result = (SearchResultListView) findViewById(R.id.listview_result);
        MapChatHttpService.getInstance().search(searchKey, new ResponseListener());

        listview_result.setonRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listview_result.onClear();
                MapChatHttpService.getInstance().search(searchKey, new ResponseListener());
            }
        });

    }

    private class ResponseListener implements IResponseListener {

        @Override
        public void onStart() {
            showDialog();
        }

        @Override
        public void onRequestResponse(int code, Object value) {
            hideDialog();
            listview_result.resetAllState();
            switch (code) {
                case MapChatMessageID.MSG_SEARCH_SUCCESS:
                    Log.i("AddFriend", "MSG_SEARCH_SUCCESS");
                    listview_result.setFriends((ArrayList<Friend>) value);
                    break;
                default:
                    WinToast.toast(SearchResultActivity.this, (String) value);
                    break;
            }
        }
    }

}
