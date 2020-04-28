package com.whp.usdtfb.Util.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 利用HttpClient进行get请求的工具类
 *
 * @author Devin <xxx>
 * @ClassName: HttpClientUtil
 * @Description: TODO
 * @date 2017年2月7日 下午1:43:38
 */
public class HttpClientUtil {
    @SuppressWarnings("resource")
    public static String doGet(String url) {
        HttpClient httpClient = null;
        HttpGet httpGet = null;
        String result = null;
        try {
            httpClient = HttpClients.createDefault();
            httpGet = new HttpGet(url + "?key=cd24d7edf2f7239b6bcdf72624b40729");
            RequestConfig.Builder configBuilder = RequestConfig.custom();
            // 设置连接超时
            configBuilder.setConnectTimeout(15000);
            // 设置读取超时
            configBuilder.setSocketTimeout(15000);
            // 设置从连接池获取连接实例的超时
            configBuilder.setConnectionRequestTimeout(15000);
            // 在提交请求之前 测试连接是否可用
            configBuilder.setStaleConnectionCheckEnabled(true);
            RequestConfig requestConfig = configBuilder.build();
            httpGet.setConfig(requestConfig);
            httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36");
            httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded");

            HttpResponse response = httpClient.execute(httpGet);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, "UTF-8");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}