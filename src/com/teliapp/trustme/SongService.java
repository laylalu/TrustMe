package com.teliapp.trustme;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.IBinder;

public class SongService extends Service implements OnCompletionListener{
	Context ctx = this;
	private final IBinder mBinder = new SongBinder();
	public MediaPlayer song = null;
	int currentPostion = 0, duration = 0;
	public boolean done;
	

	@Override
	public void onCompletion(MediaPlayer mp) {
		done = true;
		stopSelf();

	}

	@Override
	public IBinder onBind(Intent intent) {

		return mBinder;
	}

	@Override
	public void onCreate() {
		
	}

	void choseSong(){
		if(TrustMe.playPressed)	song = MediaPlayer.create(ctx, R.raw.trust);
		song.setLooping(false);
	}

	public void onDestroy() {
		if(song != null){
		if (song.isPlaying()) {
			song.stop();
		}
		song.release();
		song = null;
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		choseSong();
		song.setOnCompletionListener(this);
		int pos = intent.getIntExtra("pos", 0);
		boolean repeat = intent.getBooleanExtra("repeat", false);
			song.start();
			song.setLooping(repeat);
			song.seekTo(pos);
			done = false;
		return START_STICKY;
	}

	int getCurrentPostion(){
		try{
		if (song != null)
			return song.getCurrentPosition();
		else 				
			return 0;
		}catch(IllegalStateException e){
			return 0;
		}
	}


	int getDuration(){		
		try{
			if (song !=null) 	
				return song.getDuration();			
			else	
				return 0;
		}catch(IllegalStateException e){
			return 0;
		}
	}



	public class SongBinder extends Binder{
		SongService getService(){
			return SongService.this;
		}
	}
}
