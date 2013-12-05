package edu.yu.biengoldmold.collect;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import android.R.string;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

public class ListOfReminders extends ListActivity
{

	private Button no;
	private Button yes;
	private ArrayList<String> keys;
	private ReminderDBAdapter r;
	private Context context;
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		r = new ReminderDBAdapter(this);
		context = this;
		no = (Button)this.findViewById(R.id.dialogno);
		yes = (Button)this.findViewById(R.id.dialogyes);
		ArrayList<HashMap<String, String>> al = r.getAllDataFromAllReminders();
		String[] reminders = new String[al.size()];    
		keys = new ArrayList<String>();
		int i = 0;
		GregorianCalendar gc = new GregorianCalendar();
		for(HashMap<String, String> hm: al){
			gc.setTimeInMillis(Long.parseLong(hm.get(SavedDataOpenHelper.NEXT_DATE_TIME)));
			Date d = gc.getTime();
			String id = "To: "+ hm.get(SavedDataOpenHelper.CONTACT_NAME) +"\n" + "Message: " + hm.get(SavedDataOpenHelper.MESSAGE_CONTENT) +"\n"+ "Next reminder: " + d.toLocaleString()+"\n"+ "Number left: " + hm.get(SavedDataOpenHelper.NUMBER_LEFT);
			reminders[i] =  id;
			keys.add(hm.get(SavedDataOpenHelper.REMINDER_ID));

			i++;
		}
		setListAdapter(new ArrayAdapter<String>(this, R.layout.viewallscreen, reminders));



		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent,  View view,
					int position, long id) {
				final int pos = position;
				// When clicked, show a toast with the TextView text
				//				Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
				//						Toast.LENGTH_SHORT).show();
				//View layout = getLayoutInflater().inflate(R.layout.dialoglayout, null);                               
				AlertDialog.Builder builder = new AlertDialog.Builder(ListOfReminders.this);
				//  builder.setView(layout);
				AlertDialog alertDialog = builder.create();
				builder.setMessage("Do you want to delete this message?");
				builder.setNegativeButton("No thanks", new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), "Nothing Deleted",
								Toast.LENGTH_SHORT).show();
					}
				});
				builder.setPositiveButton("Sure am", new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						r.delete(Integer.parseInt(keys.get(pos)));
						Intent i = new Intent(context, ListOfReminders.class);
						context.startActivity(i);
					}
				});
				// this is what you forgot:
				builder.show();

			}
		});



	}

	public void onClick(View v) {
		if(v.getId() == no.getId()){
			Toast.makeText(getApplicationContext(), "no",
					Toast.LENGTH_SHORT).show();
		}else if(v.getId() == yes.getId()){
			Toast.makeText(getApplicationContext(), "yes",
					Toast.LENGTH_SHORT).show();
		}else{
			//others to do later
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(this, ReminderBienGoldMoldActivity.class);
			this.startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);    
	}
} 