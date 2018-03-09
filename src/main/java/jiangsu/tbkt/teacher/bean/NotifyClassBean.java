package jiangsu.tbkt.teacher.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by song on 2016/11/14 0014.
 */
public class NotifyClassBean implements Serializable{

    /**
     * city : 411700
     * name : 207班
     * class_id : 7
     * unit_class_id : 582438
     * school_name : 创恒中学
     * grade_id : 2
     * county : 411702
     * id : 582438
     * type : 1
     * school_id : 178610
     * size : 1
     */

    private List<UnitsBean> units;

    public List<UnitsBean> getUnits() {
        return units;
    }

    public void setUnits(List<UnitsBean> units) {
        this.units = units;
    }

    public static class UnitsBean implements Serializable{
        private String city;
        private String name;
        private int class_id;
        private int unit_class_id;
        private String school_name;
        private int grade_id;
        private String county;
        private int id;
        private int type;
        private int school_id;
        private int size;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getClass_id() {
            return class_id;
        }

        public void setClass_id(int class_id) {
            this.class_id = class_id;
        }

        public int getUnit_class_id() {
            return unit_class_id;
        }

        public void setUnit_class_id(int unit_class_id) {
            this.unit_class_id = unit_class_id;
        }

        public String getSchool_name() {
            return school_name;
        }

        public void setSchool_name(String school_name) {
            this.school_name = school_name;
        }

        public int getGrade_id() {
            return grade_id;
        }

        public void setGrade_id(int grade_id) {
            this.grade_id = grade_id;
        }

        public String getCounty() {
            return county;
        }

        public void setCounty(String county) {
            this.county = county;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getSchool_id() {
            return school_id;
        }

        public void setSchool_id(int school_id) {
            this.school_id = school_id;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }
}
