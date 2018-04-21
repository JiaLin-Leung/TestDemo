package jiangsu.tbkt.teacher.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jiangsu.tbkt.teacher.R;
import jiangsu.tbkt.teacher.api.RequestServer;
import jiangsu.tbkt.teacher.application.PreferencesManager;
import jiangsu.tbkt.teacher.bean.NotifyClassBean;
import jiangsu.tbkt.teacher.bean.ResultBean2;
import jiangsu.tbkt.teacher.object.ResultBeanObject;
import jiangsu.tbkt.teacher.picker.ChooseCityInterface;
import jiangsu.tbkt.teacher.picker.ChooseTimeUtil;
import jiangsu.tbkt.teacher.utils.BitmapUtil;
import jiangsu.tbkt.teacher.utils.Constant;
import jiangsu.tbkt.teacher.utils.FileUtils;
import jiangsu.tbkt.teacher.utils.FinalNumInter;
import jiangsu.tbkt.teacher.utils.LoadingUtil;
import jiangsu.tbkt.teacher.utils.MyToastUtils;
import jiangsu.tbkt.teacher.view.DragGridView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/10/8 0008.
 */
public class SendTaskActivity extends BaseActivity implements View.OnClickListener {

    private static final int PHOTOCODE = 100;
    private static final int PHOTORESULT = -1;
    private static final int REQUEST_NOTIFY = 101;
    private ImageView top_btnback;
    private TextView top_infotxt;
    private Button bt_confirm;
    private EditText et_notify;
    private PopupWindow tooLongWindow;
    private PopupWindow checkOutWindow;
    private ListView lv_classes;
    private LinearLayout fail_layout;
    private Button reload_btn;
    private List<NotifyClassBean.UnitsBean> notifyClassBeanList;
    private String notify;
    private MyAdapter adapter;
    private TextView tv_confirm;

