package facerecognition;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 *         相机测试
 *          2019年12月4日22:53:45
 *          有用，可以拍照，剩下的就是人脸识别了
 *
 *          实现思路：
 *          实际上是一个 github 上的项目： https://github.com/sarxos/webcam-capture
 *          CSDN关于这个项目的介绍： https://blog.csdn.net/qq_22078107/article/details/85927626
 *          需要的jar包： webcam-capture-0.3.12
 *          jar包路径：(D:\迅雷下载\Java的jar包，资料等\java调用摄像头拍照\webcam-capture-0.3.12-dist)
 *
 */

public class WebcamCapture {

    private static JFrame window;

    public static void main(String[] args)throws InterruptedException {

        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());

        WebcamPanel panel = new WebcamPanel(webcam);            // 创建拍照面板
        panel.setFPSDisplayed(true);
        panel.setDisplayDebugInfo(true);
        panel.setImageSizeDisplayed(true);
        panel.setMirrored(true);

        JFrame window = new JFrame("Test webcam panel");        // 创建一个拍照的窗体
        window.add(panel);          // 将拍照面板添加到窗体中
        window.setResizable(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);
        window.setBounds(300, 150, 1200, 800);




        /**
         *      向窗体中添加一个 button，用来拍照
         * */
        final JButton button = new JButton("拍照");
        button.setSize(50, 100);
        button.setFont(new Font("微软雅黑", Font.BOLD, 32));
        window.add(panel, BorderLayout.CENTER);
        window.add(button, BorderLayout.SOUTH);
        window.setResizable(true);
        window.pack();
        window.setVisible(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button.setEnabled(false);  //设置按钮不可点击
                //实现拍照保存-------start（存放的路径为src/images）
                String fileName = "src//images//" + System.currentTimeMillis();       //保存路径即图片名称（不用加后缀）
                System.out.println(fileName);

                /**
                 *      关键代码，通过多线程实现拍照
                 * */
                WebcamUtils.capture(webcam, fileName, ImageUtils.FORMAT_PNG);
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run()
                    {
                        JOptionPane.showMessageDialog(null, "拍照成功");
                        button.setEnabled(true);    //设置按钮可点击

                        return;
                    }
                });
                // 到这里结束
            }
        });

    }
}