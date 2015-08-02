package cn.gdeveloper.mapchat.activity;

import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import cn.gdeveloper.mapchat.R;

public abstract class BaseActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);// 使得音量键控制媒体声音
        getSupportActionBar().setLogo(R.mipmap.de_bar_logo);//actionbar 添加logo
        setContentView(setContentViewResId());
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        initView();
        initData();
    }

    /**
     * 加载layout
     *
     * @return
     */
    protected abstract int setContentViewResId();

    /**
     * 初始化view
     */
    protected abstract void initView();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * init data
     */
    protected abstract void initData();
}
