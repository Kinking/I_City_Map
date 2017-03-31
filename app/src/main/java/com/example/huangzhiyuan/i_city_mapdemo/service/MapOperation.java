package com.example.huangzhiyuan.i_city_mapdemo.service;

import com.example.huangzhiyuan.i_city_mapdemo.utils.ConnNet;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
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

import cz.msebera.android.httpclient.Header;

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
    public String getJsonFromServer(String url){
        String result = null;
        ConnNet connNet=new ConnNet();
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("request", "请求获得所有marker数据"));
        try {
            //此句必加上不然传到客户端的中文是乱码
            HttpEntity entity=new UrlEncodedFormEntity(params, HTTP.UTF_8);
            HttpPost httpPost = connNet.getHttpPost(url);
            System.out.println(httpPost.toString());
            httpPost.setEntity(entity);
            HttpClient client = new DefaultHttpClient();
            HttpResponse httpResponse = client.execute(httpPost);
            if(httpResponse.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
                //获取返回http里的正文内容
                /**
                 * 以下获得服务端的数据
                 */
                result = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
                System.out.println("result:"+result);
            } else {
                result = "传输失败";
            }
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String InitMarker(String urlPath,String jsonString){
        //定义请求字符串
        String request = null;
        //创建异步请求
        AsyncHttpClient client = new AsyncHttpClient();
        //输入要请求的url
        urlPath = "http://172.20.10.8:8080/ICity/GetMomentInfoController";
        //请求的参数对象
        RequestParams params = new RequestParams();
        //将参数加入到参数对象中去
        params.put("jsonMomentRequest",request);
        //进行post请求
        client.post(urlPath, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });


        return null;
    }

}
