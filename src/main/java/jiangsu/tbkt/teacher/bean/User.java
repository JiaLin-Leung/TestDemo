package jiangsu.tbkt.teacher.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by song on 2016/4/24.
 */
public class User implements Serializable {
    /**
     * bind_id : 2662908
     * phone_number : 13838224606
     * abb : hyl
     * user_id : 2662908
     * billing : 2
     * billing_date :
     * is_status : 0
     * sex : 1
     * state : unopen
     * portrait : http://user.tbkt.cn/site_media/images/profile/default-student.png
     * open_date : null
     * user_name : 韩莹莉
     */

    private List<StudentsBean> students;

    public List<StudentsBean> getStudents() {
        return students;
    }

    public void setStudents(List<StudentsBean> students) {
        this.students = students;
    }

    public static class StudentsBean implements Serializable {
        //syw
        public boolean isChecked=false;

        private int bind_id;
        private String phone_number;
        private String abb;
        private int user_id;
        private int billing;
        private String billing_date;
        private int is_status;
        private int sex;
        private String state;
        private String portrait;
        private Object open_date;
        private String user_name;
        private String firstLetter;
        private String pinyin;


        public String getFirstLetter() {
            return firstLetter;
        }

        public void setFirstLetter(String firstLetter) {
            this.firstLetter = firstLetter;
        }

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public int getBind_id() {
            return bind_id;
        }

        public void setBind_id(int bind_id) {
            this.bind_id = bind_id;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
        }

        public String getAbb() {
            return abb;
        }

        public void setAbb(String abb) {
            this.abb = abb;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getBilling() {
            return billing;
        }

        public void setBilling(int billing) {
            this.billing = billing;
        }

        public String getBilling_date() {
            return billing_date;
        }

        public void setBilling_date(String billing_date) {
            this.billing_date = billing_date;
        }

        public int getIs_status() {
            return is_status;
        }

        public void setIs_status(int is_status) {
            this.is_status = is_status;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public Object getOpen_date() {
            return open_date;
        }

        public void setOpen_date(Object open_date) {
            this.open_date = open_date;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }
    }
    //syw
//    public boolean isChecked=false;
//    public String portrait;
//    public String id;
//    public String bind_id;


//    private int img;
//    private String username;
//    private String pinyin;
//    private String firstLetter;
//
//
//
//    public User() {
//    }
//
//    public String getFirstLetter() {
//        return firstLetter;
//    }
//
//    public void setFirstLetter(String firstLetter) {
//        this.firstLetter = firstLetter;
//    }
//
//    public int getImg() {
//        return img;
//    }
//
//    public void setImg(int img) {
//        this.img = img;
//    }
//
//    public String getPinyin() {
//        return pinyin;
//    }
//
//    public void setPinyin(String pinyin) {
//        this.pinyin = pinyin;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public User(String firstLetter, int img, String pinyin, String username) {
//        this.firstLetter = firstLetter;
//        this.img = img;
//        this.pinyin = pinyin;
//        this.username = username;
//    }
}
