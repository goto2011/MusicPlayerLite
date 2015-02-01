package android.test.com.DialerLite;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.test.com.R;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// 10. 启动系统的拨号界面, begin
public class DialerLite extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialer_lite);
        
        // 处理电话号码输入和发送Intent.
    	final Button button = (Button)findViewById(R.id.button_id);
    	final EditText phone_number = (EditText)findViewById(R.id.phone_number_id);
    	
    	button.setOnClickListener(new Button.OnClickListener(){
    		@Override
    		public void onClick(View b){
    			// 检测用户输入的电话号码的合法性
    			String call_number = phone_number.getText().toString();
    			if (PhoneNumberUtils.isGlobalPhoneNumber(call_number)){
	    			// 发送 Intent.
	    			Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel://" + call_number));
	    			startActivity(i);
    			}else{
    				Toast.makeText(DialerLite.this, R.string.notify_incorrect_number,
    						Toast.LENGTH_LONG).show();
    			}
    		}
    	});
    }
}
//10. 启动系统的拨号界面, end