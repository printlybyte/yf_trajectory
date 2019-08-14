package com.caitiaobang.core.app.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;


@Entity
public class GreendaoLocationBean {
    @Id(autoincrement = true)
    private Long id;
    private String lat;
    private String lng;
    private String address;
    private String time;
    private String accuracy;
    private String provider;
    private String speed;
    @Generated(hash = 1282932880)
    public GreendaoLocationBean(Long id, String lat, String lng, String address,
            String time, String accuracy, String provider, String speed) {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.time = time;
        this.accuracy = accuracy;
        this.provider = provider;
        this.speed = speed;
    }
    @Generated(hash = 1406004995)
    public GreendaoLocationBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getLat() {
        return this.lat;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }
    public String getLng() {
        return this.lng;
    }
    public void setLng(String lng) {
        this.lng = lng;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getAccuracy() {
        return this.accuracy;
    }
    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }
    public String getProvider() {
        return this.provider;
    }
    public void setProvider(String provider) {
        this.provider = provider;
    }
    public String getSpeed() {
        return this.speed;
    }
    public void setSpeed(String speed) {
        this.speed = speed;
    }
}
