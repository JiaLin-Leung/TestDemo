package jiangsu.tbkt.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import jiangsu.tbkt.teacher.R;
import jiangsu.tbkt.teacher.api.RequestServer;
import jiangsu.tbkt.teacher.utils.Constant;
import jiangsu.tbkt.teacher.utils.MyToastUtils;
import jiangsu.tbkt.teacher.view.MarqueeTextView;

/**
 * Created by song on 2017/1/17 0017.
 */
public class SuppleNameActivity extends BaseActivity implements View.OnClickListener {
    EditText et_name;
    String newName;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplename);

        type = getIntent().getStringExtra("type");

        Button bt_supple = (Button) findViewById(R.id.bt_supple);
        et_name = (EditText) findViewById(R.id.et_name);
        MarqueeTextView top_infotxt = (MarqueeTextView) findViewById(R.id.top_infotxt);
        ImageView top_btnback = (ImageView) findViewById(R.id.top_btnback);

        top_infotxt.setText("完善姓名信息");
        top_btnback.setOnClickListener(this);
        bt_supple.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_btnback:
                finish();
                break;
            case R.id.bt_supple:
                newName = et_name.getText().toString().trim();
                if (newName.length() >= 2 && newName.length() <= 5) {
                    updateName();
                }else if ("".equals(newName)){
                    MyToastUtils.toastText(SuppleNameActivity.this, "姓名不能为空");
                }else {
                    MyToastUtils.toastText(SuppleNameActivity.this, "姓名长度为2-5个字");
                }
                break;
        }
    }

    private void updateName() {
        JSONObject params = new JSONObject();
        try {
            params.put("name", newName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestServer.getResultBean(this, Constant.resetName, params.toString(), new RequestServer.Callback() {
            @Override
            public void onSuccess(Object object) {
                Intent intent = null;
                if ("name".equals(type)) {
                    intent = new Intent(SuppleNameActivity.this, MainActivity.class);
                } else if ("all".equals(type)) {
                    intent = new Intent(SuppleNameActivity.this, SuppleSchoolActivity.class);
                }
                startActivity(intent);
                finish();
            }

            @Override
            public void onFail(Object object) {
                MyToastUtils.toastText(SuppleNameActivity.this, "完善姓名失败");
            }
        }, true, true, true);
    }
}
