package jiangsu.tbkt.teacher.api;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Map;

import jiangsu.tbkt.teacher.R;
import jiangsu.tbkt.teacher.activity.LoginActivity;
import jiangsu.tbkt.teacher.application.PreferencesManager;
import jiangsu.tbkt.teacher.application.ResultCode;
import jiangsu.tbkt.teacher.bean.ResultBean;
import jiangsu.tbkt.teacher.object.ResultBeanObject;
import jiangsu.tbkt.teacher.utils.CustomProgressDialog;
import jiangsu.tbkt.teacher.utils.LogUtil;
import jiangsu.tbkt.teacher.utils.NetworkStatueUtil;
import jiangsu.tbkt.teacher.utils.Tools;


/**
 * @Description: 请求网络
 * @FileName: ConnectToServer.java
 * @Package: com.tbkt.teacher_eng.api
 * @Author: zhangzl
 * @Date: 2015/4/27
 * @Version V1.0
 */
public class ConnectToServer {

    public static String BASE_URL = PreferencesManager.getInstance().getString("api","https://mapijs.m.jxtbkt.com");


    /**
     * 获取httpClient
     *
     * @return 获取httpClient
     */
//    private static HttpClient getHttpClient() {
//        HttpClient httpClient = new DefaultHttpClient();
//        return httpClient;
//    }

