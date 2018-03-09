package jiangsu.tbkt.teacher.bean;


import java.io.Serializable;
import java.util.List;

/**
 * 账户信息bean对象
 */
public class SettingManageBean implements Serializable{
    /**
     * id : 276
     * name : 人教版一年级(上册)
     */

    private BookBean book;
    /**
     * book : {"id":276,"name":"人教版一年级(上册)"}
     * grade_id : 4
     * grades : [4]
     * id : 683511
     * name : 录老师
     * pbook : {"id":476,"name":"人教版三年级(上册)"}
     * portrait : http://file.tbkt.cn/upload_media/portrait/2016/11/08/20161108000127796294.png
     * units : [{"city":"410100","class_id":3,"grade_id":4,"id":575719,"name":"403班","school_id":6165,"school_name":"郑州市二七区幸福路小学","type":1,"unit_class_id":575719}]
     */

    private int grade_id;
    private int id;
    private String name;
    /**
     * id : 476
     * name : 人教版三年级(上册)
     */

    private PbookBean pbook;
    private String portrait;
    private int subject_id;
    private int platform_id;
    private List<Integer> grades;
    private int is_unit;
    private int is_setprofile;
    private int is_suqian;

    public void setIs_unit(int is_unit){this.is_unit=is_unit;}
    public int getIs_unit(){return is_unit;}

    public void setIs_setprofile(int is_setprofile){this.is_setprofile=is_setprofile;}
    public int getIs_setprofile(){return is_setprofile;}

    public void setIs_suqian(int is_suqian){this.is_suqian=is_suqian;}
    public int getIs_suqian(){return is_suqian;}

    public void setSubject_id(int subject_id){
        this.subject_id=subject_id;
    }
    public int getSubject_id(){
        return subject_id;
    }

    public void setPlatform_id(int platform_id){this.platform_id=platform_id;}
    public int getPlatform_id(){
        return platform_id;
//        return 0;
    }
    /**
     * city : 410100
     * class_id : 3
     * grade_id : 4
     * id : 575719
     * name : 403班
     * school_id : 6165
     * school_name : 郑州市二七区幸福路小学
     * type : 1
     * unit_class_id : 575719
     */

    private List<UnitsBean> units;

    public BookBean getBook() {
        return book;
    }

    public void setBook(BookBean book) {
        this.book = book;
    }

    public int getGrade_id() {
        return grade_id;
    }

    public void setGrade_id(int grade_id) {
        this.grade_id = grade_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PbookBean getPbook() {
        return pbook;
    }

    public void setPbook(PbookBean pbook) {
        this.pbook = pbook;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public List<Integer> getGrades() {
        return grades;
    }

    public void setGrades(List<Integer> grades) {
        this.grades = grades;
    }

    public List<UnitsBean> getUnits() {
        return units;
    }

    public void setUnits(List<UnitsBean> units) {
        this.units = units;
    }

    public static class BookBean implements Serializable{
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class PbookBean implements Serializable{
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class UnitsBean implements Serializable{
        private String city;
        private int class_id;
        private int grade_id;
        private int id;
        private String name;
        private int school_id;
        private String school_name;
        private int type;
        private int unit_class_id;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getClass_id() {
            return class_id;
        }

        public void setClass_id(int class_id) {
            this.class_id = class_id;
        }

        public int getGrade_id() {
            return grade_id;
        }

        public void setGrade_id(int grade_id) {
            this.grade_id = grade_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSchool_id() {
            return school_id;
        }

        public void setSchool_id(int school_id) {
            this.school_id = school_id;
        }

        public String getSchool_name() {
            return school_name;
        }

        public void setSchool_name(String school_name) {
            this.school_name = school_name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getUnit_class_id() {
            return unit_class_id;
        }

        public void setUnit_class_id(int unit_class_id) {
            this.unit_class_id = unit_class_id;
        }
    }
}
