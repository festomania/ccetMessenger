package com.PinBoard.Notifications;

import org.Ccet.Messenger.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginPanel extends Activity {

	EditText usernameEditText;
	EditText passwordEditText;
	private String username;
	private String password;
	CheckBox loginvaue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_panel);
		usernameEditText=(EditText) findViewById(R.id.UserNameEditText);
		passwordEditText=(EditText) findViewById(R.id.PasswordEditText);
		loginvaue=(CheckBox) findViewById(R.id.keepLoggedIncheckBox);
		showUsernameSharedPref();

		TextView text = (TextView) findViewById(R.id.textView1);
        //text.setTextSize(22);
		Typeface fontobject=Typeface.createFromAsset(getAssets(),"Roboto-Black.ttf");
        text.setTypeface(fontobject);
		
	}

	public void logginButtonClick(View view){
		username=usernameEditText.getText().toString();
		password=passwordEditText.getText().toString();
		
		if((username.length()>0) && password.length()>0 && checkNetwork()){
			final ProgressDialogBox mydialog = new ProgressDialogBox(this, "Logging in..", "Loading");
			mydialog.showDialog();
			ParseUser.logInInBackground(username, password, new LogInCallback() {
				
				@Override
				public void done(ParseUser user, ParseException e) {
					// TODO Auto-generated method stub
					if(user!=null){
						mydialog.dismissDialog();
						if(ParseUser.getCurrentUser().getBoolean("emailVerified"))
						{
							//Toast toast = Toast.makeText(getApplicationContext(), "Succesfully Logged in", Toast.LENGTH_SHORT);
							//toast.show();
							saveLocallyUsername(username);
							if(loginvaue.isChecked()){
								makeLoginValueTrue();
							}
							Intent intent=new Intent(LoginPanel.this,FacultyPanel.class);
							startActivity(intent);
						}else{
							Toast toast = Toast.makeText(getApplicationContext(), "Email not verified. Please verify your account i.e. "
									+ParseUser.getCurrentUser().getString("email"), Toast.LENGTH_LONG);
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

	}
	
	public void showUsernameSharedPref(){
		SharedPreferences app_preferences = 
	    		PreferenceManager.getDefaultSharedPreferences(this);
		// Get the value for the run counter
	    String usernameLogin = app_preferences.getString("usernameLoginPanel", "");
	    if(usernameLogin.length()>0)
	    usernameEditText.setText(usernameLogin);
	}
	
	void saveLocallyUsername(String usernameLogin){
		SharedPreferences app_preferences = 
	    		PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = app_preferences.edit();
		editor.putString("usernameLoginPanel", usernameLogin);
		editor.commit();
	}

	public void signupButtonClick(View view){
		Intent intent=new Intent(LoginPanel.this,SignUpFacultyForm.class);
		startActivity(intent);
	}
	
	private boolean iskeepLoginCheckTrue() {
		SharedPreferences app_preferences = 
	    		PreferenceManager.getDefaultSharedPreferences(this);
		// Get the value for the run counter
	    int login = app_preferences.getInt("iskeeplogintrue", 0);
	    if(login==1){
	    	return true;
	    }
	    	return false;
	}
	
	void makeLoginValueTrue(){
		SharedPreferences app_preferences = 
	    		PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = app_preferences.edit();
		editor.putInt("iskeeplogintrue", 1);
		editor.commit();
		Log.e("Login Panel", "Value of login: "+Integer.toString(app_preferences.getInt("iskeeplogintrue",0)) );

	}
	
	public void navigateToForgetPassword(View view) {
		Intent intent=new Intent(LoginPanel.this,ForgetPassword.class);
		startActivity(intent);
	}
	
	boolean checkNetwork(){
		if (!CheckNetworkStatus.getInstance(this).isOnline()) {
			CheckNetworkStatus.getInstance(this).showerror(this,
					this, "No network available.");
		return false;
		}
		return true;
	}
	
	public void onBackPressed(){
		Intent intent=new Intent(LoginPanel.this,MainActivity.class);
		startActivity(intent);
	}
	

}