    public static HttpClient getHttpClient() {
        HttpClient mHttpClient=null;
        InputStream ins = null;
        try {
            ins = mContext.getAssets().open("tbkt.cer"); //下载的证书放到项目中的assets目录中

            CertificateFactory cerFactory = null;
            cerFactory = CertificateFactory.getInstance("X.509");
            Certificate cer = cerFactory.generateCertificate(ins);

//            KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");
//            keyStore.load(null, null);
//            keyStore.setCertificateEntry("trust", cer);
//            SSLSocketFactory socketFactory = new SSLSocketFactory(keyStore);
            SSLSocketFactory socketFactory = new SSLSocketFactory(null);
            Scheme sch = new Scheme("https", socketFactory, 443);

            mHttpClient = new DefaultHttpClient();
            mHttpClient.getConnectionManager().getSchemeRegistry().register(sch);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mHttpClient;
    }

    /**
     * 获取get请求的方法
     *
     * @param url
     * @return
     */
    private static HttpGet getHttpGet(String url) throws URISyntaxException {
        HttpGet httpGet = new HttpGet();
        httpGet.setURI(new URI(url));
        // 设置 请求超时时间
        return httpGet;
    }

    /**
     * 获取post请求的方法
     *
     * @param url
     * @return
     */
    private static HttpPost getHttpPost(String url) throws URISyntaxException {

        HttpPost httpPost = new HttpPost();
        httpPost.setURI(new URI(url));
        return httpPost;
    }

    public static String _MakeURL(String p_url, Map<String, Object> params) {
        if (params == null)
            return p_url;
        StringBuilder url = new StringBuilder(p_url);
        if (url.indexOf("?") < 0)
            url.append('?');

        for (String name : params.keySet()) {
            url.append('&');
            url.append(name);
            url.append('=');
            url.append(String.valueOf(params.get(name)));
            //不做URLEncoder处理
            //url.append(URLEncoder.encode(String.valueOf(params.get(name)), UTF_8));
        }

        return url.toString().replace("?&", "?");
    }

    /**
     * 获取GET请求的返回语句Stirng
     */
    public static String http_get(String url, Object object) throws IOException, URISyntaxException {
        url = BASE_URL + url;
        HttpClient httpClient = null;
        HttpGet httpGet = null;

        Map<String, Object> params = (Map<String, Object>) object;

        String responseBody = "";
        httpClient = getHttpClient();
        url = _MakeURL(url, params);
        httpGet = getHttpGet(url);

        String sessionid = PreferencesManager.getInstance().getString("sessionid", "");
//        syw 修改为Tbkt-Token
        httpGet.addHeader("Cookie", "tbkt_token=" + sessionid);
//        httpGet.addHeader("Tbkt-Token", "tbkt_token=" + sessionid);
        HttpResponse statusCode = httpClient.execute(httpGet);

        //syw sessionid信息
        Header[] headers = statusCode.getHeaders("Set-Cookie");
//        syw 修改关键字
//        Header[] headers = statusCode.getHeaders("Tbkt-Token");
        if (headers.length != 0) {
            String session = headers[0].toString();
//            Log.e("syw", "session" + session);
            if (session.contains("tbkt_token=")) {
                int start = session.indexOf("=");
                int end = session.indexOf(";");
                String sessionidSplit = session.substring(start + 1, end);
                Log.e("syw", "sessionidSplit:" + sessionidSplit);
                PreferencesManager.getInstance().putString("sessionid", sessionidSplit);
            }
        }

        if (statusCode.getStatusLine().getStatusCode() != 200) {//如果返回码不等于200 ,就要打印错误代码
            Message msg = handler.obtainMessage(ResultCode.HTTP_RESPONSE_ERROR, "连接服务器失败~，错误码：" + statusCode);
            handler.sendMessage(msg);
            return null;
        }
        responseBody = EntityUtils.toString(statusCode.getEntity());

        // 释放连接
        httpClient.getConnectionManager().shutdown();
        httpClient = null;

        //responseBody = responseBody.replaceAll("\\p{Cntrl}", "\r\n");

        return responseBody;
    }

    /**
     * 公用post方法
     * 获取POST请求的返回语句Stirng
     */
    public static String http_post(String url, Object object) throws IOException, URISyntaxException {
        url = BASE_URL + url;
        LogUtil.showPrint("http_post:" + url);
        if (!(object == null)) {
            LogUtil.showPrint("http_post:" + object.toString());
        }

        HttpClient httpClient = null;
        HttpPost httpPost = null;

        String jsonParams = "";
        if (object != null)
            jsonParams = object.toString();
        String responseBody = "";
        httpClient = getHttpClient();
        httpPost = getHttpPost(url);

        String sessionid = PreferencesManager.getInstance().getString("sessionid", "");
//        Log.e("syw", "sessionid" + sessionid);
        //        syw 修改关键字
        httpPost.addHeader("Cookie", "tbkt_token=" + sessionid);
//        httpPost.addHeader("Tbkt-Token", "tbkt_token=" + sessionid);
        httpPost.setEntity(new StringEntity(jsonParams, HTTP.UTF_8));
        HttpResponse statusCode = httpClient.execute(httpPost);

        //syw sessionid信息
        Header[] headers = statusCode.getHeaders("Set-Cookie");
//        Header[] headers = statusCode.getHeaders("Tbkt-Token");
        if (headers.length != 0) {
            String session = headers[0].toString();
            Log.e("syw", "分配了新的sessionid" + session);
            if (session.contains("tbkt_token=")) {
                int start = session.indexOf("=");
                int end = session.indexOf(";");
                String sessionidSplit = session.substring(start + 1, end);
                Log.e("syw", "sessionidSplit:" + sessionidSplit);
                PreferencesManager.getInstance().putString("sessionid", sessionidSplit);
            }
        }

        int statusCodenum = statusCode.getStatusLine().getStatusCode();

        if (statusCodenum != 200) {
            Message msg = handler.obtainMessage(ResultCode.HTTP_RESPONSE_ERROR, "连接服务器失败~，错误码：" + statusCode);
            handler.sendMessage(msg);
            return null;
        }

        responseBody = EntityUtils.toString(statusCode.getEntity());

        responseBody = responseBody.replaceAll("\\p{Cntrl}", "");

        // 释放连接
        httpClient.getConnectionManager().shutdown();

        return responseBody;
    }


    /**
     * 跳转页面
     *
     * @param context
     * @param cls
     */
    private static void jumpPage(Context context, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    //进度条
    public static CustomProgressDialog mProgressDialog = null;

    private static void showProgressDialog(final Context context, String msg, boolean isShowProgress) {
        if (isShowProgress) {
            mProgressDialog = new CustomProgressDialog(context, R.style.dialog);
            mProgressDialog.setText("正在加载");
            mProgressDialog.show();
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    ((Activity) context).finish();
                }
            });
        }
    }

    //取消对话框
    private static void dismissProgressDialog() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    //toast提示
    private static void showToastMsg(Context context, String msg, boolean isShowToast) {
        if (isShowToast && !TextUtils.isEmpty(msg)) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    // 回调
    public static interface ConnCallBack {
        void onSuccessBack(ResultBean result);

        void onFailBack(ResultBean result);
    }

    private static android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ResultCode.SUCCESS:
                    dismissProgressDialog();
                    if (msg.obj != null) {
                        String result = msg.obj.toString();
                        configClass(result);
                        try {
                            ResultBean bean = ResultBeanObject.getResultBean(result);
                            if (bean.getResponse().equalsIgnoreCase("ok")) {//返回所需数据
                                if (mConnCallBack != null) {
                                    mConnCallBack.onSuccessBack(bean);
                                }
                            } else {
                                if (bean.getError().equals("no_user")) {// 用户会话过期，调转到登录页面
                                    PreferencesManager.getInstance().putString("isExist1", "0");
                                    PreferencesManager.getInstance().putString("sessionid", "");
                                    if (mContext != null) jumpPage(mContext, LoginActivity.class);
                                } else {
                                    if (mConnCallBack != null) mConnCallBack.onFailBack(bean);
                                }
                            }

//                            显示toast提示
                            if (mContext != null) {
                                if (bean.getMessage().contains("账号或密码错误") || bean.getMessage().contains("密码将发到")) {
                                } else if (bean.getMessage().contains("暂不支持") && "switch".equals(bean.getError())) {
                                } else {
                                    showToastMsg(mContext, bean.getMessage(), mIsShowToast);
                                }
                            }

                        } catch (JSONException e) {
                            showToastMsg(mContext, "数据解析异常", true);
                            ResultBean bean = new ResultBean();
                            bean.setMessage("数据解析异常");
                            if (mConnCallBack != null) {
                                mConnCallBack.onFailBack(bean);
                            }
                        }
                    }
                    break;
                case ResultCode.FAIL:
                    dismissProgressDialog();
                    if (mContext != null) {
                        showToastMsg(mContext, "连接服务器失败~", true);
                        ResultBean bean = new ResultBean();
                        bean.setMessage("连接服务器失败~");
                        if (mConnCallBack != null) {
                            mConnCallBack.onFailBack(bean);
                        }
                    }
                    break;
                case ResultCode.HTTP_RESPONSE_ERROR://连接服务失败
                    dismissProgressDialog();
                    if (msg.obj != null && mContext != null)
                        showToastMsg(mContext, "连接服务器失败", true);
                    break;
                default:
                    dismissProgressDialog();
                    break;
            }
        }
    };

    private static Context mContext;
    private static boolean mIsShowToast;
    private static ConnCallBack mConnCallBack;

    public static void connectionServer(final Context context, final String url, final Object jsonParams, final ConnCallBack connCallBack,
                                        boolean isShowProgress, boolean isShowToast, final boolean isPost) {
        if (!NetworkStatueUtil.isConnectInternet(context)) {
            showToastMsg(context, "网络不可用，请检查网络设置", true);
            ResultBean bean = new ResultBean();
            bean.setMessage("网络不可用，请检查网络设置");

            if (connCallBack != null) {
                connCallBack.onFailBack(bean);
            }
            return;
        }
        mContext = context;
        mIsShowToast = isShowToast;
        mConnCallBack = connCallBack;
        showProgressDialog(context, null, isShowProgress);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LogUtil.showError(ConnectToServer.class, "请求的url=" + BASE_URL + url);
                    String result = "";
                    if (isPost) {
                        result = http_post(url, jsonParams);
                    } else {
                        result = http_get(url, jsonParams);
                    }
                    LogUtil.showError(ConnectToServer.class, "result:" + result);
                    Message msg = handler.obtainMessage(ResultCode.SUCCESS, result);
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("post:\nurl:" + url);
                    stringBuilder.append("\nparams:" + jsonParams);
                    stringBuilder.append("\nsessionid:" + PreferencesManager.getInstance().getString("sessionid", ""));
                    String sb = Tools.getCrashInfoToString(e);
                    stringBuilder.append("\nthrowable:" + sb);

                    handler.sendEmptyMessage(ResultCode.FAIL);
                }
            }
        }).start();
    }

    /**
     * 判断年级
     *
     * @param s
     */
    public static void configClass(String s) {
        try {
            JSONObject json = new JSONObject(s);
            JSONObject jsonExtra = json.getJSONObject("extra");
            String temp = jsonExtra.getString("school_type");
            if (temp.equals("0")) {
                return;
            } else if (temp.equals(PreferencesManager.getInstance().getString("school_type", ""))) {
                return;
            } else {
                PreferencesManager.getInstance().putString("school_type", temp);
            }
        } catch (Exception e) {
        }
    }
}
