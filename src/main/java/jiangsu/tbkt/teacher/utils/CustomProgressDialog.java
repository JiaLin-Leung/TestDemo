package jiangsu.tbkt.teacher.utils;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import jiangsu.tbkt.teacher.R;


/**
 * Created by song on 2016/12/7 0007.
 */
public class CustomProgressDialog extends ProgressDialog {
    private Context context;

    private static TextView tv;
    private ImageView loading;
    private static String message;


    public CustomProgressDialog(Context context) {
        super(context);
        this.context = context;
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress);

        tv = (TextView) this.findViewById(R.id.progressbar_tv);
        tv.setText(message);
        loading = (ImageView) this.findViewById(R.id.progressbar_loading);
        // 设置动画
        RotateAnimation a = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        a.setDuration(800);
        a.setInterpolator(new LinearInterpolator());
        a.setRepeatCount(Animation.INFINITE);
        loading.startAnimation(a);

        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
    }

    /**
     * 对话框显示初始化操作
     * @param message 显示内容
     * @return CustomProgressDialog
     */
    public static CustomProgressDialog showProgress(Context ctx, String message) {
        CustomProgressDialog d = new CustomProgressDialog(ctx);
        CustomProgressDialog.message = message;
        d.show();
        return d;
    }

    public void setText(String message) {
        CustomProgressDialog.message = message;
    }
}
