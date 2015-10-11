package cn.gdeveloper.mapchat.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import cn.gdeveloper.mapchat.R;
import cn.gdeveloper.mapchat.view.LoadingView;

/**
 *
 */
public class LoadingDialog extends Dialog{

    private LoadingView loadingView;

    public LoadingDialog(Context context) {
        super(context, R.style.WinDialog);
        setContentView(R.layout.de_ui_dialog_loading);
        loadingView = (LoadingView) findViewById(R.id.loadView);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
    public void setText(String s) {
        if (loadingView != null) {
            loadingView.setLoadingText(s);
        }
    }

    public void setText(int res) {
        setText(getContext().getResources().getString(res));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            return false;
        }
        return super.onTouchEvent(event);
    }
}
