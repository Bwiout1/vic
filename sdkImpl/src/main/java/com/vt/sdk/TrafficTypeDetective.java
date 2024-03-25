package com.vt.sdk;


import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Pair;

import com.LogUtil;
import com.Switch;
import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import org.greenrobot.eventbus.EventBus;

import com.vt.ar.SdkManager;


import org.greenrobot.eventbus.EBEvents;

import java.util.Locale;

import mac.ysl.skc.BuildConfig;


public class TrafficTypeDetective {
    private final static String TAG = "referrer";

    private final SharedPreferences storage;
    private final SdkManager sdkManager;
    private final Object af_attr;
    public TrafficTypeDetective(SdkManager sdkManager, Object af_attr){
        this.sdkManager = sdkManager;
        this.storage = sdkManager.getEncryptSP();
        this.af_attr = af_attr;
    }

    private final String type_key = "tt_paid";
    public void detect(){
        if (storage.contains(type_key)){
            EventBus.getDefault().post(EBEvents.TRAFFIC_TYPE_RDY.EVT);
            return;
        }

        ThreadUtils.runInBackground(()->{
            InstallReferrerClient client = InstallReferrerClient.newBuilder(sdkManager.getApp()).build();
            client.startConnection(new InstallReferrerStateListener() {
                @Override
                public void onInstallReferrerSetupFinished(int responseCode) {
                    if (Switch.LOG_ON){
                        LogUtil.d(TAG, "detect, onInstallReferrerSetupFinished, responseCode="+ responseCode);
                    }

                    try {
                        if (responseCode == InstallReferrerClient.InstallReferrerResponse.OK) {
                            String referrer = client.getInstallReferrer().getInstallReferrer();

                            if (Switch.LOG_ON){
                                LogUtil.d(TAG, "detect, onInstallReferrerSetupFinished, referrer"+ referrer);
                            }

                            parseGPReferrer(referrer);

                            EventBus.getDefault().post(EBEvents.TRAFFIC_TYPE_RDY.EVT);

                           sdkManager.getAnalytics().logEvent("binstall",
                                   Pair.create("ref", TextUtils.isEmpty(referrer) ? "" : referrer),
                                   Pair.create("dev", sdkManager.getDeviceMetaData()),
                                   Pair.create("af_attr", getAppsflyerAttributionData()),
                                   Pair.create("isDebug", String.valueOf(BuildConfig.DEBUG))
                            );
                        }
                    } catch (Exception ignored) {
                        if(Switch.LOG_ON){
                            ignored.printStackTrace();
                        }
                    } finally {
                        client.endConnection();
                    }
                }

                @Override
                public void onInstallReferrerServiceDisconnected() {
                    if (Switch.LOG_ON) {
                        LogUtil.d(TAG, "detect, onInstallReferrerSetupFinished, onInstallReferrerServiceDisconnected");
                    }
                }
            });
        });
    }


    private void parseGPReferrer(String referrer){
        boolean isPaidUser = false;

        do {
            if (TextUtils.isEmpty(referrer))
                break;

            isPaidUser = !referrer.toLowerCase(Locale.ENGLISH).contains("organic");
        } while (false);

        storage.edit()
                .putBoolean(type_key, isPaidUser)
                .apply();
    }


    private String getAppsflyerAttributionData(){
        if (af_attr==null)
            return "null";

        return af_attr.toString();
    }

    public boolean isPaidUser(){
        if (BuildConfig.DEBUG)
            return true;

        if (storage.contains(type_key)){
            return storage.getBoolean(type_key, false);
        }

        return false;
    }
}

