package jiangsu.tbkt.teacher.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import jiangsu.tbkt.teacher.R;
import jiangsu.tbkt.teacher.api.RequestServer;
import jiangsu.tbkt.teacher.application.AppManager;
import jiangsu.tbkt.teacher.application.PreferencesManager;
import jiangsu.tbkt.teacher.application.ResultCode;
import jiangsu.tbkt.teacher.bean.LoginResultBean;
import jiangsu.tbkt.teacher.bean.ResultBean;
import jiangsu.tbkt.teacher.bean.SettingManageBean;
import jiangsu.tbkt.teacher.bean.ShenFenBean;
import jiangsu.tbkt.teacher.utils.Constant;
import jiangsu.tbkt.teacher.utils.GlobalTools;
import jiangsu.tbkt.teacher.utils.MyToastUtils;
import jiangsu.tbkt.teacher.utils.NetworkStatueUtil;
import jiangsu.tbkt.teacher.utils.Tools;

/**
 * @Description: 登陆页面
 * @FileName: LoginActivity.java
 * @Package com.tbkt.student.set.activity
 * @Author wangxiao
 * @Date 2015-1-5
 * @Version V1.0
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private Context context;
    private Intent intent;
    private EditText login_pasw;
    AutoCompleteTextView login_account;
    private String accountTxt;
    private String passwTxt;
    private Button login_btn;
    public ProgressDialog mProgressDialog;
    private ImageView iv_clear;
    private ImageView iv_clear1;

    private PopupWindow logerrWindow;
    private PopupWindow checkOutWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    /***
     * 初始化控件和数据
     */
    @SuppressWarnings("deprecation")
//    TextView tv_he;

    private void init() {
        context = this;

//        tv_he = (TextView) findViewById(tv_he);
//        tv_he.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, WebActivity.class);
//                startActivity(intent);
//            }
//        });

//        syw 隐藏客服电话
        TextView cs_phone = (TextView) findViewById(R.id.cs_phone);
        String phone = PreferencesManager.getInstance().getString("cs_phone", "");
        if ("".equals(phone)) {
            cs_phone.setVisibility(View.GONE);
        } else {
            cs_phone.setVisibility(View.VISIBLE);
            cs_phone.setText("客服电话：" + phone);
        }


        login_account = (AutoCompleteTextView) findViewById(R.id.login_account);
        login_pasw = (EditText) findViewById(R.id.login_pasw);


        iv_clear1 = (ImageView) findViewById(R.id.iv_clear1);
        iv_clear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_account.setText("");
            }
        });

        iv_clear = (ImageView) findViewById(R.id.iv_clear);
        iv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_pasw.setText("");
            }
        });

        login_pasw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    iv_clear.setVisibility(View.VISIBLE);
                } else {
                    iv_clear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        login_account.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                if (s.length() != 0) {
                    iv_clear1.setVisibility(View.VISIBLE);
                } else {
                    iv_clear1.setVisibility(View.GONE);
                }
//                temp = s;
//                List<String> items = new ArrayList<>();
//                items.add(login_account.getText().toString() + "xs");
//                items.add(login_account.getText().toString() + "js");
//                if (temp.length() == 11) {
//                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                            LoginActivity.this,
//                            android.R.layout.simple_dropdown_item_1line, items);
//                    login_account.setAdapter(adapter);
//                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                login_pasw.setText("");
            }
        });

