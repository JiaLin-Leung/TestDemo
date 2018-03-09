package jiangsu.tbkt.teacher.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by song on 2016/8/11 0011.
 */
public class MyToastUtils {
    /**
     * 吐司纯文本
     * @param context
     * @param content
     */
    public static void toastText(Context context,String content){
        Toast toast=Toast.makeText(context,content,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}
