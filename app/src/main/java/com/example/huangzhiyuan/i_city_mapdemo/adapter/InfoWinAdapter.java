package com.example.huangzhiyuan.i_city_mapdemo.adapter;


import android.content.Context;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;

/**
 * Created by huangzhiyuan on 2017/3/16.
 */
public class InfoWinAdapter implements AMap.InfoWindowAdapter, View.OnClickListener{



    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onClick(View view) {

    }
}
