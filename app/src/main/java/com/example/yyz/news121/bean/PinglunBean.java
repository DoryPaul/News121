package com.example.yyz.news121.bean;

/**
 * Created by yyz on 2017/2/25.
 */
public class PinglunBean {

    /**
     * neirong : 不错
     * username : ying
     */

    private String neirong;
    private String username;

    public PinglunBean(String neirong, String username) {
        setNeirong(neirong);
        setUsername(username);
    }

    public String getNeirong() {
        return neirong;
    }

    public void setNeirong(String neirong) {
        this.neirong = neirong;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
