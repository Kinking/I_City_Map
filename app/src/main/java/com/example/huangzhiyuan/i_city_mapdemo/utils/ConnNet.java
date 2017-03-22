package com.example.huangzhiyuan.i_city_mapdemo.utils;

import org.apache.http.client.methods.HttpPost;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by huangzhiyuan on 2017/3/20.
 */
public class ConnNet {

      private static final String SERVICE_URL="http://172.29.0.220:8080/ICity/";  //公司wifi
//    private static final String SERVICE_URL="http://172.20.10.7:8080/ICity/";  //公司wifi
//    private static final String SERVICE_URL="http://1.168.177.239:8080/ICity/";  //公司Wi-Fi
//    private static final String SERVICE_URL="http://192.168.1.104:8080/ICity/";  //家力Wi-Fi
//    private static final String SERVICE_URL="http://119.29.69.123:8080/ICity/";  //云服务器地址119.29.69.123:8080

    public HttpURLConnection getConn(String urlPath){
        String final_URL = SERVICE_URL+urlPath;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(final_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true); //允许输入流
            connection.setDoOutput(true);//允许输出流
            connection.setUseCaches(false);//不允许使用缓存方式
            connection.setRequestMethod("POST");//设置请求方式
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return connection;
    }

    public HttpPost getHttpPost(String urlPath){
        HttpPost httpPost=new HttpPost(SERVICE_URL+urlPath);
        System.out.println(SERVICE_URL+urlPath);
        return httpPost;
    }


}
