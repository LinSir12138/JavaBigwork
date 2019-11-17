package javabean;

import java.sql.Timestamp;

public class MyPaper {
    private String title;       // 试卷编号（标题）
    private Integer subjectNumber;      // 所包含的 试题数量
    private String subjectTitle;        // 所包含的 试题编号（标题）  的汇总，用 -  分隔
    private Timestamp timestamp;        // 最后修改时间

    public MyPaper() {}

    public MyPaper(String title, Integer subjectNumber, String subjectTitle, Timestamp timestamp) {
        this.title = title;
        this.subjectNumber = subjectNumber;
        this.subjectTitle = subjectTitle;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSubjectNumber() {
        return subjectNumber;
    }

    public void setSubjectNumber(Integer subjectNumber) {
        this.subjectNumber = subjectNumber;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
