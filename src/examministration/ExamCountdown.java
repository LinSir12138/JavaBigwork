package examministration;

/*
*       2019年12月2日12:20:51
*       倒计时类，用来在 startExam  界面中显示的倒计时，利用多线程
* */
////////////////////////////////////////////////////////////////////
//                          _ooOoo_                               //
//                         o8888888o                              //
//                         88" . "88                              //
//                         (| ^_^ |)                              //
//                         O\  =  /O                              //
//                      ____/`---'\____                           //
//                    .'  \\|     |//  `.                         //
//                   /  \\|||  :  |||//  \                        //
//                  /  _||||| -:- |||||-  \                       //
//                  |   | \\\  -  /// |   |                       //
//                  | \_|  ''\---/''  |   |                       //
//                  \  .-\__  `-`  ___/-. /                       //
//                ___`. .'  /--.--\  `. . ___                     //
//              ."" '<  `.___\_<|>_/___.'  >'"".                  //
//            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
//            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
//      ========`-.____`-.___\_____/___.-`____.-'========         //
//                           `=---='                              //
//      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
//                  佛祖保佑       永不宕机     永无BUG              //
////////////////////////////////////////////////////////////////////


import javax.swing.*;

public class ExamCountdown implements Runnable {
    private JLabel labelTime;       // 对应 startExam  界面中的倒计时的标签的引用
    private long endTime;           // 考试结束时间
    private long minutes;            // 倒计时分钟数
    private long seconds;            // 倒计时秒数

    public ExamCountdown(JLabel labelTime, long endTime) {
        this.labelTime = labelTime;
        this.endTime = endTime;
    }

    @Override
    public void run() {
        System.out.println("run()");
        minutes = (endTime - System.currentTimeMillis()) / (60 * 1000);
        seconds = ((endTime - System.currentTimeMillis()) % (60 * 1000)) / 1000;

        while ((endTime - System.currentTimeMillis()) > 0) {
            try {
                Thread.sleep(1000);
                if (seconds == 0) {
                    seconds = 59;
                    minutes--;
                } else {
                    seconds--;
                }
                labelTime.setText("剩余时间：" + minutes + " 分钟 " + seconds + " 秒");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
