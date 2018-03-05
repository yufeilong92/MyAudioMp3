package com.lawyee.myaudiomp3.ui1.uitls;

import java.text.SimpleDateFormat;

/**
 * All rights Reserved, Designed By www.lawyee.com
 *
 * @version V 1.0 xxxxxxxx
 * @Title: MyAudioMp3
 * @Package com.lawyee.myaudiomp3
 * @Description: $todo$
 * @author: YFL
 * @date: 2018/3/5 16:29
 * @verdescript 版本号 修改时间  修改人 修改的概要说明
 * @Copyright: 2018 www.lawyee.com Inc. All rights reserved.
 * 注意：本内容仅限于北京法意科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class TimeUtils {
    public TimeUtils() {
    }

    public static String long2String(long time) {
        int sec = (int)time / 1000;
        int min = sec / 60;
        sec %= 60;
        return min < 10?(sec < 10?"0" + min + ":0" + sec:"0" + min + ":" + sec):(sec < 10?min + ":0" + sec:min + ":" + sec);
    }

    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(Long.valueOf(System.currentTimeMillis()));
    }
}
