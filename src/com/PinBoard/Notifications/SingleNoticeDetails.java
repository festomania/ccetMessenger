package com.PinBoard.Notifications;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.Ccet.Messenger.Pingbook.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.parse.ParseFile;

public class SingleNoticeDetails extends Activity {
	TextView heading;
	TextView content;
	String urlOfImageString=null;
	Button checkAttatchment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_notice_details);
		heading=(TextView) findViewById(R.id.heading_single_details);
		content=(TextView) findViewById(R.id.content_single_notice);
		checkAttatchment=(Button) findViewById(R.id.seeAttatchment);
		
		
		heading.setText(getIntent().getStringExtra("heading"));
		content.setText(getIntent().getStringExtra("content"));
		
		try {
			urlOfImageString=getIntent().getStringExtra("imageUrl");
			if(urlOfImageString.length()>0){
				checkAttatchment.setVisibility(1);
			}
			Log.e("ImageUrl->",getIntent().getStringExtra("imageUrl"));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("Image Error","SingleNotice-> "+e.getMessage());
			e.printStackTrace();
		}
		
		content.setTextIsSelectable(true);
		
		AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
	}
	
public void seeImage(View view){
		
		if(urlOfImageString.length()>0)	{
			Intent intent = new Intent();
	        intent.setAction(Intent.ACTION_VIEW);
	        intent.addCategory(Intent.CATEGORY_BROWSABLE);
	        intent.setData(Uri.parse(urlOfImageString));
	        startActivity(intent);
		}else{
			Toast toast = Toast.makeText(getApplicationContext(), "No Image posted for this Notice", Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
}
