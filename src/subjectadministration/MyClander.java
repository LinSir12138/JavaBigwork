/*
 * Created by JFormDesigner on Sun Nov 10 10:54:58 CST 2019
 */

package subjectadministration;

/*
*       2019年11月10日11:32:29
*       对应UI界面：在 “试题管理” 界面点击 “查找试题” ，然后在点击 “选择时间”按钮弹出来的界面
*       功能：进行时间的选择
*       调用关系：被 SearchSubject 所调用
* */

import java.awt.event.*;

import mytest.DateChooserJButton;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * @author Lin Kai
 */
public class MyClander extends JFrame {
    private JTextField textField;

    public MyClander(JTextField textField) {
        initComponents();
        this.textField = textField;
    }

    /**
    * @Description: 点击确认按钮
    * @Param: [e]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/11/10
    */
    private void buttonSureMouseReleased(MouseEvent e) {
        // TODO add your code here
        String date = buttonClander.getText();
        textField.setText(date);
        this.dispose();
    }


    private void buttonCancleMouseReleased(MouseEvent e) {
        // TODO add your code here
        this.dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Lin Kai
        buttonSure = new JButton();
        buttonCancle = new JButton();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- buttonSure ----
        buttonSure.setText("\u786e\u5b9a");
        buttonSure.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        buttonSure.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                buttonSureMouseReleased(e);
            }
        });
        contentPane.add(buttonSure);
        buttonSure.setBounds(80, 355, 155, 55);

        //---- buttonCancle ----
        buttonCancle.setText("\u53d6\u6d88");
        buttonCancle.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        buttonCancle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                buttonCancleMouseReleased(e);
            }
        });
        contentPane.add(buttonCancle);
        buttonCancle.setBounds(345, 355, 155, 55);

        contentPane.setPreferredSize(new Dimension(585, 480));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

//        ################     MyCode       #############33
        contentPane.setSize(585, 480);
        buttonClander = new DateChooserJButton();
        buttonClander.setBounds(80, 40, 400, 300);
        buttonClander.setBorder(new LineBorder(Color.BLACK, 1, false));
        buttonClander.setFont(new Font("微软雅黑", Font.BOLD, 32));
        contentPane.add(buttonClander);
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Lin Kai
    private JButton buttonSure;
    private JButton buttonCancle;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private JButton buttonClander;

    public static void main(String[] args) {
//        MyClander myClander = new MyClander();
//        myClander.setVisible(true);
    }
}
