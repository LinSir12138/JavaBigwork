package javabean;

/**
 *          考试结果对应的 javaBean 对象
 *           没被用到过，后来发现实际上不用 javabean 对象存储数据也是可以的，用 工具类 （static）就行
 *          2019年12月2日20:49:09
 * */

public class ExamResult {
    private String userName;            // 用户名
    private String examName;            // 考试名称
    private String userAnswer;          // 学生答案
    private String standardAnswer;      // 标准答案
    private String userScore;           // 学生得分
    private String totalScore;      // 试卷总分

    /**
    * @Description: 无参构造函数
    * @Param: []
    * @return:
    * @Author: 林凯
    * @Date: 2019/12/2
    */
    public ExamResult() {
    }

    public ExamResult(String userName, String examName, String userAnswer, String standardAnswer, String userScore, String totalScore) {
        this.userName = userName;
        this.examName = examName;
        this.userAnswer = userAnswer;
        this.standardAnswer = standardAnswer;
        this.userScore = userScore;
        this.totalScore = totalScore;
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
