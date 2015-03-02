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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_text_info_display);
		contentTextView= (TextView) findViewById(R.id.contentOfAboutApp);
		contentTextView.setTextIsSelectable(true);
		heading= (TextView) findViewById(R.id.layoutTextHeading);
		className=getIntent().getStringExtra("className");
		heading.setText(getIntent().getStringExtra("heading"));
		context=this;
		fetchcontent();
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.common_text_info_display, menu);
		return true;
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
	

}
