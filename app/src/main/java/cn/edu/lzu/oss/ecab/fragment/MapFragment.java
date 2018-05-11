package cn.edu.lzu.oss.ecab.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;


import java.util.ArrayList;
import java.util.List;

import cn.edu.lzu.oss.ecab.activities.MainActivity;
import cn.edu.lzu.oss.ecab.R;
import cn.edu.lzu.oss.ecab.activities.PayActivity;
import cn.edu.lzu.oss.ecab.activities.SaveActivity;
import cn.edu.lzu.oss.ecab.interfaces.BackPressInterface;
import cn.edu.lzu.oss.ecab.interfaces.UserClickInterface;
import cn.edu.lzu.oss.ecab.util.WindowUtil;

import static cn.edu.lzu.oss.ecab.util.Const.MapFragmentPermission.BAIDU_READ_PHONE_STATE;

public class MapFragment extends Fragment implements BackPressInterface {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private SlidingUpPanelLayout panelLayout;
    private ImageView userImage;
    private ImageView locationImage;
    private CardView searchCard;

    private List<LatLng> points;
    private Context context;
    private MyLocationData locData;
    private LocationClient mLocationClient;
    private MyBDAbstractLocationListener mLocationListener;
    private LocationMode mCurrentMode = LocationMode.FOLLOWING;
    private boolean isFirstLocation = true;
    private UserClickInterface clickInterface;

    private boolean clickMarker = false;


