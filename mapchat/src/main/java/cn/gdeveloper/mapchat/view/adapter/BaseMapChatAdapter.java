package cn.gdeveloper.mapchat.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

/**
 * Created by Dexing on 2015/9/2.
 */
public abstract class BaseMapChatAdapter extends BaseAdapter {

    protected Context mContext;
    protected LayoutInflater inflater;

    public BaseMapChatAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    public BaseMapChatAdapter() {
    }

}
