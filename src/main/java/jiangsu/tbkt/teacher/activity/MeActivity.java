package jiangsu.tbkt.teacher.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJBitmap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import jiangsu.tbkt.teacher.R;
import jiangsu.tbkt.teacher.api.RequestServer;
import jiangsu.tbkt.teacher.application.PreferencesManager;
import jiangsu.tbkt.teacher.bean.ResultBean2;
import jiangsu.tbkt.teacher.bean.SettingManageBean;
import jiangsu.tbkt.teacher.bean.ShenFenBean;
import jiangsu.tbkt.teacher.bean.VersionCheck;
import jiangsu.tbkt.teacher.object.ResultBeanObject;
import jiangsu.tbkt.teacher.utils.Constant;
import jiangsu.tbkt.teacher.utils.DialogUtil;
import jiangsu.tbkt.teacher.utils.MyToastUtils;
import jiangsu.tbkt.teacher.utils.Tools;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by song on 2017/2/13 0013.
 */
public class MeActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_head;
    private PopupWindow popupWindow;
    private SettingManageBean manageBean;
    private Button load_again;
    private LinearLayout fail_layout;
    private LinearLayout set_layout;
    private TextView tv_name;
    private RelativeLayout set_name;
    private RelativeLayout set_person;
    private RelativeLayout set_fankui;
    private RelativeLayout set_banben;
    //    private RelativeLayout set_detail;
    private RelativeLayout set_jiaocai;
    private RelativeLayout set_jiaofu;
    private RelativeLayout set_diandu;
    private RelativeLayout set_subject;
    private TextView tv_version;
    private RelativeLayout set_pwd;
    private KJBitmap kjBitmap;
    private Button bt_set_tuichu;
    private PopupWindow checkOutWindow;
    private Drawable drawable;
    private VersionCheck versionCheck;
    private ImageView top_btnback;
    private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        initView();
        initListener();
        kjBitmap = new KJBitmap();
        prefs = getPreferences(0);
    }


    private void initView() {
        //点击头像
        iv_head = (ImageView) findViewById(R.id.iv_set_head);
        //判断网络状态时的布局
        set_layout = (LinearLayout) findViewById(R.id.set_layout);
        fail_layout = (LinearLayout) findViewById(R.id.fail_layout);
        load_again = (Button) findViewById(R.id.reload_btn);

        //设置姓名，填充姓名
        set_name = (RelativeLayout) findViewById(R.id.set_name);
        tv_name = (TextView) findViewById(R.id.tv_name);
        //修改密码
        set_pwd = (RelativeLayout) findViewById(R.id.set_pwd);
        //设置身份
        set_person = (RelativeLayout) findViewById(R.id.set_person);
        //建议反馈
        set_fankui = (RelativeLayout) findViewById(R.id.set_fankui);
        //版本更新
        set_banben = (RelativeLayout) findViewById(R.id.set_banben);
        tv_version = (TextView) findViewById(R.id.tv_version);
        //退出登录
        bt_set_tuichu = (Button) findViewById(R.id.bt_set_tuichu);

        top_btnback = (ImageView) findViewById(R.id.top_btnback);

//        syw 临时跳转按钮
//        set_detail = (RelativeLayout) findViewById(R.id.set_detail);
        set_jiaocai = (RelativeLayout) findViewById(R.id.set_jiaocai);
        set_jiaofu = (RelativeLayout) findViewById(R.id.set_jiaofu);
        set_diandu = (RelativeLayout) findViewById(R.id.set_diandu);

        set_subject = (RelativeLayout) findViewById(R.id.set_subject);
    }

    private void initListener() {
        iv_head.setOnClickListener(this);
        set_name.setOnClickListener(this);
        set_pwd.setOnClickListener(this);
        set_person.setOnClickListener(this);
        set_fankui.setOnClickListener(this);
        set_banben.setOnClickListener(this);
        bt_set_tuichu.setOnClickListener(this);
        top_btnback.setOnClickListener(this);
//        set_detail.setOnClickListener(this);
        set_jiaocai.setOnClickListener(this);
        set_jiaofu.setOnClickListener(this);
        set_diandu.setOnClickListener(this);
        set_subject.setOnClickListener(this);
    }

    //onActivityResult() -> onStart() -> onResume()
    @Override
    public void onResume() {
        super.onResume();
        if (isResume) {
            getAccountHttpData();
        }
    }

    public void getAccountHttpData() {

        RequestServer.getSetData(this, Constant.subManageInterf, null, new RequestServer.Callback() {
            @Override
            public void onSuccess(Object object) {
                fail_layout.setVisibility(View.GONE);
                set_layout.setVisibility(View.VISIBLE);
                manageBean = (SettingManageBean) object;
                PreferencesManager.getInstance().putInt("platform_id", manageBean.getPlatform_id());
                initData();
            }

            @Override
            public void onFail(Object object) {
                set_layout.setVisibility(View.GONE);
                PreferencesManager.getInstance().putInt("platform_id", 0);
                fail_layout.setVisibility(View.VISIBLE);
                load_again.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getAccountHttpData();
                    }
                });
            }
        }, true, false, true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.loadUrlFromMe("HOME_URL");
    }

    /**
     * 数据请求完成之后，填充信息
     */
    public String jiaocaiUrl = "";
    public String jiaofuUrl = "";
    public String dianduUrl = "";
    public String notifyUrl = "";

    private void initData() {
        //获取头像
        if (TextUtils.isEmpty(manageBean.getPortrait())) {
            iv_head.setBackgroundResource(R.mipmap.ic_launcher);
        } else {
            kjBitmap.display(iv_head, manageBean.getPortrait());
        }

//        syw 2018/3/20 不判断是不是宿迁，只保留江苏。显示选择学科，取消修改姓名修改密码，通知
        iv_head.setClickable(false);
        if (manageBean.getIs_suqian()==0){
//            非宿迁用户
//            iv_head.setClickable(false);
//            set_name.setVisibility(View.GONE);
//            set_pwd.setVisibility(View.GONE);
//            set_person.setVisibility(View.GONE);
//            set_subject.setVisibility(View.VISIBLE);
        }else{
//            宿迁用户
//            iv_head.setClickable(true);
//            set_name.setVisibility(View.VISIBLE);
//            set_pwd.setVisibility(View.VISIBLE);
//            set_person.setVisibility(View.VISIBLE);
//            set_subject.setVisibility(View.GONE);
        }
//        syw 设置教师名字
        tv_name.setText(manageBean.getName());

        String is_show = PreferencesManager.getInstance().getString("is_show", "1");
//        syw 全部不显示
        if ("0".equals(is_show)) {
            set_jiaocai.setVisibility(View.GONE);
            set_jiaofu.setVisibility(View.GONE);
            set_diandu.setVisibility(View.GONE);
//            set_detail.setVisibility(View.GONE);
        } else {
            if (manageBean.getSubject_id() == 91) {
//            syw 英语
                set_jiaocai.setVisibility(View.VISIBLE);
                set_jiaofu.setVisibility(View.GONE);
                set_diandu.setVisibility(View.VISIBLE);
//            syw 教材讲解
                jiaocaiUrl = PreferencesManager.getInstance().getString("vueteayy", "http://teayy.m.jxtbkt.com") + "/en/teaching";
                dianduUrl = PreferencesManager.getInstance().getString("vueteayy", "http://teayy.m.jxtbkt.com") + "/en/teareading/0";
            } else if (manageBean.getSubject_id() == 21) {
//            syw 小学数学
                set_jiaocai.setVisibility(View.VISIBLE);
                set_jiaofu.setVisibility(View.VISIBLE);
                set_diandu.setVisibility(View.GONE);
//            syw 数学教材讲解
                jiaocaiUrl = PreferencesManager.getInstance().getString("vueteasx", "http://teasx.m.jxtbkt.com") + "/sx/mathbook/sectionList";
                jiaofuUrl = PreferencesManager.getInstance().getString("vueteasx", "http://teasx.m.jxtbkt.com") + "/sx/mathassist/sectionList";
            } else if (manageBean.getSubject_id() == 22) {
//            syw 初中数学
                set_jiaocai.setVisibility(View.GONE);
                set_jiaofu.setVisibility(View.GONE);
                set_diandu.setVisibility(View.GONE);
            } else if (manageBean.getSubject_id() == 51) {
//            syw 语文
                set_jiaocai.setVisibility(View.GONE);
                set_jiaofu.setVisibility(View.GONE);
                set_diandu.setVisibility(View.GONE);
            }
        }


//        syw 版本检测
        versionCheck1();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_btnback:
                MainActivity.loadUrlFromMe("HOME_URL");
                finish();
                break;
            //设置头像
            case R.id.iv_set_head:
                showHeadPopWindow();
                break;
            //从相册获取头像
            case R.id.tv_xiangce:
                popupWindow.dismiss();
                albumForHead();
                break;
            //拍照获取头像
            case R.id.tv_paizhao:
                popupWindow.dismiss();
                takePhotoForHead();
                break;
            //取消设置头像
            case R.id.tv_quxiao:
                popupWindow.dismiss();
                break;
            //修改姓名
            case R.id.set_name:
                Intent intent1 = new Intent(this, SetNameActivity.class);
                intent1.putExtra("name", tv_name.getText().toString().trim());
                startActivity(intent1);
                break;
            //修改密码
            case R.id.set_pwd:
                Intent intent2 = new Intent(this, SetPasswordActivity.class);
                startActivity(intent2);
                break;
            //修改班级
            case R.id.set_person:
                //syw 跳转到设置身份界面
                choiceShenFen();
                break;
            //用户反馈
            case R.id.set_fankui:
                Intent intent4 = new Intent(this, UserAdviceActivity.class);
                startActivity(intent4);
                break;
            //版本更新
            case R.id.set_banben:
//                syw RemindDialog设置取消
                if (versionCheck == null) {
                    return;
                }
                //新版本检测
                if (versionCheck.getUpdate().equals("2")) {//0没有   1有  2强制升级
                    new DialogUtil() {
                        @Override
                        public void positiveContent() {
                            openDialog(versionCheck.getDownload());
                        }

                        @Override
                        public void middleContent() {
                        }

                        @Override
                        public void negativeContent() {
                        }
                    }.singleDialog(this,
                            "更新啦",
                            versionCheck.getContent(), "立即更新");

                } else if (versionCheck.getUpdate().equals("1")) {
                    new DialogUtil() {
                        @Override
                        public void positiveContent() {
                            openDialog(versionCheck.getDownload());
                        }

                        @Override
                        public void middleContent() {
                        }

                        @Override
                        public void negativeContent() {
                            PreferencesManager.getInstance().putString("last_day", Tools.getNowTimeWithFormat("yyyy-MM-dd"));
                            prefs.edit().putLong("lastUpdateTime", System.currentTimeMillis()).commit();
                        }
                    }.doubleDialog(this,
                            "更新啦",
                            versionCheck.getContent(), "立即更新", "稍后");
                } else {
                    MyToastUtils.toastText(this, "已是最新版本");
                }
                break;
            //退出
            case R.id.bt_set_tuichu:
                showCheckOutPopWindow();
                break;
            case R.id.tv_quxiao_tuichu:
                checkOutWindow.dismiss();
                break;
            case R.id.tv_tuichu:
                checkOutWindow.dismiss();
                checkOut();
                break;
            case R.id.set_detail:
                notifyUrl = PreferencesManager.getInstance().getString("vueteacom", "http://teacom.m.jxtbkt.com") + "/notice/list";
                MainActivity.loadUrlFromMe(notifyUrl + "?tbkt_token=" + PreferencesManager.getInstance().getString("sessionid", ""));
                finish();
                break;

            case R.id.set_jiaocai:
                MainActivity.loadUrlFromMe(jiaocaiUrl + "?tbkt_token=" + PreferencesManager.getInstance().getString("sessionid", ""));
                finish();
                break;

            case R.id.set_jiaofu:
                MainActivity.loadUrlFromMe(jiaofuUrl + "?tbkt_token=" + PreferencesManager.getInstance().getString("sessionid", ""));
                finish();
                break;

            case R.id.set_diandu:
                MainActivity.loadUrlFromMe(dianduUrl + "?tbkt_token=" + PreferencesManager.getInstance().getString("sessionid", ""));
                finish();
                break;

            case R.id.set_subject:
                Intent intent = new Intent(this, ChoiceSubjectActivity.class);
                startActivity(intent);
                break;
        }
    }

    public static Toast toast;

    private void choiceShenFen() {
        RequestServer.getShenFenData(this, Constant.shenFenInterf, null, new RequestServer.Callback() {
            @Override
            public void onSuccess(Object object) {
                ShenFenBean shenFenBean = (ShenFenBean) object;
                List<ShenFenBean.DataBean> data = shenFenBean.getData();
                if (data != null && data.size() == 1) {
                    if (toast == null) {
                        toast = Toast.makeText(MeActivity.this, "你只有一个身份,不用选择", Toast.LENGTH_SHORT);
                    } else {
                        toast.setText("你只有一个身份,不用选择");
                        toast.setDuration(Toast.LENGTH_SHORT);
                    }
                    toast.show();
//                    MyToastUtils.toastText(MeActivity.this,"你只有一个身份,不用选择");
                } else {
                    Intent intent = new Intent(MeActivity.this, ChoiceShenFenActivity.class);
                    intent.putExtra("shenFenBean", shenFenBean);
                    startActivity(intent);
                }
            }

            @Override
            public void onFail(Object object) {

            }
        }, true, true, true);
    }

    /**
     * 弹出设置头像popupwindow
     * 也可以直接做一个pop直接全屏，view设置背景
     */
    private void showHeadPopWindow() {
        View convertview = View.inflate(this, R.layout.pop_setting, null);
        TextView tv_xiangce = (TextView) convertview.findViewById(R.id.tv_xiangce);
        TextView tv_paizhao = (TextView) convertview.findViewById(R.id.tv_paizhao);
        TextView tv_quxiao = (TextView) convertview.findViewById(R.id.tv_quxiao);
        tv_xiangce.setOnClickListener(this);
        tv_paizhao.setOnClickListener(this);
        tv_quxiao.setOnClickListener(this);
        //无背景，无焦点，里边按钮不可点击，消失。
        popupWindow = new PopupWindow(convertview, ViewGroup.LayoutParams.MATCH_PARENT, -2, true);
        //设置一个透明的背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //显示在整个view的底部
        if (Build.VERSION.SDK_INT < 24) {
            popupWindow.showAtLocation(iv_head, Gravity.BOTTOM, 0, 0);
        } else {
            int distance = getWinHeight() - getViewHeight(convertview);
            popupWindow.showAtLocation(iv_head, Gravity.NO_GRAVITY, 0, distance);
        }
        popupWindow.setAnimationStyle(R.style.popwindow_anim_style);
        popupWindow.update();
        backgroundAlpha(0.5f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    public int getWinHeight() {
        WindowManager wm = getWindowManager();
        return wm.getDefaultDisplay().getHeight();
    }

    public int getViewHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredHeight();
    }

    /**
     * 设置透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }


    /**
     * 请求码
     */
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    /**
     * 头像名称
     */
    private static final String IMAGE_FILE_NAME = "image.jpg";

    /**
     * 从相册获取头像
     */
    private void albumForHead() {
        Intent intentFromGallery = new Intent();
        intentFromGallery.setType("image/png*"); // 设置文件类型
        intentFromGallery.setAction(Intent.ACTION_PICK);
        startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
    }

    /**
     * 拍照设置头像
     */
    private void takePhotoForHead() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可以用，可用进行存储
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            File file = new File(path, IMAGE_FILE_NAME);
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        }
        startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
    }


    private String picPath;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //拍照获取头像
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            // 判断存储卡是否可以用，可用进行存储
            String state = Environment.getExternalStorageState();
            if (state.equals(Environment.MEDIA_MOUNTED)) {
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                File tempFile = new File(path, IMAGE_FILE_NAME);
                startPhotoZoom(Uri.fromFile(tempFile));
            } else {
                Toast.makeText(MeActivity.this, "未找到存储卡", Toast.LENGTH_SHORT).show();
            }
        }
        //从相册获取头像
        if (requestCode == IMAGE_REQUEST_CODE) {

            if (data != null && data.getData() != null) {

                String[] pojo = {MediaStore.Images.Media.DATA};
                @SuppressWarnings("deprecation")
                Cursor cursor = managedQuery(data.getData(), pojo, null, null, null);
                if (cursor != null) {
                    int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
                    cursor.moveToFirst();
                    picPath = cursor.getString(columnIndex);
//                cursor.close();
                }
                if (picPath != null && (picPath.endsWith(".gif") || picPath.endsWith(".GIF"))) {
                    MyToastUtils.toastText(MeActivity.this, "暂不支持gif图片");
                    return;
                }

                startPhotoZoom(data.getData());
            }
        }
        //图片修剪之后返回结果
        if (requestCode == RESULT_REQUEST_CODE) {
            if (data != null) {
                try {
                    isResume = false;
                    getImageToView(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    boolean isResume = true;

    @Override
    public void onPause() {
        super.onPause();
        isResume = true;
    }

    /**
     * 裁剪图片方法实现
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 340);
        intent.putExtra("outputY", 340);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    private void getImageToView(Intent data) throws IOException {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            drawable = new BitmapDrawable(this.getResources(), photo);
            BitmapDrawable bd = (BitmapDrawable) drawable;
            Bitmap bm = bd.getBitmap();
            if (bm == null) {
                return;
            }
            final File file = saveFile(bm, "head");

//            getUpLoadUrl(file);
            uploadMyFile(file);
        }
    }

    /**
     * 获取一个上传的url
     *
     * @param file
     */

//    private void getUpLoadUrl(final File file) {
//        JSONObject params = new JSONObject();
//        try {
//            params.put("dir", "portrait");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        RequestServer.getUrl(this, Constant.getUrlInterf, params.toString(), new RequestServer.Callback() {
//            @Override
//            public void onSuccess(Object object) {
//                UrlBean bean = (UrlBean) object;
//                Log.e("syw", "上传头像路径:" + bean.getUrl());
//                uploadMyFile(file, bean);
//            }
//
//            @Override
//            public void onFail(Object object) {
//                Log.e("syw", "onFail");
//            }
//        }, false, true, true);
//    }


    /**
     * 将bitmap保存为File
     *
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public File saveFile(Bitmap bm, String fileName) throws IOException {
        String path = Environment.getExternalStorageDirectory().getPath() + "/revoeye/";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();

        return myCaptureFile;
    }


    private void uploadMyFile(File file) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        String url = PreferencesManager.getInstance().getString("upload_url", "") + "portrait";
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("syw", "onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final ResultBean2 bean = ResultBeanObject.getResultBean2(response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("syw", "将文件上传之后保存的路径:" + bean.file_url);
                        saveBitmap(bean.file_url);
                    }
                });
            }
        });
    }


    private void saveBitmap(String file_url) {
        JSONObject params = new JSONObject();
        try {
            params.put("img_url", file_url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestServer.getResultBean(this, Constant.saveBitmapInterf, params.toString(), new RequestServer.Callback() {
            @Override
            public void onSuccess(Object object) {
                iv_head.setImageDrawable(drawable);
                MyToastUtils.toastText(MeActivity.this, "头像上传成功");
            }

            @Override
            public void onFail(Object object) {
                MyToastUtils.toastText(MeActivity.this, "头像上传失败");
            }
        }, false, true, true);
    }

    /**
     * 获取版本号
     *
     * @return 字符串的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "未发现版本号";
        }
    }

    /**
     * 版本检测
     */
    private void versionCheck1() {
        String url = Constant.newVersonInterf;
        JSONObject params = new JSONObject();
        try {
            params.put("type", 10);
            params.put("api", Tools.getAppVersionCode(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestServer.getVersionCheck(this, url, params.toString(), new RequestServer.Callback() {
            @Override
            public void onSuccess(Object object) {
                versionCheck = (VersionCheck) object;
                //新版本检测  0没有   1有  2强制升级
                if (versionCheck.getUpdate().equals("2") || versionCheck.getUpdate().equals("1")) {
                    tv_version.setText("发现新版本，立即更新");
                    tv_version.setTextColor(Color.parseColor("#ff6600"));
                } else {
                    tv_version.setText("当前版本:" + getVersion());
                    tv_version.setTextColor(Color.parseColor("#aaaaaa"));
                }
            }

            @Override
            public void onFail(Object object) {
                tv_version.setText("当前版本:" + getVersion());
                tv_version.setTextColor(Color.parseColor("#aaaaaa"));
            }
        }, true, false, true);
    }

    /**
     * 在对话框中下载新的APK
     */
    private void openDialog(final String url) {

        final ProgressDialog pd;    //进度条对话框
        pd = new ProgressDialog(MeActivity.this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.setCancelable(false);
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = getFileFromServer(url, pd);
                    sleep(3000);//3秒之后执行自动安装
                    installApk(file);
                    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                }
            }
        }.start();
    }

    public static File getFileFromServer(String path, ProgressDialog pd) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                //获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

    //安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }

    /**
     * 弹出退出登录的popwindow
     */
    private void showCheckOutPopWindow() {
        View convertview = View.inflate(this, R.layout.pop_checkout, null);
        //取消和退出的监听
        convertview.findViewById(R.id.tv_quxiao_tuichu).setOnClickListener(this);
        convertview.findViewById(R.id.tv_tuichu).setOnClickListener(this);
        checkOutWindow = new PopupWindow(convertview, ViewGroup.LayoutParams.MATCH_PARENT, -2, true);
        checkOutWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        //显示在整个view的底部
        if (Build.VERSION.SDK_INT < 24) {
            checkOutWindow.showAtLocation(iv_head, Gravity.CENTER, 0, 0);
        } else {
            int distance = getWinHeight() - getViewHeight(convertview);
            checkOutWindow.showAtLocation(iv_head, Gravity.NO_GRAVITY, 0, distance / 2);
        }
        checkOutWindow.setAnimationStyle(R.style.popwindow_anim_style);
        checkOutWindow.update();
        backgroundAlpha(0.5f);
        checkOutWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void checkOut() {
        // 保存退出状态 0为退出状态 1为登录状态

        //修改时间 2015-06-10 09:39:35 zhangerbin
        //boolean success = preferences.saveIsExistDValue("isExist", "0");
        //preferences.deleteAccountValue(preferences
        //.queryAccountValue("account"));
                /*this.finish();
                android.os.Process.killProcess(android.os.Process.myPid());*/
        PreferencesManager.getInstance().putString("isExist1", "0");
        PreferencesManager.getInstance().putString("sessionid", "");
        PreferencesManager.getInstance().putInt("user_id", 0);
        PreferencesManager.getInstance().putString("tea_name", "");
        PreferencesManager.getInstance().putBoolean("bindDevice", false);
        PreferencesManager.getInstance().putString("sub_id", "");//清空教师数据
        Intent i = new Intent(this, WebActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    /**
     * 隐藏Popupwindow
     *
     * @param window
     */
    public void hideWindow(PopupWindow window) {
        if (window != null && window.isShowing()) {
            window.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideWindow(popupWindow);
        hideWindow(checkOutWindow);
    }
}
