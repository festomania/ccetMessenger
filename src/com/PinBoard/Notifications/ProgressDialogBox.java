package com.PinBoard.Notifications;
import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogBox {
	
	private Context mContext;
	private String mMsg;
	private String mTitle;
	final ProgressDialog mProgressDialog;

	public ProgressDialogBox(Context context, String title, String msg) {
		// TODO Auto-generated constructor stub
		this.mContext=context;
		this.mTitle=title;
		this.mMsg=msg;
		mProgressDialog = new ProgressDialog(mContext);
	}
	
	public void showDialog(){
		
		mProgressDialog.setTitle(mTitle);
		mProgressDialog.setMessage(mMsg);
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.show();
	}
	
	public void dismissDialog() {
		mProgressDialog.dismiss();
	}

}
