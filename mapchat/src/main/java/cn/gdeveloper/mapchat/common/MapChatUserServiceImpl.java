package cn.gdeveloper.mapchat.common;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.gdeveloper.mapchat.http.BaseProxy;
import cn.gdeveloper.mapchat.http.ErrorText;
import cn.gdeveloper.mapchat.http.HttpVisitor;
import cn.gdeveloper.mapchat.http.MapChatMessageID;
import cn.gdeveloper.mapchat.http.WebResponse;
import cn.gdeveloper.mapchat.model.Friend;
import cn.gdeveloper.mapchat.model.Result;
import cn.gdeveloper.mapchat.model.User;

/**
 * Created by Administrator on 2015/7/5.
 */
public class MapChatUserServiceImpl extends BaseProxy implements IMapChatUserService, MapChatMessageID {

    private final String TAG = "MapChatHttp";
    final Result mResult = new Result();

    protected MapChatUserServiceImpl(HttpVisitor service) {
        super(service);
    }

    @Override
    public void register(String email, String username, String mobile, String password, final WebResponse response) {
        final Result error = mResult.cloneResult();
        error.setCode("-1");
        String param = null;
        try {
            JSONObject json = new JSONObject();
            json.put("Email", email);
            json.put("UserName", username);
            json.put("Password", password);
            json.put("Phone", mobile);
            param = json.toString();
        } catch (Exception e) {
            error.setValue(ErrorText.ERROR_BUILD_PARAMETER);
            response.sendMessage(MSG_MEMBER_REGISTER_FAILED, error);
            return;
        }

        final StringBuilder url = new StringBuilder(256);
        url.append(HttpHost.HOST + HttpHost.REGISTER);
        Log.i(TAG, "url " + url + "  param  " + param);
        httpService.sendHttpPostResponseByteArray(url.toString(), param, new WebResponse(response) {
            @Override
            public void dispatchResponse(Object value) {
                super.dispatchResponse(value);
                final Result result = mResult.cloneResult();
                if (analyseResult(result, value) == CODE_OK) {
                    sendMessage(MSG_MEMBER_REGISTER_SUCCESS, result);
                } else {
                    sendMessage(MSG_MEMBER_REGISTER_FAILED, result);
                }
                // dispatch
                response.dispatchResponse(result);
            }
        });
    }

    @Override
    public void login(final String loginname, final String password, final WebResponse response) {
        final StringBuilder url = new StringBuilder(256);
        url.append(HttpHost.HOST + HttpHost.LOGIN);
        url.append("/" + loginname + "/" + password);

        httpService.sendHttpGetResponseByteArray(url.toString(), new WebResponse(response) {
            @Override
            public void dispatchResponse(Object value) {
                super.dispatchResponse(value);
                final Result result = mResult.cloneResult();
                if (analyseResult(result, value) == CODE_OK) {
                    try {
                        String str = result.getValue();
                        JSONObject json = new JSONObject(str);
                        User user = new User();
                        user.setUserId(json.getString("userId"));
                        user.setToken(json.getString("token"));
                        user.setLoginname(loginname);
                        user.setPasswd(password);
                        sendMessage(MSG_MEMBER_LOGIN_SUCCESS, user);
                    } catch (Exception ex) {
                        sendMessage(MSG_MEMBER_LOGIN_FAILED, ErrorText.ERROR_PARSER_JSON);
                    }
                } else {
                    sendMessage(MSG_MEMBER_LOGIN_FAILED, result.getValue());
                }
                // dispatch
                response.dispatchResponse(result);
            }
        });
    }

    @Override
    public void search(String searchKey, final WebResponse response) {
        final StringBuilder url = new StringBuilder(256);
        url.append(HttpHost.HOST + HttpHost.SEARCH);
        url.append("/" + searchKey);

        httpService.sendHttpGetResponseByteArray(url.toString(), new WebResponse(response) {
            @Override
            public void dispatchResponse(Object value) {
                super.dispatchResponse(value);
                final Result result = mResult.cloneResult();
                if (analyseResult2(result, value) == CODE_OK) {
                    try {
                        JSONObject jsonVal = new JSONObject(result.getValue());
//                        int total = jsonVal.getInt("total");
//                        if (total == 0) {
//                            response.sendMessage(MSG_SEARCH_SUCCESS, ErrorText.ERROR_SEARCH_EMPTY);
//                            return;
//                        }
                        JSONArray jsonArray = jsonVal.getJSONArray("list_ret_friend");
                        int size = jsonArray.length();
                        ArrayList<Friend> list_friend = new ArrayList<Friend>();
                        for (int i = 0; i < size; i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            Friend friend = new Friend();
                            friend.setID(obj.getString("ID"));
                            friend.setUserName(obj.getString("userName"));
                            friend.setPortrait(obj.getString("portrait"));
                            friend.setBirthday(obj.getString("birthday"));
                            friend.setSex(obj.getInt("sex"));
                            list_friend.add(friend);
                        }
                        response.sendMessage(MSG_SEARCH_SUCCESS, list_friend);
                    } catch (Exception ex) {
                        Log.i(TAG, "search error :" + ex.getMessage());
                        response.sendMessage(MSG_SEARCH_FAILED, ErrorText.ERROR_PARSER_JSON);
                    }
                } else {
                    response.sendMessage(MSG_SEARCH_FAILED, result.getValue());
                }
            }
        });
    }
}
