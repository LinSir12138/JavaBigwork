/*
 * Created by JFormDesigner on Sat Nov 30 15:05:13 CST 2019
 */

package examministration;

/**
 *      实现的基本原理
 *      1.将题目问题和选项拼接在一起，然后给每个按钮标号，从1开始，对应着保存所有题目的二维数组
 *      2. 不同的点击事件，根据按钮上的文本内容的不同，显示不同的 题目内容
 * */

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;

import info.clearthought.layout.*;
import jdbc.ExamResultJDBC;
import jdbc.SubjectJDBC;

/**
 * @author Lin Kai
 */
public class StartExam extends JFrame {
    private JFrame fatherFrame;     // 父窗口的引用
    private String examName;            // 考试名称
    private String[] paperTitles;        // 考试对应的试卷里面的  题目标题对应的 字符串数组
    private String[][] subjectInfomation;       // 具体的题目信息，包含所有题目的二维数组
    private ArrayList<JButton> buttonlist;              // 用来保存动态创建的按钮的 ArrayList
    private boolean[] isFinished;            // 判断题目是否已经完成的数组，初始值为false，如果完成了，则为true
    private ButtonGroup buttonGroup1;           // 单选按钮组(将所有 单选按钮 加入到这个组内)
//    private ButtonGroup buttonGroup2;           // 复选按钮组（将所有 复选按钮 加入到这个组内） ,多选按钮不需要添加到按钮组中
    private ButtonGroup buttonGroup3;           // 判断对错按钮组
    // 当前所在题目（按钮）编号，下标从 0 开始，初始值为1 (特别注意：作为数组的元素下标是，需要将 1 才能正确对应)
    private int currentButtonNumber;
    
    private long startTime;     // 构造方法中初始化
    private long endTime;       // 构造方法中初始化
    private int userScore;      // 学生的得分， 在 initSubject 中初始化
    private int totalScore;     // 试卷总分
    private String[] userAnswer;            // 用户输入的答案，根据用户选择的 按钮来确定
    private String[] standardAnswer;        // 标准答案
    private boolean isFinishedExam;      // 是否完成了考试，作为一个标志，不同的值，调用题目按钮点击事件执行的结果布偶听

    /**
    * @Description: 构造方法
    * @Param: [examName, paperTitles, startTime, endTime]
    * @return:
    * @Author: 林凯
    * @Date: 2019/11/30
    */
    public StartExam(JFrame fatherFrame, String examName, String[] paperTitles, long startTime, long endTime) {
        this.fatherFrame = fatherFrame;
        this.examName = examName;
        this.paperTitles = paperTitles;
        this.startTime = startTime;
        this.endTime = endTime;
        /**
         *      // 1. 初始化题目信息  2. 初始化 isFinished 数组全部为 false   首先将该试卷中包含的所有题目一次性全部读取出来，方便以后进行查看，不需要每次都在数据库查询
         * */
        initSubject();
        initComponents();       // 初始化一些基本的界面（自动生成的代码）
        myInit();           //  初始化动态加载的界面，例如题目按钮（数量不确定）

    }

    /**
    * @Description: 初始化题目信息，获得试卷中所有题目的具体信息（以二维数组）的形式
    * @Param: []
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/30
    */
    private void initSubject() {
        // 1. 初始化 全部题目信息，一次性从数据库中读取所有的题目，避免以后重复从数据库中读取数据（浪费时间，空间）
        SubjectJDBC subjectJDBC = new SubjectJDBC();
        subjectInfomation = subjectJDBC.getSubjectByTitles(paperTitles);
        // 2. 初始化 isFinished 数组全部为 false，表示一开始所有题目都没完成
        isFinished = new boolean[paperTitles.length];
        for (int i = 0; i < paperTitles.length; i++) {
            isFinished[i] = false;
        }
        // 3. 初始化 currentButtonNumber，表示当前所在题目（按钮）编号，下标从 0 开始，初始值为1
        currentButtonNumber = 1;
        userAnswer = new String[paperTitles.length];        // 初始化学生答案数组
        userScore = 0;      // 初始化学生得分为 0 分
        totalScore = paperTitles.length * 5;     // 初始化总分,每道题目固定为5分
        standardAnswer = new String[paperTitles.length];       // 初始化 标准答案 数组
        for (int i = 0; i < standardAnswer.length; i++) {
            standardAnswer[i] = subjectInfomation[i][8];
            System.out.println("标准答案：" + standardAnswer[i]);
        }
        isFinishedExam = false;      // 初始化值为 flase ，即一开始没有完成考试
    }

