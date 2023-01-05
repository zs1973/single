package com.planet.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

@SuppressWarnings("unused")
public class NetWorkUtils {
    /**
     * 网络类型 - 无连接
     */
    public static final int NETWORK_TYPE_NO_CONNECTION = -1231545315;

    public static final String NETWORK_TYPE_WIFI = "wifi";
    public static final String NETWORK_TYPE_5G = "5g";
    public static final String NETWORK_TYPE_4G = "4g";
    public static final String NETWORK_TYPE_3G = "3g";
    public static final String NETWORK_TYPE_2G = "2g";
    public static final String NETWORK_TYPE_WAP = "wap";
    public static final String NETWORK_TYPE_UNKNOWN = "unknown";
    public static final String NETWORK_TYPE_DISCONNECT = "disconnect";


    /**
     * Get network type
     *
     * @param context context
     * @return 网络状态
     */
    public static int getNetworkType(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager == null
                ? null
                : connectivityManager.getActiveNetworkInfo();
        return networkInfo == null ? -1 : networkInfo.getType();
    }


    /**
     * 获取网络类型名称
     */
    public static String getNetworkTypeName() {
        ConnectivityManager manager
                = (ConnectivityManager) AppUtils.getApp().getApplicationContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;
        String type = NETWORK_TYPE_DISCONNECT;
        if (manager == null ||
                (networkInfo = manager.getActiveNetworkInfo()) == null) {
            return type;
        }

        if (networkInfo.isConnected()) {
            String typeName = networkInfo.getTypeName();
            if ("WIFI".equalsIgnoreCase(typeName)) {
                type = NETWORK_TYPE_WIFI;
            } else if ("MOBILE".equalsIgnoreCase(typeName)) {
                String proxyHost = android.net.Proxy.getDefaultHost();
                type = TextUtils.isEmpty(proxyHost) ? (isFastMobileNetwork() ? NETWORK_TYPE_3G : NETWORK_TYPE_2G) : NETWORK_TYPE_WAP;
            } else {
                type = NETWORK_TYPE_UNKNOWN;
            }
        }
        return type;
    }


