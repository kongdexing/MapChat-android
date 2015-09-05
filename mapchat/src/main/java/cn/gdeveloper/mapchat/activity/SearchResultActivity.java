package cn.gdeveloper.mapchat.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import cn.gdeveloper.mapchat.R;
import cn.gdeveloper.mapchat.model.Friend;
import cn.gdeveloper.mapchat.ui.WinToast;
import cn.gdeveloper.mapchat.view.BackActionBar;
import cn.gdeveloper.mapchat.view.listview.SearchResultListView;

public class SearchResultActivity extends BaseActionBarActivity {

    private BackActionBar titleBar;
    private String searchKey = "";
    private ArrayList<Friend> list_searchFriends;
    private SearchResultListView listview_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            searchKey = bundle.getString("searchKey");
            list_searchFriends = (ArrayList<Friend>) bundle.get("searchVal");
        }else{
            WinToast.toast(SearchResultActivity.this, R.string.search_result_empty);
            finish();
        }

        titleBar = (BackActionBar)findViewById(R.id.actionbar_result);
        titleBar.setTitle(searchKey);

        listview_result = (SearchResultListView)findViewById(R.id.listview_result);
        listview_result.setFriends(list_searchFriends);
    }


}
