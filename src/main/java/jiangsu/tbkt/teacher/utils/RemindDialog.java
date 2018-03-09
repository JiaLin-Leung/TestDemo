package jiangsu.tbkt.teacher.utils;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import jiangsu.tbkt.teacher.R;


/**
 * @Deprecated：自定义dialog
 * @FileName: RemindDialog.java
 * @Package com.chsy.tbkt.util
 * @Author wangxiao
 * @Date 2014-12-28
 * @Version V1.0
 */
public class RemindDialog extends Dialog {

    public RemindDialog(Context context, int theme) {
        super(context, theme);

    }

    public RemindDialog(Context context) {
        super(context);

    }

    /**
     * dialog内部装载类
     * */
    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private String postiveText;
        private String negtiveText;
        private String middleText;
        private View convertView;
        private ListView childView;
        private View view;
        private LinearLayout view_ll;
        private OnClickListener postiveButton, negtiveButton,
                middleButton;
        private OnDialogItemClickListener itemListView;
        private int width;
        private int heigh;

        private LinearLayout dialog_ll;
        private TextView dialog_title;// 标题
        private Button dialog_positive, dialog_negetive, dialog_middle;// 布局中底部按钮
        private RelativeLayout messageRl;
        private View splitViewFirst, splitViewSecond;// 底部按钮分割线
        private TextView dialog_messageTv;// dialog message显示

        private final String TAG = "RemindDialog";

        public Builder(Context context) {
            this.context = context;
            width = context.getResources().getDisplayMetrics().widthPixels;
            heigh = context.getResources().getDisplayMetrics().heightPixels;
        }

        public interface OnDialogItemClickListener {
            public void onDialogItemClick(DialogInterface dialog, View view,
                                          int position);

        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        public Builder setPositive(String postiveText,
                                   OnClickListener postiveButton) {
            this.postiveText = postiveText;
            this.postiveButton = postiveButton;
            return this;
        };

        public Builder setPositive(int postiveText,
                                   OnClickListener postiveButton) {
            this.postiveText = (String) context.getText(postiveText);
            this.postiveButton = postiveButton;
            return this;
        };

        public Builder setMiddletive(int middleText,
                                     OnClickListener middleButton) {
            this.middleText = (String) context.getText(middleText);
            this.middleButton = middleButton;
            return this;
        }

        public Builder setMiddletive(String middleText,
                                     OnClickListener middleButton) {
            this.middleText = middleText;
            this.middleButton = middleButton;

            return this;
        }

        public Builder setNegtive(String negtiveText,
                                  OnClickListener negtiveButton) {
            this.negtiveText = negtiveText;
            this.negtiveButton = negtiveButton;
            return this;
        }

        public Builder setNegtive(int negtiveText,
                                  OnClickListener negtiveButton) {
            this.negtiveText = (String) context.getText(negtiveText);
            this.negtiveButton = negtiveButton;
            return this;
        }

        public Builder setContentView(View convertView) {
            this.convertView = convertView;
            return this;
        }

        public Builder setContentView(int convertView) {
            this.convertView = View.inflate(context, convertView, null);
            return this;
        }

        public Builder setView(ListView view,
                               OnDialogItemClickListener itemListView) {
            this.childView = view;
            this.itemListView = itemListView;
            return this;
        }

        public Builder setView(View view) {
            this.view = view;
            return this;
        }