//        TextView get_sms_pass = (TextView) findViewById(R.id.get_sms_pass);
//        get_sms_pass.setOnClickListener(this);
        login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        intent = new Intent();
        switch (view.getId()) {
            case R.id.login_btn:
                login();
                break;
//            case R.id.get_sms_pass:
//                jumpToPage(FindPassActivity.class);
//                break;
            default:
                break;
        }
    }


    /***
     * 登录逻辑处理
     */
    public void login() {
        accountTxt = login_account.getText().toString().trim();
        passwTxt = login_pasw.getText().toString().trim();
        if (TextUtils.isEmpty(accountTxt)) {
            MyToastUtils.toastText(this, "账号不能为空");
            return;
        }
        if ("".endsWith(passwTxt) || null == accountTxt) {
            MyToastUtils.toastText(this, "密码不能为空");
            return;
        }

        userLogin();
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ResultCode.SUCCESS:
                    if (msg.obj != null) {
                        MyToastUtils.toastText(LoginActivity.this, msg.obj.toString());
                    }
                    break;
                case ResultCode.FAIL:
                    if (msg.obj != null) {
                    }
                    break;
                case 1000:
                    choiceShenFen();
                    break;
            }
        }
    };

    private void choiceShenFen() {
        RequestServer.getShenFenData(this, Constant.shenFenInterf, null, new RequestServer.Callback() {
            @Override
            public void onSuccess(Object object) {
                ShenFenBean shenFenBean = (ShenFenBean) object;
                List<ShenFenBean.DataBean> data = shenFenBean.getData();
                if (data != null && data.size() == 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getAccountHttpData();
                        }
                    });
                } else {
                    Intent intent = new Intent(LoginActivity.this, ChoiceShenFenActivity.class);
                    intent.putExtra("shenFenBean", shenFenBean);
                    startActivity(intent);
                }
            }

            @Override
            public void onFail(Object object) {

            }
        }, true, true, true);
    }

    public void getAccountHttpData() {

        RequestServer.getSetData(this, Constant.subManageInterf, null, new RequestServer.Callback() {
            @Override
            public void onSuccess(Object object) {
                SettingManageBean settingManageBean = (SettingManageBean) object;
                List<SettingManageBean.UnitsBean> units = settingManageBean.getUnits();
                String name = settingManageBean.getName();
                //syw 只缺姓名
                String type = "";
                Intent intent = null;
                if (!"".equals(name) && units != null && units.size() != 0) {
                    intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
//                    syw 为做测试，把所有界面都展示出来
                    if ("".equals(name) && units != null && units.size() != 0) {
                        type = "name";
                    } else if (!"".equals(name) && units != null && units.size() == 0) {
                        type = "class";
                    } else if ("".equals(name) && units != null && units.size() == 0) {
                        type = "all";
                    }
                    intent = new Intent(LoginActivity.this, SuppleInfoActivity.class);
                    intent.putExtra("type", type);
                    startActivity(intent);
                }
            }

            @Override
            public void onFail(Object object) {

            }
        }, true, true, true);
    }

    /**
     * 用户登录，登录成功跳转到主页面
     */
    private void userLogin() {
        RequestServer.login(this, Constant.loginInterf, getLoginData(), new RequestServer.Callback() {
            @Override
            public void onSuccess(Object object) {
                LoginResultBean bean = (LoginResultBean) object;
                if (bean.getResultBean().getResponse().equalsIgnoreCase("ok")) {
//                    PreferencesManager.getInstance().putString("isExist", "1");//登录成功，保存登录状态
                    PreferencesManager.getInstance().putString("name", str_username);
                    PreferencesManager.getInstance().putString("pass", str_password);
                    PreferencesManager.getInstance().putString("school_type", bean.getSchool_type());
                    GlobalTools.setMessage(LoginActivity.this, bean.getResultBean().getMessage());

                    Message message = new Message();
                    message.what = 1000;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onFail(Object object) {
                if (NetworkStatueUtil.isConnectInternet(LoginActivity.this)) {
                    ResultBean bean = (ResultBean) object;
                    String message = bean.getMessage();
                    if (message.contains("账号或密码错误")) {
//                        showErrorPopWindow();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyToastUtils.toastText(LoginActivity.this, "账号或密码错误");
                            }
                        });
                        return;
                    }

                    if ("switch".equals(bean.getError())) {
                        Message errMsg = new Message();
                        errMsg.what = 1000;
                        handler.sendMessage(errMsg);
                    }
                }
            }
        }, true, true, true);
    }


    private void showErrorPopWindow() {
        View convertview = View.inflate(this, R.layout.pop_checkout, null);
        checkOutWindow = new PopupWindow(convertview, ViewGroup.LayoutParams.MATCH_PARENT, -2, true);
//        syw设置内容
        TextView content = (TextView) convertview.findViewById(R.id.content);
        content.setText("账号或密码错误，是否找回密码？");
        //取消和退出的监听
        TextView tv_quxiao_tuichu = (TextView) convertview.findViewById(R.id.tv_quxiao_tuichu);
        tv_quxiao_tuichu.setText("找回密码");
        tv_quxiao_tuichu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkOutWindow != null && checkOutWindow.isShowing()) {
                    checkOutWindow.dismiss();
                }
                jumpToPage(FindPassActivity.class);
            }
        });
        TextView tv_tuichu = (TextView) convertview.findViewById(R.id.tv_tuichu);
        tv_tuichu.setText("重新输入");
        tv_tuichu.setTextColor(Color.parseColor("#5e89ef"));
        tv_tuichu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkOutWindow != null && checkOutWindow.isShowing()) {
                    checkOutWindow.dismiss();
                }
            }
        });

        checkOutWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        if (Build.VERSION.SDK_INT < 24) {
            checkOutWindow.showAtLocation(login_pasw, Gravity.CENTER, 0, 0);
        } else {
            int distance = getWinHeight() - getViewHeight(convertview);
            checkOutWindow.showAtLocation(login_pasw, Gravity.NO_GRAVITY, 0, distance / 2);
        }

        checkOutWindow.setAnimationStyle(R.style.popwindow_anim_style);
        checkOutWindow.update();
        backgroundAlpha(0.5f);
        checkOutWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    public int getWinHeight() {
        WindowManager wm = getWindowManager();
        return wm.getDefaultDisplay().getHeight();
    }

    public int getViewHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredHeight();
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    /**
     * 获取登陆所需数据
     *
     * @return
     */
    //syw 英语教师端：添加login_type=86，添加版本号
    String str_username = "";
    String str_password = "";

    private String getLoginData() {
        // 获取用户输入的用户名、密码
        str_username = login_account.getText().toString().trim();
        str_password = login_pasw.getText().toString().trim();
        JSONObject params = new JSONObject();
        try {
            params.put("username", str_username);
            params.put("password", str_password);
            params.put("login_type", "10");
            params.put("version", Tools.getAndroidVersion());
            params.put("name", Tools.getAndroidName(this));
            params.put("model", Tools.getDeviceISN(this));
            params.put("platform", "Android");
            params.put("uuid", Tools.getDeviceType(this));
            params.put("appversion", Tools.getAppVersion(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params.toString();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();      //调用双击退出函数
        }
        return false;
    }


    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    protected void exitBy2Click() {
        Timer tExit = null;
        if (!isExit) {
            isExit = true; // 准备退出
            MyToastUtils.toastText(this, "再次点击退出");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;//取消退出
                }
            }, 2000); //如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            AppManager.getAppManager().AppExit(this);
            finish();
        }
    }

    private void hideWindow(PopupWindow window) {
        if (window != null && window.isShowing()) {
            window.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        hideWindow(logerrWindow);
        hideWindow(checkOutWindow);
        Log.e("syw", "LoginActivity销毁");
        super.onDestroy();
    }
}
