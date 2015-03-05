package com.PinBoard.Notifications;
import java.util.List;

import org.Ccet.Messenger.Pingbook.R;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class NoticesListView extends Activity {

	ListView noticeList;
	ProgressDialog mProgressDialog;
	List<ParseObject> finalListofobjects=null;
	private Context context;
	String heading;
	String content;
	String className;

	NoticeListAdapter NL_Adapter;
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
		
		noticeList.setOnItemLongClickListener(new OnItemLongClickListener() {
			
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				showDilog(arg2);
				/*if(iskeepLoginCheckTrue())
				{
					try {
						if(finalListofobjects.get(arg2).getString("author").equals(ParseUser.getCurrentUser().getString("username"))){
							
							finalListofobjects.get(arg2).deleteInBackground(new DeleteCallback() {
								
								@Override
								public void done(ParseException e) {
									finalListofobjects.remove(arg2);
									NL_Adapter.notifyDataSetChanged();
									Toast toast = Toast.makeText(getApplicationContext(), "Deleted Successfully!!", Toast.LENGTH_SHORT);
									toast.show();
								}
							}); 
						}
						else{
							Toast toast = Toast.makeText(getApplicationContext(), "You are not Author of this Notice..!!", Toast.LENGTH_SHORT);
							toast.show();
						}
				} 	catch (Exception e) {
							Log.e("Error->", e.getMessage());
							e.printStackTrace();
						}
						
				}else{
						Toast toast = Toast.makeText(getApplicationContext(), "Please Login First !", Toast.LENGTH_SHORT);
						toast.show();
				}*/
					
				// TODO Auto-generated method stub
				return true;
			}
				
		});
		
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
	
	private boolean iskeepLoginCheckTrue() {
		SharedPreferences app_preferences = 
	    		PreferenceManager.getDefaultSharedPreferences(this);
	    int login = app_preferences.getInt("iskeeplogintrue", 0);
	    if(login==1){
	    	return true;
	    }
	    	return false;
	}
	
	void showDilog(final int arg2){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
	    alert.setTitle("Are you sure? You want to Delete it?");
	    alert.setNegativeButton("Yes",
		        new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int whichButton) {
		            	deleteNotice(arg2);
		            }
		        });
	    alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	        	deleteNotice(arg2);
	        }
	    });


	    alert.show();
		
	}
	
	void setadapter(List<ParseObject> result)
	{
		NL_Adapter=new NoticeListAdapter(this,result,heading, content);
		noticeList.setAdapter(NL_Adapter);
	}
	
	void deleteNotice(final int arg2){
		if(iskeepLoginCheckTrue())
		{
			try {
				if(finalListofobjects.get(arg2).getString("author").equals(ParseUser.getCurrentUser().getString("username"))){
					
					finalListofobjects.get(arg2).deleteInBackground(new DeleteCallback() {
						
						@Override
						public void done(ParseException e) {
							finalListofobjects.remove(arg2);
							NL_Adapter.notifyDataSetChanged();
							Toast toast = Toast.makeText(getApplicationContext(), "Deleted Successfully!!", Toast.LENGTH_SHORT);
							toast.show();
						}
					}); 
				}
				else{
					Toast toast = Toast.makeText(getApplicationContext(), "You are not Author of this Notice..!!", Toast.LENGTH_SHORT);
					toast.show();
				}
		} 	catch (Exception e) {
					Log.e("Error->", e.getMessage());
					e.printStackTrace();
				}
				
		}else{
				Toast toast = Toast.makeText(getApplicationContext(), "Please Login First !", Toast.LENGTH_SHORT);
				toast.show();
		}
			
		// TODO Auto-generated method stub
		
	
	}

}
