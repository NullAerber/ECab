package cn.edu.lzu.oss.ecab.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;


import java.util.ArrayList;
import java.util.List;

import cn.edu.lzu.oss.ecab.activities.MainActivity;
import cn.edu.lzu.oss.ecab.R;
import cn.edu.lzu.oss.ecab.interfaces.FragmentInterface;

import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static cn.edu.lzu.oss.ecab.util.Const.MapFragmentPermission.BAIDU_READ_PHONE_STATE;

public class MapFragment extends Fragment implements FragmentInterface {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private SlidingUpPanelLayout panelLayout;
    private List<LatLng> points;
    private Context context;
    public LocationClient mLocationClient = null;
    private MyLocationData locData;
    public BDAbstractLocationListener myListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            locData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    .direction(100).latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();

            mBaiduMap.setMyLocationData(locData);
            MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(1.0f);// 设置地图放大比例
            mBaiduMap.setMapStatus(msu);

        }
    };
    public MapFragment() {
    }

    public static MapFragment newInstance(){
        return new MapFragment();
    }

    public SlidingUpPanelLayout getPanel(){
        return panelLayout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        context = getActivity();
        initMap(view);
        initPanel(view);

        return view;
    }

    private void initPanel(View view) {
        panelLayout = view.findViewById(R.id.sliding_layout);

    }


    private void initMap(View view) {
        mMapView = view.findViewById(R.id.baidu_maps);
        mBaiduMap = mMapView.getMap();
        points = new ArrayList<>();
        initPoints();
        for (LatLng x:points){
            setMarker(x);
        }
        mBaiduMap.setMyLocationEnabled(true);
        initPosition();
    }

    private void initPosition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
                MapFragment.this.requestPermissions(new String[]{ Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE },BAIDU_READ_PHONE_STATE );

        }else {
            locate();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == BAIDU_READ_PHONE_STATE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                locate();
            }
        }else {
            Toast.makeText(getActivity(), "获取权限失败",Toast.LENGTH_SHORT).show();
        }
    }

    private void locate() {
        mLocationClient = new LocationClient(getActivity().getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
        //开启定位

        mLocationClient.start();
    }

    private void setMarker(LatLng latLng) {
        View v = View.inflate(getContext(),R.layout.fragment_map_marker , null);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(v);
        OverlayOptions option = new MarkerOptions().position(latLng).icon(bitmap);
        mBaiduMap.addOverlay(option);
    }

    private void initPoints() {
//        points.add(new LatLng(39.963175, 116.400244));
        points.add(new LatLng(35.949121, 104.160009));
        points.add(new LatLng(35.94947, 104.16444));
    }

    @Override
    public boolean onFragmentBackPress() {
        if (panelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED){
            panelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            return true;
        }else
            return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity){
            ((MainActivity)context).setBackInterfaceListener(this);
            ((MainActivity)context).setActive(true);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Context context = getContext();
        if (context instanceof MainActivity){
            ((MainActivity)context).setBackInterfaceListener(null);
            ((MainActivity)context).setActive(false);
        }
        if (mLocationClient != null){
            mLocationClient.stop();
        }
    }
}
