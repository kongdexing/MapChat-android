package cn.gdeveloper.mapchat.common;

import android.content.Context;

import cn.gdeveloper.mapchat.http.HttpVisitor;
import cn.gdeveloper.mapchat.http.WebResponse;
import cn.gdeveloper.mapchat.http.download.DownloaderImpl;
import cn.gdeveloper.mapchat.http.download.IDownloader;

/**
 * Created by Administrator on 2015/7/5.
 */
public class MapChatHttpService {

    private static MapChatHttpService mInstance = new MapChatHttpService();
    private final HttpVisitor HttpProxy = new HttpVisitor();
    /**
     * 资源下载
     */
    private IDownloader Downloader;
    private IMapChatUserService userService = new MapChatUserServiceImpl(HttpProxy);

    public static MapChatHttpService getInstance() {
        return mInstance;
    }

    public void init(Context context) {
        Downloader = new DownloaderImpl(context);
    }

    public IDownloader getDownloader() {
        return Downloader;
    }

    public void register(String email, String username, String mobile, String password, WebResponse response) {
        userService.register(email, username, mobile, password, response);
    }

    public void login(String loginname, String password, WebResponse response) {
        userService.login(loginname, password, response);
    }

    public void search(String searchKey, WebResponse response) {
        userService.search(searchKey, response);
    }
}
