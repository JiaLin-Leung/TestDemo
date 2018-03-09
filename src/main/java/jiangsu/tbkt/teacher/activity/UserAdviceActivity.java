package jiangsu.tbkt.teacher.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import jiangsu.tbkt.teacher.R;
import jiangsu.tbkt.teacher.api.RequestServer;
import jiangsu.tbkt.teacher.application.PreferencesManager;
import jiangsu.tbkt.teacher.bean.ResultBean;
import jiangsu.tbkt.teacher.utils.Constant;
import jiangsu.tbkt.teacher.utils.MyToastUtils;

public class UserAdviceActivity extends BaseActivity implements View.OnClickListener {
    private TextView top_infotxt, text_number;
    private ImageView top_btnback;
    private Button btn_submit;
    private EditText ad_edittext;
    private String kindStr = "1";
    private CharSequence temp;
    String str_text;
    private int selectionStart;
    private int selectionEnd;
//    private RadioButton ad_advice = null;
//    private RadioButton ad_bug = null;
//    private RadioButton ad_other = null;
    private int editStart;
    private int editEnd;
    String str = "";
    public static Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice_layout);
        init();
    }

    /**
     * 初始化数据
     */
    @SuppressLint("NewApi")
    public void init() {

        top_infotxt = (TextView) findViewById(R.id.top_infotxt);
        top_infotxt.setText("建议反馈");

//        syw 取消客服电话显示
        TextView cs_phone = (TextView) findViewById(R.id.cs_phone);
        TextView kefu = (TextView) findViewById(R.id.kefu);
        String phone = PreferencesManager.getInstance().getString("cs_phone", "");

        if ("".equals(phone)) {
            kefu.setVisibility(View.INVISIBLE);
            cs_phone.setVisibility(View.INVISIBLE);
        } else {
            kefu.setVisibility(View.VISIBLE);
            cs_phone.setVisibility(View.VISIBLE);
            cs_phone.setText(phone);
        }

        top_btnback = (ImageView) findViewById(R.id.top_btnback);
        text_number = (TextView) findViewById(R.id.text_number);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        top_btnback.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

//        ad_advice = (RadioButton) findViewById(R.id.ad_advice);
//        ad_bug = (RadioButton) findViewById(R.id.ad_bug);
//        ad_other = (RadioButton) findViewById(R.id.ad_other);

//        ad_advice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                kindStr = "1";
//                ad_advice.setChecked(true);
//                ad_bug.setChecked(false);
//                ad_other.setChecked(false);
//            }
//        });
//
//        ad_bug.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                kindStr = "2";
//                ad_advice.setChecked(false);
//                ad_bug.setChecked(true);
//                ad_other.setChecked(false);
//            }
//        });
//
//        ad_other.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                kindStr = "3";
//                ad_advice.setChecked(false);
//                ad_bug.setChecked(false);
//                ad_other.setChecked(true);
//            }
//        });

        ad_edittext = (EditText) findViewById(R.id.ad_edittext);

        ad_edittext.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
                temp = s;
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editStart = ad_edittext.getSelectionStart();
                editEnd = ad_edittext.getSelectionEnd();
                str_text = ad_edittext.getText().toString();
                String str = String.valueOf(temp).trim();
                if (str.length() > 200) {

                    if (toast == null) {
                        toast = Toast.makeText(UserAdviceActivity.this, "字数不能超过200字", Toast.LENGTH_SHORT);
                    } else {
                        toast.setText("字数不能超过200字");
                        toast.setDuration(Toast.LENGTH_SHORT);
                    }

                    toast.show();

                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    ad_edittext.setText(s);
                    ad_edittext.setSelection(tempSelection);
                } else {
                    int leftnum = 200 - str.length();
                    text_number.setText("还可输入" + leftnum + "字");
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:

                if ("".equals(ad_edittext.getText().toString().trim())) {
                    Toast.makeText(this, "反馈内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if (ad_edittext.getText().toString().trim().length() > 200) {
                    Toast.makeText(this, "字数不能超过200字", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    submitData(Constant.adviceInterf);
                }
                break;

            case R.id.top_btnback:
                this.finish();
                break;
            default:
                break;
        }
    }

    /**
     * 提交建议
     */
    public void submitData(final String url) {
        try {
            JSONObject params = new JSONObject();
            params.put("type", kindStr);
            params.put("content", String.valueOf(temp));
            params.put("app", "android");
            RequestServer.getResultBean(UserAdviceActivity.this, url, params.toString(), new RequestServer.Callback() {
                @Override
                public void onSuccess(Object object) {
                    ResultBean resultBean = (ResultBean) object;
                    if ("ok".equals(resultBean.getResponse())) {
                        MyToastUtils.toastText(UserAdviceActivity.this, "提交成功");
                        ad_edittext.setText("");
                        finish();
                    }
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
