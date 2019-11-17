/*
 * Created by JFormDesigner on Sun Oct 27 18:52:16 CST 2019
 */

package subjectadministration;

/*
*       2019年11月10日09:17:59
*       对应UI：在主界面点击 “试题管理” 之后弹出来的 试题管理界面
*       调用关系：有 MainUI 创建对象并调用
* */

import java.awt.event.*;

import jdbc.SubjectJDBC;

import java.awt.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * @author Lin Kai
 */
public class AddSubject extends JFrame {
    private int row;            // 从数据库中读取的数据的行数
    private JFrame fatherFrame;         // 父窗口，因为 题目窗口（SubjectUI） 是有 MainUI 调用，这里需要更改父窗口里面的内容
    private DefaultTableModel tableModel;           // 表格
    private boolean flag;
    private String[] result;            // 数组中放11个元素，包括title，包括changetime，changeTime（最后一次修改时间）在进行JDBC操作时生成
    private ButtonGroup buttonGroup1;           // 单选按钮组
    private ButtonGroup buttonGroup2;           // 复选按钮组


//    public AddSubject(int row) {
//        this.row = row;
//        flag = false;       // 初始值为 false，表示没有获取题目信息
//        result = new String[11];        // 数组中放11个元素，包括title，包括changeTime，changeTime（最后一次修改时间）在这里生成，直接传递给进行JDBC操作的类
//        initComponents();
//    }

    /**
    * @Description:  传入的参数 row 表示当前表格（数据库）中共有几行数据，那么这里新增的题目编号就是 row + 1
    * @Param: [row, fatherFrame]
    * @return:
    * @Author: 林凯
    * @Date: 2019/10/27
    */
    public AddSubject(int row, JFrame fatherFrame, DefaultTableModel tableModel) {
        this.row = row;
        this.fatherFrame = fatherFrame;
        this.tableModel =tableModel;
        flag = false;       // 初始值为 false，表示没有获取题目信息
        result = new String[11];        // 数组中放11个元素，包括id，包括changeTime，changeTime（最后一次修改时间）在这里生成，直接传递给进行JDBC操作的类
        initComponents();
        System.out.println("aaaaa");
    }

