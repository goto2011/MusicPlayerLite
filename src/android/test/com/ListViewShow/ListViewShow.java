package android.test.com.ListViewShow;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

//8. ��ת����һ�� activity, begin
//11. ������, begin
public class ListViewShow extends ListActivity {
	String[] weekStrings = new String[]{
			"����һ","���ڶ�","������","������","������","������","������"
	};
	private static final String TAG = "[MusicPlayer Lite]";
	
	// ����������
	ArrayAdapter<String> adapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	
    	// ����������
    	adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, weekStrings);
    	this.setListAdapter(adapter);
    	
    	// �����û��ĵ��
    	this.getListView().setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String item_click_caption = (String) ((TextView)arg1).getText();
				
				ListViewShow.this.setTitle(item_click_caption);
				Log.v(TAG, item_click_caption + " click!");
				
				// 12. ���û�ѡ�񷵻ظ��ϲ� activity, begin
				setResult(RESULT_OK, (new Intent()).setAction(item_click_caption));
				// �˳�
				finish();
				// 12. ���û�ѡ�񷵻ظ��ϲ� activity, end
			}
    	});
    	
    	// �����û���ѡ��(һ��ָ�켣��)
    	this.getListView().setOnItemSelectedListener(new OnItemSelectedListener(){
			// ��ѡ��
    		@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				ListViewShow.this.setTitle(((TextView)arg1).getText());
				Log.v(TAG,((TextView)arg1).getText() + " selected");
			}
    		
    		// �����ӿؼ���û�б�ѡ��
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				ListViewShow.this.setTitle("Nothing selected.");
			}
    	});
    }
}
//11. ������, end
//8. ��ת����һ�� activity, end