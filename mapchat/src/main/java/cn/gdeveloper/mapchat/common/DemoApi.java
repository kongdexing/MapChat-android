package cn.gdeveloper.mapchat.common;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import cn.gdeveloper.mapchat.app.MapChatContext;
import cn.gdeveloper.mapchat.http.impl.HttpHost;
import cn.gdeveloper.mapchat.model.Friends;
import cn.gdeveloper.mapchat.model.Groups;
import cn.gdeveloper.mapchat.model.Status;
import cn.gdeveloper.mapchat.model.User;
import cn.gdeveloper.mapchat.parser.GsonArrayParser;
import cn.gdeveloper.mapchat.parser.GsonParser;
import me.add1.network.AbstractHttpRequest;
import me.add1.network.ApiCallback;
import me.add1.network.ApiReqeust;
import me.add1.network.AuthType;
import me.add1.network.BaseApi;
import me.add1.network.NetworkManager;

/**
 * demo api 请求，需要设置cookie，否则会提示 “user not login”
 * 此处是 Demo 的接口，跟融云 SDK 没有关系，此处仅为示例代码，展示 App 的逻辑
 */
public class DemoApi extends BaseApi {

    public DemoApi(Context context) {
        super(NetworkManager.getInstance(), context);
    }


    /**
     * 登录 demo server
     *
     * @param email
     * @param password
     * @param callback
     * @return
     */
    public AbstractHttpRequest<User> login(String email, String password, ApiCallback<User> callback) {

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("email", email));
        nameValuePairs.add(new BasicNameValuePair("password", password));

