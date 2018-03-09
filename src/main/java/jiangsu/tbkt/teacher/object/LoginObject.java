package jiangsu.tbkt.teacher.object;


import jiangsu.tbkt.teacher.bean.LoginResultBean;
import jiangsu.tbkt.teacher.bean.ReSetPass;
import jiangsu.tbkt.teacher.bean.ResultBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Description: 解析登录数据
 * @FileName: LoginObject.java
 * @Package: com.tbkt.student.object
 * @Author: zhangzl
 * @Date: 2015/4/10
 * @Version V1.0
 */
public class LoginObject {

    /**
     * 解析登录数据
     * @return LoginResultBean
     * @throws JSONException
     */
    public static LoginResultBean getLoginResultBean(ResultBean resultBean) throws JSONException {
        LoginResultBean bean = new LoginResultBean();
        bean.setResultBean(resultBean);

        if (resultBean.getResponse().equalsIgnoreCase("ok")) {
            String data = resultBean.getData();
            if ("".equals(data)){
                return bean;
            }
            JSONObject obj = new JSONObject(data);
//            syw sessionid通过请求头添加
//            String sessionid = obj.has("sessionid") ? obj.getString("sessionid") : "";
//            bean.setSessionid(sessionid);
//            PreferencesManager.getInstance().putString("sessionid", sessionid);

//            syw 下边未用到
//            bean.setUnit_class_name(obj.has("unit_class_name") ? obj.getString("unit_class_name") : "");
//            bean.setSchool_name(obj.has("school_name") ? obj.getString("school_name") : "");
//            bean.setGrade_id(obj.has("grade_id") ? obj.getString("grade_id") : "");
//            bean.setSchool_type(obj.has("school_type") ? obj.getString("school_type") : "");
//            bean.setUnit_class_id(obj.has("unit_class_id") ? obj.getString("unit_class_id") : "0");
//            bean.setSchool_id(obj.has("school_id") ? obj.getString("school_id") : "0");
            bean.setUser_id(obj.has("user_id") ? obj.getInt("user_id") : 0);

        }

        return bean;
    }


    /**
     * 获取重找密码数据
     * @return ReSetPass
     * @throws JSONException
     */
    public static ReSetPass getRepassData(ResultBean resultBean) throws JSONException{
        ReSetPass reSetPass=new ReSetPass();
        reSetPass.setResultBean(resultBean);

        String data = resultBean.getData();
        JSONObject obj = new JSONObject(data);
        reSetPass.setNewpassword(obj.has("newpassword") ? obj.getString("newpassword") : "");
        return reSetPass;
    }
}
