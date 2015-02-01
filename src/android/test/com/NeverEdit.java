package android.test.com;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

// 5. �Զ���ؼ�, begin
// ��ע xml �����������ʹ�ô��Զ���ؼ���.
public class NeverEdit extends EditText {
	// ���췽��1.
	public NeverEdit(Context context){
		super(context);
	}
	
	// ���췽��2: ��xml������Ҫ�˹��췽��
	public NeverEdit(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	// ����ʵ�ֶ԰��������¼��Ĵ���
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// ʲôҲ����. ���� true ��ʾ�Ѿ���������¼� 
		return true;
	}
}
//5. �Զ���ؼ�, end