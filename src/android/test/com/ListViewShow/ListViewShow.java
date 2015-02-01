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

//8. 跳转到另一个 activity, begin
//11. 适配器, begin
public class ListViewShow extends ListActivity {
	String[] weekStrings = new String[]{
			"星期一","星期二","星期三","星期四","星期五","星期六","星期天"
	};
	private static final String TAG = "[MusicPlayer Lite]";
	
	// 数组适配器
	ArrayAdapter<String> adapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	
    	// 构造适配器
    	adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, weekStrings);
    	this.setListAdapter(adapter);
    	
    	// 侦听用户的点击
    	this.getListView().setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String item_click_caption = (String) ((TextView)arg1).getText();
				
				ListViewShow.this.setTitle(item_click_caption);
				Log.v(TAG, item_click_caption + " click!");
				
				// 12. 将用户选择返回给上层 activity, begin
				setResult(RESULT_OK, (new Intent()).setAction(item_click_caption));
				// 退出
				finish();
				// 12. 将用户选择返回给上层 activity, end
			}
    	});
    	
    	// 侦听用户的选择(一般指轨迹球)
    	this.getListView().setOnItemSelectedListener(new OnItemSelectedListener(){
			// 被选择
    		@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				ListViewShow.this.setTitle(((TextView)arg1).getText());
				Log.v(TAG,((TextView)arg1).getText() + " selected");
			}
    		
    		// 所有子控件都没有被选择
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				ListViewShow.this.setTitle("Nothing selected.");
			}
    	});
    }
}
//11. 适配器, end
//8. 跳转到另一个 activity, end