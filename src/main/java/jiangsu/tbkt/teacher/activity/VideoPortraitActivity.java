package jiangsu.tbkt.teacher.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import org.kymjs.kjframe.ui.ViewInject;

import jiangsu.tbkt.teacher.R;
import jiangsu.tbkt.teacher.application.AppManager;
import jiangsu.tbkt.teacher.utils.NetworkStatueUtil;

/**
 * 横屏播放
 */
public class VideoPortraitActivity extends Activity implements View.OnClickListener {
    private VideoView videoView;
    private String url;
    private ProgressDialog progressDialog;
    private LinearLayout ll_nowifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        AppManager.getAppManager().addActivity(this);
        setContentView(R.layout.activity_video_portrait);
        receiveBundle();
        initUi();
    }


    private void receiveBundle() {
        url = getIntent().getStringExtra("video_url");
    }


    private void initUi() {
        videoView = (VideoView) findViewById(R.id.videoVideo);
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


    private void prepareVideo() {
//        syw 显示缓冲条
        progressDialog = ViewInject.getprogress(VideoPortraitActivity.this, "正在缓冲...", true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                VideoPortraitActivity.this.finish();
            }
        });

//        syw 准备播放
        videoView.setMediaController(new MediaController(VideoPortraitActivity.this, false));
        Uri uri = Uri.parse(url);
        videoView.setVideoURI(uri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                progressDialog.dismiss();
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                VideoPortraitActivity.this.finish();
                if (onVideoComListener!=null){
                    onVideoComListener.onVideoCom();
                }
            }
        });
        videoView.requestFocus();
    }

    public static OnVideoComListener onVideoComListener;

    public static void setOnVideoComListener(OnVideoComListener onVideoComListener1) {
        onVideoComListener = onVideoComListener1;
    }

    public interface OnVideoComListener {
        void onVideoCom();
    }


    @TargetApi(Build.VERSION_CODES.FROYO)
    @Override
    protected void onResume() {
        videoView.resume();
//        videoView.seekTo(cur_length);
        videoView.start();
        super.onResume();
    }

    @Override
    protected void onPause() {
        videoView.pause();
//        cur_length = videoView.getCurrentPosition();
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

}
