package jiangsu.tbkt.teacher.object;


import jiangsu.tbkt.teacher.bean.ResultBean;
import jiangsu.tbkt.teacher.bean.VersionCheck;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Description:
 * @FileName: VersionCkeckObject.java
 * @Package: com.tbkt.student.object
 * @Author: zhangzl
 * @Date: 2015/4/16
 * @Version V1.0
 */
public class VersionCkeckObject {
    public static VersionCheck parseVersionCheck(ResultBean resultBean) throws JSONException {
        VersionCheck bean = new VersionCheck();

        bean.setResultBean(resultBean);
        if (resultBean.getResponse().equals("ok")) {
            String data = resultBean.getData();
            JSONObject obj = new JSONObject(data);

            if (obj != null) {
                bean.setContent(obj.has("content") ? obj.getString("content") : "");
                bean.setDownload(obj.has("download") ? obj.getString("download") : "");
                bean.setLast_version(obj.has("last_version") ? obj.getString("last_version") : "");
                bean.setTitle(obj.has("title") ? obj.getString("title") : "");
                bean.setRelease_version(obj.has("release_version") ? obj.getString("release_version") : "");
                bean.setUpdate(obj.has("update") ? obj.getString("update") : "");
            }
        }

        return bean;
    }
}
