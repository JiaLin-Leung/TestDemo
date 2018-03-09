package jiangsu.tbkt.teacher.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/1/14 0014.
 */
public class SelectClassBean implements Serializable{


    /**
     * id : 509690
     * learn_length : 6
     * max_class : 30
     * name : 小学部
     * type : 1
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        private int id;
        private int learn_length;
        private int max_class;
        private String name;
        private int type;

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

        public int getMax_class() {
            return max_class;
        }

        public void setMax_class(int max_class) {
            this.max_class = max_class;
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
