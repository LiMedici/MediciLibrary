package com.medici.stack.model.manager;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

/**
 * @desc 用来播放音频
 * @author 李宗好
 * @time:2017年1月16日 下午4:44:38
 */
public class MediaManager {

	private static MediaPlayer mMediaPlayer;
	private static boolean isPause;

	/**
	 * 播放音乐
	 * @param filePath
	 * @param onCompletionListener
	 */
	public static void playSound(String filePath, OnCompletionListener onCompletionListener) {
		if (mMediaPlayer == null) {
			mMediaPlayer = new MediaPlayer();
			
			//设置一个error监听器
			mMediaPlayer.setOnErrorListener((mediaPlayer,i,i1)->{
				mMediaPlayer.reset();
				return false;
			});
		} else {
			mMediaPlayer.reset();
		}

		try {
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMediaPlayer.setOnCompletionListener(onCompletionListener);
			mMediaPlayer.setDataSource(filePath);
			mMediaPlayer.prepare();
			mMediaPlayer.start();
		} catch (Exception e) {

		}
	}

	/**
	 * 暂停播放
	 */
	public static void pause() {
		if (mMediaPlayer != null && mMediaPlayer.isPlaying()) { //正在播放的时候
			mMediaPlayer.pause();
			isPause = true;
		}
	}

	/**
	 * 当前是isPause状态
	 */
	public static void resume() {
		if (mMediaPlayer != null && isPause) {  
			mMediaPlayer.start();
			isPause = false;
		}
	}

	/**
	 * 释放资源
	 */
	public static void release() {
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}
}
