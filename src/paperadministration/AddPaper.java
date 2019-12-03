/*
 * Created by JFormDesigner on Sat Nov 16 11:14:55 CST 2019
 */

package paperadministration;

import java.awt.event.*;
import jdbc.SubjectJDBC;
import subjectadministration.SearchSubject;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * @author Lin Kai
 */
public class AddPaper extends JFrame {
    private JFrame fatherFrame;     // 父窗口
    private JTable tableSubject;        // 包含题目信息的表格
    private DefaultTableModel tableModel;       // 表格模型
    private SubjectJDBC subjectJDBC;        // 进行试题JDBC操作的类
    private JScrollPane scrollPaneSubject;      // 表格需要放在 JScrollPane 中

    public AddPaper(JFrame fatherFrame) {
        this.fatherFrame = fatherFrame;
        initComponents();
        // 初始化数据，从数据库中读取数据，生成表格，然后把表格添加到面板中
        initDatas();
    }

    /**
    * @Description: 初始化题目信息，让用户选择需要添加到试卷里面的试题
    * @Param: []
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/16
    */
    private void initDatas() {
        String[] columNames = {"题目编号(标题)", "题目类型", "题目内容", "选项A", "选项B", "选项C", "选项D", "正确答案", "真假值", "题目备注", "最后修改时间"};
//        SubjectJDBC subjectJDBC = new SubjectJDBC("单选题", "构造方法何时被调用？", "类定义时", "创建对象时", "调用对象方法时", "使用对象的变量时", "A", "目的是让学生了解构造方法");
        subjectJDBC = new SubjectJDBC();
        // 这里没有使用表格模型，直接用的表格
        String[][] datas = subjectJDBC.readSubject("select * from subject");

        for (int i = 0; i < datas.length; i++) {
            for (int j = 0; j <datas[0].length; j++) {
                System.out.print(datas[i][j] + "  ");
            }
            System.out.println();
        }


        tableModel = new DefaultTableModel(datas, columNames);         // tableModel 对应有 11 列
        tableSubject = new JTable(tableModel);
        tableModel.setRowCount(datas.length);       // 设置表格的行数
        tableSubject.setRowHeight(50);      // 设置行高30像素
        // 注意，向JScrollPane中添加控件只能在初始化的时候添加
        scrollPaneSubject = new JScrollPane(tableSubject);

        // 将   表格  添加到对应的面板中，这里使用的是  BorderLayout   ，添加到中间即可
        this.add(scrollPaneSubject);
//        scrollPaneSubject.add(tableSubject);       // 将表格添加到滚动面板中----》   无效
        for (int row = 0; row < datas.length; row++) {
            for (int column = 0; column< datas[0].length; column++) {
                tableSubject.setValueAt(datas[row][column], row, column);     // 注： Swing 表格组件中，行列都是从0开始的
            }
        }
        tableSubject.setVisible(true);
//        tableModel.fireTableDataChanged();      // 更新表格的信息，十分必要，否则可能显示错误

    }

    /**
    * @Description: 窗体关闭时执行的方法，设置父窗体可以操作
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/16
    */
    private void thisWindowClosed(WindowEvent e) {
        // TODO add your code here
        // 设置 父窗口 可以操作
        fatherFrame.setEnabled(true);
    }

    /**
    * @Description: 点击  “筛选试题”  对应的点击事件，可以直接创建 subjectadministration 包下的 SearchSubject 类来实现
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/16
    */
    private void buttonSearchSubjectMouseReleased(MouseEvent e) {
        // TODO add your code here
        SearchSubject searchSubject = new SearchSubject(this, tableModel);
        searchSubject.setVisible(true);

        // 设置当前窗体不可操作，当子窗体操作完成之后才可以继续操作
        this.setEnabled(false);

    }

    /**
    * @Description: 点击   “添加选中的试题到试卷中”  对应的点击事件
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/16
    */
    private void buttonAddSubjectMouseReleased(MouseEvent e) {
        // TODO add your code here
        // 获得选中了哪些行,选中第一行，对应的下标为0
        int[] selectedRows = tableSubject.getSelectedRows();

        // 因为没有选中表格任何行数据时，getSelectedRows() 返回的是 new Int[0]     0个元素的数组
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "没有选中题目！！可以安装Ctrl + 鼠标左键选择多个需要删除的题目！", "错误", JOptionPane.ERROR_MESSAGE);
            return;     // 直接返回
        }

        /*
        *       1. 获取选中行的 title，然后将这些 title 添加到数据库的  subjectTitle 字段中，每一个 title 之间用 - 间隔
        *       2. 同时还有检测 title，如果该试题已经添加到了数据库中，则提示用户
        * */
        // 1.获取选中的 title
        String[] titles = new String[selectedRows.length];
        for (int i = 0; i < selectedRows.length; i++) {
            titles[i] = tableModel.getValueAt(selectedRows[i], 0).toString();
            System.out.println("已经选中的title有：" + titles[i]);
        }

        // 2.检测是否已经添加过该题目
