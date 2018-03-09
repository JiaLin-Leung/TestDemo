package jiangsu.tbkt.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import jiangsu.tbkt.teacher.R;
import jiangsu.tbkt.teacher.api.RequestServer;
import jiangsu.tbkt.teacher.application.PreferencesManager;
import jiangsu.tbkt.teacher.bean.SelectClassBean;
import jiangsu.tbkt.teacher.picker.ChooseCityInterface;
import jiangsu.tbkt.teacher.picker.ChooseCityUtil;
import jiangsu.tbkt.teacher.picker.ChooseClassUtil;
import jiangsu.tbkt.teacher.picker.CityBean;
import jiangsu.tbkt.teacher.utils.Constant;
import jiangsu.tbkt.teacher.utils.MyToastUtils;
import jiangsu.tbkt.teacher.view.MarqueeTextView;

/**
 * Created by song on 2017/1/13 0013.
 */
public class SuppleSchoolActivity extends BaseActivity implements View.OnClickListener {

    TextView tv_city, tv_school, tv_class;
    LinearLayout ll_city, ll_school, ll_class;
    MarqueeTextView top_infotxt;
    ImageView top_btnback;
    String schoolName;
    Button bt_submit;
    int buMenId, nianJiId, banJiId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school);
        schoolName = "选择学校";
        //syw 清除学校区域
        PreferencesManager.getInstance().putString("youzhengbianma", "");
        PreferencesManager.getInstance().putString("school_name", "选择学校");
        PreferencesManager.getInstance().putInt("school_id", 0);

        initView();
        initListener();
    }


    private void initView() {
        ll_city = (LinearLayout) findViewById(R.id.ll_city);
        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                syw修改文字内容
                tv_school.setText("选择学校");
                tv_class.setText("选择班级");
//                syw清空学校信息
                PreferencesManager.getInstance().putInt("school_id", 0);
                PreferencesManager.getInstance().putString("school_name", "选择学校");
//                syw清空班级信息
                buMenId = 0;
                nianJiId = 0;
                banJiId = 0;
            }
        });

        ll_school = (LinearLayout) findViewById(R.id.ll_school);
        tv_school = (TextView) findViewById(R.id.tv_school);
        tv_school.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tv_class.setText("选择班级");
//              syw清空班级信息
                buMenId = 0;
                nianJiId = 0;
                banJiId = 0;
            }
        });

        ll_class = (LinearLayout) findViewById(R.id.ll_class);
        tv_class = (TextView) findViewById(R.id.tv_class);

        bt_submit = (Button) findViewById(R.id.bt_submit);

        top_infotxt = (MarqueeTextView) findViewById(R.id.top_infotxt);
        top_infotxt.setText("完善学校信息");
        top_btnback = (ImageView) findViewById(R.id.top_btnback);
    }

    private void initListener() {
        ll_city.setOnClickListener(this);
        ll_school.setOnClickListener(this);
        ll_class.setOnClickListener(this);
        bt_submit.setOnClickListener(this);
        top_btnback.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        schoolName = PreferencesManager.getInstance().getString("school_name", "选择学校");
        tv_school.setText(schoolName);
    }

    String youzhengbianma="";
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_city:
                getAreaDate();
                break;
            case R.id.ll_school:
                youzhengbianma = PreferencesManager.getInstance().getString("youzhengbianma", "");
                if ("".equals(youzhengbianma)) {
                    MyToastUtils.toastText(SuppleSchoolActivity.this, "请先选择地市");
                    return;
                }
                Intent intent = new Intent(this, ChoiceSchoolActivity.class);
                startActivity(intent);
                break;

            case R.id.ll_class:
                getClassData();
                break;

            case R.id.bt_submit:
                youzhengbianma = PreferencesManager.getInstance().getString("youzhengbianma", "");
                if ("".equals(youzhengbianma)){
                    MyToastUtils.toastText(SuppleSchoolActivity.this, "请先选择地市");
                }else if ("选择学校".equals(schoolName)){
                    MyToastUtils.toastText(SuppleSchoolActivity.this, "请先选择学校");
                }else if (buMenId == 0 || nianJiId == 0) {
                    MyToastUtils.toastText(this, "请先选择班级");
                } else {
                    submitSchoolInfo(buMenId, nianJiId, banJiId);
                }
                break;

            case R.id.top_btnback:
                finish();
                break;
        }
    }

    /**
     * 获取县区信息
     */
    private void getAreaDate() {
        JSONObject params = new JSONObject();
        try {
            params.put("child", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestServer.getAreaData(this, Constant.areaInterf, params.toString(), new RequestServer.Callback() {
            @Override
            public void onSuccess(Object object) {
                CityBean bean = (CityBean) object;

                final ChooseCityUtil cityUtil = new ChooseCityUtil();
                String[] oldCityArray = tv_city.getText().toString().split("-");
                if (oldCityArray.length != 2) {
                    oldCityArray = new String[]{"郑州市", "中原区"};
                }

                cityUtil.createDialog(SuppleSchoolActivity.this, bean, tv_city, oldCityArray, new ChooseCityInterface() {
                    @Override
                    public void sure(String[] newCityArray) {
                        tv_city.setText(newCityArray[0] + "-" + newCityArray[1]);
                    }
                });
            }

            @Override
            public void onFail(Object object) {

            }
        }, true, true, true);
    }

    private void getClassData() {
        int school_id = PreferencesManager.getInstance().getInt("school_id", 0);
        if (school_id == 0) {
            MyToastUtils.toastText(this, "请先选择学校");
            return;
        }
        JSONObject params = new JSONObject();
        try {
            params.put("school_id", school_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestServer.getClassData(this, Constant.classInterf, params.toString(), new RequestServer.Callback() {
            @Override
            public void onSuccess(Object object) {
                SelectClassBean bean = (SelectClassBean) object;

                final ChooseClassUtil cityUtil = new ChooseClassUtil();
                cityUtil.createDialog(SuppleSchoolActivity.this, bean, tv_city, new ChooseCityInterface() {
                    @Override
                    public void sure(String[] newCityArray) {
                        tv_class.setText(newCityArray[0] + "-" + newCityArray[1] + "-" + newCityArray[2]);
                        //syw 提交学校信息
                        buMenId = PreferencesManager.getInstance().getInt("buMenId", 0);
                        nianJiId = PreferencesManager.getInstance().getInt("nianJiId", 0);
                        banJiId = PreferencesManager.getInstance().getInt("banJiId", 0);
                    }
                });
            }

            @Override
            public void onFail(Object object) {

            }
        }, true, true, true);

    }

    public void submitSchoolInfo(int buMenId, int nianJiId, int banJiId) {

        JSONObject params = new JSONObject();
        try {
            params.put("dept_id", buMenId);
            params.put("grade_id", nianJiId);
            params.put("class_id", banJiId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("syw", "dept_id" + buMenId + "grade_id" + nianJiId + "class_id" + banJiId);
        RequestServer.getResultBean(this, Constant.submitSchInterf, params.toString(), new RequestServer.Callback() {
            @Override
            public void onSuccess(Object object) {
                Intent intent = new Intent(SuppleSchoolActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFail(Object object) {

            }
        }, true, true, true);
    }
}
