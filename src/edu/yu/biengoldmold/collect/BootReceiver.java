package edu.yu.biengoldmold.collect;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.Time;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {


	@Override
	public void onReceive(Context context, Intent intent) { 

		//http://stackoverflow.com/questions/4430849/how-to-set-recurring-alarmmanager-to-execute-code-daily
			

		Intent i = new Intent(context, SendMessage.class);
		PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
		AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
		am.cancel(pi); // cancel any existing alarms
		
		am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
			    SystemClock.elapsedRealtime() + 60000,
			    60000, pi);
		/*Time t = new Time();
		t.setToNow();
		Toast.makeText(context, "I Want To Display Current Time" + t.toString(),
				Toast.LENGTH_LONG).show();*/
	}
}
