package jiangsu.tbkt.teacher.bean;

import java.io.Serializable;

/**
 * @Description: 登录信息返回
 * @FileName: LoginResultBean.java
 * @Package: com.tbkt.student.javabean.login
 * @Author: zhangzl
 * @Date: 2015/4/10
 * @Version V1.0
 */

public class LoginResultBean implements Serializable {
    private String sessionid = "";
    private int user_id = 0;
    private String school_name =""; // 学校名称
    private String school_id = "0"; // 未加入学校返回0
    private String unit_class_id = "0"; // 未加入班级返回0
    private String unit_class_name = "0"; // 班级名称
    private String grade_id = "0"; // 年级ID
    private String school_type = "0";

    private ResultBean resultBean = new ResultBean();

    public ResultBean getResultBean() {
        return resultBean;
    }

    public void setResultBean(ResultBean resultBean) {
        this.resultBean = resultBean;
    }

    public void setSchool_type(String school_type){
        this.school_type = school_type;
    }
    public String getSchool_type(){
        return school_type;
    }


    @Override
    public String toString() {
        return "LoginResultBean{" +
                "sessionid='" + sessionid + '\'' +
                ", user_id='" + user_id + '\'' +
                ", school_name='" + school_name + '\'' +
                ", school_id='" + school_id + '\'' +
                ", unit_class_id='" + unit_class_id + '\'' +
                ", unit_class_name='" + unit_class_name + '\'' +
                ", grade_id='" + grade_id + '\'' +
                ", school_type='" + school_type + '\'' +
                ", resultBean=" + resultBean +
                '}';
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getUnit_class_id() {
        return unit_class_id;
    }

    public void setUnit_class_id(String unit_class_id) {
        this.unit_class_id = unit_class_id;
    }

    public String getUnit_class_name() {
        return unit_class_name;
    }

    public void setUnit_class_name(String unit_class_name) {
        this.unit_class_name = unit_class_name;
    }

    public String getGrade_id() {
        return grade_id;
    }

    public void setGrade_id(String grade_id) {
        this.grade_id = grade_id;
    }
}
