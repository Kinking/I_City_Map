package com.example.huangzhiyuan.i_city_mapdemo.bean;

import java.io.Serializable;

/**
 * Created by huangzhiyuan on 2017/3/19.
 */
public class Moment {
    private Integer mId;
    private Integer uId;
    private String userNickname;
    private String momentContent;
    private String mUpTime;
    private Double longitude;
    private Double latitude;

    public Moment(){
        super();
    }

    public Moment(Integer mId, Integer uId, String userNickName,String momentContent, String mUpTime, Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.mId = mId;
        this.momentContent = momentContent;
        this.mUpTime = mUpTime;
        this.uId = uId;
        this.userNickname = userNickName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public String getMomentContent() {
        return momentContent;
    }

    public void setMomentContent(String momentContent) {
        this.momentContent = momentContent;
    }

    public String getmUpTime() {
        return mUpTime;
    }

    public void setmUpTime(String mUpTime) {
        this.mUpTime = mUpTime;
    }

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public String getUserNickName() {
        return userNickname;
    }

    public void setUserNickName(String userNickName) {
        this.userNickname = userNickName;
    }
}
