package cn.gdeveloper.mapchat.http.impl;

import cn.gdeveloper.mapchat.http.request.IResponseListener;

public interface IMapChatUserService {

    void register(String email, String username, String mobile, String password, IResponseListener listener);

    void login(String loginName, String password, IResponseListener listener);

    void search(String searchKey, IResponseListener listener);

    void addFriend(String fromId, String toId, String content, IResponseListener listener);

    void deleteFriendRequest(String toId, IResponseListener listener);
}
