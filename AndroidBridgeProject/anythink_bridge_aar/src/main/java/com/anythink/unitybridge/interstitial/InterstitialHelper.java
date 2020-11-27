package com.anythink.unitybridge.interstitial;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.ATAdStatusInfo;
import com.anythink.core.api.AdError;
import com.anythink.interstitial.api.ATInterstitial;
import com.anythink.interstitial.api.ATInterstitialListener;
import com.anythink.unitybridge.MsgTools;
import com.anythink.unitybridge.UnityPluginUtils;
import com.anythink.unitybridge.utils.Const;
import com.anythink.unitybridge.utils.TaskManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Copyright (C) 2018 {XX} Science and Technology Co., Ltd.
 */
public class InterstitialHelper {
    public static final String TAG = UnityPluginUtils.TAG;
    InterstitialListener mListener;
    Activity mActivity;
    ATInterstitial mInterstitialAd;
    String mUnitId;

    boolean isReady = false;

    public InterstitialHelper(InterstitialListener listener) {
        MsgTools.pirntMsg("InterstitialHelper >>> " + this);
        if (listener == null) {
            Log.e(TAG, "Listener == null ..");
        }
        mListener = listener;
        mActivity = UnityPluginUtils.getActivity("InterstitialHelper");
    }


    public void initInterstitial(final String unitid) {
        MsgTools.pirntMsg("initInterstitial 1>>> " + this);

        mInterstitialAd = new ATInterstitial(mActivity, unitid);
        mUnitId = unitid;


        MsgTools.pirntMsg("initInterstitial 2>>> " + this);

        mInterstitialAd.setAdListener(new ATInterstitialListener() {
            @Override
            public void onInterstitialAdLoaded() {
                MsgTools.pirntMsg("initInterstitial onInterstitialAdLoaded>>> ");
                isReady = true;
                TaskManager.getInstance().run_proxy(new Runnable() {
                    @Override
                    public void run() {
                        if (mListener != null) {
                             synchronized (InterstitialHelper.this) {
                                mListener.onInterstitialAdLoaded(mUnitId);
                            }
                        }
                    }
                });
            }

            @Override
            public void onInterstitialAdLoadFail(final AdError adError) {
                MsgTools.pirntMsg("initInterstitial onInterstitialAdLoadFail>>> " + adError.printStackTrace());
                TaskManager.getInstance().run_proxy(new Runnable() {
                    @Override
                    public void run() {
                        if (mListener != null) {
                             synchronized (InterstitialHelper.this) {
                                mListener.onInterstitialAdLoadFail(mUnitId, adError.getCode(), adError.printStackTrace());
                            }
                        }
                    }
                });
            }

            @Override
            public void onInterstitialAdClicked(final ATAdInfo adInfo) {
                MsgTools.pirntMsg("initInterstitial onInterstitialAdClicked>>> ");
                TaskManager.getInstance().run_proxy(new Runnable() {
                    @Override
                    public void run() {
                        if (mListener != null) {
                             synchronized (InterstitialHelper.this) {
                                mListener.onInterstitialAdClicked(mUnitId, adInfo.toString());
                            }
                        }
                    }
                });
            }

            @Override
            public void onInterstitialAdShow(final ATAdInfo adInfo) {
                MsgTools.pirntMsg("initInterstitial onInterstitialAdShow>>> ");
                TaskManager.getInstance().run_proxy(new Runnable() {
                    @Override
                    public void run() {
                        if (mListener != null) {
                             synchronized (InterstitialHelper.this) {
                                mListener.onInterstitialAdShow(mUnitId, adInfo.toString());
                            }
                        }
                    }
                });
            }

            @Override
            public void onInterstitialAdClose(final ATAdInfo adInfo) {
                MsgTools.pirntMsg("initInterstitial onInterstitialAdClose>>> ");
                TaskManager.getInstance().run_proxy(new Runnable() {
                    @Override
                    public void run() {
                        if (mListener != null) {
                             synchronized (InterstitialHelper.this) {
                                mListener.onInterstitialAdClose(mUnitId, adInfo.toString());
                            }
                        }
                    }
                });
            }

            @Override
            public void onInterstitialAdVideoStart(final ATAdInfo adInfo) {
                MsgTools.pirntMsg("initInterstitial onInterstitialAdVideoStart>>> ");
                TaskManager.getInstance().run_proxy(new Runnable() {
                    @Override
                    public void run() {
                        if (mListener != null) {
                             synchronized (InterstitialHelper.this) {
                                mListener.onInterstitialAdVideoStart(mUnitId, adInfo.toString());
                            }
                        }
                    }
                });
            }

            @Override
            public void onInterstitialAdVideoEnd(final ATAdInfo adInfo) {
                MsgTools.pirntMsg("initInterstitial onInterstitialAdVideoEnd>>> ");
                TaskManager.getInstance().run_proxy(new Runnable() {
                    @Override
                    public void run() {
                        if (mListener != null) {
                             synchronized (InterstitialHelper.this) {
                                mListener.onInterstitialAdVideoEnd(mUnitId, adInfo.toString());
                            }
                        }
                    }
                });
            }

            @Override
            public void onInterstitialAdVideoError(final AdError adError) {
                MsgTools.pirntMsg("initInterstitial onInterstitialAdVideoError>>> :" + adError.printStackTrace());
                TaskManager.getInstance().run_proxy(new Runnable() {
                    @Override
                    public void run() {
                        if (mListener != null) {
                             synchronized (InterstitialHelper.this) {
                                mListener.onInterstitialAdVideoError(mUnitId, adError.getCode(), adError.printStackTrace());
                            }
                        }
                    }
                });
            }
        });
        MsgTools.pirntMsg("initInterstitial 3>>> " + this);
    }


