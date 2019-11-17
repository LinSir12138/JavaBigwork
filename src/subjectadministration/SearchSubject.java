/*
 * Created by JFormDesigner on Sun Nov 10 09:37:53 CST 2019
 */

package subjectadministration;

/*
*       2019年11月10日09:38:39
*       对应UI界面：“查找试题” 对应的界面
*       调用关系：  由 MainUI 类里面的 buttonSearchSubjectMouseReleased() 方法创建对对象并调用相关的方法
* */

import jdbc.SubjectJDBC;

import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * @author Lin Kai
 */
public class SearchSubject extends JFrame {
    private JFrame fatherFrame;     //  父类窗口引用
    private DefaultTableModel tableModel;           // 表格引用，用来修改父类的表格
//    Map<String, String> map;            // 用来存储用户指定的搜索条件，由 setMap() 方法初始化
    private SubjectJDBC subjectJDBC;
    private long beginTime = 0;     // 开始时间对应的毫秒数
    private long endTime = 0;       // 结束事件对应的毫秒数

    /**
    * @Description:    构造方法
    * @Param: [fatherJFame, tableModel]
    * @return:
    * @Author: 林凯
    * @Date: 2019/11/10
    */
    public SearchSubject(JFrame fatherJFame, DefaultTableModel tableModel) {
        initComponents();
        this.fatherFrame = fatherJFame;
        this.tableModel = tableModel;
    }

    /**
    * @Description: 选择时间按钮对应的点击事件---》 选择起始时间
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/10
    */
    private void buttonChooseTimeBeginMouseReleased(MouseEvent e) {
        // TODO add your code here
        MyClander myClander = new MyClander(textFieldDateBegin);
        myClander.setVisible(true);
    }

    /**
    * @Description: 选择时间按钮对应的点击事件----》 选择结束时间
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/10
    */
    private void buttonChooseTimeEndMouseReleased(MouseEvent e) {
        // TODO add your code here
        MyClander myClander = new MyClander(textFieldDateEnd);
        myClander.setVisible(true);
    }

    /**
    * @Description: 点击 “题目编号（标题）” 后面的 “不做为筛选条件” 按钮（单选按钮）
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/10
    */
    private void radioButtonTitleMouseReleased(MouseEvent e) {
        // TODO add your code here
        JRadioButton radioButton = (JRadioButton) e.getSource();
        if (radioButton.isSelected()) {
            textFieldTitle.setText("");
            textFieldTitle.setEditable(false);
        } else {
            textFieldTitle.setEditable(true);
        }
    }

    /**
    * @Description: 点击 “题目内容” 后面的 “不做为筛选条件” 按钮（单选按钮）对应的点击事件
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/10
    */
    private void radioButtonContentMouseReleased(MouseEvent e) {
        // TODO add your code here
        System.out.println("rrrrrrrrrrrr");
        JRadioButton radioButton = (JRadioButton) e.getSource();
        if (radioButton.isSelected()) {
            textAreaContent.setText("");
            textAreaContent.setEditable(false);
        } else {
            textAreaContent.setEditable(true);
        }
    }

    /**
    * @Description: 窗体关闭时的事件
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/10
    */
    private void thisWindowClosed(WindowEvent e) {
        // TODO add your code here
        fatherFrame.setEnabled(true);
        System.out.println("bbbb---close");
    }

    /**
    * @Description: 点击 “查找试题”按钮对应的点击事件--》 从数据库中查找，然后修改父窗口里面的表格
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/10
    */
    private void buttonSearchMouseReleased(MouseEvent e) {
        // TODO add your code here
        /*
        *       首先：检测用户输入的时间是否合法
        *       通过 DateFormat 类将时间转换为 long 类型的毫秒数
        * */
        SimpleDateFormat begin = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat end = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

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


        String sql = setSQL();      // 获得对应的 SQL 语句
        System.out.println(sql);

        subjectJDBC = new SubjectJDBC();        // 初始化
        String[][] tempDatas = subjectJDBC.readSubject(sql);

        // 将数组中的内容设置到表格当中
        tableModel.setRowCount(tempDatas.length);       // 重新设置表格的行数

        for (int row = 0; row < tempDatas.length; row++) {
            for (int column = 0; column< tempDatas[0].length; column++) {
                tableModel.setValueAt(tempDatas[row][column], row, column);     // 注： Swing 表格组件中，行列都是从0开始的
            }
        }
        tableModel.fireTableDataChanged();      // 更新表格的信息，十分必要，否则可能显示错误

        fatherFrame.setEnabled(true);       // 设置父窗口可以操作
        this.dispose();     // 设置本窗口隐藏

    }

