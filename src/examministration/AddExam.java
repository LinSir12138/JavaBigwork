/*
 * Created by JFormDesigner on Wed Nov 27 21:29:11 CST 2019
 */

package examministration;

import jdbc.ExamJDBC;
import jdbc.PaperJDBC;
import jdbc.UserJDBC;
import subjectadministration.MyClander;

import java.awt.*;
import java.awt.event.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/*
 *       2019年11月27日21:26:51
 *       UI界面： 在  “考试管理”  界面点击   “发布考试” 之后弹出的的 ui 界面
 *       调用关系： 由 MainUI  类中对应的点击事件创建对象之后再调用
 * */

/**
 * @author Lin Kai
 */
public class AddExam extends JFrame {
    private JFrame fatherFrame;
    private DefaultTableModel tableModel;

    public AddExam(JFrame fatherFrame, DefaultTableModel tableModel) {
        this.fatherFrame = fatherFrame;
        this.tableModel = tableModel;
        initComponents();
    }

    /**
    * @Description: 考试开始时间
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/27
    */
    private void buttonChooseTimeBeginMouseReleased(MouseEvent e) {
        // TODO add your code here
        MyClander myClander = new MyClander(textFieldDateBegin);
        myClander.setVisible(true);
    }

    /**
    * @Description: 考试结束时间
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/27
    */
    private void buttonChooseTimeEndMouseReleased(MouseEvent e) {
        // TODO add your code here
        MyClander myClander = new MyClander(textFieldDateEnd);
        myClander.setVisible(true);
    }

