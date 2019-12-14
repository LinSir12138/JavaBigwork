package facerecognition;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;
import com.sun.jna.platform.unix.X11;
import jdbc.ImageJDBC;
import jdbc.UserJDBC;
import main.MainUI;

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


    /**
    * @Description: 调用摄像头进行拍照 ，然后将拍照完成的图片保存到本地（利用到了IO流）
    * @Param: [mainUIFrame, userName]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/12/13
    */
    public static void takePhote(JFrame mainUIFrame, String userName) throws InterruptedException {
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
                        startRecognition(mainUIFrame ,userName, randomNumber[0]);

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

    /**
    * @Description: 拍照完成之后，将本地图片和从数据库中读取的标准人脸进行比对，获得返回值，并对返回值
     *              进行分析，得到评判的得分。
     *              功能2： 从 数据库中获取图片到本地
    * @Param: [mainUIFrame, userName, randomNumber]
    * @return: void
    * @Author: 林凯
    * @Date: 2019/12/13
    */
    private static void startRecognition(JFrame mainUIFrame, String userName, long randomNumber) {
        //
        // 参数：
        //
        // 1 ~~~~~~ userName ，用来在数据库中找到usreName对应的 id，然后根据id确定从数据库中读取的图片的名称

        /**
         *    拍照完毕，从数据库中读取用户的头像信息到本地
         *    参数：
         *      （1） ~~~~~~ userName ，用来在数据库中找到usreName对应的 id，然后根据id确定从数据库中读取的图片的名称
         *      （2）~~~~~ randomNumber   拍照生成的图片的名称
         *
         * */
        UserJDBC userJDBC = new UserJDBC();
        String id =  userJDBC.readIdByUserName(userName);           // 从数据库中通过用户名获得id
        System.out.println("id = " + id);
        ImageJDBC.readImageFromDatabase("src//images//", id);           // 从数据库中读取图片保存到本地

        String filePath1 = "src//images//" +id + ".png";
        System.out.println("file1:" + filePath1);
        String filePath2 = "src//images//" + randomNumber + ".png";
        System.out.println("file2:" + filePath2);
        String result = FaceMatch.faceMatch(filePath1, filePath2);
//        System.out.println(result);

        double score =  getScoreFromResult(result);
        System.out.println("得分为：" + score);
        MainUI mainUI = (MainUI) mainUIFrame;

        if (score >= 80) {
            mainUI.faceMatching(true);
        } else {
            mainUI.faceMatching(false);
        }


    }

    /**
    * @Description: 从人脸识别的结果中获得 得分，即score后面的一串数字
    * @Param: []
    * @return: int
    * @Author: 林凯
    * @Date: 2019/12/5
    */
    private static Double getScoreFromResult(String result) {
        /**
         *       返回的 result 字符串是以json格式返回的，如下：
         *   {"error_code":0,"error_msg":"SUCCESS","log_id":2019910184201,"timestamp":1575526526,"cached":0,
         *      "result":{"score":92.98625183,"face_list":[{"face_token":"76c01de52392ab95553295ab685f7c76"},
         *      {"face_token":"76b0fb479b396bceb4d5a04d7868fa3c"}]}}
         *
         *      完全检测不到人脸是，返回的 score 里面的值为 0，返回的 json 格式如下：
         *      {"error_code":0,"error_msg":"SUCCESS","log_id":9989791520179,"timestamp":1576239669,"cached":0,
         *      "result":{"score":0,"face_list":[{"face_token":"76c01de52392ab95553295ab685f7c76"},
         *      {"face_token":"a1ff0cef1a6470bbbb32c53aa9224753"}]}}
         *
         *      当是其他人脸的时候，返回的 score 值特别低(例如这里的就是)，返回的 json 格式如下：
         *      {"error_code":0,"error_msg":"SUCCESS","log_id":9425059425253,"timestamp":1576239859,"cached":0,
         *      "result":{"score":76.13053131,"face_list":[{"face_token":"76c01de52392ab95553295ab685f7c76"},
         *      {"face_token":"37dfe52754865fbd5b0e282a093cc348"}]}}
         *
         * */

        int firstIndex = result.indexOf("score") + 7;
        int lastIndex = result.indexOf("score") + 7 + 11;
        String score = result.substring(firstIndex, lastIndex);

        /**
         *      如果未检测到人脸，则score == 0，那么就会截取到其他英文字母，用Double.valueOf() 时会抛出异常，这里要先处理一下
         * */
        if (score.charAt(0) == '0') {
            return 0.0;
        }
        System.out.println("得分为：" + score);
        return Double.valueOf(score);
    }

}