    public MapFragment() {
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    public SlidingUpPanelLayout getPanel() {
        return panelLayout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        context = getActivity();
        initMap(view);
        initPanel(view);
        initView(view);
        initPosition();
        setImageListener(view);
        return view;
    }

    private void setImageListener(View view) {
        ImageView add_1 = view.findViewById(R.id.image_add_1);
        ImageView add_2 = view.findViewById(R.id.image_add_2);
        ImageView add_3 = view.findViewById(R.id.image_add_3);
        final TextView length = view.findViewById(R.id.textview_length_number);
        final TextView width = view.findViewById(R.id.textview_width_number);
        TextView weight = view.findViewById(R.id.textview_weight_number);
        View.OnClickListener imageListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.image_add_1:
                        length.setText("2");
                        break;
                    case R.id.image_add_2:
                        width.setText("2");
                        break;
                    case R.id.image_add_3:
                        break;
                    default:
                        break;
                }
            }
        };
        add_1.setOnClickListener(imageListener);
        add_2.setOnClickListener(imageListener);
        add_3.setOnClickListener(imageListener);
        ImageView imageView = view.findViewById(R.id.imageView8);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SaveActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setClickInterface(UserClickInterface clickInterface) {
        this.clickInterface = clickInterface;
    }

    private void initMap(View view) {
        mMapView = view.findViewById(R.id.baidu_maps);
        mBaiduMap = mMapView.getMap();
        MapStatus mMapStatus = new MapStatus.Builder().zoom(18).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        points = new ArrayList<>();
        initPoints();
        for (LatLng x : points) {
            setMarker(x);
        }
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            public void onMapClick(LatLng point) {
                if (panelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    panelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                } else if (clickMarker) {
                    mBaiduMap.hideInfoWindow();
                    clickMarker = false;
                }
            }

            public boolean onMapPoiClick(MapPoi poi) {
                if (clickMarker) {
                    mBaiduMap.hideInfoWindow();
                    clickMarker = false;
                    return true;
                } else
                    return false;
            }
        });
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                clickMarker = true;
                MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(marker.getPosition());
                mBaiduMap.animateMapStatus(status);//动画的方式到中间
                Log.i("id:", marker.getId());
                panelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                View windowView = initWindowView();
                int elses = (int) (WindowUtil.getHEIGHT() / 5);
                InfoWindow infoWindow = new InfoWindow(windowView, marker.getPosition(), elses);
                mBaiduMap.showInfoWindow(infoWindow);

                return true;
            }
        });
        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);
        mBaiduMap.setMyLocationEnabled(true);
    }

    private View initWindowView() {
        View view = View.inflate(getContext(), R.layout.fragment_map_info_window, null);
        CardView cardView = view.findViewById(R.id.info_window_card);
        ViewGroup.LayoutParams params = cardView.getLayoutParams();
        params.width = WindowUtil.getWIDTH() / 10 * 6;
        cardView.setLayoutParams(params);
        return view;
    }

    private void initView(View view) {
        userImage = view.findViewById(R.id.image_user);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean res = clickInterface.openDrawer();
                if (!res) {
                    Log.i("OpenDrawer", "error");
                }
            }
        });
        locationImage = view.findViewById(R.id.image_now_location);
        locationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentMode = LocationMode.FOLLOWING;
                mLocationClient.requestLocation();
            }
        });
        List<ImageView> button = new ArrayList<>();
        button.add(userImage);
        button.add(locationImage);
        int length = WindowUtil.getWIDTH() / 6;
        if (length != 0) {
            for (ImageView x : button) {
                ViewGroup.LayoutParams params = x.getLayoutParams();
                params.width = length;
                params.height = length;
                x.setLayoutParams(params);
            }
        }

        searchCard = view.findViewById(R.id.search_map_cardview);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) searchCard.getLayoutParams();
        params.width = WindowUtil.getWIDTH() == 0 ? 240 : WindowUtil.getWIDTH() / 3 * 2;
        params.height = 120;
        params.topMargin = 320;
        searchCard.setLayoutParams(params);
    }

    public class MyBDAbstractLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            locData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    .direction(bdLocation.getDirection())
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude())
                    .build();
            MyLocationConfiguration config = new MyLocationConfiguration(
                    mCurrentMode, true, null);
            mBaiduMap.setMyLocationConfiguration(config);
            mBaiduMap.setMyLocationData(locData);
            if (mCurrentMode == LocationMode.FOLLOWING)
                mCurrentMode = LocationMode.NORMAL;
            if (isFirstLocation) {
                LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(status);//动画的方式到中间
                isFirstLocation = false;
                Log.i("位置：", bdLocation.getAddrStr() == null ? "null" : bdLocation.getAddrStr());
            }
        }
    }

    private void initLocation() {
        Context context = getContext().getApplicationContext();
        mLocationClient = new LocationClient(context);
        mLocationListener = new MyBDAbstractLocationListener();
        /*注册监听*/
        mLocationClient.registerLocationListener(mLocationListener);
        /*配置定位*/
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIgnoreKillProcess(true);
        option.SetIgnoreCacheException(false);
        option.setWifiCacheTimeOut(5 * 60 * 1000);
        option.setEnableSimulateGps(false);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    private void initPanel(View view) {
        panelLayout = view.findViewById(R.id.sliding_layout);
    }


    private void initPosition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                MapFragment.this.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, BAIDU_READ_PHONE_STATE);
            else {
                initLocation();
            }
        } else {
            initLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == BAIDU_READ_PHONE_STATE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initLocation();
            }
        } else {
            Toast.makeText(getActivity(), "获取权限失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void setMarker(LatLng latLng) {
        View v = View.inflate(getContext(), R.layout.fragment_map_marker, null);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(v);
        OverlayOptions option = new MarkerOptions().position(latLng).icon(bitmap);
        mBaiduMap.addOverlay(option);
    }

    private void initPoints() {
        points.add(new LatLng(35.949121, 104.160009));
        points.add(new LatLng(35.94947, 104.16444));
    }

    @Override
    public boolean onFragmentBackPress() {
        if (panelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            panelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            return true;
        } else
            return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            ((MainActivity) context).setBackInterfaceListener(this);
            ((MainActivity) context).setActive(true);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Context context = getContext();
        if (context instanceof MainActivity) {
            ((MainActivity) context).setBackInterfaceListener(null);
            ((MainActivity) context).setActive(false);
        }
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
    }
}
