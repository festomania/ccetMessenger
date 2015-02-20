package com.PinBoard.Notifications;
import java.util.List;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.Ccet.Messenger.R;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.RefreshCallback;
import com.parse.SaveCallback;

public class StudentPanel extends Activity {

	private RadioButton genderFemaleButton;
	private RadioButton genderMaleButton;
	private EditText ageEditText;
	private EditText name_EditText;
	private RadioGroup genderRadioGroup;
	private Spinner deptt_spinner;
	private Spinner branch_spinner;
	private Spinner sem_spinner;
	
	
	ListView listview;
	List<ParseObject> ob;
	ProgressDialog mProgressDialog;
	ArrayAdapter<String> adapter;

	public static final String GENDER_MALE = "male";
	public static final String GENDER_FEMALE = "female";
	
	String[] deptt_array;
	String[] branch_array;
	String[] sem_array;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_panel);

		TextView text = (TextView) findViewById(R.id.puText);
        //text.setTextSize(22);
		Typeface fontobject=Typeface.createFromAsset(getAssets(),"Roboto-Black.ttf");
        text.setTypeface(fontobject);

		// Track app opens.
		ParseAnalytics.trackAppOpened(getIntent());
		
		// Set up our UI member properties.
		this.genderFemaleButton = (RadioButton) findViewById(R.id.gender_female_button);
		this.genderMaleButton = (RadioButton) findViewById(R.id.gender_male_button);
		this.ageEditText = (EditText) findViewById(R.id.age_edit_text);
		this.genderRadioGroup = (RadioGroup) findViewById(R.id.gender_radio_group);
		this.name_EditText = (EditText) findViewById(R.id.nameEditText);
		this.deptt_spinner=(Spinner) findViewById(R.id.deptt_selector);
		this.branch_spinner=(Spinner) findViewById(R.id.branch_selector);
	    this.sem_spinner=(Spinner) findViewById(R.id.sem_selector);

		deptt_array=getResources().getStringArray(R.array.deptt_arrays);
		branch_array=getResources().getStringArray(R.array.branch_arrays);
		sem_array=getResources().getStringArray(R.array.sem_arrays);
		//displayUserProfile();
		//refreshUserProfile();
		
		
	}

	@Override
	public void onStart() {
		
		Toast toast = Toast.makeText(getApplicationContext(), 
				"Pls Scroll down and Enter correct details", Toast.LENGTH_SHORT);
	    //toast.getView().setBackgroundColor();
		toast.show();
		// Display the current values for this user, such as their age and gender.
		displayUserProfile();
		refreshUserProfile();
		super.onStart();
	}

	// Save the user's profile to their installation.
	public void saveUserProfile(View view) {	
		if(checkNetwork()){
			String ageTextString = ageEditText.getText().toString();
			String nameTextString = name_EditText.getText().toString();
			String depttSelected=String.valueOf(deptt_spinner.getSelectedItem());
			String branchSelected=String.valueOf(branch_spinner.getSelectedItem());
			String semSelected=String.valueOf(sem_spinner.getSelectedItem());
			
		
			if (ageTextString.length() > 0) {
				ParseInstallation.getCurrentInstallation().put("age", Integer.valueOf(ageTextString));
			}
			if (nameTextString.length() > 0) {
				ParseInstallation.getCurrentInstallation().put("name", nameTextString);
			}
			if(depttSelected.length()>0){
				ParseInstallation.getCurrentInstallation().put("deptt", depttSelected);
			}
			if(branchSelected.length()>0){
				ParseInstallation.getCurrentInstallation().put("branch", branchSelected);
			}
			if(semSelected.length()>0){
				ParseInstallation.getCurrentInstallation().put("sem", semSelected);
			}
	
			if (genderRadioGroup.getCheckedRadioButtonId() == genderFemaleButton.getId()) {
				ParseInstallation.getCurrentInstallation().put("gender", GENDER_FEMALE);
			} else if (genderRadioGroup.getCheckedRadioButtonId() == genderMaleButton.getId()) {
				ParseInstallation.getCurrentInstallation().put("gender", GENDER_MALE);
			} else {
				ParseInstallation.getCurrentInstallation().remove("gender");
			}
	
			//InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			//imm.hideSoftInputFromWindow(ageEditText.getWindowToken(), 0);
	
			final ProgressDialogBox mydialog = new ProgressDialogBox(this, "Saving info..", "Loading");
			mydialog.showDialog();
			
			ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
				@Override
				public void done(ParseException e) {
					if (e == null) {
						Toast toast = Toast.makeText(getApplicationContext(), R.string.alert_dialog_success, Toast.LENGTH_SHORT);
						mydialog.dismissDialog();
						toast.show();
					} else {
						e.printStackTrace();
	
						Toast toast = Toast.makeText(getApplicationContext(), R.string.alert_dialog_failed, Toast.LENGTH_SHORT);
						toast.show();
						mydialog.dismissDialog();
						Log.e("Failed Error",e.getMessage());
					}
				}
			});
		}// if condition ends
	}
	
	// Refresh the UI with the data obtained from the current ParseInstallation object.
	private void displayUserProfile() {
		ParseObject object=ParseInstallation.getCurrentInstallation();
		Log.e("1-->>", "Before object!=null");
		if(object!=null)
		{	
			Log.e("name", ParseInstallation.getCurrentInstallation().getString("name"));
			String gender = ParseInstallation.getCurrentInstallation().getString("gender");
			int age = ParseInstallation.getCurrentInstallation().getInt("age");
			String name= ParseInstallation.getCurrentInstallation().getString("name");
			String deptt= ParseInstallation.getCurrentInstallation().getString("deptt");
			String branch= ParseInstallation.getCurrentInstallation().getString("branch");
			String sem= ParseInstallation.getCurrentInstallation().getString("sem");
			Log.e("3-->>", "Strings initialised");
			if (gender != null) {
				genderMaleButton.setChecked(gender.equalsIgnoreCase(GENDER_MALE));
				genderFemaleButton.setChecked(gender.equalsIgnoreCase(GENDER_FEMALE));
			} else {
				genderMaleButton.setChecked(false);
				genderFemaleButton.setChecked(false);
			}
			
			if (age > 0) {
				ageEditText.setText(Integer.valueOf(age).toString());
			}
			if((name.length()>0)){
				name_EditText.setText(name);
			}
			if(deptt.length()>0){
				int pos=0;
				for(int i=0;i<deptt_array.length;i++){
					if(deptt.equalsIgnoreCase(deptt_array[i])){
						pos=i;
					}
				}
				deptt_spinner.setSelection(pos);
			}
			if(branch.length()>0){
				int pos=0;
				for(int i=0;i<branch_array.length;i++){
					if(branch.equalsIgnoreCase(branch_array[i])){
						pos=i;
					}
				}
				branch_spinner.setSelection(pos);
			}
	
			if(sem.length()>0){
				int pos=0;
				for(int i=0;i<sem_array.length;i++){
					if(sem.equalsIgnoreCase(sem_array[i])){
						pos=i;
						break;
					}
				}
				sem_spinner.setSelection(pos);
			}
		}
	}
	
	// Get the latest values from the ParseInstallation object.
	@SuppressWarnings("deprecation")
	private void refreshUserProfile() {		
		ParseInstallation.getCurrentInstallation().refreshInBackground(new RefreshCallback() {
			
			@Override
			public void done(ParseObject object, ParseException e) {
				if (e == null) {
					//displayUserProfile();
					try {
						Log.e("0-->>","Refresh result"+ object.get("sem").toString());
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						Log.e("roobal>>","Refresh result"+ e.getMessage().toString());
						e1.printStackTrace();
					}
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
	
}
