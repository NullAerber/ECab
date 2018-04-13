package cn.edu.lzu.oss.ecab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.baidu.mapapi.SDKInitializer;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import cn.edu.lzu.oss.ecab.fragment.ItemFragment;
import cn.edu.lzu.oss.ecab.fragment.MapFragment;
import cn.edu.lzu.oss.ecab.view.BaseViewPager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private BaseViewPager viewPager;
    private List<Fragment> fragments;
    private SegmentTabLayout segmentTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSDK();
        setContentView(R.layout.activity_main);
        initAll();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initSDK() {
        SDKInitializer.initialize(getApplicationContext());
    }

    private void initAll() {
        fragments = new ArrayList<>();
        fragments.add(MapFragment.newInstance());
        fragments.add(ItemFragment.newInstance());
        viewPager = findViewById(R.id.main_view_pager);
        viewPager.setScanScroll(false);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments == null ? 0 : fragments.size();
            }
        });
        segmentTabLayout = findViewById(R.id.seg_tab);
        segmentTabLayout.setTabData(new String[]{"aaa", "bbb"});
        segmentTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        try {
            ((MapFragment) getSupportFragmentManager().findFragmentByTag("")).getPanel().getPanelState();
        } catch (Exception e) {
            Log.i("Fail", "Fail to find MapFragment");
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
