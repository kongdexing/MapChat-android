package cn.gdeveloper.mapchat.common;

import cn.gdeveloper.mapchat.http.WebResponse;

/**
 * Created by Administrator on 2015/7/5.
 */
public interface IMapChatUserService {

    void register(String email, String username, String mobile, String password,WebResponse response);

    void login(String loginname,String password,WebResponse response);
}
