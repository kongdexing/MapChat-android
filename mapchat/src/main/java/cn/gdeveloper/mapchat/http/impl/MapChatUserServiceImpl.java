package cn.gdeveloper.mapchat.http.impl;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import cn.gdeveloper.mapchat.http.request.ErrorText;
import cn.gdeveloper.mapchat.http.request.HttpRequestHelper;
import cn.gdeveloper.mapchat.http.request.IResponseListener;
import cn.gdeveloper.mapchat.http.request.MapChatMessageID;
import cn.gdeveloper.mapchat.http.request.RequestListener;
import cn.gdeveloper.mapchat.model.Friend;
import cn.gdeveloper.mapchat.model.Result;
import cn.gdeveloper.mapchat.model.User;
import io.rong.imlib.model.UserInfo;

/**
 * Created by Administrator on 2015/7/5.
 */
public class MapChatUserServiceImpl extends BaseProxy implements IMapChatUserService, MapChatMessageID {

    private final String TAG = "MapChatHttp";
    final Result mResult = new Result();

    @Override
    public void register(String email, String username, String mobile, String password, final IResponseListener listener) {
        String param = null;
        try {
            JSONObject json = new JSONObject();
            json.put("Email", email);
            json.put("UserName", username);
            json.put("Password", password);
            json.put("Phone", mobile);
            param = json.toString();
        } catch (Exception e) {
            listener.onRequestResponse(MSG_MEMBER_REGISTER_FAILED, ErrorText.ERROR_BUILD_PARAMETER);
            return;
        }

        final StringBuilder url = new StringBuilder(256);
        url.append(HttpHost.HOST + HttpHost.REGISTER);
        Log.i(TAG, "url " + url + "  param  " + param);
        HttpRequestHelper.getInstance().sendHttpPostRequest(url.toString(), param, new RequestListener(listener) {
            @Override
            public void onResponse(int code, Object value) {
                super.onResponse(code, value);
                final Result result = mResult.cloneResult();
                analyseResult(result, value);
                switch (code) {
                    case HttpURLConnection.HTTP_OK:
                        if (listener != null) {
                            listener.onRequestResponse(MSG_MEMBER_REGISTER_SUCCESS, result.getValue());
                        }
                        break;
                    default:
                        if (listener != null) {
                            listener.onRequestResponse(MSG_MEMBER_REGISTER_FAILED, result.getValue());
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void login(final String loginName, final String password, final IResponseListener listener) {
        final StringBuilder url = new StringBuilder(256);
        url.append(HttpHost.HOST + HttpHost.LOGIN);
        url.append("/" + loginName + "/" + password);

        HttpRequestHelper.getInstance().sendHttpGetRequest(url.toString(), null, new RequestListener(listener) {
            @Override
            public void onResponse(int code, Object value) {
                super.onResponse(code, value);
                final Result result = mResult.cloneResult();
                switch (code) {
                    case HttpURLConnection.HTTP_OK:
                        if (analyseResult(result, value) == CODE_OK) {
                            try {
                                JSONObject json = new JSONObject(result.getValue());
                                User.getInstance().setUserId(json.getString("userId"));
                                User.getInstance().setToken(json.getString("token"));
                                User.getInstance().setLoginName(loginName);
                                User.getInstance().setPassword(password);
                                User.getInstance().setLoginState(true);
                                if (listener != null) {
                                    listener.onRequestResponse(MSG_MEMBER_LOGIN_SUCCESS, null);
                                }
                            } catch (Exception ex) {
                                if (listener != null) {
                                    listener.onRequestResponse(MSG_MEMBER_LOGIN_FAILED, ErrorText.ERROR_PARSER_JSON);
                                }
                            }
                        } else {
                            if (listener != null) {
                                listener.onRequestResponse(MSG_MEMBER_LOGIN_FAILED, result.getValue());
                            }
                        }
                        break;
                    default:
                        if (listener != null) {
                            listener.onRequestResponse(MSG_MEMBER_LOGIN_FAILED, result.getValue());
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void search(String searchKey, final IResponseListener listener) {
        final StringBuilder url = new StringBuilder(256);
        url.append(HttpHost.HOST + HttpHost.SEARCH);
        url.append("/" + searchKey+"/"+ User.getInstance().getUserId());

        HttpRequestHelper.getInstance().sendHttpGetRequest(url.toString(), null, new RequestListener(listener) {
            @Override
            public void onResponse(int code, Object value) {
                super.onResponse(code, value);
                final Result result = mResult.cloneResult();
                switch (code) {
                    case HttpURLConnection.HTTP_OK:
                        if (analyseJSONObject(result, value) == CODE_OK) {
                            try {
                                JSONObject jsonVal = result.getJSONObject();
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
                                    friend.setFriendState(obj.getInt("friendState"));
                                    list_friend.add(friend);
                                }
                                if (listener != null) {
                                    listener.onRequestResponse(MSG_SEARCH_SUCCESS, list_friend);
                                }
                            } catch (Exception ex) {
                                if (listener != null) {
                                    listener.onRequestResponse(MSG_SEARCH_FAILED, ErrorText.ERROR_PARSER_JSON);
                                }
                            }
                        } else {
                            if (listener != null) {
                                listener.onRequestResponse(MSG_SEARCH_FAILED, result.getValue());
                            }
                        }
                        break;
                    default:
                        if (listener != null) {
                            listener.onRequestResponse(MSG_SEARCH_FAILED, result.getValue());
                        }
                        break;
                }
            }
        });

    }

    @Override
    public void addFriend(String fromId, String toId, String content, final IResponseListener listener) {
        String param = null;
        try {
            JSONObject json = new JSONObject();
            json.put("FromUserID", fromId);
            json.put("ToUserID", toId);
            json.put("ObjectName", "");
            json.put("Content", content);
            param = json.toString();
        } catch (Exception e) {
            listener.onRequestResponse(MSG_ADDFRIEND_FAILED, ErrorText.ERROR_BUILD_PARAMETER);
            return;
        }

        final StringBuilder url = new StringBuilder(256);
        url.append(HttpHost.HOST + HttpHost.AddFriend);

        HttpRequestHelper.getInstance().sendHttpPostRequest(url.toString(), param, new RequestListener(listener) {
            @Override
            public void onResponse(int code, Object value) {
                super.onResponse(code, value);
                final Result result = mResult.cloneResult();
                switch (code) {
                    case HttpURLConnection.HTTP_OK:
                        if (listener != null) {
                            listener.onRequestResponse(MSG_ADDFRIEND_SUCCESS, "");
                        }
                        break;
                    default:
                        if (listener != null) {
                            listener.onRequestResponse(MSG_ADDFRIEND_FAILED, result.getValue());
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void deleteFriendRequest(String toId, IResponseListener listener) {

    }
}
