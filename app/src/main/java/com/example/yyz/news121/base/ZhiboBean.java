package com.example.yyz.news121.base;

/**
 * Created by yyz on 2017/2/25.
 */
public class ZhiboBean {

    /**
     * content : 比赛即将开始
     * time : 2017-02-25 17:10:41
     * name : 封义
     */

    private String content;
    private String time;
    private String name;

    public ZhiboBean(String time, String content, String name) {
        setContent(content);
        setName(name);
        setTime(time);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
