package com.example.huangzhiyuan.i_city_mapdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.offlinemap.OfflineMapCity;
import com.amap.api.maps.offlinemap.OfflineMapManager;
import com.amap.api.maps.offlinemap.OfflineMapStatus;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.huangzhiyuan.i_city_mapdemo.R;
import com.example.huangzhiyuan.i_city_mapdemo.overlay.PoiOverlay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



/**
 * Created by huangzhiyuan on 2017/3/7.
 */
public class MainActivity extends AppCompatActivity implements PoiSearch.OnPoiSearchListener, AMapLocationListener,LocationSource {

    private AMap aMap;
    private MapView mapView;
    private LatLonPoint centerpoint = new LatLonPoint(39.983178,116.464348);
    private ViewPoiOverlay poiOverlay;


    private LocationSource.OnLocationChangedListener mListener = null;
    public AMapLocationClient mapLocationClient = null;
    public AMapLocationClientOption mapLocationClientOption = null;

////    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        /**
         *设置离线地图存储目录，在下载离线地图或初始化地图设置
         * 使用过程中可自行设置，若自行设置了离线地图存储的路径，
         * 则需要在离线地图下载和使用地图页面进行路径设置
         */

        //Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
        //MapsInitializer.sdcardDir = OfflineMapUtils.getSdCacheDir(this);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);//此方法必须重写
        init();
        aMap.setLocationSource(this);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示
        //设置为true表示显示定位层并可触发定位,false表示隐藏定位层并不可触发定位,默认是false
        aMap.setMyLocationEnabled(true);
        //设置定位的类型为定位模式,可以有定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        doPOISearch();

//        button = (Button) findViewById(R.id.bt);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent();
//                i.setClass(MainActivity.this,Location_Activity.class);
//                startActivity(i);
//            }
//        });

//        mLocationClient = new AMapLocationClient(this);
//        //初始化定位参数
//        mLocationOption = new AMapLocationClientOption();
//        //设置定位监听
//        mLocationClient.setLocationListener(this);
//        //设置定位模式为高精度模式,Battery_Saving为低功耗模式,Device_Sensors是仅设备模式
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        //设置定位间隔,单位毫秒,默认为2000ms
//        mLocationOption.setInterval(2000);
//        //设置定位参数
//        mLocationClient.setLocationOption(mLocationOption);
//        //此方法为每隔固定时间发起一次定位请求,为了减少电量消耗或网络流量消耗
//        //注意设置合适的定位时间的间隔(最小间隔支持为2000ms),并且在合适的时间调用stopLocation()方法来取消定位请求
//        //在定位结束后,在合适的生命周期调用onDestroy()方法
//        //在单词定位情况下,定位无论成功与否,都无需调用stopLocation()方法移除请求,定位SDK内部会移除
//        //启动定位
//        mLocationClient.startLocation();

    }

    private void doPOISearch(){
        PoiSearch.Query query = new PoiSearch.Query("公园","110101","北京");
        query.setPageSize(10); //设每页最多返回多少条poiitem
        query.setPageNum(0);
        query.requireSubPois(true);
        PoiSearch poiSearch = new PoiSearch(this,query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.setBound(new PoiSearch.SearchBound(centerpoint,5000,true));
        //设置搜索区域以1p点为圆心，其周围5000米范围
        poiSearch.searchPOIAsyn();//异步搜索
    }

    //地图初始化函数
    private void init(){
        if(aMap == null){
            aMap = mapView.getMap();
        }
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(converTolagLng(centerpoint),13));
    }

    @Override
    protected void onResume(){
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        //在activity执行onDestroy时执行map的destroy,实现生命周期的管理
        mapView.onDestroy();
        mapLocationClient.onDestroy();
    }

    @Override
    protected void onStop(){
        super.onStop();
        mapLocationClient.stopLocation();
    }




    //搜索返回结果回调
    @Override
    public void onPoiSearched(PoiResult poiResult,int errorCode){
        if(errorCode == 1000){
            if(poiResult != null && poiResult.getQuery() != null){
                List<PoiItem>poiItems = poiResult.getPois();
                if(poiItems!=null&&poiItems.size()>0){
                    aMap.clear();//清理之前的图标
                    poiOverlay = new ViewPoiOverlay(aMap,poiItems);
                    poiOverlay.removeFromMap();
                    poiOverlay.addToMap();
                    poiOverlay.zoomToSpan();
                }else {
                    Toast.makeText(MainActivity.this,"无搜索结果",Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(MainActivity.this,"无搜索结果",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem,int i){

    }

    /**
     * 把LatLonPoint对象转化为LatLon对象
     */
    public static LatLng converTolagLng(LatLonPoint latLonPoint){
        if(latLonPoint==null){
            return null;
        }
        return new LatLng(latLonPoint.getLatitude(),latLonPoint.getLatitude());
    }


    public class ViewPoiOverlay extends PoiOverlay{

        public ViewPoiOverlay(AMap aMap, List<PoiItem> list) {
            super(aMap, list);
        }

        @Override
        protected BitmapDescriptor getBitmapDescriptor(int index){
            View view = null;
            view = View.inflate(MainActivity.this,R.layout.custom_view,null);
            TextView textView = (TextView) view.findViewById(R.id.ttitle);
            textView.setText(getTitle(index));

            return BitmapDescriptorFactory.fromView(view);
        }
    }



    //设置定位监听的函数
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if(aMapLocation!=null){
            if(aMapLocation.getErrorCode()==0){

                mListener.onLocationChanged(aMapLocation);//显示系统小蓝点

                StringBuilder stringBuilder= new StringBuilder();

                //定位成功回调信息，设置相关消息
//                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果
//                aMapLocation.getLatitude();//获取纬度
//                aMapLocation.getLongitude();//获取经度
//                aMapLocation.getAccuracy();//获取精度信息
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date date = new Date(aMapLocation.getTime());
//                df.format(date);
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

    //激活定位
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener){
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

    //停止定位
    @Override
    public void deactivate(){
        mListener = null;
        if(mapLocationClient!=null){
            mapLocationClient.stopLocation();
            mapLocationClient.onDestroy();
        }
        mapLocationClient = null;
    }

}
