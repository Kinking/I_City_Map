package com.example.huangzhiyuan.i_city_mapdemo.overlay;

import android.graphics.Bitmap;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.PoiItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangzhiyuan on 2017/3/7.
 * Poi图层,在高德地图API里,如果要显示Poi,可以用此来创建Poi涂层。如不满足需求,也可以自己创建自定义的Poi图层。
 */
public class PoiOverlay {

    private List<PoiItem> mPois;   //序列化
    private AMap mAMap;  //定义一个地图对象
    private ArrayList<Marker> mPoiMarks = new ArrayList<Marker>();

    /** Marker：地图上的一个点绘制图标
     *    属性:
     * 锚点:  图标摆在地图上的基准点
     * 位置:  通过经纬度值标记在地图上
     * 标题:  点击marker显示在信息窗口上的文字，随时可以改
     * 片段:  除了标题外的文字随时可以改
     * 图标:  显示的图标
     */


    /**
     * 通过此构造函数创建Poi图层对象
     * @param amap 地图对象
     * @param pois 要在地图上添加的poi。列表中的poi对象详见搜索服务模块的基础核心包(com.amap.api.services.core)中的类...
     */
    public PoiOverlay(AMap amap, List<PoiItem> pois){
        mAMap = amap;
        mPois = pois;
    }

    /**
     * 添加Marker到地图中
     */
    public void addToMap(){
        try {
            for(int i = 0;i<mPois.size();i++){
                Marker marker = mAMap.addMarker(getMarkerOptions(i));
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    /**
     * 去掉PoiOverLay上所有的Marker
     */
    public void removeFromMap(){
        for(Marker marker:mPoiMarks){
            marker.remove();
        }
    }

    /**
     * 移动镜头到当前的视角
     */
    public void zoomToSpan(){
        try {
            if(mPois != null && mPois.size()>0){
                if(mAMap == null)
                    return;
                if(mPois.size()==1){
                    mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mPois.get(0).getLatLonPoint().getLatitude(),
                            mPois.get(0).getLatLonPoint().getLongitude()),18f));
                }else {
                    LatLngBounds bounds = getLatLngBounds();
                    mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,30));
                }
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    private LatLngBounds getLatLngBounds(){
        LatLngBounds.Builder b = LatLngBounds.builder();
        for(int i=0;i<mPois.size();i++){
            b.include(new LatLng(mPois.get(i).getLatLonPoint().getLatitude(),
                    mPois.get(i).getLatLonPoint().getLongitude()));
        }
        return b.build();
    }




    private MarkerOptions getMarkerOptions(int index){
        return new MarkerOptions()
                .position(
                        new LatLng(mPois.get(index).getLatLonPoint()
                                .getLatitude(),mPois.get(index)
                                .getLatLonPoint().getLongitude()))
                .title(getTitle(index)).snippet(getSnippet(index))
                .icon(getBitmapDescriptor(index));
    }

    /**
     * 给第几个Marker设置图标,并返回更换图标的图片。如不使用图片,需要重写此方法
     * @param index 第几个Marker
     * @return 更换Marker图片
     */

    protected BitmapDescriptor getBitmapDescriptor(int index){
        return null;
    }

    /**
     * 返回第index的Marker标题
     * @param index 第几个Marker
     * @return Marker的标题
     */

    protected String getTitle(int index){
        return mPois.get(index).getTitle();
    }

    /**
     * 返回第index的Marker的详情
     * @param index 第几个Marker
     * @return Marker的详情
     */
    protected String getSnippet(int index){
        return mPois.get(index).getSnippet();
    }

    /**
     * 从Marker中得到poi在list的位置
     * @param marker 一个标记的对象
     * @return 返回该marker对应的poi在list的位置
     */
    public int getPoiIndex(Marker marker){
        for (int i = 0;i<mPoiMarks.size();i++){
            if(mPoiMarks.get(i).equals(marker)){
                return i;
            }
        }
        return -1;
    }

    /**
     * 返回第index的poi的信息
     * @param index 第几个poi
     * @return poi的信息。poi对象详见搜索服务模块的基础核心包(com.amap.api.services.core)中的类....
     */
    public PoiItem getPoiItem(int index){
        if(index<0||index>=mPois.size()){
            return null;
        }
        return mPois.get(index);
    }




}