    /**
    * @Description: 对应下拉列表的动作监听事件
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/10/27
    */
    private void comboBoxSubjectActionPerformed(ActionEvent e) {
        // TODO add your code here
        JComboBox comboBox = (JComboBox) e.getSource();     // 获得触发事件的控件
        if (comboBox.getSelectedItem().equals("单选题")) {
            radioButtonA.setVisible(true);
            radioButtonB.setVisible(true);
            radioButtonC.setVisible(true);
            radioButtonD.setVisible(true);
            radioButtonTrue.setVisible(false);
            radioButtonFalse.setVisible(false);
            checkBoxA.setVisible(false);
            checkBoxB.setVisible(false);
            checkBoxC.setVisible(false);
            checkBoxD.setVisible(false);
            textFieldA.setVisible(true);
            textFieldB.setVisible(true);
            textFieldC.setVisible(true);
            textFieldD.setVisible(true);
            labelA.setVisible(true);
            labelB.setVisible(true);
            labelC.setVisible(true);
            labelD.setVisible(true);
            labelJudge.setVisible(false);
        } else if (comboBox.getSelectedItem().equals("多选题")) {
            radioButtonA.setVisible(false);
            radioButtonB.setVisible(false);
            radioButtonC.setVisible(false);
            radioButtonD.setVisible(false);
            radioButtonTrue.setVisible(false);
            radioButtonFalse.setVisible(false);
            checkBoxA.setVisible(true);
            checkBoxB.setVisible(true);
            checkBoxC.setVisible(true);
            checkBoxD.setVisible(true);
            textFieldA.setVisible(true);
            textFieldB.setVisible(true);
            textFieldC.setVisible(true);
            textFieldD.setVisible(true);
            labelA.setVisible(true);
            labelB.setVisible(true);
            labelC.setVisible(true);
            labelD.setVisible(true);
            labelJudge.setVisible(false);
        } else if (comboBox.getSelectedItem().equals("判断题")) {
            radioButtonA.setVisible(false);
            radioButtonB.setVisible(false);
            radioButtonC.setVisible(false);
            radioButtonD.setVisible(false);
            radioButtonTrue.setVisible(true);
            radioButtonFalse.setVisible(true);
            checkBoxA.setVisible(false);
            checkBoxB.setVisible(false);
            checkBoxC.setVisible(false);
            checkBoxD.setVisible(false);
            textFieldA.setVisible(false);
            textFieldB.setVisible(false);
            textFieldC.setVisible(false);
            textFieldD.setVisible(false);
            textFieldA.setVisible(false);
            textFieldB.setVisible(false);
            textFieldC.setVisible(false);
            textFieldD.setVisible(false);
            labelA.setVisible(false);
            labelB.setVisible(false);
            labelC.setVisible(false);
            labelD.setVisible(false);
            labelJudge.setVisible(true);
        } else if (comboBox.getSelectedItem().equals("简答题")) {
            radioButtonA.setVisible(false);
            radioButtonB.setVisible(false);
            radioButtonC.setVisible(false);
            radioButtonD.setVisible(false);
            radioButtonTrue.setVisible(false);
            radioButtonFalse.setVisible(false);
            checkBoxA.setVisible(false);
            checkBoxB.setVisible(false);
            checkBoxC.setVisible(false);
            checkBoxD.setVisible(false);
            textFieldA.setVisible(false);
            textFieldB.setVisible(false);
            textFieldC.setVisible(false);
            textFieldD.setVisible(false);
            labelA.setVisible(false);
            labelB.setVisible(false);
            labelC.setVisible(false);
            labelD.setVisible(false);
            labelJudge.setVisible(false);
        }

    }

    /**
    * @Description: 此窗体关闭时的监听事件
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/10/27
    */
    private void thisWindowClosed(WindowEvent e) {
        // TODO add your code here
        // 设置父窗体看=可操作
        fatherFrame.setEnabled(true);
        System.out.println("bbbb---close");
}

    // 将题目对应的结果数组返回
//    public String[] getResult() {
//        if (flag) {
//            return result;
//        } else {
//            return null;
//
//        }
//    }

