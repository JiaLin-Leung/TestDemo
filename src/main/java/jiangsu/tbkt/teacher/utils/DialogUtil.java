package jiangsu.tbkt.teacher.utils;



import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/**
 * @Deprecated：对话框工具类
 * @FileName: DialogUtil.java
 * @Package com.tbkt.student.util
 * @Author wangxiao
 * @Date 2014-12-28
 * @Version V1.0
 */
public abstract class DialogUtil {

    /**
     * 三个按钮的对话框风格
     * @param title 标题
     * @param message 信息
     * @param positiveText 确定文字
     * @param middleText 中间按钮文字
     * @param negetiveText 取消按钮文字
     * @return void
     * */
    public  void multiDialog(Context context,String title,String message
            ,String positiveText,String middleText,String negetiveText){

        new RemindDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositive(positiveText, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        positiveContent();
                        dialog.dismiss();
                    }
                })
                .setMiddletive(middleText, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        middleContent();
                        dialog.dismiss();
                    }
                })
                .setNegtive(negetiveText, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        negativeContent();
                        dialog.dismiss();
                    }
                })
                .createDialog()
                .show();

    }
    /**
     * 两个按钮的对话框风格
     * @param title 标题
     * @param message 信息
     * @param positiveText 确定文字
     * @param negetiveText 取消按钮文字
     * @return void
     * */
    public  void doubleDialog(Context context,String title,String message
            ,String positiveText,String negetiveText){

        new RemindDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositive(positiveText, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        positiveContent();
                        dialog.dismiss();

                    }
                })

                .setNegtive(negetiveText, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        negativeContent();
                        dialog.dismiss();

                    }
                })
                .createDialog()
                .show();
    }

    /**
     * 一个按钮的对话框风格
     *
     * */
    public  void singleDialog(Context context,String title,String message
            ,String positiveText){

        new RemindDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositive(positiveText, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        positiveContent();
                        dialog.dismiss();

                    }
                })

                .createDialog()
                .show();

    }

    public abstract void positiveContent();
    public abstract void middleContent();
    public abstract void negativeContent();

}
