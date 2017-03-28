package com.example.yyz.news121.bean;

/**
 * Created by yyz on 2017/2/23.
 */
public class EastBean {

    /**
     * teamname : 克里夫兰骑士
     * teamiconurl : http://mat1.gtimg.com/sports/nba/logo/1602/5.png
     * win : 24
     * lost : 7
     * local : EAST
     */

    private String teamname;
    private String teamiconurl;
    private String win;
    private String lost;
    private String local;

    public EastBean(String teamname, String teamiconurl, String win, String lost, String local) {
        setLocal(local);
        setLost(lost);
        setTeamiconurl(teamiconurl);
        setTeamname(teamname);
        setWin(win);
    }

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public String getTeamiconurl() {
        return teamiconurl;
    }

    public void setTeamiconurl(String teamiconurl) {
        this.teamiconurl = teamiconurl;
    }

    public String getWin() {
        return win;
    }

    public void setWin(String win) {
        this.win = win;
    }

    public String getLost() {
        return lost;
    }

    public void setLost(String lost) {
        this.lost = lost;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }
}
