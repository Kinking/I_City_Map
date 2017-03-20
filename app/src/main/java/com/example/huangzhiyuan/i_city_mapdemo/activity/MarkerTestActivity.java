package com.example.huangzhiyuan.i_city_mapdemo.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.example.huangzhiyuan.i_city_mapdemo.R;
import com.example.huangzhiyuan.i_city_mapdemo.bean.Moment;
import com.example.huangzhiyuan.i_city_mapdemo.service.MapOperation;
import com.example.huangzhiyuan.i_city_mapdemo.utils.json.WriteJson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MarkerTestActivity extends AppCompatActivity implements AMapLocationListener,LocationSource{

    private AMap aMap;  //定义地图对象
    private MapView mapView;  //一个用于显示地图的视图，从服务端获取数据，捕捉屏幕触控手势事件
    private Button button = null;
    private EditText et = null;

    double Latitude;
    double Longitude;


    String jsonMomentString=null;


    /**
     * 定位当前位置
     */
    private LocationSource.OnLocationChangedListener mListener = null;
    public AMapLocationClient mapLocationClient = null;
    public AMapLocationClientOption mapLocationClientOption = null;
    private AMapLocation aMapLocation=null;
    /*****/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_test);
        button= (Button) findViewById(R.id.bt_test2);
        et = (EditText) findViewById(R.id.et_state);
        //获取地图控件引用
        mapView = (MapView) findViewById(R.id.map_marker);
        //创建地图
        mapView.onCreate(savedInstanceState);
        init();
        aMap.setLocationSource(this);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示
        //设置为true表示显示定位层并可触发定位,false表示隐藏定位层并不可触发定位,默认是false
        aMap.setMyLocationEnabled(true);
        //设置定位的类型为定位模式,可以有定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

        //获取朋友圈内容
        String momentContent = et.getText().toString().trim();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //设置一个表示经纬度地理位置的对象
                LatLng latLngSH = new LatLng(Latitude,Longitude);
                String momentContent = et.getText().toString().trim();


//                final Marker marker = aMap.addMarker(new MarkerOptions().position(latLngSH).title("上海").snippet("DefaultMarker"));
                final Marker marker = aMap.addMarker(new MarkerOptions().position(latLngSH).snippet(momentContent));
                marker.showInfoWindow();

                try{

                    //获取当前时间
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String date = simpleDateFormat.format(new java.util.Date());
                    /**
                     * 接下来的逻辑要将moment转换为json字符串传送到服务器
                     */
                    String momentContentInner = et.getText().toString().trim();
                    Moment moment = new Moment(1,1,"jerry",momentContentInner,date,Latitude,Longitude);
                    //构造一个moment对象
                    List<Moment> list = new ArrayList<Moment>();
                    list.add(moment);
                    WriteJson writeJson = new WriteJson();
                    jsonMomentString = writeJson.getJsonData(list);
                    System.out.println(jsonMomentString+"------------------------------");
                    /**
                     * 下面要写的是async-http框架负责发送请求的内容
                     */
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MapOperation operation = new MapOperation();
                            String result = operation.uploadContent("MomentController",jsonMomentString);
                            Message msg = new Message();
                            msg.obj = result;
                            handler.sendMessage(msg);
                        }
                    }).start();
                }catch (Exception e){
                    Toast.makeText(MarkerTestActivity.this, "服务器未打开", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }




            }
        });


        /**
         * addMarker 在地图上添加一个Marker
         * 相关参数：
         *   1.MarkerOptions  设置marker覆盖物的锚点图标
         *   2.position       设置放锚点的坐标
         *   3.title          设置放锚点的标题文字
         *   4.snippet        电表记得内容
         *   5.draggable      点是否可拖拽
         *   6.visible        点标记是否可见
         *   7.anchor         点标记的锚点
         *   8.alpha          点的透明度
         */


    }

    /******************* Handler **************************/
    android.os.Handler handler=new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
//            dialog.dismiss();

            try {

                String msgobj=msg.obj.toString();
                super.handleMessage(msg);

            }catch (Exception e){
                Toast.makeText(MarkerTestActivity.this, "服务器未打开", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    };
    /*********************************************/


    //地图初始化函数
    private void init(){
        if(aMap == null){
            aMap = mapView.getMap();
        }
    }

    /***地图生命周期***/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行map的destroy,实现生命周期的管理
        mapView.onDestroy();
        mapLocationClient.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop(){
        super.onStop();
        mapLocationClient.stopLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if(aMapLocation!=null){
            if(aMapLocation.getErrorCode()==0){

                mListener.onLocationChanged(aMapLocation);//显示系统小蓝点

                StringBuilder stringBuilder= new StringBuilder();

                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果
                this.Latitude=aMapLocation.getLatitude();//获取纬度
                this.Longitude=aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);
                //成功回调消息，设置相关信息
                int type = aMapLocation.getLocationType();
                String address = aMapLocation.getAddress();
                stringBuilder.append(type+address);
                Toast.makeText(this,stringBuilder.toString(),Toast.LENGTH_SHORT).show();
            }else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("Error Info",aMapLocation.getErrorCode()+"---" + aMapLocation.getErrorInfo());
            }
        }

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if(mapLocationClient == null){
            //初始化AMapLocationClient，并绑定监听
            mapLocationClient = new AMapLocationClient(getApplicationContext());

            //初始化定位参数
            mapLocationClientOption = new AMapLocationClientOption();
            //设置定位精度
            mapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //是否返回地址
            mapLocationClientOption.setNeedAddress(true);
            //是否只定位一次
            mapLocationClientOption.setOnceLocation(false);
            //设置是否强制刷新WIFI，默认为强制刷新
            mapLocationClientOption.setWifiActiveScan(true);
            //是否允许模拟位置
            mapLocationClientOption.setMockEnable(false);
            //定位时间间隔
            mapLocationClientOption.setInterval(2000);
            //给定位客户端对象设置定位参数
            mapLocationClient.setLocationOption(mapLocationClientOption);
            //绑定监听
            mapLocationClient.setLocationListener(this);
            //开启监听
            mapLocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if(mapLocationClient!=null){
            mapLocationClient.stopLocation();
            mapLocationClient.onDestroy();
        }
        mapLocationClient = null;
    }
    /***地图生命周期***/
}
