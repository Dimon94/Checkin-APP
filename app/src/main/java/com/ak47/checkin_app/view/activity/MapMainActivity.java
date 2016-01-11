package com.ak47.checkin_app.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.ak47.checkin_app.R;

public class MapMainActivity extends Activity {
    private LinearLayout mapLayout;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    boolean isFirstLoc = true;
    private BitmapDescriptor bitmap;
    private InfoWindow mInfoWindow;
    private LocationClient mLocClient;
    private boolean isRequest = false;//是否手动触发请求定位
    public MyLocationListenner myListener = new MyLocationListenner();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.map_main);
        SDKInitializer.initialize(getApplicationContext()); //初始化地图sdk
        mapLayout = (LinearLayout) findViewById(R.id.map);
        initMap();
    }
    private void initMap() {
        BaiduMapOptions options = new BaiduMapOptions();
        options.compassEnabled(false); // 允许指南针
        options.zoomControlsEnabled(true); // 显示缩放按钮
        options.scaleControlEnabled(true); // 显示比例尺
        mMapView = new MapView(this, options); // 创建一个map对象
        mapLayout.addView(mMapView); // 把map添加到界面上
        mBaiduMap = mMapView.getMap(); // 获取BaiduMap对象
        mMapView.removeViewAt(1); // 去掉百度logo
        mBaiduMap.setMyLocationEnabled(true); //不显示我的位置，样覆盖物代替
        mBaiduMap.setMaxAndMinZoomLevel(18, 17); //地图的最大最小缩放比例3-18
        mLocClient = new LocationClient(this); //地图在tabhost中，请传入getApplicationContext()
        mLocClient.registerLocationListener(myListener); //绑定定位监听
        LocationClientOption option = new LocationClientOption(); //配置参数
        option.setOpenGps(true);
        option.setAddrType("all"); //设置使其可以获取具体的位置，把精度纬度换成具体地址
        option.setCoorType("bd09ll");
        option.setScanSpan(500);
        mLocClient.setLocOption(option);
        mLocClient.start();  //开始定位
    }

    public class MyLocationListenner implements BDLocationListener {  //定位

        @Override
        public void onReceiveLocation(final BDLocation location) {
//定位
            if (location == null || mMapView == null)
                return;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius()).direction(100)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                //定位成功
//                showLocation(location);
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);

                //点击按钮手动请求定位
                Button btn = (Button) findViewById(R.id.request);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isRequest = true;
                        mLocClient.start();
                        LatLng ll = new LatLng(location.getLatitude(),
                                location.getLongitude());
                        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                        mBaiduMap.animateMapStatus(u);
                        showLocation(location);

                        Intent intent = new Intent();
                        intent.putExtra("LOCDATA", location.getAddrStr());
                        setResult(11, intent);

                    }
                });

            }
        }

        public void onReceivePoi(BDLocation poiLocation) {

        }
    }

    public void showLocation(BDLocation location){
        LatLng pt ;

        View view = LayoutInflater.from(this).inflate(R.layout.map_item, null); //自定义气泡形状
        TextView textView = (TextView) view.findViewById(R.id.location_tips);
        textView.setText(location.getAddrStr());
        pt = new LatLng(location.getLatitude() + 0.0004, location.getLongitude() + 0.00005);

        mInfoWindow = new InfoWindow(view, pt,null);
        mBaiduMap.showInfoWindow(mInfoWindow); //显示气泡
        Toast.makeText(MapMainActivity.this, "定位成功,请返回",Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onBackPressed() {
        mLocClient.stop();
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

}