    /**
    * @Description: 根据 Map 生成对应的 SQL 语句
    * @Param: []
    * @return: java.lang.String
    * @Author: 林凯
    * @Date: 2019/11/10
    */
    private String setSQL() {
        StringBuilder sql = new StringBuilder("select * from subject where ");      // 注意空格
//        if (radioButtonTitle.isSelected() == false || radioButtonTitle.getText().equals("")) {
//            // 如果没有选中单选按钮，说明title做为筛选条件
//        }
        sql.append("title like '%" + textFieldTitle.getText() + "%' ");     // 注意空格，标点符号


        //  处理“题目类型” getSelectedIndex() 返回数字从 0 开始，0表示第一项（从上到下）
        if (comboBoxType.getSelectedIndex() == 0) {
            // 什么也不用做
        } else if (comboBoxType.getSelectedIndex() == 1) {
            // 单选题
            sql.append("and type = '单选题' ");
        } else if (comboBoxType.getSelectedIndex() == 2) {
            // 多选题
            sql.append("and type = '多选题' ");
        } else if (comboBoxType.getSelectedIndex() == 3) {
            // 判断题
            sql.append("and type = '判断题' ");
        } else {
            // 简答题
            sql.append("and type = '简答题' ");
        }

        // 处理 “题目内容”
        if (radioButtonContent.isSelected() == false) {
            sql.append("and content like '%" + textAreaContent.getText() + "%' ");
        }

        /*
        *       处理 “最后修改时间”,要求两个时间框中都有时间时才添加关于时间查询的 SQL 语句
        *       时间戳的比较，有3种方式，见博客：https://blog.csdn.net/wide288/article/details/37744773
        * */

        if (beginTime <= endTime && (!textFieldDateBegin.getText().equals("")) && (!textFieldDateEnd.getText().equals(""))) {
//            sql.append("and changeTime >= " + textFieldDateBegin.getText() + " " + "and changeTime <= " + textFieldDateEnd.getText());
            sql.append("and (changeTime between " +  "'" + textFieldDateBegin.getText() + "'" + " and " + "'" + textFieldDateEnd.getText() + "')");
        }

        return sql.toString();
    }

//    /**
//    * @Description: 根据用户选择的内容，生成对应的Map对象（将用户选择的内容存放到Map集合中）
//    * @Param: []
//    * @return: void
//    * @Author: 林凯
//    * @Date: 2019/11/10
//    */
//    private void setMap() {
//        map = new HashMap<>();      // 创建 HashMap 的对象
//        if (radioButtonTitle.isSelected()) {
//            map.put("Title", null);
//        } else {
//            map.put("Title", textFieldTitle.getText());
//        }
//        map.put("Type", String.valueOf(comboBoxType.getSelectedIndex()));
//        if (radioButtonContent.isSelected()) {
//            map.put("Content", null);
//        } else {
//            map.put("Content", textAreaContent.getText());
//        }
//        map.put("DateBegin", textFieldDateBegin.getText());
//        map.put("DateEnd", textFieldDateEnd.getText());
//    }

    /**
    * @Description: 初始化界面
    * @Param: []
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/10
    */
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Lin Kai
        label1 = new JLabel();
        textFieldTitle = new JTextField();
        radioButtonTitle = new JRadioButton();
        label2 = new JLabel();
        label3 = new JLabel();
        comboBoxType = new JComboBox();
        scrollPane1 = new JScrollPane();
        textAreaContent = new JTextArea();
        radioButtonContent = new JRadioButton();
        buttonSearch = new JButton();
        label4 = new JLabel();
        textFieldDateBegin = new JTextField();
        buttonChooseTimeBegin = new JButton();
        textFieldDateEnd = new JTextField();
        label5 = new JLabel();
        buttonChooseTimeEnd = new JButton();

        //======== this ========
        setTitle("\u67e5\u627e\u8bd5\u9898");
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
        label1.setText("\u8bd5\u9898\u7f16\u53f7\uff08\u6807\u9898\uff09\uff1a");
        label1.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        contentPane.add(label1);
        label1.setBounds(25, 25, 165, 55);

        //---- textFieldTitle ----
        textFieldTitle.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
        contentPane.add(textFieldTitle);
        textFieldTitle.setBounds(225, 35, 185, 40);