    /**
    * @Description: 点击保存按钮对应的点击事件
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/10/27
    */
    private void buttonSaveMouseReleased(MouseEvent e) {
        // TODO add your code here
        /*
        *       首先检查用户输入的题目编号是否重复（调用 SubjectJDBC 的 readTitle() 方法获得title那列对应的String数组）
        * */
        SubjectJDBC subjectJDBC01 = new SubjectJDBC();
        String[] tempDatas = subjectJDBC01.readTitle();
        List<String> list = new ArrayList<>();
        Collections.addAll(list, tempDatas);        // 将数组元素添加到列表中
//        System.out.println(list.size());      // 测试代码
        if (list.contains(textFieldNumber.getText())) {
            // 如果该题目编号（标题）重复，弹出提示框
            JOptionPane.showMessageDialog(this, "题目编号（标题）重复！", "错误", JOptionPane.ERROR_MESSAGE);
            return;     // 不在执行后面的语句
        }

        if (comboBoxSubject.getSelectedItem().equals("单选题")) {
            // 返回一个表示题目信息的数组，共有10个元素，包括id，不包括changeTime，changeTime（最后一次修改时间）在进行JDBC操作时生成
            result[0] = textFieldNumber.getText();         // 题目编号（标题）     title
            result[1] = comboBoxSubject.getSelectedItem().toString();       // 题目类型     type
            result[2] = textAreaContent.getText();      // 题目内容         content
            result[3] = textFieldA.getText();           // A选项             optionA
            result[4] = textFieldB.getText();           // B选项             optionB
            result[5] = textFieldC.getText();           // C选项             optionC
            result[6] = textFieldD.getText();           // D选项             optionD
            //  result[7]  表示答案   answer
            if (radioButtonA.isSelected()) {
                result[7] = "A";
            } else if (radioButtonB.isSelected()) {
                result[7] = "B";
            } else if (radioButtonC.isSelected()) {
                result[7] = "C";
            } else if (radioButtonD.isSelected()) {
                result[7] = "D";
            }
            result[8] = "无";        // 因为是单选题，所以不存在对错       judge
            result[9] = textAreaRemarks.getText();
        } else if (comboBoxSubject.getSelectedItem().equals("多选题")) {
// 返回一个表示题目信息的数组，共有10个元素，包括id，不包括changeTime，changeTime（最后一次修改时间）在进行JDBC操作时生成
            result[0] = textFieldNumber.getText();        // 题目编号（标题）     title
            result[1] = comboBoxSubject.getSelectedItem().toString();       // 题目类型     type
            result[2] = textAreaContent.getText();      // 题目内容         content
            result[3] = textFieldA.getText();           // A选项             optionA
            result[4] = textFieldB.getText();           // B选项             optionB
            result[5] = textFieldC.getText();           // C选项             optionC
            result[6] = textFieldD.getText();           // D选项             optionD
            //  result[7]  表示答案   answer
            result[7] = "";
            if (checkBoxA.isSelected()) {
                result[7] += "A";
            }
            if (checkBoxB.isSelected()) {
                result[7] += "B";
            }
            if (checkBoxC.isSelected()) {
                result[7] += "C";
            }
            if (checkBoxD.isSelected()) {
                result[7] += "D";
            }
            result[8] = "无";        // 因为是单选题，所以不存在对错       judge
            result[9] = textAreaRemarks.getText();
        } else if (comboBoxSubject.getSelectedItem().equals("判断题")) {
// 返回一个表示题目信息的数组，共有10个元素，包括id，不包括changeTime，changeTime（最后一次修改时间）在进行JDBC操作时生成
            result[0] = textFieldNumber.getText();         // 题目编号（标题）     title
            result[1] = comboBoxSubject.getSelectedItem().toString();       // 题目类型     type
            result[2] = textAreaContent.getText();      // 题目内容         content
            result[3] = "无";           // A选项             optionA
            result[4] = "无";           // B选项             optionB
            result[5] = "无";           // C选项             optionC
            result[6] = "无";           // D选项             optionD
            //  result[7]  表示答案   answer
            result[7] = "无";
            if (radioButtonTrue.isSelected()) {
                result[8] = "正确";
            } else {
                result[8] = "错误";
            }
            result[9] = textAreaRemarks.getText();
        } else {
            // 简答题
// 返回一个表示题目信息的数组，共有10个元素，包括id，不包括changeTime，changeTime（最后一次修改时间）在进行JDBC操作时生成
            result[0] = textFieldNumber.getText();         // 编号     id
            result[1] = comboBoxSubject.getSelectedItem().toString();       // 题目类型     type
            result[2] = textAreaContent.getText();      // 题目内容         content
            result[3] = "无";           // A选项             optionA
            result[4] = "无";           // B选项             optionB
            result[5] = "无";           // C选项             optionC
            result[6] = "无";           // D选项             optionD
            //  result[7]  表示答案   answer
            result[7] = "无";
            result[8] = "无";
            result[9] = textAreaRemarks.getText();
        }
        flag = true;        //  说明成功获取了题目信息
        Timestamp changeTime = new Timestamp(System.currentTimeMillis());
        // 数组中第 11 个元素（下标为 10 的元素）赋值为当前时间
        result[10] = changeTime.toString();
        SubjectJDBC subjectJDBC02 = new SubjectJDBC(result[0], result[1], result[2], result[3], result[4], result[5], result[6], result[7], result[8], result[9], changeTime);
        subjectJDBC02.storeSubject();
        fatherFrame.setEnabled(true);
        tableModel.addRow(result);

        this.dispose();         // 关闭当前窗口
    }

