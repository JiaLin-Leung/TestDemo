package jiangsu.tbkt.teacher.bean;

import java.io.Serializable;


public class UrlGetBean implements Serializable {


    /**
     * cs_phone : 0371-63373776
     * file_size : 1048576
     * hosts : {"api":"http://mapi8.m.tbkt.cn","apisx":"http://mapisx8.m.tbkt.cn","apisx2":"http://mapisx28.m.tbkt.cn","apixcps":"http://mapixcp8.m.tbkt.cn","apiyy":"http://mapiyy8.m.tbkt.cn","vuestu":"http://stu8.m.tbkt.cn","vuestusx":"http://stusx8.m.tbkt.cn","vuestusx2":"http://stusx28.m.tbkt.cn","vuestuxcps":"http://stuxcp8.m.tbkt.cn","vuestuyw":"http://stuyw8.m.tbkt.cn","vuestuyy":"http://stuyy8.m.tbkt.cn","vuetea":"http://tea8.m.tbkt.cn","vueteaxcps":"http://teaxcp8.m.tbkt.cn"}
     * joinclass_style : 0
     * upload_url : http://file.tbkt.cn/swf_upload/?upcheck=3f078cbb4b6fc3a6b40aa228f607c769&upType=
     */

    private DataBean data;
    /**
     * data : {"cs_phone":"0371-63373776","file_size":1048576,"hosts":{"api":"http://mapi8.m.tbkt.cn","apisx":"http://mapisx8.m.tbkt.cn","apisx2":"http://mapisx28.m.tbkt.cn","apixcps":"http://mapixcp8.m.tbkt.cn","apiyy":"http://mapiyy8.m.tbkt.cn","vuestu":"http://stu8.m.tbkt.cn","vuestusx":"http://stusx8.m.tbkt.cn","vuestusx2":"http://stusx28.m.tbkt.cn","vuestuxcps":"http://stuxcp8.m.tbkt.cn","vuestuyw":"http://stuyw8.m.tbkt.cn","vuestuyy":"http://stuyy8.m.tbkt.cn","vuetea":"http://tea8.m.tbkt.cn","vueteaxcps":"http://teaxcp8.m.tbkt.cn"},"joinclass_style":0,"upload_url":"http://file.tbkt.cn/swf_upload/?upcheck=3f078cbb4b6fc3a6b40aa228f607c769&upType="}
     * error :
     * message :
     * next :
     * response : ok
     */