        //---- radioButtonTitle ----
        radioButtonTitle.setText("\u4e0d\u505a\u4e3a\u7b5b\u9009\u6761\u4ef6");
        radioButtonTitle.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        radioButtonTitle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                radioButtonTitleMouseReleased(e);
            }
        });
        contentPane.add(radioButtonTitle);
        radioButtonTitle.setBounds(465, 40, 205, 30);

        //---- label2 ----
        label2.setText("\u8bd5\u9898\u7c7b\u578b\uff1a");
        label2.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        contentPane.add(label2);
        label2.setBounds(90, 110, 90, 35);

        //---- label3 ----
        label3.setText("\u8bd5\u9898\u5185\u5bb9\uff1a");
        label3.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        contentPane.add(label3);
        label3.setBounds(85, 165, 105, 65);

        //---- comboBoxType ----
        comboBoxType.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        contentPane.add(comboBoxType);
        comboBoxType.setBounds(230, 115, 110, 32);

        //======== scrollPane1 ========
        {

            //---- textAreaContent ----
            textAreaContent.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
            scrollPane1.setViewportView(textAreaContent);
        }
        contentPane.add(scrollPane1);
        scrollPane1.setBounds(190, 185, 380, 195);

        //---- radioButtonContent ----
        radioButtonContent.setText("\u4e0d\u505a\u4e3a\u7b5b\u9009\u6761\u4ef6");
        radioButtonContent.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        radioButtonContent.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                radioButtonContentMouseReleased(e);
            }
        });
        contentPane.add(radioButtonContent);
        radioButtonContent.setBounds(605, 270, 170, 30);

        //---- buttonSearch ----
        buttonSearch.setText("\u67e5\u627e\u8bd5\u9898");
        buttonSearch.setFont(new Font(Font.DIALOG, Font.BOLD, 22));
        buttonSearch.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                buttonSearchMouseReleased(e);
            }
        });
        contentPane.add(buttonSearch);
        buttonSearch.setBounds(290, 540, 175, 50);

        //---- label4 ----
        label4.setText("\u6700\u7ec8\u4fee\u6539\u65f6\u95f4\u8303\u56f4\uff1a");
        label4.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        contentPane.add(label4);
        label4.setBounds(15, 410, 155, 40);

        //---- textFieldDateBegin ----
        textFieldDateBegin.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        textFieldDateBegin.setEditable(false);
        contentPane.add(textFieldDateBegin);
        textFieldDateBegin.setBounds(180, 410, 205, 40);

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
        buttonChooseTimeBegin.setBounds(220, 465, 110, 35);

        //---- textFieldDateEnd ----
        textFieldDateEnd.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        textFieldDateEnd.setEditable(false);
        contentPane.add(textFieldDateEnd);
        textFieldDateEnd.setBounds(480, 410, 210, 40);

        //---- label5 ----
        label5.setText("\u5230");
        label5.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        contentPane.add(label5);
        label5.setBounds(425, 420, 25, 25);

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
        buttonChooseTimeEnd.setBounds(535, 470, 105, buttonChooseTimeEnd.getPreferredSize().height);

        contentPane.setPreferredSize(new Dimension(800, 650));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        /**
        * @Description: ####################3     MyCode    ######################3
        * @Param: []
        * @return: void
        * @Author: 林凯
        * @Date: 2019/11/10
        */
        // 设置窗体大小，首先要在 jfd 文件里面吧 auto-size 取消，才能设置成功
        contentPane.setSize(800, 650);

        // 设置下拉列表
        String[] items = {"全部", "单选题", "多选题", "判断题", "简答题"};
        ComboBoxModel comboBoxModel = new DefaultComboBoxModel(items);
        comboBoxType.setModel(comboBoxModel);
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Lin Kai
    private JLabel label1;
    private JTextField textFieldTitle;
    private JRadioButton radioButtonTitle;
    private JLabel label2;
    private JLabel label3;
    private JComboBox comboBoxType;
    private JScrollPane scrollPane1;
    private JTextArea textAreaContent;
    private JRadioButton radioButtonContent;
    private JButton buttonSearch;
    private JLabel label4;
    private JTextField textFieldDateBegin;
    private JButton buttonChooseTimeBegin;
    private JTextField textFieldDateEnd;
    private JLabel label5;
    private JButton buttonChooseTimeEnd;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


    // 用来测试的main方法
    public static void main(String[] args) {
//        SearchSubject searchSubject = new SearchSubject();
//        searchSubject.setVisible(true);
    }
}
