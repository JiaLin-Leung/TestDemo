package jiangsu.tbkt.teacher.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import jiangsu.tbkt.teacher.R;

/**
 * Created by LiuShiQi on 2018/3/30.
 * 权限检测
 */

public class PopUtils {


    public static void showMICPopwindow(Context context) {
        View convertview = null;

        convertview = View.inflate(context, R.layout.pop_mic, null);

        TextView tv_confirm = (TextView) convertview.findViewById(R.id.tv_confirm);

        final PopupWindow checkOutWindow = new PopupWindow(convertview, ViewGroup.LayoutParams.MATCH_PARENT, -1, true);
        checkOutWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        checkOutWindow.showAtLocation(tv_confirm, Gravity.CENTER, 0, 0);
        checkOutWindow.setAnimationStyle(R.style.popwindow_anim_style);
        checkOutWindow.update();

        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkOutWindow.dismiss();
            }
        });
    }

    public static void showCameraPopwindow(Context context) {
        View convertview = null;

        convertview = View.inflate(context, R.layout.pop_camera, null);

        TextView tv_confirm = (TextView) convertview.findViewById(R.id.tv_confirm);

        final PopupWindow checkOutWindow = new PopupWindow(convertview, ViewGroup.LayoutParams.MATCH_PARENT, -1, true);
        checkOutWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        checkOutWindow.showAtLocation(tv_confirm, Gravity.CENTER, 0, 0);
        checkOutWindow.setAnimationStyle(R.style.popwindow_anim_style);
        checkOutWindow.update();

        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkOutWindow.dismiss();
            }
        });
    }
}
