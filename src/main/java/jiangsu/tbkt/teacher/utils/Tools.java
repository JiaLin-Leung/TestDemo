package jiangsu.tbkt.teacher.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @Description:
 * @FileName: Tools.java
 * @Package: com.tbkt.student.util
 * @Author: zhangzl
 * @Date: 2015/4/16
 * @Version V1.0
 */
public class Tools {

    /**
     * ms -- mm : ss
     */
    public static String formatMs(int ms){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        return simpleDateFormat.format(new Date(ms));
    }
    /**
     * 格式化当前时间
     * @return
     */
    public static String formatMsToHour(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");//HH:24,hh:12
        return simpleDateFormat.format(new Date());
    }
    /**
     * 格式化日期
     *
     * @param format 日期格式(yyyy-MM-dd)
     * @return
     */
    public static String getNowTimeWithFormat(String format) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(currentTime);
    }

    /**
     * 获取下载app地址URL
     *
     * @param context
     * @return
     */
    public static String getDownloadUrl(Context context) {
        String url = "";//URLs.httpStr + URLs.downloadAppInterf;
        JSONObject params = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            params.put("version", getAndroidVersion());
            params.put("name", getAndroidName(context));
            params.put("model", getDeviceISN(context));
            params.put("platform", "Android");
            params.put("uuid", getDeviceType(context));
            params.put("appversion", getAppVersion(context));

            jsonObject.put("data", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return url + "&" + jsonObject.toString();
    }

    /* 获取手机系统版本号 */
    public static String getAndroidVersion() {
        String version_release = Build.VERSION.RELEASE;
        return version_release;
    }

    /* 获取手机设备名称 */
    public static String getAndroidName(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return androidId;
    }

    /* 获取设备序列号 */
    public static String getDeviceISN(Context context) {

        String device_model = Build.MODEL;
        return device_model;
    }

    /* 获取设备型号UUID */
    public static String getDeviceType(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        return deviceId;
    }

    /* 获取app当前版本号 */
    public static String getAppVersion(Context context) {
        String version = "";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            version = info.versionName;


        } catch (Exception e) {
            e.printStackTrace();

        }
        return version;
    }

    /* 获取app当前版本号 */
    public static int getAppVersionCode(Context context) {
        int version = 1;
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            version = info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return version;
    }


    /**
     * 设置Selector。
     */
    public static StateListDrawable newSelector(Context context, int idNormal,
                                                int idPressed, int idFocused, int idUnable) {
        StateListDrawable bg = new StateListDrawable();
        Drawable normal = idNormal == -1 ? null : context.getResources()
                .getDrawable(idNormal);
        Drawable pressed = idPressed == -1 ? null : context.getResources()
                .getDrawable(idPressed);
        Drawable focused = idFocused == -1 ? null : context.getResources()
                .getDrawable(idFocused);
        Drawable unable = idUnable == -1 ? null : context.getResources()
                .getDrawable(idUnable);
        // View.PRESSED_ENABLED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_pressed,
                android.R.attr.state_enabled}, pressed);
        // View.ENABLED_FOCUSED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_enabled,
                android.R.attr.state_focused}, focused);
        // View.ENABLED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_enabled}, normal);
        // View.FOCUSED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_focused}, focused);
        // View.WINDOW_FOCUSED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_window_focused}, unable);
        // View.EMPTY_STATE_SET
        bg.addState(new int[]{}, normal);
        return bg;
    }


    /**
     * 2015-06-15 11:54:49  zhangerbin
     *
     * @param days
     * @return
     */

    public static String getDate(Date sDate, int days) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        Calendar calendar = Calendar.getInstance();
        StringBuffer buffer = new StringBuffer();
        String dayNames[] = {"(星期日)", "(星期一)", "(星期二)", "(星期三)", "(星期四)", "(星期五)", "(星期六)"};
        DateFormat df_M = new SimpleDateFormat("MM");
        DateFormat df_d = new SimpleDateFormat("dd");
        calendar.setTime(sDate);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        Date date = new Date(calendar.getTimeInMillis());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0)
            dayOfWeek = 0;
        return buffer.append(df_M.format(date)).append("月").append(df_d.format(date)).append("日").append(dayNames[dayOfWeek]).toString();
    }


    /**
     * 获取错误信息
     *
     * @param ex
     * @return
     */
    public static String getCrashInfoToString(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        String traceInfo = getTraceInfo(ex);
        sb.append(traceInfo + "\n");
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);

        return sb.toString();
    }

    /**
     * 获取堆栈信息
     *
     * @param ex
     * @return
     */
    public static String getTraceInfo(Throwable ex) {
        StringBuffer sb = new StringBuffer();

        StackTraceElement[] stacks = ex.getStackTrace();
        sb.append("class: ").append(stacks[1].getClassName()).append("; method: ").append(stacks[1].getMethodName()).append("; number: ").append(stacks[1].getLineNumber());

        return sb.toString();
    }

}


