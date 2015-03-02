package com.PinBoard.Notifications;

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

public class KnowMe extends Activity {

	private Context context;
	private TextView contentOfKnowMe;
	private ImageView fbLink;
	private ImageView playstoreLink;
	private Button collegeWebsite;
	private String className;
	String college;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_know_me);
		
		contentOfKnowMe=(TextView) findViewById(R.id.contentOfKnowMe);
		fbLink=(ImageView) findViewById(R.id.facebooklink);
		playstoreLink=(ImageView) findViewById(R.id.playstoreLink);
		collegeWebsite=(Button) findViewById(R.id.collegewebsite);
		college=getResources().getString(R.string.college);
		fbLink.setClickable(true);
		playstoreLink.setClickable(true);
		className=getIntent().getStringExtra("className");
		context=this;
		contentOfKnowMe.setTextIsSelectable(true);
		fetchKnoeMeObject();
	}

	private void fetchKnoeMeObject() {
		// TODO Auto-generated method stub
		new GetNoticesAsyncTask(context,className,college){
			protected void onPostExecute(List<ParseObject> result)
			{
				mProgressDialog.dismiss();
				try {
					Log.e("content-->>",(String)result.get(0).getString("content"));
					displayData(result.get(0));
					
				} catch (Exception e) {
					e.printStackTrace();
					Log.e("Error-->>",e.getMessage());
				}
			}
			}.execute();
	}
	
	private void displayData(final ParseObject obj) {
		contentOfKnowMe.setText(obj.getString("content"));
		fbLink.setOnClickListener(new View.OnClickListener() {

	        public void onClick(View v) {
	            Intent intent = new Intent();
	            intent.setAction(Intent.ACTION_VIEW);
	            intent.addCategory(Intent.CATEGORY_BROWSABLE);
	            intent.setData(Uri.parse(obj.getString("facebook")));
	            startActivity(intent);
	        }
	    });
		
		playstoreLink.setOnClickListener(new View.OnClickListener() {

	        public void onClick(View v) {
	            Intent intent = new Intent();
	            intent.setAction(Intent.ACTION_VIEW);
	            intent.addCategory(Intent.CATEGORY_BROWSABLE);
	            intent.setData(Uri.parse(obj.getString("playstore")));
	            startActivity(intent);
	        }
	    });
		
		collegeWebsite.setOnClickListener(new View.OnClickListener() {

	        public void onClick(View v) {
	            Intent intent = new Intent();
	            intent.setAction(Intent.ACTION_VIEW);
	            intent.addCategory(Intent.CATEGORY_BROWSABLE);
	            intent.setData(Uri.parse(obj.getString("collegewebsite")));
	            startActivity(intent);
	        }
	    });
		
	}


}
