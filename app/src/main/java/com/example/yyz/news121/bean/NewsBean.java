package com.example.yyz.news121.bean;

/**
 * Created by yyz on 2017/2/19.
 */
public class NewsBean {

    /**
     * title : 31分13篮板对飙阿杜
     * desc : 在圣诞大战这样的大舞台，詹姆斯又展现了100%努力的自己打球有多可怕，出战超过40分钟的他交出了31分13篮板4助攻的答卷
     * time : 2017-01-02 16:45:35
     * picurl : http://img1.gtimg.com/chinanba/pics/hv1/224/76/2172/141253904_small2.jpg
     */

    private  String newsid;
    private String title;
    private String desc;
    private String time;
    private String picurl;
    private  boolean dian;

    public NewsBean(String newsid, String title, String desc, String time, String picurl, boolean dian){
        setNewsid(newsid);
        setTitle(title);
        setDesc(desc);
        setTime(time);
        setPicurl(picurl);
        setDian(dian);
    }

    public String getNewsid() {
        return newsid;
    }

    public void setNewsid(String newsid) {
        this.newsid = newsid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public boolean isDian() {
        return dian;
    }

    public void setDian(boolean dian) {
        this.dian = dian;
    }

    @Override
    public String toString() {
        return "NewsBean{" +
                "title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", time='" + time + '\'' +
                ", picurl='" + picurl + '\'' +
                '}';
    }
}
