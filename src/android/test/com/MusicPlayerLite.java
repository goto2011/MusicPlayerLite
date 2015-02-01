package android.test.com;

// ========================= �����б� =============================
// 0. �����������+log
// 1. option�˵����
// 2. context�˵����
// 3. �Ի���
// 4. ������
// 5. �Զ���ؼ�
// 6. �������ص� �� ����
// 6.1 �����ֻ��ϰ��������µ��¼��ص�
// 6.2 �����ֻ��ϰ�����������̧����¼��ص�
// 6.3 ����켣����¼��ص�
// 6.4 �����������¼��ص�
// 6.5 ������ı���¼��ص�
// 7. ��ť����
// 8. ��ת����һ�� activity
// 9. �˳�Ӧ��
// 10. ����ϵͳ�Ĳ��Ž���
// 11. ������ع���
// 12. ���û�ѡ�񷵻ظ��ϲ� activity
// 13. ����(����, �ر�, ��, ȥ��)service
// 14. ����Ӧ��Ȩ��(uses-permission)
// 15. ע�� BroadcastReceiver, �������յ��� Broadcast(û�㶨)
// 16. ý�岥����(MusicPlayLite)
//==================================================================

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.test.com.ListViewShow.ListViewShow;
import android.test.com.SMSLite.SMSLite;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.test.com.DialerLite.*;

public class MusicPlayerLite extends Activity {
	final int MENU_SAVE = Menu.FIRST;
	final int MENU_DELETE = Menu.FIRST + 1;
	final int MENU_ABOUT = Menu.FIRST + 2;
	
	final int C_MENU_NEW = Menu.FIRST + 100;
	final int C_MENU_OPEN = Menu.FIRST + 101;
	final int C_MENU_SEARCH = Menu.FIRST + 102;
	final int C_MENU_LOCAL_NET = Menu.FIRST + 103;
	final int C_MENU_INTERNET = Menu.FIRST + 104;
	final int C_MENU_INPUT_DATE = Menu.FIRST + 105;
	final int C_MENU_SHOW_PROGRESS = Menu.FIRST + 106;
	final int C_MENU_SHOW_WEEK = Menu.FIRST + 107;
	final int C_MENU_SHOW_EXIT = Menu.FIRST + 108;
	
	final int DLALOG_WELCOME = 1;
	final int DIALOG_DATE_PICKER = 2;
	
	final int DIALOG_PROGRESS = 3;
	final int MAX_PROGRESS = 100;
	final int MSG_PROGRESS = 1;
	Handler pHandler = null;
	ProgressDialog pd = null;

    // 1. option�˵����, begin
	// ����menu�˵�
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	// �Ӳ˵�
    	SubMenu sub_menu = menu.addSubMenu(R.string.search);
    	sub_menu.setIcon(android.R.drawable.ic_menu_search);
    	sub_menu.add(0,C_MENU_LOCAL_NET,0,R.string.search_local);
    	sub_menu.add(0,C_MENU_INTERNET,0,R.string.search_internet);
    	
    	// ����ѡȡ��
    	menu.add(0,C_MENU_INPUT_DATE,0,R.string.input_date).setIcon(android.R.drawable.ic_menu_today);

    	// �������ڼ�
    	menu.add(0,C_MENU_SHOW_WEEK,0,R.string.input_week).setIcon(android.R.drawable.ic_menu_week);

    	// ������
    	menu.add(0,C_MENU_SHOW_PROGRESS,0,R.string.show_progress).setIcon(android.R.drawable.ic_menu_gallery);

    	// about �˵���
    	menu.add(0,MENU_ABOUT,0,R.string.about_me).setIcon(android.R.drawable.ic_menu_help);

    	// save �˵���
    	menu.add(0,MENU_SAVE,0,R.string.option_menu_save).setIcon(android.R.drawable.ic_menu_save);
    	// delete �˵���
    	menu.add(0,MENU_DELETE,0,R.string.option_menu_delete).setIcon(android.R.drawable.ic_menu_delete);
    	
