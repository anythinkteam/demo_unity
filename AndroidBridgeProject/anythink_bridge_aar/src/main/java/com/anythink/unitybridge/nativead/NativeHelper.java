package com.anythink.unitybridge.nativead;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.AdError;
import com.anythink.nativead.api.ATNative;
import com.anythink.nativead.api.ATNativeAdView;
import com.anythink.nativead.api.ATNativeDislikeListener;
import com.anythink.nativead.api.ATNativeEventListener;
import com.anythink.nativead.api.ATNativeNetworkListener;
import com.anythink.nativead.api.NativeAd;
import com.anythink.unitybridge.MsgTools;
import com.anythink.unitybridge.UnityPluginUtils;
import com.anythink.unitybridge.utils.Const;
import com.anythink.unitybridge.utils.TaskManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Copyright (C) 2018 {XX} Science and Technology Co., Ltd.
 *
 * @version V{XX_XX}
 * @Author ：Created by zhoushubin on 2018/8/3.
 * @Email: zhoushubin@salmonads.com
 */

@SuppressWarnings("all")
public class NativeHelper {
    public static final String TAG = UnityPluginUtils.TAG;
    NativeListener mListener;
    Activity mActivity;

    String mUnitId;

    ATNativeAdView mATNativeAdView;
    ATNative mATNative;

    public NativeHelper(NativeListener pListener) {
        if (pListener == null) {
            Log.e(TAG, "Listener == null ..");
        }
        mListener = pListener;
        mActivity = UnityPluginUtils.getActivity("NativeHelper");
        mUnitId = "";
    }

    public void initNative(String unitid) {
        MsgTools.pirntMsg("initNative .. ");
        mUnitId = unitid;
        mATNative = new ATNative(mActivity, unitid, new ATNativeNetworkListener() {
            @Override
            public void onNativeAdLoaded() {
                MsgTools.pirntMsg("onNativeAdLoaded ..");
                TaskManager.getInstance().run_proxy(new Runnable() {
                    @Override
                    public void run() {
                        if (mListener != null) {
                            synchronized (NativeHelper.this) {
                                mListener.onNativeAdLoaded(mUnitId);
                            }
                        }
                    }
                });
            }

            @Override
            public void onNativeAdLoadFail(final AdError pAdError) {
                MsgTools.pirntMsg("onNativeAdLoadFail .." + pAdError.printStackTrace());
                TaskManager.getInstance().run_proxy(new Runnable() {
                    @Override
                    public void run() {
                        if (mListener != null) {
                            synchronized (NativeHelper.this) {
                                mListener.onNativeAdLoadFail(mUnitId, pAdError.getCode(), pAdError.printStackTrace());
                            }
                        }
                    }
                });
            }
        });

        if (mATNativeAdView == null) {
            mATNativeAdView = new ATNativeAdView(mActivity);
        }

        MsgTools.pirntMsg("initNative ..2");
    }

    public void loadNative(final String localExtra) {
        MsgTools.pirntMsg("loadNative .. localExtra: " + localExtra);
        UnityPluginUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mATNative == null) {
                    Log.e(TAG, "you must call initNative first ..");
                    TaskManager.getInstance().run_proxy(new Runnable() {
                        @Override
                        public void run() {
                            if (mListener != null) {
                                synchronized (NativeHelper.this) {
                                    mListener.onNativeAdLoadFail(mUnitId, "-1", "you must call initNative first ..");
                                }
                            }
                        }
                    });
                    return;
                }

