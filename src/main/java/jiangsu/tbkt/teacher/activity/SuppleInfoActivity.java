package jiangsu.tbkt.teacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import jiangsu.tbkt.teacher.R;
import jiangsu.tbkt.teacher.view.MarqueeTextView;

/**
 * Created by song on 2017/1/17 0017.
 */
public class SuppleInfoActivity extends BaseActivity implements View.OnClickListener {
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suppleinfo);

        type = getIntent().getStringExtra("type");

        Button bt_supple = (Button) findViewById(R.id.bt_supple);
        ImageView top_btnback = (ImageView) findViewById(R.id.top_btnback);
        MarqueeTextView top_infotxt = (MarqueeTextView) findViewById(R.id.top_infotxt);
        top_infotxt.setText("完善信息");
        bt_supple.setOnClickListener(this);
        top_btnback.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_supple:
                Intent intent = null;
                //syw 跳转完善姓名界面
                if ("name".equals(type) || "all".equals(type)) {
                    intent = new Intent(SuppleInfoActivity.this, SuppleNameActivity.class);
                    intent.putExtra("type", type);
                //syw 跳转完善学校信息界面
                } else if ("class".equals(type)) {
                    intent = new Intent(SuppleInfoActivity.this, SuppleSchoolActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.top_btnback:
                finish();
                break;
        }
    }
}
