package jiangsu.tbkt.teacher.bean;

import java.io.Serializable;

/**
 * @Description: 版本检测返回信息
 * @FileName: VersionCheck.java
 * @Package: com.tbkt.student.javabean
 * @Author: zhangzl
 * @Date: 2015/4/16
 * @Version V1.0
 */
public class VersionCheck implements Serializable {
    private ResultBean resultBean = new ResultBean();

    private String last_version = "0";
    private String release_version = "0";
    private String title = "";
    private String content = "";
    private String download = "";
    private String update = "";

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public ResultBean getResultBean() {
        return resultBean;
    }

    public void setResultBean(ResultBean resultBean) {
        this.resultBean = resultBean;
    }

    public String getLast_version() {
        return last_version;
    }

    public void setLast_version(String last_version) {
        this.last_version = last_version;
    }

    public String getRelease_version() {
        return release_version;
    }

    public void setRelease_version(String release_version) {
        this.release_version = release_version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }
}