    /**
    * @Description: 点击  “确定”  按钮对应的点击事件
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/27
    */
    private void buttonSureMouseReleased(MouseEvent e) {
        // TODO add your code here
        // 1. 首先要检测用户输入的内容是否合法，例如考试名称不能重复，选择的考试对应的试卷的名称是否正确，输入的考试时间是否合法
        // 1.1 检测用户输入是否为空
        if (textFieldExamName.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "请输入考试名称", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (textFieldExamPaperName.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "请输入考试对应的试卷名称", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (textFieldDateBegin.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "请输入考试开始时间", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (textFieldDateEnd.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "请输入考试截止", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 1.2 检查 考试名称是否存在同名，考试对应试卷名称是否存在
        String[] alreadyExamName = new String[tableModel.getRowCount()];        // 数据库中已经存在的考试名称
        for (int i = 0; i < tableModel.getRowCount(); i++) { // 从父窗口的表格中获取已经存在的考试名称
            alreadyExamName[i] = tableModel.getValueAt(i, 0).toString();
        }
        for (int i = 0; i < alreadyExamName.length; i++) {  // 检查是否存在重复考试名称,如果存在，提示用户，然后直接返回即可
            if (alreadyExamName[i].equals(textFieldExamName.getText())) {
                JOptionPane.showMessageDialog(this, "考试名称重复!", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        String[] alreadyPaperName = PaperJDBC.readPaperTitle();     // 从数据库中获取已经存在的 试卷名称
        boolean flag = false;
        for (int i = 0; i < alreadyPaperName.length; i++) {  // 检查 是否存在该试卷，如果不存在，提示用户
            if (alreadyPaperName[i].equals(textFieldExamPaperName.getText())) {
                flag = true;        // 更改标志
            }
        }
        if (flag == false) {
            JOptionPane.showMessageDialog(this, "考试对应试卷不存在，请先创建试卷！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 1.3 检查用户输入的时间是否合法
        /*
         *       首先：检测用户输入的时间是否合法
         *       通过 DateFormat 类将时间转换为 long 类型的毫秒数
         * */
        SimpleDateFormat begin = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat end = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        long beginTime = 0;
        long endTime = 0;
        try {
            Date dateBegin = begin.parse(textFieldDateBegin.getText());
            Date dateEnd = end.parse(textFieldDateEnd.getText());
            beginTime = dateBegin.getTime();
            endTime = dateEnd.getTime();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        if (beginTime > endTime) {
            // 如果开始时间大于结束事件，弹出警告框
            JOptionPane.showMessageDialog(this, "时间选择错误！", "错误", JOptionPane.ERROR_MESSAGE);
            return;     // 直接返回，不在执行后面的语句
        }

        // 2. 一切检查完毕之后，在数据库中增加新的考试记录，同时更新父窗口里面的表格记录
        Timestamp beginTimeStamp = new Timestamp(beginTime);
        Timestamp endTimeStamp = new Timestamp(endTime);
        ExamJDBC.insertExam(textFieldExamName.getText(), textFieldExamPaperName.getText(), "0",beginTimeStamp, endTimeStamp);
        //  2.2  更新父窗口里面的表格记录
        String[] tempData = new String[tableModel.getColumnCount()];  // 创建一个临时保存数据的数组，用来在 表格中增加新的一行
        tempData[0] = textFieldExamName.getText();          // 获得考试名称
        tempData[1] = textFieldExamPaperName.getText();     // 获得考试对应的试卷名称
        tempData[2] = "0";      // 初始化的时候，还没有任何一个学生参加了考试
        tempData[3] = beginTimeStamp.toString();
        tempData[4] = endTimeStamp.toString();
        tableModel.addRow(tempData);
        // 2.3 同时还要调用 UserJDBC 类的 addColmn() 方法，动态得在 User 表中添加 1 列数据
        UserJDBC userJDBC = new UserJDBC();
//        userJDBC.addColmn(textFieldExamName.getText().toString());      // 传入的参数为考试的名称

        this.dispose();     // 本窗口关闭，释放内存资源
    }

    /**
    * @Description: 当调用 dispose()  方法后，自动会调用的 窗口关闭 的方法， 里面设置父窗口可操作
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/12/14
    */
    private void thisWindowClosed(WindowEvent e) {
        // TODO add your code here
        fatherFrame.setEnabled(true);       // 设置父窗口可见
    }

    /**
    * @Description:  JFrameDesigner 动态生成的代码
    * @Param: []
    * @return: void
    * @Author: 林凯
    * @Date: 2019/12/14
    */
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Lin Kai
        label1 = new JLabel();
        label2 = new JLabel();
        label4 = new JLabel();
        label5 = new JLabel();
        textFieldDateBegin = new JTextField();
        buttonChooseTimeBegin = new JButton();
        textFieldDateEnd = new JTextField();
        buttonChooseTimeEnd = new JButton();
        textFieldExamName = new JTextField();
        textFieldExamPaperName = new JTextField();
        buttonSure = new JButton();

        //======== this ========
        setTitle("\u53d1\u5e03\u8003\u8bd5");
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
        label1.setText("\u8003\u8bd5\u540d\u79f0\uff1a");
        label1.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
        contentPane.add(label1);
        label1.setBounds(130, 25, 95, 55);

        //---- label2 ----
        label2.setText("\u5bf9\u5e94\u8bd5\u5377\u540d\u79f0\uff1a");
        label2.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
        contentPane.add(label2);
        label2.setBounds(90, 105, 160, 60);

        //---- label4 ----
        label4.setText("\u8003\u8bd5\u5f00\u59cb\u65f6\u95f4\uff1a");
        label4.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
        contentPane.add(label4);
        label4.setBounds(90, 185, 155, 65);

        //---- label5 ----
        label5.setText("\u8003\u8bd5\u7ed3\u675f\u65f6\u95f4\uff1a");
        label5.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
        contentPane.add(label5);
        label5.setBounds(85, 275, 160, 60);

        //---- textFieldDateBegin ----
        textFieldDateBegin.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
        textFieldDateBegin.setEditable(false);
        contentPane.add(textFieldDateBegin);
        textFieldDateBegin.setBounds(255, 200, 205, 40);

        //---- buttonChooseTimeBegin ----
        buttonChooseTimeBegin.setText("\u9009\u62e9\u65f6\u95f4");
        buttonChooseTimeBegin.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        buttonChooseTimeBegin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                buttonChooseTimeBeginMouseReleased(e);
            }
        });
        contentPane.add(buttonChooseTimeBegin);
        buttonChooseTimeBegin.setBounds(505, 205, 110, 35);

        //---- textFieldDateEnd ----
        textFieldDateEnd.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
        textFieldDateEnd.setEditable(false);
        contentPane.add(textFieldDateEnd);
        textFieldDateEnd.setBounds(255, 285, 210, 40);

        //---- buttonChooseTimeEnd ----
        buttonChooseTimeEnd.setText("\u9009\u62e9\u65f6\u95f4");
        buttonChooseTimeEnd.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        buttonChooseTimeEnd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                buttonChooseTimeEndMouseReleased(e);
            }
        });
        contentPane.add(buttonChooseTimeEnd);
        buttonChooseTimeEnd.setBounds(505, 285, 110, 35);

        //---- textFieldExamName ----
        textFieldExamName.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
        contentPane.add(textFieldExamName);
        textFieldExamName.setBounds(255, 35, 195, 45);

        //---- textFieldExamPaperName ----
        textFieldExamPaperName.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
        contentPane.add(textFieldExamPaperName);
        textFieldExamPaperName.setBounds(255, 115, 195, 45);

        //---- buttonSure ----
        buttonSure.setText("\u786e\u5b9a");
        buttonSure.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
        buttonSure.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                buttonSureMouseReleased(e);
            }
        });
        contentPane.add(buttonSure);
        buttonSure.setBounds(270, 370, 155, 55);

        contentPane.setPreferredSize(new Dimension(690, 480));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        this.setSize(690, 475);
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Lin Kai
    private JLabel label1;
    private JLabel label2;
    private JLabel label4;
    private JLabel label5;
    private JTextField textFieldDateBegin;
    private JButton buttonChooseTimeBegin;
    private JTextField textFieldDateEnd;
    private JButton buttonChooseTimeEnd;
    private JTextField textFieldExamName;
    private JTextField textFieldExamPaperName;
    private JButton buttonSure;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
