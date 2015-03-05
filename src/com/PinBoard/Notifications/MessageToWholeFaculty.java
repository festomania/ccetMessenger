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
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MessageToWholeFaculty extends Activity {
	
	EditText messageEdittext;
	String message;
	CheckBox dntsaveNjustSend;
	CheckBox branchCheckBox;
	CheckBox sendtoallCheckBox;
	Spinner branchSpinner;
	String college;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_to_whole_faculty);

		messageEdittext=(EditText) findViewById(R.id.MsgtowholefacultyEditText);
	    dntsaveNjustSend=(CheckBox) findViewById(R.id.dontsaveserverWholeFacultyMsg);
	    college=getResources().getString(R.string.college);
	    branchCheckBox=(CheckBox) findViewById(R.id.checkBox2);
	    branchSpinner= (Spinner) findViewById(R.id.BranchSpinnerFacCommunication);
	    sendtoallCheckBox=(CheckBox) findViewById(R.id.SendtowholefacultycheckBox);
	}

	public void sendMessageToWholeFaculty(View view){
		message=messageEdittext.getText().toString();
		if(message.length()>0 && checkNetwork()){
			if(dntsaveNjustSend.isChecked()){
				onlyBroadcast();
			}else{
				saveAndBroadcastmessage(message);
			}
		}
	}
	
	void onlyBroadcast(){
		ParsePush push = new ParsePush();
		//push.setChannel("Faculty");
		push.setMessage(transformMessage(message));
		

		ParseQuery pushQuery = ParseInstallation.getQuery();

		pushQuery.whereEqualTo("college", college);
		pushQuery.whereEqualTo("isFaculty", true);
		
		if(!sendtoallCheckBox.isChecked()){
			if(branchCheckBox.isChecked()){
				pushQuery.whereEqualTo("branch", String.valueOf(branchSpinner.getSelectedItem()));
			}
		}
		push.setQuery(pushQuery);
		
		final ProgressDialogBox mydialog = new ProgressDialogBox(this, "Sending attendence..", "Loading");
		mydialog.showDialog();
		push.sendInBackground( new SendCallback() {
			@Override
			public void done(ParseException e) {
				Toast toast = Toast.makeText(getApplicationContext(), "Successfully sent", Toast.LENGTH_SHORT);
				toast.show();
				mydialog.dismissDialog();
			}
		});
	}
	
	
	public void saveAndBroadcastmessage(String msg){
		ParseQuery pushQuery = ParseInstallation.getQuery();
		ParseObject facultyMessageAttendenceClass = new ParseObject("FacultyAttendenceMsgs");
		
			
			final ParsePush push = new ParsePush();
			push.setMessage(transformMessage(msg));
			//push.setChannel("Faculty");
			if(!sendtoallCheckBox.isChecked()){
				if(branchCheckBox.isChecked()){
					pushQuery.whereEqualTo("branch", String.valueOf(branchSpinner.getSelectedItem()));
				}
			}
			
			pushQuery.whereEqualTo("college", college);
			pushQuery.whereEqualTo("isFaculty", true);
			push.setQuery(pushQuery);
			
			//push.setQuery(pushQuery);
			//Log.e("Before sendinbackrnd: ", transformMessage(msg));
			final ProgressDialogBox mydialog = new ProgressDialogBox(this, "Sending message..", "Loading");
			mydialog.showDialog();
			
			facultyMessageAttendenceClass.put("message", transformMessage(msg));
			facultyMessageAttendenceClass.put("college", college);
			facultyMessageAttendenceClass.put("senderName", "By: "+ParseUser.getCurrentUser().get("username"));
			facultyMessageAttendenceClass.put("author", ParseUser.getCurrentUser().getString("username"));
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
			newMsg="\""+msg+"\"";
			newMsg=newMsg+"\n\n" + "From: "+ currentUser.getString("username")+
			"\n(Deptt: "+currentUser.get("deptt")+")"+"\nTHIS MESSAGE WAS SENT TO ALL FACULTY MEMBERS";
			
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

}