    /**
    * @Description: JFormdesigner自动生成的初始化代码
    * @Param: []
    * @return: void
    * @Author: 林凯
    * @Date: 2019/10/27
    */
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Lin Kai
        label1 = new JLabel();
        label3 = new JLabel();
        label2 = new JLabel();
        label4 = new JLabel();
        labelA = new JLabel();
        label9 = new JLabel();
        buttonSave = new JButton();
        labelJudge = new JLabel();
        textFieldA = new JTextField();
        scrollPane1 = new JScrollPane();
        textAreaContent = new JTextArea();
        textFieldNumber = new JTextField();
        comboBoxSubject = new JComboBox();
        radioButtonTrue = new JRadioButton();
        labelB = new JLabel();
        labelD = new JLabel();
        labelC = new JLabel();
        textFieldC = new JTextField();
        textFieldD = new JTextField();
        textFieldB = new JTextField();
        radioButtonD = new JRadioButton();
        radioButtonC = new JRadioButton();
        radioButtonB = new JRadioButton();
        radioButtonA = new JRadioButton();
        radioButtonFalse = new JRadioButton();
        scrollPane2 = new JScrollPane();
        textAreaRemarks = new JTextArea();
        buttonReset = new JButton();
        checkBoxA = new JCheckBox();
        checkBoxB = new JCheckBox();
        checkBoxC = new JCheckBox();
        checkBoxD = new JCheckBox();

