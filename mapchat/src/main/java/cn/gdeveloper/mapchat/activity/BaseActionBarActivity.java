package cn.gdeveloper.mapchat.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import cn.gdeveloper.mapchat.R;
import cn.gdeveloper.mapchat.common.MapChatHandler;
import cn.gdeveloper.mapchat.ui.LoadingDialog;
import cn.gdeveloper.mapchat.ui.SystemBarTintManager;

public class BaseActionBarActivity extends FragmentActivity {

    protected SystemBarTintManager mTintManager;
    protected MapChatHandler mHandler;
    private LoadingDialog mDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        mHandler = getMessageHandler();
        mTintManager = new SystemBarTintManager(this);
        mTintManager.setStatusBarTintEnabled(true);
        mTintManager.setStatusBarTintResource(R.color.default_bg);

        mDialog = new LoadingDialog(this);
    }

    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /** 每个Activity Override该方法来响应业务逻辑处理 */
    protected MapChatHandler getMessageHandler() {
        return new MapChatHandler(this) ;
    }

    public void showDialog() {
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public void hideDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }
}
