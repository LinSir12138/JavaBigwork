package TestALY;

/*
*       /*
 *       2019年10月11日22:18:15
 *       测试用Java发送手机验证码短信
 *       使用的是阿里云的API ，是从阿里云市场购买的专门提供这种服务的产品，调用他们的API
 *       -----------------------——》》》已经完成封装可以使用
 * */

import mytest.SendMessage;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SendMessageByALBB {
    private String phoneNumber;     // 手机号码用字符串来表示
    private long randomNumber;      // 生成的随机数

    public SendMessageByALBB() {
        // 如果没有给构造函数传递参数，就默认给自己的手机号码发送验证码
        this.phoneNumber = "15970819628";       // 自己的电信手机号码为：19970411540
    }

    public SendMessageByALBB(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getRandomNumber() {
        return randomNumber;
    }

    // 生成随机数
    public long myRandom() {
        Random random = new Random();
        long randNumber = random.nextInt(900000) + 100000;
        System.out.println(randNumber);
        return randNumber;
    }

    // 发送验证码
    public void send() {
        String host = "http://yzx.market.alicloudapi.com";
        String path = "/yzx/sendSms";
        String method = "POST";
        String appcode = "d805ead9c45d40b28662e4ea9ea65abb";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phoneNumber);
        randomNumber = myRandom();
//        querys.put("param", "code:8888");
        querys.put("param", "code:"+ randomNumber);
        System.out.println(randomNumber);
        querys.put("tpl_id", "TP1710262");
//        querys.put("tpl_id", "TP1910128");            // 这个是申请的模版，但是不能使用
        Map<String, String> bodys = new HashMap<String, String>();

        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SendMessageByALBB sendByALBB = new SendMessageByALBB("15970819628");
        sendByALBB.send();
    }
}
