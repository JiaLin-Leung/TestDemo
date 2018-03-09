package jiangsu.tbkt.teacher.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import jiangsu.tbkt.teacher.R;
import jiangsu.tbkt.teacher.api.RequestServer;
import jiangsu.tbkt.teacher.utils.Constant;
import jiangsu.tbkt.teacher.utils.MyToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by song on 2016/8/10 0010.
 */
public class SetNameActivity extends Activity implements View.OnClickListener {

    private Button bt_confirm;
    private EditText et_setname;
    private String newName;
    private ImageView iv_back;
    private String oldName;
    private ProgressDialog pd;
    private TextView tv_top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setname);
        //返回按鈕
        iv_back = (ImageView) findViewById(R.id.top_btnback);
        iv_back.setOnClickListener(this);
        tv_top = (TextView) findViewById(R.id.top_infotxt);
        tv_top.setText("修改姓名");
        //确定按钮可见
        bt_confirm = (Button) findViewById(R.id.bt_confirm1);
//        bt_confirm.setVisibility(View.VISIBLE);
        bt_confirm.setOnClickListener(this);
        //填写名字
        et_setname = (EditText) findViewById(R.id.et_setname);
        oldName = getIntent().getStringExtra("name");
//        et_setname.setHint(oldName);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.top_btnback:
                SetNameActivity.this.finish();
                break;
            case R.id.bt_confirm1:
                newName = et_setname.getText().toString().trim();
                if (newName.length()>=2&& newName.length()<=5){
                    updateName();
                }else if ("".equals(newName)){
                    MyToastUtils.toastText(SetNameActivity.this,"姓名不能为空");
                }else{
                    MyToastUtils.toastText(SetNameActivity.this,"姓名长度为2-5个字");
                }
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
                MyToastUtils.toastText(SetNameActivity.this,"修改姓名成功");
                finish();
            }

            @Override
            public void onFail(Object object) {
                MyToastUtils.toastText(SetNameActivity.this,"修改姓名失败");
            }
        }, true, true, true);
    }
}
