package jiangsu.tbkt.teacher.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import jiangsu.tbkt.teacher.R;
import jiangsu.tbkt.teacher.application.PreferencesManager;
import jiangsu.tbkt.teacher.bean.UrlGetBean;
import jiangsu.tbkt.teacher.utils.MyToastUtils;
import jiangsu.tbkt.teacher.utils.NetworkStatueUtil;
import jiangsu.tbkt.teacher.utils.Tools;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 欢迎页面
 */
public class WelcomeActivity extends BaseActivity {

    private String systemInfoUrl;
    private OkHttpClient mHttpClient;
    private String arg1 = "";
    private String platform = "";
    private String token1;
    private String appToken;//第三方拉起数据
    private String userType;//第三方拉起数据
    RelativeLayout layoutContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        appToken = getIntent().getStringExtra("appToken");
        userType = getIntent().getStringExtra("userType");
        systemInfoUrl = "https://appconfig.m.jxtbkt.cn/system/info?flag=1&code=320000&platform=3&version="
                + Tools.getAppVersion(this)
                + "&user_id=" + PreferencesManager.getInstance().getInt("user_id", 0);
        Log.e("syw", "url:" + systemInfoUrl);
        initOkHttp();
        copyDataToLocal("tbkt_20180408.cer");
        layoutContent = (RelativeLayout) findViewById(R.id.layout_content);
        if (!NetworkStatueUtil.isConnectInternet(this)) {
            MyToastUtils.toastText(this, "网络不可用,请检查网络设置");
            return;
        }
        getUrlData(systemInfoUrl);
    }


    private void showPopWindow() {
        View convertview = null;

        convertview = View.inflate(WelcomeActivity.this, R.layout.pop_internet, null);
        TextView tv_confirm = (TextView) convertview.findViewById(R.id.tv_confirm);
        final PopupWindow checkOutWindow = new PopupWindow(convertview, ViewGroup.LayoutParams.MATCH_PARENT, -1, true);
        checkOutWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        checkOutWindow.showAtLocation(layoutContent, Gravity.CENTER, 0, 0);
        checkOutWindow.setAnimationStyle(R.style.popwindow_anim_style);
        checkOutWindow.update();
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkOutWindow.dismiss();
                getUrlData(systemInfoUrl);
            }
        });
    }



    // 把数据库从assert文件里复制到手机
    private void copyDataToLocal(final String fileName) {
        // 判断文件是否存在
        File file = new File(getFilesDir(), fileName);
        if (file.exists()) {
            return;
        }
        new Thread() {
            public void run() {
                try {
                    //从assets文件夹获取一个指定文件的输入流
                    AssetManager manager = getAssets();
                    InputStream is = manager.open(fileName);
                    //TODO 定义输出流 /data/data/包名/files/address.db
                    //上下文里边的方法，可以根据上下文和文件名获得路径
                    //比自己定义的方便
                    FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);

                    int len = -1;
                    byte[] bytes = new byte[1024];
                    while ((len = is.read(bytes)) != -1) {
                        fos.write(bytes, 0, len);
                    }
                    fos.close();
                    is.close();
                    Log.e("syw", "复制文件完成");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void initOkHttp() {
        mHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    HashMap<String, String> hashMap;

    private void getUrlData(final String url) {
        Log.e("syw", "systemUrl:" + url);
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showPopWindow();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                final UrlGetBean urlGetBean = gson.fromJson(response.body().string(), UrlGetBean.class);
                if ("fail".equals(urlGetBean.getResponse())) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showPopWindow();
                        }
                    });
                } else {
                    PreferencesManager.getInstance().putString("api", urlGetBean.getData().getHosts().getApi());//存储公共域名
                    PreferencesManager.getInstance().putString("apisx", urlGetBean.getData().getHosts().getApisx());//存储数学域名
                    PreferencesManager.getInstance().putString("apisx2", urlGetBean.getData().getHosts().getApisx2());//存储数学域名
                    PreferencesManager.getInstance().putString("apixcps", urlGetBean.getData().getHosts().getApixcps());//存储学测评域名
                    PreferencesManager.getInstance().putString("apiyy", urlGetBean.getData().getHosts().getApiyy());//存储英语域名
                    PreferencesManager.getInstance().putString("vuestu", urlGetBean.getData().getHosts().getVuestu());//存储数学域名
                    PreferencesManager.getInstance().putString("vuestusx", urlGetBean.getData().getHosts().getVuestusx());//存储英语域名
                    PreferencesManager.getInstance().putString("vuestusx2", urlGetBean.getData().getHosts().getVuestusx2());//存储英语域名
                    PreferencesManager.getInstance().putString("vuestuxcps", urlGetBean.getData().getHosts().getVuestuxcps());//存储学测评域名
                    PreferencesManager.getInstance().putString("vuestuyw", urlGetBean.getData().getHosts().getVuestuyw());//存储H5教师域名
                    PreferencesManager.getInstance().putString("vuestuyy", urlGetBean.getData().getHosts().getVuestuyy());//存储英语域名
//                syw 为了给梁佳琳测试+111
                    PreferencesManager.getInstance().putString("vuetea", urlGetBean.getData().getHosts().getVuetea());//存储英语域名
                    PreferencesManager.getInstance().putString("vueteaxcps", urlGetBean.getData().getHosts().getVueteaxcps());//存储英语域名
                    PreferencesManager.getInstance().putString("qr", urlGetBean.getData().getHosts().getQr());//存储英语域名
                    PreferencesManager.getInstance().putString("get_token_from_ws", urlGetBean.getData().getHosts().getGet_token_from_ws());//存储第三方拉起获取token域名

                    PreferencesManager.getInstance().putString("vueteayy", urlGetBean.getData().getHosts().getVueteayy());
                    PreferencesManager.getInstance().putString("vueteasx", urlGetBean.getData().getHosts().getVueteasx());
                    PreferencesManager.getInstance().putString("vueteacom", urlGetBean.getData().getHosts().getVueteacom());

                    PreferencesManager.getInstance().putString("cs_phone", urlGetBean.getData().getCs_phone());
                    PreferencesManager.getInstance().putInt("file_size", urlGetBean.getData().getFile_size());
                    PreferencesManager.getInstance().putString("upload_url", urlGetBean.getData().getUpload_url());
                    PreferencesManager.getInstance().putString("is_show", urlGetBean.getData().getIs_show());
                    PreferencesManager.getInstance().putInt("joinclass_style", urlGetBean.getData().getJoinclass_style());

                    PreferencesManager.getInstance().putString("userurl", urlGetBean.getData().getHosts().getUserurl());
                    PreferencesManager.getInstance().putString("goqgurl", urlGetBean.getData().getHosts().getGoqgurl());
                    PreferencesManager.getInstance().putString("other_login_url", urlGetBean.getData().getHosts().getOther_login_url());
                }

                String platform_dict = urlGetBean.getData().getPlatform_dict();
                String[] dict_items = platform_dict.split(",");

                hashMap = new HashMap();
                for (int i = 0; i < dict_items.length; i++) {
                    String dict_item = dict_items[i];
                    String[] item = dict_item.split(":");
                    hashMap.put(item[0], item[1]);
                }

                Intent intent = getIntent();
                if (intent != null) {
                    arg1 = intent.getStringExtra("token");
                    platform = intent.getStringExtra("platform");
                    Log.e("syw", "arg1:" + arg1 + "platform:" + platform);
                } else {
                    arg1 = "";
                }

                Message msg = new Message();
                msg.what = 1000;
                handler.sendMessageDelayed(msg, 2000);
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1000:
                    if (appToken == null || userType == null) {
                        jumpToPage();
                    } else {
                        //第三方拉起获取之后请求token值，而后判断跳转
                        getTBKTToken();
                    }
                    break;
            }
        }
    };

    private void jumpToPage() {
        //获取最新的版本号与本地存储的版本号对比，判断页面跳转
        int oldVersionCode = PreferencesManager.getInstance().getInt("version", Tools.getAppVersionCode(WelcomeActivity.this) - 1);
        int newVersionCode = Tools.getAppVersionCode(WelcomeActivity.this);
        if (oldVersionCode == newVersionCode) {
            Log.e("lsq", "版本相同");
            //获取登录状态，判断页面跳转
            String isExist = PreferencesManager.getInstance().getString("isExist", "0");
            if (isExist.equals("1")) {// 0为退出状态  1为登录状态
                jumpToPage(MainActivity.class, null, true);

            } else {
                Intent intent = new Intent(this, WebActivity.class);
                startActivityForResult(intent, 10010);
            }
        } else {
            Log.e("lsq", "版本不同");
            Intent intent = new Intent(this, WebActivity.class);
            startActivityForResult(intent, 10010);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10010:
                Intent intent = new Intent(this, WebActivity.class);
                startActivityForResult(intent, 10010);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hashMap = null;
    }

    private String username;
    private String tbkt_token;
    private String user_id;

    /**
     * 根据第三方传值获取tbkt_token
     */
    private void getTBKTToken() {

        final Gson gson = new Gson();
        String url = PreferencesManager.getInstance().getString("get_token_from_ws", "http://gojs.jxtbkt.com");

        //Form表单格式的参数传递
        FormBody formBody = new FormBody
                .Builder()
                .add("appToken", appToken)//设置参数名称和参数值
                .add("userType", userType)
                .build();
        Request request = new Request
                .Builder()
                .post(formBody)//Post请求的参数传递
                .url(url + "/app/")
                .build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyToastUtils.toastText(WelcomeActivity.this,"网络错误，请重试");
                        Intent intent = new Intent(WelcomeActivity.this, WebActivity.class);
                        startActivityForResult(intent, 10010);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                //此方法运行在子线程中，不能在此方法中进行UI操作。
                final TokenBean bean = gson.fromJson(response.body().string(), TokenBean.class);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (bean.getResponse().equals("ok")) {
                            tbkt_token = bean.getData().getTbkt_token();
                            user_id = bean.getData().getUser_id();
                            username = bean.getData().getUsername();
                            PreferencesManager.getInstance().putInt("user_id", Integer.parseInt(user_id));
                            PreferencesManager.getInstance().putString("sessionid", tbkt_token);
                            jumpToPage(MainActivity.class, null, true);
                        } else {
                            String error = bean.getError();
                            MyToastUtils.toastText(WelcomeActivity.this, error);
                            Intent intent = new Intent(WelcomeActivity.this, WebActivity.class);
                            startActivityForResult(intent, 10010);
                        }
                    }
                });
            }
        });

    }

    class TokenBean {

        private String message;
        private String next;
        private String response;
        private String error;
        private Bean data;

        class Bean {
            private String username;
            private String tbkt_token;
            private String user_id;

            public String getUsername() {
                return username;
            }

            public String getTbkt_token() {
                return tbkt_token;
            }

            public String getUser_id() {
                return user_id;
            }
        }

        public String getMessage() {
            return message;
        }

        public String getNext() {
            return next;
        }

        public String getResponse() {
            return response;
        }

        public String getError() {
            return error;
        }

        public Bean getData() {
            return data;
        }
    }
}