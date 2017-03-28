package com.example.yyz.news121.bean;

/**
 * Created by yyz on 2017/2/21.
 */
public class WorkerBean {

    /**
     * id : 1
     * name : 文辉祥
     * zhiwei : 主教练
     * salary : 2000
     * year : 3
     */

    private String id;
    private String name;
    private String zhiwei;
    private String salary;
    private String year;
    public WorkerBean(String id, String name, String zhiwei, String salary, String year){
        setId(id);
        setName(name);
        setZhiwei(zhiwei);
        setSalary(salary);
        setYear(year);

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

    public String getZhiwei() {
        return zhiwei;
    }

    public void setZhiwei(String zhiwei) {
        this.zhiwei = zhiwei;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "WorkerBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", zhiwei='" + zhiwei + '\'' +
                ", salary='" + salary + '\'' +
                ", year='" + year + '\'' +
                '}';
    }
}
