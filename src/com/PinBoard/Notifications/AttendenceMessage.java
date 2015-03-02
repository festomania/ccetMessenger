package com.PinBoard.Notifications;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SendCallback;
import org.Ccet.Messenger.Pingbook.R;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class AttendenceMessage extends Activity {
	
	EditText recipientEdittext;
	EditText messageEdittext;
	String recipient;
	String message;
	CheckBox dntsaveNjustSend;
	String college;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attendence_message);

		recipientEdittext=(EditText) findViewById(R.id.recipientEditText);
		messageEdittext=(EditText) findViewById(R.id.attendenceMsgEditText);

	    dntsaveNjustSend=(CheckBox) findViewById(R.id.dontsaveonserverAttendence);
		showRecipientMsgSharedPref();

		college = getResources().getString(R.string.college);
	}
	
	public void sendMessageToRecipient(View view){
		recipient=recipientEdittext.getText().toString();
		message=messageEdittext.getText().toString();
		if(checkNetwork() && recipient.length()>0 && message.length()>0){
			if(dntsaveNjustSend.isChecked())
				onlyBrodcastMessage(message);
			else
				saveAndBroadcastmessage(message);
		}
	}

	public void onlyBrodcastMessage(final String msg){
		ParseQuery pushQuery = ParseInstallation.getQuery();
			
			final ParsePush push = new ParsePush();
			push.setMessage(transformMessage(msg));

			pushQuery.whereEqualTo("username", recipient);
			pushQuery.whereEqualTo("college", college);
			push.setQuery(pushQuery);
			Log.e("Before sendinbackrnd: ", transformMessage(msg));

			Log.e("Recipient ", recipient + "  "+ msg);
			final ProgressDialogBox mydialog = new ProgressDialogBox(this, "Sending attendence..", "Loading");
			mydialog.showDialog();
			
			push.sendInBackground(new SendCallback() {
				
				@Override
				public void done(ParseException e) {
					// TODO Auto-generated method stub
					if(e==null){
						saveLocallyRecipientMsg(recipient,msg);
						Toast toast = Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_SHORT);
						mydialog.dismissDialog();
						toast.show();
					}
					else{
						Toast toast = Toast.makeText(getApplicationContext(), "Unseccessful", Toast.LENGTH_SHORT);
						toast.show();
						mydialog.dismissDialog();
					}
					
				}
			});
}
	
	
	public void saveAndBroadcastmessage(final String msg){
		ParseQuery pushQuery = ParseInstallation.getQuery();
		ParseObject facultyMessageAttendenceClass = new ParseObject("FacultyAttendenceMsgs");
		
			
			final ParsePush push = new ParsePush();
			push.setMessage(transformMessage(msg));

			pushQuery.whereEqualTo("username", recipient);
			push.setQuery(pushQuery);
			Log.e("Before sendinbackrnd: ", transformMessage(msg));
			final ProgressDialogBox mydialog = new ProgressDialogBox(this, "Sending message..", "Loading");
			mydialog.showDialog();
			
			facultyMessageAttendenceClass.put("message", transformMessage(msg));
			facultyMessageAttendenceClass.put("senderName", "By: "+ParseUser.getCurrentUser().get("username"));
			facultyMessageAttendenceClass.put("recieverName", recipient);
			facultyMessageAttendenceClass.put("college", college);
			facultyMessageAttendenceClass.saveInBackground( new SaveCallback() {
				
				@Override
				public void done(ParseException e) {
					// TODO Auto-generated method stub
					if(e==null){
						push.sendInBackground(new SendCallback() {
							
							@Override
							public void done(ParseException e) {
								// TODO Auto-generated method stub
								if(e==null){
									saveLocallyRecipientMsg(recipient,msg);
									Toast toast = Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_SHORT);
									mydialog.dismissDialog();
									toast.show();
								}
								else{
									Toast toast = Toast.makeText(getApplicationContext(), "Unseccessful", Toast.LENGTH_SHORT);
									toast.show();
									mydialog.dismissDialog();
								}
								
							}
						});
					
					}
					else{
						Toast toast = Toast.makeText(getApplicationContext(), "Unseccessful", Toast.LENGTH_SHORT);
						toast.show();
						mydialog.dismissDialog();
					}
				}
			});
	
}
	
	
public String transformMessage(String msg) {
		
		String newMsg;
		try {
			ParseUser currentUser = ParseUser.getCurrentUser();
			newMsg="To "+recipient + ": " + msg;
			newMsg=newMsg+"\n\n" + "From: "+ currentUser.getString("username")+
			"\n(Deptt: "+currentUser.get("deptt")+")";
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("TransformMsg", e.getMessage());
			return msg;
		}
		return newMsg;
		
	}

	boolean checkNetwork(){
		if (!CheckNetworkStatus.getInstance(this).isOnline()) {
			CheckNetworkStatus.getInstance(this).showerror(this,
					this, "No network available.");
		return false;
		}
		return true;
	}
	
	public void showRecipientMsgSharedPref(){
		SharedPreferences app_preferences = 
	    		PreferenceManager.getDefaultSharedPreferences(this);
		// Get the value for the run counter
	    String recipientSharedpref = app_preferences.getString("recipientAttendence", "");
	    String msgSharedPref = app_preferences.getString("msgAttendence", "");
	    if(recipientSharedpref.length()>0)
	    	recipientEdittext.setText(recipientSharedpref);
	    if(msgSharedPref.length()>0)
	    	messageEdittext.setText(msgSharedPref);
	}
	
	void saveLocallyRecipientMsg(String recipient, String msg){
		SharedPreferences app_preferences = 
	    		PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = app_preferences.edit();
		editor.putString("recipientAttendence", recipient);
		editor.putString("msgAttendence", msg);
		editor.commit();
	}

}
