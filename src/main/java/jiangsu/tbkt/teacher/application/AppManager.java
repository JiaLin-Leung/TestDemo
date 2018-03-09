package jiangsu.tbkt.teacher.application;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @Description: 应用程序Activity管理类：用于Activity管理和应用程序退出
 * @FileName: AppManager.java
 * @Package: com.tbkt.student.application
 * @Author: zhangzl
 * @Date: 2015/3/28
 * @Version V1.0
 */
public class AppManager {
    private static Stack<Activity> activityStack;
    private static AppManager mInstance;

    private AppManager(){}
    /**
     * 获取单一实例
     */
    public static AppManager getAppManager() {
        if (mInstance == null) {
            mInstance = new AppManager();
        }
        if (activityStack == null){
            activityStack = new Stack<Activity>();
        }
        return mInstance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }


    /**
     * 关闭指定类的activity
     * @param cls 需要关闭的activity类
     */
    public void finishSpecailActivity(Class cls) {
        if (activityStack == null || activityStack.size() <= 0) {
            return;
        }
        List<Activity> list = new ArrayList<>();
        for (Activity ac : activityStack) {
            if (ac.getClass().getName().equals(cls.getName())) {
                list.add(ac);
            }
        }
        for (Activity ac : list) {
            if (activityStack.contains(ac)) {
                activityStack.remove(ac);
            }
        }
        for (Activity ac : list) {
            ac.finish();
        }
    }

    /**
     * 关闭所有继承与BaseActivity的activity
     */
    public void finishAllActivity() {
        if (activityStack == null || activityStack.size() <= 0) {
            return;
        }
        for (int i = 0; i < activityStack.size(); i++){
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭Activity列表中的所有Activity*/
    public void AppExit(){
        try {
            finishAllActivity();
            ArrayList<Activity> list = new ArrayList<Activity>();
            for (Activity activity : list) {
                if (null != activity) {
                    activity.finish();
                }
            }
            //杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }catch (Exception e){

        }
    }
}