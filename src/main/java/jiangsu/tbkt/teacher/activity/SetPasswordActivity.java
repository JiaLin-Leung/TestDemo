package jiangsu.tbkt.teacher.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import jiangsu.tbkt.teacher.R;
import jiangsu.tbkt.teacher.api.RequestServer;
import jiangsu.tbkt.teacher.application.AppManager;
import jiangsu.tbkt.teacher.application.PreferencesManager;
import jiangsu.tbkt.teacher.utils.Constant;
import jiangsu.tbkt.teacher.utils.MyToastUtils;

/**
 * Created by song on 2016/8/11 0011.
 */
public class SetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_newword1, et_newword2;
//    private EditText et_curword;
    private ImageView top_btnback;
    private TextView top_infotxt;
    private Button bt_confirm;
    private String oldword;
    private String newword1;
    private String newword2;
    private String saveword;
    private ProgressDialog pd;
//    private StudentSharedPreferences preferences;
//    private View view1;
//    private View view2;
//    private View view3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setpassword);
        initView();
    }

    private void initView() {
//        et_curword = (EditText) findViewById(R.id.et_curword);
        et_newword1 = (EditText) findViewById(R.id.et_newword1);
        et_newword2 = (EditText) findViewById(R.id.et_newword2);
//        view1 = findViewById(R.id.view1);
//        view2 = findViewById(R.id.view2);
//        view3 = findViewById(R.id.view3);
//        et_curword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if (b) {
//                    view1.setBackgroundColor(Color.parseColor("#C3EDFC"));
//                } else {
//                    view1.setBackgroundColor(Color.parseColor("#dcdcdc"));
//                }
//            }
//        });
//
//        et_newword1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if (b) {
//                    view2.setBackgroundColor(Color.parseColor("#C3EDFC"));
//                } else {
//                    view2.setBackgroundColor(Color.parseColor("#dcdcdc"));
//                }
//            }
//        });
//
//        et_newword2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if (b) {
//                    view3.setBackgroundColor(Color.parseColor("#C3EDFC"));
//                } else {
//                    view3.setBackgroundColor(Color.parseColor("#dcdcdc"));
//                }
//            }
//        });

        //修改标题头
        top_infotxt = (TextView) findViewById(R.id.top_infotxt);
        top_infotxt.setText("修改密码");
        //返回按钮
        top_btnback = (ImageView) findViewById(R.id.top_btnback);
        top_btnback.setOnClickListener(this);
        //确认按钮
        bt_confirm = (Button) findViewById(R.id.bt_confirm1);
//        bt_confirm.setVisibility(View.VISIBLE);
        bt_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_btnback:
                SetPasswordActivity.this.finish();
                break;
            case R.id.bt_confirm1:
                //需要提交数据
//                oldword = et_curword.getText().toString().trim();
                newword1 = et_newword1.getText().toString().trim();
                newword2 = et_newword2.getText().toString().trim();
                saveword = PreferencesManager.getInstance().getString("pass", "");
//                if (!saveword.equals(oldword)) {
//                    MyToastUtils.toastText(SetPasswordActivity.this, "当前密码错误");
//                    return;
//                } else
                if ("".equals(newword1)||"".equals(newword2)){
                    MyToastUtils.toastText(SetPasswordActivity.this, "密码不能为空");
                    return;
                }else if (!newword1.equals(newword2)) {
                    MyToastUtils.toastText(SetPasswordActivity.this, "两次输入密码不一致");
                    return;
                }else if (newword1.length() < 6 || newword1.length() > 16) {
                    MyToastUtils.toastText(SetPasswordActivity.this, "密码长度为6-16个字符");
                    return;
                }else {
                    updatePassword();
                }
                break;
            default:

                break;
        }
    }

    private void updatePassword() {
        JSONObject params = new JSONObject();
        try {
            params.put("old_pwd", saveword);
            params.put("new_pwd", newword1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestServer.getResultBean(this, Constant.resetPwd, params.toString(), new RequestServer.Callback() {
            @Override
            public void onSuccess(Object object) {
                MyToastUtils.toastText(SetPasswordActivity.this,"密码修改成功,请重新登录");
                AppManager.getAppManager().finishSpecailActivity(MainActivity.class);
                Intent intent=new Intent(SetPasswordActivity.this,LoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFail(Object object) {
                MyToastUtils.toastText(SetPasswordActivity.this,"密码修改失败");
            }
        }, true, true, true);
    }
}
