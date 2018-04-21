package jiangsu.tbkt.teacher.api;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.util.List;

import jiangsu.tbkt.teacher.application.PreferencesManager;
import jiangsu.tbkt.teacher.bean.LoginResultBean;
import jiangsu.tbkt.teacher.bean.NotifyClassBean;
import jiangsu.tbkt.teacher.bean.ResultBean;
import jiangsu.tbkt.teacher.bean.SchoolBean;
import jiangsu.tbkt.teacher.bean.SelectClassBean;
import jiangsu.tbkt.teacher.bean.SettingManageBean;
import jiangsu.tbkt.teacher.bean.ShenFenBean;
import jiangsu.tbkt.teacher.bean.SubjectBean;
import jiangsu.tbkt.teacher.bean.SwitchShenFenBean;
import jiangsu.tbkt.teacher.bean.TemplateBean;
import jiangsu.tbkt.teacher.bean.UrlBean;
import jiangsu.tbkt.teacher.bean.User;
import jiangsu.tbkt.teacher.bean.VersionCheck;
import jiangsu.tbkt.teacher.object.DomainObject;
import jiangsu.tbkt.teacher.object.LoginObject;
import jiangsu.tbkt.teacher.object.VersionCkeckObject;
import jiangsu.tbkt.teacher.picker.CityBean;


/**
 * 数据请求
 */
public class RequestServer {


