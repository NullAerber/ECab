package cn.edu.lzu.oss.ecab.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import cn.edu.lzu.oss.ecab.R;

/**
 * Created by Aerber on 2017/3/16.
 */
public class LaunchActivity extends Activity {
    ConnectivityManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //加载启动界面
        setContentView(R.layout.activity_splash);

        /**
         * 检测网络是否连接
         * @return
         */
        boolean flag = false;
        //得到网络连接信息
        manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null) {
            flag = manager.getActiveNetworkInfo().isAvailable();
        }
        if (!flag) {
            Toast.makeText(this, R.string.NO_NETWORK,Toast.LENGTH_SHORT).show();
            finish();
        }else {
            Handler handler =new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    User user = UserModel.getInstance().getCurrentUser();
//                    if (user == null) {
//                        startActivity(LoginActivity.class,null,true);
//                    }else{
//                        startActivity(MainActivity.class,null,true);
//                    }
                    Intent intent =  new Intent(LaunchActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            },1000);
        }
    }
}