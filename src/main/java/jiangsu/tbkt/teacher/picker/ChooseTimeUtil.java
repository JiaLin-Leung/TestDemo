package jiangsu.tbkt.teacher.picker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.format.Time;
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
import java.util.Calendar;
import java.util.TimeZone;

import jiangsu.tbkt.teacher.R;

/**
 * Developer : xiongwenwei@aliyun.com
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ChooseTimeUtil implements View.OnClickListener, NumberPicker.OnValueChangeListener {

    Context context;
    ChooseCityInterface cityInterface;
    NumberPicker npProvince, npCity;
    TextView tvCancel, tvSure;
    String[] resultArray = new String[2];

    PopupWindow checkOutWindow;
    String[] provinceArray;
    String[] cityArray;

    public void createDialog(Context context, ChooseCityInterface cityInterface) {
        this.context = context;
        this.cityInterface = cityInterface;

        showCheckOutPopWindow();

        setNomal();
        //省：设置选择器最小值、最大值、初始值
        provinceArray = getFivteendate();
        cityArray = getFiveHour();

        npProvince.setDisplayedValues(provinceArray);//设置选择器数据、默认值
        npProvince.setMinValue(0);
        npProvince.setMaxValue(provinceArray.length - 1);
        npProvince.setWrapSelectorWheel(false);

        npCity.setDisplayedValues(cityArray);
        npCity.setMinValue(0);
        npCity.setMaxValue(cityArray.length - 1);
        npCity.setWrapSelectorWheel(false);

        resultArray[0] = provinceArray[0];
        resultArray[1] = cityArray[0];
    }


    public String[] getFiveHour() {

        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        int hour = t.hour; // 0-23
        String[] hours=null;
        if (hour==23){
            hours=new String[]{"0时","1时", "2时", "3时", "4时", "5时", "6时", "7时", "8时",
                    "9时", "10时", "11时", "12时", "13时", "14时", "15时", "16时", "17时", "18时",
                    "19时", "20时", "21时", "22时", "23时"};
            return hours;
        }else{
            hours = new String[23-hour];
        }

        for (int i = 0; i < 24; i++) {

            hour++;
            hours[i] = hour + "时";
            Log.e("syw", "hour:" + hour);
            if (hour == 23) {
                break;
            }

        }
        return hours;
    }

    String mYear, mMonth, mDay;

    /**
     * 获取今天往后一周的日期（几月几号）
     */
    public String[] getFivteendate() {
        String[] dates = new String[20];
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        int hour = t.hour; // 0-23



        for ( int i = 0; i < 20; i++) {
            mYear = String.valueOf(c.get(Calendar.YEAR));// 获取当前年份
            mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
            if (hour==23){
                mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + i+1);// 获取当前日份的日期号码
            }else{
                mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + i);// 获取当前日份的日期号码
            }

            String date = mMonth + "月" + mDay + "日";
            int maxDay = MaxDayFromDay_OF_MONTH(Integer.parseInt(mYear), Integer.parseInt(mMonth));
            if (Integer.parseInt(mDay) > maxDay) {
                int curDay = i - (maxDay - (c.get(Calendar.DAY_OF_MONTH)));
                date = c.get(Calendar.MONTH) + 2 + "月" + curDay + "日";
            }
            Log.e("syw", "date:" + date);
            dates[i] = date;
        }
        return dates;
    }


    /**
     * 得到当年当月的最大日期
     **/
    public static int MaxDayFromDay_OF_MONTH(int year, int month) {
        Calendar time = Calendar.getInstance();
        time.clear();
        time.set(Calendar.YEAR, year);
        time.set(Calendar.MONTH, month - 1);//注意,Calendar对象默认一月为0
        int day = time.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数
        Log.e("syw", "year:" + year + "mouth:" + month + "最大日期:" + day);
        return day;
    }


    private void showCheckOutPopWindow() {
        View convertview = View.inflate(context, R.layout.dialog_choose_time, null);
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
            checkOutWindow.showAtLocation(tvSure, Gravity.BOTTOM, 0, 0);
        } else {
            int distance = getWinHeight() - getViewHeight(convertview);
            checkOutWindow.showAtLocation(tvSure, Gravity.NO_GRAVITY, 0, distance);
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
        Activity act = (Activity) context;
        WindowManager.LayoutParams lp = act.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        act.getWindow().setAttributes(lp);
    }

    public int getWinHeight() {
        Activity activity = (Activity) context;
        WindowManager wm = activity.getWindowManager();
        return wm.getDefaultDisplay().getHeight();
    }

    public int getViewHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredHeight();
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
                cityInterface.sure(resultArray);
                break;
        }
    }

    //选择器选择值监听
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        switch (picker.getId()) {
            case R.id.npProvince:
                resultArray[0] = provinceArray[npProvince.getValue()];
                resultArray[1] = cityArray[0];
                changeHours(npProvince.getValue());
                break;


            case R.id.npCity:
                resultArray[1] = cityArray[npCity.getValue()];
                break;
        }
    }

    private void changeHours(int value) {
        try {
            if (value == 0) {
                cityArray = getFiveHour();
            } else {
                cityArray = new String[]{"0时","1时", "2时", "3时", "4时", "5时", "6时", "7时", "8时",
                        "9时", "10时", "11时", "12时", "13时", "14时", "15时", "16时", "17时", "18时",
                        "19时", "20时", "21时", "22时", "23时"};
            }
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
