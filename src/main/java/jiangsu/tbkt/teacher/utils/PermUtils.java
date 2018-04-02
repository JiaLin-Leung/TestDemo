package jiangsu.tbkt.teacher.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by LiuShiQi on 2018/3/30.
 */

public class PermUtils {
    // 判断是否缺少权限
    public static boolean checkInternetPermission(Context context) {

        PackageManager pm = context.getPackageManager();
        boolean permission= PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.INTERNET", "com.tbkt.teacher");
        Log.e("syw","检查网络权限："+permission);
        return permission;
    }

    public static boolean checkMicPermission(Context context) {

        PackageManager pm = context.getPackageManager();

        boolean permission= PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.RECORD_AUDIO", "com.tbkt.teacher");
        Log.e("syw","检查网络权限："+permission);
        return permission;

    }

    public static boolean checkCameraPermission(Context context) {
        PackageManager pm = context.getPackageManager();
        boolean permission= PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.CAMERA", "com.tbkt.teacher");
        Log.e("syw","检查网络权限："+permission);
        return permission;
    }
}
