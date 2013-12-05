package edu.yu.biengoldmold.collect;

import java.util.Map.Entry;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MessageText extends Activity implements OnClickListener {
	
	private Button save;
	private Button back;
	private EditText message;
	private Context context;
	private Bundle extras;
	private ContentValues cv;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.writetextlayout);
		context = this;
		extras = getIntent().getExtras();
		cv=new ContentValues();
		
		//Put all the extras into a ContentValue pair
		
		for(String s:extras.keySet()){
			if(extras.get(s) instanceof String){
			cv.put(s, (String) extras.get(s));
			}
			else if (extras.get(s) instanceof Integer)
				cv.put(s, (Integer) extras.get(s));
			else if (extras.get(s) instanceof Long)
				cv.put(s, (Long) extras.get(s));
			else if (extras.get(s) instanceof Boolean)
				cv.put(s, (Boolean) extras.get(s));
		}
		
		save = (Button)this.findViewById(R.id.savemessage);
		save.setOnClickListener(this);
		back = (Button)this.findViewById(R.id.backtosetup);
		back.setOnClickListener(this);
		message = (EditText) this.findViewById(R.id.message);
//		if(!extras.getBoolean("email")){
//			message.setText("RemindIt: ");
//		}
//		

	}
	public void onClick(View v) {
		switch(v.getId()){
		//This is where we save the stuff to the database
		case R.id.savemessage:
			String mes = message.getText().toString();
			cv.put(SavedDataOpenHelper.MESSAGE_CONTENT, mes);
			ReminderDBAdapter rda=new ReminderDBAdapter(this);
			rda.insert(cv);
			//String s=rda.rowString(rda.dBsize());//it said dbsize()-1 before for some reason??!???!
			//Toast.makeText(context,""+ s, Toast.LENGTH_LONG).show();
			Intent intent = new Intent(this, ListOfReminders.class);
			this.startActivity(intent);

			
			break;
		case R.id.backtosetup:
			finish();
			break;
		}
	}

}
