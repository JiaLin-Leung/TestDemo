package jiangsu.tbkt.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJBitmap;

import java.util.List;

import jiangsu.tbkt.teacher.R;
import jiangsu.tbkt.teacher.api.RequestServer;
import jiangsu.tbkt.teacher.bean.SettingManageBean;
import jiangsu.tbkt.teacher.bean.ShenFenBean;
import jiangsu.tbkt.teacher.utils.Constant;
import jiangsu.tbkt.teacher.view.MarqueeTextView;

/**
 * Created by Administrator on 2017/1/14 0014.
 */
public class ChoiceShenFenActivity extends BaseActivity implements View.OnClickListener {
    ListView lv_shenfen;
    ShenFenBean bean;
    List<ShenFenBean.DataBean> dataSource;
    KJBitmap kjBitmap;
    PopupWindow logerrWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_shenfen);

        bean = (ShenFenBean) getIntent().getSerializableExtra("shenFenBean");
        dataSource = bean.getData();

        kjBitmap = new KJBitmap();
        initView();

    }

    private void initView() {
        MarqueeTextView top_infotxt = (MarqueeTextView) findViewById(R.id.top_infotxt);
        top_infotxt.setText("选择身份");

        ImageView top_btnback = (ImageView) findViewById(R.id.top_btnback);
        top_btnback.setOnClickListener(this);
        lv_shenfen = (ListView) findViewById(R.id.lv_shenfen);
        lv_shenfen.setAdapter(new ShenFenAdapter());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_btnback:
                finish();
                break;
        }
    }

    class ShenFenAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dataSource.size();
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
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(ChoiceShenFenActivity.this, R.layout.item_shenfen, null);
                holder.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_kemu = (TextView) convertView.findViewById(R.id.tv_kemu);
                holder.bt_shiyong = (Button) convertView.findViewById(R.id.bt_shiyong);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final ShenFenBean.DataBean dataBean = dataSource.get(i);
            kjBitmap.display(holder.iv_head, dataBean.getPortrait());
            holder.tv_name.setText(dataBean.getName());
//            syw 修改名字
            String name="";
            if (dataBean.getGrade_id()==1){
                holder.tv_kemu.setText("一年级"+dataBean.getRole());
            }else if (dataBean.getGrade_id()==2){
                holder.tv_kemu.setText("二年级"+dataBean.getRole());
            }else if (dataBean.getGrade_id()==3){
                holder.tv_kemu.setText("三年级"+dataBean.getRole());
            }else if (dataBean.getGrade_id()==4){
                holder.tv_kemu.setText("四年级"+dataBean.getRole());
            }else if (dataBean.getGrade_id()==5){
                holder.tv_kemu.setText("五年级"+dataBean.getRole());
            }else if (dataBean.getGrade_id()==6){
                holder.tv_kemu.setText("六年级"+dataBean.getRole());
            }else if (dataBean.getGrade_id()==7){
                holder.tv_kemu.setText("七年级"+dataBean.getRole());
            }else if (dataBean.getGrade_id()==8){
                holder.tv_kemu.setText("八年级"+dataBean.getRole());
            }else if (dataBean.getGrade_id()==9){
                holder.tv_kemu.setText("九年级"+dataBean.getRole());
            }else{
                holder.tv_kemu.setText(dataBean.getRole());
            }

            holder.bt_shiyong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switchShenFen(dataBean);
                }
            });

            return convertView;
        }
    }


    private void switchShenFen(final ShenFenBean.DataBean dataBean) {
        JSONObject params = new JSONObject();
        try {
            params.put("user_id", dataBean.getBind_id());
            params.put("grade_id",dataBean.getGrade_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestServer.switchShenFen(this, Constant.switchShenFenInterf, params.toString(), new RequestServer.Callback() {
            @Override
            public void onSuccess(Object object) {
                //syw 切换身份成功之后查看信息是否完整
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getAccountHttpData();
                    }
                });
            }

            @Override
            public void onFail(Object object) {
//                ResultBean resultBean= (ResultBean) object;
//                String message=resultBean.getMessage();
//                if (message!=null||!"".equals(message)){
//                    if (message.contains(",")){
//                        message=message.replace(",","\n");
//                    }
//                    showLogErrorWindow(message);
//                }
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
//                    syw选择身份之后重新刷新页面
                    MainActivity.isFlush=true;
//                    syw 临时跳转到MeActivity
                    intent = new Intent(ChoiceShenFenActivity.this, MainActivity.class);
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
                    intent = new Intent(ChoiceShenFenActivity.this, SuppleInfoActivity.class);
                    intent.putExtra("type", type);
                    startActivity(intent);
                }
            }

            @Override
            public void onFail(Object object) {

            }
        }, true, true, true);
    }

    class ViewHolder {
        ImageView iv_head;
        TextView tv_name;
        TextView tv_kemu;
        Button bt_shiyong;
    }
}
