package jiangsu.tbkt.teacher.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import jiangsu.tbkt.teacher.R;
import jiangsu.tbkt.teacher.api.RequestServer;
import jiangsu.tbkt.teacher.application.AppManager;
import jiangsu.tbkt.teacher.bean.ResultBean;
import jiangsu.tbkt.teacher.bean.SubjectBean;
import jiangsu.tbkt.teacher.utils.Constant;
import jiangsu.tbkt.teacher.utils.MyToastUtils;
import jiangsu.tbkt.teacher.view.MarqueeTextView;


/**
 * Created by Administrator on 2017/11/1 0001.
 */
public class ChoiceSubjectActivity extends BaseActivity {
    ListView lv_subject;
    List<SubjectBean.DataBean> subjects;
    SubjectAdapter adapter;
    ImageView top_btnback;
    MarqueeTextView top_infotxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_subject);
        lv_subject = (ListView) findViewById(R.id.lv_subject);
        top_btnback = (ImageView) findViewById(R.id.top_btnback);

        top_infotxt = (MarqueeTextView) findViewById(R.id.top_infotxt);
        top_infotxt.setText("选择学科");

        top_btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSubjectData();
    }

    private void getSubjectData() {
        RequestServer.getSubjectData(this, Constant.getSubjectInterf, null, new RequestServer.Callback() {
            @Override
            public void onSuccess(Object object) {
                subjects = (List<SubjectBean.DataBean>) object;
                Log.e("syw","subjects:"+subjects.size());

                if (adapter==null){
                    adapter = new SubjectAdapter();
                }
                lv_subject.setAdapter(adapter);
            }

            @Override
            public void onFail(Object object) {

            }
        }, true, true, false);
    }

    class SubjectAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return subjects.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(ChoiceSubjectActivity.this, R.layout.item_subject, null);
                holder.tv_kemu = (TextView) convertView.findViewById(R.id.tv_kemu);
                holder.bt_shiyong = (Button) convertView.findViewById(R.id.bt_shiyong);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final SubjectBean.DataBean dataBean = subjects.get(position);
            holder.tv_kemu.setText(dataBean.getName());

            holder.bt_shiyong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switchSubject(dataBean);
                }
            });

            return convertView;
        }
    }

    private void switchSubject(SubjectBean.DataBean dataBean) {
        JSONObject params = new JSONObject();
        try {
            params.put("sid", dataBean.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestServer.switchSubject(this, Constant.switchSubjectInterf, params.toString(), new RequestServer.Callback() {
            @Override
            public void onSuccess(Object object) {
                ResultBean result= (ResultBean) object;
                if ("ok".equals(result.getResponse())){
                    MyToastUtils.toastText(ChoiceSubjectActivity.this,"切换学科成功");
                    AppManager.getAppManager().finishSpecailActivity(MeActivity.class);
                    MainActivity.isFlush=true;
                    finish();
                }
            }

            @Override
            public void onFail(Object object) {

            }
        }, true, true, true);
    }

    class ViewHolder {
        TextView tv_kemu;
        Button bt_shiyong;
    }
}
