package jiangsu.tbkt.teacher.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 *
 * @Description: 参数存储类
 * @FileName: StudentSharedPreferences.java
 * @Package com.tbkt.student.util
 * @Author wangxiao
 * @Date 2015-1-5
 * @Version V1.0
 */
public class StudentSharedPreferences {
    private Context context;

    public StudentSharedPreferences(Context context) {
        this.context = context;
    }


    /**
     *  查询退出程序状态
     */
    public String queryIsExistDValue(String propertyName){
        SharedPreferences sharedPreferences = null;
        String propertyValue = "";
        try {
            sharedPreferences = context.getSharedPreferences("isExist", Context.MODE_PRIVATE);
            propertyValue = sharedPreferences.getString(propertyName, "0");
        } catch (Exception e) {
            e.printStackTrace();
            //Log.e(TAG, e.getLocalizedMessage(), e);
        }finally{
            if (sharedPreferences != null) {
                sharedPreferences = null;
            }
        }

        return propertyValue;
    }




    /**
     *保存退出程序状态
     */
    public boolean saveIsExistDValue(String propertyName,String propertyValue){
        SharedPreferences sharedPreferences = null;
        Editor editor = null;
        boolean isSuccess = false;
        try {
            sharedPreferences = context.getSharedPreferences("isExist", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit(); //获取编辑器
            editor.putString(propertyName, propertyValue);

        } catch (Exception e) {
            e.printStackTrace();
            //Log.e(TAG, e.getLocalizedMessage(), e);
        }finally{
            if (editor != null) {
                isSuccess = editor.commit();
                editor = null;
            }
            if (sharedPreferences != null) {
                sharedPreferences = null;
            }
        }
        return isSuccess;

    }




    /**
     *保存当前账户
     */
    public boolean saveAccountValue(String propertyName,String propertyValue){
        SharedPreferences sharedPreferences = null;
        Editor editor = null;
        boolean isSuccess = false;
        try {
            sharedPreferences = context.getSharedPreferences("account", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit(); //获取编辑器
            editor.putString(propertyName, propertyValue);

        } catch (Exception e) {
            e.printStackTrace();
            //Log.e(TAG, e.getLocalizedMessage(), e);
        }finally{
            if (editor != null) {
                isSuccess = editor.commit();
                editor = null;
            }
            if (sharedPreferences != null) {
                sharedPreferences = null;
            }
        }
        return isSuccess;

    }


    /**
     *查询当前账户
     */
    public String queryAccountValue(String propertyName){
        SharedPreferences sharedPreferences = null;
        String accountValue = "";
        try {
            sharedPreferences = context.getSharedPreferences("account", Context.MODE_PRIVATE);
            accountValue = sharedPreferences.getString(propertyName, "0");
        } catch (Exception e) {
            e.printStackTrace();
           // Log.e(TAG, e.getLocalizedMessage(), e);
        }finally{
            if (sharedPreferences != null) {
                sharedPreferences = null;
            }
        }

        return accountValue;

    }


    /**
     * 清空状态
     */
    public boolean deleteAccountValue(String propertyName){
        SharedPreferences sharedPreferences = null;
        Editor editor = null;
        boolean isSuccess = false;
        try {
            sharedPreferences = context.getSharedPreferences("account", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit(); //获取编辑器
            editor.clear();
        } catch (Exception e) {
            e.printStackTrace();
           // Log.e(TAG, e.getLocalizedMessage(), e);
        }finally{
            if (editor != null) {
                isSuccess = editor.commit();
                editor = null;
            }
            if (sharedPreferences != null) {
                sharedPreferences = null;
            }
        }
        return isSuccess;
    }


}
