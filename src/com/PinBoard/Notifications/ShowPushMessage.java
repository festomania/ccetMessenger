package com.PinBoard.Notifications;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import org.Ccet.Messenger.R;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class ShowPushMessage extends Activity {

	TextView messageContent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_push_message);
		messageContent = (TextView) findViewById(R.id.MessageContent);
		messageContent.setText((getIntent().getStringExtra("pushMessageContent")));
		messageContent.setTextIsSelectable(true);
		
		AdView mAdView = (AdView) findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
	}

	public void NavigateToNoticeList(View view){
		Intent intent=new Intent(this,NoticesListView.class);
		startActivity(intent);
	}
	
	public void navigateToMessageListView(View view) {
		Intent intent=new Intent(ShowPushMessage.this,MessagesListView.class);
		startActivity(intent);
	}
	
	public void navigateToMainActivity(View view){
		Intent intent=new Intent(this,MainActivity.class);
		startActivity(intent);
	}

}