                Map<String, Object> map = new HashMap<>();
                try {
                    JSONObject _jsonObject = new JSONObject(localExtra);
                    Iterator _iterator = _jsonObject.keys();
                    String key;
                    while (_iterator.hasNext()) {
                        key = (String) _iterator.next();
                        map.put(key, _jsonObject.get(key));

                        if (TextUtils.equals(Const.Native.native_ad_size, key)) {
                            String native_ad_size = _jsonObject.optString(key);

                            if (!TextUtils.isEmpty(native_ad_size)) {
                                String[] sizes = native_ad_size.split("x");
                                map.put("key_width", sizes[0]);
                                map.put("key_height", sizes[1]);
                            }
                        }
                    }
                    mATNative.setLocalExtra(map);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mATNative.makeAdRequest();
            }
        });
    }

    NativeAd mNativeAd;
    ViewInfo pViewInfo;

    private ViewInfo parseViewInfo(String showConfig) {
        pViewInfo = new ViewInfo();

        if (TextUtils.isEmpty(showConfig)) {
            Log.e(TAG, "showConfig is null ,user defult");
            pViewInfo = ViewInfo.createDefualtView(mActivity);
        }

        try {
            JSONObject _jsonObject = new JSONObject(showConfig);

            if (_jsonObject.has("parent")) {
                String tempjson = _jsonObject.getString("parent");
                MsgTools.pirntMsg("parent----> " + tempjson);
                pViewInfo.rootView = pViewInfo.parseINFO(tempjson, "parent", 0, 0);
            }

            if (_jsonObject.has("appIcon")) {
                String tempjson = _jsonObject.getString("appIcon");
                MsgTools.pirntMsg("appIcon----> " + tempjson);
                pViewInfo.IconView = pViewInfo.parseINFO(tempjson, "appIcon", 0, 0);
            }

            if (_jsonObject.has("mainImage")) {
                String tempjson = _jsonObject.getString("mainImage");
                MsgTools.pirntMsg("mainImage----> " + tempjson);
                pViewInfo.imgMainView = pViewInfo.parseINFO(tempjson, "mainImage", 0, 0);

            }

            if (_jsonObject.has("title")) {
                String tempjson = _jsonObject.getString("title");
                MsgTools.pirntMsg("title----> " + tempjson);
                pViewInfo.titleView = pViewInfo.parseINFO(tempjson, "title", 0, 0);
            }

            if (_jsonObject.has("desc")) {
                String tempjson = _jsonObject.getString("desc");
                MsgTools.pirntMsg("desc----> " + tempjson);
                pViewInfo.descView = pViewInfo.parseINFO(tempjson, "desc", 0, 0);
            }

            if (_jsonObject.has("adLogo")) {
                String tempjson = _jsonObject.getString("adLogo");
                MsgTools.pirntMsg("adLogo----> " + tempjson);
                pViewInfo.adLogoView = pViewInfo.parseINFO(tempjson, "adLogo", 0, 0);
            }

            if (_jsonObject.has("cta")) {
                String tempjson = _jsonObject.getString("cta");
                MsgTools.pirntMsg("cta----> " + tempjson);
                pViewInfo.ctaView = pViewInfo.parseINFO(tempjson, "cta", 0, 0);
            }


        } catch (JSONException pE) {
            pE.printStackTrace();
        }
        return pViewInfo;
    }


    public void show(final String showConfig) {
        MsgTools.pirntMsg("show" + showConfig);

        UnityPluginUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NativeAd nativeAd = mATNative.getNativeAd();
                if (nativeAd != null) {
                    MsgTools.pirntMsg("nativeAd:" + nativeAd.toString());
                    pViewInfo = parseViewInfo(showConfig);
                    currViewInfo.add(pViewInfo);
                    mNativeAd = nativeAd;
                    nativeAd.setNativeEventListener(new ATNativeEventListener() {
                        @Override
                        public void onAdImpressed(ATNativeAdView view, final ATAdInfo adInfo) {
                            TaskManager.getInstance().run_proxy(new Runnable() {
                                @Override
                                public void run() {
                                    if (mListener != null) {
                                        synchronized (NativeHelper.this) {
                                            mListener.onAdImpressed(mUnitId, adInfo.toString());
                                        }
                                    }
                                }
                            });
                        }

                        @Override
                        public void onAdClicked(ATNativeAdView view, final ATAdInfo adInfo) {
                            TaskManager.getInstance().run_proxy(new Runnable() {
                                @Override
                                public void run() {
                                    if (mListener != null) {
                                        synchronized (NativeHelper.this) {
                                            mListener.onAdClicked(mUnitId, adInfo.toString());
                                        }
                                    }
                                }
                            });
                        }

                        @Override
                        public void onAdVideoStart(ATNativeAdView view) {
                            TaskManager.getInstance().run_proxy(new Runnable() {
                                @Override
                                public void run() {
                                    if (mListener != null) {
                                        synchronized (NativeHelper.this) {
                                            mListener.onAdVideoStart(mUnitId);
                                        }
                                    }
                                }
                            });
                        }

                        @Override
                        public void onAdVideoEnd(ATNativeAdView view) {
                            TaskManager.getInstance().run_proxy(new Runnable() {
                                @Override
                                public void run() {
                                    if (mListener != null) {
                                        synchronized (NativeHelper.this) {
                                            mListener.onAdVideoEnd(mUnitId);
                                        }
                                    }
                                }
                            });

                        }

                        @Override
                        public void onAdVideoProgress(ATNativeAdView view, final int progress) {
                            TaskManager.getInstance().run_proxy(new Runnable() {
                                @Override
                                public void run() {
                                    if (mListener != null) {
                                        synchronized (NativeHelper.this) {
                                            mListener.onAdVideoProgress(mUnitId, progress);
                                        }
                                    }
                                }
                            });

                        }
                    });

                    nativeAd.setDislikeCallbackListener(new ATNativeDislikeListener() {
                        @Override
                        public void onAdCloseButtonClick(ATNativeAdView atNativeAdView, final ATAdInfo atAdInfo) {
                            TaskManager.getInstance().run_proxy(new Runnable() {
                                @Override
                                public void run() {
                                    if (mListener != null) {
                                        synchronized (NativeHelper.this) {
                                            mListener.onAdCloseButtonClicked(mUnitId, atAdInfo.toString());
                                        }
                                    }
                                }
                            });
                        }
                    });

                    try {
                        nativeAd.renderAdView(mATNativeAdView, new ATUnityRender(mActivity, pViewInfo));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (pViewInfo.adLogoView != null) {
                        FrameLayout.LayoutParams adLogoLayoutParams = new FrameLayout.LayoutParams(pViewInfo.adLogoView.mWidth, pViewInfo.adLogoView.mHeight);
                        adLogoLayoutParams.leftMargin = pViewInfo.adLogoView.mX;
                        adLogoLayoutParams.topMargin = pViewInfo.adLogoView.mY;
                        nativeAd.prepare(mATNativeAdView, adLogoLayoutParams);
                        MsgTools.pirntMsg("prepare native ad with logo:" + mUnitId);
                    } else {
                        nativeAd.prepare(mATNativeAdView);
                        MsgTools.pirntMsg("prepare native ad:" + mUnitId);
                    }

                    ViewInfo.addNativeAdView2Activity(mActivity, pViewInfo, mATNativeAdView);
                } else {
                    TaskManager.getInstance().run_proxy(new Runnable() {
                        @Override
                        public void run() {
                            if (mListener != null) {
                                synchronized (NativeHelper.this) {
                                    mListener.onNativeAdLoadFail(mUnitId, "-1", "onNativeAdLoadFail");
                                }
                            }
                        }
                    });
                }
            }
        });


    }


    public boolean isAdReady() {
        mNativeAd = mATNative.getNativeAd();
        return mNativeAd != null;
    }

    public void clean() {
        if (mATNative != null) {
//            mATNative.destory();
        }
    }

    static List<ViewInfo> currViewInfo = new ArrayList<>();

    public void cleanView() {

        UnityPluginUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {

                    for (ViewInfo tempinfo : currViewInfo) {
                        if (mATNativeAdView != null) {
                            ViewGroup _viewGroup = (ViewGroup) mATNativeAdView.getParent();
                            if (_viewGroup != null) {
                                _viewGroup.removeView(mATNativeAdView);

                            }
                        }
                    }

                } catch (Exception e) {
                    if (Const.DEBUG) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    public void onPause() {
        if (mNativeAd != null) {
            mNativeAd.onPause();
        }
    }

    public void onResume() {
        if (mNativeAd != null) {
            mNativeAd.onResume();
        }
    }
}
