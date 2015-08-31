package cn.gdeveloper.mapchat.view.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by Dexing on 2015/8/1.
 */
public class SearchFriendListView extends PullToRefreshListView implements
        AdapterView.OnItemClickListener {

    public SearchFriendListView(Context context) {
        super(context);
    }

    public SearchFriendListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void onLoadNextPage() {

    }

    @Override
    public void onClear() {

    }

    @Override
    public void onReload() {

    }
}
