﻿using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using AnyThinkAds.Common;
using AnyThinkAds.Api;

namespace AnyThinkAds.Android
{
    public class ATSDKAPIClient : AndroidJavaProxy, IATSDKAPIClient
    {
		private AndroidJavaObject sdkInitHelper;
        private ATSDKInitListener sdkInitListener;
        public ATSDKAPIClient () : base("com.anythink.unitybridge.sdkinit.SDKInitListener")
        {
            this.sdkInitHelper = new AndroidJavaObject(
                "com.anythink.unitybridge.sdkinit.SDKInitHelper", this);
		}

        public void initSDK(string appId, string appKey)
        {
            this.initSDK(appId, appKey, null);
        }

        public void initSDK(string appId, string appKey, ATSDKInitListener listener)
        {
            Debug.Log("initSDK....");
            sdkInitListener = listener;
            try
            {
                if (this.sdkInitHelper != null)
                {
                    this.sdkInitHelper.Call("initAppliction", appId, appKey);
                }
            }
            catch (System.Exception e)
            {
                System.Console.WriteLine("Exception caught: {0}", e);
				Debug.Log ("ATSDKAPIClient :  error."+e.Message);
            }
        }

        public void getUserLocation(ATGetUserLocationListener listener)
        {
            ATNetTrafficListener netTrafficListener = new ATNetTrafficListener(listener);
            try
            {
                if (this.sdkInitHelper != null)
                {
                    this.sdkInitHelper.Call("checkIsEuTraffic", netTrafficListener);
                }
            }
            catch (System.Exception e)
            {
                System.Console.WriteLine("Exception caught: {0}", e);
                Debug.Log("ATSDKAPIClient :  error." + e.Message);
            }
            //implement getting location here
        }

        public void setGDPRLevel(int level)
        {
			Debug.Log ("setGDPRLevel....");
			try{
				if (this.sdkInitHelper != null) {
					this.sdkInitHelper.Call ("setGDPRLevel",level);
				}
			}catch(System.Exception e){
				System.Console.WriteLine("Exception caught: {0}", e);
				Debug.Log ("ATSDKAPIClient :  error."+e.Message);
			}
           
        }

        public void showGDPRAuth()
        {
			Debug.Log ("showGDPRAuth....");
			try{
				if (this.sdkInitHelper != null) {
					this.sdkInitHelper.Call ("showGDPRAuth");
				}
			}catch(System.Exception e){
				System.Console.WriteLine("Exception caught: {0}", e);
				Debug.Log ("ATSDKAPIClient :  error."+e.Message);

			}
        }

        public void setChannel(string channel)
        {
            Debug.Log("setChannel....");
            try
            {
                if (this.sdkInitHelper != null)
                {
                    this.sdkInitHelper.Call("setChannel", channel);
                }
            }
            catch (System.Exception e)
            {
                System.Console.WriteLine("Exception caught: {0}", e);
                Debug.Log("ATSDKAPIClient :  error." + e.Message);
            }
        }

        public void setSubChannel(string subchannel)
        {
            Debug.Log("setSubChannel....");
            try
            {
                if (this.sdkInitHelper != null)
                {
                    this.sdkInitHelper.Call("setSubChannel", subchannel);
                }
            }
            catch (System.Exception e)
            {
                System.Console.WriteLine("Exception caught: {0}", e);
                Debug.Log("ATSDKAPIClient :  error." + e.Message);
            }
        }

        public void initCustomMap(string jsonMap)
        {
            Debug.Log("initCustomMap....");
            try
            {
                if (this.sdkInitHelper != null)
                {
                    this.sdkInitHelper.Call("initCustomMap", jsonMap);
                }
            }
            catch (System.Exception e)
            {
                System.Console.WriteLine("Exception caught: {0}", e);
                Debug.Log("ATSDKAPIClient :  error." + e.Message);
            }
        }

        public void setCustomDataForPlacementID(string customData, string placementID)
        {
            Debug.Log("setCustomDataForPlacementID....");
            try
            {
                if (this.sdkInitHelper != null)
                {
                    this.sdkInitHelper.Call("initPlacementCustomMap", placementID, customData);
                }
            }
            catch (System.Exception e)
            {
                System.Console.WriteLine("Exception caught: {0}", e);
                Debug.Log("ATSDKAPIClient :  error." + e.Message);
            }
        }

        public void setLogDebug(bool isDebug)
        {
            Debug.Log("setLogDebug....");
            try
            {
                if (this.sdkInitHelper != null)
                {
                    this.sdkInitHelper.Call("setDebugLogOpen", isDebug);
                }
            }
            catch (System.Exception e)
            {
                System.Console.WriteLine("Exception caught: {0}", e);
                Debug.Log("ATSDKAPIClient :  error." + e.Message);
            }
        }



        public void setPurchaseFlag() {
        
        }

        public void clearPurchaseFlag() {

        }

        public bool purchaseFlag() {
            return false;
        }

        public void addNetworkGDPRInfo(int networkType, string mapJson)
        {
			Debug.Log ("addNetworkGDPRInfo...." + networkType + "mapjson:"+mapJson);
			try{
				if (this.sdkInitHelper != null) {
					this.sdkInitHelper.Call ("addNetworkGDPRInfo",networkType,mapJson);
				}
			}catch(System.Exception e){
				System.Console.WriteLine("Exception caught: {0}", e);
				Debug.Log ("ATSDKAPIClient :  error."+e.Message);
			}

        }

        public void initSDKSuccess(string appid)
        {
            Debug.Log("initSDKSuccess...unity3d.");
            if(sdkInitListener != null){
                sdkInitListener.initSuccess();
            }
        }

        public void initSDKError(string appid, string message)
        {
            Debug.Log("initSDKError..unity3d..");
            if (sdkInitListener != null)
            {
                sdkInitListener.initFail(message);
            }
        }

        public int getGDPRLevel()
        {
            Debug.Log("getGDPRLevel....");
            try
            {
                if (this.sdkInitHelper != null)
                {
                    return this.sdkInitHelper.Call<int>("getGDPRLevel");
                }
            }
            catch (System.Exception e)
            {
                System.Console.WriteLine("Exception caught: {0}", e);
                Debug.Log("ATSDKAPIClient :  error." + e.Message);
            }
            return 2; //UNKNOW
        }

        public bool isEUTraffic()
        {
            Debug.Log("isEUTraffic....");
            try
            {
                if (this.sdkInitHelper != null)
                {
                    return this.sdkInitHelper.Call<bool>("isEUTraffic");
                }
            }
            catch (System.Exception e)
            {
                System.Console.WriteLine("Exception caught: {0}", e);
                Debug.Log("ATSDKAPIClient :  error." + e.Message);
            }
            return false;
        }
    }
}
