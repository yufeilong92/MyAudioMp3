package com.lawyee.myaudiomp3;

import android.Manifest;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lawyee.myaudiomp3.ui1.AudioPlayerUtil;
import com.lawyee.myaudiomp3.ui1.AudioRecorderUtil;
import com.lawyee.myaudiomp3.ui1.PopupWindowFactory;
import com.lawyee.myaudiomp3.ui1.uitls.TimeUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity  {

    private boolean audioRecorder = false;
    private AudioPlayerUtil player;
    private Button recordBtn;//录音按钮
    private String ROOT_PATH;
    ImageView mImageView;
    TextView mTextView, tv_time;
    PopupWindowFactory mPop;
    View view;
    LinearLayout record_contentLayout;
    ImageView recordDetailView;
    private String audioFilePath;// 录音文件保存路径
    private AnimationDrawable animationDrawable;
 public static final String TAG = "MainActivity.class";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recordBtn = (Button) findViewById(R.id.recordBtn);
        record_contentLayout = (LinearLayout) findViewById(R.id.record_contentLayout);
        recordDetailView = (ImageView) findViewById(R.id.record_detailView);
        tv_time = (TextView) findViewById(R.id.tv_time);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        init(this);
        initAudioRecorderBtn();
    }

    public void init(Context context) {
        try {
            ROOT_PATH = context.getExternalFilesDir(null).getAbsolutePath();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + context.getPackageName();
            } else {
                ROOT_PATH = context.getFilesDir().getAbsolutePath();
            }
        } catch (Throwable e) {
            ROOT_PATH = context.getFilesDir().getAbsolutePath();
        }
    }

    private void initAudioRecorderBtn() {
        view = View.inflate(this, R.layout.item_dialog, null);
        mPop = new PopupWindowFactory(this, view);
        //PopupWindow布局文件里面的控件
        mImageView = (ImageView) view.findViewById(R.id.iv_recording_icon);
        mTextView = (TextView) view.findViewById(R.id.tv_recording_time);
        final AudioRecorderUtil audioRecorderUtil = new AudioRecorderUtil(ROOT_PATH + File.separator + "audio");
        audioRecorderUtil.setOnAudioStatusUpdateListener(new AudioRecorderUtil.OnAudioStatusUpdateListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onProgress(double db, long time) {
                //根据分贝值来设置录音时话筒图标的上下波动,同时设置录音时间
                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
                mTextView.setText(TimeUtils.long2String(time));
            }

            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onStop(String filePath) {
                mPop.dismiss();
                record_contentLayout.setVisibility(View.VISIBLE);
                audioFilePath = filePath;
                Log.e("===path", audioFilePath);
                // TODO 上传音频文件
            }
        });

        recordBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 停止播放
                if (player != null) {
                    player.stop();
                }
                audioRecorder = true;//正在录音
                // 处理动作
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        audioRecorderUtil.start();
                        mPop.showAtLocation(view.getRootView(), Gravity.CENTER, 0, 0);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        long time = audioRecorderUtil.getSumTime();
                        if (time < 1000) {
                            audioRecorderUtil.cancel();
                            Toast.makeText(MainActivity.this, "录音时间太短！", Toast.LENGTH_SHORT).show();
                            audioFilePath = "";
                        } else {
                            tv_time.setText(time / 1000 + "s");
                        }
                        mImageView.getDrawable().setLevel(0);
                        mTextView.setText(TimeUtils.long2String(0));
                        audioRecorderUtil.stop();
                        mPop.dismiss();
                        break;
                }
                return true;
            }
        });

        record_contentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(audioFilePath) || !audioRecorder) {
                    return;
                }
                if (player == null) {
                    player = new AudioPlayerUtil();
                } else {
                    player.stop();
                }
//                recordDetailView.setImageResource(R.drawable.play_voice_right_anim);
                animationDrawable = (AnimationDrawable) recordDetailView.getDrawable();
                animationDrawable.start();
                player.start(audioFilePath, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        animationDrawable.stop();
//                        recordDetailView.setImageResource(R.mipmap.voice_right);
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.stop();
        }
    }

    public void palyAudio(View view) {
        AudioPlayerUtil audioPlayerUtil = new AudioPlayerUtil();
        audioPlayerUtil.start(audioFilePath, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (!mp.isPlaying()) {

                }
            }
        });
    }
}