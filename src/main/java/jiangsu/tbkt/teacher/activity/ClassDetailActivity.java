package jiangsu.tbkt.teacher.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJBitmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jiangsu.tbkt.teacher.R;
import jiangsu.tbkt.teacher.api.RequestServer;
import jiangsu.tbkt.teacher.application.PreferencesManager;
import jiangsu.tbkt.teacher.bean.User;
import jiangsu.tbkt.teacher.utils.ChineseToPinyinHelper;
import jiangsu.tbkt.teacher.utils.Constant;
import jiangsu.tbkt.teacher.view.LetterIndexView;

/**
 * Created by song on 2016/10/8 0008.
 */
public class ClassDetailActivity extends BaseActivity implements View.OnClickListener {

    private ArrayList<User.StudentsBean> list;
    private LinearLayout ll_all;
    private MyAdapter adapter;
    private TextView top_infotxt;
    private Button bt_confirm;
    private CheckBox cb_all;
    private ImageView top_btnback;
    private int selectNum = 0;
    private String id;
    private LinearLayout fail_layout;
    private Button reload_btn;
    private ArrayList<User.StudentsBean> userList;
    private ListView listView;
    private TextView textView;
    private LetterIndexView letterIndexView;
    private String name;
    private String arrayIds;
    private String[] splitIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_class_detail);
        id = getIntent().getIntExtra("id",0)+"";
        name = getIntent().getStringExtra("name");
        arrayIds = PreferencesManager.getInstance().getString(id, "");

        //查找选择全部
        ll_all = (LinearLayout) findViewById(R.id.ll_all);
        ll_all.setOnClickListener(this);
        cb_all = (CheckBox) findViewById(R.id.cb_all);
        //syw 查找大标题
        top_infotxt = (TextView) findViewById(R.id.top_infotxt);
        top_infotxt.setText(name);
        //查找确定按钮
        bt_confirm = (Button) findViewById(R.id.bt_confirm);
        bt_confirm.setVisibility(View.VISIBLE);
        bt_confirm.setOnClickListener(this);
        //返回键
        top_btnback = (ImageView) findViewById(R.id.top_btnback);
        top_btnback.setOnClickListener(this);
        //加载失败
        fail_layout = (LinearLayout) findViewById(R.id.fail_layout);
        reload_btn = (Button) findViewById(R.id.reload_btn);

        listView = (ListView) findViewById(R.id.lv);


        textView = (TextView) findViewById(R.id.show_letter_in_center);
        letterIndexView = (LetterIndexView) findViewById(R.id.letter_index_view);

        //syw 设置条目的点击监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User.StudentsBean user = list.get(i);
                user.isChecked = !user.isChecked;
                if (user.isChecked) {
                    if (!arrayIds.contains(user.getUser_id()+"")) {
                        arrayIds = arrayIds + "," + user.getUser_id()+"";
                    }
                } else {
                    if (arrayIds.contains(user.getUser_id()+"")) {
                        if (arrayIds.equals(user.getUser_id()+"")) {
                            arrayIds = "";
                        } else if (arrayIds.startsWith(user.getUser_id()+"")){
                            arrayIds = arrayIds.replace(user.getUser_id()+"", "");
                        }else{
                            arrayIds = arrayIds.replace("," + user.getUser_id(), "");
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                selectNum = 0;
                //全选之后点击一个条目取消，则取消全选
                for (User.StudentsBean user1 : list) {
                    if (user1.isChecked) {
                        selectNum++;
                    }
                }
                if (selectNum == list.size()) {
                    cb_all.setChecked(true);
                } else {
                    cb_all.setChecked(false);
                }
            }
        });

        getNotifyStudent();
    }

    private void getNotifyStudent() {
        RequestServer.getNotifyStudentData(ClassDetailActivity.this, Constant.getNotifyStudentInterf, getParams(), new RequestServer.Callback() {
            @Override
            public void onSuccess(Object object) {
                fail_layout.setVisibility(View.GONE);
                userList = (ArrayList<User.StudentsBean>) object;

                initData();
            }

            @Override
            public void onFail(Object object) {
                fail_layout.setVisibility(View.VISIBLE);
                reload_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getNotifyStudent();
                    }
                });
            }
        }, true, true, true);
    }

    private String getParams() {
        JSONObject params = new JSONObject();
        try {
            params.put("unit_id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params.toString();
    }

    /**
     * 请求数据并排序
     */
    private void initData() {
        list = new ArrayList();
        for (User.StudentsBean user : userList) {
            //解决名字为空或者空格的情况
            if (user.getUser_name().trim().length() < 1) {
                user.setFirstLetter("#");
                list.add(user);
                continue;
            }

            String convert = ChineseToPinyinHelper.getInstance().getPinyin(user.getUser_name()).toUpperCase();
            user.setPinyin(convert);
            String substring = convert.substring(0, 1);
            if (substring.matches("[A-Z]")) {
                user.setFirstLetter(substring);
            } else {
                user.setFirstLetter("#");
            }
            list.add(user);
        }
        Collections.sort(list, new Comparator<User.StudentsBean>() {
            @Override
            public int compare(User.StudentsBean lhs, User.StudentsBean rhs) {
                if (lhs.getFirstLetter().contains("#")) {
                    return -1;
                } else if (rhs.getFirstLetter().contains("#")) {
                    return 1;
                } else {
                    return lhs.getFirstLetter().compareTo(rhs.getFirstLetter());
                }
            }
        });

        adapter = new MyAdapter(this, list);
        listView.setAdapter(adapter);

        //回显全选状态
        if (arrayIds.contains(",")) {
            splitIds = arrayIds.split(",");
            if (splitIds.length == list.size()) {
                cb_all.setChecked(true);
            } else {
                cb_all.setChecked(false);
            }
        } else {
            //syw 只有一个学生
            if(list.size()==1&&list.get(0)!=null&&arrayIds.equals(list.get(0).getUser_id())){
                cb_all.setChecked(true);
            }else{
                cb_all.setChecked(false);
            }
        }

        for (User.StudentsBean user : list) {
            if (arrayIds.contains(user.getUser_id()+"")) {
                user.isChecked = true;
            } else {
                user.isChecked = false;
            }
        }

        letterIndexView.setTextViewDialog(textView);
        letterIndexView.setUpdateListView(new LetterIndexView.UpdateListView() {
            @Override
            public void updateListView(String currentChar) {
                int positionForSection = adapter.getPositionForSection(currentChar.charAt(0));
                listView.setSelection(positionForSection);
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int sectionForPosition = adapter.getSectionForPosition(firstVisibleItem);
                letterIndexView.updateLetterIndexView(sectionForPosition);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_all:
                //更改checkbox状态
                if (cb_all.isChecked()) {
                    cb_all.setChecked(false);
                    //选择全部，遍历集合，全部取消
                    for (User.StudentsBean user : list) {
                        user.isChecked = false;
                    }
                    arrayIds = "";
                } else {
                    cb_all.setChecked(true);
                    //选择全部，遍历集合，全部选择
                    for (User.StudentsBean user : list) {
                        user.isChecked = true;
                        if (!arrayIds.contains(user.getUser_id()+"")) {
                            arrayIds = arrayIds + "," + user.getUser_id();
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                break;

            case R.id.top_btnback:
                finish();
                break;

            case R.id.bt_confirm:
                //syw 确定选择按钮
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < list.size(); i++) {
                    User.StudentsBean user = list.get(i);
                    if (user.isChecked) {
                        //拼接","
                        stringBuffer.append("," + user.getUser_id());
                    }
                }

                //剪切","
                String stus = stringBuffer.toString();
                if (stus.length() < 1) {
                    PreferencesManager.getInstance().putString(id, "");
                } else {
                    stus = stus.substring(1);
                    PreferencesManager.getInstance().putString(id, stus);
                }
                Log.e("syw","详情界面获取的学生ids:"+stus);
                finish();
                break;
        }
    }


    class MyAdapter extends BaseAdapter implements SectionIndexer {
        private List<User.StudentsBean> list;
        private Context context;
        private LayoutInflater inflater;
        private final KJBitmap kjBitmap;


        public MyAdapter(Context context, List<User.StudentsBean> list) {
            this.context = context;
            this.list = list;
            inflater = LayoutInflater.from(context);
            kjBitmap = new KJBitmap();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.listview_item, null);
                holder = new ViewHolder();
                holder.showLetter = (TextView) convertView.findViewById(R.id.show_letter);
                holder.username = (TextView) convertView.findViewById(R.id.username);
                holder.userface = (ImageView) convertView.findViewById(R.id.userface);
                //syw 添加checkbox
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_stu);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            User.StudentsBean user = list.get(position);

            //syw 判断是否已选
            if (arrayIds.contains(user.getUser_id()+"")) {
                user.isChecked = true;
                holder.checkBox.setChecked(true);
            } else {
                user.isChecked = false;
                holder.checkBox.setChecked(false);
            }


            holder.username.setText(user.getUser_name());

            if (TextUtils.isEmpty(user.getPortrait())) {
                holder.userface.setBackgroundResource(R.mipmap.ic_launcher);
            } else {
                kjBitmap.display(holder.userface, user.getPortrait());
            }

            int sectionForPosition = getSectionForPosition(position);

            int positionForSection = getPositionForSection(sectionForPosition);

            if (position == positionForSection) {
                holder.showLetter.setVisibility(View.VISIBLE);
                holder.showLetter.setText(user.getFirstLetter());
            } else {
                holder.showLetter.setVisibility(View.GONE);
            }
            return convertView;
        }

        @Override
        public Object[] getSections() {
            return new Object[0];
        }

        @Override
        public int getPositionForSection(int sectionIndex) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getFirstLetter().charAt(0) == sectionIndex) {
                    return i;
                }
            }
            return -1;
        }

        @Override
        public int getSectionForPosition(int position) {
            if (list.size() == 0) {
                return "A".charAt(0);
            }
            return list.get(position).getFirstLetter().charAt(0);
        }

        class ViewHolder {
            TextView username, showLetter;
            CheckBox checkBox;
            ImageView userface;
        }
    }
}
