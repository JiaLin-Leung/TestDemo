package jiangsu.tbkt.teacher.bean;

import java.io.Serializable;

/**
 * @Description:
 * @FileName: ResultBean.java
 * @Package: com.tbkt.teacher_eng.javabean
 * @Author: zhangzl
 * @Date: 2015/4/9
 * @Version V1.0
 */
public class ResultBean implements Serializable {
    private String response = ""; //OK表示成功
    private String error = ""; //错误代码
    private String message = "";//成功或失败的提示语
    private String data = "" ;  //数据data

    @Override
    public String toString() {
        return "ResultBean{" +
            "response='" + response + '\'' +
            ", error='" + error + '\'' +
            ", message='" + message + '\'' +
            '}';
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
