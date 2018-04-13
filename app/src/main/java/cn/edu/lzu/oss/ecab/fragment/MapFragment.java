package cn.edu.lzu.oss.ecab.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;


import cn.edu.lzu.oss.ecab.MainActivity;
import cn.edu.lzu.oss.ecab.R;
import cn.edu.lzu.oss.ecab.interfaces.FragmentInterface;

public class MapFragment extends Fragment implements FragmentInterface {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private SlidingUpPanelLayout panelLayout;

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
    }
}
