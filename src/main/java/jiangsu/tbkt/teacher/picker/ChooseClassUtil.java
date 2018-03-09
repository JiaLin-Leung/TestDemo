package jiangsu.tbkt.teacher.picker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import jiangsu.tbkt.teacher.R;
import jiangsu.tbkt.teacher.application.PreferencesManager;
import jiangsu.tbkt.teacher.bean.SelectClassBean;
import jiangsu.tbkt.teacher.utils.MyToastUtils;

/**
 * Developer : xiongwenwei@aliyun.com
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ChooseClassUtil implements View.OnClickListener, NumberPicker.OnValueChangeListener {

    Context context;
    AlertDialog dialog;
    ChooseCityInterface cityInterface;
    NumberPicker npProvince, npCity, npCounty;
    TextView tvCancel, tvSure;
    String[] newCityArray = new String[3];
    SelectClassBean bean;
    View view;
    String flag = "";
    String[] cityArray;
    String[] countyArray;
    String[] provinceArray;
    PopupWindow checkOutWindow;
    int buMenId;

    public void createDialog(Context context, SelectClassBean selectClassBean, View view, ChooseCityInterface cityInterface) {
        this.context = context;
        this.cityInterface = cityInterface;
        this.view = view;
        this.bean = selectClassBean;

        showCheckOutPopWindow();
        setNomal();

        //syw 由于测试网数据错误，会返回多个初中部和小学部，添加过滤
        //使用一个新的集合，将过滤后的对象添加进去，再设置给selectClassBean
        List<SelectClassBean.DataBean> data = new ArrayList<>();

        //syw 对传递过来的集合过滤
        List<SelectClassBean.DataBean> dataList = bean.getData();
        for (int i = 0; i < dataList.size(); i++) {
            SelectClassBean.DataBean dataBean = dataList.get(i);
            if (!flag.contains("" + dataBean.getType())) {
                flag = flag + "," + dataBean.getType();
                data.add(dataBean);
            }
        }
        //syw 将过滤后的只有一个小学，一个初中的对象设置给selectClassBean
        this.bean.setData(data);

        //省：设置选择器最小值、最大值、初始值
        provinceArray = new String[bean.getData().size()];//初始化省数组
        for (int i = 0; i < provinceArray.length; i++) {//省数组填充数据
            provinceArray[i] = bean.getData().get(i).getName();
        }
        npProvince.setDisplayedValues(provinceArray);//设置选择器数据、默认值
        npProvince.setMinValue(0);
        npProvince.setMaxValue(provinceArray.length - 1);
        newCityArray[0] = provinceArray[0];

        //syw 显示市级数据
        changeCity(npProvince.getValue());
    }

    private void showCheckOutPopWindow() {
        View convertview = View.inflate(context, R.layout.dialog_choose_class, null);
        //取消和退出的监听
        tvCancel = (TextView) convertview.findViewById(R.id.tvCancel);
        tvSure = (TextView) convertview.findViewById(R.id.tvSure);
        tvCancel.setOnClickListener(this);
        tvSure.setOnClickListener(this);

        npProvince = (NumberPicker) convertview.findViewById(R.id.npProvince);
        npCity = (NumberPicker) convertview.findViewById(R.id.npCity);
        npCounty = (NumberPicker) convertview.findViewById(R.id.npCounty);

        npProvince.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npCity.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npCounty.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

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
        SelectClassBean.DataBean selectCityData = bean.getData().get(provinceTag);
        if (selectCityData == null) {
            return;
        }

        int len = selectCityData.getLearn_length();
        if (selectCityData.getType() == 1) {    //syw 1代表小学
            if (len == 3) {
                cityArray = new String[]{"一年级", "二年级", "三年级"};
            } else if (len == 5) {
                cityArray = new String[]{"一年级", "二年级", "三年级", "四年级", "五年级"};
            } else if (len == 6) {
                cityArray = new String[]{"一年级", "二年级", "三年级", "四年级", "五年级", "六年级"};
            }
        } else if (selectCityData.getType() == 2) {
            cityArray = new String[]{"七年级", "八年级", "九年级"};
        }

        newCityArray[1] = cityArray[0];

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
                changeCounty(provinceTag, i);//联动县数据
                return;
            }
        }
        npCity.setValue(0);
        changeCounty(provinceTag, npCity.getValue());//联动县数据
    }

    //    根据市,联动县数据
    private void changeCounty(int provinceTag, int cityTag) {
        SelectClassBean.DataBean selectCityData = bean.getData().get(provinceTag);
        if (selectCityData == null) {
            return;
        }

        int max_class = selectCityData.getMax_class();
        if (max_class != 30) {
            MyToastUtils.toastText(context, "目前不是30条，先按照30条计算");
        }
        countyArray = new String[]{"0班", "1班", "2班", "3班", "4班", "5班", "6班", "7班", "8班", "9班", "10班", "11班", "12班",
                "13班", "14班", "15班", "16班", "17班", "18班", "19班", "20班", "21班", "22班", "23班", "24班",
                "25班", "26班", "27班", "28班", "29班", "30班"};

        newCityArray[2] = countyArray[0];

        try {
            npCounty.setMinValue(0);
            npCounty.setMaxValue(countyArray.length - 1);
            npCounty.setWrapSelectorWheel(false);
            npCounty.setDisplayedValues(countyArray);//设置选择器数据、默认值
        } catch (Exception e) {
            npCounty.setDisplayedValues(countyArray);//设置选择器数据、默认值
            npCounty.setMinValue(0);
            npCounty.setMaxValue(countyArray.length - 1);
            npCounty.setWrapSelectorWheel(false);
        }
        for (int i = 0; i < countyArray.length; i++) {
            if (countyArray[i].equals(newCityArray[2])) {
                npCounty.setValue(i);
                return;
            }
        }
        npCounty.setValue(0);
    }

    //设置NumberPicker的分割线透明、字体颜色、设置监听
    private void setNomal() {
        //设置监听
        npProvince.setOnValueChangedListener(this);
        npCity.setOnValueChangedListener(this);
        npCounty.setOnValueChangedListener(this);
        //去除分割线
        setNumberPickerDividerColor(npProvince);
        setNumberPickerDividerColor(npCity);
        setNumberPickerDividerColor(npCounty);
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
                //syw将部门id记录下来
                buMenId = bean.getData().get(npProvince.getValue()).getId();
                //syw 年级在其他地方记录
                String s = cityArray[npCity.getValue()];
                if ("一年级".equals(s)){
                    PreferencesManager.getInstance().putInt("nianJiId",1);
                }else if ("二年级".equals(s)){
                    PreferencesManager.getInstance().putInt("nianJiId",2);
                }else if ("三年级".equals(s)){
                    PreferencesManager.getInstance().putInt("nianJiId",3);
                }else if ("四年级".equals(s)){
                    PreferencesManager.getInstance().putInt("nianJiId",4);
                }else if ("五年级".equals(s)){
                    PreferencesManager.getInstance().putInt("nianJiId",5);
                }else if ("六年级".equals(s)){
                    PreferencesManager.getInstance().putInt("nianJiId",6);
                }else if ("七年级".equals(s)){
                    PreferencesManager.getInstance().putInt("nianJiId",7);
                }else if ("八年级".equals(s)){
                    PreferencesManager.getInstance().putInt("nianJiId",8);
                }else if ("九年级".equals(s)){
                    PreferencesManager.getInstance().putInt("nianJiId",9);
                }
                //syw 将班级记录下来
                PreferencesManager.getInstance().putInt("buMenId", buMenId);
                PreferencesManager.getInstance().putInt("banJiId", npCounty.getValue());
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
                newCityArray[1] = cityArray[0];
                newCityArray[2] = countyArray[0];

                changeCity(npProvince.getValue());
                break;
            case R.id.npCity:
                newCityArray[1] = cityArray[npCity.getValue()];
                newCityArray[2] = countyArray[0];
                break;
            case R.id.npCounty:
                newCityArray[2] = countyArray[npCounty.getValue()];
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
    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        boolean result = false;
        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                try {
                    Field selectorWheelPaintField = numberPicker.getClass().getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText) child).setTextColor(color);
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
