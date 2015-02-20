package com.PinBoard.Notifications;
import java.util.List;

import org.Ccet.Messenger.R;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.parse.ParseObject;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import com.PinBoard.Notifications.GetMessagesAsyncTask;

public class MessagesListView extends Activity {

	ListView msgListView;
	ProgressDialog mProgressDialog;
	List<ParseObject> finalListofobjects=null;
	private Context context;
	String college;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages_list_view);
		msgListView=(ListView) findViewById(R.id.messagesListView);
		college=getResources().getString(R.string.college);
		
		context=this;
		
		new GetMessagesAsyncTask(context,"Messages", college){
		protected void onPostExecute(List<ParseObject> result)
		{
			mProgressDialog.dismiss();
			try {
				Log.e("Succesfull-->>",(String)result.get(1).get("heading"));
				finalListofobjects=result;
				
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("Error-->>",e.getMessage());
			}
			
			Log.e("Size of result-->>",Integer.toString(result.size()));
			setadapter(result);
		}
		}.execute();
		
		AdView mAdView = (AdView) findViewById(R.id.adViewMessagesList);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
	}
	
	void setadapter(List<ParseObject> result)
	{
		msgListView.setAdapter(new MessageListAdapter(this,result));
	}


}
