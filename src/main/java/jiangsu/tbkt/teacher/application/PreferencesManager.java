package jiangsu.tbkt.teacher.application;

import android.content.Context;

/**
 * @Description:
 * @FileName: PreferencesManager.java
 * @Package: com.tbkt.student.application
 * @Author: zhangzl
 * @Date: 2015/4/13
 * @Version V1.0
 */
public class PreferencesManager {
    private static PreferencesManager instance_ = new PreferencesManager(MyApplication.getInstance());
    private Context context_;

    private static final String PREFERENCE_NAME = "tbkt_tea_preference";

    public PreferencesManager(Context context) {
        this.context_ = context;
    }

    public static PreferencesManager getInstance(){

        return instance_;
    }

    public boolean putInt(String key, int value){
        boolean flag = context_.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit().putInt(key, value).commit();
        return flag;
    }

    public int getInt(String key, int defValue){
        return context_.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getInt(key, defValue);
    }

    public boolean putLong(String key, long value){
        boolean flag = context_.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit().putLong(key, value).commit();
        return flag;
    }

    public long getLong(String key, long defValue){
        return context_.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getLong(key, defValue);
    }

    public boolean putString(String key, String value){
        return context_.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit().putString(key, value).commit();
    }

    public String getString(String key, String defValue){
        return context_.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getString(key, defValue);
    }

    public boolean putBoolean(String key, boolean value){
        return context_.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit().putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key, boolean defValue){
        return context_.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getBoolean(key, defValue);
    }

    public boolean clearAllValue(){
        return context_.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit().clear().commit();
    }
}
