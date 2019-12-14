package mytest;

import javax.swing.*;

public class Test14 {
    public JButton button;

    public void test() {
        fun(button);
        if (button == null) {
            System.out.println("Error");
        }
    }

    public void fun(JButton button) {
        button = new JButton();
        button.setText("40");
    }

    public static void main(String[] args) {
        Test14 test14 = new Test14();
        test14.test();
    }
}
