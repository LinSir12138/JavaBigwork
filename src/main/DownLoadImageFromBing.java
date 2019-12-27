package main;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *      利用网络+正则表达式，从Bing的官网爬取图片的连接，然后通过IO流将图片下载到本地。
 *      关键知识点：
 *      （1）根据网页URl获取网页源码，封装在 getURLContent() 方法内部
 *      （2）从网页的源代码中提取我们需要的图片的连接，利用正则表达式。
 *          这里完全要看源代码的具体内容来写，方便的是，Bing把背景图片放在了第一个 Link 标签的 href 属性里面，
 *          所以只要简单的利用正则表达式就可以获取。
 *       （3）根据图片URL下载图片。
 *       注意：这里和获取网页源码不太一样，获取网页源码是直接创建URL对象，然后直接打开输入流就行
 *              而这里获取图片，还需要发送类似于 Http 请求子类的东西，就是封装在 getHttpURLConnection() 方法内，
 *              这里面的请求看不太懂，先暂时了解就好，之后在去学习
 *             ① 首先要获得和该图片链接，即获得 HttpURLConnection，这步封装在 getHttpURLConnection() 方法内部
 *             ② 获得 HttpURLConnection 之后，就是简单的 IO 操作了。
 *      2019年12月26日23:42:22
 * */

/**
 *      2019年12月27日00:29:41
 *      关于利用正则表达式去解析源码的部分有点生疏，明天还需要在看一看。或者，今天晚上吧关键代码敲完？
 * */

public class DownLoadImageFromBing {

    /**
     * @Description: 获得URL对应的网页源码内容，第一个参数： 网页URL，第2个参数：字符集编码
     * @Param: [urlStr]
     * @return: java.lang.String
     * @Author: 林凯
     * @Date: 2019/11/3
     */
    public static String getURLContent(String urlStr, String charsetName) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlStr);

            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), charsetName));        // 打开一个输入流
            String temp = "";
            while (null != (temp = reader.readLine())) {
//                System.out.println(temp);
                sb.append(temp);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /** 
    * @Description: 取得所有匹配的字符串，因为 Link 标签里面的 href 属性可能有多个，这里一次性把全部的属性取值都取过来
    * @Param: [destStr, regexStr] 
    * @return: java.util.List<java.lang.String> 
    * @Author: 林凯
    * @Date: 2019/12/26 
    */ 
    public static List<String> getMatherSubstrs(String destStr, String regexStr) {
        Pattern p = Pattern.compile(regexStr);      // 调用静态方法，创建 Pattern 对象
        Matcher m = p.matcher(destStr);         // 通过 Pattern 对象获得 Matcher 对象
        List<String> restult = new ArrayList<>();

        while (m.find()) {
            restult.add(m.group(1));
            // 只需要取得第一个 href 属性里面的值就可以,所以这里直接 break
            break;
        }
        return  restult;
    }

    /** 
    * @Description: 获取网络连接，这里获取的是 HTTPURLConnection，用来下载图片，传入的是图片的URL
    * @Param: [httpUrl] 
    * @return: java.net.HttpURLConnection 
    * @Author: 林凯
    * @Date: 2019/12/26 
    */ 
    public static HttpURLConnection getHttpURLConnection(String httpUrl) {
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(httpUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/octet-stream");
            connection.setDoOutput(true); connection.setDoInput(true);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.connect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return connection;
    }

    /**
     *      上面三个封装好的方法都会被下面这个方法所调用
     * */
    /** 
    * @Description: 封装好的方法，从Bing下载每日壁纸，返回的是下载到本地的图片的名字
    * @Param: [] 
    * @return: java.lang.String 
    * @Author: 林凯
    * @Date: 2019/12/27 
    */ 
    public static String downloadImageFromBing(String imageName) {
        System.out.println("开始从Bing.com爬取图片");
        long beginTime = System.currentTimeMillis();        // 获得下载开始的系统时间

        BufferedInputStream bis =null;
        BufferedOutputStream bos=null;

        /**
         *      1. 获得 Bing 国内版的网页源代码
         * */
        String resourceCode = getURLContent("http://bing.com", "UTF-8");        // 获取Bing官网的源码，字符集设置为 UTF-8

        /**
         *      2. 从网页源码中获得背景图片图片的 地址，存放在 List 列表中，在进行字符串拼接，得到完整图片URL
         * */
        List<String> result = getMatherSubstrs(resourceCode, "href=\\\"(.+?)\\\"");
        String imageAddress = result.get(0);        // 获得第一个元素即可，这里的地址不是完整的，还有进行进一步的拼接
        String trueImageURL = "https://cn.bing.com/" + imageAddress;       // 进行字符串拼接，得到完整的图片URL


        long time = System.currentTimeMillis();     // 获得当前时间作为图片名字
//        String imageName = "src//images//" + "Download" + String.valueOf(time) + ".jpg";
        String imagePath = "src//images//" + imageName;
        System.out.println("保存到本地的图片路径" + imagePath);

        try {
            /**
             *      调用封装好的方法 getHttpURLConnection()，获得图片的 Connection，然后中获得输入流
             * */
            bis = new BufferedInputStream(getHttpURLConnection(trueImageURL).getInputStream());
            bos = new BufferedOutputStream(new FileOutputStream(imagePath));
            
            int length = 0;
            byte[] buffer = new byte[1024];
            while (-1 != (length = bis.read(buffer))) {
                bos.write(buffer, 0, length);
            }
            bos.flush();        // 强制刷新一下
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if ((bis != null)) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        long endTime = System.currentTimeMillis();      // 获得下载完成之后的试卷
        double useTime = (double) ((endTime - beginTime) / 1000);
        System.out.println(endTime - beginTime);
        System.out.println("下载用时:" + useTime + "秒");
        double ImgeaSize = getHttpURLConnection(trueImageURL).getContentLength() / 1024;
        System.out.println("图片的大小为：" + ImgeaSize + "KB");

        return imageName;       // 返回图片的名称
    }

    public static void main(String[] args) {
        downloadImageFromBing("Download" + String.valueOf(System.currentTimeMillis()) + ".jpg");
    }
}
