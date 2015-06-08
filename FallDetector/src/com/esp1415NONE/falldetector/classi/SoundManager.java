package com.esp1415NONE.falldetector.classi;

import com.esp1415NONE.falldetector.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundManager {
	private static SoundPool soundPool;
	private static int suoneria;
	private static int a=0;

	@SuppressWarnings("deprecation")
	public static void init(Context context){
		soundPool=new SoundPool(1, AudioManager.STREAM_MUSIC, 100);
		suoneria=soundPool.load(context,R.raw.avviso, 0);
	}
	public static void play(){
		a = soundPool.play(suoneria, 1f, 1f, 1, 0, 1f);
	}
	public static void stop(){
		soundPool.stop(a);
	}

}
