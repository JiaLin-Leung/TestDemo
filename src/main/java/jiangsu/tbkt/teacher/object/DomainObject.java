package jiangsu.tbkt.teacher.object;

import com.google.gson.Gson;

import jiangsu.tbkt.teacher.bean.NotifyClassBean;
import jiangsu.tbkt.teacher.bean.ResultBean;
import jiangsu.tbkt.teacher.bean.SchoolBean;
import jiangsu.tbkt.teacher.bean.SelectClassBean;
import jiangsu.tbkt.teacher.bean.SettingManageBean;
import jiangsu.tbkt.teacher.bean.ShenFenBean;
import jiangsu.tbkt.teacher.bean.SubjectBean;
import jiangsu.tbkt.teacher.bean.SwitchShenFenBean;
import jiangsu.tbkt.teacher.bean.TemplateBean;
import jiangsu.tbkt.teacher.bean.UrlBean;
import jiangsu.tbkt.teacher.bean.User;
import jiangsu.tbkt.teacher.picker.CityBean;

/**
 * 数据解析
 */
public class DomainObject {

    public static SettingManageBean getSetData(ResultBean resultBean) {
        String data = resultBean.getData();
        Gson gson = new Gson();
        SettingManageBean settingManageBean = gson.fromJson(data, SettingManageBean.class);
        return settingManageBean;
    }

    public static UrlBean getUrlData(ResultBean resultBean) {
        String data = resultBean.getData();
        Gson gson = new Gson();
        UrlBean settingManageBean = gson.fromJson(data, UrlBean.class);
        return settingManageBean;
    }

    public static NotifyClassBean getNotifyData(ResultBean resultBean) {
        String data = resultBean.getData();
        Gson gson = new Gson();
        NotifyClassBean settingManageBean = gson.fromJson(data, NotifyClassBean.class);
        return settingManageBean;
    }

    public static User getStudentsData(ResultBean resultBean) {
        String data = resultBean.getData();
        Gson gson = new Gson();
        User settingManageBean = gson.fromJson(data, User.class);
        return settingManageBean;
    }

    public static CityBean getAreaData(ResultBean resultBean) {
        String data = resultBean.getData();
        CityBean settingManageBean=new CityBean();
        if ("".equals(data)){
            return settingManageBean;
        }
        data = "{\"data\":" + data + "}";
        Gson gson = new Gson();
        settingManageBean = gson.fromJson(data, CityBean.class);
        return settingManageBean;
    }

    public static SchoolBean getSchoolListData(ResultBean resultBean) {
        String data = resultBean.getData();
        SchoolBean settingManageBean = new SchoolBean();
        if ("{}".equals(data)) {
            return settingManageBean;
        }
        data = "{\"data\":" + data + "}";
        Gson gson = new Gson();
        settingManageBean = gson.fromJson(data, SchoolBean.class);
        return settingManageBean;
    }

    public static SelectClassBean getClassData(ResultBean resultBean) {
        String data = resultBean.getData();
        SelectClassBean settingManageBean = new SelectClassBean();
        if ("".equals(data)) {
            return settingManageBean;
        }
        data = "{\"data\":" + data + "}";
        Gson gson = new Gson();
        settingManageBean = gson.fromJson(data, SelectClassBean.class);
        return settingManageBean;
    }

    /**
     * 解析身份方法
     * @param resultBean
     * @return
     */
    public static ShenFenBean getShenFen(ResultBean resultBean) {
        String data = resultBean.getData();
        ShenFenBean settingManageBean = new ShenFenBean();
        if ("".equals(data)) {
            return settingManageBean;
        }
        data = "{\"data\":" + data + "}";
        Gson gson = new Gson();
        settingManageBean = gson.fromJson(data, ShenFenBean.class);
        return settingManageBean;
    }

    /**
     * 解析身份方法
     * @param resultBean
     * @return
     */
    public static TemplateBean getTemplate(ResultBean resultBean) {
        String data = resultBean.getData();
        TemplateBean settingManageBean = new TemplateBean();
        if ("".equals(data)) {
            return settingManageBean;
        }
        data = "{\"data\":" + data + "}";
        Gson gson = new Gson();
        settingManageBean = gson.fromJson(data, TemplateBean.class);
        return settingManageBean;
    }

    /**
     * 解析切换身份方法
     * @param resultBean
     * @return
     */
    public static SwitchShenFenBean getSwitchShenFen(ResultBean resultBean) {
        String data = resultBean.getData();
        SwitchShenFenBean settingManageBean = new SwitchShenFenBean();
        if ("".equals(data)) {
            return settingManageBean;
        }
        data = "{\"data\":" + data + "}";
        Gson gson = new Gson();
        settingManageBean = gson.fromJson(data, SwitchShenFenBean.class);
        return settingManageBean;
    }

    /**
     * 解析学科列表
     * @param resultBean
     * @return
     */
    public static SubjectBean getSubjectData(ResultBean resultBean) {
        String data = resultBean.getData();
        SubjectBean settingManageBean = new SubjectBean();
        if ("".equals(data)) {
            return settingManageBean;
        }
        data = "{\"data\":" + data + "}";
        Gson gson = new Gson();
        settingManageBean = gson.fromJson(data, SubjectBean.class);
        return settingManageBean;
    }

}
