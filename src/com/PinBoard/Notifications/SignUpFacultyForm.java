package com.PinBoard.Notifications;
import org.Ccet.Messenger.R;
import com.parse.LogInCallback;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import android.net.ParseException;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SignUpFacultyForm extends Activity {
	EditText username;
	EditText password;
	EditText email;
	EditText officialKey;
	EditText officialUsername;
	EditText officialPassword;
	Spinner deptt_Spinner;
	ProgressDialogBox myDialog;
	String college;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up_faculty_form);

		username=(EditText) findViewById(R.id.UsernameEditTextSignup);
		password=(EditText) findViewById(R.id.passwordEditTextSignupS);
		email=(EditText) findViewById(R.id.emailEditText);
		officialUsername=(EditText) findViewById(R.id.officialUsernameEditText);
		officialPassword=(EditText) findViewById(R.id.OfficialPasswordEditText);
		officialKey=(EditText) findViewById(R.id.OfficialKeyEditText);
		deptt_Spinner=(Spinner) findViewById(R.id.depttSpinnerSignUp);
		college=getResources().getString(R.string.college);
		//initialiseViews();

		Toast toast = Toast.makeText(getApplicationContext(), "Imp: Pls enter correct data", Toast.LENGTH_SHORT);
		toast.show();
	}
	
	public void SignUpOnClick(View view) {
		
		if(checkNetwork()){
	
			if(username.getText().toString().length()>0){
				if(password.getText().toString().length()>0){
					if(email.getText().toString().length()>0){
							if(officialUsername.getText().toString().length()>0 && officialPassword.getText().toString().length()>0 && officialKey.getText().toString().length()>0){
								checkOfficialCredentials(officialUsername.getText().toString(), officialPassword.getText().toString());
							}
							else{
								Toast toast = Toast.makeText(getApplicationContext(), "Please enter official credentials", Toast.LENGTH_SHORT);
								toast.show();
							}
					}
					else{
						Toast toast = Toast.makeText(getApplicationContext(), "Please enter Email", Toast.LENGTH_SHORT);
						toast.show();
					}
				}
				else{
					Toast toast = Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_SHORT);
					toast.show();
				}
			}
			else{
				Toast toast = Toast.makeText(getApplicationContext(), "Please enter Username", Toast.LENGTH_SHORT);
				toast.show();
			}
		}
		else{
			Toast toast = Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
	private void checkOfficialCredentials(String offUsername, String offPasswd) {
		final ProgressDialogBox mydialog = new ProgressDialogBox(this, "Verifying official credentials..", "Loading");
		mydialog.showDialog();
		ParseUser.logInInBackground(offUsername, offPasswd, new LogInCallback() {
			
			@Override
			public void done(ParseUser user, com.parse.ParseException e) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				if(user!=null){
					mydialog.dismissDialog();
					if(new String(ParseUser.getCurrentUser().get("officialKey").toString()).equals(officialKey.getText().toString()))
					{
						//signupOnServer();
						saveUsername();
					}
					else{
						Toast toast = Toast.makeText(getApplicationContext(), "Invalid Official Key", Toast.LENGTH_SHORT);
						toast.show();
					}
				}
				else{
					Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
					toast.show();
					mydialog.dismissDialog();
				}
				
			}
		});
	}
	
	private void signupOnServer() {
		// TODO Auto-generated method stub
		
			ParseUser user = new ParseUser();
			user.setUsername(username.getText().toString());
			user.setPassword(password.getText().toString());
			user.setEmail(email.getText().toString());
			user.put("deptt", String.valueOf(deptt_Spinner.getSelectedItem()));
			user.put("college", college);
			user.put("university", "Panjab University, Chd");
			final ProgressDialogBox mydialog = new ProgressDialogBox(this, "Creating account..", "Loading");
			mydialog.showDialog();
			user.signUpInBackground(new SignUpCallback() {
				@Override
				public void done(com.parse.ParseException e) {
					// TODO Auto-generated method stub
					if(e==null){
						mydialog.dismissDialog();
						ParsePush.subscribeInBackground("Faculty");
						Toast toast = Toast.makeText(getApplicationContext(), "Verification Mail Sent: Please verify your account by clicking on link sent to u", Toast.LENGTH_LONG);
						toast.show();
					
					}
					else{
						Toast toast = Toast.makeText(getApplicationContext(), "Unsuccessful"+"\nReason: "+e.getMessage(), Toast.LENGTH_SHORT);
						toast.show();
						mydialog.dismissDialog();
					}
					
				}
			});
		
		
	}
	
	void saveUsername(){
		ParseInstallation.getCurrentInstallation().put("username",username.getText().toString() );
		ParseInstallation.getCurrentInstallation().put("isFaculty",true);
		
		myDialog= new ProgressDialogBox(this, "Fetching..", "Loading");
		myDialog.showDialog();

	ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
		
		@Override
		public void done(com.parse.ParseException e) {
			// TODO Auto-generated method stub
			if (e == null) {
				signupOnServer();
				myDialog.dismissDialog();
			
			} else {
				e.printStackTrace();
				Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
				toast.show();
				myDialog.dismissDialog();
			}
		}
	});
	}
	
	
	void initialiseViews(){
		username=(EditText) findViewById(R.id.UsernameEditTextSignup);
		password=(EditText) findViewById(R.id.passwordEditTextSignupS);
		email=(EditText) findViewById(R.id.emailEditText);
		officialUsername=(EditText) findViewById(R.id.officialUsernameEditText);
		password=(EditText) findViewById(R.id.OfficialPasswordEditText);
		deptt_Spinner=(Spinner) findViewById(R.id.depttSpinnerSignUp);
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
