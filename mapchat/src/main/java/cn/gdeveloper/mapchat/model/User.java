package cn.gdeveloper.mapchat.model;

import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import cn.gdeveloper.mapchat.app.MapChatContext;
import cn.gdeveloper.mapchat.utils.SharedPreferencesUtil;
import me.add1.common.ParcelUtils;

/**
 * Entity mapped to table USER.
 */
public class User implements Parcelable, Serializable {

    private String userId;
    private String loginName;
    private String password;
    /**
     * 返回码
     */
    private int code;
    /**
     * 错误码 message
     */
    private String message;
    private String token;

    private static User mUser = new User();

    public User() {

    }

    public static User getInstance() {
        return mUser;
    }

    public User(Parcel in) {
        userId = ParcelUtils.readFromParcel(in);
        password = ParcelUtils.readFromParcel(in);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, userId);
        ParcelUtils.writeToParcel(dest, password);
    }

    public void init() {
        userId = MapChatContext.getInstance().getSharedPreferences().getString(SharedPreferencesUtil.USER_ID, "");
        loginName = MapChatContext.getInstance().getSharedPreferences().getString(SharedPreferencesUtil.USER_LOGINNAME, "");
        password = MapChatContext.getInstance().getSharedPreferences().getString(SharedPreferencesUtil.USER_PASSWORD, "");
        token = MapChatContext.getInstance().getSharedPreferences().getString(SharedPreferencesUtil.USER_TOKEN, "");
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        SharedPreferences.Editor editor = MapChatContext.getInstance().getSharedPreferences().edit();
        editor.putString(SharedPreferencesUtil.USER_ID, userId);
        editor.apply();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        SharedPreferences.Editor editor = MapChatContext.getInstance().getSharedPreferences().edit();
        editor.putString(SharedPreferencesUtil.USER_PASSWORD, password);
        editor.apply();
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
        SharedPreferences.Editor editor = MapChatContext.getInstance().getSharedPreferences().edit();
        editor.putString(SharedPreferencesUtil.USER_LOGINNAME, loginName);
        editor.apply();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        SharedPreferences.Editor editor = MapChatContext.getInstance().getSharedPreferences().edit();
        editor.putString(SharedPreferencesUtil.USER_TOKEN, token);
        editor.apply();
    }
}