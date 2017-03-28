package com.example.yyz.news121.bean;

/**
 * Created by yyz on 2017/2/20.
 */
public class AnalyseBean {

    /**
     * qiuyuanpicurl : http://nbachina.qq.com/media/img/players/head/260x190/101108.png
     * name : 克里斯-保罗
     * weizhi : 控球后卫
     * qiudui : 洛杉矶快船
     * gongzi :  2147 万美元
     * score : 17.6
     * assist : 9.6
     * block : 0.2
     * rebound : 5.2
     * steal : 2.3
     */

    private String qiuyuanpicurl;
    private String name;
    private String weizhi;
    private String qiudui;
    private String gongzi;
    private String score;
    private String assist;
    private String block;
    private String rebound;
    private String steal;

    public AnalyseBean(String qiuyuanpicurl, String name, String weizhi, String qiudui, String gongzi, String score, String assist,
                       String block, String rebound, String steal){
        setQiuyuanpicurl(qiuyuanpicurl);
        setName(name);
        setWeizhi(weizhi);
        setQiudui(qiudui);
        setGongzi(gongzi);
        setScore(score);
        setAssist(assist);
        setBlock(block);
        setRebound(rebound);
        setSteal(steal);
    }
    public String getQiuyuanpicurl() {
        return qiuyuanpicurl;
    }

    public void setQiuyuanpicurl(String qiuyuanpicurl) {
        this.qiuyuanpicurl = qiuyuanpicurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeizhi() {
        return weizhi;
    }

    public void setWeizhi(String weizhi) {
        this.weizhi = weizhi;
    }

    public String getQiudui() {
        return qiudui;
    }

    public void setQiudui(String qiudui) {
        this.qiudui = qiudui;
    }

    public String getGongzi() {
        return gongzi;
    }

    public void setGongzi(String gongzi) {
        this.gongzi = gongzi;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getAssist() {
        return assist;
    }

    public void setAssist(String assist) {
        this.assist = assist;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getRebound() {
        return rebound;
    }

    public void setRebound(String rebound) {
        this.rebound = rebound;
    }

    public String getSteal() {
        return steal;
    }

    public void setSteal(String steal) {
        this.steal = steal;
    }

    @Override
    public String toString() {
        return "AnalyseBean{" +
                "qiuyuanpicurl='" + qiuyuanpicurl + '\'' +
                ", name='" + name + '\'' +
                ", weizhi='" + weizhi + '\'' +
                ", qiudui='" + qiudui + '\'' +
                ", gongzi='" + gongzi + '\'' +
                ", score='" + score + '\'' +
                ", assist='" + assist + '\'' +
                ", block='" + block + '\'' +
                ", rebound='" + rebound + '\'' +
                ", steal='" + steal + '\'' +
                '}';
    }
}
