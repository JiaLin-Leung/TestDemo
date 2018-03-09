package jiangsu.tbkt.teacher.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.kymjs.kjframe.ui.ViewInject;

import jiangsu.tbkt.teacher.R;
import jiangsu.tbkt.teacher.application.AppManager;
import jiangsu.tbkt.teacher.utils.NetworkStatueUtil;
import jiangsu.tbkt.teacher.utils.Tools;
import jiangsu.tbkt.teacher.view.VideoView;

/**
 * 横屏播放
 */
public class VideoAllScreenActivity extends Activity implements View.OnClickListener {
    private VideoView videoView;
    private String url;
    private ProgressDialog progressDialog;
    private LinearLayout ll_nowifi;
    private GestureDetector detector;
    private static final int msg_update_play_progress = 1;
    private static final int msg_auto_hide_controller = 2;
    private LinearLayout video_player_ll_top;
    private LinearLayout video_player_ll_bottom;
    private TextView video_player_tv_position;
    private TextView video_player_tv_duration;
    private SeekBar video_player_sk_position;
    private ImageView video_player_iv_pause;
    private int cur_length=0;
    //控制条是否显示
    private boolean isControllerShowing = false;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {

                case msg_update_play_progress:
                    updatePlayProgress();
                    break;

                case msg_auto_hide_controller:
                    hideController();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AppManager.getAppManager().addActivity(this);
        setContentView(R.layout.activity_video_all);

        receiveBundle();
        initUi();
    }


    private void receiveBundle() {
        url = getIntent().getStringExtra("video_url");
    }

