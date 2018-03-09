package jiangsu.tbkt.teacher.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/1 0001.
 */
public class SubjectBean {

    /**
     * id : 2
     * name : 数学
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
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
}
