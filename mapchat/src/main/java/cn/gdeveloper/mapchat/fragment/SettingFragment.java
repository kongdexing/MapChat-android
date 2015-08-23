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
public class SettingFragment extends BaseFragment {

    private MainActionBar actionbar_setting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, null, false);

        actionbar_setting = (MainActionBar)view.findViewById(R.id.actionbar_setting);
        actionbar_setting.setMainTitle(R.string.tab_setting);

        return view;
    }

}
