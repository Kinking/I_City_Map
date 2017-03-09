package com.example.huangzhiyuan.i_city_mapdemo.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;

import com.example.huangzhiyuan.i_city_mapdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 继承了Activity，实现Android6.0的运行时权限检测
 * 需要进行运行时权限检测的Activity可以继承这个类
 *
 * Created by huangzhiyuan on 2017/3/9.
 */

public class CheckPermissionsActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback{

    /**
     * 需要进行检测的权限数组
     */

    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    private static final int PERMISSION_REQUESTCODE = 0;

    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;

    @Override
    protected void onResume(){
        super.onResume();
        if(isNeedCheck){
            checkPermissions(needPermissions);
        }
    }

    /**

     * @since 2.5.0
     */
    private void checkPermissions(String... permissions){
        List<String> needRequestPermissionList = findDeniedPermissions(permissions);
        if(null != needRequestPermissionList
                && needRequestPermissionList.size()>0){
            ActivityCompat.requestPermissions(this,
                    needRequestPermissionList.toArray(
                            new String[needRequestPermissionList.size()]),
                            PERMISSION_REQUESTCODE
                    );
        }
    }

    /**
     * 获得权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     *
     */
    private List<String> findDeniedPermissions(String[] permissions){
        List<String> needRequestPermissionList = new ArrayList<String>();
        for(String perm:permissions){
            if(ContextCompat.checkSelfPermission(this,
                    perm)!= PackageManager.PERMISSION_GRANTED
                    ||ActivityCompat.shouldShowRequestPermissionRationale(
                    this,perm)){
                needRequestPermissionList.add(perm);
            }
        }
        return needRequestPermissionList;
    }

    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     *
     */

    private boolean vertifyPermissions(int[] grantResults){
        for(int result:grantResults){
            if(result!=PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     *
     */
    private void showMissingPermissionDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.notifyTitle);
        builder.setMessage(R.string.notifyMsg);


        //拒绝,退出应用
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });


        builder.setPositiveButton(R.string.setting,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });
        builder.setCancelable(false);
        builder.show();
    }


    /**
     *   启动应用的设置
     *
     *   @since 2.5.0
     *
     *
     */

    private void startAppSettings(){
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            this.finish();
            return true;
        }
        return  super.onKeyDown(keyCode,event);
    }

}
