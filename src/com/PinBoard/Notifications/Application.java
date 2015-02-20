package com.PinBoard.Notifications;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

public class Application extends android.app.Application {

  public Application() {
  }

  @Override
  public void onCreate() {
    super.onCreate();

	//// Initialize the Parse SDK. roobaljindal9@gmail.com
	//Parse.initialize(this, "iGxAhxxQQDLizFPi0ROnhX6bC6NoK2DQeIEsM7qH", "rd7xJ01MtkmWQfklPGa1I2lEAU4N47QmXR7j3Ggw"); 
	
     //Initialize the Parse SDK. pussgrchsp@gmail.com
	Parse.initialize(this, "OXBewe8dYl2rKMDSjkOgHVaOn4nYvqPrwblkygTQ", "XmVgPkUlM65sxNvTiAJ0XRB20LEAPJMBiywQUaqK"); 

	// Specify an Activity to handle all pushes by default.
	ParseInstallation.getCurrentInstallation().saveInBackground();
	PushService.setDefaultPushCallback(this, MainActivity.class);
  }
}