package edu.yu.biengoldmold.collect;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.*;
import android.os.Bundle;
import android.telephony.*;
import android.util.Log;
import android.widget.Toast;
//http://androidsourcecode.blogspot.com/2010/10/receiving-sms-using-broadcastreceiver.html
public class SMSReceiver extends BroadcastReceiver {

	private static final String TAG = "Message received";
	private static final String MAGICPHRASE = "cancel#";
	
	
	@Override
	public void onReceive(Context context, Intent intent) {    
		Bundle pudsBundle = intent.getExtras();
		Object[] pdus = (Object[]) pudsBundle.get("pdus");
		SmsMessage messages =SmsMessage.createFromPdu((byte[]) pdus[0]);    
		Log.i(TAG,  messages.getMessageBody());
		ReminderDBAdapter r = new ReminderDBAdapter(context);
		if(messages.getMessageBody().toLowerCase().startsWith(MAGICPHRASE)){
			String phoneNum = messages.getOriginatingAddress();
			int primaryKey = Integer.parseInt(messages.getMessageBody().substring(MAGICPHRASE.length()).trim());
			abortBroadcast();
			if(r.verifyPhoneNumer(primaryKey, phoneNum)){//------this has worked without this if------
			String ns = Context.NOTIFICATION_SERVICE;
			NotificationManager mNotificationManager = (NotificationManager)
			context.getSystemService(context.NOTIFICATION_SERVICE);
			
		//	Instantiate the Notification:
			int icon = R.drawable.ic_launcher;
			CharSequence tickerText = "RemindIt";
			long when = System.currentTimeMillis();

			Notification notification = new Notification(icon, tickerText, when);
			//Define the notification's message and PendingIntent:
			String s = r.getRow(primaryKey).get(SavedDataOpenHelper.CONTACT_NAME);
			CharSequence contentTitle = "Automated Reminder";
			CharSequence contentText = s + " has canceled a reminder";
			Intent notificationIntent = new Intent(context, ListOfReminders.class);
			PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

			notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
			//Pass the Notification to the NotificationManager:
			final int HELLO_ID = 23;

			mNotificationManager.notify(HELLO_ID, notification);
			r.delete(primaryKey);
//				String notifyDeleteString=r.getRow(primaryKey).get(SavedDataOpenHelper.CONTACT_NAME)+" has canceled your reminder.";
//				Toast.makeText(context, notifyDeleteString,
//						Toast.LENGTH_LONG).show();
			}
//		Toast.makeText(context, messages.getOriginatingAddress() + "-" + messages.getMessageBody().substring(MAGICPHRASE.length()).trim(),
//				Toast.LENGTH_LONG).show();
		//tested and works. receiving text: "Tszcancel 7 " -> returned "9148375150-7" my number being 9148375150. questions ask me
		}else{
			//this causes a force close for some reason??????
			Toast.makeText(context, "" + r.dBsize(),
					Toast.LENGTH_LONG).show();
		//returned "FALSE" when i was texted "The"
		}
		
	}

}