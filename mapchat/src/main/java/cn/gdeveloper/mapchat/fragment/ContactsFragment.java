package cn.gdeveloper.mapchat.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.gdeveloper.mapchat.R;
import cn.gdeveloper.mapchat.activity.AddFriendsActivity;
import cn.gdeveloper.mapchat.view.FloatingActionButtonL;
import cn.gdeveloper.mapchat.view.MainActionBar;

/**
 * Created by Dexing on 2015/8/22.
 */
public class ContactsFragment extends BaseFragment implements View.OnClickListener {

    private FloatingActionButtonL fab_addFriend;
    private MainActionBar actionbar_contacts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, null, false);
        fab_addFriend = (FloatingActionButtonL) view.findViewById(R.id.fabButton);
        fab_addFriend.setDrawableIcon(getResources().getDrawable(R.mipmap.add_user));
        fab_addFriend.setBackgroundColor(getResources().getColor(R.color.blue));
        fab_addFriend.setOnClickListener(this);

        actionbar_contacts = (MainActionBar) view.findViewById(R.id.actionbar_contacts);
        actionbar_contacts.setMainTitle(R.string.tab_contact);
        actionbar_contacts.setMapIconEnable(true);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabButton:
                this.getActivity().startActivity(new Intent(this.getActivity(), AddFriendsActivity.class));
                break;
        }
    }
}