//        String tempStr = tableModel.getValueAt()



    }

    /**
    * @Description: 点击   “确认”   按钮对应的点击事件，关闭当前窗口
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/16
    */
    private void buttonSureMouseReleased(MouseEvent e) {
        // TODO add your code here
        // 关闭当前窗口
        this.dispose();
    }

    /**
    * @Description: 自动生成的初始化代码
    * @Param: []
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/16
    */
    private void initComponents() {

        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Lin Kai
        panel1 = new JPanel();
        label1 = new JLabel();
        buttonAddSubject = new JButton();
        buttonSearchSubject = new JButton();
        panelCenter = new JPanel();
        panel2 = new JPanel();
        label2 = new JLabel();
        buttonSure = new JButton();

        //======== this ========
        setTitle("\u6dfb\u52a0\u8bd5\u5377");
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
            panel1.setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border. EmptyBorder
            ( 0, 0, 0, 0) , "JF\u006frmDes\u0069gner \u0045valua\u0074ion", javax. swing. border. TitledBorder. CENTER, javax. swing. border
            . TitledBorder. BOTTOM, new java .awt .Font ("D\u0069alog" ,java .awt .Font .BOLD ,12 ), java. awt
            . Color. red) ,panel1. getBorder( )) ); panel1. addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void
            propertyChange (java .beans .PropertyChangeEvent e) {if ("\u0062order" .equals (e .getPropertyName () )) throw new RuntimeException( )
            ; }} );
            panel1.setLayout(null);

            //---- label1 ----
            label1.setText(" ");
            label1.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
            panel1.add(label1);
            label1.setBounds(0, 0, 20, 85);

            //---- buttonAddSubject ----
            buttonAddSubject.setText("\u6dfb\u52a0\u9009\u4e2d\u7684\u8bd5\u9898\u5230\u8bd5\u5377\u4e2d");
            buttonAddSubject.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
            buttonAddSubject.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    buttonAddSubjectMouseReleased(e);
                }
            });
            panel1.add(buttonAddSubject);
            buttonAddSubject.setBounds(575, 20, 219, 45);

            //---- buttonSearchSubject ----
            buttonSearchSubject.setText("\u7b5b\u9009\u8bd5\u9898");
            buttonSearchSubject.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
            buttonSearchSubject.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    buttonSearchSubjectMouseReleased(e);
                }
            });
            panel1.add(buttonSearchSubject);
            buttonSearchSubject.setBounds(85, 20, 115, 45);

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

        //======== panelCenter ========
        {
            panelCenter.setLayout(null);

            //======== panel2 ========
            {
                panel2.setLayout(null);

                //---- label2 ----
                label2.setText(" ");
                label2.setFont(new Font(Font.DIALOG, Font.BOLD, 48));
                panel2.add(label2);
                label2.setBounds(0, 0, label2.getPreferredSize().width, 67);

                //---- buttonSure ----
                buttonSure.setText("\u786e\u8ba4");
                buttonSure.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
                buttonSure.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        buttonSureMouseReleased(e);
                    }
                });
                panel2.add(buttonSure);
                buttonSure.setBounds(380, 10, 125, 50);

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
            panelCenter.add(panel2);
            panel2.setBounds(0, 435, 878, panel2.getPreferredSize().height);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < panelCenter.getComponentCount(); i++) {
                    Rectangle bounds = panelCenter.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panelCenter.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panelCenter.setMinimumSize(preferredSize);
                panelCenter.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(panelCenter, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents


        /**
         * @Description: ######################################    下面是自己添加的代码       #######################3
         * @Param: []
         * @return: void
         * @Author: 林凯
         * @Date: 2019/11/16
         */

        this.setBounds(490, 170, 1100, 730);
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Lin Kai
    private JPanel panel1;
    private JLabel label1;
    private JButton buttonAddSubject;
    private JButton buttonSearchSubject;
    private JPanel panelCenter;
    private JPanel panel2;
    private JLabel label2;
    private JButton buttonSure;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
