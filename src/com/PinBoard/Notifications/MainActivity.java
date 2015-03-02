package com.PinBoard.Notifications;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.Ccet.Messenger.CommonTextInfoDisplay;
import org.Ccet.Messenger.R;
import com.parse.GetDataCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.RefreshCallback;
import com.parse.SaveCallback;

public class MainActivity extends Activity {
	
	ProgressDialogBox myDialog;
	TextView text;
	int exitCount=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Track app opens.
		ParseAnalytics.trackAppOpened(getIntent());
		text= (TextView) findViewById(R.id.pnjbUniTextView);
		Typeface fontobject=Typeface.createFromAsset(getAssets(),"Roboto-Black.ttf");
        text.setTypeface(fontobject);
        
		try {
			Intent intent = getIntent();
			Bundle extras = intent.getExtras();
			String jsonData = extras.getString("com.parse.Data");
			JSONObject json = new JSONObject(jsonData);
			String pushStore = json.getString("alert");
			
			if(pushStore!=null) {
			    Intent pushIntent = new Intent(MainActivity.this, ShowPushMessage.class);
			    pushIntent.putExtra("pushMessageContent", pushStore);
			    startActivity(pushIntent);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//firstTimeAppDetector();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case R.id.contactUs:
        	Intent intent1=new Intent(MainActivity.this,CommonTextInfoDisplay.class);
			intent1.putExtra("heading", "Contact Us");
			intent1.putExtra("className", "ContactUs");
			startActivity(intent1);
            return true;
        case R.id.aboutAppMenu:
        	if(checkNetwork())
    		{
    			Intent intent6=new Intent(MainActivity.this,CommonTextInfoDisplay.class);
    			intent6.putExtra("heading", "About App");
    			intent6.putExtra("className", "AboutApp");
    			startActivity(intent6);
    		}
            return true;
        case R.id.wantApp:
        	if(checkNetwork())
    		{
    			Intent intent2=new Intent(MainActivity.this,CommonTextInfoDisplay.class);
    			intent2.putExtra("heading", "Want Similar App?");
    			intent2.putExtra("className", "WantYourOwnApp");
    			startActivity(intent2);
    		}
            return true;
        case R.id.fb:
        	Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse("https://www.facebook.com/CCETofficial"));
            startActivity(intent);
        	return true;
        	
        case R.id.clgWebsiteMenu:
        	Intent intent4 = new Intent();
            intent4.setAction(Intent.ACTION_VIEW);
            intent4.addCategory(Intent.CATEGORY_BROWSABLE);
            intent4.setData(Uri.parse("http://www.ccet.ac.in/"));
            startActivity(intent4);
        	return true;
        case R.id.exit:
        	this.finish();
        	return true;
        default:
            return super.onOptionsItemSelected(item);
    }
	}
	
	
	private boolean isAppOpenedFirstTime() {
		SharedPreferences app_preferences = 
	    		PreferenceManager.getDefaultSharedPreferences(this);
		// Get the value for the run counter
	    String nameOfUser = app_preferences.getString("name", "");
	    if(nameOfUser.length()==0)
	    {
			 return true;
	    }
	    return false;
	}

	private void firstTimeAppDetector() {
		SharedPreferences app_preferences = 
	    		PreferenceManager.getDefaultSharedPreferences(this);
		// Get the value for the run counter
	    String nameOfUser = app_preferences.getString("name", "");
	    if(nameOfUser.length()==0 && checkNetwork())
	    {
			 saveUserProfile();
	    }
	}

	@Override
	public void onStart() {
			refreshUserProfile();
		super.onStart();
		exitCount=0;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		exitCount=0;
	}
	
	void saveLocallySharedPref(){
		SharedPreferences app_preferences = 
	    		PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = app_preferences.edit();
		editor.putString("name", "dummy");
		editor.commit();
	}
	
	public void saveUserProfile() {		
		
			String college = getResources().getString(R.string.college);
			ParseInstallation.getCurrentInstallation().put("age", 20);
			ParseInstallation.getCurrentInstallation().put("name", "MyName");
			ParseInstallation.getCurrentInstallation().put("deptt", "BE");
		    ParseInstallation.getCurrentInstallation().put("branch", "CSE");
			ParseInstallation.getCurrentInstallation().put("sem", "1st Sem");
			ParseInstallation.getCurrentInstallation().put("gender", "male");
			ParseInstallation.getCurrentInstallation().put("university", "Panjab University, Chd");
			ParseInstallation.getCurrentInstallation().put("college", college);
			ParseInstallation.getCurrentInstallation().put("pushTurnedOn", true);
			
			myDialog= new ProgressDialogBox(this, "Registering app..", "Loading");
			myDialog.showDialog();

			ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
					Toast toast = Toast.makeText(getApplicationContext(), "Successfuly Registered !", Toast.LENGTH_SHORT);
					toast.show();
					myDialog.dismissDialog();
					saveLocallySharedPref();
				
				} else {
					e.printStackTrace();

					Toast toast = Toast.makeText(getApplicationContext(), R.string.alert_dialog_failed, Toast.LENGTH_SHORT);
					toast.show();
				}
			}
		});
	}
	
	boolean checkNetwork(){
		if (!CheckNetworkStatus.getInstance(this).isOnline()) {
			CheckNetworkStatus.getInstance(this).showerror(this,
					this, "No network available.");
		return false;
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	private void refreshUserProfile() {	
		
		if(!isAppOpenedFirstTime()){
			ParseInstallation.getCurrentInstallation().refreshInBackground(new RefreshCallback() {
				@Override
				public void done(ParseObject object, ParseException e) {
					if (e == null) {
						}
				}
			});
		}
		else{
			if(checkNetwork()){
				myDialog= new ProgressDialogBox(this, "Registering app..", "Loading");
				myDialog.showDialog();
				ParseInstallation.getCurrentInstallation().refreshInBackground(new RefreshCallback() {
					
					@Override
					public void done(ParseObject object, ParseException e) {
						if (e == null) {
							if(checkNetwork())
								myDialog.dismissDialog();
								firstTimeAppDetector();
							//Log.e("0-->>","Refresh result"+ ParseInstallation.getCurrentInstallation().get("sem").toString());
						}
						else{
							myDialog.dismissDialog();
						}
					}
				});
			}
		}
	}
	
	
	public void getToNoticesActivity(View view){
		if(checkNetwork()){
			Intent intent=new Intent(MainActivity.this,NoticesListView.class);
			startActivity(intent);
		}
	}

	public void navigateToStudentPanel(View view){
		SharedPreferences app_preferences = 
	    		PreferenceManager.getDefaultSharedPreferences(this);
		// Get the value for the run counter
	    String nameOfUser = app_preferences.getString("name", "");
	    if(!(nameOfUser.length()==0))
	    {
			Intent intent=new Intent(MainActivity.this,StudentPanel.class);
			startActivity(intent);
	    }else{
	    	checkNetwork();
	    }
	}
	
	public void getToBulletinBoard(View view){
		Intent intent=new Intent(MainActivity.this,BulletinBoard.class);
		startActivity(intent);
	}
	
	public void navigateToFacultyPanel(View view){
		if(!iskeepLoginCheckTrue()){
			Intent intent=new Intent(MainActivity.this,LoginPanel.class);
			startActivity(intent);
		}
		else{
			Intent intent=new Intent(MainActivity.this,FacultyPanel.class);
			startActivity(intent);
		}
	}
	public void navigateToLoginPanel(View view) {
		Intent intent=new Intent(MainActivity.this,LoginPanel.class);
		startActivity(intent);
	}
	
	public void navigateToMessageListView(View view) {
		Intent intent=new Intent(MainActivity.this,MessagesListView.class);
		startActivity(intent);
	}
	
	public void navigateToKnowMeActivity(View view) {
		if(checkNetwork())
		{
			Intent intent=new Intent(MainActivity.this,KnowMe.class);
			intent.putExtra("className", "KnowAboutApp");
			startActivity(intent);
		}
	}
	
	private boolean iskeepLoginCheckTrue() {
		SharedPreferences app_preferences = 
	    		PreferenceManager.getDefaultSharedPreferences(this);
		// Get the value for the run counter
	    int login = app_preferences.getInt("iskeeplogintrue", 0);
		Log.e("MainActivity", "Value of login: "+Integer.toString(app_preferences.getInt("iskeeplogintrue",0)) );

	    if(login==1){
	    	return true;
	    }
	    	return false;
	}
	
	public void onBackPressed(){
		exitCount++;
		if(exitCount>1){
			exitCount=0;
			finish();
		}
		else{
			Toast toast = Toast.makeText(getApplicationContext(), "Press again to exit", Toast.LENGTH_SHORT);
			toast.show();
		}
	}
}
