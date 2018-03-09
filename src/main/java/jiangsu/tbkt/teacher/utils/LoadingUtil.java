package jiangsu.tbkt.teacher.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import jiangsu.tbkt.teacher.R;


/**
 * Created by song on 2016/12/7 0007.
 */
public class LoadingUtil {

    public static CustomProgressDialog mProgressDialog = null;

    public static void showDialog(final Context context){
        if (context==null){
            return;
        }
        if (mProgressDialog==null){
            mProgressDialog=new CustomProgressDialog(context, R.style.dialog);
            mProgressDialog.setText("正在加载");
        }
        mProgressDialog.show();
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                ((Activity) context).finish();
            }
        });
    }

    //取消对话框
    public static void dismissDialog() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

}
