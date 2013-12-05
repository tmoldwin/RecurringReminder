package edu.yu.biengoldmold.collect;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class ReminderDBAdapter {
	private SavedDataOpenHelper sdh;
	private SQLiteDatabase db;

	public ReminderDBAdapter(Context c) {
		sdh = new SavedDataOpenHelper(c);
	}

	public void open() {
		try {
			db = sdh.getWritableDatabase();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		db.close();
	}

	// Insert a ContentValue into a database;
	public long insert(ContentValues cv) {
		open();
		long l = db.insert(SavedDataOpenHelper.REMINDER_TABLE_NAME, "null", cv);
		close();
		return l;
	}

	// Deletes a row with the specified row ID (tested)
	public int delete(int rowID) {
		open();
		String deleteString = "" + SavedDataOpenHelper.REMINDER_ID + "="
				+ rowID;
		int i = db.delete(SavedDataOpenHelper.REMINDER_TABLE_NAME,
				deleteString, null);
		close();
		return i;
	}

	// Returns a Cursor with all the reminders (not sure if this works);
	public Cursor getRemindersCursor() {
		open();
		Cursor c = db.query(SavedDataOpenHelper.REMINDER_TABLE_NAME, null,
				null, null, null, null, null);
		close();
		return c;
	}

	/**
	 * @return an ArrayList of HashMaps (one for each row) with <Column_Name,
	 *         Column_Value> as the data pair.
	 */
	public ArrayList<HashMap<String, String>> getAllDataFromAllReminders() {
		ArrayList<HashMap<String, String>> al = new ArrayList<HashMap<String, String>>();
		open();
		Cursor c = db.query(SavedDataOpenHelper.REMINDER_TABLE_NAME, null,
				null, null, null, null, null);
		for (int row = 0; row < c.getCount(); row++) {
			HashMap<String, String> currentRow = new HashMap<String, String>();
			c.moveToPosition(row);
			for (int i1 = 0; i1 < c.getColumnCount(); i1++) {
				currentRow.put(c.getColumnName(i1), c.getString(i1));
			}
			al.add(currentRow);
		}
		c.close();
		close();
		return al;
	}

	// Updates the reminder specified by reminder_ID with the values stored in
	// cv
	public int update(int reminder_ID, ContentValues cv) {
		open();
		String whereClause = "" + SavedDataOpenHelper.REMINDER_ID + "="
				+ reminder_ID;
		int i = db.update(SavedDataOpenHelper.REMINDER_TABLE_NAME, cv,
				whereClause, null);
		close();
		return i;
	}

	public boolean existsRow(int rowId) {
		if (dBsize() == 0) return false;
		open();
		String selection = "" + SavedDataOpenHelper.REMINDER_ID + "=" + rowId;
		Cursor c = db.query(SavedDataOpenHelper.REMINDER_TABLE_NAME, null,
				selection, null, null, null, null);
		boolean b=c.moveToFirst();
		// If there is nothing in the cursor, exists is false.
		c.close();
		close();
		return b;

	}

	public HashMap<String, String> getRow(int i) {
		open();
		HashMap<String, String> hm = new HashMap<String, String>();
		String selection = "" + SavedDataOpenHelper.REMINDER_ID + "=" + i;
		Cursor c = db.query(SavedDataOpenHelper.REMINDER_TABLE_NAME, null,
				selection, null, null, null, null);
		c.moveToFirst();
		for (int i1 = 0; i1 < c.getColumnCount(); i1++) {
			hm.put(c.getColumnName(i1), c.getString(i1));
		}
		c.close();
		close();
		return hm;
	}

	// Returns a string representation of the given row, mainly for debugging.
	public String rowString(int i) {
		String s = "";
		HashMap<String, String> hm = getRow(i);
		for (String key : hm.keySet()) {
			s += key + ":" + hm.get(key) + "\n";
		}
		return s;
	}

	// Returns an ArrayList of IDs for the rows whose Next_Reminder_Time have
	// passed.
	public ArrayList<Integer> getCurrentReminders() {
		long currentTime;
		GregorianCalendar gc = new GregorianCalendar();
		currentTime = gc.getTimeInMillis();
		ArrayList<HashMap<String, String>> al = getAllDataFromAllReminders();
		ArrayList<Integer> reminderRows = new ArrayList<Integer>();
		for (HashMap<String, String> hm : al) {
			if (Long.parseLong(hm.get(SavedDataOpenHelper.NEXT_DATE_TIME)) <= currentTime) {
				reminderRows.add(Integer.parseInt(hm
						.get(SavedDataOpenHelper.REMINDER_ID)));
			}

		}
		return reminderRows;
	}

	// Increments the NextTime field by the reminderFrequency
	public void updateNextTime(int rowID) {
		if (Long.parseLong(getRow(rowID).get(
				SavedDataOpenHelper.FREQUENCY_OF_REMINDER)) == 0) {
			delete(rowID);
		} else {
			GregorianCalendar currentTime = new GregorianCalendar();
			Long increment = Long.parseLong(getRow(rowID).get(
					SavedDataOpenHelper.FREQUENCY_OF_REMINDER));
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTimeInMillis(Long.parseLong(getRow(rowID).get(
					SavedDataOpenHelper.NEXT_DATE_TIME)));
			// while (gc.getTimeInMillis() < currentTime.getTimeInMillis()) {
			// gc.setTimeInMillis(currentTime.getTimeInMillis()+currentTime.getTimeInMillis()%increment);
			gc.setTimeInMillis(currentTime.getTimeInMillis() + increment);
			// }
			long newTime = gc.getTimeInMillis();
			ContentValues cv = new ContentValues();
			cv.put(SavedDataOpenHelper.NEXT_DATE_TIME, newTime);
			update(rowID, cv);
		}
	}

	public void updateNumberLeft(int rowID) {
		int numleft = Integer.parseInt(getRow(rowID).get(
				SavedDataOpenHelper.NUMBER_LEFT));
		numleft--;
		if (numleft <= 0) {
			delete(rowID);
		} else {
			ContentValues cv = new ContentValues();
			cv.put(SavedDataOpenHelper.NUMBER_LEFT, numleft);
			update(rowID, cv);
		}
	}


	// Still not sure if this works
	public boolean verifyPhoneNumer(int rowID, String telephoneNumber) {
		if (existsRow(rowID)) {
			HashMap<String, String> hm = getRow(rowID);
			if (hm.get(SavedDataOpenHelper.REMINDER_ID) != null) {
				return hm.get(SavedDataOpenHelper.PHONE_NUMBER).equals(
						telephoneNumber);
			}
		}
		return false;
	}

	// Returns the number of rows in the database(Tested)
	public int dBsize() {
		open();
		Cursor c = db.query(SavedDataOpenHelper.REMINDER_TABLE_NAME, null,
				null, null, null, null, null);
		int i = c.getCount();
		c.close();
		close();
		return i;
	}

}
