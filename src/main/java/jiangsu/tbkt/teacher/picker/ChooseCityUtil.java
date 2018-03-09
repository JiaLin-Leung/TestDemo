package jiangsu.tbkt.teacher.picker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.List;

import jiangsu.tbkt.teacher.R;
import jiangsu.tbkt.teacher.application.PreferencesManager;

/**
 * Developer : xiongwenwei@aliyun.com
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ChooseCityUtil implements View.OnClickListener, NumberPicker.OnValueChangeListener {

    Context context;
    //    AlertDialog dialog;
    ChooseCityInterface cityInterface;
    NumberPicker npProvince, npCity;
    //    NumberPicker npCounty;
    TextView tvCancel, tvSure;
    String[] newCityArray = new String[2];
    CityBean bean;
    View view;
    PopupWindow checkOutWindow;
    String[] provinceArray;
    String[] cityArray;
    String id;

    public void createDialog(Context context, CityBean cityBean, View view, String[] oldCityArray, ChooseCityInterface cityInterface) {
        this.context = context;
        this.cityInterface = cityInterface;
        this.view = view;
        bean = cityBean;

        newCityArray[0] = oldCityArray[0];
        newCityArray[1] = oldCityArray[1];
        showCheckOutPopWindow();

        List<CityBean.DataBean.ChildBean> child = bean.getData().get(npProvince.getValue()).getChild();
        id = child.get(npCity.getValue()).getId();


        setNomal();
        //省：设置选择器最小值、最大值、初始值
        provinceArray = new String[bean.getData().size()];//初始化省数组
        for (int i = 0; i < provinceArray.length; i++) {//省数组填充数据
            provinceArray[i] = bean.getData().get(i).getName();
        }
        npProvince.setDisplayedValues(provinceArray);//设置选择器数据、默认值
        npProvince.setMinValue(0);
        npProvince.setMaxValue(provinceArray.length - 1);
        for (int i = 0; i < provinceArray.length; i++) {
            if (provinceArray[i].equals(newCityArray[0])) {
                npProvince.setValue(i);
                changeCity(i);//联动市数据
            }
        }
    }

    private void showCheckOutPopWindow() {
        View convertview = View.inflate(context, R.layout.dialog_choose_city, null);
        //取消和退出的监听
        tvCancel = (TextView) convertview.findViewById(R.id.tvCancel);
        tvSure = (TextView) convertview.findViewById(R.id.tvSure);
        tvCancel.setOnClickListener(this);
        tvSure.setOnClickListener(this);

        npProvince = (NumberPicker) convertview.findViewById(R.id.npProvince);
        npCity = (NumberPicker) convertview.findViewById(R.id.npCity);

        //syw 设置不可编辑
        npProvince.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npCity.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

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


    //根据省,联动市数据
    private void changeCity(int provinceTag) {
        List<CityBean.DataBean.ChildBean> cityList = bean.getData().get(provinceTag).getChild();
        cityArray = new String[cityList.size()];
        for (int i = 0; i < cityArray.length; i++) {
            cityArray[i] = cityList.get(i).getName();
        }
        try {
            npCity.setMinValue(0);
            npCity.setMaxValue(cityArray.length - 1);
            npCity.setWrapSelectorWheel(false);
            npCity.setDisplayedValues(cityArray);//设置选择器数据、默认值
        } catch (Exception e) {
            npCity.setDisplayedValues(cityArray);//设置选择器数据、默认值
            npCity.setMinValue(0);
            npCity.setMaxValue(cityArray.length - 1);
            npCity.setWrapSelectorWheel(false);
        }
        for (int i = 0; i < cityArray.length; i++) {
            if (cityArray[i].equals(newCityArray[1])) {
                npCity.setValue(i);
                return;
            }
        }
        npCity.setValue(0);
    }


    //设置NumberPicker的分割线透明、字体颜色、设置监听
    private void setNomal() {
        //设置监听
        npProvince.setOnValueChangedListener(this);
        npCity.setOnValueChangedListener(this);
        //去除分割线
        setNumberPickerDividerColor(npProvince);
        setNumberPickerDividerColor(npCity);
        //设置字体颜色
//        setNumberPickerTextColor(npProvince, context.getResources().getColor(R.color.text_unchecked));
//        setNumberPickerTextColor(npCity, context.getResources().getColor(R.color.text_unchecked));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancel:
                checkOutWindow.dismiss();
                break;
            case R.id.tvSure:
                checkOutWindow.dismiss();
                PreferencesManager.getInstance().putString("youzhengbianma", id);
                Log.e("syw","youzhengbianma"+id);
                cityInterface.sure(newCityArray);
                break;
        }
    }

    //选择器选择值监听
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        switch (picker.getId()) {
            case R.id.npProvince:
                List<CityBean.DataBean> dataList = bean.getData();
                newCityArray[0] = dataList.get(npProvince.getValue()).getName();
                changeCity(npProvince.getValue());
                newCityArray[1] = dataList.get(npProvince.getValue()).getChild().get(0).getName();

                List<CityBean.DataBean.ChildBean> child = bean.getData().get(npProvince.getValue()).getChild();
                id = child.get(npCity.getValue()).getId();

                break;


            case R.id.npCity:
                List<CityBean.DataBean.ChildBean> cityList = bean.getData().get(npProvince.getValue()).getChild();
                newCityArray[1] = cityList.get(npCity.getValue()).getName();

                id = cityList.get(npCity.getValue()).getId();
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

    //设置选择器字体颜色
    public boolean setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        boolean result = false;
        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                try {
                    Field selectorWheelPaintField = numberPicker.getClass().getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
                    //syw 设置默认颜色
                    if (i == 0) {
                        ((EditText) child).setTextColor(context.getResources().getColor(R.color.black));
                    } else {
                        ((EditText) child).setTextColor(color);
                    }
                    numberPicker.invalidate();
                    result = true;
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
