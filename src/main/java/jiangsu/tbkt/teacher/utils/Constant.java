package jiangsu.tbkt.teacher.utils;

/**
 * @Description: 静态常量类
 * @FileName: Constant.java
 * @Package com.tbkt.student.util
 * @Author wangxiao
 * @Date 2015-2-5
 * @Version V1.0
 */
public class Constant {
    // sdk版本
    public static int VERSION_SDK = 5;
    //
    public static final int BOOK = 0;
    public static final int WORKBOOK = 1;
    public static final int AREA = 2;
    public static final int SCHOOL = 3;
    public static final int CLASS = 4;
    public static final int CLASS_SET = 5;
    public static final int Earth = 0;
    public static final int COUNTY = 7;
    public static final int GRADE = 6;
    public static final int CITY = 8;
    public static final int PROVINCE = 9;
    public static final int OPENSUB = 10;


    /*
    科目id
     */
    public static final String PRIM_MATH_ID = "21";
    public static final String PRIM_ENG = "91";
    public static final String MID_MATH_ID = "22";

    /**
     * 当前版本号
     */
    public static final int CUR_VRSION_API = 8;


    /////////////////////////////
    //syw 公共接口
    ////////////////////////////


    // syw 获取接口地址   需要修改
    public static String getInterInterf = "/system/hosts";

    public static String crashInterf = "/account/test_error";

    // syw 登录接口
    public static String loginInterf = "/account/login/t";

    // syw 获取所有身份接口
    public static String shenFenInterf = "/account/accounts";

    // syw 获取模板接口
    public static String templateInterf = "/im/template";

    // syw 切换身份接口
    public static String switchShenFenInterf = "/account/switch";

    //syw 重置密码
    public static String resetPwd = "/account/password";

    // syw 修改姓名
    public static String resetName = "/account/name";

    // syw 设置页面数据
    public static String subManageInterf = "/account/profile";

    // syw 意见反馈接口
    public static String adviceInterf = "/account/feedback";

    // syw 检测新版本接口  英语教师端type=6
    public static String newVersonInterf = "/system/version";

    // syw 获取县区接口
    public static String areaInterf = "/class/cities";

    // syw 获取班级列表接口
    public static String classInterf = "/class/departments";

    // syw 提交班级接口
    public static String submitSchInterf = "/class/join";

    // syw 获取学校接口
    public static String schoolListInterf = "/class/schools";

    //syw 获取验证码
    public static String getAccountSMSpass = "/account/vcode";

    //syw 获取密码
    public static String getNewPass = "/account/findpwd";

    //syw 上传头像保存
    public static String saveBitmapInterf = "/account/portrait";

    //syw 上传图片保存
    public static String savePicsInterf = "/im/sendmessage";

    //syw 获取通知
    public static String getNotifyClassInterf = "/account/units";

    //    syw获取学生接口
    public static String getNotifyStudentInterf = "/class/students";

    // syw 切换学科
    public static String getSubjectInterf = "/account/get_subjects";

    // syw 设置页面数据
    public static String switchSubjectInterf = "/account/set_subject";

    //syw 上传头像url接口
//    public static String getUrlInterf = "/account/uploadurl";

    //上传图片保存
    public static String savehuodongInterf = "/im/outside/task/send";

}
