package com.PinBoard.Notifications;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
@SuppressLint("NewApi")
public class GetNoticesAsyncTask extends AsyncTask<Void, Void , List<ParseObject>> {
	protected ProgressDialog mProgressDialog;
	List<ParseObject> listofobjects = null;
	String classNameFromParse;
	Context context;
	//String college="CCET, Chd";
	String college;
	
	public GetNoticesAsyncTask(Context context, String classNameFromParse, String collegeName) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.classNameFromParse=classNameFromParse;
		college=collegeName;
		Log.e("cnst 2-->>","Constructor: 2");
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// Create a progressdialog
		mProgressDialog = new ProgressDialog(context);
		// Set progressdialog title
		mProgressDialog.setTitle("Fetching data from server..");
		// Set progressdialog message
		mProgressDialog.setMessage("Loading...");
		mProgressDialog.setIndeterminate(false);
		// Show progressdialog
		mProgressDialog.show();
	}

	@Override
	protected List<ParseObject> doInBackground(Void... params) {
		// Locate the class table named "Country" in Parse.com
		
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
				classNameFromParse);

		query.orderByDescending("updatedAt");
		query.whereEqualTo("college", college);
		
		query.setLimit(60);
		try {
			listofobjects = query.find();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return listofobjects;
	}

	@Override
	protected void onPostExecute(List<ParseObject> result) {
		// Locate the listview in listview_main.xml

		mProgressDialog.dismiss();
		

}
}