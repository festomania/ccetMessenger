package com.PinBoard.Notifications;
import org.Ccet.Messenger.Pingbook.R;
import com.parse.ParseUser;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class FacultyPanel extends Activity {
	TextView welcometext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_faculty_panel);
		try {
			welcometext= (TextView) findViewById(R.id.welcomeTexti);
			welcometext.setText("Welcome "+ ParseUser.getCurrentUser().get("username")+" !");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//welcometext.setText("welcome");
	}
	
	public void makeLoginValueFalse(View view){
		SharedPreferences app_preferences = 
	    		PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = app_preferences.edit();
		editor.putInt("iskeeplogintrue", 0);
		editor.commit();
		Log.e("FacultyPanel", "Value of login: "+Integer.toString(app_preferences.getInt("iskeeplogintrue",0)) );
		if(app_preferences.getInt("iskeeplogintrue",0)==0){
			Intent intent=new Intent(FacultyPanel.this,LoginPanel.class);
			startActivity(intent);
		}
	}

	
	public void navigateToSendNotification(View view){
		Intent intent=new Intent(FacultyPanel.this,NotificationSendFaculty.class);
		startActivity(intent);
	}
	
	public void navigateToPostNotice(View view){
		Intent intent=new Intent(FacultyPanel.this,PostNoticeFaculty.class);
		startActivity(intent);
	}

	public void navigateToHome(View view){
		Intent intent=new Intent(FacultyPanel.this,MainActivity.class);
		startActivity(intent);
	}

	public void navigateToAttendenceMessage(View view){
		Intent intent=new Intent(FacultyPanel.this,AttendenceMessage.class);
		startActivity(intent);
	}
	public void navigateToMsgToWholeFaculty(View view){
		Intent intent=new Intent(FacultyPanel.this,MessageToWholeFaculty.class);
		startActivity(intent);
	}
	public void navigateToNoticesforFacultymsgs(View view){
		Intent intent=new Intent(FacultyPanel.this,NoticesListView.class);
		intent.putExtra("heading", "senderName");
		intent.putExtra("content", "message");
		intent.putExtra("className", "FacultyAttendenceMsgs");
		startActivity(intent);
	}
	
}
