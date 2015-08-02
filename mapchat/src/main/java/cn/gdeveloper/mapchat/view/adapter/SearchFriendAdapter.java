package cn.gdeveloper.mapchat.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cn.gdeveloper.mapchat.model.Friend;

/**
 * Created by Dexing on 2015/8/1.
 */
public class SearchFriendAdapter extends RecyclerView.Adapter<SearchFriendAdapter.ListItemViewHolder> {

    private ArrayList<Friend> list_friend;

    public SearchFriendAdapter(ArrayList<Friend> friends) {
        super();
        list_friend = friends;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder listItemViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return list_friend != null ? list_friend.size() : 0;
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder {
        public ListItemViewHolder(View itemView) {
            super(itemView);
        }
    }

}
