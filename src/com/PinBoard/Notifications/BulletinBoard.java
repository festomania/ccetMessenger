package com.PinBoard.Notifications;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.Ccet.Messenger.Pingbook.R;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.SaveCallback;


public class BulletinBoard extends Activity {


	ProgressDialogBox myDialog;
	ToggleButton toggleNotifications;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bulletin_board);
		toggleNotifications=(ToggleButton) findViewById(R.id.toggleButton);
		TextView text = (TextView) findViewById(R.id.textView1);
		Typeface fontobject=Typeface.createFromAsset(getAssets(),"Roboto-Black.ttf");
        text.setTypeface(fontobject);
        toggleNotifications.setChecked(ParseInstallation.getCurrentInstallation().getBoolean("pushTurnedOn"));
	}

	
	public void getToNoticesActivity(View view){
		if(checkNetwork()){
			Intent intent=new Intent(BulletinBoard.this,NoticesListView.class);

			intent.putExtra("heading", "heading");
			intent.putExtra("content", "content");
			intent.putExtra("className", "Notices");
			startActivity(intent);
		}
	}
	
	public void navigateToMessageListView(View view) {
		if(checkNetwork() && !isProfileNotYetCreated()){
			Intent intent=new Intent(BulletinBoard.this,MessagesListView.class);
			startActivity(intent);
		}
	}
	
	public void toggleResponse(View view){
		if(checkNetwork()){
			if(toggleNotifications.isChecked()){
				ParseInstallation.getCurrentInstallation().put("pushTurnedOn", true);
				myDialog= new ProgressDialogBox(this, "Enabling Notifications..", "Loading");
			}
			else{
				ParseInstallation.getCurrentInstallation().put("pushTurnedOn", false);
				myDialog= new ProgressDialogBox(this, "Disabling Notifications..", "Loading");
			}
						
						myDialog.showDialog();
			
					ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
						@Override
						public void done(ParseException e) {
							if (e == null) {
								Toast toast = Toast.makeText(getApplicationContext(), toggleNotifications.getText(), Toast.LENGTH_SHORT);
								toast.show();
								myDialog.dismissDialog();
							
							} else {
								e.printStackTrace();
			
								Toast toast = Toast.makeText(getApplicationContext(), toggleNotifications.getText(), Toast.LENGTH_SHORT);
								toast.show();
							}
						}
					});
		}
		else{
			if(toggleNotifications.isChecked()){
				toggleNotifications.setChecked(false);
			}else{
				toggleNotifications.setChecked(true);
			}
		}
	}
	
	boolean checkNetwork(){
		if (!CheckNetworkStatus.getInstance(this).isOnline()) {
			CheckNetworkStatus.getInstance(this).showerror(this,
					this, "No network available.");
		return false;
		}
		return true;
	}
	
	private boolean isProfileNotYetCreated() {
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


}
