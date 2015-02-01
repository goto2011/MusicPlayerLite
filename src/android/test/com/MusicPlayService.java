package android.test.com;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

//13. ����(����, �ر�, ��, ȥ��)service, begin
public class MusicPlayService extends Service{
	private static final String TAG = "[MusicPlayer Lite]";
	
	public static final int CMD_PLAY = 0;
	public static final int CMD_PAUSE = 1;
	public static final int CMD_STOP = 2;
	
	public static final int STATUS_PLAYING = 0;
	public static final int STATUS_PAUSED = 1;
	public static final int STATUS_STOPPED = 2;
	
	public static final String MUSIC_CONTROL = "MusicPlyerLite.ACTION_CONTROL";
	public static final String UPDATE_STATUS = "MusicPlyerLite.ACTION_UPDATE";
	
	// ���ڱ�ʾ��ǰ����״̬.
	int status;
	// ý�岥��������
	MediaPlayer mp;
	OnCompletionListener completed;
	CommandReceiver doCommand;
	
	// �������½���״̬�� Broadcast
	private void updateUi(){
		Intent intent = new Intent(UPDATE_STATUS);
		intent.putExtra("status", status);
		sendBroadcast(intent);
	}
	
	// ��������״̬����
	public class CommandReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			switch(intent.getIntExtra("cmd", -1)){
			case CMD_PLAY:
				Uri media = intent.getData();
				if(media != null){
					// ֹͣ��ǰ���ŵ�����, �����Ŵ������� uri
					mp.release();
					mp = MediaPlayer.create(context, media);
					
					// �������ֲ�����Ϻ�ļ�����
					mp.setOnCompletionListener(completed);
					
					try{
						mp.start();
					}catch (Exception e){
						// ����ʧ�� 
					}
				}else{
					// ��������
					mp.start();
				}
				
				status = STATUS_PLAYING;
				break;
				
			case CMD_PAUSE:
				// ������ͣ
				mp.pause();
				status = STATUS_PAUSED;
				break;
				
			case CMD_STOP:
				// ���Ž���
				mp.stop();
				status = STATUS_STOPPED;
				break;
			default:
				break;	
			}
			
			// ���½���
			updateUi();
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "Music Play Service Bind");
		return null;
	}

	@Override
	public boolean onUnbind(Intent intent){
		Log.d(TAG, "Music Play Service Unbind");
		return super.onUnbind(intent);
	}
	
	@Override
	public void onRebind(Intent intent){
		super.onRebind(intent);
		Log.d(TAG, "Music Play Service Rebind");
	}
	
	@Override
	public void onCreate(){
		Log.d(TAG, "Music Play Service Create");
		
		mp = new MediaPlayer();
		
		// ��ʼ��״̬Ϊֹͣ
		status = STATUS_STOPPED;
		doCommand = new CommandReceiver();
		
        // ����һ��IntentFilter��
		// action:"MusicPlyerLite.ACTION_CONTROL"
		// scheme:"http"
		// ��ע��
		IntentFilter filter1 = new IntentFilter();
		filter1.addAction(MUSIC_CONTROL);
		filter1.addDataScheme("http");
		registerReceiver(doCommand, filter1);
		
        // ����һ��IntentFilter��
        // action:"MusicPlyerLite.ACTION_CONTROL"
        // ��ע��
		IntentFilter filter2 = new IntentFilter();
		filter1.addAction(MUSIC_CONTROL);
		registerReceiver(doCommand, filter2);
		
		completed = new OnCompletionListener(){
			@Override
			public void onCompletion(MediaPlayer player){
				player.stop();
				status = STATUS_STOPPED;
				updateUi();
			}
		};
	}
	
	@Override
	public void onDestroy(){
		Log.d(TAG, "Music Play Service Destroy");
		
		unregisterReceiver(doCommand);
		mp.release();
	}
	
	@Override
	public void onStart(Intent intent, int startId){
		Log.d(TAG, "Music Play Service Start");
		
		updateUi();
	}
}
//13. ����(����, �ر�, ��, ȥ��)service, end