package cn.edu.lzu.oss.ecab.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baidu.mapapi.SDKInitializer;

import java.util.ArrayList;
import java.util.List;

import cn.edu.lzu.oss.ecab.R;
import cn.edu.lzu.oss.ecab.fragment.ItemFragment;
import cn.edu.lzu.oss.ecab.fragment.MapFragment;
import cn.edu.lzu.oss.ecab.interfaces.BackPressInterface;
import cn.edu.lzu.oss.ecab.interfaces.UserClickInterface;
import cn.edu.lzu.oss.ecab.util.WindowUtil;
import cn.edu.lzu.oss.ecab.view.BaseViewPager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ImageView sendImage;
    private ImageView receiveImage;
    private BaseViewPager viewPager;
    private List<Fragment> fragments;
    private BackPressInterface backInterfaceListener;
    private boolean isActive = false;
    private UserClickInterface clickInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSDK();
        WindowUtil.initWindows(this);
        setContentView(R.layout.activity_main);

        initViews();

    }

    private void initSDK() {
        SDKInitializer.initialize(getApplicationContext());
    }

    private void initViews() {
        clickInterface = new UserClickInterface() {
            @Override
            public boolean openDrawer() {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                if (!drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.openDrawer(GravityCompat.START);
                    return true;
                } else
                    return false;
            }
        };
        MapFragment maps = MapFragment.newInstance();
        maps.setClickInterface(clickInterface);
        fragments = new ArrayList<>();
        fragments.add(maps);
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
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        sendImage = findViewById(R.id.image_send);
        receiveImage = findViewById(R.id.image_receive);
        List<ImageView> button = new ArrayList<>();
        button.add(sendImage);
        button.add(receiveImage);
        int width = (int) (WindowUtil.getWIDTH() / 2.5);
        if (width != 0) {
            for (ImageView x : button) {
                ViewGroup.LayoutParams params = x.getLayoutParams();
                params.width = width;
                x.setLayoutParams(params);
            }
        }
        sendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.send_select));
                receiveImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.receive_no_select));
                viewPager.setCurrentItem(0);
            }
        });
        receiveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.send_no_select));
                receiveImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.receive_select));
                viewPager.setCurrentItem(1);
            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (isActive && backInterfaceListener != null) {
            boolean result = backInterfaceListener.onFragmentBackPress();
            if (!result) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setBackInterfaceListener(BackPressInterface backInterfaceListener) {
        this.backInterfaceListener = backInterfaceListener;
    }

    public void setActive(boolean active) {
        isActive = active;
    }



}
