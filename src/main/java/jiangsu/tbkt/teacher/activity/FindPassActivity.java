package jiangsu.tbkt.teacher.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import jiangsu.tbkt.teacher.R;
import jiangsu.tbkt.teacher.api.RequestServer;
import jiangsu.tbkt.teacher.application.ResultCode;
import jiangsu.tbkt.teacher.bean.ResultBean;
import jiangsu.tbkt.teacher.utils.Constant;
import jiangsu.tbkt.teacher.utils.MyToastUtils;

/**
 * 找回密码页面
 */
public class FindPassActivity extends BaseActivity implements View.OnClickListener {
    private String TAG = "RegisteSMSActivity";

    private TextView top_infotxt;
    private ImageView top_btnback;
    private EditText add_count, add_pass;
    private Button reGetPassBtn, putInfoBtn;
    String countText, passText;
    private CharSequence temp;
    private boolean justResult = false;

    private int mimute = 60;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.sendEmptyMessage(ResultCode.UPDATE);
        }
    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ResultCode.UPDATE:
                    mimute--;
                    if (mimute <= 0) {
                        handler.removeCallbacks(runnable);
                        mimute = 60;
                        reGetPassBtn.setEnabled(true);
                        reGetPassBtn.setText("获取验证码");
                        reGetPassBtn.setTextColor(Color.WHITE);
                        add_count.setEnabled(true);

                    } else {
                        reGetPassBtn.setEnabled(false);
                        reGetPassBtn.setText(mimute + "秒后重新获取");
                        reGetPassBtn.setTextColor(Color.parseColor("#aaaaaa"));
                        handler.postDelayed(runnable, 1000);
                    }

                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacks(runnable);
            handler = null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pass);
        init();

    }

    private void init() {
        top_infotxt = (TextView) findViewById(R.id.top_infotxt);
        top_infotxt.setText("找回密码");
        top_btnback = (ImageView) findViewById(R.id.top_btnback);
        top_btnback.setOnClickListener(this);


        add_count = (EditText) findViewById(R.id.add_count);
        add_pass = (EditText) findViewById(R.id.add_pass);

        reGetPassBtn = (Button) findViewById(R.id.reGetPassBtn);
        putInfoBtn = (Button) findViewById(R.id.putInfoBtn);
        reGetPassBtn.setOnClickListener(this);
        putInfoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_btnback:
                this.finish();
                break;
            case R.id.reGetPassBtn:
                if (TextUtils.isEmpty(add_count.getText().toString().trim())) {
                    MyToastUtils.toastText(this,"手机号不能为空");
                    return;
                }
                getPassHttpData(add_count.getText().toString());

                break;
            case R.id.putInfoBtn:
                if (TextUtils.isEmpty(add_count.getText().toString().trim())) {
                    MyToastUtils.toastText(this,"手机号不能为空");
                    return;
                }

                if (TextUtils.isEmpty(add_pass.getText().toString().trim())) {
                    MyToastUtils.toastText(this,"验证码不能为空");
                    return;
                }
                getNewPass(add_count.getText().toString(), add_pass.getText().toString());
                break;
            default:
                break;
        }
    }




    /**
     * 获取短信验证码
     *
     * @param phoneNum
     */
    public void getPassHttpData(String phoneNum) {
        httpurl = Constant.getAccountSMSpass;
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("phone_number",phoneNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestServer.getResultBean(FindPassActivity.this, httpurl, jsonObject.toString(), new RequestServer.Callback() {
            @Override
            public void onSuccess(Object object) {
                ResultBean resultBean = (ResultBean) object;
                if ("ok".equals(resultBean.getResponse())) {
                    handler.sendEmptyMessage(ResultCode.UPDATE);
                    add_count.setEnabled(false);
                }
            }

            @Override
            public void onFail(Object object) {
            }
        }, true, true, true);
    }


    //getNewPass
    public void getNewPass(String phoneNum, String pass) {
        httpurl = Constant.getNewPass;

        JSONObject jsonRes;
        JSONObject params = new JSONObject();
        try {
            params.put("phone_number", phoneNum);
            params.put("code", pass);

            RequestServer.getNewPassWord(FindPassActivity.this, httpurl, params.toString(), new RequestServer.Callback() {
                @Override
                public void onSuccess(Object object) {
//                    ResultBean resultBean= (ResultBean) object;
                    MyToastUtils.toastText(FindPassActivity.this,"密码将发送到您手机上，请注意查收");
                    finish();
                }

                @Override
                public void onFail(Object object) {

                }
            }, true, true, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
