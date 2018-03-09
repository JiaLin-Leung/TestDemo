package jiangsu.tbkt.teacher.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/1/13 0013.
 */
public class SchoolBean {

    /**
     * ecid : 1
     * id : 21
     * learn_length : 3
     * name : 高新区大谢中学
     * type : 2
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String ecid;
        private int id;
        private int learn_length;
        private String name;
        private int type;

        public String getEcid() {
            return ecid;
        }

        public void setEcid(String ecid) {
            this.ecid = ecid;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLearn_length() {
            return learn_length;
        }

        public void setLearn_length(int learn_length) {
            this.learn_length = learn_length;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
