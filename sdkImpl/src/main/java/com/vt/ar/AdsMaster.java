package com.vt.ar;

import android.app.Activity;
import android.content.Context;
import android.util.Pair;
import android.view.ViewGroup;

import com.LogUtil;
import com.Switch;
import com.vt.sdk.AdImpCallback;
import com.vt.sdk.Analytics;
import com.vt.sdk.ExtAdImpRecord;
import com.vt.sdk.FairbidAdapter;
import com.vt.sdk.PangleAdapter;
import com.vt.sdk.ThreadUtils;

import org.greenrobot.eventbus.EventBus;

import org.greenrobot.eventbus.EBEvents;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class AdsMaster {
    private final static String TAG = "ads";

    private final RemoteConfigManager configManager;
    private final ExtAdImpRecord record;
    private final Analytics analytics;
    private final PangleAdapter pglAdapter;
    private final FairbidAdapter dtApdater;

    AdsMaster(SdkManager sdkManager) {
        configManager = sdkManager.getConfigManager();
        record = sdkManager.getExtAdImpRecord();
        analytics = sdkManager.getAnalytics();

        pglAdapter = new PangleAdapter(this, configManager);
        dtApdater = new FairbidAdapter(this, configManager);
        EventBus.getDefault().registerForMainThread(this);
    }

    AtomicBoolean pglInit = new AtomicBoolean(false);
    AtomicBoolean dtInit = new AtomicBoolean(false);

    public void init(Context context) {
        if (Switch.LOG_ON) LogUtil.d(TAG, "init, context=" + context);
        if (!pglInit.get() && configManager.sdkEnabled()) {
            pglAdapter.init(context);
        }

        if (!dtInit.get() && (context instanceof Activity) && configManager.sdkEnabled()) {
            dtApdater.init((Activity) context);
        }
    }

    void onEvent(EBEvents.PANGLE_SDK_RDY rdy) {
        if (Switch.LOG_ON) LogUtil.d(TAG, "onEvent, EBEvents.PANGLE_SDK_RDY");

        EventBus.getDefault().unregister(this, EBEvents.PANGLE_SDK_RDY.class);
        pglInit.set(true);

        if (record.hasPangleQuota()) {
            pglAdapter.loadInters(1);
        }

        if (record.hasWebOfferQuota()) {
            pglAdapter.loadNative(1);
        }
    }

    void onEvent(EBEvents.DT_SDK_RDY rdy) {
        if (Switch.LOG_ON) LogUtil.d(TAG, "onEvent, EBEvents.DT_SDK_RDY");

        EventBus.getDefault().unregister(this, EBEvents.DT_SDK_RDY.class);
        dtInit.set(true);

        if (record.hasDtQuota()) {
            dtApdater.loadInters();
        }
    }

    public boolean hasInters() {
        boolean rdy = false;

        if (configManager.sdkEnabled()) {
            rdy = pglAdapter.hasInters() || dtApdater.hasInters();
        }

        return rdy;
    }

    public void loadInters() {
        if (configManager.sdkEnabled()) {
            if (record.hasPangleQuota() && pglInit.get()) {
                pglAdapter.loadInters(1);
            }

            if (record.hasDtQuota() && dtInit.get()) {
                dtApdater.loadInters();
            }
        }
    }

    public void showInters(Activity activity) {
        if (activity.isDestroyed() || activity.isFinishing())
            return;

        if (!configManager.sdkEnabled())
            return;

        AdImpCallback callback = new AdImpCallback() {
            @Override
            public void onShow(String network) {
                analytics.logEvent("inapp_inters",
                        Pair.create("adn", network));
            }
        };

        if (pglAdapter.hasInters()
                && (new Random().nextInt(100) < 66 || !dtApdater.hasInters())) {
            pglAdapter.showInters(activity, callback);

        } else if (dtApdater.hasInters()) {
            dtApdater.showInters(activity, callback);
        }
    }

    public void showInters(Activity activity, AdImpCallback callback) {
        if (activity.isDestroyed() || activity.isFinishing()) {
            if (callback != null) {
                ThreadUtils.runInUIThread(() -> callback.onShowFail("act-finish"));
            }
            return;
        }

        if (!configManager.sdkEnabled()) {
            if (callback != null) {
                ThreadUtils.runInUIThread(() -> callback.onShowFail("!sdk-enable"));
            }
            return;
        }

        if (pglAdapter.hasInters() && record.hasPangleQuota()
                && (new Random().nextInt(100) < 66 || !dtApdater.hasInters())) {
            pglAdapter.showInters(activity, callback);
        } else if (dtApdater.hasInters() && record.hasDtQuota()) {
            dtApdater.showInters(activity, callback);
        } else {
            if (callback != null) {
                ThreadUtils.runInUIThread(() -> callback.onShowFail("!rdy"));
            }
        }
    }


    public boolean hasInner(boolean medium) {
        boolean rdy = false;
        if (configManager.sdkEnabled()) {
            rdy = pglAdapter.hasNative();
        }

        return rdy;
    }

    public void loadInner(boolean medium) {
        if (configManager.sdkEnabled() && record.hasWebOfferQuota() && pglInit.get()) {
            pglAdapter.loadNative(1);
        }
    }

    public void addInner(Activity activity, ViewGroup container) {
        if (container == null || activity.isDestroyed() || activity.isFinishing())
            return;

        if (!configManager.sdkEnabled())
            return;

        if (container.getChildCount() > 0)
            return;

        String size = (String) container.getTag();
        boolean medium = size.contains("b");

        if (pglAdapter.hasNative()) {
            pglAdapter.renderNative(activity, container, medium, new AdImpCallback() {
                @Override public void onShow(String network) {
                    analytics.logEvent("inapp_inner",
                            Pair.create("adn", "pgl"),
                            Pair.create("size", medium ? "medium":"banner"));
                }
            });
        } else {
            loadInner(medium);
        }
    }

    public void addInner(Activity activity, ViewGroup container, AdImpCallback callback) {
        if (container == null || activity.isDestroyed() || activity.isFinishing()) {
            if (callback != null) {
                ThreadUtils.runInUIThread(() -> callback.onShowFail("act-finish"));
            }
            return;
        }

        if (!configManager.sdkEnabled()) {
            if (callback != null) {
                ThreadUtils.runInUIThread(() -> callback.onShowFail("!sdk-enable"));
            }
            return;
        }

        if (container.getChildCount() > 0) {
            if (callback != null) {
                ThreadUtils.runInUIThread(() -> callback.onShowFail("havedone"));
            }
            return;
        }

        String size = (String) container.getTag();
        boolean medium = size.contains("b");

        if (pglAdapter.hasNative()) {
            pglAdapter.renderNative(activity, container, medium, callback);
        } else {
            loadInner(medium);
            if (callback != null) {
                ThreadUtils.runInUIThread(() -> callback.onShowFail("!rdy"));
            }
        }
    }
}
