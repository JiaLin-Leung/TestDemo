package jiangsu.tbkt.teacher.object;


import jiangsu.tbkt.teacher.bean.ResultBean;
import jiangsu.tbkt.teacher.bean.ResultBean2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ResultBeanObject {

    public static ResultBean getResultBean(String json) throws JSONException {
        ResultBean bean = new ResultBean();
        JSONObject obj = new JSONObject(json);
        bean.setResponse(obj.has("response") ? obj.getString("response") : "fail");
        bean.setMessage(obj.has("message") ? obj.getString("message") : "");
        bean.setError(obj.has("error") ? obj.getString("error") : "");
        bean.setData(obj.has("data") ? obj.getString("data") : "");
        return bean;
    }

    /**
     * 特殊的ResultBean
     * @param json
     * @return
     */
    public static ResultBean2 getResultBean2(String json){
        ResultBean2 bean = new ResultBean2();
        try {
            JSONArray jsonArray=new JSONArray(json);
            JSONObject jsonObject = jsonArray.optJSONObject(0);
            bean.file_name=jsonObject.optString("file_name");
            bean.file_size=jsonObject.optString("file_size");
            bean.file_url=jsonObject.optString("file_url");
            bean.status=jsonObject.optInt("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bean;
    }
}
