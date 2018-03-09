package jiangsu.tbkt.teacher.bean;

/**
 * Created by song on 2017/1/17 0017.
 */
public class SwitchShenFenBean {

    /**
     * user_id : 897447
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int user_id;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }
    }
}
