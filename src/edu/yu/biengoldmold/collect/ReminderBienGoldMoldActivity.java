package edu.yu.biengoldmold.collect;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ReminderBienGoldMoldActivity extends Activity implements OnClickListener  {
    /** Called when the activity is first created. */
	private Button createNewReminder;
	private Button viewReminders;
	private Button information;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homescreen);
		createNewReminder = (Button)this.findViewById(R.id.CreateNewReminder);
		createNewReminder.setOnClickListener(this);	
		viewReminders = (Button)this.findViewById(R.id.reminders);
		viewReminders.setOnClickListener(this);	
		information = (Button)this.findViewById(R.id.information);
		information.setOnClickListener(this);	
		
		Intent i = new Intent(this, SendMessage.class);
		PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.cancel(pi); // cancel any existing alarms
		
//		am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//			    SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES,
//			    AlarmManager.INTERVAL_FIFTEEN_MINUTES, pi);
		
		am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
		    SystemClock.elapsedRealtime() + 60000,
		    60000, pi);
		
		//http://stackoverflow.com/questions/4430849/how-to-set-recurring-alarmmanager-to-execute-code-daily
		
	}

	public void onClick(View v) {
		if(v.getId() == createNewReminder.getId()){
			Intent i = new Intent(this, ReminderSetup.class);
			this.startActivity(i);
		}else if(v.getId() == viewReminders.getId()){
			Intent i = new Intent(this, ListOfReminders.class);
			this.startActivity(i);
		}else{
			Intent i = new Intent(this, Information.class);
			this.startActivity(i);
		}

	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(true);
		}
		return super.onKeyDown(keyCode, event);    
	}
}