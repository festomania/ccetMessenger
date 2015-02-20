package com.PinBoard.Notifications;

import org.Ccet.Messenger.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ForgetPassword extends Activity {

	EditText email;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_password);
		email=(EditText) findViewById(R.id.emailResetPassword);
		
	}

	public void resetYourPassword(View view){
		
		if(checkNetwork())
		{
			final ProgressDialogBox mydialog = new ProgressDialogBox(this, "Sending mail..", "Loading");
			mydialog.showDialog();
			if(email.getText().toString().length()>0){
				ParseUser.requestPasswordResetInBackground( email.getText().toString(), new RequestPasswordResetCallback() {
					
					@Override
					public void done(ParseException e) {
						// TODO Auto-generated method stub
						if(e==null){
							mydialog.dismissDialog();
							Toast toast = Toast.makeText(getApplicationContext(), "Mail sent successfuly", Toast.LENGTH_SHORT);
							toast.show();
						}
						else{
							mydialog.dismissDialog();
							Toast toast = Toast.makeText(getApplicationContext(), "Something went wrong. Please try later", Toast.LENGTH_SHORT);
							toast.show();
						}
						
					}
				});
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
	
	

}
