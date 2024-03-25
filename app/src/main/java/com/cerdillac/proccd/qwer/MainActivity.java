package com.cerdillac.proccd.qwer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;



public class MainActivity extends Activity {
    private static String[] PERMISSIONS_STORAGE = {
            "Manifest.permission.READ_EXTERNAL_STORAGE",
            "Manifest.permission.WRITE_EXTERNAL_STORAGE"};
    private static int REQUEST_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        checkPermission();
    }private void initView() {

        long startTime = SystemClock.elapsedRealtime();
        long elapsedTime = SystemClock.elapsedRealtime() - startTime;

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            //这里打开app的首页
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);

            if (BuildConfig.DEBUG) {
                //todo,注意
                //debug 模式下， fairbid可能会弹出test suit对话框。
                // app首次启动时，fairbid在mainActivity:onCreate时初始化，关掉splash没有问题。
                // 在非首次启动时，fairbid在splash::onCreate时初始化，然后弹出一个依附于splash的对话框，如果此处把splash关掉，app会crash
                //会导致安卓6上hideIcon时应用被杀
            } else {
                //release模式下，不会弹test suit对话框，可安全关掉splash页面
                finish();
            }
        }, Math.max(0, 3000 - elapsedTime)); //不足3秒则等待3秒
    }
    private void checkPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,  android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, getString(R.string.please_open_the_relevant_permissions_otherwise_the_application_cannot_be_used_normally), Toast.LENGTH_SHORT).show();
            }
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{ android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);

        } else {
            Toast.makeText(this, getString(R.string.authorization_succeeded), Toast.LENGTH_SHORT).show();
            Log.e("aaaaa", getString(R.string.checkPermission_Authorization_succeeded));
        }
    }
}