    private List<String> dataSourceList = new ArrayList<>();
    private ImageAdaper imageAdaper;
    private DragGridView dragGridView;
    private TextView tv_time;
    private String content, type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sendtask);

        type = getIntent().getStringExtra("type");
        content = getIntent().getStringExtra("content");

        //填充view
        initView();

        initGridView();

        getClassListData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //syw 请求数据成功之后，填充列表
        if (notifyClassBeanList != null) {
            adapter.notifyDataSetChanged();
            setListViewHeight();
        }
    }

    private void initGridView() {
        imageAdaper = new ImageAdaper(this, dataSourceList);
        dragGridView.setAdapter(imageAdaper);
        dragGridView.setOnChangeListener(new DragGridView.OnChanageListener() {
            @Override
            public void onChange(int form, int to) {
                String temp = (String) imageAdaper.getItem(form);
                if (to < dataSourceList.size()) {
                    Collections.swap(dataSourceList, form, to);
                } else {
                    dataSourceList.add(dataSourceList.remove(form));
                }
                dataSourceList.set(to, temp);
                imageAdaper.notifyDataSetChanged();
                setGridViewHeight(dataSourceList);
            }

            @Override
            public boolean onDown(int position) {
                if (position == imageAdaper.getCount() - 1 || position == imageAdaper.getCount() - 2) {
                    return false;
                } else {
//						HashMap<String, Object> map = (HashMap<String, Object>) adaper.getItem(position);
//						getbitmap(map,mImageView);
                    return true;
                }
            }
        });
        dragGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideInput();
                ImageAdaper adapter = (ImageAdaper) parent.getAdapter();
                if (adapter.objects.size() < 9 && position == parent.getCount() - 1) {
                    showHeadPopWindow();
                } else {
                    String map = (String) adapter.getItem(position);
                    if (adapter.getShowDel()) {
                        if (null == adapter.getItem(0)) {
//                            mImageView.setImageResource(ProjectClassificationHelper.getIncitaionEventsUriStyle(type));
                        } else {
//                            BitmapUtil.setImageViewByImagLoading((String) adaper.getItem(0), mImageView);
//                            dataSourceList.remove(map);
//                            adapter.update(dataSourceList);
                        }
                    } else {
//                        BitmapUtil.setImageViewByImagLoading(map, mImageView);
                    }
                }
            }
        });
    }

    public void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et_notify, InputMethodManager.SHOW_FORCED);
        imm.hideSoftInputFromWindow(et_notify.getWindowToken(), 0); //强制隐藏键盘
    }

    /**
     * 弹出设置头像popupwindow
     * 也可以直接做一个pop直接全屏，view设置背景
     */
    private PopupWindow popupWindow;

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
            popupWindow.showAtLocation(lv_classes, Gravity.BOTTOM, 0, 0);
        } else {
            int distance = getWinHeight() - getViewHeight(convertview);
            popupWindow.showAtLocation(lv_classes, Gravity.NO_GRAVITY, 0, distance);
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

    int picIndex = 0;
    //    syw 保存服务器返回图片路径
    ArrayList<String> picPaths = new ArrayList<>();

    // syw 上传图片功能
    public void uploadPictures() {
        LoadingUtil.showDialog(this);
        File file = null;
        if (picIndex >= dataSourceList.size()) {
            return;
        } else {
            file = new File(dataSourceList.get(picIndex));
        }

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        String url = PreferencesManager.getInstance().getString("upload_url", "") + "notify";
        Log.e("syw", "url:" + url);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                uploadPictures();
                Log.e("syw", "上传头像失败:" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final ResultBean2 bean = ResultBeanObject.getResultBean2(response.body().string());
                picPaths.add(bean.file_url);

                if (picIndex == dataSourceList.size() - 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LoadingUtil.dismissDialog();
                            sendNotify();
                        }
                    });

                }

                picIndex++;
                uploadPictures();
            }
        });
    }

    private String classids = "";

    private void sendNotify() {
        JSONObject params = new JSONObject();

        if (classids.endsWith(",")) {
            classids = classids.substring(0, classids.length() - 1);
        }

//        syw 拼接图片id
        JSONArray images = new JSONArray();
        for (int i = 0; i < picPaths.size(); i++) {
            images.put(picPaths.get(i));
        }

        String end_time = tv_time.getText().toString().trim();
        if (TextUtils.isEmpty(end_time) || end_time.contains("null")) {
            MyToastUtils.toastText(SendTaskActivity.this, "请选择截止日期");
            return;
        }

        try {
            params.put("content", et_notify.getText().toString().trim());
            params.put("end_time", end_time);
            params.put("images", images);
            params.put("unit_id", classids);
            params.put("type", Integer.parseInt(type));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("syw", "params.toString():" + params.toString());
        RequestServer.getResultBean(this, Constant.savehuodongInterf, params.toString(), new RequestServer.Callback() {
            @Override
            public void onSuccess(Object object) {
//                syw 清空照片列表
                dataSourceList.clear();
//                syw 清空上传路径列表
                picPaths.clear();
//                syw 清空选择班级
                classids = "";

                imageAdaper.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
                setGridViewHeight(dataSourceList);

                MyToastUtils.toastText(SendTaskActivity.this, "发送成功");
                finish();
                MainActivity.loadUrlFromMe("HOME_URL");
            }

            @Override
            public void onFail(Object object) {
                MyToastUtils.toastText(SendTaskActivity.this, "发送失败");
            }
        }, true, true, true);
    }

    private CharSequence temp;
    private int editStart;
    private int editEnd;
    private String str_text;
    public static Toast toast;

    private void initView() {
        //返回按钮
        top_btnback = (ImageView) findViewById(R.id.top_btnback);
        top_btnback.setOnClickListener(this);

        //标题
        top_infotxt = (TextView) findViewById(R.id.top_infotxt);
        top_infotxt.setText("课外活动");
        //确定发送按钮
        bt_confirm = (Button) findViewById(R.id.bt_confirm);
        bt_confirm.setVisibility(View.VISIBLE);
        bt_confirm.setOnClickListener(this);
        bt_confirm.setText("发送");
//        上传图片工具

        dragGridView = (DragGridView) findViewById(R.id.dragGridView);
        //通知内容
        et_notify = (EditText) findViewById(R.id.et_notify);
        et_notify.setPadding(10, 10, 10, 0);
        et_notify.setText(content);
        et_notify.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
                temp = s;
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editStart = et_notify.getSelectionStart();
                editEnd = et_notify.getSelectionEnd();
                str_text = et_notify.getText().toString();
                String str = String.valueOf(temp).trim();
                if (str.length() > 200) {

                    if (toast==null){
                        toast = Toast.makeText(SendTaskActivity.this, "字数不能超过200字", Toast.LENGTH_SHORT);
                    }else{
                        toast.setText("字数不能超过200字");
                        toast.setDuration(Toast.LENGTH_SHORT);
                    }

                    toast.show();

                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    et_notify.setText(s);
                    et_notify.setSelection(tempSelection);
                }
                switchButton();
            }
        });
        //加载失败
        fail_layout = (LinearLayout) findViewById(R.id.fail_layout);
        reload_btn = (Button) findViewById(R.id.reload_btn);

        //班级列表
        lv_classes = (ListView) findViewById(R.id.lv_classes);

        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseTimeUtil chooseTimeUtil = new ChooseTimeUtil();
                chooseTimeUtil.createDialog(SendTaskActivity.this, new ChooseCityInterface() {
                    @Override
                    public void sure(String[] newCityArray) {
                        tv_time.setText(newCityArray[0] + newCityArray[1]);
                        switchButton();
                    }
                });
            }
        });
    }

    /**
     * 弹出退出登录的popwindow
     */
    private void showTooLongWindow() {

        View convertview = View.inflate(this, R.layout.pop_longnotify, null);
        tv_confirm = (TextView) convertview.findViewById(R.id.tv_confirm);
        tv_confirm.setOnClickListener(this);
        if (tooLongWindow==null){
            tooLongWindow = new PopupWindow(convertview, ViewGroup.LayoutParams.MATCH_PARENT, -2, true);
            tooLongWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        tooLongWindow.showAtLocation(convertview, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.7f);
        tooLongWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

    }

    /**
     * 获取班级列表数据
     */
    private void getClassListData() {
        RequestServer.getNotifyClassData(SendTaskActivity.this, Constant.getNotifyClassInterf, null, new RequestServer.Callback() {
            @Override
            public void onSuccess(Object object) {
                fail_layout.setVisibility(View.GONE);
                NotifyClassBean notifyBean = (NotifyClassBean) object;
                notifyClassBeanList = notifyBean.getUnits();
//                //syw 请求数据成功之后，填充列表
                adapter = new MyAdapter();
                lv_classes.setAdapter(adapter);
                setListViewHeight();
            }

            @Override
            public void onFail(Object object) {
                fail_layout.setVisibility(View.VISIBLE);
                reload_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getClassListData();
                    }
                });
            }
        }, true, true, false);

    }

    private void setListViewHeight() {

        ListAdapter adapter = lv_classes.getAdapter();
        View view = adapter.getView(0, null, lv_classes);
        view.measure(0, 0);
        int totalHeight = view.getMeasuredHeight();
        totalHeight = totalHeight * lv_classes.getCount();
        ViewGroup.LayoutParams layoutParams = lv_classes.getLayoutParams();
        layoutParams.height = totalHeight;

        lv_classes.setLayoutParams(layoutParams);
    }

    public int getWinWidth() {
        WindowManager wm = getWindowManager();
        return wm.getDefaultDisplay().getWidth();
    }

    public void setGridViewHeight(List<String> dataSourceList) {
        switchButton();
        itemHeight = (getWinWidth() - 40) / 3;
        Log.e("syw", "setGridViewHeight:" + itemHeight);
        ViewGroup.LayoutParams layoutParams = dragGridView.getLayoutParams();
        if (dataSourceList.size() >= 0 && dataSourceList.size() <= 2) {
            layoutParams.height = itemHeight + 20;
        } else if (dataSourceList.size() <= 5) {
            layoutParams.height = (itemHeight + 20) * 2;
        } else if (dataSourceList.size() <= 9) {
            layoutParams.height = (itemHeight + 20) * 3;
        }
        ((ViewGroup.MarginLayoutParams) layoutParams).setMargins(10, 10, 10, 10);
        dragGridView.setLayoutParams(layoutParams);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_btnback:
                onBackPressed();
                break;
            case R.id.bt_confirm:
//   syw             只有在选择过学生并且有图片或者有文字的情况下才能点击
                Log.e("syw", "点击bt_confirm");
                int length = et_notify.getText().toString().trim().length();
                notify = et_notify.getText().toString().trim();

                if (dataSourceList.size() != 0) {
//                    syw 有图片有文字，有图片没文字都可实现
                    uploadPictures();
                } else if (length == 0) {
                    MyToastUtils.toastText(this, "请输入文字或选择图片");
                } else {
//                    syw 没图片，只有文字
                    sendNotify();
                }

                break;
            case R.id.tv_confirm:
                tooLongWindow.dismiss();
                break;

            //从相册获取头像
            case R.id.tv_xiangce:
                popupWindow.dismiss();
                Intent intent = new Intent();
                intent.setClass(SendTaskActivity.this, SelectPictureActivity.class);
                intent.putExtra("intent_max_num", dataSourceList.size());
                startActivityForResult(intent, PHOTOCODE);
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

            //取消设置头像
            case R.id.tv_quxiao_tuichu:
                checkOutWindow.dismiss();
                break;

            //取消设置头像
            case R.id.tv_tuichu:
//                syw 清空照片列表
                dataSourceList.clear();
//                syw 清空上传路径列表
                picPaths.clear();
//                syw 清空选择的学生
                for (int i = 0; i < notifyClassBeanList.size(); i++) {
                    NotifyClassBean.UnitsBean unitsBean = notifyClassBeanList.get(i);
                    PreferencesManager.getInstance().putString(unitsBean.getId() + "", "");
                }
                finish();
                break;

        }
    }

    /**
     * 拍照设置头像
     */
    private static final String IMAGE_FILE_NAME = "image.jpg";
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    private static final int TAKE_PICTURE = 520;
    private File picture;

    private void takePhotoForHead() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String fileName = System.currentTimeMillis() + ".jpg";
        picture = FileUtils.getFile(fileName);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picture));
        openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switchButton();
        if (requestCode == PHOTOCODE && resultCode == PHOTORESULT) {
            List<String> phothpathList = data.getStringArrayListExtra("paths");
            for (String path : phothpathList) {
                dataSourceList.add(path);
            }
            imageAdaper.setShowDel(false);
            imageAdaper.update(dataSourceList);
        }
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            // 判断存储卡是否可以用，可用进行存储
            String state = Environment.getExternalStorageState();
            if (state.equals(Environment.MEDIA_MOUNTED)) {
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                File tempFile = new File(path, IMAGE_FILE_NAME);
                startPhotoZoom(Uri.fromFile(tempFile));
            } else {
                Toast.makeText(SendTaskActivity.this, "未找到存储卡", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == TAKE_PICTURE) {
            // 设置文件保存路径
            startPhotoZoom(Uri.fromFile(picture));
            return;
        }
        if (requestCode == PHOTO_RESOULT) {
            if (data == null) {
                return;
            }
            dataSourceList.add(picture.getAbsolutePath());
            imageAdaper.setShowDel(false);
            imageAdaper.update(dataSourceList);
            return;
        }

        if (requestCode == REQUEST_NOTIFY && data != null) {
            String notifys = data.getStringExtra("notifys");
            et_notify.setText(notifys);
        }
    }


    /**
     * 裁剪图片方法实现
     */
    int PHOTO_RESOULT = 3;// 结果

    public void startPhotoZoom(Uri uri) {
        Log.e("syw", "startPhotoZoom");
        Intent intent = new Intent("com.android.camera.action.CROP");// 调用Android系统自带的一个图片剪裁页面,
        intent.setDataAndType(uri, FinalNumInter.IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");// 进行修剪
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", FinalNumInter.PHOTO_ZOOM_OUTPUT_X);
        intent.putExtra("outputY", FinalNumInter.PHOTO_ZOOM_OUTPUT_Y);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_RESOULT);
    }


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return notifyClassBeanList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(SendTaskActivity.this, R.layout.item_class_sim, null);
                holder.tv_class = (TextView) convertView.findViewById(R.id.tv_class);
                holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv_class.setText(notifyClassBeanList.get(position).getName());

            holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        classids = classids + notifyClassBeanList.get(position).getId() + ",";
                    } else {
                        if (classids.contains(notifyClassBeanList.get(position).getId() + ",")) {
                            classids = classids.replace(notifyClassBeanList.get(position).getId() + ",", "");
                        }
                    }
                    switchButton();
                    Log.e("syw", "classids:" + classids);
                }
            });

            switchButton();
            return convertView;
        }
    }

    //            syw 切换确定按钮状态
    public void switchButton() {
        if (dataSourceList == null) {
            bt_confirm.setEnabled(false);
            bt_confirm.setTextColor(Color.parseColor("#afc4f7"));
        }


        if (!TextUtils.isEmpty(classids) && !tv_time.getText().toString().trim().equals("截止时间")) {
            if (!et_notify.getText().toString().trim().equals("") || dataSourceList.size() > 0) {
                bt_confirm.setEnabled(true);
                bt_confirm.setTextColor(Color.parseColor("#5e89ef"));
            } else {
                bt_confirm.setEnabled(false);
                bt_confirm.setTextColor(Color.parseColor("#afc4f7"));
            }
//syw没有截止时间或没有选择班级，都不可点击
        } else {
            bt_confirm.setEnabled(false);
            bt_confirm.setTextColor(Color.parseColor("#afc4f7"));
        }
    }

    class ViewHolder {
        public TextView tv_class;
        public CheckBox checkbox;
    }

    int itemHeight;

    public class ImageAdaper extends BaseAdapter {
        private boolean isShowDel = false;
        private Context ctx;
        private List<String> objects;


        public ImageAdaper(Context ctx, List<String> objects) {
            this.objects = objects;
            this.ctx = ctx;
        }

        @Override
        public int getCount() {
            if (objects.size() + 1>9) {
                return 9;
            }
            return objects.size() + 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            if (position >= objects.size()) {
                return null;
            } else {
                return objects.get(position);
            }
        }

        public boolean getShowDel() {
//            return isShowDel;
            return true;
        }

        public void setShowDel(boolean isShowDel) {
//            this.isShowDel = isShowDel;
            this.isShowDel = true;
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            final String map = (String) getItem(position);
            if (null == convertView) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(ctx).inflate(R.layout.draggridview_items, null);
                viewHolder.dragGridView_image = (ImageView) convertView.findViewById(R.id.dragGridView_image);
                viewHolder.dragGridView_del = (ImageView) convertView.findViewById(R.id.dragGridView_del);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (getShowDel()) {
                viewHolder.dragGridView_del.setVisibility(View.VISIBLE);
            } else {
                viewHolder.dragGridView_del.setVisibility(View.INVISIBLE);
            }

            if (map == null) {
                viewHolder.dragGridView_del.setVisibility(View.GONE);
                Glide
                        .with(SendTaskActivity.this)
                        .load(R.mipmap.raise)
                        .into(viewHolder.dragGridView_image);
            } else {
                BitmapUtil.setImageViewByImagLoading(ctx, map, viewHolder.dragGridView_image);
            }

            viewHolder.dragGridView_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String map = (String) getItem(position);
                    dataSourceList.remove(map);
                    update(dataSourceList);
                }
            });

            return convertView;
        }

        public void update(List<String> dataSourceList) {
            this.objects = dataSourceList;
            notifyDataSetChanged();
            setGridViewHeight(dataSourceList);
        }

        final class ViewHolder {
            ImageView dragGridView_del;
            ImageView dragGridView_image;
        }
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
     * popwindow消失
     *
     * @param window
     */
    public void dismissWindow(PopupWindow window) {
        if (window != null && window.isShowing()) {
            window.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        String stuIds = "";
        for (int i = 0; i < notifyClassBeanList.size(); i++) {
            NotifyClassBean.UnitsBean unitsBean = notifyClassBeanList.get(i);
            String id = PreferencesManager.getInstance().getString(unitsBean.getId() + "", "");
            stuIds += id;
        }
        if ("".equals(stuIds) && "".equals(et_notify.getText().toString()) && dataSourceList.size() == 0) {
            super.onBackPressed();
        } else {
            showCheckOutPopWindow();
        }
    }

    /**
     * 弹出退出登录的popwindow
     */
    private void showCheckOutPopWindow() {
        View convertview = View.inflate(this, R.layout.pop_checkout, null);
        //取消和退出的监听
        TextView content = (TextView) convertview.findViewById(R.id.content);
        convertview.findViewById(R.id.tv_quxiao_tuichu).setOnClickListener(this);
        convertview.findViewById(R.id.tv_tuichu).setOnClickListener(this);

        content.setText("是否退出编辑");
        checkOutWindow = new PopupWindow(convertview, ViewGroup.LayoutParams.MATCH_PARENT, -2, true);
        checkOutWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        //显示在整个view的底部
        if (Build.VERSION.SDK_INT < 24) {
            checkOutWindow.showAtLocation(lv_classes, Gravity.CENTER, 0, 0);
        } else {
            int distance = getWinHeight() - getViewHeight(convertview);
            checkOutWindow.showAtLocation(lv_classes, Gravity.NO_GRAVITY, 0, distance / 2);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSourceList = null;
        dismissWindow(tooLongWindow);
        dismissWindow(popupWindow);
        dismissWindow(checkOutWindow);
        if (notifyClassBeanList != null) {
            for (int i = 0; i < notifyClassBeanList.size(); i++) {
                NotifyClassBean.UnitsBean notifyClassBean = notifyClassBeanList.get(i);
                PreferencesManager.getInstance().putString(notifyClassBean.getId() + "", "");
            }
        }
    }
}
