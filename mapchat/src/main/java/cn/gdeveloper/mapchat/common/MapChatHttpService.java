package cn.gdeveloper.mapchat.common;

import cn.gdeveloper.mapchat.http.HttpVisitor;
import cn.gdeveloper.mapchat.http.WebResponse;

/**
 * Created by Administrator on 2015/7/5.
 */
public class MapChatHttpService {

    private static MapChatHttpService mInstance = new MapChatHttpService();
    private final HttpVisitor HttpProxy = new HttpVisitor();
    private IMapChatUserService userService = new MapChatUserServiceImpl(HttpProxy);

    public static MapChatHttpService getInstance(){
        return mInstance;
    }

    public void register(String email, String username, String mobile, String password,WebResponse response){
        userService.register(email,username,mobile,password,response);
    }

    public void login(String loginname,String password,WebResponse response){
        userService.login(loginname,password,response);
    }

    public void search(String searchKey,WebResponse response){
        userService.search(searchKey,response);
    }
}
