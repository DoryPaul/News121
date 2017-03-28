package com.example.yyz.news121.bean;

/**
 * appname:News121
 * Author:  应义正
 * CreateDate: 2017/3/13
 * Function:
 */
public class PlayerBean {

    /**
     * id : 1
     * name : 克里斯-保罗
     * playericonurl : http://nbachina.qq.com/media/img/players/head/260x190/101108.png
     * qiuduiid : 1
     * shengao : 183cm
     * shengti : 1985-05-06
     * tizhong : 79.4KG
     * weizhi : 控球后卫
     */

    private String id;
    private String name;
    private String playericonurl;
    private String qiuduiid;
    private String shengao;
    private String shengti;
    private String tizhong;
    private String weizhi;
    private String gongzi;

    public PlayerBean(String id, String playericonurl, String name, String weizhi, String qiuduiid, String shengao, String tizhong, String shengti,String gongzi) {
        setId(id);
        setPlayericonurl(playericonurl);
        setName(name);
        setWeizhi(weizhi);
        setQiuduiid(qiuduiid);
        setShengao(shengao);
        setTizhong(tizhong);
        setShengti(shengti);
        setGongzi(gongzi);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlayericonurl() {
        return playericonurl;
    }

    public void setPlayericonurl(String playericonurl) {
        this.playericonurl = playericonurl;
    }

    public String getQiuduiid() {
        return qiuduiid;
    }

    public void setQiuduiid(String qiuduiid) {
        this.qiuduiid = qiuduiid;
    }

    public String getShengao() {
        return shengao;
    }

    public void setShengao(String shengao) {
        this.shengao = shengao;
    }

    public String getShengti() {
        return shengti;
    }

    public void setShengti(String shengti) {
        this.shengti = shengti;
    }

    public String getTizhong() {
        return tizhong;
    }

    public void setTizhong(String tizhong) {
        this.tizhong = tizhong;
    }

    public String getWeizhi() {
        return weizhi;
    }

    public void setWeizhi(String weizhi) {
        this.weizhi = weizhi;
    }

    public String getGongzi() {
        return gongzi;
    }

    public void setGongzi(String gongzi) {
        this.gongzi = gongzi;
    }
}