    	// �˳�
    	menu.add(0,C_MENU_SHOW_EXIT,0,R.string.exit).setIcon(android.R.drawable.ic_menu_delete);
    	
    	return true;
    }
    
    // ��Ӧ�û�����¼�.
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	super.onOptionsItemSelected(item);
    	
    	// ����id��ȡ TextView����.
		TextView save_status_tv = (TextView)findViewById(R.id.save_text);
		
    	switch(item.getItemId()){
    	case MENU_SAVE:
    		// ����id�����ַ�����Դ, ������ TextView����.
    		// ��һ������: �ַ���������, Ӧ����ô����?
    		save_status_tv.setText(getResources().getString(R.string.string_data)
    				+ getResources().getString(R.string.save_status_saved));
    		break;
    	case MENU_DELETE:
    		save_status_tv.setText(getResources().getString(R.string.string_data)
    				+ getResources().getString(R.string.save_status_deleted));
    		break;
    		
    	case C_MENU_LOCAL_NET:
    		save_status_tv.setText(R.string.search_local);
    		break;
    	case C_MENU_INTERNET:
    		save_status_tv.setText(R.string.search_internet);
    		break;
    		
    	case C_MENU_INPUT_DATE:
    		showDialog(DIALOG_DATE_PICKER);
    		break;
    	case C_MENU_SHOW_PROGRESS:
    		show_progress();
    		break;
    	
    	// 8. ��ת����һ�� activity, begin
    	// �������ڼ�
    	case C_MENU_SHOW_WEEK:
    		Intent week_intent = new Intent();
    		week_intent.setClass(MusicPlayerLite.this, ListViewShow.class);
    		
    		// 12. ���û�ѡ�񷵻ظ��ϲ� activity, begin
    		// ��ת����һ��activity, ������Ҫ��ȡ����ֵ
    		// startActivity(week_intent);
    		// ��ת����һ��activity, ����Ҫ��ȡ����ֵ
    		startActivityForResult(week_intent, DIALOG_DATE_PICKER);
    		// 12. ���û�ѡ�񷵻ظ��ϲ� activity, end
    		
    		// ������� finish(), ����ת��ListViewShowʱ, MusicPlayerLite �Ѿ�������. 
    		// ������, ��MusicPlayerLite����OnPause״̬, ListViewShow �˳���, MusicPlayerLite������ʾ. 
    		// finish();
    		break;
    	// 8. ��ת����һ�� activity, end
    	
    	case MENU_ABOUT:
	    	// ��ʼ���Ի���
	    	showDialog(DLALOG_WELCOME);
	    	break;
	    
	    // 9. �˳�����, begin
    	case C_MENU_SHOW_EXIT:
    		finish();
    		break;
    	}
	    // 9. �˳�����, end
    	
    	return false;
    }
    // 1. option�˵����, end
    
    // 2. context�˵����, begin
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
    	super.onCreateContextMenu(menu, v, menuInfo);
    	menu.add(0,C_MENU_NEW,0,R.string.context_menu_new);
    	menu.add(0,C_MENU_OPEN,0,R.string.context_menu_open);
    }
    
    // ��Ӧ�û�����¼�.
    @Override
    public boolean onContextItemSelected(MenuItem item){
    	super.onContextItemSelected(item);
    	
    	TextView status_tv = (TextView)findViewById(R.id.save_text);
    	
    	switch (item.getItemId()){
    	case C_MENU_NEW:
    		status_tv.setText(R.string.context_menu_new);
    		break;
    	case C_MENU_OPEN:
    		status_tv.setText(R.string.context_menu_open);
    		break;
    	}
		return false;
    }
    // 2. context�˵����, end
    
    // 3. �Ի���, begin
    @Override
    protected Dialog onCreateDialog(int id){
    	final TextView status_tv = (TextView)findViewById(R.id.save_text);
    	Dialog ret;
    	
    	switch(id){
    	// һ��Ի���, begin
    	case DLALOG_WELCOME:
    		// ���������﷨֮�����е�ͨ, ����Ϊ��Щ set***() �ĺ���������dialog����, ��������������.
    		ret = new AlertDialog.Builder(this)
    			.setIcon(android.R.drawable.ic_dialog_info)
    			.setTitle(R.string.welcom_pro)
    			.setMessage(R.string.welcom)
    			.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener(){
    				// �����û��ڶԻ����еĵ��. �����������һ�� activity.
					@Override
					public void onClick(DialogInterface dialog, int which) {
						status_tv.setText(R.string.about_me);
					}
    			})
    			.create();
    		break;
        // һ��Ի���, end
    		
    	// ����ѡȡ��, begin
    	case DIALOG_DATE_PICKER:
    		Calendar c;
    		c = Calendar.getInstance();
    		
    		ret = new DatePickerDialog(this, new OnDateSetListener(){
    			@Override
    			public void onDateSet(DatePicker v, int y, int m, int d){
    				// ���ȷ����ť��������.
    	    		status_tv.setText(getResources().getString(R.string.select_time) + y 
    	    				+ getResources().getString(R.string.time_separator) + m 
    	    				+ getResources().getString(R.string.time_separator) + d);
    			}
    		}, c.get(Calendar.YEAR),
    		c.get(Calendar.MONTH),
    		c.get(Calendar.DAY_OF_MONTH));
    		break;
    	// ����ѡȡ��, end
    	
    	// ������, begin
    	case DIALOG_PROGRESS:
    		pd = new ProgressDialog(this);
    		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    		pd.setMax(MAX_PROGRESS);
    		pd.setProgress(0);
    		ret = pd;
    		break;
        // ������, end
    		
    	default:
    		ret = null;
    		break;
    	}
    	
    	return ret;
    }
    // 3. �Ի���, end
    
    // 4. ������, begin
    protected void show_progress(){
    	pHandler = new Handler(){
    		@Override
    		public void handleMessage(Message msg){
    			super.handleMessage(msg);
    			// �ж���Ϣ
    			switch (msg.what){
    			case MSG_PROGRESS:
    				if(pd.getProgress() >= MAX_PROGRESS){
    					pd.dismiss();
    				}else{
    					pd.incrementProgressBy(1);
    					pHandler.sendEmptyMessageDelayed(MSG_PROGRESS,100);
    				}
    				break;
    			default:
    				break;	
    			}
    		}
    	};
    	
    	showDialog(DIALOG_PROGRESS);
    	pHandler.sendEmptyMessage(DIALOG_PROGRESS);
    }
    // 4. ������, end
    
    // 5. �Զ���ؼ�, begin
    // ��ش��� move �� NeverEdit.java 
    // 5. �Զ���ؼ�, end
    
    // 6. �������ص� �� ����, begin
    // 6.1 �����ֻ��ϰ��������µ��¼��ص�. 
    // ���: keyCode: ���µļ�ֵ. KeyEvent: �����¼��Ķ���.
    // ����ֵ: �����ϣ�������ص�����ü�ֵ, �򷵻� true; ���򷵻�false.
    public boolean onKeyDown(int keyCode, KeyEvent event){
		return false;
    }
    
    // 6.2 �����ֻ��ϰ�����������̧����¼��ص�. 
    // ����ֵ: �����ϣ�������ص�����ü�ֵ, �򷵻� true; ���򷵻�false.
    public boolean onKeyUp(int keyCode, KeyEvent event){
		return false;
    }

    // 6.3 ����켣����¼��ص�. 
    // ���: MotionEvent: �켣���¼��Ķ���.
    // ����ֵ: �����ϣ�������ص�����ü�ֵ, �򷵻� true; ���򷵻�false.
    public boolean onTracjballEvent(MotionEvent event){
		return false;
    }

    // 6.4 �����������¼��ص�. 
    // ���: MotionEvent: �켣���¼��Ķ���.
    // ����ֵ: �����ϣ�������ص�����ü�ֵ, �򷵻� true; ���򷵻�false.
    public boolean onTouchEvent(MotionEvent event){
		return false;
    }

    // 6.5 ������ı���¼��ص�. 
    // ���: gainFoucus: ���������¼���view�Ƿ����˽���; 
    //		direction: ��ʾ�����ƶ��ķ���;
    // 		previous: ��ʾ�ڴ����¼���view��ͬһ��������ϵ��ǰһ����ý���Ľ������.
    // ����ֵ: �����ϣ�������ص�����ü�ֵ, �򷵻� true; ���򷵻�false.
    public boolean onFocusChanges(boolean gainFoucus, int direction, Rect previous){
		return false;
    }
    // 6. �������ص� �� ����, end
    
    // 7. ��ť����, begin
    private void onButtonClick(){
    	// ��ȡʵ������.
    	final Button append_button = (Button)findViewById(R.id.append);
    	final Button clear_button = (Button)findViewById(R.id.clear);
    	final Button start_service_button = (Button)findViewById(R.id.start_service);
    	final Button start_dialer_button = (Button)findViewById(R.id.start_dialer_lite);
    	final Button start_sms_button = (Button)findViewById(R.id.start_sms_lite);
    	
    	final NeverEdit to_be_edit = (NeverEdit)findViewById(R.id.never_edit);
    	final TextView status_tv = (TextView)findViewById(R.id.save_text);
    	
    	// �����û����.
    	OnClickListener button_click = new OnClickListener(){
			@Override
			public void onClick(View v) {
    			if (v == append_button){
    				// ����ַ���
    				to_be_edit.append(getResources().getString(R.string.clear));
    				status_tv.setText(R.string.append);
    			}else if(v == clear_button){
    				// ����ַ���
    				to_be_edit.getText().clear();
    				status_tv.setText(R.string.save_status_init);
    			// 8. ��ת����һ�� activity, begin
    			}else if(v == start_service_button){
        			// ���� service manager ����.
    				Log.d(TAG, "serviec manager start...");
    				
    				Intent start_manager_intent = new Intent();
    				start_manager_intent.setClass(MusicPlayerLite.this, ServiceManager.class);
    				
    	    		startActivity(start_manager_intent);
    			}else if(v == start_dialer_button){
        			// ���� dialer lite ����.
    				Log.d(TAG, "dialer lite start...");
    				
    				Intent start_dialer_intent = new Intent();
    				start_dialer_intent.setClass(MusicPlayerLite.this, DialerLite.class);
    				
    	    		startActivity(start_dialer_intent);
    			}else if(v == start_sms_button){
        			// ���� SMS lite ����.
    				Log.d(TAG, "SMS lite start...");
    				
    				Intent start_sms_intent = new Intent();
    				start_sms_intent.setClass(MusicPlayerLite.this, SMSLite.class);
    				
    	    		startActivity(start_sms_intent);
    	    	// 8. ��ת����һ�� activity, end
    			}else{
    				// ���������view, ʲôҲ����.
    			}
			}
    	};
    	
    	// ��������. ע��, �˴������ᱻ����.
    	append_button.setOnClickListener(button_click);
    	clear_button.setOnClickListener(button_click);
    	
    	start_service_button.setOnClickListener(button_click);
    	start_dialer_button.setOnClickListener(button_click);
    	start_sms_button.setOnClickListener(button_click);
    }
    // 7. ��ť����, end
    
    // 8. ��ת����һ�� activity, begin
    // ��ش��� move �� ListViewShow.java 
    // 8. ��ת����һ�� activity, end
    
    // 16. ý�岥����(MusicPlayLite), begin
    int status;
    ImageButton play_or_pause_button;
    ImageButton stop_button;
    Uri nowPlaying;
    OnClickListener listener;
    UpdateReceiver doUpdate;
    
    // ���ܲ���״̬�������û�����.
    public class UpdateReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context content, Intent intent) {
			// ��ȡ״̬
			status = intent.getIntExtra("status", -1);
			switch(status){
			case MusicPlayService.STATUS_PLAYING:
				play_or_pause_button.setImageResource(R.drawable.pauseselector);
				break;
			case MusicPlayService.STATUS_PAUSED:
			case MusicPlayService.STATUS_STOPPED:
				play_or_pause_button.setImageResource(R.drawable.playselector);
				break;
			}
		}
    }
    // 16. ý�岥����(MusicPlayLite), end
    
    // 0. �����������+log, begin
	private static final String TAG = "[MusicPlayer Lite]";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // ���� textview�������ַ���.
		TextView save_status_tv = (TextView)findViewById(R.id.save_text);
		save_status_tv.setText(R.string.save_status_init);
		
		// ���������Ĳ˵�
		TextView context_menu_tv = (TextView)findViewById(R.id.context_menu_tv);
		registerForContextMenu(context_menu_tv);
		// setContentView(context_menu_tv);   // �˺����� onCreate() ��ֻ�ܵ���һ��.
		
		// ����ť.
		onButtonClick();
		
		// 16. ý�岥����(MusicPlayLite), begin
		play_or_pause_button = (ImageButton)findViewById(R.id.play_or_pause);
		stop_button = (ImageButton)findViewById(R.id.stop);
		
		// ���õ�ǰ���ŵ� uri
		nowPlaying = Uri.parse("http://www.5billion.com.cn/music.mp3");
		
		// ���尴ť�����¼���������
		listener = new OnClickListener(){
			@Override
			public void onClick(View view){
				// ����ѡ�����ڲ��ŵ����֣�ֱ�Ӻ��Բ���.
				if(nowPlaying == null){
					return;
				}
				
				int cmd = 0;
				Intent i = new Intent(MusicPlayService.MUSIC_CONTROL);
				switch(view.getId()){
				case R.id.play_or_pause:
					if(status == MusicPlayService.STATUS_PLAYING){
						// ���ǵ�ǰ���ڲ���ʱ������ͣ
						cmd = MusicPlayService.CMD_PAUSE;
					}else if(status == MusicPlayService.STATUS_STOPPED){
						// ����ֹͣ״̬���Ͱ�Uriװ�벢����
						i.setData(nowPlaying);
						cmd = MusicPlayService.CMD_PLAY;
					}else{
						// ������ͣ״̬���ͼ�������
						cmd = MusicPlayService.CMD_PLAY;
					}
					break;
					
				case R.id.stop:
					cmd = MusicPlayService.CMD_STOP;
					break;
					
				default:
					break;
				}
				
				i.putExtra("cmd", cmd);
				sendBroadcast(i);
			}
		};
		
		play_or_pause_button.setOnClickListener(listener);
		stop_button.setOnClickListener(listener);
		
		doUpdate = new UpdateReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(MusicPlayService.UPDATE_STATUS);
        registerReceiver(doUpdate,filter);
        
        startService(new Intent(this,MusicPlayService.class));
		// 16. ý�岥����(MusicPlayLite), end
		
        Log.v(TAG,"onCreate");
    }
    
	@Override
    public void onRestart(){
    	super.onRestart();
    	Log.v(TAG,"onRestart");
    }
    
    @Override
    public void onStart(){
    	super.onStart();
    	Log.v(TAG,"onStart");
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	Log.v(TAG,"onResume");
    }
    
    @Override
    public void onPause(){
    	super.onPause();
    	Log.v(TAG,"onPause");
    }
    
    @Override
    public void onStop(){
    	super.onStop();
    	Log.v(TAG,"onStop");
    }
    
    @Override
    public void onDestroy(){
    	// 16. ý�岥����(MusicPlayLite), begin
    	if (status == MusicPlayService.STATUS_STOPPED) {
            stopService(new Intent(this,MusicPlayService.class));
        }
        unregisterReceiver(doUpdate);
        // 16. ý�岥����(MusicPlayLite), end
        
    	super.onDestroy();
    	Log.v(TAG,"onDestroy");
    }
    // 0. �����������+log, end
    
    // 12. ���û�ѡ�񷵻ظ��ϲ� activity, begin
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent  data){
    	TextView save_status_tv = (TextView)findViewById(R.id.save_text);
    	save_status_tv.setText(data.toString());
    	
    	
    }
    // 12. ���û�ѡ�񷵻ظ��ϲ� activity, end
}