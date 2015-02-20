package com.PinBoard.Notifications;
import java.util.List;

import org.Ccet.Messenger.R;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

public class NoticesListView extends Activity {

	ListView noticeList;
	ProgressDialog mProgressDialog;
	List<ParseObject> finalListofobjects=null;
	private Context context;
	String heading;
	String content;
	String className;
	String college;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notices_list_view);
		noticeList= (ListView) findViewById(R.id.listview);
		context=this;
		heading=(getIntent().getStringExtra("heading"));
		content=(getIntent().getStringExtra("content"));
		className=(getIntent().getStringExtra("className"));
		college=getResources().getString(R.string.college);
		
		new GetNoticesAsyncTask(context,className,college){
		protected void onPostExecute(List<ParseObject> result)
		{
			mProgressDialog.dismiss();
			try {
				Log.e("Succesfull-->>",(String)result.get(1).get(heading));
				finalListofobjects=result;
				
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("Error-->>",e.getMessage());
			}
			
			Log.e("Size of result-->>",Integer.toString(result.size()));
			setadapter(result);
		}
		}.execute();
		
		AdView mAdView = (AdView) findViewById(R.id.adViewNoticeList);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
		
	}
	
	void setadapter(List<ParseObject> result)
	{
		noticeList.setAdapter(new NoticeListAdapter(this,result,heading, content));
	}

}
