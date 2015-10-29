package cn.gdeveloper.mapchat.http.impl;

import android.content.Context;

import cn.gdeveloper.mapchat.http.download.DownloaderImpl;
import cn.gdeveloper.mapchat.http.download.IDownloader;
import cn.gdeveloper.mapchat.http.request.HttpRequestHelper;
import cn.gdeveloper.mapchat.http.request.IResponseListener;

/**
 * Created by Administrator on 2015/7/5.
 */
public class MapChatHttpService {

    private static MapChatHttpService mInstance = new MapChatHttpService();
    /**
     * 资源下载
     */
    private IDownloader Downloader;
    private IMapChatUserService userService = new MapChatUserServiceImpl();

    public static MapChatHttpService getInstance() {
        return mInstance;
    }

    public void init(Context context) {
        HttpRequestHelper.getInstance().init(context);
        Downloader = new DownloaderImpl(context);
    }

    public IDownloader getDownloader() {
        return Downloader;
    }

    public void register(String email, String username, String mobile, String password, IResponseListener listener) {
        userService.register(email, username, mobile, password, listener);
    }

    public void login(String loginname, String password, IResponseListener listener) {
        userService.login(loginname, password, listener);
    }

    public void search(String searchKey, IResponseListener listener) {
        userService.search(searchKey, listener);
    }

    public void addFriend(String fromId, String toId, String content, IResponseListener listener) {
        userService.addFriend(fromId, toId, content, listener);
    }

    public void deleteFriendRequest(String toId, IResponseListener listener) {
        userService.deleteFriendRequest(toId, listener);
    }

}
