package com.example.yyz.news121.bean;

/**
 * Created by yyz on 2017/2/21.
 */
public class AllBean {


    /**
     * id : 1
     * title : 得分
     * dataurl : http://192.168.133.2/NBA/player/defen.php
     */

    private String id;
    private String title;
    private String dataurl;

    public AllBean(String id, String title, String dataurl) {
        setId(id);
        setDataurl(dataurl);
        setTitle(title);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDataurl() {
        return dataurl;
    }

    public void setDataurl(String dataurl) {
        this.dataurl = dataurl;
    }

    @Override
    public String toString() {
        return "AllBean{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", dataurl='" + dataurl + '\'' +
                '}';
    }
}