        ApiReqeust<User> apiReqeust = new DefaultApiReqeust<User>(ApiReqeust.POST_METHOD, URI.create(HttpHost.HOST + HttpHost.LOGIN), nameValuePairs, callback);
        AbstractHttpRequest<User> httpRequest = apiReqeust.obtainRequest(new GsonParser<User>(User.class), null, null);
        NetworkManager.getInstance().requestAsync(httpRequest);
        return httpRequest;
    }

    /**
     * demo server 注册新用户
     *
     * @param email
     * @param username
     * @param password
     * @param callback
     * @return
     */
    public AbstractHttpRequest<Status> register(String email, String username, String mobile, String password, ApiCallback<Status> callback) {

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("Email", email));
        nameValuePairs.add(new BasicNameValuePair("UserName", username));
        nameValuePairs.add(new BasicNameValuePair("Password", password));
        nameValuePairs.add(new BasicNameValuePair("Phone", mobile));

        ApiReqeust<Status> apiReqeust = new DefaultApiReqeust<Status>(ApiReqeust.POST_METHOD, URI.create(HttpHost.HOST + HttpHost.REGISTER), nameValuePairs, callback);
        AbstractHttpRequest<Status> httpRequest = apiReqeust.obtainRequest(new GsonParser<Status>(Status.class), null, null);
        NetworkManager.getInstance().requestAsync(httpRequest);

        return httpRequest;

    }

    /**
     * demo server 注册新用户
     *
     * @param username
     * @param callback
     * @return
     */
    public AbstractHttpRequest<Status> updateProfile(String username, ApiCallback<Status> callback) {

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("username", username));

        return null;

    }

    /**
     * 登陆成功后获得token
     *
     * @param callback
     * @return
     */
    public AbstractHttpRequest<User> getToken(ApiCallback<User> callback) {
        return null;
    }


    /**
     * demo server 获取好友
     * 获取所有好友信息
     * @param callback
     * @return
     */

    public AbstractHttpRequest<Friends> getFriends(ApiCallback<Friends> callback) {

//        ApiReqeust<Friends> apiReqeust = new DefaultApiReqeust<Friends>(ApiReqeust.GET_METHOD, URI.create(HttpHost.HOST + HttpHost.DEMO_FRIENDS), callback);
//        AbstractHttpRequest<Friends> httpRequest = apiReqeust.obtainRequest(new GsonParser<Friends>(Friends.class), mAuthType);
//        NetworkManager.getInstance().requestAsync(httpRequest);

        return null;

    }

    /**
     * demo server 加入群组
     *
     * @param callback
     * @return
     */

    public AbstractHttpRequest<Status> joinGroup(String username, ApiCallback<Status> callback) {

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("id", username + ""));

        ApiReqeust<Status> apiReqeust = new DefaultApiReqeust<Status>(ApiReqeust.GET_METHOD, URI.create(HttpHost.HOST + HttpHost.DEMO_JOIN_GROUP), nameValuePairs, callback);
        AbstractHttpRequest<Status> httpRequest = apiReqeust.obtainRequest(new GsonParser<Status>(Status.class), null, null);
        NetworkManager.getInstance().requestAsync(httpRequest);

        return httpRequest;

    }

    /**
     * demo server 退出群组
     *
     * @param callback
     * @return
     */

    public AbstractHttpRequest<Status> quitGroup(String username, ApiCallback<Status> callback) {

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("id", username + ""));

        ApiReqeust<Status> apiReqeust = new DefaultApiReqeust<Status>(ApiReqeust.GET_METHOD, URI.create(HttpHost.HOST + HttpHost.DEMO_QUIT_GROUP), nameValuePairs, callback);
        AbstractHttpRequest<Status> httpRequest = apiReqeust.obtainRequest(new GsonParser<Status>(Status.class), null, null);
        NetworkManager.getInstance().requestAsync(httpRequest);

        return httpRequest;

    }


    /**
     * demo server 获取所有群组列表
     *
     * @param callback
     * @return
     */
    public AbstractHttpRequest<Groups> getAllGroups(ApiCallback<Groups> callback) {

        ApiReqeust<Groups> apiReqeust = new DefaultApiReqeust<Groups>(ApiReqeust.GET_METHOD, URI.create(HttpHost.HOST + HttpHost.DEMO_GET_ALL_GROUP), callback);
        AbstractHttpRequest<Groups> httpRequest = apiReqeust.obtainRequest(new GsonParser<Groups>(Groups.class), mAuthType);
        NetworkManager.getInstance().requestAsync(httpRequest);

        return httpRequest;

    }

    /**
     * demo server 获取我的群组列表
     *
     * @param callback
     * @return
     */
    public AbstractHttpRequest<Groups> getMyGroups(ApiCallback<Groups> callback) {

        ApiReqeust<Groups> apiReqeust = new DefaultApiReqeust<Groups>(ApiReqeust.GET_METHOD, URI.create(HttpHost.HOST + HttpHost.DEMO_GET_MY_GROUP), callback);
        AbstractHttpRequest<Groups> httpRequest = apiReqeust.obtainRequest(new GsonParser<Groups>(Groups.class), mAuthType);
        NetworkManager.getInstance().requestAsync(httpRequest);

        return httpRequest;

    }

    /**
     * 通过用户名搜索用户
     *
     * @param callback
     * @return
     */

    public AbstractHttpRequest<Friends> searchUserByUserName(String username, ApiCallback<Friends> callback) {

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("username", username));

        ApiReqeust<Friends> apiReqeust = new DefaultApiReqeust<Friends>(ApiReqeust.GET_METHOD, URI.create(HttpHost.HOST + HttpHost.DEMO_SEARCH_NAME), nameValuePairs, callback);
        AbstractHttpRequest<Friends> httpRequest = apiReqeust.obtainRequest(new GsonParser<Friends>(Friends.class), null, null);
        NetworkManager.getInstance().requestAsync(httpRequest);

        return httpRequest;

    }

    /**
     * 获取好友列表
     * 获取添加过的好友信息
     * @param callback
     * @return
     */

    public AbstractHttpRequest<Friends> getNewFriendlist(ApiCallback<Friends> callback) {

        ApiReqeust<Friends> apiReqeust = new DefaultApiReqeust<Friends>(ApiReqeust.GET_METHOD, URI.create(HttpHost.HOST + HttpHost.DEMO_GET_FRIEND), callback);
        AbstractHttpRequest<Friends> httpRequest = apiReqeust.obtainRequest(new GsonParser<Friends>(Friends.class), null, null);
        NetworkManager.getInstance().requestAsync(httpRequest);

        return httpRequest;

    }


    /**
     * 发好友邀请
     *
     * @param callback
     * @return
     */

    public AbstractHttpRequest<User> sendFriendInvite(String userid, String message, ApiCallback<User> callback) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("id", userid + ""));
        nameValuePairs.add(new BasicNameValuePair("message", message));

        ApiReqeust<User> apiReqeust = new DefaultApiReqeust<User>(ApiReqeust.POST_METHOD, URI.create(HttpHost.HOST + HttpHost.DEMO_REQUEST_FRIEND), nameValuePairs, callback);
        AbstractHttpRequest<User> httpRequest = apiReqeust.obtainRequest(new GsonParser<>(User.class), null, null);
        NetworkManager.getInstance().requestAsync(httpRequest);

        return httpRequest;

    }

    /**
     * 发好友邀请
     *
     * @param callback
     * @return
     */

    public AbstractHttpRequest<ArrayList<User>> sendFriesdfndInvite(String userid, ApiCallback<ArrayList<User>> callback) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("id", userid + ""));

        ApiReqeust<ArrayList<User>> apiReqeust = new DefaultApiReqeust<ArrayList<User>>(ApiReqeust.GET_METHOD, URI.create(HttpHost.HOST + HttpHost.DEMO_REQUEST_FRIEND), nameValuePairs, callback);
        AbstractHttpRequest<ArrayList<User>> httpRequest = apiReqeust.obtainRequest(new GsonArrayParser<>(new TypeToken<ArrayList<User>>() {
        }), null, null);
        NetworkManager.getInstance().requestAsync(httpRequest);

        return httpRequest;

    }

    /**
     * demo server 删除好友
     *
     * @param callback
     * @return
     */

    public AbstractHttpRequest<Status> deletefriends(String id, ApiCallback<Status> callback) {

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("id", id + ""));

        ApiReqeust<Status> apiReqeust = new DefaultApiReqeust<Status>(ApiReqeust.POST_METHOD, URI.create(HttpHost.HOST + HttpHost.DEMO_DELETE_FRIEND), nameValuePairs, callback);
        AbstractHttpRequest<Status> httpRequest = apiReqeust.obtainRequest(new GsonParser<Status>(Status.class), null, null);
        NetworkManager.getInstance().requestAsync(httpRequest);

        return httpRequest;

    }

    /**
     * demo server 处理好友请求好友
     *
     * @param callback
     * @return
     */

    public AbstractHttpRequest<Status> processRequestFriend(String id, String isaccess, ApiCallback<Status> callback) {

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("id", id + ""));
        nameValuePairs.add(new BasicNameValuePair("is_access", isaccess));

        ApiReqeust<Status> apiReqeust = new DefaultApiReqeust<Status>(ApiReqeust.POST_METHOD, URI.create(HttpHost.HOST + HttpHost.DEMO_PROCESS_REQUEST_FRIEND), nameValuePairs, callback);
        AbstractHttpRequest<Status> httpRequest = apiReqeust.obtainRequest(new GsonParser<Status>(Status.class), null, null);
        NetworkManager.getInstance().requestAsync(httpRequest);

        return httpRequest;

    }


    AuthType mAuthType = new AuthType() {

        @Override
        public void signRequest(HttpRequest httpRequest, List<NameValuePair> nameValuePairs) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
            if (MapChatContext.getInstance().getSharedPreferences().getString("DEMO_COOKIE", null) != null) {
                httpRequest.addHeader("cookie", MapChatContext.getInstance().getSharedPreferences().getString("DEMO_COOKIE", null));
            }

        }
    };


}
