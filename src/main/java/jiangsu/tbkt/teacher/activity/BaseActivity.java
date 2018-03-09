package jiangsu.tbkt.teacher.activity;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import jiangsu.tbkt.teacher.api.RequestServer;
import jiangsu.tbkt.teacher.application.AppManager;
import jiangsu.tbkt.teacher.application.MyApplication;
import jiangsu.tbkt.teacher.application.PreferencesManager;
import jiangsu.tbkt.teacher.bean.VersionCheck;
import jiangsu.tbkt.teacher.utils.Constant;
import jiangsu.tbkt.teacher.utils.DialogUtil;
import jiangsu.tbkt.teacher.utils.Tools;

/**
 * 基类activity
 */
public class BaseActivity extends FragmentActivity {
    private static final int UPDATE = 1000;
    public ProgressDialog mProgressDialog;

    public String httpurl = "";
    private SharedPreferences prefs;
    private Long lastUpdateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        AppManager.getAppManager().addActivity(this);
        prefs = getPreferences(0);
        lastUpdateTime = prefs.getLong("lastUpdateTime", System.currentTimeMillis());
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    protected void exitBy2Click() {
        Timer tExit = null;
        if (!isExit) {
            isExit = true; // 准备退出
            //修改时间 2015-06-10 10:08:53 zhangerbin
            Toast.makeText(this, "双击退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;//取消退出
                }
            }, 2000); //如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            AppManager.getAppManager().AppExit(this);
        }
    }

    /**
     * 跳转到其他界面
     *
     * @param cls         跳转页面
     * @param bundle      Bundle参数
     * @param isReturn    是否返回
     * @param requestCode 请求Code
     * @param isFinish    是否销毁当前页面
     */
    public void jumpToPage(Class<?> cls, Bundle bundle, boolean isReturn,
                           int requestCode, boolean isFinish) {
        if (cls == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (isReturn) {
            startActivityForResult(intent, requestCode);
        } else {
            startActivity(intent);
        }
        if (isFinish) {
            finish();
        }
    }

    /**
     * 跳转到其他界面
     *
     * @param cls      跳转页面
     * @param bundle   Bundle参数
     * @param isFinish 是否销毁当前页面
     */
    public void jumpToPage(Class<?> cls, Bundle bundle, boolean isFinish) {
        jumpToPage(cls, bundle, false, 0, isFinish);
    }

    /**
     * 跳转到其他界面，不销毁当前页面，也不传参数
     *
     * @param cls 跳转页面
     */
    public void jumpToPage(Class<?> cls) {
        jumpToPage(cls, null, false, 0, false);
    }


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE:
                    versionCheck();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("Start");
        MobclickAgent.onResume(this);

        if(!MyApplication.getInstance().getActive()){
            if ((lastUpdateTime + (3 * 60 * 60 * 1000)) < System.currentTimeMillis()){
                //如果上次更新的时间加上3小时之后,小于现在的时间,那么就更新版本
                handler.sendEmptyMessageDelayed(UPDATE,1500);
            }else{
//                Toast.makeText(BaseActivity.this,"时间未到呢!",Toast.LENGTH_SHORT).show();
            }
            MyApplication.getInstance().setActive(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("End");
        MobclickAgent.onPause(this);
    }

    //    @Override
//    protected void onStart() {
//        super.onStart();
//        if ((lastUpdateTime + (3 * 60 * 60 * 1000)) < System.currentTimeMillis()) {
//            //如果上次更新的时间加上3小时之后,小于现在的时间,那么就更新版本
//            handler.sendEmptyMessageDelayed(UPDATE, 1500);
//        }
//        MyApplication.getInstance().setActive(true);
//    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isAppOnForeground()) {
            MyApplication.getInstance().setActive(false);
        }
    }

    /**
     * 程序是否在前台运行
     *
     * @return 返回是不是在前台的Boolean值
     */
    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    private String last_versino;
    private String new_version;

    /**
     * 版本检测
     */
    public void versionCheck() {
        String url = Constant.newVersonInterf;
        JSONObject params = new JSONObject();
        try {
            params.put("type", 10);
            params.put("api", Tools.getAppVersionCode(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestServer.getVersionCheck(BaseActivity.this, url, params.toString(), new RequestServer.Callback() {
            @Override
            public void onSuccess(Object object) {
                final VersionCheck versionCheck = (VersionCheck) object;
                last_versino = versionCheck.getLast_version();
                new_version = PreferencesManager.getInstance().getString("last_versino", "1.0.0");
                //新版本检测
                if (versionCheck.getUpdate().equals("2")) {//0没有   1有  2强制升级
                    new DialogUtil() {
                        @Override
                        public void positiveContent() {
                            openDialog(versionCheck.getDownload());
                        }

                        @Override
                        public void middleContent() {
                        }

                        @Override
                        public void negativeContent() {
                        }
                    }.singleDialog(BaseActivity.this,
                            "更新啦",
                            versionCheck.getContent(), "立即更新");

                } else if (versionCheck.getUpdate().equals("1")) {
                    new DialogUtil() {
                        @Override
                        public void positiveContent() {
                            openDialog(versionCheck.getDownload());
                        }

                        @Override
                        public void middleContent() {
                        }

                        @Override
                        public void negativeContent() {
                            PreferencesManager.getInstance().putString("last_day", Tools.getNowTimeWithFormat("yyyy-MM-dd"));
                            prefs.edit().putLong("lastUpdateTime", System.currentTimeMillis()).commit();
                        }
                    }.doubleDialog(BaseActivity.this,
                            "更新啦",
                            versionCheck.getContent(), "立即更新", "稍后");
                } else {
                }
            }

            @Override
            public void onFail(Object object) {
            }
        }, true, false, true);
    }

    /**
     * 在对话框中下载新的APK
     */
    private void openDialog(final String url) {

        final ProgressDialog pd;    //进度条对话框
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.setCancelable(false);
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = getFileFromServer(url, pd);
                    sleep(3000);//3秒之后执行自动安装
                    installApk(file);
                    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                }
            }
        }.start();
    }

    public static File getFileFromServer(String path, ProgressDialog pd) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                //获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

    //安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