        //======== this ========
        setTitle("\u6dfb\u52a0\u8bd5\u9898");
        setFont(new Font(Font.DIALOG, Font.BOLD, 12));
        setResizable(false);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                thisWindowClosed(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- label1 ----
        label1.setText("\u8bd5\u9898\u7f16\u53f7(\u6807\u9898)\uff1a");
        label1.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        contentPane.add(label1);
        label1.setBounds(15, 20, 175, 35);

        //---- label3 ----
        label3.setText("\u8bd5\u9898\u5185\u5bb9\uff1a");
        label3.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        contentPane.add(label3);
        label3.setBounds(15, 70, 114, 35);

        //---- label2 ----
        label2.setText("\u8bd5\u9898\u7c7b\u578b\uff1a");
        label2.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        contentPane.add(label2);
        label2.setBounds(375, 20, 111, 35);

        //---- label4 ----
        label4.setText("\u8f93\u5165\u9898\u76ee\u7684\u9009\u9879\uff0c\u540c\u65f6\u9009\u62e9\u6b63\u786e\u7684\u9009\u9879");
        label4.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        contentPane.add(label4);
        label4.setBounds(new Rectangle(new Point(15, 235), label4.getPreferredSize()));

        //---- labelA ----
        labelA.setText("A\uff1a");
        labelA.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        contentPane.add(labelA);
        labelA.setBounds(60, 280, 42, 30);

        //---- label9 ----
        label9.setText("\u5907\u6ce8\uff1a");
        label9.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        contentPane.add(label9);
        label9.setBounds(15, 425, 75, 28);

        //---- buttonSave ----
        buttonSave.setText("\u4fdd\u5b58");
        buttonSave.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        buttonSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                buttonSaveMouseReleased(e);
            }
        });
        contentPane.add(buttonSave);
        buttonSave.setBounds(515, 430, 120, 35);

        //---- labelJudge ----
        labelJudge.setText("\u5224\u65ad\u9898\uff1a");
        labelJudge.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        contentPane.add(labelJudge);
        labelJudge.setBounds(60, 305, 90, labelJudge.getPreferredSize().height);

        //---- textFieldA ----
        textFieldA.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
        contentPane.add(textFieldA);
        textFieldA.setBounds(110, 280, 179, 30);

        //======== scrollPane1 ========
        {

            //---- textAreaContent ----
            textAreaContent.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
            scrollPane1.setViewportView(textAreaContent);
        }
        contentPane.add(scrollPane1);
        scrollPane1.setBounds(135, 70, 585, 147);

        //---- textFieldNumber ----
        textFieldNumber.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        contentPane.add(textFieldNumber);
        textFieldNumber.setBounds(190, 20, 156, 35);

        //---- comboBoxSubject ----
        comboBoxSubject.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        comboBoxSubject.addActionListener(e -> comboBoxSubjectActionPerformed(e));
        contentPane.add(comboBoxSubject);
        comboBoxSubject.setBounds(510, 20, 105, 35);

        //---- radioButtonTrue ----
        radioButtonTrue.setText("\u6b63\u786e");
        radioButtonTrue.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        contentPane.add(radioButtonTrue);
        radioButtonTrue.setBounds(155, 300, 85, 43);

        //---- labelB ----
        labelB.setText("B\uff1a");
        labelB.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        contentPane.add(labelB);
        labelB.setBounds(415, 280, 42, 30);

        //---- labelD ----
        labelD.setText("D\uff1a");
        labelD.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        contentPane.add(labelD);
        labelD.setBounds(415, 325, 42, 30);

        //---- labelC ----
        labelC.setText("C\uff1a");
        labelC.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        contentPane.add(labelC);
        labelC.setBounds(60, 325, 42, 30);

        //---- textFieldC ----
        textFieldC.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
        contentPane.add(textFieldC);
        textFieldC.setBounds(110, 325, 179, 30);

        //---- textFieldD ----
        textFieldD.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
        contentPane.add(textFieldD);
        textFieldD.setBounds(465, 325, 179, 30);

        //---- textFieldB ----
        textFieldB.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
        contentPane.add(textFieldB);
        textFieldB.setBounds(465, 280, 179, 30);

        //---- radioButtonD ----
        radioButtonD.setText("D");
        radioButtonD.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        contentPane.add(radioButtonD);
        radioButtonD.setBounds(new Rectangle(new Point(655, 325), radioButtonD.getPreferredSize()));

        //---- radioButtonC ----
        radioButtonC.setText("C");
        radioButtonC.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        contentPane.add(radioButtonC);
        radioButtonC.setBounds(new Rectangle(new Point(295, 325), radioButtonC.getPreferredSize()));

        //---- radioButtonB ----
        radioButtonB.setText("B");
        radioButtonB.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        contentPane.add(radioButtonB);
        radioButtonB.setBounds(new Rectangle(new Point(655, 275), radioButtonB.getPreferredSize()));

        //---- radioButtonA ----
        radioButtonA.setText("A");
        radioButtonA.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        contentPane.add(radioButtonA);
        radioButtonA.setBounds(new Rectangle(new Point(295, 275), radioButtonA.getPreferredSize()));

        //---- radioButtonFalse ----
        radioButtonFalse.setText("\u9519\u8bef");
        radioButtonFalse.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        contentPane.add(radioButtonFalse);
        radioButtonFalse.setBounds(250, 300, 105, 45);

        //======== scrollPane2 ========
        {

            //---- textAreaRemarks ----
            textAreaRemarks.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
            scrollPane2.setViewportView(textAreaRemarks);
        }
        contentPane.add(scrollPane2);
        scrollPane2.setBounds(105, 425, 330, 130);

        //---- buttonReset ----
        buttonReset.setText("\u91cd\u7f6e");
        buttonReset.setIcon(null);
        buttonReset.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        contentPane.add(buttonReset);
        buttonReset.setBounds(515, 505, 120, 35);

        //---- checkBoxA ----
        checkBoxA.setText("A");
        checkBoxA.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        contentPane.add(checkBoxA);
        checkBoxA.setBounds(300, 275, 52, 36);

        //---- checkBoxB ----
        checkBoxB.setText("B");
        checkBoxB.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        contentPane.add(checkBoxB);
        checkBoxB.setBounds(655, 275, 52, 36);

        //---- checkBoxC ----
        checkBoxC.setText("C");
        checkBoxC.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
        contentPane.add(checkBoxC);
        checkBoxC.setBounds(300, 325, 52, 36);

        //---- checkBoxD ----
        checkBoxD.setText("D");
        checkBoxD.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        contentPane.add(checkBoxD);
        checkBoxD.setBounds(655, 325, 52, 36);

        contentPane.setPreferredSize(new Dimension(785, 640));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        // #######################  手动添加的代码     #####################
        setSize(785, 640);



        /**
        * @Description: #####################################################3        下面是自己编写的代码      ##################################
        * @Param: []
        * @return: void
        * @Author: 林凯
        * @Date: 2019/11/15
        */
        // 设置题目编号
