package jiangsu.tbkt.teacher.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 *
 * @Description: 全局工作类
 * @FileName: GlobalTools.java
 * @Package com.tbkt.student.util
 * @Author wangxiao
 * @Date 2015-1-9
 * @Version V1.0
 */

public class GlobalTools {

    /** 设置Selector。 */
    public static StateListDrawable newSelector(Context context, int idNormal,
                                                int idPressed, int idFocused, int idUnable) {
        StateListDrawable bg = new StateListDrawable();
        Drawable normal = idNormal == -1 ? null : context.getResources()
                .getDrawable(idNormal);
        Drawable pressed = idPressed == -1 ? null : context.getResources()
                .getDrawable(idPressed);
        Drawable focused = idFocused == -1 ? null : context.getResources()
                .getDrawable(idFocused);
        Drawable unable = idUnable == -1 ? null : context.getResources()
                .getDrawable(idUnable);
        // View.PRESSED_ENABLED_STATE_SET
        bg.addState(new int[] { android.R.attr.state_pressed,
                android.R.attr.state_enabled }, pressed);
        // View.ENABLED_FOCUSED_STATE_SET
        bg.addState(new int[] { android.R.attr.state_enabled,
                android.R.attr.state_focused }, focused);
        // View.ENABLED_STATE_SET
        bg.addState(new int[] { android.R.attr.state_enabled }, normal);
        // View.FOCUSED_STATE_SET
        bg.addState(new int[] { android.R.attr.state_focused }, focused);
        // View.WINDOW_FOCUSED_STATE_SET
        bg.addState(new int[] { android.R.attr.state_window_focused }, unable);
        // View.EMPTY_STATE_SET
        bg.addState(new int[] {}, normal);
        return bg;
    }

    /*
     * 根据最大班级长度  设置数据
     */
    public static ArrayList<String> setClassItem(String max_length) {
        ArrayList<String> namlist = new ArrayList<String>();
        for (int i = 0; i < Integer.valueOf(max_length); i++) {
            namlist.add(i + "班");
        }
        return namlist;
    }

//    /**
//     * 判断小学返回选择值
//     *
//     * @param ac
//     * @param gradeBean
//     * @return
//     */
//    public static String[] JudegPrimItem(Activity ac, GradeBean gradeBean) {
//        String[] item = null;
//
//        if (gradeBean.getLearn_length().equals("5")) {// 5年制
//            item = ac.getResources().getStringArray(R.array.prim_grade_five);
//
//            return item;
//        }
//        if (gradeBean.getLearn_length().equals("6")) {// 6年制
//            item = ac.getResources().getStringArray(R.array.prim_grade_six);
//
//            return item;
//        }
//        if (gradeBean.getLearn_length().equals("9")) {// 9年制
//            item = ac.getResources().getStringArray(R.array.grade_item);
//
//            return item;
//        }
//
//        return item;
//
//    }
//
//    /**
//     * 判断显示年级
//     *
//     * @param gradeBeanList
//     */
//    public static String[] JudgeGradeData(Activity ac,
//                                          List<GradeBean> gradeBeanList) {
//        String[] item = null;
//        int size = gradeBeanList.size();
//        if (2 == size) {// 2个部门
//            item = ac.getResources().getStringArray(R.array.grade_section_item);
//
//            return item;
//        }
//
//        if (1 == size) {// 1个部门
//            GradeBean gradeBean = null;
//            for (int i = 0; i < size; i++) {
//                gradeBean = gradeBeanList.get(i);
//                if (gradeBean.getType().equals("1")) {// 小学
//                    return GlobalTools.JudegPrimItem(ac, gradeBean);
//                }
//
//                if (gradeBean.getType().equals("2")) {// 初中
//                    item=new String[gradeBeanList.get(i).getGrade_list().size()];
//                    for (int j = 0; j <gradeBeanList.get(i).getGrade_list().size() ; j++) {
//                        item[j]=gradeBeanList.get(i).getGrade_list().get(j).getName();
//                        if(item[j].contains("六")){
//                            PreferencesManager.getInstance().putString("hava_liu", "true");
//                        }
//                    }
//
//                }
//            }
//
//        }
//        return item;
//    }
//
//
//    /**
//     * 判断年级
//     *
//     * @param BackGradeStr
//     * @return
//     */
//    public static String setGradeNumber(String BackGradeStr) {
//        String gradeId = "";
//        if (BackGradeStr.contains("一")) {
//            gradeId = "1";
//        }
//        if (BackGradeStr.contains("二")) {
//            gradeId = "2";
//        }
//        if (BackGradeStr.contains("三")) {
//            gradeId = "3";
//        }
//        if (BackGradeStr.contains("四")) {
//            gradeId = "4";
//        }
//        if (BackGradeStr.contains("五")) {
//            gradeId = "5";
//        }
//        if (BackGradeStr.contains("六")) {
//            gradeId = "6";
//        }
//        if (BackGradeStr.contains("七")) {
//            gradeId = "7";
//        }
//        if (BackGradeStr.contains("八")) {
//            gradeId = "8";
//        }
//        if (BackGradeStr.contains("九")) {
//            gradeId = "9";
//        }
//        return gradeId;
//    }
//
//    /**
//     * 判断是否移动手机号码
//     *
//     * @param mobile
//     * @return
//     */
//    public static int validateMobile(String mobile) {
//        if (mobile.trim().length() != 11) {
//            return 0;// 不是完整的手机号
//        }
//        if (mobile.trim().substring(0, 3).equals("134")
//                || mobile.trim().substring(0, 3).equals("135")
//                || mobile.trim().substring(0, 3).equals("136")
//                || mobile.trim().substring(0, 3).equals("137")
//                || mobile.trim().substring(0, 3).equals("138")
//                || mobile.trim().substring(0, 3).equals("139")
//                || mobile.trim().substring(0, 3).equals("182")
//                || mobile.trim().substring(0, 3).equals("150")
//                || mobile.trim().substring(0, 3).equals("151")
//                || mobile.trim().substring(0, 3).equals("152")
//                || mobile.trim().substring(0, 3).equals("157")
//                || mobile.trim().substring(0, 3).equals("158")
//                || mobile.trim().substring(0, 3).equals("159")
//                || mobile.trim().substring(0, 3).equals("187")
//                || mobile.trim().substring(0, 3).equals("188")) {
//            return 1;// 移动手机号
//        } else if (mobile.trim().substring(0, 3).equals("130")
//                || mobile.trim().substring(0, 3).equals("131")
//                || mobile.trim().substring(0, 3).equals("132")
//                || mobile.trim().substring(0, 3).equals("156")
//                || mobile.trim().substring(0, 3).equals("185")
//                || mobile.trim().substring(0, 3).equals("186")) {
//            return 2;// 联通手机号
//        } else if (mobile.trim().substring(0, 3).equals("133")
//                || mobile.trim().substring(0, 3).equals("153")
//                || mobile.trim().substring(0, 3).equals("180")
//                || mobile.trim().substring(0, 3).equals("189")) {
//            return 3;// 电信手机号
//        }
//        return 0;
//    }


    /**
     * 显示弹框
     * @param message
     * @param context
     */
    public static void setTaost(String message,Context context){
        if (!"".equals(message)) {
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 判断信息
     * @param message
     */
    public static void setMessage(Context context,String message){

        if (!"".equals(message)) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            return;
        }
    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
            + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
