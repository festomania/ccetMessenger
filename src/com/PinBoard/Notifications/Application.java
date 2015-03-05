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
	//Parse.initialize(this, "OXBewe8dYl2rKMDSjkOgHVaOn4nYvqPrwblkygTQ", "XmVgPkUlM65sxNvTiAJ0XRB20LEAPJMBiywQUaqK"); 

	 //Initialize the Parse SDK. ccetdedwingchd@gmail.com
	Parse.initialize(this, "H9pEVbq5fqf1zmvFr4hlo0IeHqw0PV6BpwV3CFqt", "Y4jwI0mSOq4id24Ktb6gvrLfAhVSRNbsqbwnHbw0"); 

	// Specify an Activity to handle all pushes by default.
	ParseInstallation.getCurrentInstallation().saveInBackground();
	PushService.setDefaultPushCallback(this, MainActivity.class);
  }
}