    private final class SimpleVideoOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            //单击,显示和隐藏顶部底部控制条
            Log.e("syw", "单击事件！！！");
            hideOrShowController();
            return true;
        }
    }

    /**
     * 控制控制条,显示和隐藏
     */
    public void hideOrShowController() {
        //隐藏-显示
        if (!isControllerShowing) {
            showController();
        } else {
            //显示-隐藏
            //不用自定义隐藏
            handler.removeMessages(msg_auto_hide_controller);
            hideController();
        }
    }

    //显示
    private void showController() {
        video_player_ll_top.setVisibility(View.VISIBLE);
        video_player_ll_bottom.setVisibility(View.VISIBLE);
        isControllerShowing = true;
        //显示后,2s自动隐藏
        handler.sendEmptyMessageDelayed(msg_auto_hide_controller, 3000);
    }

    //隐藏
    private void hideController() {
        video_player_ll_top.setVisibility(View.GONE);
        video_player_ll_bottom.setVisibility(View.GONE);
        isControllerShowing = false;
    }

    private void initUi() {
        videoView = (VideoView) findViewById(R.id.videoVideo);
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.e("syw", "touch");
                detector.onTouchEvent(motionEvent);
                return true;
            }
        });
        TextView tv_fanhui = (TextView) findViewById(R.id.tv_fanhui);
        tv_fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        video_player_iv_pause = (ImageView) findViewById(R.id.video_player_iv_pause);
        video_player_iv_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchPlayAndPause();
            }
        });
        video_player_ll_top = (LinearLayout) findViewById(R.id.video_player_ll_top);
        video_player_ll_bottom = (LinearLayout) findViewById(R.id.video_player_ll_bottom);
        video_player_tv_position = (TextView) findViewById(R.id.video_player_tv_position);
        video_player_tv_duration = (TextView) findViewById(R.id.video_player_tv_duration);
        video_player_sk_position = (SeekBar) findViewById(R.id.video_player_sk_position);
        video_player_sk_position.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                //改变进度
                if (fromUser) {
                    videoView.seekTo(progress);
                    //更新当前进度
                    video_player_tv_position.setText(Tools.formatMs(progress));
                }
            }

            //开始拖拽
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //停止自动隐藏
                Log.e("syw", "开始拖拽");
                handler.removeMessages(msg_auto_hide_controller);
            }

            //停止拖拽
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //开启自动隐藏
                Log.e("syw", "停止拖拽");
                handler.sendEmptyMessageDelayed(msg_auto_hide_controller, 3000);
            }
        });
        detector = new GestureDetector(this, new SimpleVideoOnGestureListener());

        ll_nowifi = (LinearLayout) findViewById(R.id.ll_nowifi);
        View view = findViewById(R.id.view_nowifi);
        TextView tv_nowifi = (TextView) findViewById(R.id.tv_nowifi);
        TextView tv_quxiao_tuichu = (TextView) findViewById(R.id.tv_quxiao_tuichu);
        TextView tv_tuichu = (TextView) findViewById(R.id.tv_tuichu);

        tv_quxiao_tuichu.setOnClickListener(this);
        tv_tuichu.setOnClickListener(this);

        if (!NetworkStatueUtil.isWifi(this) && NetworkStatueUtil.isConnectInternet(this)) {
            ll_nowifi.setVisibility(View.VISIBLE);
            tv_nowifi.setText("当前处于移动数据网络,观看视频\n需要耗费手机流量,继续观看?");
            tv_tuichu.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
            return;
        } else if (!NetworkStatueUtil.isConnectInternet(this)) {
            ll_nowifi.setVisibility(View.VISIBLE);
            tv_nowifi.setText("无网络连接,请检查网络!");
            tv_tuichu.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            return;
        } else {
            ll_nowifi.setVisibility(View.GONE);
        }
        prepareVideo();
    }

    /**
     * 更新播放进度
     */
    public void updatePlayProgress() {
        if (progressDialog!=null&&progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        //获取当前进度,初始化
        int currentPosition = videoView.getCurrentPosition();//ms
        int duration = videoView.getDuration();//ms
        //更新当前进度
        video_player_tv_position.setText(Tools.formatMs(currentPosition));
        //更新总时长
        video_player_tv_duration.setText(Tools.formatMs(duration));
        //更新sk
        video_player_sk_position.setMax(duration);
        video_player_sk_position.setProgress(currentPosition);
        //每隔一段时间,获取一次
        handler.sendEmptyMessageDelayed(msg_update_play_progress, 500);
    }

    /**
     * 切换播放和暂停
     */
    private void switchPlayAndPause() {
        if (videoView.isPlaying()) {
            //暂停
            videoView.pause();
            handler.removeMessages(msg_update_play_progress);
        } else {
            //继续播放
            videoView.start();
            handler.sendEmptyMessageDelayed(msg_update_play_progress, 500);
        }
        handler.removeMessages(msg_auto_hide_controller);
        handler.sendEmptyMessageDelayed(msg_auto_hide_controller, 3000);
        switchPlayAndPausePic();
    }

    /**
     * 切换播放和暂停按钮图片
     */
    public void switchPlayAndPausePic() {
        //是否正在播放
        if (videoView.isPlaying()) {
            //暂停按钮
            video_player_iv_pause.setImageResource(R.drawable.video_pause_selector);
        } else {
            //播放按钮
            video_player_iv_pause.setImageResource(R.drawable.video_play_selector);
        }
    }


    private void prepareVideo() {
//        syw 显示缓冲条
        progressDialog = ViewInject.getprogress(VideoAllScreenActivity.this, "正在缓冲...", true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                VideoAllScreenActivity.this.finish();
            }
        });

//        syw 准备播放
//        videoView.setMediaController(new MediaController(VideoAllScreenActivity.this));
        Uri uri = Uri.parse(url);
        videoView.setVideoURI(uri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoView.start();
                switchPlayAndPausePic();
                updatePlayProgress();
//                progressDialog.dismiss();
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                video_player_sk_position.setProgress(videoView.getDuration());
                video_player_tv_position.setText(Tools.formatMs(videoView.getDuration()));
                //停止更新当前进度
                handler.removeMessages(msg_update_play_progress);
                video_player_iv_pause.setImageResource(R.drawable.video_play_selector);
                finish();
            }
        });
        videoView.requestFocus();
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    @Override
    protected void onResume() {
//        videoView.resume();
//        videoView.start();
//        prepareVideo();
        super.onResume();
    }

    @Override
    protected void onPause() {
//        videoView.pause();
        finish();
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_quxiao_tuichu:
                ll_nowifi.setVisibility(View.GONE);
                finish();
                break;
            case R.id.tv_tuichu:
                ll_nowifi.setVisibility(View.GONE);
                prepareVideo();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
