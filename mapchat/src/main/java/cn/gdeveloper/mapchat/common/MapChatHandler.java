package cn.gdeveloper.mapchat.common;

import android.content.Context;
import android.os.Message;
import android.os.Handler;

/**
 * Created by Dexing on 2015/8/30.
 */
public class MapChatHandler extends Handler {

    private Context mContext ;

    /** 构造函数 */
    public MapChatHandler(Context ctx){
        this.mContext = ctx ;
    }

    /** 覆盖此方法 */
    @Override
    public void handleMessage(Message msg) {

    }

    public Context getContext(){
        return mContext ;
    }

}
