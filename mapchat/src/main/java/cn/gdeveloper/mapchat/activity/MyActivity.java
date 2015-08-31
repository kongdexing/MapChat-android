package cn.gdeveloper.mapchat.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import cn.gdeveloper.mapchat.R;
import cn.gdeveloper.mapchat.fragment.BaseFragment;
import cn.gdeveloper.mapchat.fragment.ContactsFragment;
import cn.gdeveloper.mapchat.fragment.MessageFragment;
import cn.gdeveloper.mapchat.fragment.SettingFragment;
import cn.gdeveloper.mapchat.view.viewpagerindicator.IconPagerAdapter;
import cn.gdeveloper.mapchat.view.viewpagerindicator.IconTabPageIndicator;

public class MyActivity extends BaseActionBarActivity {

    private ViewPager mViewPager;
    private IconTabPageIndicator mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        initViews();
    }

    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setFitsSystemWindows(true);
        mIndicator = (IconTabPageIndicator) findViewById(R.id.indicator);
        List<BaseFragment> fragments = initFragments();
        FragmentAdapter adapter = new FragmentAdapter(fragments, getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mIndicator.setViewPager(mViewPager);
        mViewPager.setCurrentItem(1);
    }

    private List<BaseFragment> initFragments() {
        List<BaseFragment> fragments = new ArrayList<BaseFragment>();

        MessageFragment messageFragment = new MessageFragment();
        messageFragment.setTitle(getResources().getString(R.string.tab_message));
        messageFragment.setIconId(R.drawable.tab_record_selector);
        fragments.add(messageFragment);

        ContactsFragment contactFragment = new ContactsFragment();
        contactFragment.setTitle(getResources().getString(R.string.tab_contact));
        contactFragment.setIconId(R.drawable.tab_user_selector);
        fragments.add(contactFragment);

        SettingFragment settingFragment = new SettingFragment();
        settingFragment.setTitle(getResources().getString(R.string.tab_setting));
        settingFragment.setIconId(R.drawable.tab_setting_selector);
        fragments.add(settingFragment);

        return fragments;
    }

    class FragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
        private List<BaseFragment> mFragments;

        public FragmentAdapter(List<BaseFragment> fragments, FragmentManager fm) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int i) {
            return mFragments.get(i);
        }

        @Override
        public int getIconResId(int index) {
            return mFragments.get(index).getIconId();
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragments.get(position).getTitle();
        }
    }
}