    /**
     * Whether is fast mobile network
     *
     * @return FastMobileNetwork
     */
    @SuppressLint("MissingPermission")
    private static boolean isFastMobileNetwork() {
        TelephonyManager telephonyManager
                = (TelephonyManager) AppUtils.getApp().getApplicationContext().getSystemService(
                Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return false;
        }

        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true;
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
            default:
                return false;
        }
    }


    /**
     * 获取当前网络的状态
     *
     * @return 当前网络的状态。
     * 具体类型可参照NetworkInfo.State.CONNECTED、NetworkInfo.State.CONNECTED.DISCONNECTED等字段。
     * 当前没有网络连接时返回null
     */
    public static NetworkInfo.State getCurrentNetworkState() {
        NetworkInfo networkInfo
                = ((ConnectivityManager) AppUtils.getApp().getApplicationContext().getSystemService(
                Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return networkInfo != null ? networkInfo.getState() : null;
    }


    /**
     * 获取当前网络的类型
     *
     * @return 当前网络的类型。
     * 具体类型可参照ConnectivityManager中的TYPE_BLUETOOTH、TYPE_MOBILE、TYPE_WIFI等字段。
     * 当前没有网络连接时返回NetworkUtils.NETWORK_TYPE_NO_CONNECTION
     */
    public static int getCurrentNetworkType() {
        NetworkInfo networkInfo
                = ((ConnectivityManager) AppUtils.getApp().getApplicationContext().getSystemService(
                Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return networkInfo != null ? networkInfo.getType() : NETWORK_TYPE_NO_CONNECTION;
    }


    /**
     * 获取当前网络的具体类型
     *
     * @return 当前网络的具体类型。
     * 具体类型可参照TelephonyManager中的NETWORK_TYPE_1xRTT、
     * NETWORK_TYPE_CDMA等字段。当前没有网络连接时返回NetworkUtils.NETWORK_TYPE_NO_CONNECTION
     */
    public static int getCurrentNetworkSubtype() {
        NetworkInfo networkInfo
                = ((ConnectivityManager) AppUtils.getApp().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return networkInfo != null
                ? networkInfo.getSubtype()
                : NETWORK_TYPE_NO_CONNECTION;
    }


    /**
     * 判断当前网络是否已经连接
     *
     * @return 当前网络是否已经连接。false：尚未连接
     */
    public static boolean isConnected() {
        return getCurrentNetworkState() == NetworkInfo.State.CONNECTED;
    }


    /**
     * 判断当前网络是否正在连接
     *
     * @return 当前网络是否正在连接
     */
    public static boolean isConnecting() {
        return getCurrentNetworkState() == NetworkInfo.State.CONNECTING;
    }

    /**
     * 判断当前网络是否正在断开
     *
     * @return 当前网络是否正在断开
     */
    public static boolean isDisconnecting() {
        return getCurrentNetworkState() == NetworkInfo.State.DISCONNECTING;
    }


    /**
     * 判断当前网络是否已经断开
     *
     * @return 当前网络是否正在断开
     */
    public static boolean isDisconnected() {
        return getCurrentNetworkState() == NetworkInfo.State.DISCONNECTED;
    }

    /**
     * 判断当前网络是否已经暂停
     *
     * @return 当前网络是否已经暂停
     */
    public static boolean isSuspendedByState() {
        return getCurrentNetworkState() == NetworkInfo.State.SUSPENDED;
    }


    /**
     * 判断当前网络是否处于未知状态中
     *
     * @return 当前网络是否处于未知状态中
     */
    public static boolean isUnknownByState() {
        return getCurrentNetworkState() == NetworkInfo.State.UNKNOWN;
    }


    /**
     * 判断当前网络的类型是否是移动网络
     *
     * @return 当前网络的类型是否是移动网络。false：当前没有网络连接或者网络类型不是移动网络
     */
    public static boolean isMobile() {
        return getCurrentNetworkType() == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * 判断当前网络的类型是否是Wifi
     *
     * @return 当前网络的类型是否是Wifi。false：当前没有网络连接或者网络类型不是wifi
     */
    public static boolean isWifi() {
        return getCurrentNetworkType() == ConnectivityManager.TYPE_WIFI;
    }


    /**
     * 判断当前网络的类型是否是WiMax
     *
     * @return 当前网络的类型是否是WiMax。false：当前没有网络连接或者网络类型不是WiMax
     */
    public static boolean isWiMaxByType() {
        return getCurrentNetworkType() == ConnectivityManager.TYPE_WIMAX;
    }


    /**
     * 判断当前网络的具体类型是否是1XRTT
     *
     * @return false：当前网络的具体类型是否是1XRTT。false：当前没有网络连接或者具体类型不是1XRTT
     */
    public static boolean is1XRTTBySubtype() {
        return getCurrentNetworkSubtype() ==
                TelephonyManager.NETWORK_TYPE_1xRTT;
    }


    /**
     * 判断当前网络的具体类型是否是CDMA（Either IS95A or IS95B）
     *
     * @return false：当前网络的具体类型是否是CDMA。false：当前没有网络连接或者具体类型不是CDMA
     */
    public static boolean isCDMABySubtype() {
        return getCurrentNetworkSubtype() ==
                TelephonyManager.NETWORK_TYPE_CDMA;
    }


    /**
     * 判断当前网络的具体类型是否是EDGE
     *
     * @return false：当前网络的具体类型是否是EDGE。false：当前没有网络连接或者具体类型不是EDGE
     */
    public static boolean isEDGEBySubtype() {
        return getCurrentNetworkSubtype() ==
                TelephonyManager.NETWORK_TYPE_EDGE;
    }


    /**
     * 判断当前网络的具体类型是否是EHRPD
     *
     * @return false：当前网络的具体类型是否是EHRPD。false：当前没有网络连接或者具体类型不是EHRPD
     */
    public static boolean isEHRPDBySubtype() {
        return getCurrentNetworkSubtype() ==
                TelephonyManager.NETWORK_TYPE_EHRPD;
    }


    /**
     * 判断当前网络的具体类型是否是EVDO_0
     *
     * @return false：当前网络的具体类型是否是EVDO_0。false：当前没有网络连接或者具体类型不是EVDO_0
     */
    public static boolean isEVDO_0BySubtype() {
        return getCurrentNetworkSubtype() ==
                TelephonyManager.NETWORK_TYPE_EVDO_0;
    }


    /**
     * 判断当前网络的具体类型是否是EVDO_A
     *
     * @return false：当前网络的具体类型是否是EVDO_A。false：当前没有网络连接或者具体类型不是EVDO_A
     */
    public static boolean isEVDO_ABySubtype() {
        return getCurrentNetworkSubtype() ==
                TelephonyManager.NETWORK_TYPE_EVDO_A;
    }


    /**
     * 判断当前网络的具体类型是否是EDGE
     *
     * @return false：当前网络的具体类型是否是EVDO_B。false：当前没有网络连接或者具体类型不是EVDO_B
     */
    public static boolean isEVDO_BBySubtype() {
        return getCurrentNetworkSubtype() ==
                TelephonyManager.NETWORK_TYPE_EVDO_B;
    }


    /**
     * 判断当前网络的具体类型是否是GPRS
     * EVDO_Bam context 上下文
     *
     * @return false：当前网络的具体类型是否是GPRS。false：当前没有网络连接或者具体类型不是GPRS
     */
    public static boolean isGPRSBySubtype() {
        return getCurrentNetworkSubtype() ==
                TelephonyManager.NETWORK_TYPE_GPRS;
    }


    /**
     * 判断当前网络的具体类型是否是HSDPA
     *
     * @return false：当前网络的具体类型是否是HSDPA。false：当前没有网络连接或者具体类型不是HSDPA
     */
    public static boolean isHSDPABySubtype() {
        return getCurrentNetworkSubtype() ==
                TelephonyManager.NETWORK_TYPE_HSDPA;
    }


    /**
     * 判断当前网络的具体类型是否是HSPA
     *
     * @return false：当前网络的具体类型是否是HSPA。false：当前没有网络连接或者具体类型不是HSPA
     */
    public static boolean isHSPABySubtype() {
        return getCurrentNetworkSubtype() ==
                TelephonyManager.NETWORK_TYPE_HSPA;
    }


    /**
     * 判断当前网络的具体类型是否是HSPAP
     *
     * @return false：当前网络的具体类型是否是HSPAP。false：当前没有网络连接或者具体类型不是HSPAP
     */
    public static boolean isHSPAPBySubtype() {
        return getCurrentNetworkSubtype() ==
                TelephonyManager.NETWORK_TYPE_HSPAP;
    }


    /**
     * 判断当前网络的具体类型是否是HSUPA
     *
     * @return false：当前网络的具体类型是否是HSUPA。false：当前没有网络连接或者具体类型不是HSUPA
     */
    public static boolean isHSUPABySubtype() {
        return getCurrentNetworkSubtype() ==
                TelephonyManager.NETWORK_TYPE_HSUPA;
    }


    /**
     * 判断当前网络的具体类型是否是IDEN
     *
     * @return false：当前网络的具体类型是否是IDEN。false：当前没有网络连接或者具体类型不是IDEN
     */
    public static boolean isIDENBySubtype() {
        return getCurrentNetworkSubtype() ==
                TelephonyManager.NETWORK_TYPE_IDEN;
    }


    /**
     * 判断当前网络的具体类型是否是LTE
     *
     * @return false：当前网络的具体类型是否是LTE。false：当前没有网络连接或者具体类型不是LTE
     */
    public static boolean is4G() {
        return getCurrentNetworkSubtype() ==
                TelephonyManager.NETWORK_TYPE_LTE;
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static boolean is5G() {
        return getCurrentNetworkSubtype() ==
                TelephonyManager.NETWORK_TYPE_NR;
    }

    /**
     * 判断当前网络的具体类型是否是UMTS
     *
     * @return false：当前网络的具体类型是否是UMTS。false：当前没有网络连接或者具体类型不是UMTS
     */
    public static boolean isUMTSBySubtype() {
        return getCurrentNetworkSubtype() ==
                TelephonyManager.NETWORK_TYPE_UMTS;
    }


    /**
     * 判断当前网络的具体类型是否是UNKNOWN
     *
     * @return false：当前网络的具体类型是否是UNKNOWN。false：当前没有网络连接或者具体类型不是UNKNOWN
     */
    public static boolean isUNKNOWNBySubtype() {
        return getCurrentNetworkSubtype() ==
                TelephonyManager.NETWORK_TYPE_UNKNOWN;
    }


    /**
     * 判断当前网络是否是中国移动2G网络
     *
     * @param context 上下文
     * @return false：不是中国移动2G网络或者当前没有网络连接
     */
    public static boolean isChinaMobile2G(Context context) {
        return isEDGEBySubtype();
    }


    /**
     * 判断当前网络是否是中国联通2G网络
     *
     * @param context 上下文
     * @return false：不是中国联通2G网络或者当前没有网络连接
     */
    public static boolean isChinaUnion2G(Context context) {
        return isGPRSBySubtype();
    }


    /**
     * 判断当前网络是否是中国联通3G网络
     *
     * @return false：不是中国联通3G网络或者当前没有网络连接
     */
    public static boolean isChinaUnion3G() {
        return isHSDPABySubtype() || isUMTSBySubtype();
    }


    /**
     * 判断当前网络是否是中国电信2G网络
     *
     * @param context 上下文
     * @return false：不是中国电信2G网络或者当前没有网络连接
     */
    public static boolean isChinaTelecom2G(Context context) {
        return isCDMABySubtype();
    }


    /**
     * 判断当前网络是否是中国电信3G网络
     *
     * @return false：不是中国电信3G网络或者当前没有网络连接
     */
    public static boolean isChinaTelecom3G() {
        return isEVDO_0BySubtype() || isEVDO_ABySubtype() ||
                isEVDO_BBySubtype();
    }


    /**
     * 获取Wifi的状态，需要ACCESS_WIFI_STATE权限
     *
     * @return 取值为WifiManager中的WIFI_STATE_ENABLED、WIFI_STATE_ENABLING、WIFI_STATE_DISABLED、
     * WIFI_STATE_DISABLING、WIFI_STATE_UNKNOWN之一
     * @throws Exception 没有找到wifi设备
     */
    public static int getWifiState() {
        WifiManager wifiManager = ((WifiManager) AppUtils.getApp().getApplicationContext().getSystemService(
                Context.WIFI_SERVICE));
        return wifiManager.getWifiState();

    }


    /**
     * 判断Wifi是否打开，需要ACCESS_WIFI_STATE权限
     *
     * @return true：打开；false：关闭
     */
    public static boolean isWifiOpen() {
        int wifiState = getWifiState();
        return wifiState == WifiManager.WIFI_STATE_ENABLED ||
                wifiState == WifiManager.WIFI_STATE_ENABLING;
    }


    /**
     * 打开WIFI设置，需要CHANGE_WIFI_STATE权限
     *
     * @param enable wifi状态
     * @return 设置是否成功
     */
    public static boolean setWifiStatus(boolean enable) {
        //如果当前wifi的状态和要设置的状态不一样
        if (isWifiOpen() != enable) {
            ((WifiManager) AppUtils.getApp().getApplicationContext().getSystemService(
                    Context.WIFI_SERVICE)).setWifiEnabled(enable);
        }
        return true;
    }


    /**
     * 判断移动网络是否打开，需要ACCESS_NETWORK_STATE权限
     *
     * @return true：打开；false：关闭
     */
    public static boolean isMobileNetworkOpen() {
        return (((ConnectivityManager) AppUtils.getApp().getApplicationContext().getSystemService(
                Context.CONNECTIVITY_SERVICE)).getNetworkInfo(
                ConnectivityManager.TYPE_MOBILE)).isConnected();
    }


    /**
     * 是否开启网络连接
     *
     * @return true开启
     */
    public static boolean isNetworkAvailable() {
        return isMobileNetworkOpen() || isWifiOpen();
    }


    /**
     * 获取本机IP地址
     *
     * @return null：没有网络连接
     */
    public static String getThisDeviceIpAddress() {
        try {
            NetworkInterface networkInterface;
            InetAddress inetAddress;
            for (Enumeration<NetworkInterface> en
                 = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements(); ) {
                networkInterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddress
                     = networkInterface.getInetAddresses();
                     enumIpAddress.hasMoreElements(); ) {
                    inetAddress = enumIpAddress.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
            return null;
        } catch (SocketException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}