package com.yinfeng.yf_trajectory.moudle.utils;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * ============================================
 * 描  述：语音播放
 * 包  名：com.lgd.conmoncore.utils
 * 类  名：PlayVoice
 * 创建人：lgd
 * 创建时间：2018/11/8 14:48
 * ============================================
 **/
public class PlayVoice {

        private static MediaPlayer mediaPlayer;

        public static void playVoice(Context context,int resid){
            try {
                mediaPlayer= MediaPlayer.create(context, resid);
                mediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //停止播放声音
        public  static void stopVoice(){
            if(null!=mediaPlayer) {
                mediaPlayer.stop();
            }
        }

}
