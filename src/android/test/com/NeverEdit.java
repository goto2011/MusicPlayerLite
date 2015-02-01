package android.test.com;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

// 5. 自定义控件, begin
// 关注 xml 布局中是如何使用此自定义控件的.
public class NeverEdit extends EditText {
	// 构造方法1.
	public NeverEdit(Context context){
		super(context);
	}
	
	// 构造方法2: 用xml布局需要此构造方法
	public NeverEdit(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	// 重新实现对按键按下事件的处理
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 什么也不做. 返回 true 表示已经处理过此事件 
		return true;
	}
}
//5. 自定义控件, end