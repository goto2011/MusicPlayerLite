package android.test.com;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;

// 13. 管理(启动, 关闭, 绑定, 去绑定)service, begin
public class ServiceManager extends Activity {
    OnClickListener listener;
    ServiceConnection connection;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_manager);
        
        connection = new ServiceConnection(){
            @Override
            public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            }
            @Override
            public void onServiceDisconnected(ComponentName arg0) {
            }
        };
        /*定义Button的点击监听器*/
        listener = new OnClickListener(){
            @Override
            public void onClick(View v) {
            	// 第一个入参为发出Intent者, 第二个入参为接受Intent者. 
                Intent i = new Intent(ServiceManager.this,MusicPlayService.class);
                switch (v.getId()) {
                case R.id.start_service:
                    startService(i);
                    break;
                case R.id.stop_service:
                    stopService(i);
                    break;
                case R.id.bind_service:
                    bindService(i, connection, BIND_AUTO_CREATE);
                    break;
                case R.id.unbind_service:
                    unbindService(connection);
                    break;
                default:
                    break;
                }
            }
        };
        /*设置点击监听器*/
        findViewById(R.id.start_service).setOnClickListener(listener);
        findViewById(R.id.stop_service).setOnClickListener(listener);
        findViewById(R.id.bind_service).setOnClickListener(listener);
        findViewById(R.id.unbind_service).setOnClickListener(listener);
    }
}
//13. 管理(启动, 关闭, 绑定, 去绑定)service, end