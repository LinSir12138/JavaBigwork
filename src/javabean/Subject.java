package javabean;

/*
*       2019年10月27日15:07:43
*       题目对应的 javabean 对象
*       主要存储的类型有：
*       "题目编号     id       int                  ------------>  不需要存储了，由MySQL自动生成，转换为存储title
*       "题目标题"      title    varchar
*       "题目类型",     type    varchar
*       选项A         optionA     varchar
*       选项B         optionB     varchar
*       选项C         optionC     varchar
*       选项D         optionD     varchar
*       "题目内容",     content     varchar
*       "正确答案", "   answer      varchar
        * 题目备注"     remarks     varchar
* */

import java.sql.Timestamp;

public class Subject {
    private String title;     // 自动赋值，自动添加
    private String type;
    private String content;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String answer;
    private String judge;
    private String remarks;
    private Timestamp changeTime;       // sql 包  里面的时间戳

    public Subject() {
    }

    public Subject(String title, String type, String content, String optionA, String optionB, String optionC, String optionD, String answer, String judge, String remarks, Timestamp changeTime) {
        this.title = title;
        this.type = type;
        this.content = content;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.answer = answer;
        this.judge = judge;
        this.remarks = remarks;
        this.changeTime = changeTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getJudge() {
        return judge;
    }

    public void setJudge(String judge) {
        this.judge = judge;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Timestamp getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Timestamp changeTime) {
        this.changeTime = changeTime;
    }
}
