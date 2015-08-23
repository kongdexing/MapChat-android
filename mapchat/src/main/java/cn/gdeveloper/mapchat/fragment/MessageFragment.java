package cn.gdeveloper.mapchat.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.gdeveloper.mapchat.R;
import cn.gdeveloper.mapchat.view.MainActionBar;

/**
 * Created by Dexing on 2015/8/17.
 */
public class MessageFragment extends BaseFragment {

    private MainActionBar actionbar_message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, null, false);
        actionbar_message = (MainActionBar)view.findViewById(R.id.actionbar_message);
        actionbar_message.setMainTitle(R.string.tab_message);
        return view;
    }

}
