package com.lawyee.myaudiomp3.ui1;

import android.media.MediaPlayer;

/**
 * All rights Reserved, Designed By www.lawyee.com
 *
 * @version V 1.0 xxxxxxxx
 * @Title: MyAudioMp3
 * @Package com.lawyee.myaudiomp3
 * @Description: $todo$
 * @author: YFL
 * @date: 2018/3/5 16:28
 * @verdescript 版本号 修改时间  修改人 修改的概要说明
 * @Copyright: 2018 www.lawyee.com Inc. All rights reserved.
 * 注意：本内容仅限于北京法意科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class AudioPlayerUtil {
    private static final String TAG = "AudioRecordTest";
    private MediaPlayer mPlayer;

    public AudioPlayerUtil() {
    }

    public void start(String mFileName, MediaPlayer.OnCompletionListener listener) {
        if(this.mPlayer == null) {
            this.mPlayer = new MediaPlayer();
        } else {
            this.mPlayer.reset();
        }

        try {
            this.mPlayer.setDataSource(mFileName);
            this.mPlayer.prepare();
            this.mPlayer.start();
            if(listener != null) {
                this.mPlayer.setOnCompletionListener(listener);
            }
        } catch (Exception var4) {
        }

    }

    public void stop() {
        if(this.mPlayer != null) {
            this.mPlayer.stop();
            this.mPlayer.release();
            this.mPlayer = null;
        }

    }
}
