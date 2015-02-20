package com.PinBoard.Notifications;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckNetworkStatus {
	private static Context mainContext;
	private static CheckNetworkStatus instance;

	public static CheckNetworkStatus getInstance(Context context) {
		mainContext = context;
		if (instance == null) {
			instance = new CheckNetworkStatus(mainContext);

		}

		return instance;
	}

	public CheckNetworkStatus(Context context) {
		mainContext = context;
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) mainContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if ((netInfo != null) && (netInfo.isConnected())) {
			return true;
		}
		return false;
	}


	public void showerror(final Activity activity, Context context, String msg) {
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {

			AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

			alertDialog.setMessage(msg);

			alertDialog.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {

							dialog.dismiss();

						}
					});

			alertDialog.show();

		} else {
			AlertDialog.Builder myDialog = new AlertDialog.Builder(activity,
					AlertDialog.THEME_HOLO_LIGHT);

			myDialog.setTitle("Alert");

			myDialog.setMessage(msg);

			myDialog.setNeutralButton("Okay", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			});

			myDialog.create().show();
		}

	}

}
