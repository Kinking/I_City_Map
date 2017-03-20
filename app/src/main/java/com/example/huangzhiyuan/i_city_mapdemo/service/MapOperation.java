package com.example.huangzhiyuan.i_city_mapdemo.service;

import com.example.huangzhiyuan.i_city_mapdemo.utils.ConnNet;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangzhiyuan on 2017/3/20.
 */
public class MapOperation {
    //上传Mark朋友圈内容
    public String uploadContent(String urlPath,String jsonString){
        String result = null;
        List<NameValuePair> list=new ArrayList<NameValuePair>();
        NameValuePair nvp=new BasicNameValuePair("jsonstring", jsonString);
        list.add(nvp);
        ConnNet connNet = new ConnNet();
        HttpPost httpPost = connNet.getHttpPost(urlPath);
        try {
            //此句必加上不然传到客户端的中文是乱码
            HttpEntity entity = new UrlEncodedFormEntity(list, HTTP.UTF_8);
            httpPost.setEntity(entity);
            HttpClient client = new DefaultHttpClient();
            HttpResponse httpResponse = client.execute(httpPost);
            if(httpResponse.getStatusLine().getStatusCode()==200){
                result = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
                System.out.println("result:"+result);
            } else {
                result = "传输失败";
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return  result;
    }


    /***获取服务器数据***/
    public void getJsonFromServer(String url,String name){
        
    }

}
