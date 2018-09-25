import com.aiwsport.core.entity.User;
import com.aiwsport.web.utlis.HttpRequestor;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;

/**
 * Created by yangjian9 on 2018/9/18.
 */
public class Mytest {
    public static void main(String args[]) throws Exception{

//        String sign = DigestUtils.md5Hex("yangjianjijijijijiji");
        System.out.println(System.currentTimeMillis() / 100000);
        Thread.sleep(60*1000);
        System.out.println(System.currentTimeMillis() / 100000);


//        System.out.println(System.currentTimeMillis() / 5000);


//        HttpClient client = new HttpClient();
//        // 设置代理服务器地址和端口
//        //client.getHostConfiguration().setProxy("proxy_host_addr",proxy_port);
//        // 使用 GET 方法 ，如果服务器需要通过 HTTPS 连接，那只需要将下面 URL 中的 http 换成 https
//        HttpMethod method=new GetMethod("http://10.75.28.152:8888/2/place/pois/add_checkin.json?access_token=2.00_HfMtCVMeOFE946aabf71a0PDGhY&poiid=B2094654D069ABF84698&status=%e4%b8%ba%e4%ba%ba%e6%b0%91%e6%9c%8d%e5%8a%a1");
//        method.setRequestHeader("X-Proto","SSL");
////        method.setRequestHeader("Accept-Encoding","gzip");
//
//
//        //使用POST方法
//        //HttpMethod method = new PostMethod("http://java.sun.com");
//
//
//        client.executeMethod(method);
//
//        //打印服务器返回的状态
//        System.out.println(method.getStatusLine());
//        //打印返回的信息
//        System.out.println(method.getResponseBodyAsString());
////        for (Header header : method.getResponseHeaders()) {
////            System.out.println(header.getName() + "======" + header.getValue());
////        }
//        for (Header header : method.getRequestHeaders()) {
//            System.out.println(header.getName() + "======" + header.getValue());
//        }
//
//        //释放连接
//        method.releaseConnection();



//        HttpGet httpget = new HttpGet(url);



    }







}
