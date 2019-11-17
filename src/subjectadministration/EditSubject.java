/*
 * Created by JFormDesigner on Wed Nov 13 22:39:29 CST 2019
 */

package subjectadministration;

import jdbc.SubjectJDBC;
import java.awt.*;
import java.awt.event.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * @author Lin Kai
 */
public class EditSubject extends JFrame {
    JFrame fatherFrame;
    DefaultTableModel tableModel;
    int row;
    private ButtonGroup buttonGroup1;           // 单选按钮组
    private ButtonGroup buttonGroup2;           // 复选按钮组
    String oldTitle;
    String[] result;        // 用来保存题目对应的数组，有10个元素，用来表示一道试题的具体内容
    Timestamp timestamp;        //最后修改时间戳

    public EditSubject(JFrame fatherFrame, DefaultTableModel tableModel, int row) {
        this.fatherFrame = fatherFrame;
        this.tableModel = tableModel;
        this.row = row;
        // 将原来的 题目编号（标题） 先保存起来，到那时候用来判断用户更改的 题目编号（标题）是否合法
        oldTitle = tableModel.getValueAt(row, 0).toString();
        initComponents();

        // 1.从传进来的 TableModel 中获取数据，然后赋值到各文本框以及选项卡中
        myInit();
    }

    /**
    * @Description:  从传进来的 TableModel 中获取数据，然后赋值到各文本框以及选项卡中
    * @Param: []
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/15
    */
    private void myInit() {
        // 1.设置   题目编号（标题）  文本框中的值
        textFieldNumber.setText(tableModel.getValueAt(row, 0).toString());

        // 2.设置  试题类型  下拉列表对应的值，通根据下拉列表的值设置对应的具体的题目内容
        if (tableModel.getValueAt(row, 1).toString().equals("单选题")) {
            // 2.0 设置下拉列表的值
            comboBoxSubject.setSelectedIndex(0);
            // 2.1 同时还要设置   选项内容
            textFieldA.setText(tableModel.getValueAt(row, 3).toString());
            textFieldB.setText(tableModel.getValueAt(row, 4).toString());
            textFieldC.setText(tableModel.getValueAt(row, 5).toString());
            textFieldD.setText(tableModel.getValueAt(row, 6).toString());

            // 2.2 设置   正确的选项
            if (tableModel.getValueAt(row, 7).toString().equals("A") ) {
                radioButtonA.setSelected(true);
            } else if (tableModel.getValueAt(row, 7).toString().equals("B")) {
                radioButtonB.setSelected(true);
            } else if (tableModel.getValueAt(row, 7).toString().equals("C")) {
                radioButtonC.setSelected(true);
            } else {
                radioButtonD.setSelected(true);
            }

        } else if (tableModel.getValueAt(row, 1).toString().equals("多选题")) {
            comboBoxSubject.setSelectedIndex(1);

            // 2.1 同时还要设置   选项内容
            textFieldA.setText(tableModel.getValueAt(row, 3).toString());
            textFieldB.setText(tableModel.getValueAt(row, 4).toString());
            textFieldC.setText(tableModel.getValueAt(row, 5).toString());
            textFieldD.setText(tableModel.getValueAt(row, 6).toString());

            // 2.2 设置   正确的选项
            char[] chars = tableModel.getValueAt(row, 7).toString().toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == 'A') {
                    checkBoxA.setSelected(true);
                } else if (chars[i] == 'B') {
                    checkBoxB.setSelected(true);
                } else if (chars[i] == 'C') {
                    checkBoxC.setSelected(true);
                } else {
                    checkBoxD.setSelected(true);
                }
            }

        } else if (tableModel.getValueAt(row, 1).toString().equals("判断题")) {
            comboBoxSubject.setSelectedIndex(2);
            // 设置真假值对应的 button
            if (tableModel.getValueAt(row, 8).toString().equals("正确")) {
                radioButtonTrue.setSelected(true);
            } else {
                radioButtonFalse.setSelected(true);
            }

        } else {            // 简答题
            comboBoxSubject.setSelectedIndex(3);
        }

        // 3. 设置试题内容
        textAreaContent.setText(tableModel.getValueAt(row, 2).toString());
        // 4.设置试题备注
        textAreaRemarks.setText(tableModel.getValueAt(row, 9).toString());

    }

    /**
    * @Description: 窗口关闭时调用的
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/15
    */
    private void thisWindowClosed(WindowEvent e) {
        // TODO add your code here
        // 设置父窗体看=可操作
        fatherFrame.setEnabled(true);

        // 更改父窗口的表格里面的内容
        tableModel.setValueAt(result[0], row, 0);
        tableModel.setValueAt(result[1], row, 1);
        tableModel.setValueAt(result[2], row, 2);
        tableModel.setValueAt(result[3], row, 3);
        tableModel.setValueAt(result[4], row, 4);
        tableModel.setValueAt(result[5], row, 5);
        tableModel.setValueAt(result[6], row, 6);
        tableModel.setValueAt(result[7], row, 7);
        tableModel.setValueAt(result[8], row, 8);
        tableModel.setValueAt(result[9], row, 9);
        tableModel.setValueAt(timestamp, row, 10);

    }

    /**
    * @Description: 用户点击保存按钮，更新数据库中的信息，之后再更新表格中的信息，然后当前窗体关闭，回到主窗口
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/15
    */
    private void buttonSaveMouseReleased(MouseEvent e) {
        // TODO add your code here
        SubjectJDBC subjectJDBC = new SubjectJDBC();

        // 1.首先判断用户输入的  题目编号（标题）是否合法
        if (textFieldNumber.getText().equals(oldTitle) == false) {
            // 用户更改了  题目编号 ，需要检测该 题目编号是否合法
            String[] tempTitle = subjectJDBC.readTitle();
            ArrayList<String> list = new ArrayList<>();
            Collections.addAll(list, tempTitle);
            if (list.contains(textFieldNumber.getText())) {
                JOptionPane.showMessageDialog(this, "题目编号（标题）重复！", "错误", JOptionPane.ERROR_MESSAGE);
                return;     // 不在执行后面的语句
            }
        }

        /*
        *       2.题目编号合法之后，生成代表题目信息的 String 数组，然后调用 SubjectJDBC 来更新数据
        *       准备10个元素的数组，不包括 changeTime，因为changeTime是TimeStamp类型，不好传递，在执行JDBC操作是生成
        * */
        result = new String[10];
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

        timestamp = new Timestamp(System.currentTimeMillis());

        // 更新数据
        subjectJDBC.updateSubject(result, textFieldNumber.getText(), timestamp);

        this.dispose();     // 关闭当前窗口，关闭之前会调用 thisWindowCLosed() 方法
    }

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
        comboBoxSubject.addActionListener(e -> {
			comboBoxSubjectActionPerformed(e);
			comboBoxSubjectActionPerformed(e);
		});
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

        contentPane.setPreferredSize(new Dimension(785, 639));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents


        /**
        * @Description: ########################################3    自己编写的代码    ##################################3
        * @Param: []
        * @return: void
        * @Author: 林凯
        * @Date: 2019/11/15
        */

        contentPane.setSize(800, 650);


        // 1.为  题目类型    下拉列表赋初值
        comboBoxSubject.addItem("单选题");
        comboBoxSubject.addItem("多选题");
        comboBoxSubject.addItem("判断题");
        comboBoxSubject.addItem("简答题");

        // 设置单选按钮组，有两个单选按钮组
        buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(radioButtonA);
        buttonGroup1.add(radioButtonB);
        buttonGroup1.add(radioButtonC);
        buttonGroup1.add(radioButtonD);
        buttonGroup2 = new ButtonGroup();
        buttonGroup2.add(radioButtonTrue);
        buttonGroup2.add(radioButtonFalse);


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
}
