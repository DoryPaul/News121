package com.example.yyz.news121.bean;

/**
 * Created by yyz on 2017/2/19.
 */
public class MatchBean {

    /**
     * team1 : 洛杉矶快船
     * team2 : 洛杉矶湖人
     * team1score : 0
     * team2score : 0
     * team1icon : http://mat1.gtimg.com/sports/nba/logo/1602/12.png
     * team2icon : http://mat1.gtimg.com/sports/nba/logo/1602/13.png
     */

    private String team1,matchid;
    private String team2;
    private String team1score;
    private String team2score;
    private String team1icon;
    private String team2icon;

    public MatchBean(String matchid, String team1, String team2, String team1score, String team2score, String team1icon, String team2icon){
        setMatchid(matchid);
        setTeam1(team1);
        setTeam2(team2);
        setTeam1score(team1score);
        setTeam2score(team2score);
        setTeam1icon(team1icon);
        setTeam2icon(team2icon);
    }

    public String getMatchid() {
        return matchid;
    }

    public void setMatchid(String matchid) {
        this.matchid = matchid;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getTeam1score() {
        return team1score;
    }

    public void setTeam1score(String team1score) {
        this.team1score = team1score;
    }

    public String getTeam2score() {
        return team2score;
    }

    public void setTeam2score(String team2score) {
        this.team2score = team2score;
    }

    public String getTeam1icon() {
        return team1icon;
    }

    public void setTeam1icon(String team1icon) {
        this.team1icon = team1icon;
    }

    public String getTeam2icon() {
        return team2icon;
    }

    public void setTeam2icon(String team2icon) {
        this.team2icon = team2icon;
    }

    @Override
    public String toString() {
        return "MatchBean{" +
                "team1='" + team1 + '\'' +
                ", team2='" + team2 + '\'' +
                ", team1score='" + team1score + '\'' +
                ", team2score='" + team2score + '\'' +
                ", team1icon='" + team1icon + '\'' +
                ", team2icon='" + team2icon + '\'' +
                '}';
    }
}
