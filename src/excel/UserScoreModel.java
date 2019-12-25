package excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import javax.sql.rowset.BaseRowSet;

/**
 *      导出的学生成绩的 Excel 表格模型，这里比较有意思的是用到了注解来实现，和平常的类不一样
 *      只是简单的封装，不需要构造方法，只需要有 get 和 set 方法
 *      2019年12月24日20:02:05
 *      可是，最终还是失败了，因为 JAR 包冲突了，网上找不着完整的 JAR 包，Maven有不会。
 *      所以只能换一种方式了，利用 List 嵌套存放Excel需要的数据。
 * */

/**
 *      需要继承 BaseRowModel 类
 * */
public class UserScoreModel extends BaseRowModel {

    @ExcelProperty(value = "ID", index = 0)
    private String id;

    @ExcelProperty(value = "学生姓名", index = 1)
    private String userName;

    @ExcelProperty(value = "考试名称", index = 2)
    private String examName;

    @ExcelProperty(value = "学生答案", index = 3)
    private String userAnswer;

    @ExcelProperty(value = "标准答案", index = 4)
    private String standardAnswer;

    @ExcelProperty(value = "学生得分", index = 5)
    private String userScore;

    @ExcelProperty(value = "试卷总分", index = 6)
    private String totalScore;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getStandardAnswer() {
        return standardAnswer;
    }

    public void setStandardAnswer(String standardAnswer) {
        this.standardAnswer = standardAnswer;
    }

    public String getUserScore() {
        return userScore;
    }

    public void setUserScore(String userScore) {
        this.userScore = userScore;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }
}
