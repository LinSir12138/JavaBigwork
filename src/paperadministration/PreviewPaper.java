/*
 * Created by JFormDesigner on Tue Nov 26 20:21:53 CST 2019
 */

package paperadministration;

import java.awt.*;
import javax.swing.*;
import info.clearthought.layout.*;

/**
 * @author Lin Kai
 */
public class PreviewPaper extends JFrame {
    public PreviewPaper() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Lin Kai
        panel2 = new JPanel();
        label5 = new JLabel();
        panel3 = new JPanel();
        label3 = new JLabel();
        label4 = new JLabel();
        panel4Center = new JPanel();
        panelLeft = new JPanel();
        scrollPane1 = new JScrollPane();
        panel4 = new JPanel();
        button2 = new JButton();
        button4 = new JButton();
        button3 = new JButton();
        button1 = new JButton();

        //======== this ========
        setTitle("\u9884\u89c8\u8bd5\u5377");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel2 ========
        {
            panel2.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border
            .EmptyBorder(0,0,0,0), "JF\u006frmD\u0065sig\u006eer \u0045val\u0075ati\u006fn",javax.swing.border.TitledBorder.CENTER,javax
            .swing.border.TitledBorder.BOTTOM,new java.awt.Font("Dia\u006cog",java.awt.Font.BOLD,
            12),java.awt.Color.red),panel2. getBorder()));panel2. addPropertyChangeListener(new java.beans
            .PropertyChangeListener(){@Override public void propertyChange(java.beans.PropertyChangeEvent e){if("\u0062ord\u0065r".equals(e.
            getPropertyName()))throw new RuntimeException();}});
            panel2.setLayout(null);

            //---- label5 ----
            label5.setText(" ");
            panel2.add(label5);
            label5.setBounds(40, 35, label5.getPreferredSize().width, 75);

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
        contentPane.add(panel2, BorderLayout.NORTH);

        //======== panel3 ========
        {
            panel3.setLayout(new BorderLayout());

            //---- label3 ----
            label3.setText(" ");
            label3.setFont(new Font(Font.DIALOG, Font.BOLD, 48));
            panel3.add(label3, BorderLayout.NORTH);

            //---- label4 ----
            label4.setText(" ");
            label4.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
            panel3.add(label4, BorderLayout.CENTER);
        }
        contentPane.add(panel3, BorderLayout.SOUTH);

        //======== panel4Center ========
        {
            panel4Center.setLayout(null);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < panel4Center.getComponentCount(); i++) {
                    Rectangle bounds = panel4Center.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panel4Center.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panel4Center.setMinimumSize(preferredSize);
                panel4Center.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(panel4Center, BorderLayout.CENTER);

        //======== panelLeft ========
        {
            panelLeft.setAutoscrolls(true);
            panelLeft.setLayout(new BorderLayout());

            //======== scrollPane1 ========
            {

                //======== panel4 ========
                {
                    panel4.setLayout(new TableLayout(new double[][] {
                        {TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED},
                        {TableLayout.PREFERRED, TableLayout.PREFERRED}}));
                    ((TableLayout)panel4.getLayout()).setHGap(5);
                    ((TableLayout)panel4.getLayout()).setVGap(5);

                    //---- button2 ----
                    button2.setText("text");
                    panel4.add(button2, new TableLayoutConstraints(0, 0, 0, 0, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

                    //---- button4 ----
                    button4.setText("text");
                    panel4.add(button4, new TableLayoutConstraints(1, 0, 1, 0, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

                    //---- button3 ----
                    button3.setText("text");
                    panel4.add(button3, new TableLayoutConstraints(2, 0, 2, 0, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

                    //---- button1 ----
                    button1.setText("text");
                    panel4.add(button1, new TableLayoutConstraints(0, 1, 0, 1, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
                }
                scrollPane1.setViewportView(panel4);
            }
            panelLeft.add(scrollPane1, BorderLayout.CENTER);
        }
        contentPane.add(panelLeft, BorderLayout.WEST);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents


        /**
        * @Description: ##################################################3  自己敲的代码    ######################################
        * @Param: []
        * @return: void
        * @Author: 林凯
        * @Date: 2019/11/26
        */

        JButton button1 = new JButton("test01");
        JButton button2 = new JButton("test01");
        JButton button3 = new JButton("test01");
        JButton button4 = new JButton("test01");
        JButton button5 = new JButton("test01");
        panel4.add(button1);
        panel4.add(button2);
        panel4.add(button3);
        panel4.add(button4);
        panel4.add(button5);

    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Lin Kai
    private JPanel panel2;
    private JLabel label5;
    private JPanel panel3;
    private JLabel label3;
    private JLabel label4;
    private JPanel panel4Center;
    private JPanel panelLeft;
    private JScrollPane scrollPane1;
    private JPanel panel4;
    private JButton button2;
    private JButton button4;
    private JButton button3;
    private JButton button1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


    public static void main(String[] args) {
        PreviewPaper perviewPaper = new PreviewPaper();
        perviewPaper.setVisible(true);
    }
}
