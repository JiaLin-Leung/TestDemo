package jiangsu.tbkt.teacher.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * @Description: 判断网络状态
 * @FileName: NetworkStatueUtil.java
 * @Package: com.tbkt.teacher_eng.utils
 * @Author: zhangzl
 * @Date: 2015/4/27
 * @Version V1.0
 */
public class NetworkStatueUtil {
    /**
     * 是否联网
     * @param context
     * @return
     */
    public static boolean isConnectInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = connectivity.getActiveNetworkInfo();
        if (info != null) {
            return info.isAvailable();
        }

//        WifiManager mWifiManager = (WifiManager) context
//                .getSystemService(Context.WIFI_SERVICE);
//        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
//        if (wifiInfo != null) {
//            int ipAddress = wifiInfo.getIpAddress();
//            if (mWifiManager.isWifiEnabled() && ipAddress != 0) {
//                return true;
//            }
//        }
        return false;
    }
    /**
     * 当前网络是否是WIFI
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
        WifiManager mWifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        if (wifiInfo != null) {
            int ipAddress = wifiInfo.getIpAddress();
            if (mWifiManager.isWifiEnabled() && ipAddress != 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMobile(Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = connectivity.getActiveNetworkInfo();
        if (info.getType() == ConnectivityManager.TYPE_MOBILE){
            return true;
        }
        return false;
    }
}
