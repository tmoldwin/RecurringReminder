package edu.yu.biengoldmold.collect;

import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class ReminderSetup extends Activity implements OnClickListener {
	public static final int MINUTE_IN_MILLIS = 60000;
	public static final int HOUR_IN_MILLIS = 60000 * 60;
	public static final int DAY_IN_MILLIS = 60000 * 60 * 24;
	public static final int WEEK_IN_MILLIS = 60000 * 60 * 24 * 7;

	/** Called when the activity is first created. */
	private Button next;
	private Button cancel;
	private EditText freq;
	private EditText numberOfReminders;
	private Spinner messageType;
	private Spinner freqSpinner;
	private int howOften;

	private boolean noContactName; // This will be true if an email or phone
									// number is input directly without a
									// contact name.
	private EditText contact;
	private int frequency;
	private int numReminders;
	
	private DatePicker dp;
	private TimePicker tp;
	private Context context;

	private String contactName;

	private String contactValue;// This will either be a phone number or an
								// email address, for now, it's just the
								// contact's phone number. 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remindersetup);
		context = this;
		next = (Button) this.findViewById(R.id.nextConfig);
		cancel = (Button) this.findViewById(R.id.cancelConfig);
		freq = (EditText) this.findViewById(R.id.FrequencyEditText);
		numberOfReminders=(EditText) this.findViewById(R.id.numberOfReminders);
		
		messageType = (Spinner) this.findViewById(R.id.type_spinner);
		freqSpinner = (Spinner) this.findViewById(R.id.fr);

		next.setOnClickListener(this);
		cancel.setOnClickListener(this);
		noContactName = false;
		contact = (EditText) this.findViewById(R.id.contactinfo);
		contact.setVisibility(EditText.GONE);
		// This is the adapter for the message type spinner
		ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
				this, R.array.messagetype_array,
				android.R.layout.simple_spinner_item);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		messageType.setAdapter(adapter1);

		// This is the adapter for the frequency spinner
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.frequency_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		freqSpinner.setAdapter(adapter);

		messageType.setOnItemSelectedListener(new MyOnItemSelectedListener());
		freqSpinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
		contact.setOnClickListener(this);
		dp = (DatePicker) this.findViewById(R.id.datePicker);
		tp = (TimePicker) this.findViewById(R.id.timePicker);
	}

	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.nextConfig:

			if (messageType.getSelectedItemPosition() == 0) { // If it's an
																// unnamed
																// contact,
																// we're going
																// to determine
																// whether it's
																// an email
																// address or
																// phone number.
				noContactName = true;

				// If it's an phone number.
				if (contact
						.getText()
						.toString()
						.matches("^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$")) {
//					isEmail = false;
				}
				/*
				 * //If it's an email address else
				 * if(contact.getText().toString().matches(
				 * "[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})"
				 * )){ isEmail = true; }
				 */
				else {
					Toast.makeText(this, "Please enter a valid phone number",
							Toast.LENGTH_LONG).show();
					break;
				}
			}

			else { // If we do have a contact name.
				noContactName = false;
			}

			Intent i = new Intent(this, MessageText.class);
			Bundle b = new Bundle();
			
			//Set the total number of reminders
			if (!numberOfReminders.getText().toString().equals("")&&Integer.parseInt(numberOfReminders.getText().toString())>0) {
				numReminders = Integer.parseInt(numberOfReminders.getText().toString());
			} else {
				Toast.makeText(this, "Please enter a valid number of reminders",
						Toast.LENGTH_LONG).show();
				break;
			}

			// Gets the reminder frequency, unless it's null, in which case we
			// show an error.
			if (!freq.getText().toString().equals("")) {
				frequency = Integer.parseInt(freq.getText().toString());
			} else {
				Toast.makeText(this, "Please enter a valid reminder frequency",
						Toast.LENGTH_LONG).show();
				break;
			}

			// Gregorian calendar is an object that stores a datetime, it allows
			// us to convert a datetime to UTC
			GregorianCalendar StartDateCalendar = new GregorianCalendar(
					dp.getYear(), dp.getMonth(), dp.getDayOfMonth(),
					tp.getCurrentHour(), tp.getCurrentMinute());
			long utc = StartDateCalendar.getTimeInMillis();

			long totalFrequency;
			if (howOften == 0) {
				totalFrequency = frequency * MINUTE_IN_MILLIS;
			} else if (howOften == 1) {
				totalFrequency = frequency * HOUR_IN_MILLIS;
			} else if (howOften == 2) {
				totalFrequency = frequency * DAY_IN_MILLIS;
			} else {
				totalFrequency = frequency * WEEK_IN_MILLIS;
			}
//			b.putBoolean(SavedDataOpenHelper.TYPE_OF_REMINDER, isEmail);
			b.putLong(SavedDataOpenHelper.FREQUENCY_OF_REMINDER, totalFrequency);
			b.putLong(SavedDataOpenHelper.NEXT_DATE_TIME, utc);
			b.putInt(SavedDataOpenHelper.NUMBER_LEFT, numReminders);

			b.putString(SavedDataOpenHelper.CONTACT_NAME, contact.getText()
					.toString()); // This is whatever is in the box.

			if (noContactName) {

				b.putString(SavedDataOpenHelper.PHONE_NUMBER, contact.getText()
						.toString());

			}

			// If a contact was selected, we input that contact's phone number.
			// Later we'll change this to account for emailing contact as well.
			else if (!noContactName) {
				b.putString("PHONE_NUMBER", contactValue);
			}

			i.putExtras(b);
			this.startActivity(i);
			break;
		case R.id.cancelConfig:
			finish();
			break;
		case R.id.contactinfo:
			if (!noContactName) {

			}
			break;
		}

	}

	public class MyOnItemSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			switch (parent.getId()) {
			case R.id.fr:

				if (pos == 0) {
					howOften = 0;
				} else if (pos == 1) {
					howOften = 1;
				} else if (pos == 2) {
					howOften = 2;
				} else {
					howOften = 3;
				}
			case R.id.type_spinner:
				if (pos == 1) {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setType(Contacts.Phones.CONTENT_ITEM_TYPE);
					startActivityForResult(intent, 1);
					noContactName = false;

				} else {
					noContactName = true;

				}
				contact.setVisibility(EditText.VISIBLE);
				break;
			}
		}

		public void onNothingSelected(AdapterView parent) {
			// Do nothing.
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			Uri uri = data.getData();

			if (uri != null) {
				Cursor c = null;
				try {
					c = getContentResolver()
							.query(uri,
									new String[] { Contacts.Phones.NUMBER,
											Contacts.Phones.TYPE,
											Contacts.Phones.NAME }, null, null,
									null);

					if (c != null && c.moveToFirst()) {
						// Gets the name
						contactName = c.getString(2);
						contact.setText(contactName);
						// Gets the number
						contactValue = c.getString(0);
					}

				} finally {
					if (c != null) {
						c.close();
					}
				}
			}
		}
	}
}
