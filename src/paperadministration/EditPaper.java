/*
 * Created by JFormDesigner on Sun Nov 17 19:15:56 CST 2019
 */

package paperadministration;

import java.awt.event.*;
import javabean.Subject;
import jdbc.PaperJDBC;
import jdbc.SubjectJDBC;
import org.apache.commons.lang.text.StrBuilder;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * @author Lin Kai
 */
public class EditPaper extends JFrame {
    private JFrame fatherFrame;     // 父窗口的引用
    private DefaultTableModel tableModelOfFather;       // 父窗口的表格模型的引用
    private SubjectJDBC subjectJDBC;
    int row;

    // 自己添加的控件（添加一些 JFormdesigner 不方便操作的空间）
    private JTable tableSubject;
    private JScrollPane scrollPaneSubject;
    private DefaultTableModel tableModel;

    public EditPaper(JFrame fatherFrame, DefaultTableModel tableModelOfFather, int row) {
        this.fatherFrame = fatherFrame;
        this.tableModelOfFather = tableModelOfFather;
        this.row = row;
        subjectJDBC = new SubjectJDBC();
        initComponents();
        initJTabel();       // 初始化表格
    }

    /** 
    * @Description: 初始化题目表格，表格中的数据是当前编辑试卷里面所含有的试题
    * @Param: [] 
    * @return: void 
    * @Author: 林凯
    * @Date: 2019/11/17 
    */ 
    private void initJTabel() {
        String[] columNames = {"题目编号(标题)", "题目类型", "题目内容", "选项A", "选项B", "选项C", "选项D", "正确答案", "真假值", "题目备注", "最后修改时间"};
        String tempTitle = tableModelOfFather.getValueAt(row, 2).toString();
        System.out.println(tempTitle);
        // 由于是把所有包含的试题编号（标题）放在一个字符串里面，用 - 分隔，所以取出来的时候也要分隔取出
        String[] titles = tempTitle.split("-");
        for (int i = 0; i < titles.length; i++) {
            System.out.println(titles[i]);
        }

        StringBuilder sql = new StringBuilder("select * from subject where title = " + titles[0] + " ");

        // 这里的下标从 1 开始
        for (int i = 1; i < titles.length; i++) {
            sql.append("or title = " + titles[i] + " ");
        }

        System.out.println(sql.toString());
        String[][] datas = subjectJDBC.readSubject(sql.toString());
        System.out.println("datas的长度" + datas.length + "  " + datas[0].length);

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
        tableModel.fireTableDataChanged();      // 更新表格的信息，十分必要，否则可能显示错误
    }

    /**
    * @Description: 点击  “完成”  按钮对应的点击事件
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/17
    */
    private void buttonSureMouseReleased(MouseEvent e) {
        // TODO add your code here
        this.dispose();
    }

    /**
    * @Description: 窗体关闭时候调用的方法，使父窗口可以编辑
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/17
    */
    private void thisWindowClosed(WindowEvent e) {
        // TODO add your code here
        fatherFrame.setEnabled(true);
    }

    /**
    * @Description: 点击   “添加新的试题到试卷中”  对应的按钮的点击事件
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/17
    */
    private void buttonAddMouseReleased(MouseEvent e) {
        // TODO add your code here
        AddSubjectToPaper addSubjectToPaper = new AddSubjectToPaper(this, tableModel);
        addSubjectToPaper.setVisible(true);
        this.setEnabled(false);     // 设置当前窗口不可编辑
    }

    /**
    * @Description: 初始化窗体，由 JFrameDesigner 自动生成
    * @Param: []
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/17
    */
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Lin Kai
        panel1 = new JPanel();
        label1 = new JLabel();
        buttonAdd = new JButton();
        buttonDelete = new JButton();
        buttonSure = new JButton();
        label2 = new JLabel();
        panelCenter = new JPanel();

        //======== this ========
        setTitle("\u7f16\u8f91\u8bd5\u5377");
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
            panel1.setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border. EmptyBorder(
            0, 0, 0, 0) , "JF\u006frmD\u0065sig\u006eer \u0045val\u0075ati\u006fn", javax. swing. border. TitledBorder. CENTER, javax. swing. border. TitledBorder
            . BOTTOM, new java .awt .Font ("Dia\u006cog" ,java .awt .Font .BOLD ,12 ), java. awt. Color.
            red) ,panel1. getBorder( )) ); panel1. addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java .
            beans .PropertyChangeEvent e) {if ("\u0062ord\u0065r" .equals (e .getPropertyName () )) throw new RuntimeException( ); }} );
            panel1.setLayout(null);

            //---- label1 ----
            label1.setText(" ");
            panel1.add(label1);
            label1.setBounds(0, 0, label1.getPreferredSize().width, 80);

            //---- buttonAdd ----
            buttonAdd.setText("\u6dfb\u52a0\u65b0\u7684\u8bd5\u9898\u5230\u8bd5\u5377\u4e2d");
            buttonAdd.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
            buttonAdd.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    buttonAddMouseReleased(e);
                }
            });
            panel1.add(buttonAdd);
            buttonAdd.setBounds(365, 20, 235, 40);

            //---- buttonDelete ----
            buttonDelete.setText("\u4ece\u8bd5\u5377\u4e2d\u5220\u9664\u9009\u4e2d\u8bd5\u9898");
            buttonDelete.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
            panel1.add(buttonDelete);
            buttonDelete.setBounds(620, 20, 235, 40);

            //---- buttonSure ----
            buttonSure.setText("\u5b8c\u6210");
            buttonSure.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
            buttonSure.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    buttonSureMouseReleased(e);
                }
            });
            panel1.add(buttonSure);
            buttonSure.setBounds(870, 20, 95, 40);

            //---- label2 ----
            label2.setText("\u4e0b\u9762\u662f\u8be5\u8bd5\u5377\u4e2d\u5df2\u7ecf\u5b58\u5728\u7684\u8bd5\u9898");
            label2.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
            panel1.add(label2);
            label2.setBounds(15, 20, 310, 40);

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

//        this.setBounds(330, 150, 1060, 666);
        this.setBounds(490, 170, 1100, 730);

    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Lin Kai
    private JPanel panel1;
    private JLabel label1;
    private JButton buttonAdd;
    private JButton buttonDelete;
    private JButton buttonSure;
    private JLabel label2;
    private JPanel panelCenter;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
