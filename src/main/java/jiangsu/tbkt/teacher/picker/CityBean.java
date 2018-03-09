package jiangsu.tbkt.teacher.picker;

import java.util.List;

/**
 * Created by song on 2017/1/13 0013.
 */
public class CityBean {


    /**
     * child : [{"id":"410102","name":"中原区"},{"id":"410103","name":"二七区"},{"id":"410104","name":"管城回族区"},{"id":"410105","name":"金水区"},{"id":"410106","name":"上街区"},{"id":"410108","name":"惠济区"},{"id":"410122","name":"中牟县"},{"id":"410181","name":"巩义市"},{"id":"410182","name":"荥阳市"},{"id":"410183","name":"新密市"},{"id":"410184","name":"新郑市"},{"id":"410185","name":"登封市"},{"id":"410190","name":"郑东新区"},{"id":"410191","name":"高新技术开发区"},{"id":"410192","name":"经济技术开发区"},{"id":"410193","name":"郑州出口加工区"},{"id":"410101","name":"市辖区"}]
     * id : 410100
     * name : 郑州市
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String id;
        private String name;
        /**
         * id : 410102
         * name : 中原区
         */

        private List<ChildBean> child;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ChildBean> getChild() {
            return child;
        }

        public void setChild(List<ChildBean> child) {
            this.child = child;
        }

        public static class ChildBean {
            private String id;
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
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
}
