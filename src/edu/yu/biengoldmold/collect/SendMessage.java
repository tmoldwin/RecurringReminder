package edu.yu.biengoldmold.collect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SendMessage extends Service{
	//
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onCreate(){
		super.onCreate();


		ReminderDBAdapter r = new ReminderDBAdapter(this);
		ArrayList<Integer> al = r.getCurrentReminders();

		

		for(Integer i: al){
			HashMap<String, String> row=r.getRow(i);
			String message= "" + row.get(SavedDataOpenHelper.MESSAGE_CONTENT) + " (This is an automated message from RemindIt. Reply cancel#" + i + " to cancel)";
			String phoneNumber=row.get(SavedDataOpenHelper.PHONE_NUMBER);
			sendSMS(phoneNumber, message, i);
			r.updateNextTime(i);
			r.updateNumberLeft(i);
		}
		

	//	sendSMS("9148375150", "This should be sent every 1 bla bla",0);
		stopSelf();
	
	}

	private void sendSMS(String phoneNumber, String message, Integer primary)
	{        
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";
		final Integer primaryKey = primary; 
		boolean sent = false;

		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
				new Intent(SENT), 0);

		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
				new Intent(DELIVERED), 0);

		//---when the SMS has been sent---
		registerReceiver(new BroadcastReceiver(){


			@Override
			public void onReceive(Context arg0, Intent arg1) {
				if (getResultCode() == Activity.RESULT_OK){
//					ReminderDBAdapter r = new ReminderDBAdapter(arg0);
//					r.updateNextTime(primaryKey);
			// I WOULD LIKE TO SEE IF THIS WORKS IF I MOVE IT DOWN TO "SMS deliv"
//					Toast.makeText(getBaseContext(), "SMS sent", 
//							Toast.LENGTH_SHORT).show();
				}else{

					Toast.makeText(getBaseContext(), "SMS not sent", 
							Toast.LENGTH_SHORT).show();
				}
			}
		}, new IntentFilter(SENT));

		//---when the SMS has been delivered---
		registerReceiver(new BroadcastReceiver(){
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				if (getResultCode() == Activity.RESULT_OK){

					Toast.makeText(getBaseContext(), "SMS deliv", 
							Toast.LENGTH_SHORT).show();
				}else{

					Toast.makeText(getBaseContext(), "SMS not deliv", 
							Toast.LENGTH_SHORT).show();
				}
			}
		}, new IntentFilter(DELIVERED));        

		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);        
	}
}
