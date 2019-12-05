package facerecognition;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;
import com.sun.jna.platform.unix.X11;
import jdbc.ImageJDBC;
import jdbc.UserJDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


/**
 *         相机测试
 *          功能：
 *          1. 可以调用摄像头拍照并保存到本地 ------》 takePhote() 方法
 *          2. 可以从数据库中读取用户的图片进行人脸识别验证-------》 startRecognition() 方法
 *              在该方法中，会自动调用 FaceMatch() 方法，传入的参数是2张本地图片的路径，返回值是 resltul 字符串，
 *              里面包含着识别的分数 score，分数score越高，说明越相似
 *
 *          注： 数据库中读取的图片名称为 ：  id.jpg       id是整数，对应图片所在行的id
 *               本地图片的名称是：  当前时间毫秒.jpg     随机数在方法内部自动生成
 *
 *          实现思路：
 *          实际上是一个 github 上的项目： https://github.com/sarxos/webcam-capture
 *          CSDN关于这个项目的介绍： https://blog.csdn.net/qq_22078107/article/details/85927626
 *          需要的jar包： webcam-capture-0.3.12
 *          jar包路径：(D:\迅雷下载\Java的jar包，资料等\java调用摄像头拍照\webcam-capture-0.3.12-dist)
 *          2019年12月4日22:53:45
 *
 */

public class WebcamCapture {

    /**
     *      解决参数传递问题：MainUI中传入一个this对象，然后点击按钮是调用 startRecognition（），
     *      在根据结果调用 MainUI里面的方法，在mainUI里面的方法弹出考试界面
     * */

    private static void startRecognition(String userName, long randomNumber) {
        //
        // 参数：
        //
        // 1 ~~~~~~ userName ，用来在数据库中找到usreName对应的 id，然后根据id确定从数据库中读取的图片的名称

        /**
         *    拍照完毕，
         *    参数：
         *      （1） ~~~~~~ userName ，用来在数据库中找到usreName对应的 id，然后根据id确定从数据库中读取的图片的名称
         *      （2）~~~~~ randomNumber   拍照生成的图片的名称
         *    1.从数据库中读取用户的头像信息到本地
         * */
        UserJDBC userJDBC = new UserJDBC();
        String id =  userJDBC.readIdByUserName(userName);           // 从数据库中通过用户名获得id
        System.out.println("id = " + id);
        ImageJDBC.readImageFromDatabase("src//images//", id);           // 从数据库中读取图片保存到本地

        String filePath1 = "src//images//" +id + ".png";
        String filePath2 = "src//images//" + randomNumber + ".png";
        String result = FaceMatch.faceMatch(filePath1, filePath2);
        System.out.println(result);


    }

    public static void takePhote(String userName) throws InterruptedException {
        /**
         *      循环判断条件，为false时，会一直停留在循环里面，方法不会执行完毕
         *      只有当按下拍照按钮时，拍照完成才结束方法
         * */
        final long[] randomNumber = {0};          // 生成的随机数，作为拍照生成图片的名称
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());
//        webcam.close()

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
        window.setBounds(300, 150, 1500, 1000);
        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        /**
         *      给本窗口添加关闭事件（点击右上角的关闭按钮）
         *      因为摄像头不会自动关闭，所以要在这里手动关闭摄像头
         * */
        window.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {

            }

            @Override
            public void windowClosed(WindowEvent e) {
                webcam.close();     // 关闭摄像头
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });

        /**
         *      向窗体中添加一个 button，用来拍照
         * */
        JButton button = new JButton("拍照");
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
                randomNumber[0] = System.currentTimeMillis();
                String fileName = "src//images//" + randomNumber[0] + ".png";       //保存路径即图片名称（不用加后缀）
                System.out.println(fileName);

                /**
                 *      关键代码，通过多线程实现拍照
                 * */
                WebcamUtils.capture(webcam, fileName, ImageUtils.FORMAT_PNG);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run()
                    {
                        JOptionPane.showMessageDialog(null, "拍照成功");
                        button.setEnabled(true);    //设置按钮可点击
                        startRecognition(userName, randomNumber[0]);

                        /**
                         *       // 释放资源，拍照摄像头关闭，窗口关闭
                         * */
                        webcam.close();
                        window.dispose();
                        return;
                    }
                });
                // 到这里拍照完毕
            }
        });

    }
}