    /**
    * @Description: 动态初始化加载的界面，例如题目按钮（数量不确定） ，倒计时等等
    * @Param: []
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/30
    */
    private void myInit() {
        int buttonNumber = paperTitles.length;      // 按钮的数量等于 String 数组的长度
        buttonlist = new ArrayList<>();
        /**
         *      1. 动态的创建 n 个按钮，并把这n 个按钮添加到 ArrayList中，（n就是试卷中试题的数量）
         * */
        /*
        *       假设一行放置 5 个 JButton
        * */
        int row = 0;        // 该按钮需要放在第几行 （下标从 0 开始）
        int col = 0;        // 该按钮需要放在第几列 （下标从 0 开始）
        for (int i = 0; i < buttonNumber; i++) {
            JButton button = new JButton(String.valueOf(i + 1));
            button.setFont(new Font("微软雅黑", Font.BOLD, 22));
            buttonlist.add(button);
            //  因为 i 是从0开始，所以 i + 1 用来
            //  表示是第几个按钮，i = 0时，i+1 = 1，表示第一个按钮（下面几行代码就是找规律）
            if ((i + 1) % 5 == 0) {
                col = 4;
            } else {
                col = (i + 1) % 5 - 1;        // 下标从0开始，计算列时需要减一
            }
            if ((i + 1) % 5 == 0) {     // 分情况，被5整除，说明在一行的末尾
                row = (i + 1) / 5 - 1;
                System.out.println("i = " + i + " , " + "row = " + row);
            } else {
                row = (i + 1) / 5;        // 下标从 0 开始，计算行时，不需要减一（找规律）
            }
            panelTable.add(button, new TableLayoutConstraints(col, row, col, row, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        }
//        System.out.println("按钮的数量" + buttonlist.size());      // 测试用


        /**
        *      2. 动态地添加点击事件
        * */
        for (int i = 0; i < paperTitles.length; i++) {
            JButton tempButton = buttonlist.get(i);
            tempButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("点击事件被调用");
                    myButtonAction(e, isFinishedExam);
                }
            });
        }

        /**
         *      3. 同时设置初始值（即一开始就是选择了第一题，第一个按钮是被选择状态）
         * */
        JButton firstButton = buttonlist.get(0);        // 获得第一个按钮对象，并使用 firstButton 指向它
        currentButtonNumber = Integer.valueOf(firstButton.getText().toString());
        firstButton.setSelected(true);      // 设置第一个按钮是被选中状态
        firstButton.setBackground(new Color(255, 255, 0));
        StringBuilder firstText = new StringBuilder();          // 下面是设置第一个按钮（第一题）的对应的初始界面
        firstText.append(firstButton.getText().toString() + ". ");
        firstText.append(subjectInfomation[currentButtonNumber - 1][3] + "\n");      // 设置文本框为  题目的问题
        // 只有单选题和多选题有需要显示选项
        if (subjectInfomation[currentButtonNumber - 1][2].equals("单选题") || subjectInfomation[currentButtonNumber - 1][2].equals("多选题")) {
            firstText.append("A. " + subjectInfomation[currentButtonNumber - 1][4] + "\n");      // 选项按钮
            firstText.append("B. " +  subjectInfomation[currentButtonNumber - 1][5] + "\n");
            firstText.append("C. " + subjectInfomation[currentButtonNumber - 1][6] + "\n");
            firstText.append("D. " + subjectInfomation[currentButtonNumber - 1][7] + "\n");
        }

        textPaneContent.setText(firstText.toString());

        showButton(subjectInfomation[Integer.parseInt(firstButton.getText().toString())][2]);       // 选择那些按钮需要显示

        /**
         *      初始化所以的选项状态都是未选中
         * */
        radioButtonA.setSelected(false);
        radioButtonB.setSelected(false);
        radioButtonC.setSelected(false);
        radioButtonD.setSelected(false);
        checkBoxA.setSelected(false);
        checkBoxB.setSelected(false);
        checkBoxC.setSelected(false);
        checkBoxD.setSelected(false);
        radioButtonTrue.setSelected(false);
        radioButtonFalse.setSelected(false);

        /**
         *      设置考试名称，考试倒计时
         * */
        labelExamName.setText(examName);
        long remainTime = endTime - System.currentTimeMillis();

