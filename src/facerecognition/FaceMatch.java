package facerecognition;

//import com.baidu.ai.aip.utils.HttpUtil;
//import com.baidu.ai.aip.utils.GsonUtils;

/*
*       人脸识别类
*       功能：人脸识别，拍照完毕后立即进行人脸识别，返回一个result字符串，里面包含着 score 信息
*               score 越高，说明越相似
*       调用关系： 被 WebcamCapture 类调用，拍照完毕后立即进行人脸识别，
*
*  2019年11月21日19:39:15
*       实现人脸对比的思路：
*       （1）在 AuthService 类中获取 access_token （注意：access_token的有效期为30天，切记需要每30天进行定期更换，或者每次请求都拉取新token；）
*           ---》 需要导入 json.jar  包才能跑，因为有这样一条语句： import org.json.JSONObject;
*                   注： json.jar 包，位于 D:\BaiduNetdiskDownload\我的资源  文件夹下 （从网上下载的）
*       (2) 在 FaceMath 中，按照提供下载百度提供的4个工具类，为了使工具类不报错，还得导入   gson-2.2.0.jar   包
*           因为GsonUtils中有 ： import com.google.gson.Gson;
                                import com.google.gson.GsonBuilder;
                                import com.google.gson.JsonParseException;
*                               3条语句
*
*       （3）     通过 Capture 类，实现java调用笔记本摄像头拍照。
*               利用的是： 利用 Webcam Capture API
*                        Github 地址 ：https://github.com/sarxos/webcam-capture
*               从这个网址里面获取对应 jar 包，或者用 maven 部署（不太会）
*
* */

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 人脸对比
 */
public class FaceMatch {

    /**
     * 重要提示代码中所需工具类
     * FileUtil,Base64Util,HttpUtil,GsonUtils请从
     * https://ai.baidu.com/file/658A35ABAB2D404FBF903F64D47C1F72
     * https://ai.baidu.com/file/C8D81F3301E24D2892968F09AE1AD6E2
     * https://ai.baidu.com/file/544D677F5D4E4F17B4122FBD60DB82B3
     * https://ai.baidu.com/file/470B3ACCA3FE43788B5A963BF0B625F3
     * 下载
     */
    public static String faceMatch(String filePath1, String filePath2) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/match";
        try {

//            String filePath1 = "D:\\IDEA文件\\人脸识别测试\\FaceTest01\\src\\images\\test1.png";
 //            String filePath2 = "D:\\IDEA文件\\人脸识别测试\\FaceTest01\\src\\images\\test2.png";

            System.out.println("file1"  +filePath1);
            System.out.println("file2"  +filePath2);
            byte[] imgData1 = FileUtil.readFileByBytes(filePath1);
            String imgStr1 = Base64Util.encode(imgData1);
            byte[] imgData2 = FileUtil.readFileByBytes(filePath2);
            String imgStr2 = Base64Util.encode(imgData2);

            ArrayList<HashMap<String, String>> map = new ArrayList<>();
            HashMap<String, String> map1 = new HashMap<>();
            HashMap<String, String> map2 = new HashMap<>();
            map1.put("image", imgStr1);
            map1.put("image_type", "BASE64");
            map1.put("face_type", "LIVE");
            map1.put("quality_control", "LOW");
            map1.put("liveness_control", "NONE");
            map2.put("image", imgStr2);
            map2.put("image_type", "BASE64");
            map2.put("face_type", "LIVE");
            map2.put("quality_control", "LOW");
            map2.put("liveness_control", "NONE");
            map.add(map1);
            map.add(map2);

            String param = GsonUtils.toJson(map);
            System.out.println(param);

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = AuthService.getAuth();

            String result = HttpUtil.post(url, accessToken, "application/json", param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    /**
//     *
//     * @param name 图片名称
//     * @return
//     */
//    public static String match(String name) {
//        // 请求url
//        String url = "https://aip.baidubce.com/rest/2.0/face/v3/match";
//        try {
//            // 本地文件路径
////            String filePath =  "C:\\temp\\img\\cover.png";
//            String filePath = "D:\\IDEA文件\\人脸识别测试\\FaceTest01\\a1.jpg";
//            byte[] imgData = FileUtil.readFileByBytes(filePath);
//            String imgStr = Base64Util.encode(imgData);
////            System.out.println(imgStr);
//            String imgParam = URLEncoder.encode(imgStr, "UTF-8");
////            System.out.println(imgParam);
//
//            //摄像头拍照图片路径
//            // String filePath2 =  "C:\\temp\\cover.png";
////            String filePath2 =  "C:\\temp\\"+name+".jpg";
//            String filePath2 = "D:\\IDEA文件\\人脸识别测试\\FaceTest01\\a2.jpg";
//            byte[] imgData2 = FileUtil.readFileByBytes(filePath2);
//            String imgStr2 = Base64Util.encode(imgData2);
//            String imgParam2 = URLEncoder.encode(imgStr2, "UTF-8");
//
//            String param = "images=" + imgParam + "," + imgParam2;
//            System.out.println(param);
//            String param2 = "[\n" +
//                    "    {\n" +
//                    "        \"image\": " + "https://cdn2.ettoday.net/images/2278/2278352.jpg" + ",\n" +
//                    "        \"image_type\": \"URL\",\n" +
//                    "        \"face_type\": \"LIVE\",\n" +
//                    "        \"quality_control\": \"LOW\",\n" +
//                    "        \"liveness_control\": \"HIGH\"\n" +
//                    "    },\n" +
//                    "    {\n" +
//                    "        \"image\": " + "https://cdn2.ettoday.net/images/2278/2278352.jpg" + ",\n" +
//                    "        \"image_type\": \"URL\",\n" +
//                    "        \"face_type\": \"IDCARD\",\n" +
//                    "        \"quality_control\": \"LOW\",\n" +
//                    "        \"liveness_control\": \"HIGH\"\n" +
//                    "    }\n" +
//                    "]\n";
//
////            String param2 = "[\n" +
////                    "    {\n" +
////                    "        \"image\": " + imgStr + ",\n" +
////                    "        \"image_type\": \"BASE64\",\n" +
////                    "        \"face_type\": \"LIVE\",\n" +
////                    "        \"quality_control\": \"LOW\",\n" +
////                    "        \"liveness_control\": \"HIGH\"\n" +
////                    "    },\n" +
////                    "    {\n" +
////                    "        \"image\": " + imgStr2 + ",\n" +
////                    "        \"image_type\": \"BASE64\",\n" +
////                    "        \"face_type\": \"IDCARD\",\n" +
////                    "        \"quality_control\": \"LOW\",\n" +
////                    "        \"liveness_control\": \"HIGH\"\n" +
////                    "    }\n" +
////                    "]\n";
////            System.out.println("\n" +  "000000000000" + "\n");
////            System.out.println("\n" +  "000000000000" + "\n");
////            System.out.println("\n" +  "000000000000" + "\n");
////            System.out.println(param2);
//
//            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
//            //自己生成的access_token
//            // String accessToken = AuthService.getAuth();
//            //String accessToken ="";
//            String result = HttpUtil.post(url, AuthService.getAuth(), "application/json", param2);
//            System.out.println(result);
//            return result;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public static void main(String[] args) {
//        FaceMatch.faceMatch();
//        System.out.println(FaceMatch.match("a"));
    }
}