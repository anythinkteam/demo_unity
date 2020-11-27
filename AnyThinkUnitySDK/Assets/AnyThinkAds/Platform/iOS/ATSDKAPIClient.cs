using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using AnyThinkAds.Common;
using AnyThinkAds.Api;
using AOT;
using System;

namespace AnyThinkAds.iOS {
	public class ATSDKAPIClient : IATSDKAPIClient {
        static private ATGetUserLocationListener locationListener;
        public ATSDKAPIClient () {
            Debug.Log("Unity:ATSDKAPIClient::ATSDKAPIClient()");
		}
		public void initSDK(string appId, string appKey) {
			Debug.Log("Unity:ATSDKAPIClient::initSDK(string, string)");
			initSDK(appId, appKey, null);
	    }

	    public void initSDK(string appId, string appKey, ATSDKInitListener listener) {
	    	Debug.Log("Unity:ATSDKAPIClient::initSDK(string, string, ATSDKInitListener)");
	    	bool started = ATManager.StartSDK(appId, appKey);
            if (listener != null)
            {
                if (started)
                {
                    listener.initSuccess();
                }
                else
                {
                    listener.initFail("Failed to init.");
                }
            }
	    }

        [MonoPInvokeCallback(typeof(Func<string, int>))]
       static public int DidGetUserLocation(string location)
        {
            if (locationListener != null) { locationListener.didGetUserLocation(Int32.Parse(location)); }
            return 0;
        }

        public void getUserLocation(ATGetUserLocationListener listener)
        {
            Debug.Log("Unity:ATSDKAPIClient::getUserLocation()");
            ATSDKAPIClient.locationListener = listener;
            ATManager.getUserLocation(DidGetUserLocation);
        }

        public void setGDPRLevel(int level) {
	    	Debug.Log("Unity:ATSDKAPIClient::setGDPRLevel()");
	    	ATManager.SetDataConsent(level);
	    }

	    public void showGDPRAuth() {
	    	Debug.Log("Unity:ATSDKAPIClient::showGDPRAuth()");
	    	ATManager.ShowGDPRAuthDialog();
	    }

	    public void setPurchaseFlag() {
			ATManager.setPurchaseFlag();
		}

		public void clearPurchaseFlag() {
			ATManager.clearPurchaseFlag();
		}

		public bool purchaseFlag() {
			return ATManager.purchaseFlag();
		}

	    public void addNetworkGDPRInfo(int networkType, string mapJson) {
	    	Debug.Log("Unity:ATSDKAPIClient::addNetworkGDPRInfo()");
	    	ATManager.SetNetworkGDPRInfo(networkType, mapJson);
	    }

        public void setChannel(string channel)
        {
            ATManager.setChannel(channel);
        }

        public void setSubChannel(string subchannel)
        {
            ATManager.setSubChannel(subchannel);
        }

        public void initCustomMap(string jsonMap)
        {
            ATManager.setCustomMap(jsonMap);
        }

        public void setCustomDataForPlacementID(string customData, string placementID)
        {
            ATManager.setCustomDataForPlacementID(customData, placementID);
        }

        public void setLogDebug(bool isDebug)
        {
            ATManager.setLogDebug(isDebug);
        }

        public int getGDPRLevel()
        {
            return ATManager.GetDataConsent();
        }

        public bool isEUTraffic()
        {
            return ATManager.isEUTraffic();
        }

        public void deniedUploadDeviceInfo(string deniedInfo)
        {
            ATManager.deniedUploadDeviceInfo(deniedInfo);
        }
	}
}