        new Thread(new ExamCountdown(labelTime, endTime)).start();




    }

    /**
    * @Description: 按钮的点击事件，单独封装出来，方便调用，被 myInit() 调用
    * @Param: [event]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/30
    */
    public void myButtonAction(ActionEvent event, boolean isFinishedExam) {
        JButton tempButton = (JButton) event.getSource();
        // 获得当前按钮的编号(将按钮上的文字内容转换为 int 类型)
        currentButtonNumber = Integer.parseInt(tempButton.getText().toString());
        // 注意：按钮编号从 1 开始，而数组下标从 0 开始
        String tempType = subjectInfomation[currentButtonNumber - 1][2];        // 获得当前题目的临时 类型

        /**
         *      设置题目的具体内容，这里把题目的问题和选项拼接在一起
         * */
        StringBuilder tempText = new StringBuilder();
        tempText.append(tempButton.getText() + ". ");
        tempText.append(subjectInfomation[currentButtonNumber - 1][3] + "\n");     // 添加题目的问题，减一是因为按钮标号从1开始，而数组下标从0开始

        // 只有单选题和多选题有需要显示选项
        if (subjectInfomation[currentButtonNumber - 1][2].equals("单选题") || subjectInfomation[currentButtonNumber - 1][2].equals("多选题")) {
            tempText.append("A. " + subjectInfomation[currentButtonNumber - 1][4] + "\n");       // 添加选项 A
            tempText.append("B. " + subjectInfomation[currentButtonNumber - 1][5] + "\n");        // 添加选项 B
            tempText.append("C. " + subjectInfomation[currentButtonNumber - 1][6] + "\n");        // 添加选项 C
            tempText.append("D. " + subjectInfomation[currentButtonNumber - 1][7] + "\n");       // 添加选项 D
        }
        textPaneContent.setText(tempText.toString());
        textPaneContent.setCaretPosition(0);        // 让光标移动到开头位置


        /**
         *      如果题目已经完成，显示选择选项，如果没有完成，那么所以选项置为未选择状态
         * */
        if (isFinished[currentButtonNumber - 1]) {
            System.out.println("题目已经完成！");
            if (tempType.equals("单选题")) {
                if (userAnswer[currentButtonNumber - 1].equals("A")) {
                    radioButtonA.setSelected(true);
                } else if (userAnswer[currentButtonNumber - 1].equals("B")) {
                    radioButtonB.setSelected(true);
                } else if (userAnswer[currentButtonNumber - 1].equals("C")) {
                    radioButtonC.setSelected(true);
                } else if (userAnswer[currentButtonNumber - 1].equals("D")) {
                    radioButtonD.setSelected(true);
                }
            } else if (tempType.equals("多选题")) {
                char[] items = userAnswer[currentButtonNumber - 1].toCharArray();       // 转换成为字符数组
                for (int i = 0; i <items.length; i++) {
                    // 注意，这里不能用if~else，因为有可能全部的按钮都会被选上
                    if (items[i] == 'A') {
                        checkBoxA.setSelected(true);
                    } else if (items[i] == 'B') {
                        checkBoxB.setSelected(true);
                    } else if (items[i] == 'C') {
                        checkBoxC.setSelected(true);
                    } else if (items[i] == 'D') {
                        checkBoxD.setSelected(true);
                    }
                }
            } else if (tempType.equals("判断题")) {
                if (userAnswer[currentButtonNumber - 1].equals("正确")) {
                    radioButtonTrue.setSelected(true);
                } else if (userAnswer[currentButtonNumber - 1].equals("错误")) {
                    radioButtonFalse.setSelected(true);
                }
            }

        } else {
            System.out.println("题目未完成");

            if (tempType.equals("单选题")) {
                buttonGroup1.clearSelection();      // 设置按钮组中的所有按钮都是未选择状态
            } else if (tempType.equals("多选题")) {
                checkBoxA.setSelected(false);
                checkBoxB.setSelected(false);
                checkBoxC.setSelected(false);
                checkBoxD.setSelected(false);
            } else if (tempType.equals("判断题")) {
               buttonGroup3.clearSelection();       // 设置按钮组中的所有按钮都是未选择状态
            }

            // 还没有完成这道题目,设置所以选项为未选择状态

//            if (radioButtonA.isSelected()) {
//                System.out.println("A");
//                radioButtonA.setSelected(false);
//                radioButtonA.setVisible(true);
//                System.out.println(radioButtonA.isSelected());
//            } else if (radioButtonB.isSelected()) {
//                System.out.println("b");
//            }else if (radioButtonC.isSelected()) {
//
//                System.out.println("c");
//            }else if (radioButtonD.isSelected()) {
//                System.out.println("d");
//            }


//            buttonGroup1.getSelection();
//            ButtonModel buttonModel = buttonGroup1.getSelection();
//            buttonModel.setSelected(false);





        }

        /**
         *      设置题目的 选项按钮，选择哪些按钮来显示，单选还是多选
         * */
        showButton(tempType);       // 选择按钮显示

        /**
         *      设置按钮的颜色
         * */
        if (isFinishedExam == false) {
            for (int i = 0; i < buttonlist.size(); i++) {
                if (buttonlist.get(i).getText().toString().equals(tempButton.getText().toString())) {
                    // 只要选中了该题目，都显示为黄色
                    buttonlist.get(i).setBackground(new Color(255, 255, 0));
                } else if (isFinished[i]) {
                    //  如果该题目已经完成了，则显示为绿色
                    buttonlist.get(i).setBackground(new Color(0, 255, 0));
                } else {
                    // 如果该题目没有完成，且没有选中该题目，则设置为默认，传入的参数为null
                    buttonlist.get(i).setBackground(null);
                }
            }
        } else {
            // 当考试完成时，按钮颜色发生改变
            if (standardAnswer[currentButtonNumber - 1].equals(userAnswer[currentButtonNumber - 1])) {
                label3.setVisible(false);

            } else {

                if (subjectInfomation[currentButtonNumber - 1][2].equals("单选题") || subjectInfomation[currentButtonNumber - 1][2].equals("多选题")) {
                    label3.setText("正确答案是：" + standardAnswer[currentButtonNumber - 1]);
                    label3.setVisible(true);            // 设置可见
                } else if (subjectInfomation[currentButtonNumber - 1][2].equals("判断题")){
                    label3.setText("正确答案是：" + subjectInfomation[currentButtonNumber - 1][9]);
                    label3.setVisible(true);            // 设置可见
                }
            }

        }



    }

    /**
    * @Description: 根据传进来的 subjectType 选择那些按钮需要显示，哪些按钮不需要显示
    * @Param: []
    * @return: void
    * @Author: 林凯
    * @Date: 2019/12/1
    */
    public void showButton(String subjectType) {
        if (subjectType.equals("单选题")) {
            radioButtonA.setVisible(true);
            radioButtonB.setVisible(true);
            radioButtonC.setVisible(true);
            radioButtonD.setVisible(true);
            checkBoxA.setVisible(false);
            checkBoxB.setVisible(false);
            checkBoxC.setVisible(false);
            checkBoxD.setVisible(false);
            radioButtonTrue.setVisible(false);
            radioButtonFalse.setVisible(false);
        } else if (subjectType.equals("多选题")) {
            radioButtonA.setVisible(false);
            radioButtonB.setVisible(false);
            radioButtonC.setVisible(false);
            radioButtonD.setVisible(false);
            checkBoxA.setVisible(true);
            checkBoxB.setVisible(true);
            checkBoxC.setVisible(true);
            checkBoxD.setVisible(true);
            radioButtonTrue.setVisible(false);
            radioButtonFalse.setVisible(false);
        } else if (subjectType.equals("判断题")) {
            radioButtonA.setVisible(false);
            radioButtonB.setVisible(false);
            radioButtonC.setVisible(false);
            radioButtonD.setVisible(false);
            checkBoxA.setVisible(false);
            checkBoxB.setVisible(false);
            checkBoxC.setVisible(false);
            checkBoxD.setVisible(false);
            radioButtonTrue.setVisible(true);
            radioButtonFalse.setVisible(true);

        }

    }


    /**
    * @Description: 点击   “确定”   按钮对应的点击事件，将当前题目用户的选项临时保存
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/12/1
    */
    private void buttonSureMouseReleased(MouseEvent e) {
        if (isFinishedExam == true) {
            return;         //  如果考试已经完成，在点击就没有任何反应了
        }
        // TODO add your code here
        /**
         *      需要完成的功能：
         *      1. 更新 isFinished 数组对应的值为 true，表示当前题目已经完成了
         *      2. 根据用户的选项，临时保存用户的选项到 userAnswer 数组对应的地方
         *      3. 判断用户是否进行选择，如果没有选择直接点击的确定，提示用户首先要选择一个选项
         * */

        String tempType = subjectInfomation[currentButtonNumber - 1][2];        // 获得当前题目的题目类型
        isFinished[currentButtonNumber - 1] = true;     // 设置当前题目已经完成

        if (tempType.equals("单选题")) {
            if (radioButtonA.isSelected()) {
                userAnswer[currentButtonNumber - 1] = "A";
            } else if (radioButtonB.isSelected()) {
                userAnswer[currentButtonNumber - 1] = "B";
            } else if (radioButtonC.isSelected()) {
                userAnswer[currentButtonNumber - 1] = "C";
            } else if (radioButtonD.isSelected()) {
                userAnswer[currentButtonNumber - 1] = "D";
            } else {
                // 如果 4 个选项都没有选择，那么提示用户首先进行选择，然后在点击确定按钮
                JOptionPane.showMessageDialog(this, "请先选择一个选项！", "错误", JOptionPane.ERROR_MESSAGE);
            }
            System.out.println("userAnswer:" + userAnswer[currentButtonNumber - 1]);
        } else if (tempType.equals("多选题")) {
            StringBuilder stringBuilder = new StringBuilder();      // 因为多选题的答案有多个，所以需要使用 stringBuilder 进行拼接
            if (checkBoxA.isSelected()) {
                stringBuilder.append("A");
            }
            if (checkBoxB.isSelected()) {
                stringBuilder.append("B");
            }
            if (checkBoxC.isSelected()) {
                stringBuilder.append("C");
            }
            if (checkBoxD.isSelected()) {
                stringBuilder.append("D");
            }
            System.out.println();
           if (checkBoxA.isSelected() == false && checkBoxB.isSelected() == false && checkBoxC.isSelected() == false && checkBoxD.isSelected() == false){
                // 如果 4 个选项都没有选择，那么提示用户首先进行选择，然后在点击确定按钮
                JOptionPane.showMessageDialog(this, "请先选择一个选项！", "错误", JOptionPane.ERROR_MESSAGE);
            } else {
               // 如果至少选择了一个选项，则将选项结果写入 userAnswer 中
               userAnswer[currentButtonNumber - 1] = stringBuilder.toString();
               System.out.println("myChoose=" + stringBuilder.toString());
           }

        } else if (tempType.equals("判断题")) {
            if (radioButtonTrue.isSelected()) {
                userAnswer[currentButtonNumber - 1] = "正确";
            } else if (radioButtonFalse.isSelected()) {
                userAnswer[currentButtonNumber - 1] = "错误";
            } else {
                // 如果 2 个选项都没有选择，那么提示用户选择
                JOptionPane.showMessageDialog(this, "请先选择一个选项！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
    * @Description: 点击   “提交试卷”  按钮对应点击事件
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/12/2
    */
    private void buttonCommitMouseReleased(MouseEvent e) {
        // TODO add your code here
        /**
         *      需要实现的功能
         *      1. 如果用户没有答完所有的题目，提示用户
         *      2. 如果已经打完了所有的题目，将用户的答题信息保存到数据库中
         * */

        if (isFinishedExam == true) {
            JOptionPane.showMessageDialog(this, "已经提交过试卷了，不能再次提交！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        isFinishedExam = true;          // 设置标志为 true ，说明已经完成了考试
        boolean flag = true;            //  判断用户是否已经完成所有题目的标志
        for (int i = 0; i < isFinished.length; i++) {
            if (isFinished[i] == false) {
                flag = false;
            }
        }

        if (flag == false) {
            JOptionPane.showMessageDialog(this, "您还没有完成所有的题目！", "错误", JOptionPane.ERROR_MESSAGE);
            int result = JOptionPane.showConfirmDialog(this, "您还没答完全部题目，确定交卷吗？", "确定交卷吗？", 2);
            if (result == -1) {
                // 如何用户点击了  “取消”，则直接返回
                return;
            }
        }

        userScore =  getUserScore();

        /**
         *      将学生的考试数据保存到数据库中
         * */
        saveDate();

        /**
         *      在当前界面显示考试的具体信息，学生哪到题目做对了，哪道题目做错了，以及题目的备注信息
         * */
        showExamResult();

        /**
         *      弹出一个考试结果窗口，显示学生的得分
         * */
        ShowResult showResult = new ShowResult(this,totalScore, userScore);
        showResult.setVisible(true);
        this.setEnabled(false);     // 设置本窗体不能被编辑
    }

    /**
    * @Description: 在当前界面显示考试的具体信息，学生哪到题目做对了，哪道题目做错了，以及题目的备注信息
    * @Param: []
    * @return: void
    * @Author: 林凯
    * @Date: 2019/12/2
    */
    private void showExamResult() {
        labelScore.setVisible(true);        // 设置得分的标签为显示状态。
        labelScore.setText("总分为：" + totalScore + "  您的得分为：" + userScore);

        label1.setText("绿色表示做对了的题目");
        label2.setText("红色表示做错了的题目");
        // 按钮颜色发生改变
        JButton tempButton;
        for (int i = 0; i < standardAnswer.length; i++) {
            tempButton = buttonlist.get(i);
            if (subjectInfomation[i][2].equals("单选题") || subjectInfomation[i][2].equals("多选题")) {
                if (standardAnswer[i].equals(userAnswer[i])) {
                    tempButton.setBackground(new Color(0, 255, 0));
                } else {
                    tempButton.setBackground(new Color(255, 0, 0));
                }
            } else if (subjectInfomation[i][2].equals("判断题")){
                if (subjectInfomation[currentButtonNumber -1][9].equals(userAnswer[i])) {
                    tempButton.setBackground(new Color(0, 255, 0));
                } else {
                    tempButton.setBackground(new Color(255, 0, 0));
                }
            }

        }

        /**
         *      设置选项不能选中
         * */
        radioButtonA.setEnabled(false);
        radioButtonB.setEnabled(false);
        radioButtonC.setEnabled(false);
        radioButtonD.setEnabled(false);
        checkBoxA.setEnabled(false);
        checkBoxB.setEnabled(false);
        checkBoxC.setEnabled(false);
        checkBoxD.setEnabled(false);
        radioButtonTrue.setEnabled(false);
        radioButtonFalse.setEnabled(false);


        //   待完成
    }

    /**
    * @Description: 将学生的考试数据保存到数据库中，学生点击  “提交试卷”  后被调用
     *                在 buttonCommitMouseReleased() 方法中被调用
    * @Param: []
    * @return: void
    * @Author: 林凯
    * @Date: 2019/12/2
    */
    private void saveDate() {
        ExamResultJDBC.insertExamResult("linkai", examName, userAnswer, standardAnswer, String.valueOf(userScore), String.valueOf(totalScore));
    }

    /** 
    * @Description: 计算学生的得分 
    * @Param: [] 
    * @return: int 
    * @Author: 林凯
    * @Date: 2019/12/2 
    */ 
    private int getUserScore() {
        int score = 0;
        for (int i = 0; i < paperTitles.length; i++) {
            if (subjectInfomation[i][2].equals("单选题") || subjectInfomation[i][2].equals("多选题")) {
                if (standardAnswer[i].equals(userAnswer[i])) {
                    score += 5;
                }
            } else if (subjectInfomation[i][2].equals("判断题")){
                if (subjectInfomation[i][9].equals(userAnswer[i])) {
                    score += 5;
                }
            }

        }
        return score;
    }

    private void thisWindowClosed(WindowEvent e) {
        // TODO add your code here
        fatherFrame.setEnabled(true);           // 设置父窗口可编辑
    }


    /** 
    * @Description: 初始一下基本的界面，有 JFormdesigner 自动生成 
    * @Param: [] 
    * @return: void 
    * @Author: 林凯
    * @Date: 2019/12/2 
    */ 
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Lin Kai
        panel1 = new JPanel();
        labelExamName = new JLabel();
        labelTime = new JLabel();
        label4 = new JLabel();
        labelScore = new JLabel();
        panel2 = new JPanel();
        scrollPaneButton = new JScrollPane();
        panelTable = new JPanel();
        label1 = new JLabel();
        buttonCommit = new JButton();
        label2 = new JLabel();
        panel3 = new JPanel();
        scrollPane3 = new JScrollPane();
        textPaneContent = new JTextPane();
        panel4 = new JPanel();
        textArea1 = new JTextArea();
        radioButtonA = new JRadioButton();
        radioButtonB = new JRadioButton();
        radioButtonC = new JRadioButton();
        radioButtonD = new JRadioButton();
        checkBoxA = new JCheckBox();
        checkBoxB = new JCheckBox();
        checkBoxC = new JCheckBox();
        checkBoxD = new JCheckBox();
        radioButtonTrue = new JRadioButton();
        radioButtonFalse = new JRadioButton();
        label3 = new JLabel();
        buttonSure = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                thisWindowClosed(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel1 ========
        {
            panel1.setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing
            . border. EmptyBorder( 0, 0, 0, 0) , "JF\u006frmD\u0065sig\u006eer \u0045val\u0075ati\u006fn", javax. swing. border. TitledBorder
            . CENTER, javax. swing. border. TitledBorder. BOTTOM, new java .awt .Font ("Dia\u006cog" ,java .
            awt .Font .BOLD ,12 ), java. awt. Color. red) ,panel1. getBorder( )) )
            ; panel1. addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e
            ) {if ("\u0062ord\u0065r" .equals (e .getPropertyName () )) throw new RuntimeException( ); }} )
            ;
            panel1.setLayout(null);

            //---- labelExamName ----
            labelExamName.setText("\u8003\u8bd5\u540d\u79f0");
            labelExamName.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
            panel1.add(labelExamName);
            labelExamName.setBounds(20, 15, 185, 55);

            //---- labelTime ----
            labelTime.setText("\u5012\u8ba1\u65f6");
            labelTime.setFont(new Font(Font.DIALOG, Font.BOLD, 28));
            panel1.add(labelTime);
            labelTime.setBounds(780, 10, 590, 70);

            //---- label4 ----
            label4.setText("  ");
            panel1.add(label4);
            label4.setBounds(0, 20, 15, 70);

            //---- labelScore ----
            labelScore.setText("\u5f97\u5206\uff1a");
            labelScore.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
            labelScore.setForeground(new Color(255, 0, 51));
            panel1.add(labelScore);
            labelScore.setBounds(340, 15, 400, 55);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < panel1.getComponentCount(); i++) {
                    Rectangle bounds = panel1.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panel1.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panel1.setMinimumSize(preferredSize);
                panel1.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(panel1, BorderLayout.NORTH);

        //======== panel2 ========
        {
            panel2.setLayout(null);

            //======== scrollPaneButton ========
            {

                //======== panelTable ========
                {
                    panelTable.setLayout(new TableLayout(new double[][] {
                        {TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED},
                        {TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED}}));
                    ((TableLayout)panelTable.getLayout()).setHGap(5);
                    ((TableLayout)panelTable.getLayout()).setVGap(5);
                }
                scrollPaneButton.setViewportView(panelTable);
            }
            panel2.add(scrollPaneButton);
            scrollPaneButton.setBounds(65, 125, 420, 530);

            //---- label1 ----
            label1.setText("\u7eff\u8272\u8868\u793a\u5df2\u7ecf\u5b8c\u6210\u7684\u9898\u76ee");
            label1.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
            label1.setForeground(Color.green);
            panel2.add(label1);
            label1.setBounds(95, 15, 410, label1.getPreferredSize().height);

            //---- buttonCommit ----
            buttonCommit.setText("\u63d0\u4ea4\u8bd5\u5377");
            buttonCommit.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
            buttonCommit.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    buttonCommitMouseReleased(e);
                }
            });
            panel2.add(buttonCommit);
            buttonCommit.setBounds(115, 695, 230, 65);

            //---- label2 ----
            label2.setText("\u9ec4\u8272\u8868\u793a\u6b63\u5728\u505a\u7684\u9898\u76ee");
            label2.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
            label2.setForeground(Color.red);
            panel2.add(label2);
            label2.setBounds(95, 75, 380, 35);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < panel2.getComponentCount(); i++) {
                    Rectangle bounds = panel2.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panel2.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panel2.setMinimumSize(preferredSize);
                panel2.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(panel2, BorderLayout.WEST);

        //======== panel3 ========
        {
            panel3.setLayout(null);

            //======== scrollPane3 ========
            {

                //---- textPaneContent ----
                textPaneContent.setText("\u8bd5\u9898\u5185\u5bb9\uff0c\u7528\u6765\u4fdd\u5b58\u8bd5\u9898\u5185\u5bb9\u7684 textPane");
                textPaneContent.setFont(new Font(Font.DIALOG, Font.PLAIN, 28));
                scrollPane3.setViewportView(textPaneContent);
            }
            panel3.add(scrollPane3);
            scrollPane3.setBounds(210, 5, 760, 385);

            //======== panel4 ========
            {
                panel4.setBorder(new LineBorder(Color.black, 1, true));
                panel4.setLayout(null);
                panel4.add(textArea1);
                textArea1.setBounds(new Rectangle(new Point(0, 195), textArea1.getPreferredSize()));

                //---- radioButtonA ----
                radioButtonA.setText("A");
                radioButtonA.setFont(new Font(Font.DIALOG, Font.BOLD, 28));
                panel4.add(radioButtonA);
                radioButtonA.setBounds(90, 20, 50, 50);

                //---- radioButtonB ----
                radioButtonB.setText("B");
                radioButtonB.setFont(new Font(Font.DIALOG, Font.BOLD, 28));
                panel4.add(radioButtonB);
                radioButtonB.setBounds(425, 20, 50, 50);

                //---- radioButtonC ----
                radioButtonC.setText("C");
                radioButtonC.setFont(new Font(Font.DIALOG, Font.BOLD, 28));
                panel4.add(radioButtonC);
                radioButtonC.setBounds(90, 110, 50, 50);

                //---- radioButtonD ----
                radioButtonD.setText("D");
                radioButtonD.setFont(new Font(Font.DIALOG, Font.BOLD, 28));
                panel4.add(radioButtonD);
                radioButtonD.setBounds(425, 110, 50, 50);

                //---- checkBoxA ----
                checkBoxA.setText("A");
                checkBoxA.setFont(new Font(Font.DIALOG, Font.BOLD, 28));
                panel4.add(checkBoxA);
                checkBoxA.setBounds(90, 20, 50, 50);

                //---- checkBoxB ----
                checkBoxB.setText("B");
                checkBoxB.setFont(new Font(Font.DIALOG, Font.BOLD, 28));
                panel4.add(checkBoxB);
                checkBoxB.setBounds(425, 20, 50, 50);

                //---- checkBoxC ----
                checkBoxC.setText("C");
                checkBoxC.setFont(new Font(Font.DIALOG, Font.BOLD, 28));
                panel4.add(checkBoxC);
                checkBoxC.setBounds(90, 110, 50, 50);

                //---- checkBoxD ----
                checkBoxD.setText("D");
                checkBoxD.setFont(new Font(Font.DIALOG, Font.BOLD, 28));
                panel4.add(checkBoxD);
                checkBoxD.setBounds(425, 110, 50, 50);

                //---- radioButtonTrue ----
                radioButtonTrue.setText("\u6b63\u786e");
                radioButtonTrue.setFont(new Font(Font.DIALOG, Font.BOLD, 28));
                panel4.add(radioButtonTrue);
                radioButtonTrue.setBounds(140, 80, 105, 31);

                //---- radioButtonFalse ----
                radioButtonFalse.setText("\u9519\u8bef");
                radioButtonFalse.setFont(new Font(Font.DIALOG, Font.BOLD, 28));
                panel4.add(radioButtonFalse);
                radioButtonFalse.setBounds(475, 75, 110, 36);

                //---- label3 ----
                label3.setText("\u6b63\u786e\u7b54\u6848\u662f\uff1a");
                label3.setFont(new Font(Font.DIALOG, Font.BOLD, 22));
                panel4.add(label3);
                label3.setBounds(150, 175, 250, 50);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for(int i = 0; i < panel4.getComponentCount(); i++) {
                        Rectangle bounds = panel4.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = panel4.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    panel4.setMinimumSize(preferredSize);
                    panel4.setPreferredSize(preferredSize);
                }
            }
            panel3.add(panel4);
            panel4.setBounds(245, 405, 655, 245);

            //---- buttonSure ----
            buttonSure.setText("\u786e\u5b9a");
            buttonSure.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
            buttonSure.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    buttonSureMouseReleased(e);
                }
            });
            panel3.add(buttonSure);
            buttonSure.setBounds(480, 700, 180, 65);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < panel3.getComponentCount(); i++) {
                    Rectangle bounds = panel3.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panel3.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panel3.setMinimumSize(preferredSize);
                panel3.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(panel3, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        /**
        * @Description: ####################################   自己写的代码    ###########################
        * @Param: []
        * @return: void
        * @Author: 林凯
        * @Date: 2019/11/30
        */

        this.setBounds(70, 60, 1650, 975);

        /**
         *      1.1 将 单选按钮，复选按钮，对错按钮添加到按钮组中
         * */
        buttonGroup1 = new ButtonGroup();
//        buttonGroup2 = new ButtonGroup();
        buttonGroup3 = new ButtonGroup();

        buttonGroup1.add(radioButtonA);
        buttonGroup1.add(radioButtonB);
        buttonGroup1.add(radioButtonC);
        buttonGroup1.add(radioButtonD);
        //  多选按钮不需要添加到按钮组中
//        buttonGroup2.add(checkBoxA);
//        buttonGroup2.add(checkBoxB);
//        buttonGroup2.add(checkBoxC);
//        buttonGroup2.add(checkBoxD);
        buttonGroup3.add(radioButtonTrue);
        buttonGroup3.add(radioButtonFalse);

        labelScore.setVisible(false);       // 在考试为完成之前，将考试结果的分数设置为不可见，等到考试结束之后，才设置为可见
        label3.setVisible(false);       // 提示正确答案的标签设置为不可见

    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Lin Kai
    private JPanel panel1;
    private JLabel labelExamName;
    private JLabel labelTime;
    private JLabel label4;
    private JLabel labelScore;
    private JPanel panel2;
    private JScrollPane scrollPaneButton;
    private JPanel panelTable;
    private JLabel label1;
    private JButton buttonCommit;
    private JLabel label2;
    private JPanel panel3;
    private JScrollPane scrollPane3;
    private JTextPane textPaneContent;
    private JPanel panel4;
    private JTextArea textArea1;
    private JRadioButton radioButtonA;
    private JRadioButton radioButtonB;
    private JRadioButton radioButtonC;
    private JRadioButton radioButtonD;
    private JCheckBox checkBoxA;
    private JCheckBox checkBoxB;
    private JCheckBox checkBoxC;
    private JCheckBox checkBoxD;
    private JRadioButton radioButtonTrue;
    private JRadioButton radioButtonFalse;
    private JLabel label3;
    private JButton buttonSure;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


}