//        textFieldNumber.setEditable(false);     // 设置为不可编辑状态（题目编号由用户输入）

        // 设置下拉列表的选项
//        textFieldNumber.setText(String.valueOf(row));

        contentPane.setSize(800, 650);


        String[] items = {"单选题", "多选题", "判断题", "简答题"};
        ComboBoxModel comboBoxModel = new DefaultComboBoxModel(items);
        comboBoxSubject.setModel(comboBoxModel);

        // 设置默认选中按钮
        radioButtonA.setSelected(true);
        checkBoxA.setSelected(true);
        radioButtonTrue.setSelected(true);

        // 设置单选按钮组，有两个单选按钮组

        buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(radioButtonA);
        buttonGroup1.add(radioButtonB);
        buttonGroup1.add(radioButtonC);
        buttonGroup1.add(radioButtonD);
        buttonGroup2 = new ButtonGroup();
        buttonGroup2.add(radioButtonTrue);
        buttonGroup2.add(radioButtonFalse);

        // 设置各种控件的默认显示方式（默认是添加单选题）
        radioButtonA.setVisible(true);
        radioButtonB.setVisible(true);
        radioButtonC.setVisible(true);
        radioButtonD.setVisible(true);
        radioButtonTrue.setVisible(false);
        radioButtonFalse.setVisible(false);
        checkBoxA.setVisible(false);
        checkBoxB.setVisible(false);
        checkBoxC.setVisible(false);
        checkBoxD.setVisible(false);
        textFieldA.setVisible(true);
        textFieldB.setVisible(true);
        textFieldC.setVisible(true);
        textFieldD.setVisible(true);
        labelA.setVisible(true);
        labelB.setVisible(true);
        labelC.setVisible(true);
        labelD.setVisible(true);
        labelJudge.setVisible(false);


    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Lin Kai
    private JLabel label1;
    private JLabel label3;
    private JLabel label2;
    private JLabel label4;
    private JLabel labelA;
    private JLabel label9;
    private JButton buttonSave;
    private JLabel labelJudge;
    private JTextField textFieldA;
    private JScrollPane scrollPane1;
    private JTextArea textAreaContent;
    private JTextField textFieldNumber;
    private JComboBox comboBoxSubject;
    private JRadioButton radioButtonTrue;
    private JLabel labelB;
    private JLabel labelD;
    private JLabel labelC;
    private JTextField textFieldC;
    private JTextField textFieldD;
    private JTextField textFieldB;
    private JRadioButton radioButtonD;
    private JRadioButton radioButtonC;
    private JRadioButton radioButtonB;
    private JRadioButton radioButtonA;
    private JRadioButton radioButtonFalse;
    private JScrollPane scrollPane2;
    private JTextArea textAreaRemarks;
    private JButton buttonReset;
    private JCheckBox checkBoxA;
    private JCheckBox checkBoxB;
    private JCheckBox checkBoxC;
    private JCheckBox checkBoxD;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

//    public static void main(String[] args) {
////        AddSubject subject = new AddSubject(12);
////        subject.setVisible(true);
////    }



}