    private String error;
    private String message;
    private String next;
    private String response;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public static class DataBean {
        private String cs_phone;
        private int file_size;
        /**
         * api : http://mapi8.m.tbkt.cn
         * apisx : http://mapisx8.m.tbkt.cn
         * apisx2 : http://mapisx28.m.tbkt.cn
         * apixcps : http://mapixcp8.m.tbkt.cn
         * apiyy : http://mapiyy8.m.tbkt.cn
         * vuestu : http://stu8.m.tbkt.cn
         * vuestusx : http://stusx8.m.tbkt.cn
         * vuestusx2 : http://stusx28.m.tbkt.cn
         * vuestuxcps : http://stuxcp8.m.tbkt.cn
         * vuestuyw : http://stuyw8.m.tbkt.cn
         * vuestuyy : http://stuyy8.m.tbkt.cn
         * vuetea : http://tea8.m.tbkt.cn
         * vueteaxcps : http://teaxcp8.m.tbkt.cn
         */

        private HostsBean hosts;
        private int joinclass_style;
        private String upload_url;
        private String is_show;
        private String platform_dict;

        public String getPlatform_dict(){return platform_dict;}
        public void setPlatform_dict(String platform_dict){this.platform_dict=platform_dict;}

        public String getIs_show(){return is_show;}
        public void setIs_show(String is_show){this.is_show=is_show;}

        public String getCs_phone() {
            return cs_phone;
        }

        public void setCs_phone(String cs_phone) {
            this.cs_phone = cs_phone;
        }

        public int getFile_size() {
            return file_size;
        }

        public void setFile_size(int file_size) {
            this.file_size = file_size;
        }

        public HostsBean getHosts() {
            return hosts;
        }

        public void setHosts(HostsBean hosts) {
            this.hosts = hosts;
        }

        public int getJoinclass_style() {
            return joinclass_style;
        }

        public void setJoinclass_style(int joinclass_style) {
            this.joinclass_style = joinclass_style;
        }

        public String getUpload_url() {
            return upload_url;
        }

        public void setUpload_url(String upload_url) {
            this.upload_url = upload_url;
        }

        public static class HostsBean {
            private String api;
            private String apisx;
            private String apisx2;
            private String apixcps;
            private String apiyy;
            private String vuestu;
            private String vuestusx;
            private String vuestusx2;
            private String vuestuxcps;
            private String vuestuyw;
            private String vuestuyy;
            private String vuetea;
            private String vueteaxcps;
            private String qr;

            private String goqgurl;
            private String userurl;

            private String other_login_url;
            private String get_token_from_ws;

//          syw 阿里云迁移之后设置页面教材、教辅、点读机修改url
            private String vueteayy;
            private String vueteasx;
            private String vueteacom;

            public String getGet_token_from_ws() {
                return get_token_from_ws;
            }

            public void setVueteayy(String vueteayy) {
                this.vueteayy = vueteayy;
            }

            public String getVueteayy() {
                return vueteayy;
            }

            public void setVueteasx(String vueteasx) {
                this.vueteasx = vueteasx;
            }

            public String getVueteasx() {
                return vueteasx;
            }

            public void setVueteacom(String vueteacom) {
                this.vueteacom = vueteacom;
            }

            public String getVueteacom() {
                return vueteacom;
            }

            public void setOther_login_url(String other_login_url){this.other_login_url=other_login_url;}
            public String getOther_login_url(){return other_login_url;}

            public String getUserurl(){return userurl;}
            public void setUserurl(String userurl){this.userurl=userurl;}

            public String getGoqgurl(){return goqgurl;}
            public void setGoqgurl(String goqgurl){this.goqgurl=goqgurl;}

            public String getQr(){return qr;}
            public void setQr(String qr){this.qr=qr;}

            public String getApi() {
                return api;
            }

            public void setApi(String api) {
                this.api = api;
            }

            public String getApisx() {
                return apisx;
            }

            public void setApisx(String apisx) {
                this.apisx = apisx;
            }

            public String getApisx2() {
                return apisx2;
            }

            public void setApisx2(String apisx2) {
                this.apisx2 = apisx2;
            }

            public String getApixcps() {
                return apixcps;
            }

            public void setApixcps(String apixcps) {
                this.apixcps = apixcps;
            }

            public String getApiyy() {
                return apiyy;
            }

            public void setApiyy(String apiyy) {
                this.apiyy = apiyy;
            }

            public String getVuestu() {
                return vuestu;
            }

            public void setVuestu(String vuestu) {
                this.vuestu = vuestu;
            }

            public String getVuestusx() {
                return vuestusx;
            }

            public void setVuestusx(String vuestusx) {
                this.vuestusx = vuestusx;
            }

            public String getVuestusx2() {
                return vuestusx2;
            }

            public void setVuestusx2(String vuestusx2) {
                this.vuestusx2 = vuestusx2;
            }

            public String getVuestuxcps() {
                return vuestuxcps;
            }

            public void setVuestuxcps(String vuestuxcps) {
                this.vuestuxcps = vuestuxcps;
            }

            public String getVuestuyw() {
                return vuestuyw;
            }

            public void setVuestuyw(String vuestuyw) {
                this.vuestuyw = vuestuyw;
            }

            public String getVuestuyy() {
                return vuestuyy;
            }

            public void setVuestuyy(String vuestuyy) {
                this.vuestuyy = vuestuyy;
            }

            public String getVuetea() {
                return vuetea;
            }

            public void setVuetea(String vuetea) {
                this.vuetea = vuetea;
            }

            public String getVueteaxcps() {
                return vueteaxcps;
            }

            public void setVueteaxcps(String vueteaxcps) {
                this.vueteaxcps = vueteaxcps;
            }
        }
    }
}