        /**
         * 自定义控件初始化
         *
         * @return dialog实例
         * */
        public RemindDialog createDialog() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.dialog_remind_item, null);
            final RemindDialog dialog = new RemindDialog(context, R.style.remindDialog);
            initView(layout);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            // dialog.setCancelable(false);
            // dialog.addContentView(layout, new ViewGroup.LayoutParams(600,
            // 400));
            Window dialogWindow = dialog.getWindow();
            WindowManager.LayoutParams params = dialogWindow.getAttributes();
            params.width = width / 3;
            params.height = heigh / 2;
            dialogWindow.setAttributes(params);
            Log.v(TAG, "reminddialog<<<<" + "111");
            if (message == null && view == null) {
                view_ll.setVisibility(View.VISIBLE);
                messageRl.setVisibility(View.GONE);
                RelativeLayout.LayoutParams ll_params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                view_ll.setLayoutParams(ll_params);
                if (view_ll.getChildCount() > 0) {
                    for (int i = 0; i <= view_ll.getChildCount(); i++) {
                        view_ll.removeViewAt(i);
                    }
                }

                view_ll.addView(childView);
				/*
				 * LinearLayout.LayoutParams pa = new
				 * LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				 * android.widget.LinearLayout.LayoutParams.MATCH_PARENT);
				 * pa.bottomMargin = 20; childView.setLayoutParams(pa);
				 */
                childView
                        .setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                Log.v(TAG, "reminddialog<<<<" + childView.getCount());
                childView.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        itemListView.onDialogItemClick(dialog, view, position);

                    }
                });
            } else if (message == null && childView == null) {
                view_ll.setVisibility(View.VISIBLE);
                messageRl.setVisibility(View.GONE);
                RelativeLayout.LayoutParams ll_params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                view_ll.setLayoutParams(ll_params);
                if (view_ll.getChildCount() > 0) {
                    for (int i = 0; i <= view_ll.getChildCount(); i++) {
                        view_ll.removeViewAt(i);
                    }
                }
                view_ll.addView(view);
                LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                view.setLayoutParams(pa);
            } else {
                dialog_messageTv.setText(message);
                view_ll.setVisibility(View.GONE);

            }

            if (title != null) {
                dialog_title.setText(title);
            }
            ////只有一个取消按钮
            if (postiveText == null && negtiveText != null
                    && middleText == null) {
                // 设置其他按钮为gone
                splitViewFirst.setVisibility(View.GONE);
                splitViewSecond.setVisibility(View.GONE);
                dialog_positive.setVisibility(View.GONE);
                dialog_middle.setVisibility(View.GONE);
                dialog_negetive.setText(negtiveText);
                dialog_negetive.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        negtiveButton.onClick(dialog,
                                DialogInterface.BUTTON_NEGATIVE);
                        // negtiveButton.onClick(dialog, DialogInterface.)
                    }
                });
                // 只有确定按钮
            } else if (postiveText != null && negtiveText == null
                    && middleText == null) {
                // 设置其他按钮为gone
                splitViewFirst.setVisibility(View.GONE);
                splitViewSecond.setVisibility(View.GONE);
                dialog_middle.setVisibility(View.GONE);
                dialog_negetive.setVisibility(View.GONE);
                dialog_positive.setText(postiveText);
                dialog_positive.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        postiveButton.onClick(dialog,
                                DialogInterface.BUTTON_POSITIVE);

                    }
                });
                // 只有确定和取消按钮
            } else if (postiveText != null && negtiveText != null
                    && middleText == null) {

                splitViewFirst.setVisibility(View.GONE);
                dialog_middle.setVisibility(View.GONE);
                dialog_negetive.setText(negtiveText);
                dialog_positive.setText(postiveText);

                dialog_negetive.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        negtiveButton.onClick(dialog,
                                DialogInterface.BUTTON_NEGATIVE);

                    }

                });

                dialog_positive.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        postiveButton.onClick(dialog,
                                DialogInterface.BUTTON_POSITIVE);

                    }
                });
                // 底部三个按钮都存在
            } else if (postiveText != null && negtiveText != null
                    && middleText != null) {
                dialog_middle.setText(middleText);
                dialog_negetive.setText(negtiveText);
                dialog_positive.setText(postiveText);
                dialog_negetive.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        negtiveButton.onClick(dialog,
                                DialogInterface.BUTTON_NEGATIVE);

                    }

                });

                dialog_positive.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        postiveButton.onClick(dialog,
                                DialogInterface.BUTTON_POSITIVE);

                    }
                });

                dialog_middle.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        middleButton.onClick(dialog,
                                DialogInterface.BUTTON_NEUTRAL);

                    }
                });
            }

            dialog.setContentView(layout);

            return dialog;
        }

        /**
         * 初始化控件
         * */
        private void initView(View layout) {
            dialog_title = (TextView) layout
                    .findViewById(R.id.reminddialog_title);
            dialog_positive = (Button) layout
                    .findViewById(R.id.reminddialog_sure);
            messageRl = (RelativeLayout) layout
                    .findViewById(R.id.reminddialog_message_rl);
            dialog_negetive = (Button) layout
                    .findViewById(R.id.reminddialog_cancle);
            splitViewFirst = layout
                    .findViewById(R.id.reminddialog_bottom_split_first);
            splitViewSecond = layout
                    .findViewById(R.id.reminddialog_bottom_split_second);
            dialog_messageTv = (TextView) layout
                    .findViewById(R.id.reminddialog_message_tv);
            dialog_ll = (LinearLayout) layout
                    .findViewById(R.id.reminddialog_ll);
            dialog_middle = (Button) layout
                    .findViewById(R.id.reminddialog_middle);
            view_ll = (LinearLayout) layout
                    .findViewById(R.id.reminddialog_view_ll);
        }
    }
}
