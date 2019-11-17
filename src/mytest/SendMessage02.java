package mytest;

/*
*       2019年10月11日22:18:15
*       测试用Java发送手机验证码短信
*       使用的是 阿里云 的API -------------------》(失败，余额不足)
* */

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

public class SendMessage02 {
    public static void main(String[] args) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "<LTAI4FiNcQseapHSKnALVvJ1>", "<fLXxzNYZ4VZRl2QJPm1aee9z610FsU>");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("QuerySendDetails");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumber", "15970819628");
        request.putQueryParameter("SendDate", "你好，这是第一次测试");
        request.putQueryParameter("PageSize", "10");
        request.putQueryParameter("CurrentPage", "1");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
