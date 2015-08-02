package cn.gdeveloper.mapchat.model;

import android.text.TextUtils;

import java.io.Serializable;

import cn.gdeveloper.mapchat.helper.DePinyinHelper;
import me.add1.resource.Resource;

/**
 * Created by Bob on 2015/3/24.
 */
public class Friend implements Serializable, IFilterModel {

    private String ID;
    private String userName;
    private String usernamePinyin;
    private String portrait; // 头像
    private char searchKey;
    private String birthday;
    private String sex;
    private String phone;
    private String email;
    private Resource portraitResource;
    private boolean isSelected = false;
    private boolean isAdd = false;

    public Friend() {

    }

    private final void createSeachKey(String nickname) {

        if (TextUtils.isEmpty(nickname)) {
            return;
        }

        usernamePinyin = DePinyinHelper.getInstance().getPinyins(nickname, "");

        if (usernamePinyin != null && usernamePinyin.length() > 0) {
            char key = usernamePinyin.charAt(0);
            if (key >= 'A' && key <= 'Z') {

            } else if (key >= 'a' && key <= 'z') {
                key -= 32;
            } else if (key == '★') {
                key = '★';
            } else {
                key = '#';
            }
            searchKey = key;
        } else {
            searchKey = '#';
        }
    }

    public String getID() {
        return ID;
    }

    public void setID(String id) {
        this.ID = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        createSeachKey(userName);
    }

    public String getUsernamePinyin() {
        return usernamePinyin;
    }

    public void setUsernamePinyin(String usernamePinyin) {
        this.usernamePinyin = usernamePinyin;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public char getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(char searchKey) {
        this.searchKey = searchKey;
    }

    public Resource getPortraitResource() {
        return portraitResource;
    }

    public void setPortraitResource(Resource portraitResource) {
        this.portraitResource = portraitResource;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public String getFilterKey() {
        return getUserName() + getUsernamePinyin();
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean isAdd) {
        this.isAdd = isAdd;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
