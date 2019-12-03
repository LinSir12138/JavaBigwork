package javabean;

/*
*       考试对应的 Javabean 对象
* */


import java.sql.Timestamp;

public class Exam {
    private String examName;        // 本场考试名称
    private String paperName;       // 本场考试对应的试卷名称
    private String studentNumber;       // 参加考试的学生数量
    private Timestamp startTime;       // 考试开始时间
    private Timestamp endTime;          // 考试结束时间

    // 无参构造函数
    public Exam() {

    }

    public Exam(String examName, String paperName, String studentNumber, Timestamp startTime, Timestamp endTime) {
        this.examName = examName;
        this.paperName = paperName;
        this.studentNumber = studentNumber;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
}
