package jiangsu.tbkt.teacher.picker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.lang.reflect.Field;

import jiangsu.tbkt.teacher.R;
import jiangsu.tbkt.teacher.application.PreferencesManager;

/**
 * Developer : xiongwenwei@aliyun.com
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ChooseProvinceUtil implements View.OnClickListener, NumberPicker.OnValueChangeListener {

    Context context;
    ChooseCityInterface cityInterface;
    NumberPicker npProvince;
    TextView tvCancel, tvSure;
    String[] newCityArray = new String[3];
//    SelectClassBean bean;
    View view;
    String flag = "";
    String[] provinceArray;
    PopupWindow checkOutWindow;

    public void createDialog(Context context, View view, ChooseCityInterface cityInterface) {
        this.context = context;
        this.cityInterface = cityInterface;
        this.view = view;

        showCheckOutPopWindow();
        setNomal();

        //省：设置选择器最小值、最大值、初始值
        provinceArray = new String[]{"河南","江西","广西","江苏"};//初始化省数组

        npProvince.setDisplayedValues(provinceArray);//设置选择器数据、默认值
        npProvince.setMinValue(0);
        npProvince.setMaxValue(provinceArray.length - 1);
        newCityArray[0] = provinceArray[0];

    }

    private void showCheckOutPopWindow() {
        View convertview = View.inflate(context, R.layout.dialog_choose_province, null);
        //取消和退出的监听
        tvCancel = (TextView) convertview.findViewById(R.id.tvCancel);
        tvSure = (TextView) convertview.findViewById(R.id.tvSure);
        tvCancel.setOnClickListener(this);
        tvSure.setOnClickListener(this);

        npProvince = (NumberPicker) convertview.findViewById(R.id.npProvince);
        npProvince.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        checkOutWindow = new PopupWindow(convertview, ViewGroup.LayoutParams.MATCH_PARENT, -2, true);
        checkOutWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (Build.VERSION.SDK_INT < 24) {
            checkOutWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        } else {
            int distance=getWinHeight()-getViewHeight(convertview);
            checkOutWindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, distance);
        }

        backgroundAlpha(0.5f);
        checkOutWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
        checkOutWindow.setAnimationStyle(R.style.popwindow_anim_style);
        checkOutWindow.update();
    }

    /**
     * 设置透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        Activity act= (Activity) context;
        WindowManager.LayoutParams lp = act.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        act.getWindow().setAttributes(lp);
    }

    public int getWinHeight(){
        Activity activity=(Activity)context;
        WindowManager wm = activity.getWindowManager();
        return wm.getDefaultDisplay().getHeight();
    }

    public int getViewHeight(View view){
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredHeight();
    }


    //设置NumberPicker的分割线透明、字体颜色、设置监听
    private void setNomal() {
        //设置监听
        npProvince.setOnValueChangedListener(this);

        //去除分割线
        setNumberPickerDividerColor(npProvince);

        //设置字体颜色
//        setNumberPickerTextColor(npProvince, context.getResources().getColor(R.color.red));
//        setNumberPickerTextColor(npCity, context.getResources().getColor(R.color.red));
//        setNumberPickerTextColor(npCounty, context.getResources().getColor(R.color.red));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancel:
                checkOutWindow.dismiss();
                break;
            case R.id.tvSure:
                //syw 将班级记录下来
                PreferencesManager.getInstance().putString("province", newCityArray[0]);
                checkOutWindow.dismiss();
                cityInterface.sure(newCityArray);
                break;
        }
    }

    //选择器选择值监听
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        switch (picker.getId()) {
            case R.id.npProvince:
                newCityArray[0] = provinceArray[npProvince.getValue()];
                break;

        }
    }

    //设置分割线颜色
    private void setNumberPickerDividerColor(NumberPicker numberPicker) {
        NumberPicker picker = numberPicker;
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //设置分割线的颜色值
                    pf.set(picker, new ColorDrawable(context.getResources().getColor(R.color.divider)));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
