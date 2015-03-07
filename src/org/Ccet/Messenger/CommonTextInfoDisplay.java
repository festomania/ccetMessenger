package org.Ccet.Messenger;

import java.util.List;
import com.PinBoard.Notifications.*;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import java.util.List;
import org.Ccet.Messenger.Pingbook.R;

import com.parse.ParseObject;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CommonTextInfoDisplay extends Activity {
	
	TextView contentTextView;
	TextView heading;
	String className;
	Context context;
	Button invivibleWantSimilarApp;
	Menu menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_text_info_display);
		contentTextView= (TextView) findViewById(R.id.contentOfAboutApp);
		invivibleWantSimilarApp=(Button) findViewById(R.id.wantsimilarappInvivisible);
		contentTextView.setTextIsSelectable(true);
		
		heading= (TextView) findViewById(R.id.layoutTextHeading);
		className=getIntent().getStringExtra("className");
		heading.setText(getIntent().getStringExtra("heading"));
		if(className.equalsIgnoreCase("AboutApp")){
			invivibleWantSimilarApp.setVisibility(0);
		}
		context=this;
		fetchcontent();
	}
	
	

	public void navigateToWantSimilarApp(View view){
		Intent intent2=new Intent(CommonTextInfoDisplay.this,CommonTextInfoDisplay.class);
		intent2.putExtra("heading", "Want Similar App?");
		intent2.putExtra("className", "WantYourOwnApp");
		startActivity(intent2);
	}
	
	private void fetchcontent() {
		
		final ProgressDialogBox myDialog= new ProgressDialogBox(this, "Fetching Data", "Loading");
		myDialog.showDialog();
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
				className);

		query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				// TODO Auto-generated method stub
				myDialog.dismissDialog();
				displayData(objects.get(0));
				
			}
		});
	}
	
	private void displayData(final ParseObject obj) {
		contentTextView.setText(obj.getString("content"));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.common_text_info_display, menu);
		this.menu=menu;
		updateMenu(true);
		return true;
	}
	
	void updateMenu(Boolean val){
		MenuItem disclaimericon= menu.findItem(R.id.disclaimer);
		if(className.equalsIgnoreCase("AboutApp") && val==true){;
			disclaimericon.setVisible(true);
		}
		if(val==false){
			disclaimericon.setVisible(false);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        
        case R.id.disclaimer:
        	if(checkNetwork())
    		{
        		updateMenu(false);
    			heading.setText("Disclaimer !!");
    			contentTextView.setText("Pingbook Inc. is not responsible for any kind of misuse of this app.\n" +
    					"We are not responsible for any kind of offensive/vulgarity/fake content. \n" +
    					"Thanks\n\n" +
    					"Regards\n" +
    					"Pingbook Inc.");
    		}
            return true;
        
        default:
            return super.onOptionsItemSelected(item);
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