    public void loadInterstitialAd(final String jsonMap) {
        MsgTools.pirntMsg("loadInterstitialAd >>> " + this + ", jsonMap >>> " + jsonMap);

        if (!TextUtils.isEmpty(jsonMap)) {
            Map<String, Object> localExtra = new HashMap<>();
            try {
                JSONObject jsonObject = new JSONObject(jsonMap);
                String useRewardedVideoAsInterstitial = (String) jsonObject.get(Const.Interstital.UseRewardedVideoAsInterstitial);

                if (useRewardedVideoAsInterstitial != null && TextUtils.equals(Const.Interstital.UseRewardedVideoAsInterstitialYes, useRewardedVideoAsInterstitial)) {
                    localExtra.put("is_use_rewarded_video_as_interstitial", true);
                }

                Const.fillMapFromJsonObject(localExtra, jsonObject);

                if (mInterstitialAd != null) {
                    mInterstitialAd.setLocalExtra(localExtra);
                }
            } catch (Throwable e) {
            }

        }

        UnityPluginUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (mInterstitialAd != null) {

                    mInterstitialAd.load();
                } else {
                    Log.e(TAG, "loadInterstitialAd error  ..you must call initInterstitial first " + this);
                    TaskManager.getInstance().run_proxy(new Runnable() {
                        @Override
                        public void run() {
                            if (mListener != null) {
                                 synchronized (InterstitialHelper.this) {
                                    mListener.onInterstitialAdLoadFail(mUnitId, "-1", "you must call initInterstitial first ..");
                                }
                            }
                        }
                    });
                }

            }
        });
    }

    public void showInterstitialAd(final String jsonMap) {
        MsgTools.pirntMsg("showInterstitial >>> " + this + ", jsonMap >>> " + jsonMap);
        UnityPluginUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mInterstitialAd != null) {
                    isReady = false;

                    String scenario = "";
                    if (!TextUtils.isEmpty(jsonMap)) {
                        try {
                            JSONObject _jsonObject = new JSONObject(jsonMap);
                            if (_jsonObject.has(Const.SCENARIO)) {
                                scenario = _jsonObject.optString(Const.SCENARIO);
                            }
                        } catch (Exception e) {
                            if (Const.DEBUG) {
                                e.printStackTrace();
                            }
                        }
                    }
                    MsgTools.pirntMsg("showInterstitialAd >>> " + this + ", scenario >>> " + scenario);
                    mInterstitialAd.show(mActivity, scenario);
                } else {
                    Log.e(TAG, "showInterstitial error  ..you must call initInterstitial first " + this);
                    TaskManager.getInstance().run_proxy(new Runnable() {
                        @Override
                        public void run() {
                            if (mListener != null) {
                                 synchronized (InterstitialHelper.this) {
                                    mListener.onInterstitialAdLoadFail(mUnitId, "-1", "you must call initInterstitial first ..");
                                }
                            }
                        }
                    });
                }
            }
        });

    }

    public boolean isAdReady() {
        MsgTools.pirntMsg("isAdReady >start>> " + this);

        try {
            if (mInterstitialAd != null) {
                boolean isAdReady = mInterstitialAd.isAdReady();
                MsgTools.pirntMsg("isAdReady >>> " + isAdReady);
                return isAdReady;
            } else {
                Log.e(TAG, "isAdReady error  ..you must call initInterstitial first ");

            }
            MsgTools.pirntMsg("isAdReady >ent>> " + this);
        } catch (Exception e) {
            MsgTools.pirntMsg("isAdReady >Exception>> " + e.getMessage());
//            e.printStackTrace();
            return isReady;

        } catch (Throwable e) {
            MsgTools.pirntMsg("isAdReady >Throwable>> " + e.getMessage());
//            e.printStackTrace();
            return isReady;
        }
        return isReady;
    }

    public void clean() {
        MsgTools.pirntMsg("clean >>> " + this);
        if (mInterstitialAd != null) {
            isReady = false;
            mInterstitialAd.clean();
        } else {
            Log.e(TAG, "clean error  ..you must call initInterstitial first ");

        }

    }

    public void onPause() {
        MsgTools.pirntMsg("onPause-->");
        if (mInterstitialAd != null) {
            mInterstitialAd.onPause();
        }
    }

    public void onResume() {
        MsgTools.pirntMsg("onResume-->");
        if (mInterstitialAd != null) {
            mInterstitialAd.onResume();
        }
    }

    public String checkAdStatus() {
        if (mInterstitialAd != null) {
            ATAdStatusInfo atAdStatusInfo = mInterstitialAd.checkAdStatus();
            boolean loading = atAdStatusInfo.isLoading();
            boolean ready = atAdStatusInfo.isReady();
            ATAdInfo atTopAdInfo = atAdStatusInfo.getATTopAdInfo();

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("isLoading", loading);
                jsonObject.put("isReady", ready);
                jsonObject.put("adInfo", atTopAdInfo);

                return jsonObject.toString();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return "";
    }

}
