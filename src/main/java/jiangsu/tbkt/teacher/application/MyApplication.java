package jiangsu.tbkt.teacher.application;

import android.app.Application;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;

import com.umeng.socialize.PlatformConfig;

import jiangsu.tbkt.teacher.utils.FileUtils;

/**
 * @Description: 应用程序
 * @FileName: MyApplication.java
 * @Package: com.tbkt.teacher_eng.application
 * @Author: zhangzl
 * @Date: 2015/4/27
 * @Version V1.0
 */
public class MyApplication extends Application {
    /**
     * 应用程序instance
     */
    private static MyApplication mInstance = null;

    /**
     * 应用程序缓存根目录
     */
    public static String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/tbkt/";

    /**
     * 缓存HTML数据
     */
    public static String CACHE_HTML;

    public static MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        //注册App异常崩溃处理器
//        Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());
        initFileInfo();
        //捕获程序异常
//        catchException();
    }

    {
        PlatformConfig.setWeixin("wx7798a8953f1d1f75","6a553d7043e05a2f545be9e70f4b5f9a");
        PlatformConfig.setQQZone("1106250470","w9vrIFlOyEsMMpMD");
    }


    /**
     * 捕获程序异常
     */
    private void catchException() {
        CrashHandlerException crashHandler = CrashHandlerException.getInstance();
        crashHandler.init(getApplicationContext());
    }

    //初始化文件目录
    private void initFileInfo() {
        if (!FileUtils.isSDExist()) {
            ROOT_PATH = getVirtualSdcardPath() + "/tbkt/";
        }

        CACHE_HTML = ROOT_PATH + "cache_html/";

        //创建目录
        FileUtils.createDir(ROOT_PATH);
        //HTML缓存目录
        FileUtils.createDir(CACHE_HTML);
    }

    /**
     * 获取虚拟Sdcard路径
     * @return
     */
    public String getVirtualSdcardPath() {
        String _path = "";
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                _path = cursor.getString(cursor.getColumnIndex("_data"));
            }
            cursor.close();
        }
        if (!_path.equals("")) {
            String _rootPath = _path.substring(_path.indexOf("/", 1) + 1);
            String _childPath = _rootPath.substring(0, _rootPath.indexOf("/"));
            _path = _path.substring(0, _path.indexOf("/", 1) + 1) + _childPath;
        } else {
            _path = "/mnt/sdcard2";
        }
        return _path;
    }

    public Boolean isActive = false;
    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
