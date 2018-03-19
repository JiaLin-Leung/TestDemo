package jiangsu.tbkt.teacher.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import org.xwalk.core.ClientCertRequest;
import org.xwalk.core.XWalkNavigationHistory;
import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkSettings;
import org.xwalk.core.XWalkView;

import java.io.IOException;

import jiangsu.tbkt.teacher.R;
import jiangsu.tbkt.teacher.application.PreferencesManager;
import jiangsu.tbkt.teacher.utils.MyToastUtils;
import jiangsu.tbkt.teacher.utils.NetworkStatueUtil;
import jiangsu.tbkt.teacher.utils.Tools;
import jiangsu.tbkt.teacher.view.MyXwalkview;

/**
 * Created by song on 2016/9/28 0028.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final int YS_SHARE = 7000;
    private String HOME_URL;
    public static boolean isFlush = false;
    public static MyXwalkview web_home;
    private LinearLayout fail_layout;
    private Button reload_btn;
    private String urlToReload = HOME_URL;
    //    private TextView tv_scan;
    public String tempStr;
    private static final int YS_CLEAN = 6000;

    private String share_url = "";
    private String title;


    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1000:
                    HOME_URL = PreferencesManager.getInstance().getString("vuetea", "https://teacomjs.m.jxtbkt.com") + "/?t=" + System.currentTimeMillis() + "&tbkt_token="
                            + PreferencesManager.getInstance().getString("sessionid", "")+"&platform=3&version="
                            + Tools.getAppVersion(MainActivity.this);
                    loadUrl(HOME_URL);
                    break;
                case 2000:
                    String video_url = tempStr.split(",")[1];
                    Intent intent = new Intent(MainActivity.this, VideoAllScreenActivity.class);
                    intent.putExtra("video_url", video_url);
                    startActivity(intent);
                    break;
                case 3000:
                    PreferencesManager.getInstance().putString("isExist1", "0");
                    Intent i = new Intent(MainActivity.this, WebActivity.class);
                    startActivity(i);
                    finish();
                    break;
                case YS_CLEAN:
                    xWalkNavigationHistory.clear();
                    break;
                case YS_SHARE:
                    ShareWeb(share_url);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        version=Tools.getAppVersion(MainActivity.this);

//        syw保留登录状态
        PreferencesManager.getInstance().putString("isExist1", "1");//登录成功，保存登录状态
//        PreferencesManager.getInstance().putString("vuetea", "http://teacom.m.jxtbkt.com");
        versionCheck();

        fail_layout = (LinearLayout) findViewById(R.id.fail_layout);
        reload_btn = (Button) findViewById(R.id.reload_btn);
        reload_btn.setOnClickListener(this);

        web_home = (MyXwalkview) findViewById(R.id.web_home);
        web_home.setOnLongClickListener(new View.OnLongClickListener() {//设置禁止复制粘贴
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
        initWebView();
    }


    private void initWebView() {
        XWalkPreferences.setValue("enable-javascript", true);//添加对javascript支持
        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);//开启调式,支持谷歌浏览器调式
        XWalkPreferences.setValue(XWalkPreferences.ALLOW_UNIVERSAL_ACCESS_FROM_FILE, true);//置是否允许通过file url加载的Javascript可以访问其他的源,包括其他的文件和http,https等其他的源
        XWalkPreferences.setValue(XWalkPreferences.JAVASCRIPT_CAN_OPEN_WINDOW, true);//JAVASCRIPT_CAN_OPEN_WINDOW
        XWalkPreferences.setValue(XWalkPreferences.SUPPORT_MULTIPLE_WINDOWS, true);// enable multiple windows.

        //设置滑动样式。。。
        XWalkView mXwview = new XWalkView(MainActivity.this);
        mXwview.setHorizontalScrollBarEnabled(false);
        mXwview.setVerticalScrollBarEnabled(false);
        mXwview.setScrollBarStyle(XWalkView.SCROLLBARS_OUTSIDE_INSET);
        mXwview.setScrollbarFadingEnabled(true);

        XWalkSettings settings = web_home.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setLayoutAlgorithm(XWalkSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局

        web_home.addJavascriptInterface(new Object() {
            @org.xwalk.core.JavascriptInterface
            public void Interactive(String str) {
                Log.e("syw", "str:" + str);
                if ("ys_me".equals(str)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, MeActivity.class);
                            startActivity(intent);
                        }
                    });
                } else if (str.contains("ys_video")) {
                    tempStr = str;
                    Message msg = new Message();
                    msg.what = 2000;
                    handler.sendMessage(msg);
                } else if (str.contains("ys_notice")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, SendNotifyActivity.class);
                            startActivity(intent);
                        }
                    });
                } else if (str.contains("ys_hd")) {
                    String video_url = str.substring(str.indexOf(",") + 1, str.length());
                    Uri uri = Uri.parse(video_url + "?tbkt_token=" + PreferencesManager.getInstance().getString("sessionid", ""));
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else if (str.contains("ys_nouser")) {
                    checkOut();

                } else if (str.contains("ys_scan")) {
                    //打开扫描界面扫描条形码或二维码
                    Intent openCameraIntent = new Intent(MainActivity.this, CaptureActivity.class);
                    startActivityForResult(openCameraIntent, 0);
                } else if (str.contains("ys_share")) {
                    share_url = str.substring(str.indexOf(",") + 1, str.indexOf("|"));
                    title = str.substring(str.indexOf("|") + 1);
                    handler.sendEmptyMessage(YS_SHARE);
                } else if (str.contains("ys_back")) {
                    Message msg = new Message();
                    msg.what = 1000;
                    handler.sendMessage(msg);
                } else if (str.contains("ys_cleanhistory")) {
                    handler.sendEmptyMessage(YS_CLEAN);
                } else if (str.contains("ys_isexit")) {
                    if (str.contains("true")) {
                        isExit = true;
                    } else {
                        isExit = false;
                    }
                }else if (str.contains("ys_playaudio,")) {
                    String audio_url = str.substring(str.indexOf(",") + 1, str.length());
                    initAudio();
                    playAudio(audio_url);
                }else if (str.contains("ys_playaudiopause")) {
                    if (musicPlayer != null) {
                        musicPlayer.pause();
                    }
                } else if (str.contains("ys_playaudiogoon")) {
                    if (musicPlayer != null) {
                        musicPlayer.start();
                    }
                } else if (str.contains("ys_playaudiostop")) {
                    if (musicPlayer != null) {
                        musicPlayer.stop();
                        musicPlayer.release();
                        musicPlayer = null;
                    }
                }

            }
        }, "appobject");

        HOME_URL = PreferencesManager.getInstance().getString("vuetea", "https://teacomjs.m.jxtbkt.com") + "/?t=" + System.currentTimeMillis()
                + "&tbkt_token=" + PreferencesManager.getInstance().getString("sessionid", "")+"&platform=3&version="
                + Tools.getAppVersion(MainActivity.this);
        ;
        Log.e("syw", "onCreate：HOME_URL:" + HOME_URL);
        loadUrl(HOME_URL);
    }

    //    syw 播放h5传递的视频
    MediaPlayer musicPlayer;

    private void initAudio() {
        if (musicPlayer == null) {
            musicPlayer = new MediaPlayer();
        }
        musicPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });

        musicPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (musicPlayer != null) {
                    musicPlayer.stop();
                    musicPlayer.release();
                    musicPlayer = null;
                }
//syw 回调h5的方法
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        web_home.load("javascript:playfinish();", null);
                    }
                });
            }
        });
    }

    private void playAudio(String audioUrl) {
        try {
            musicPlayer.reset();
            musicPlayer.setDataSource(audioUrl);
            musicPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void ShareWeb(String thumb_img) {
        UMImage thumb = new UMImage(MainActivity.this, R.mipmap.ic_launcher);
        UMWeb web = new UMWeb(thumb_img);
        web.setThumb(thumb);
        web.setDescription(title);
        web.setTitle("同步课堂");
        new ShareAction(MainActivity.this)
                .withMedia(web)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                .setCallback(umShareListener)
                .open();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            isFlush=false;
            web_home.load("javascript:shareover();", null);
            Toast.makeText(MainActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            isFlush=false;
            if (t != null) {
                Log.e("syw", "t.getMessage():" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            isFlush=false;
//            Toast.makeText(MainActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    private void checkOut() {
        // 保存退出状态 0为退出状态 1为登录状态
        PreferencesManager.getInstance().putString("isExist1", "0");
        PreferencesManager.getInstance().putInt("user_id", 0);
        PreferencesManager.getInstance().putString("sessionid", "");
        PreferencesManager.getInstance().putString("tea_name", "");
        PreferencesManager.getInstance().putBoolean("bindDevice", false);
        PreferencesManager.getInstance().putString("sub_id", "");//清空教师数据
        Intent i = new Intent(this, WebActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("syw", "onResume--isExit:" + isExit);
        if (isFlush) {
            HOME_URL = PreferencesManager.getInstance().getString("vuetea", "https://teacomjs.m.jxtbkt.com") + "/?t=" + System.currentTimeMillis() + "&tbkt_token="
                    + PreferencesManager.getInstance().getString("sessionid", "")+"&platform=3&version="
                    + Tools.getAppVersion(MainActivity.this);;

            Log.e("syw", "onResume：HOME_URL:" + HOME_URL);
            loadUrl(HOME_URL);
        }
        if (web_home != null) {
            web_home.resumeTimers();
            web_home.onShow();
        }
        isFlush = false;
    }

    private void loadUrl(String url) {
        if (!NetworkStatueUtil.isConnectInternet(this)) {
            fail_layout.setVisibility(View.VISIBLE);
            return;
        }

        web_home.setResourceClient(new XWalkResourceClient(web_home) {

            @Override
            public boolean shouldOverrideUrlLoading(XWalkView view, String url) {
//                syw 无论是加载错误，还是正常加载，记录加载的url，判断刷新和断网之后加载
                urlToReload = url;
                web_home.load(url, null);
                if (url.contains("ys_me")) {
                    Intent intent = new Intent(MainActivity.this, MeActivity.class);
                    startActivity(intent);
                }
                return true;
            }

            @Override
            public void onReceivedLoadError(XWalkView view, int errorCode, String description, String failingUrl) {
                super.onReceivedLoadError(view, errorCode, description, failingUrl);

                urlToReload = failingUrl;
                fail_layout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadFinished(XWalkView view, String url) {
                super.onLoadFinished(view, url);

                if (NetworkStatueUtil.isConnectInternet(MainActivity.this)) {
                    fail_layout.setVisibility(View.GONE);
                } else {
                    fail_layout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoadStarted(XWalkView view, String url) {
                super.onLoadStarted(view, url);
                xWalkNavigationHistory = web_home.getNavigationHistory();
            }

            @Override
            public void onReceivedClientCertRequest(XWalkView view, ClientCertRequest handler) {
                super.onReceivedClientCertRequest(view, handler);
            }

            @Override
            public void onReceivedSslError(XWalkView view, ValueCallback<Boolean> callback, SslError error) {
                super.onReceivedSslError(view, callback, error);
            }
        });
        web_home.load(url, null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reload_btn:
                loadUrl(HOME_URL);
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (web_home != null) {
            web_home.onNewIntent(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 0) {
            Bundle bundle = data.getExtras();
            if (bundle == null) {
                String result = data.getStringExtra("result");
                if ("xcp".equals(result)) {
                    web_home.load(PreferencesManager.getInstance().getString("vuestuxcps", "https://stuxcpjs.m.jxtbkt.com") + "/?tbkt_token="
                            + PreferencesManager.getInstance().getString("sessionid", ""), null);
                }
            } else {
                String scanResult = bundle.getString("result");
                String qr = PreferencesManager.getInstance().getString("qr", "tbkt.cn");
                if (!"xcp".equals(scanResult)) {
                    if (scanResult.contains("?")) {
                        scanResult = scanResult + "&tbkt_token=" + PreferencesManager.getInstance().getString("sessionid", "");
                    } else {
                        scanResult = scanResult + "?tbkt_token=" + PreferencesManager.getInstance().getString("sessionid", "");
                    }
                } else {
                    scanResult = PreferencesManager.getInstance().getString("vuestuxcps", "https://stuxcpjs.m.jxtbkt.com") + "/?tbkt_token="
                            + PreferencesManager.getInstance().getString("sessionid", "");
                }

                if (!scanResult.contains(qr)) {
                    Log.e("syw", "扫描结果:" + scanResult);
                    MyToastUtils.toastText(MainActivity.this, "无效的二维码");
                    return;
                }

                Log.e("syw", "scanResult:" + scanResult);
                web_home.load(scanResult, null);
            }
        }
    }

    private XWalkNavigationHistory xWalkNavigationHistory;//webview的历史记录
    private int vvv;//webview的历史记录个数
    public static boolean isExit;

    //调用双击退出函数
    @SuppressLint("NewApi")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.e("syw", "isExit:" + isExit);
            if (isExit) {
                isExit = false;
                Intent intent = new Intent(this, MeActivity.class);
                startActivity(intent);
                Log.e("syw", "onKeyDown--isExit:" + isExit);
            } else {
                vvv = xWalkNavigationHistory.getCurrentIndex();
                Log.e("syw","vvv:"+vvv);
                if (vvv == 0) {
                    exitBy2Click();
                }
            }
            return true;
        }
        return false;
    }

    public static String version;
    public static void loadUrlFromMe(String url) {
        if ("HOME_URL".equals(url)) {
            web_home.load(PreferencesManager.getInstance().getString("vuetea", "https://teacomjs.m.jxtbkt.com") + "/?t=" + System.currentTimeMillis() + "&tbkt_token="
                    + PreferencesManager.getInstance().getString("sessionid", "")+"&platform=3&version="
                    + version, null);
            isExit = false;
        } else {
            web_home.load(url, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (web_home != null) {
//            web_home.pauseTimers();
//            web_home.onHide();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        urlToReload = HOME_URL;
        if (web_home != null) {
            web_home.onDestroy();
        }
    }
}