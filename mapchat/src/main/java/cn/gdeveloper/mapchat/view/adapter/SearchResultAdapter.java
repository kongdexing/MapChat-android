package cn.gdeveloper.mapchat.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.gdeveloper.mapchat.R;
import cn.gdeveloper.mapchat.http.download.IDownloader;
import cn.gdeveloper.mapchat.http.impl.MapChatHttpService;
import cn.gdeveloper.mapchat.http.request.IResponseListener;
import cn.gdeveloper.mapchat.http.request.MapChatMessageID;
import cn.gdeveloper.mapchat.model.Friend;
import cn.gdeveloper.mapchat.model.User;
import cn.gdeveloper.mapchat.ui.WinToast;
import cn.gdeveloper.mapchat.utils.DateUtils;

/**
 * Created by Dexing on 2015/8/23.
 */
public class SearchResultAdapter extends BaseMapChatAdapter {

    private ArrayList<Friend> list_searchFriends = null;

    public SearchResultAdapter(Context context) {
        super(context);
    }

    public void loadFriends(ArrayList<Friend> friends) {
        list_searchFriends = friends;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list_searchFriends != null ? list_searchFriends.size() : 0;
    }

    @Override
    public Friend getItem(int position) {
        return list_searchFriends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_item_searchresult, null);
            viewHolder.img_user = (ImageView) convertView.findViewById(R.id.img_user);
            viewHolder.txt_username = (TextView) convertView.findViewById(R.id.txt_name);
            viewHolder.ll_sexbg = (LinearLayout) convertView.findViewById(R.id.ll_sexbg);
            viewHolder.img_sex = (ImageView) convertView.findViewById(R.id.img_sex);
            viewHolder.txt_age = (TextView) convertView.findViewById(R.id.txt_age);
            viewHolder.txt_location = (TextView) convertView.findViewById(R.id.txt_location);
            viewHolder.btn_add = (Button) convertView.findViewById(R.id.btn_send);
            viewHolder.btn_cancel = (Button) convertView.findViewById(R.id.btn_cancel);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txt_location.setVisibility(View.GONE);

        final IDownloader downloader = MapChatHttpService.getInstance().getDownloader();
        final Friend friend = getItem(position);
        if (friend != null) {
            downloader.downloadBitmap(friend.getPortrait(), viewHolder.img_user, R.mipmap.user_def);
            viewHolder.txt_username.setText(friend.getUserName());
            viewHolder.txt_age.setText(DateUtils.getAge(friend.getBirthday()));
            //0 女 1 男 -1 未知
            if (friend.getSex() == 0) {
                viewHolder.img_sex.setBackgroundResource(R.mipmap.icon_female);
                viewHolder.ll_sexbg.setBackgroundResource(R.drawable.friend_item_femaleage);
            } else if (friend.getSex() == 1) {
                viewHolder.img_sex.setBackgroundResource(R.mipmap.icon_male);
                viewHolder.ll_sexbg.setBackgroundResource(R.drawable.friend_item_maleage);
            } else {
                viewHolder.img_sex.setBackgroundResource(0);
            }

            if (friend.getFriendState()==Friend.STATE_FRIEND){
                viewHolder.btn_add.setVisibility(View.GONE);
                viewHolder.btn_cancel.setVisibility(View.GONE);
            }else if(friend.getFriendState()==Friend.STATE_SENDED){
                viewHolder.btn_add.setVisibility(View.VISIBLE);
                viewHolder.btn_cancel.setVisibility(View.VISIBLE);
                viewHolder.btn_add.setText(R.string.public_added);
            }else if(friend.getFriendState()==Friend.STATE_UNSEND){
                viewHolder.btn_add.setVisibility(View.VISIBLE);
                viewHolder.btn_cancel.setVisibility(View.GONE);
            }

            viewHolder.btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    MapChatHttpService.getInstance().addFriend(User.getInstance().getUserId(), friend.getID(), "", new IResponseListener() {
                        @Override
                        public void onStart() {
                            view.setEnabled(false);
                        }

                        @Override
                        public void onRequestResponse(int code, Object value) {
                            view.setEnabled(true);
                            switch (code) {
                                case MapChatMessageID.MSG_ADDFRIEND_SUCCESS:
                                    WinToast.toast(mContext, "add success ");
                                    break;
                                default:
                                    WinToast.toast(mContext, "add failed ");
                                    break;
                            }
                        }
                    });
                }
            });

            viewHolder.btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView img_user;
        TextView txt_username;
        LinearLayout ll_sexbg;
        ImageView img_sex;
        TextView txt_age;
        TextView txt_location;
        Button btn_add;
        Button btn_cancel;
    }

}
