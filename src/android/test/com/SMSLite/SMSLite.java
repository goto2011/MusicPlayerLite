package android.test.com.SMSLite;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.test.com.R;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// 11. 短信相关功能, begin 
public class SMSLite extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_lite);
        
        // 处理短信发送
        final Button btn_send_sms = (Button)findViewById(R.id.send_SMS);
        final EditText txt_phone_number = (EditText)findViewById(R.id.input_phone_number);
        final EditText txt_sms = (EditText)findViewById(R.id.input_SMS_content);
        
        btn_send_sms.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View v){
        		String phone_number = txt_phone_number.getText().toString();
        		String sms = txt_sms.getTag().toString();
        		
        		if(phone_number.length()>0 && sms.length()>0){
        			sendSMS(phone_number,sms);
        		}else{
        			Toast.makeText(SMSLite.this, R.string.notify_re_input, Toast.LENGTH_LONG).show();
        		}
        	}
        });
    }
    
    // 发送短信.
	protected void sendSMS(String phone_number, String sms) {
		// 使用 PendingIntent 对象, 该对象指向 SMSLite Activity, 
		// 因此当用户按下发送键后, 用户界面会回到SMSLite.
		PendingIntent pi = PendingIntent.getActivity(this, 0,
				new Intent(this, SMSLite.class), 0);
		
		// SmsManager 为 android.telephony.SmsManager 中定义的短信管理类. 它的用法有点特殊, 
		// 不能直接实例化, 而只能调用静态方法 getDefault() 获得对象.
		SmsManager sms_send = SmsManager.getDefault();
		
		// 发送短信到指定号码.
		sms_send.sendTextMessage(phone_number, null, sms, pi, null); 
	}
}
// 11. 短信相关功能, end