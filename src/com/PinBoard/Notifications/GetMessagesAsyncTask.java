package com.PinBoard.Notifications;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.Ccet.Messenger.R;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@SuppressLint("NewApi")
class GetMessagesAsyncTask extends AsyncTask<Void, Void , List<ParseObject>> {
	ProgressDialog mProgressDialog;
	@SuppressLint("NewApi")
	List<ParseObject> listofobjects = null;
	String classNameFromParse;
	Context context;
	//String college="CCET, Chd";
	String college;
	
	@SuppressLint("NewApi")
	public GetMessagesAsyncTask(Context context, String classNameFromParse,String collegeName) {
		// TODO Auto-generated constructor stub
			this.context=context;
			this.classNameFromParse=classNameFromParse;
			college=collegeName;
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
		query.setLimit(60);
		
		String[] depttSel={"sendToAll", ParseInstallation.getCurrentInstallation().getString("deptt")};
		query.whereContainedIn("deptt",Arrays.asList(depttSel));
		String[] branchSel={"sendToAll", ParseInstallation.getCurrentInstallation().getString("branch")};
		query.whereContainedIn("branch",Arrays.asList(branchSel));
		String[] semSel={"sendToAll", ParseInstallation.getCurrentInstallation().getString("sem")};
		query.whereContainedIn("sem",Arrays.asList(semSel));
		query.whereEqualTo("college", college);
		
			try {
			listofobjects = query.find();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

			List<ParseObject> newListofObjcts = null;
			ParseQuery<ParseObject> sendToAllquery = new ParseQuery<ParseObject>(
				classNameFromParse);
			sendToAllquery.whereEqualTo("sendToAll","sendToAll");

			sendToAllquery.whereEqualTo("college", college);
			try {
				newListofObjcts = sendToAllquery.find();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			List<ParseObject> listFinal = new ArrayList<ParseObject>();
			listFinal.addAll(listofobjects);
			listFinal.addAll(newListofObjcts);
			
		return listFinal;
	}

	@Override
	protected void onPostExecute(List<ParseObject> result) {
		// Locate the listview in listview_main.xml

		mProgressDialog.dismiss();
}
}