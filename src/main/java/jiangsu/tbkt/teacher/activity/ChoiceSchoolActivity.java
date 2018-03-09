package jiangsu.tbkt.teacher.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import jiangsu.tbkt.teacher.R;
import jiangsu.tbkt.teacher.api.RequestServer;
import jiangsu.tbkt.teacher.application.PreferencesManager;
import jiangsu.tbkt.teacher.bean.SchoolBean;
import jiangsu.tbkt.teacher.utils.Constant;
import jiangsu.tbkt.teacher.utils.MyToastUtils;
import jiangsu.tbkt.teacher.view.MarqueeTextView;

/**
 * Created by song on 2017/1/13 0013.
 */
public class ChoiceSchoolActivity extends BaseActivity {

    EditText et_search;
    ListView lv_school;
    long lastTime;
    long currTime;
    List<SchoolBean.DataBean> schools;
    ImageView iv_clear;
    TextView tv_noschool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_school);

        initView();
    }

    private void initView() {
        MarqueeTextView top_infotxt = (MarqueeTextView) findViewById(R.id.top_infotxt);
        top_infotxt.setText("选择学校");

        tv_noschool = (TextView) findViewById(R.id.tv_noschool);

        ImageView top_btnback = (ImageView) findViewById(R.id.top_btnback);
        top_btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        et_search = (EditText) findViewById(R.id.et_search);
        lv_school = (ListView) findViewById(R.id.lv_school);
        iv_clear = (ImageView) findViewById(R.id.iv_clear);
        iv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_search.setText("");
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if ("".equals(charSequence.toString())) {
                    iv_clear.setVisibility(View.INVISIBLE);
                } else {
                    iv_clear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                lastTime = System.currentTimeMillis();

                Message message = new Message();
                message.obj = editable.toString().trim();
                message.what = 1;
                handler.sendMessageDelayed(message, 500);
            }
        });

        lv_school.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (schools != null) {
                    SchoolBean.DataBean dataBean = schools.get(i);
                    // syw 将school_id保存下来，用于请求班级信息
                    PreferencesManager.getInstance().putInt("school_id", dataBean.getId());
                    PreferencesManager.getInstance().putString("school_name", dataBean.getName());
                    finish();
                }
            }
        });
        //开始的时候，没有关键字，填充列表
        searchSchool();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    currTime = System.currentTimeMillis();
                    if (currTime - lastTime > 500) {
                        searchSchool();
                    }
                    break;
            }
        }
    };


    private void searchSchool() {
        String schoolName = et_search.getText().toString().trim();
        String youzhengbianma = PreferencesManager.getInstance().getString("youzhengbianma", "");
        Log.e("syw", "searchSchool" + youzhengbianma);
        if ("".equals(youzhengbianma)) {
            MyToastUtils.toastText(ChoiceSchoolActivity.this, "请先选择地市");
            return;
        }
        JSONObject params = new JSONObject();
        try {
            params.put("county", youzhengbianma);
            params.put("keyword", schoolName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestServer.getSchoolListData(this, Constant.schoolListInterf, params.toString(), new RequestServer.Callback() {
            @Override
            public void onSuccess(Object object) {
                SchoolBean bean = (SchoolBean) object;
                schools = bean.getData();
                if (schools != null && schools.size() != 0) {
                    lv_school.setVisibility(View.VISIBLE);
                    tv_noschool.setVisibility(View.GONE);
                    lv_school.setAdapter(new SchoolAdapter());
                } else {
                    lv_school.setVisibility(View.GONE);
                    tv_noschool.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFail(Object object) {

            }
        }, true, true, true);
    }

    class SchoolAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return schools.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(ChoiceSchoolActivity.this, R.layout.item_school, null);
                holder.tv_school_name = (TextView) convertView.findViewById(R.id.tv_school_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            SchoolBean.DataBean dataBean = schools.get(i);
            holder.tv_school_name.setText(dataBean.getName());

            return convertView;
        }
    }

    class ViewHolder {
        TextView tv_school_name;
    }
}
