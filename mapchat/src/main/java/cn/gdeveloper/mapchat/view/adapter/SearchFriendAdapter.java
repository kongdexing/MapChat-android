package cn.gdeveloper.mapchat.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.gdeveloper.mapchat.R;
import cn.gdeveloper.mapchat.model.Friend;

/**
 * Created by Dexing on 2015/8/1.
 */
public class SearchFriendAdapter extends RecyclerView.Adapter<SearchFriendAdapter.ListItemViewHolder> {
    private String TAG = SearchFriendAdapter.class.getSimpleName();
    private ArrayList<Friend> list_friend;

    public SearchFriendAdapter() {
        super();
    }

    public void loadData(ArrayList<Friend> friends) {
        list_friend = friends;
        Log.i(TAG, "loadData " + list_friend.size());
        this.notifyDataSetChanged();
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_searchresult, viewGroup, false);
        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder listItemViewHolder, int position) {
        Log.i(TAG, "onBindViewHolder " + position);
        Friend friend = list_friend.get(position);
        listItemViewHolder.txt_name.setText(friend.getUserName());
        listItemViewHolder.txt_birthday.setText(friend.getBirthday());
    }

    @Override
    public int getItemViewType(int position) {
        Log.i(TAG, "getItemViewType " + position);
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        Log.i(TAG, "getItemId " + position);
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount " + (list_friend != null ? list_friend.size() : 0));
        return list_friend != null ? list_friend.size() : 0;
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name;
        TextView txt_birthday;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            Log.i("ListItemViewHolder", "ListItemViewHolder init ");
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_birthday = (TextView) itemView.findViewById(R.id.txt_birthday);
        }
    }

}
