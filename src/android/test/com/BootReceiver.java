package android.test.com;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

//15. 注册 BroadcastReceiver, 并处理收到的 Broadcast, begin
public class BootReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// Intent i = new Intent(arg0, MusicPlayService.class);
		Intent i = new Intent(arg0, ServiceManager.class);
		arg0.startService(i);
	}
}
//15. 注册 BroadcastReceiver, 并处理收到的 Broadcast, end