    /**
     * 登陆
     */
    public static void login(final Context context, String url, Object jsonParams, final Callback callback,
                             boolean isShowProgress, boolean isShowToast, boolean isPost) {
        ConnectToServer.connectionServer(context, url, jsonParams, new ConnectToServer.ConnCallBack() {
            @Override
            public void onSuccessBack(ResultBean result) {
                if (result != null) {
                    try {
                        LoginResultBean bean = LoginObject.getLoginResultBean(result);
                        PreferencesManager.getInstance().putInt("user_id",bean.getUser_id());
                        if (callback != null) {
                            callback.onSuccess(bean);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ResultBean rb = new ResultBean();
                        rb.setMessage("数据解析异常");
                        if (callback != null) {
                            callback.onFail(rb);
                        }
                    }
                } else {
                    if (callback != null) {
                        callback.onFail(result);
                    }
                }
            }

            @Override
            public void onFailBack(ResultBean result) {
                if (callback != null) {
                    callback.onFail(result);
                }
            }
        }, isShowProgress, isShowToast, isPost);
    }


    /**
     * syw 检测版本更新
     */
    public static void getVersionCheck(Context context, String url, Object jsonParams, final Callback callback,
                                       boolean isShowProgress, boolean isShowToast, boolean isPost) {
        ConnectToServer.connectionServer(context, url, jsonParams, new ConnectToServer.ConnCallBack() {
            @Override
            public void onSuccessBack(ResultBean result) {
                if (result != null) {
                    try {
                        VersionCheck versionCheck = VersionCkeckObject.parseVersionCheck(result);

                        if (callback != null) {
                            callback.onSuccess(versionCheck);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (callback != null) {
                            callback.onFail(result);
                        }
                    }
                } else {
                    if (callback != null) {
                        callback.onFail(result);
                    }
                }
            }

            @Override
            public void onFailBack(ResultBean result) {
                if (callback != null) {
                    callback.onFail(result);
                }
            }
        }, isShowProgress, isShowToast, isPost);
    }


    /**
     * syw 用户建议
     * syw 获取验证码
     * syw 修改姓名
     * syw 修改密码
     * syw 提交学校信息
     */
    public static void getResultBean(Context context, String url, Object jsonParams, final Callback callback,
                                     boolean isShowProgress, boolean isShowToast, boolean isPost) {
        ConnectToServer.connectionServer(context, url, jsonParams, new ConnectToServer.ConnCallBack() {
            @Override
            public void onSuccessBack(ResultBean result) {
                if (result != null) {
                    if (callback != null) {
                        callback.onSuccess(result);
                    }
                } else {
                    if (callback != null) {
                        callback.onFail(result);
                    }
                }
            }

            @Override
            public void onFailBack(ResultBean result) {
                if (callback != null) {
                    callback.onFail(result);
                }
            }
        }, isShowProgress, isShowToast, isPost);
    }

    /**
     * syw 设置页面
     */
    public static void getSetData(Context context, String url, Object jsonParams, final Callback callback,
                                  boolean isShowProgress, boolean isShowToast, boolean isPost) {
        ConnectToServer.connectionServer(context, url, jsonParams, new ConnectToServer.ConnCallBack() {
            @Override
            public void onSuccessBack(ResultBean result) {
                if (result != null) {
                    SettingManageBean settingManageBean = DomainObject.getSetData(result);
                    if (callback != null) {
                        callback.onSuccess(settingManageBean);
                    }
                } else {
                    if (callback != null) {
                        callback.onFail(result);
                    }
                }
            }

            @Override
            public void onFailBack(ResultBean result) {
                if (callback != null) {
                    callback.onFail(result);
                }
            }
        }, isShowProgress, isShowToast, isPost);
    }

    /**
     * syw 设置身份
     */
    public static void getShenFenData(Context context, String url, Object jsonParams, final Callback callback,
                                  boolean isShowProgress, boolean isShowToast, boolean isPost) {
        ConnectToServer.connectionServer(context, url, jsonParams, new ConnectToServer.ConnCallBack() {
            @Override
            public void onSuccessBack(ResultBean result) {
                if (result != null) {
                    ShenFenBean settingManageBean = DomainObject.getShenFen(result);
                    if (callback != null) {
                        callback.onSuccess(settingManageBean);
                    }
                } else {
                    if (callback != null) {
                        callback.onFail(result);
                    }
                }
            }

            @Override
            public void onFailBack(ResultBean result) {
                if (callback != null) {
                    callback.onFail(result);
                }
            }
        }, isShowProgress, isShowToast, isPost);
    }

    /**
     * syw 设置身份
     */
    public static void getTemplateData(Context context, String url, Object jsonParams, final Callback callback,
                                  boolean isShowProgress, boolean isShowToast, boolean isPost) {
        ConnectToServer.connectionServer(context, url, jsonParams, new ConnectToServer.ConnCallBack() {
            @Override
            public void onSuccessBack(ResultBean result) {
                if (result != null) {
                    TemplateBean settingManageBean = DomainObject.getTemplate(result);
                    if (callback != null) {
                        callback.onSuccess(settingManageBean);
                    }
                } else {
                    if (callback != null) {
                        callback.onFail(result);
                    }
                }
            }

            @Override
            public void onFailBack(ResultBean result) {
                if (callback != null) {
                    callback.onFail(result);
                }
            }
        }, isShowProgress, isShowToast, isPost);
    }

    /**
     * syw 设置身份
     */
    public static void switchShenFen(Context context, String url, Object jsonParams, final Callback callback,
                                  boolean isShowProgress, boolean isShowToast, boolean isPost) {
        ConnectToServer.connectionServer(context, url, jsonParams, new ConnectToServer.ConnCallBack() {
            @Override
            public void onSuccessBack(ResultBean result) {
                if (result != null) {
                    SwitchShenFenBean settingManageBean = DomainObject.getSwitchShenFen(result);
                    PreferencesManager.getInstance().putInt("user_id",settingManageBean.getData().getUser_id());
                    Log.e("syw","切换身份改变user_id:"+settingManageBean.getData().getUser_id());
                    if (callback != null) {
                        callback.onSuccess(settingManageBean);
                    }
                } else {
                    if (callback != null) {
                        callback.onFail(result);
                    }
                }
            }

            @Override
            public void onFailBack(ResultBean result) {
                if (callback != null) {
                    callback.onFail(result);
                }
            }
        }, isShowProgress, isShowToast, isPost);
    }

    /**
     * syw 获取县区数据
     */
    public static void getAreaData(Context context, String url, Object jsonParams, final Callback callback,
                                  boolean isShowProgress, boolean isShowToast, boolean isPost) {
        ConnectToServer.connectionServer(context, url, jsonParams, new ConnectToServer.ConnCallBack() {
            @Override
            public void onSuccessBack(ResultBean result) {
                if (result != null) {
                    CityBean settingManageBean = DomainObject.getAreaData(result);
                    if (callback != null) {
                        callback.onSuccess(settingManageBean);
                    }
                } else {
                    if (callback != null) {
                        callback.onFail(result);
                    }
                }
            }

            @Override
            public void onFailBack(ResultBean result) {
                if (callback != null) {
                    callback.onFail(result);
                }
            }
        }, isShowProgress, isShowToast, isPost);
    }


    /**
     * syw 获取班级数据
     */
    public static void getClassData(Context context, String url, Object jsonParams, final Callback callback,
                                   boolean isShowProgress, boolean isShowToast, boolean isPost) {
        ConnectToServer.connectionServer(context, url, jsonParams, new ConnectToServer.ConnCallBack() {
            @Override
            public void onSuccessBack(ResultBean result) {
                if (result != null) {
                    SelectClassBean settingManageBean = DomainObject.getClassData(result);
                    if (callback != null) {
                        callback.onSuccess(settingManageBean);
                    }
                } else {
                    if (callback != null) {
                        callback.onFail(result);
                    }
                }
            }

            @Override
            public void onFailBack(ResultBean result) {
                if (callback != null) {
                    callback.onFail(result);
                }
            }
        }, isShowProgress, isShowToast, isPost);
    }


    /**
     * syw 设置页面
     */
    public static void getSchoolListData(Context context, String url, Object jsonParams, final Callback callback,
                                   boolean isShowProgress, boolean isShowToast, boolean isPost) {
        ConnectToServer.connectionServer(context, url, jsonParams, new ConnectToServer.ConnCallBack() {
            @Override
            public void onSuccessBack(ResultBean result) {
                if (result != null) {
                    SchoolBean settingManageBean = DomainObject.getSchoolListData(result);
                    if (callback != null) {
                        callback.onSuccess(settingManageBean);
                    }
                } else {
                    if (callback != null) {
                        callback.onFail(result);
                    }
                }
            }

            @Override
            public void onFailBack(ResultBean result) {
                if (callback != null) {
                    callback.onFail(result);
                }
            }
        }, isShowProgress, isShowToast, isPost);
    }

    /**
     * syw 获取上传头像的url
     */
    public static void getUrl(Context context, String url, Object jsonParams, final Callback callback,
                                  boolean isShowProgress, boolean isShowToast, boolean isPost) {
        ConnectToServer.connectionServer(context, url, jsonParams, new ConnectToServer.ConnCallBack() {
            @Override
            public void onSuccessBack(ResultBean result) {
                if (result != null) {
                    UrlBean settingManageBean = DomainObject.getUrlData(result);
                    if (callback != null) {
                        callback.onSuccess(settingManageBean);
                    }
                } else {
                    if (callback != null) {
                        callback.onFail(result);
                    }
                }
            }

            @Override
            public void onFailBack(ResultBean result) {
                if (callback != null) {
                    callback.onFail(result);
                }
            }
        }, isShowProgress, isShowToast, isPost);
    }




    /**
     * syw 找回密码
     */
    public static void getNewPassWord(Context context, String url, Object jsonParams, final Callback callback,
                                      boolean isShowProgress, boolean isShowToast, boolean isPost) {
        ConnectToServer.connectionServer(context, url, jsonParams, new ConnectToServer.ConnCallBack() {
            @Override
            public void onSuccessBack(ResultBean result) {
                if (result != null) {
                    if (callback != null) {
                        callback.onSuccess(result);
                    }
                } else {
                    if (callback != null) {
                        callback.onFail(result);
                    }
                }
            }

            @Override
            public void onFailBack(ResultBean result) {
                if (callback != null) {
                    callback.onFail(result);
                }
            }
        }, isShowProgress, isShowToast, isPost);
    }


    public static void getNotifyClassData(Context context, String url, Object jsonParams, final Callback callback,
                                      boolean isShowProgress, boolean isShowToast, boolean isPost) {
        ConnectToServer.connectionServer(context, url, jsonParams, new ConnectToServer.ConnCallBack() {
            @Override
            public void onSuccessBack(ResultBean result) {
                if (result != null) {
                    NotifyClassBean settingManageBean = DomainObject.getNotifyData(result);
                    if (callback != null) {
                        callback.onSuccess(settingManageBean);
                    }
                } else {
                    if (callback != null) {
                        callback.onFail(result);
                    }
                }
            }

            @Override
            public void onFailBack(ResultBean result) {
                if (callback != null) {
                    callback.onFail(result);
                }
            }
        }, isShowProgress, isShowToast, isPost);
    }


    public static void getNotifyStudentData(Context context, String url, Object jsonParams, final Callback callback,
                                          boolean isShowProgress, boolean isShowToast, boolean isPost) {
        ConnectToServer.connectionServer(context, url, jsonParams, new ConnectToServer.ConnCallBack() {
            @Override
            public void onSuccessBack(ResultBean result) {
                if (result != null) {
                    User settingManageBean = DomainObject.getStudentsData(result);
                    List<User.StudentsBean> students = settingManageBean.getStudents();
                    if (callback != null) {
                        callback.onSuccess(students);
                    }
                } else {
                    if (callback != null) {
                        callback.onFail(result);
                    }
                }
            }

            @Override
            public void onFailBack(ResultBean result) {
                if (callback != null) {
                    callback.onFail(result);
                }
            }
        }, isShowProgress, isShowToast, isPost);
    }

    public static void getSubjectData(Context context, String url, Object jsonParams, final Callback callback,
                                      boolean isShowProgress, boolean isShowToast, boolean isPost) {
        ConnectToServer.connectionServer(context, url, jsonParams, new ConnectToServer.ConnCallBack() {
            @Override
            public void onSuccessBack(ResultBean result) {
                if (result != null) {
                    SubjectBean settingManageBean = DomainObject.getSubjectData(result);
                    List<SubjectBean.DataBean> subjects = settingManageBean.getData();
                    if (callback != null) {
                        callback.onSuccess(subjects);
                    }
                } else {
                    if (callback != null) {
                        callback.onFail(result);
                    }
                }
            }

            @Override
            public void onFailBack(ResultBean result) {
                if (callback != null) {
                    callback.onFail(result);
                }
            }
        }, isShowProgress, isShowToast, isPost);
    }

    public static void switchSubject(Context context, String url, Object jsonParams, final Callback callback,
                                     boolean isShowProgress, boolean isShowToast, boolean isPost) {
        ConnectToServer.connectionServer(context, url, jsonParams, new ConnectToServer.ConnCallBack() {
            @Override
            public void onSuccessBack(ResultBean result) {
                if (result != null) {
                    if (callback != null) {
                        callback.onSuccess(result);
                    }
                } else {
                    if (callback != null) {
                        callback.onFail(result);
                    }
                }
            }

            @Override
            public void onFailBack(ResultBean result) {
                if (callback != null) {
                    callback.onFail(result);
                }
            }
        }, isShowProgress, isShowToast, isPost);
    }


    /**
     * 错误日志
     */
    public static void crashUpload(final Context context, String url, Object jsonParams, final Callback callback,
                                   boolean isShowProgress, boolean isShowToast, boolean isPost) {
        ConnectToServer.connectionServer(context, url, jsonParams, new ConnectToServer.ConnCallBack() {
            @Override
            public void onSuccessBack(ResultBean result) {
                if (result != null) {
                    if (callback != null) {
                        callback.onSuccess(result);
                    }
                } else {
                    if (callback != null) {
                        callback.onFail(result);
                    }
                }
            }

            @Override
            public void onFailBack(ResultBean result) {
                if (callback != null) {
                    callback.onFail(result);
                }
            }
        }, isShowProgress, isShowToast, isPost);
    }

    /**
     * 回调
     */
    public static interface Callback {
        void onSuccess(Object object);

        void onFail(Object object);
    }
}
