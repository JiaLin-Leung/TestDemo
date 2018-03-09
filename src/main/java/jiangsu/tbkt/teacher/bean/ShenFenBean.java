package jiangsu.tbkt.teacher.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/1/14 0014.
 */
public class ShenFenBean implements Serializable{

    /**
     * bind_id : 4613001
     * name : 录老师
     * portrait : http://file.tbkt.cn/upload_media/portrait/2016/11/08/20161108000127796294.png
     * role : 数学老师
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
//        private int bind_id;
        private int user_id;
        private String unit_name;
        private String role;
        private String name;
        private String portrait;

        private int subject_id;
        private int type;
        private int grade_id;

        public void setSubject_id(int subject_id){this.subject_id=subject_id;}
        public int getSubject_id(){return subject_id;}

        public void setType(int type){this.type=type;}
        public int getType(){return type;}

        public void setGrade_id(int grade_id){this.grade_id=grade_id;}
        public int getGrade_id(){return grade_id;}

        public void setUnit_name(String unit_name) {
            this.unit_name = unit_name;
        }
        public String getUnit_name(){return unit_name;}

        public int getBind_id() {
            return user_id;
        }

        public void setBind_id(int bind_id) {
            this.user_id = bind_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}
