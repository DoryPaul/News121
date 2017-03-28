package com.example.yyz.news121.bean;

/**
 * Created by yyz on 2017/2/20.
 */
public class QiutanBean {
    String name,university,age,weizhi,score;

    public QiutanBean(String name, String university, String age, String weizhi, String score ){
        setName(name);
        setUniversity(university);
        setAge(age);
        setWeizhi(weizhi);
        setScore(score);
    }
    public String getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public String getScore() {
        return score;
    }

    public String getUniversity() {
        return university;
    }

    public String getWeizhi() {
        return weizhi;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public void setWeizhi(String weizhi) {
        this.weizhi = weizhi;
    }

    @Override
    public String toString() {
        return "QiutanBean{" +
                "name='" + name + '\'' +
                ", university='" + university + '\'' +
                ", age='" + age + '\'' +
                ", weizhi='" + weizhi + '\'' +
                ", score='" + score + '\'' +
                '}';
    }
}
