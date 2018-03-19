package jiangsu.tbkt.teacher.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

import jiangsu.tbkt.teacher.R;
import jiangsu.tbkt.teacher.application.PreferencesManager;
import jiangsu.tbkt.teacher.utils.MyToastUtils;
import jiangsu.tbkt.teacher.view.MyXwalkview;
import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/9/27 0027.
 */
public class WebActivity extends BaseActivity {
    private MyXwalkview webview;
    private OkHttpClient mHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webview = (MyXwalkview) findViewById(R.id.webview);

        initWebview();
        String url= PreferencesManager.getInstance().getString("other_login_url","https://edu.10086.cn/test-sso/login?service=https%3A%2F%2Fedu.10086.cn%2Ftest-oauth%2Foauth%2Fauthorize%3Fresponse_type%3Dcode%26client_id%3DaMNxVV16%26redirect_uri%3Dhttp%253A%252F%252Fgoqg.beta.tbkt.cn%253Ffrom%253Dwebstu1");
        url=url+"?t=t";
        Log.e("syw","url:"+url);
        webview.load(url, null);
    }

    private void initWebview() {
        //添加对javascript支持
        XWalkPreferences.setValue("enable-javascript", true);
        //开启调式,支持谷歌浏览器调式
        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
        //置是否允许通过file url加载的Javascript可以访问其他的源,包括其他的文件和http,https等其他的源
        XWalkPreferences.setValue(XWalkPreferences.ALLOW_UNIVERSAL_ACCESS_FROM_FILE, true);
        //JAVASCRIPT_CAN_OPEN_WINDOW
        XWalkPreferences.setValue(XWalkPreferences.JAVASCRIPT_CAN_OPEN_WINDOW, true);
        // enable multiple windows.
        XWalkPreferences.setValue(XWalkPreferences.SUPPORT_MULTIPLE_WINDOWS, true);
        //设置滑动样式。。。
        webview.setHorizontalScrollBarEnabled(false);
        webview.setVerticalScrollBarEnabled(false);
        webview.setScrollBarStyle(XWalkView.SCROLLBARS_OUTSIDE_INSET);
        webview.setScrollbarFadingEnabled(true);
        webview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webview.setUIClient(new XWalkUIClient(webview) {
            @Override
            public void onPageLoadStopped(XWalkView view, String url, LoadStatus status) {
                super.onPageLoadStopped(view, url, status);
            }

            @Override
            public void onFullscreenToggled(XWalkView view, boolean enterFullscreen) {
                super.onFullscreenToggled(view, enterFullscreen);

            }
        });

        webview.setResourceClient(new XWalkResourceClient(webview) {
            @Override
            public void onLoadStarted(XWalkView view, String url) {
                super.onLoadStarted(view, url);
            }

            @Override
            public void onProgressChanged(XWalkView view, int progressInPercent) {
                super.onProgressChanged(view, progressInPercent);
            }

            @Override
            public void onLoadFinished(XWalkView view, String url) {
                super.onLoadFinished(view, url);
            }
        });

        webview.addJavascriptInterface(new Object() {
            @org.xwalk.core.JavascriptInterface
            public void Interactive(String str) {
                Log.e("syw","str:"+str);
                if (str.contains("ys_accesstoken")) {
                    String token = str.substring(str.indexOf(",") + 1, str.length());
                    Log.e("syw", "tbkt_token:" + token);
                    PreferencesManager.getInstance().putString("sessionid", token);
                    PreferencesManager.getInstance().putString("isExist", "1");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            jumpToPage(MainActivity.class, null, true);
                        }
                    });

                } else if (str.contains("ys_notoken")) {
                    final String token = str.substring(str.indexOf(",") + 1, str.length());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyToastUtils.toastText(WebActivity.this,token);
                            finish();
                        }
                    });
                }
            }
        }, "appobject");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();      //调用双击退出函数
        }
        return false;
    }
}
