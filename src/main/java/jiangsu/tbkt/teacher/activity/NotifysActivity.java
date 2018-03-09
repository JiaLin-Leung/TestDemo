package jiangsu.tbkt.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import jiangsu.tbkt.teacher.R;
import jiangsu.tbkt.teacher.api.RequestServer;
import jiangsu.tbkt.teacher.bean.TemplateBean;
import jiangsu.tbkt.teacher.utils.Constant;
import jiangsu.tbkt.teacher.view.MarqueeTextView;

/**
 * Created by Administrator on 2017/8/30 0030.
 */
public class NotifysActivity extends BaseActivity implements View.OnClickListener {

    public TextView tv1, tv2, tv3;
    public ImageView top_btnback;
    public MarqueeTextView top_infotxt;
    public ListView lv_template;
    public List<String> templates;
    public MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifys);
        initView();
        getTemplate();
    }

    private void getTemplate() {
        RequestServer.getTemplateData(this, Constant.templateInterf, null, new RequestServer.Callback() {
            @Override
            public void onSuccess(Object object) {
                TemplateBean templateBean = (TemplateBean) object;

                templates = templateBean.getData();
                if (adapter == null) {
                    adapter = new MyAdapter();
                }
                lv_template.setAdapter(adapter);
            }

            @Override
            public void onFail(Object object) {

            }
        }, true, true, true);
    }

    private void initView() {
        lv_template = (ListView) findViewById(R.id.lv_template);

        top_btnback = (ImageView) findViewById(R.id.top_btnback);
        top_infotxt = (MarqueeTextView) findViewById(R.id.top_infotxt);
        top_infotxt.setText("通知模板");

        top_btnback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.top_btnback:
                finish();
                break;
        }
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return templates.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(NotifysActivity.this, R.layout.item_template, null);
                holder.textView = (TextView) convertView.findViewById(R.id.tv_template);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView.setText(templates.get(position));
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent();
                    intent1.putExtra("notifys", templates.get(position));
                    setResult(RESULT_OK, intent1);
                    finish();
                }
            });
            return convertView;
        }
    }

    class ViewHolder {
        TextView textView;
    }
}
