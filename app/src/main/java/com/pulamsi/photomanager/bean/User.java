package com.pulamsi.photomanager.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-01-04
 * Time: 10:26
 * FIXME
 */
@Entity
public class User {

    @Id
    private Long id;

    @NotNull
    private String mid;

    @NotNull
    private String name;

    @NotNull
    private boolean isLogin;

    @NotNull
    private String imgUrl;

    @NotNull
    private int userType;// 0默认 1代理商 2经销商 3管理员

    private String sex;

    private String birthDay;

    private String autograph;

    private String rongyunToken;//融云Token



    @Generated(hash = 290772109)
    public User(Long id, @NotNull String mid, @NotNull String name, boolean isLogin,
            @NotNull String imgUrl, int userType, String sex, String birthDay,
            String autograph, String rongyunToken) {
        this.id = id;
        this.mid = mid;
        this.name = name;
        this.isLogin = isLogin;
        this.imgUrl = imgUrl;
        this.userType = userType;
        this.sex = sex;
        this.birthDay = birthDay;
        this.autograph = autograph;
        this.rongyunToken = rongyunToken;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public String getMid() {
        return this.mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public boolean getIsLogin() {
        return this.isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRongyunToken() {
        return rongyunToken;
    }

    public void setRongyunToken(String rongyunToken) {
        this.rongyunToken = rongyunToken;
    }

    public String getAutograph() {
        return autograph;
    }

    public void setAutograph(String autograph) {
        this.autograph = autograph;
    }


    public String getBirthDay() {
        return this.birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", mid='" + mid + '\'' +
                ", name='" + name + '\'' +
                ", isLogin=" + isLogin +
                ", imgUrl='" + imgUrl + '\'' +
                ", userType=" + userType +
                ", sex='" + sex + '\'' +
                ", birthDay='" + birthDay + '\'' +
                ", autograph='" + autograph + '\'' +
                ", rongyunToken='" + rongyunToken + '\'' +
                '}';
    }
}
