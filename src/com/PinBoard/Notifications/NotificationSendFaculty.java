package com.PinBoard.Notifications;
import org.json.JSONException;
import org.json.JSONObject;

import org.Ccet.Messenger.Pingbook.R;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SendCallback;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NotificationSendFaculty extends Activity {

	public EditText messageToBePushed;
	private Spinner deptt_spinner;
	private Spinner branch_spinner;
	private Spinner sem_spinner;
	CheckBox sendtoallCheck;
	CheckBox depttCheck;
	CheckBox branchCheck;
	CheckBox semCheck;
	CheckBox dntsaveNjustSend;
	String college;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification_send_faculty);
		messageToBePushed=(EditText) findViewById(R.id.MessageData);
		this.deptt_spinner=(Spinner) findViewById(R.id.DepttspinnerFaculty);
		this.branch_spinner=(Spinner) findViewById(R.id.BranchSpinnerFaculty);
	    this.sem_spinner=(Spinner) findViewById(R.id.SemSpinnerFaculty);
	    sendtoallCheck = (CheckBox) findViewById(R.id.SendToAllcheckBox);
	    depttCheck=(CheckBox) findViewById(R.id.DepttcheckBox);
	    branchCheck=(CheckBox) findViewById(R.id.branchcheckBox3);
	    semCheck=(CheckBox) findViewById(R.id.SemcheckBox);
	    dntsaveNjustSend=(CheckBox) findViewById(R.id.DontSaveMsgscheckBox);
	    college=getResources().getString(R.string.college);
		
		Button sendPushButton = (Button) findViewById(R.id.sendPushButton);
		sendPushButton.setOnClickListener(new View.OnClickListener() 
		{
			
			public void onClick(View v) 
			{
				if(messageToBePushed.getText().length()>0 && checkNetwork()){
					if(dntsaveNjustSend.isChecked()){
						onlyBrodcastMessage(messageToBePushed.getText().toString());
					}else{
						saveAndBroadcastmessage(messageToBePushed.getText().toString());
					}
				}
			}
		});
		
	}

	
	
	@SuppressWarnings("unchecked")
	public void saveAndBroadcastmessage(String msg){
		ParseQuery pushQuery = ParseInstallation.getQuery();
		ParseObject messageClass = new ParseObject("Messages");

		pushQuery.whereEqualTo("college", college);
		pushQuery.whereEqualTo("pushTurnedOn", true);
		
		
		if(!sendtoallCheck.isChecked()){
			if(depttCheck.isChecked()){
				pushQuery.whereEqualTo("deptt", String.valueOf(deptt_spinner.getSelectedItem()));
				messageClass.put("deptt", String.valueOf(deptt_spinner.getSelectedItem()));
			}
			else{
				messageClass.put("deptt", "sendToAll");
			}
			if(branchCheck.isChecked()){
				pushQuery.whereEqualTo("branch", String.valueOf(branch_spinner.getSelectedItem()));
				messageClass.put("branch", String.valueOf(branch_spinner.getSelectedItem()));
			}
			else{
				messageClass.put("branch", "sendToAll");
			}
			if(semCheck.isChecked()){
				pushQuery.whereEqualTo("sem", String.valueOf(sem_spinner.getSelectedItem()));
				messageClass.put("sem", String.valueOf(sem_spinner.getSelectedItem()));
			}
			else{
				messageClass.put("sem", "sendToAll");
			}
		}
		else{
			messageClass.put("sendToAll", "sendToAll");
		}
		
		if(!sendtoallCheck.isChecked()&&!depttCheck.isChecked() && !branchCheck.isChecked()&&!semCheck.isChecked()){
			Toast toast = Toast.makeText(getApplicationContext(), 
					"Pls mark the checkbox to whom you want to send the message", Toast.LENGTH_SHORT);
			toast.show();
		}
		else{
			
			final ParsePush push = new ParsePush();
			push.setMessage(transformMessage(msg));
			push.setQuery(pushQuery);
			Log.e("Before sendinbackrnd: ", transformMessage(msg));
			final ProgressDialogBox mydialog = new ProgressDialogBox(this, "Sending notification..", "Loading");
			mydialog.showDialog();

			messageClass.put("college", college);
			messageClass.put("message", msg);
			messageClass.put("senderName", ParseUser.getCurrentUser().get("username"));
			messageClass.saveInBackground( new SaveCallback() {
				
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
	
}
	
	public void onlyBrodcastMessage(String msg){
		ParseQuery pushQuery = ParseInstallation.getQuery();
		pushQuery.whereEqualTo("college", college);
		pushQuery.whereEqualTo("pushTurnedOn", true);
		if(!sendtoallCheck.isChecked()){
			if(depttCheck.isChecked()){
				pushQuery.whereEqualTo("deptt", String.valueOf(deptt_spinner.getSelectedItem()));
			}
			if(branchCheck.isChecked()){
				pushQuery.whereEqualTo("branch", String.valueOf(branch_spinner.getSelectedItem()));
			}
			if(semCheck.isChecked()){
				pushQuery.whereEqualTo("sem", String.valueOf(sem_spinner.getSelectedItem()));
			}
		}
		
		if(!sendtoallCheck.isChecked()&&!depttCheck.isChecked() && !branchCheck.isChecked()&&!semCheck.isChecked()){
			Toast toast = Toast.makeText(getApplicationContext(), 
					"Pls mark the checkbox to whom you want to send the message", Toast.LENGTH_SHORT);
			toast.show();
		}
		else{
			
			final ParsePush push = new ParsePush();
			push.setMessage(transformMessage(msg));
			push.setQuery(pushQuery);
			Log.e("Before sendinbackrnd: ", transformMessage(msg));
			final ProgressDialogBox mydialog = new ProgressDialogBox(this, "Sending notification..", "Loading");
			mydialog.showDialog();
			
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
	
}
	
	
	
	public String transformMessage(String msg) {
		
		String newMsg;
		try {
			ParseUser currentUser = ParseUser.getCurrentUser();
			newMsg=(String) currentUser.get("username") +": "+ msg;
			newMsg=newMsg+"\n\n(Deptt: "+currentUser.get("deptt")+")";
			
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
