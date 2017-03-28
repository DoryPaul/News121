package com.example.yyz.news121.bean;

/**
 * Created by yyz on 2017/2/9.
 */
public class TabDetailBean {

    /**
     * id : 3
     * blengqiudui : 金州勇士
     * weizhi : 小前锋
     * name : 凯文-杜兰特
     * playericonurl : http://china.nba.com/media/img/players/head/260x190/201142.png
     * rank : 1
     * data : 25.8
     */

    private String id;
    private String blengqiudui;
    private String weizhi;
    private String name;
    private String playericonurl;
    private int rank;
    private String data;

    /**
     * id : 3
     * blengqiudui : 金州勇士
     * weizhi : 小前锋
     * name : 凯文-杜兰特
     * playericonurl : http://china.nba.com/media/img/players/head/260x190/201142.png
     * data : 25.8
     */



    public TabDetailBean(String id, String blengqiudui, String weizhi, String name, String playericonurl, String data) {
        setId(id);
        setPlayericonurl(playericonurl);
        setWeizhi(weizhi);
        setName(name);
        setBlengqiudui(blengqiudui);
        setData(data);

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBlengqiudui() {
        return blengqiudui;
    }

    public void setBlengqiudui(String blengqiudui) {
        this.blengqiudui = blengqiudui;
    }

    public String getWeizhi() {
        return weizhi;
    }

    public void setWeizhi(String weizhi) {
        this.weizhi = weizhi;
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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
