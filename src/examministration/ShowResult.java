/*
 * Created by JFormDesigner on Mon Dec 02 20:30:45 CST 2019
 */

package examministration;

/**
*           2019年12月2日20:41:32
*           用来展示考试结果的界面，父窗口是 startExam 界面
*           在 startExam 类中被创建对象并调用方法
* */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author Lin Kai
 */
public class ShowResult extends JFrame {
    private JFrame fatherFrame;     // 父窗口的引用
    private int totalSocre;         // 试卷的总分
    private int userScore;      // 学生的得分

    public ShowResult(JFrame fatherFrame, int totalSocre, int userScore) {
        this.fatherFrame = fatherFrame;
        this.totalSocre = totalSocre;
        this.userScore = userScore;
        initComponents();
    }

    /**
    * @Description: 确定按钮的点击事件，退出窗口
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/12/2
    */
    private void button1MouseReleased(MouseEvent e) {
        // TODO add your code here
        fatherFrame.setEnabled(true);       // 设置父窗口可编辑（父窗口就是 StartExam 对应的窗口）
        this.dispose();

    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Lin Kai
        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();
        button1 = new JButton();

        //======== this ========
        setTitle("\u8003\u8bd5\u7ed3\u679c");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- label1 ----
        label1.setText("\u606d\u559c\u60a8\u5b8c\u6210\u4e86\u8003\u8bd5\uff01\uff01\uff01");
        label1.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        contentPane.add(label1);
        label1.setBounds(45, 15, 465, 55);

        //---- label2 ----
        label2.setText("\u603b\u5206\u662f100\u5206\uff0c\u60a8\u7684\u5f97\u5206\u662f\uff1a");
        label2.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        contentPane.add(label2);
        label2.setBounds(40, 75, 390, 40);

        //---- label3 ----
        label3.setText("100");
        label3.setFont(new Font(Font.DIALOG, Font.BOLD, 100));
        label3.setForeground(Color.red);
        contentPane.add(label3);
        label3.setBounds(245, 140, 235, 235);

        //---- button1 ----
        button1.setText("\u786e\u5b9a");
        button1.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                button1MouseReleased(e);
            }
        });
        contentPane.add(button1);
        button1.setBounds(225, 420, 205, 65);

        contentPane.setPreferredSize(new Dimension(660, 540));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        this.setSize(660, 540);
        label2.setText("总分是：" + totalSocre + " 您的得分是：");
        label3.setText(String.valueOf(userScore));
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Lin Kai
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JButton button1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


//    public static void main(String[] args) {
//        ShowResult showResult = new ShowResult();
//        showResult.setVisible(true);
//    }
}
