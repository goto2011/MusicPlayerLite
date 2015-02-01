package android.test.com;

// ========================= 功能列表 =============================
// 0. 生命周期相关+log
// 1. option菜单相关
// 2. context菜单相关
// 3. 对话框
// 4. 进度条
// 5. 自定义控件
// 6. 处理按键回调 和 侦听
// 6.1 处理手机上按键被按下的事件回调
// 6.2 处理手机上按键被按下再抬起的事件回调
// 6.3 处理轨迹球的事件回调
// 6.4 处理触摸屏的事件回调
// 6.5 处理焦点改变的事件回调
// 7. 按钮处理
// 8. 跳转到另一个 activity
// 9. 退出应用
// 10. 启动系统的拨号界面
// 11. 短信相关功能
// 12. 将用户选择返回给上层 activity
// 13. 管理(启动, 关闭, 绑定, 去绑定)service
// 14. 设置应用权限(uses-permission)
// 15. 注册 BroadcastReceiver, 并处理收到的 Broadcast(没搞定)
// 16. 媒体播放器(MusicPlayLite)
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

    // 1. option菜单相关, begin
	// 生成menu菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	// 子菜单
    	SubMenu sub_menu = menu.addSubMenu(R.string.search);
    	sub_menu.setIcon(android.R.drawable.ic_menu_search);
    	sub_menu.add(0,C_MENU_LOCAL_NET,0,R.string.search_local);
    	sub_menu.add(0,C_MENU_INTERNET,0,R.string.search_internet);
    	
    	// 日期选取器
    	menu.add(0,C_MENU_INPUT_DATE,0,R.string.input_date).setIcon(android.R.drawable.ic_menu_today);

    	// 输入星期几
    	menu.add(0,C_MENU_SHOW_WEEK,0,R.string.input_week).setIcon(android.R.drawable.ic_menu_week);

    	// 进度条
    	menu.add(0,C_MENU_SHOW_PROGRESS,0,R.string.show_progress).setIcon(android.R.drawable.ic_menu_gallery);

    	// about 菜单项
    	menu.add(0,MENU_ABOUT,0,R.string.about_me).setIcon(android.R.drawable.ic_menu_help);

    	// save 菜单项
    	menu.add(0,MENU_SAVE,0,R.string.option_menu_save).setIcon(android.R.drawable.ic_menu_save);
    	// delete 菜单项
    	menu.add(0,MENU_DELETE,0,R.string.option_menu_delete).setIcon(android.R.drawable.ic_menu_delete);
    	
    	// 退出
    	menu.add(0,C_MENU_SHOW_EXIT,0,R.string.exit).setIcon(android.R.drawable.ic_menu_delete);
    	
    	return true;
    }
    
    // 响应用户点击事件.
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	super.onOptionsItemSelected(item);
    	
    	// 根据id获取 TextView对象.
		TextView save_status_tv = (TextView)findViewById(R.id.save_text);
		
    	switch(item.getItemId()){
    	case MENU_SAVE:
    		// 根据id加载字符串资源, 并付给 TextView对象.
    		// 另一个看点: 字符串的连接, 应该怎么处理?
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
    	
    	// 8. 跳转到另一个 activity, begin
    	// 输入星期几
    	case C_MENU_SHOW_WEEK:
    		Intent week_intent = new Intent();
    		week_intent.setClass(MusicPlayerLite.this, ListViewShow.class);
    		
    		// 12. 将用户选择返回给上层 activity, begin
    		// 跳转到另一个activity, 但不需要获取返回值
    		// startActivity(week_intent);
    		// 跳转到另一个activity, 且需要获取返回值
    		startActivityForResult(week_intent, DIALOG_DATE_PICKER);
    		// 12. 将用户选择返回给上层 activity, end
    		
    		// 如果调用 finish(), 则跳转到ListViewShow时, MusicPlayerLite 已经销毁了. 
    		// 不调用, 则MusicPlayerLite存在OnPause状态, ListViewShow 退出后, MusicPlayerLite会再显示. 
    		// finish();
    		break;
    	// 8. 跳转到另一个 activity, end
    	
    	case MENU_ABOUT:
	    	// 初始化对话框
	    	showDialog(DLALOG_WELCOME);
	    	break;
	    
	    // 9. 退出代码, begin
    	case C_MENU_SHOW_EXIT:
    		finish();
    		break;
    	}
	    // 9. 退出代码, end
    	
    	return false;
    }
    // 1. option菜单相关, end
    
    // 2. context菜单相关, begin
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
    	super.onCreateContextMenu(menu, v, menuInfo);
    	menu.add(0,C_MENU_NEW,0,R.string.context_menu_new);
    	menu.add(0,C_MENU_OPEN,0,R.string.context_menu_open);
    }
    
    // 响应用户点击事件.
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
    // 2. context菜单相关, end
    
    // 3. 对话框, begin
    @Override
    protected Dialog onCreateDialog(int id){
    	final TextView status_tv = (TextView)findViewById(R.id.save_text);
    	Dialog ret;
    	
    	switch(id){
    	// 一般对话框, begin
    	case DLALOG_WELCOME:
    		// 下面这种语法之所以行得通, 是因为这些 set***() 的函数都返回dialog自身, 所以能连续调用.
    		ret = new AlertDialog.Builder(this)
    			.setIcon(android.R.drawable.ic_dialog_info)
    			.setTitle(R.string.welcom_pro)
    			.setMessage(R.string.welcom)
    			.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener(){
    				// 侦听用户在对话框中的点击. 将点击返回上一层 activity.
					@Override
					public void onClick(DialogInterface dialog, int which) {
						status_tv.setText(R.string.about_me);
					}
    			})
    			.create();
    		break;
        // 一般对话框, end
    		
    	// 日期选取器, begin
    	case DIALOG_DATE_PICKER:
    		Calendar c;
    		c = Calendar.getInstance();
    		
    		ret = new DatePickerDialog(this, new OnDateSetListener(){
    			@Override
    			public void onDateSet(DatePicker v, int y, int m, int d){
    				// 点击确定按钮做的事情.
    	    		status_tv.setText(getResources().getString(R.string.select_time) + y 
    	    				+ getResources().getString(R.string.time_separator) + m 
    	    				+ getResources().getString(R.string.time_separator) + d);
    			}
    		}, c.get(Calendar.YEAR),
    		c.get(Calendar.MONTH),
    		c.get(Calendar.DAY_OF_MONTH));
    		break;
    	// 日期选取器, end
    	
    	// 进度条, begin
    	case DIALOG_PROGRESS:
    		pd = new ProgressDialog(this);
    		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    		pd.setMax(MAX_PROGRESS);
    		pd.setProgress(0);
    		ret = pd;
    		break;
        // 进度条, end
    		
    	default:
    		ret = null;
    		break;
    	}
    	
    	return ret;
    }
    // 3. 对话框, end
    
    // 4. 进度条, begin
    protected void show_progress(){
    	pHandler = new Handler(){
    		@Override
    		public void handleMessage(Message msg){
    			super.handleMessage(msg);
    			// 判断消息
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
    // 4. 进度条, end
    
    // 5. 自定义控件, begin
    // 相关代码 move 到 NeverEdit.java 
    // 5. 自定义控件, end
    
    // 6. 处理按键回调 和 侦听, begin
    // 6.1 处理手机上按键被按下的事件回调. 
    // 入参: keyCode: 按下的键值. KeyEvent: 按键事件的对象.
    // 返回值: 如果不希望其它回调处理该键值, 则返回 true; 否则返回false.
    public boolean onKeyDown(int keyCode, KeyEvent event){
		return false;
    }
    
    // 6.2 处理手机上按键被按下再抬起的事件回调. 
    // 返回值: 如果不希望其它回调处理该键值, 则返回 true; 否则返回false.
    public boolean onKeyUp(int keyCode, KeyEvent event){
		return false;
    }

    // 6.3 处理轨迹球的事件回调. 
    // 入参: MotionEvent: 轨迹球事件的对象.
    // 返回值: 如果不希望其它回调处理该键值, 则返回 true; 否则返回false.
    public boolean onTracjballEvent(MotionEvent event){
		return false;
    }

    // 6.4 处理触摸屏的事件回调. 
    // 入参: MotionEvent: 轨迹球事件的对象.
    // 返回值: 如果不希望其它回调处理该键值, 则返回 true; 否则返回false.
    public boolean onTouchEvent(MotionEvent event){
		return false;
    }

    // 6.5 处理焦点改变的事件回调. 
    // 入参: gainFoucus: 表明触发事件的view是否获得了焦点; 
    //		direction: 表示焦点移动的方向;
    // 		previous: 表示在触发事件的view的同一界面坐标系中前一个获得焦点的界面矩形.
    // 返回值: 如果不希望其它回调处理该键值, 则返回 true; 否则返回false.
    public boolean onFocusChanges(boolean gainFoucus, int direction, Rect previous){
		return false;
    }
    // 6. 处理按键回调 和 侦听, end
    
    // 7. 按钮处理, begin
    private void onButtonClick(){
    	// 获取实例变量.
    	final Button append_button = (Button)findViewById(R.id.append);
    	final Button clear_button = (Button)findViewById(R.id.clear);
    	final Button start_service_button = (Button)findViewById(R.id.start_service);
    	final Button start_dialer_button = (Button)findViewById(R.id.start_dialer_lite);
    	final Button start_sms_button = (Button)findViewById(R.id.start_sms_lite);
    	
    	final NeverEdit to_be_edit = (NeverEdit)findViewById(R.id.never_edit);
    	final TextView status_tv = (TextView)findViewById(R.id.save_text);
    	
    	// 侦听用户点击.
    	OnClickListener button_click = new OnClickListener(){
			@Override
			public void onClick(View v) {
    			if (v == append_button){
    				// 添加字符串
    				to_be_edit.append(getResources().getString(R.string.clear));
    				status_tv.setText(R.string.append);
    			}else if(v == clear_button){
    				// 清除字符串
    				to_be_edit.getText().clear();
    				status_tv.setText(R.string.save_status_init);
    			// 8. 跳转到另一个 activity, begin
    			}else if(v == start_service_button){
        			// 启动 service manager 界面.
    				Log.d(TAG, "serviec manager start...");
    				
    				Intent start_manager_intent = new Intent();
    				start_manager_intent.setClass(MusicPlayerLite.this, ServiceManager.class);
    				
    	    		startActivity(start_manager_intent);
    			}else if(v == start_dialer_button){
        			// 启动 dialer lite 界面.
    				Log.d(TAG, "dialer lite start...");
    				
    				Intent start_dialer_intent = new Intent();
    				start_dialer_intent.setClass(MusicPlayerLite.this, DialerLite.class);
    				
    	    		startActivity(start_dialer_intent);
    			}else if(v == start_sms_button){
        			// 启动 SMS lite 界面.
    				Log.d(TAG, "SMS lite start...");
    				
    				Intent start_sms_intent = new Intent();
    				start_sms_intent.setClass(MusicPlayerLite.this, SMSLite.class);
    				
    	    		startActivity(start_sms_intent);
    	    	// 8. 跳转到另一个 activity, end
    			}else{
    				// 如果是其它view, 什么也不做.
    			}
			}
    	};
    	
    	// 设置侦听. 注意, 此处经常会被忘掉.
    	append_button.setOnClickListener(button_click);
    	clear_button.setOnClickListener(button_click);
    	
    	start_service_button.setOnClickListener(button_click);
    	start_dialer_button.setOnClickListener(button_click);
    	start_sms_button.setOnClickListener(button_click);
    }
    // 7. 按钮处理, end
    
    // 8. 跳转到另一个 activity, begin
    // 相关代码 move 到 ListViewShow.java 
    // 8. 跳转到另一个 activity, end
    
    // 16. 媒体播放器(MusicPlayLite), begin
    int status;
    ImageButton play_or_pause_button;
    ImageButton stop_button;
    Uri nowPlaying;
    OnClickListener listener;
    UpdateReceiver doUpdate;
    
    // 接受播放状态并更新用户界面.
    public class UpdateReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context content, Intent intent) {
			// 获取状态
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
    // 16. 媒体播放器(MusicPlayLite), end
    
    // 0. 生命周期相关+log, begin
	private static final String TAG = "[MusicPlayer Lite]";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // 设置 textview的内容字符串.
		TextView save_status_tv = (TextView)findViewById(R.id.save_text);
		save_status_tv.setText(R.string.save_status_init);
		
		// 设置上下文菜单
		TextView context_menu_tv = (TextView)findViewById(R.id.context_menu_tv);
		registerForContextMenu(context_menu_tv);
		// setContentView(context_menu_tv);   // 此函数在 onCreate() 中只能调用一次.
		
		// 处理按钮.
		onButtonClick();
		
		// 16. 媒体播放器(MusicPlayLite), begin
		play_or_pause_button = (ImageButton)findViewById(R.id.play_or_pause);
		stop_button = (ImageButton)findViewById(R.id.stop);
		
		// 设置当前播放的 uri
		nowPlaying = Uri.parse("http://www.5billion.com.cn/music.mp3");
		
		// 定义按钮单击事件的侦听器
		listener = new OnClickListener(){
			@Override
			public void onClick(View view){
				// 若无选定正在播放的音乐，直接忽略操作.
				if(nowPlaying == null){
					return;
				}
				
				int cmd = 0;
				Intent i = new Intent(MusicPlayService.MUSIC_CONTROL);
				switch(view.getId()){
				case R.id.play_or_pause:
					if(status == MusicPlayService.STATUS_PLAYING){
						// 若是当前正在播放时，就暂停
						cmd = MusicPlayService.CMD_PAUSE;
					}else if(status == MusicPlayService.STATUS_STOPPED){
						// 若是停止状态，就把Uri装入并播放
						i.setData(nowPlaying);
						cmd = MusicPlayService.CMD_PLAY;
					}else{
						// 若是暂停状态，就继续播放
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
		// 16. 媒体播放器(MusicPlayLite), end
		
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
    	// 16. 媒体播放器(MusicPlayLite), begin
    	if (status == MusicPlayService.STATUS_STOPPED) {
            stopService(new Intent(this,MusicPlayService.class));
        }
        unregisterReceiver(doUpdate);
        // 16. 媒体播放器(MusicPlayLite), end
        
    	super.onDestroy();
    	Log.v(TAG,"onDestroy");
    }
    // 0. 生命周期相关+log, end
    
    // 12. 将用户选择返回给上层 activity, begin
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent  data){
    	TextView save_status_tv = (TextView)findViewById(R.id.save_text);
    	save_status_tv.setText(data.toString());
    	
    	
    }
    // 12. 将用户选择返回给上层 activity, end
}