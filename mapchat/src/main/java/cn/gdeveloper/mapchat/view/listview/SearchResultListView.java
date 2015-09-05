package cn.gdeveloper.mapchat.view.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;

import cn.gdeveloper.mapchat.model.Friend;
import cn.gdeveloper.mapchat.view.adapter.SearchResultAdapter;

/**
 * Created by Dexing on 2015/8/23.
 */
public class SearchResultListView extends PullToRefreshListView implements
        AdapterView.OnItemClickListener {

    private SearchResultAdapter mAdapter;
    private ArrayList<Friend> list_searchFriends;

    public SearchResultListView(Context context) {
        this(context, null);
    }

    public SearchResultListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mAdapter = new SearchResultAdapter(context);
        setAdapter(mAdapter);
        setOnItemClickListener(this);
        uniteListView(this);

        setOverScrollMode(View.OVER_SCROLL_NEVER);
        setonRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                onClear();
                onReload();
            }
        });
        setIdleState();
    }

    public void setFriends(ArrayList<Friend> friends) {
        list_searchFriends = friends;
        mAdapter.loadFriends(list_searchFriends);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void onLoadNextPage() {

    }

    @Override
    public void onClear() {
        list_searchFriends.clear();
    }

    @Override
    public void onReload() {

    }
}
