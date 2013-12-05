package edu.yu.biengoldmold.collect;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SavedDataOpenHelper extends SQLiteOpenHelper {
	
	//All the names of columns are static final strings
	 final static String REMINDER_ID="REMINDER_ID";
	 final static String TYPE_OF_REMINDER="TYPE_OF_REMINDER";
	 final static String FREQUENCY_OF_REMINDER="FREQUENCY_OF_REMINDER";
	 final static String CONTACT_NAME="CONTACT_NAME";
	 final static String PHONE_NUMBER="PHONE_NUMBER";
	 final static String NUMBER_LEFT="NUMBER_LEFT";
	 final static String MESSAGE_CONTENT="MESSAGE_CONTENT";
	 final static String NEXT_DATE_TIME="NEXT_DATE_TIME";

	
	private static final String DATABASE_NAME = "Reminder_Database";
	private static final int DATABASE_VERSION = 8;
	public static final String REMINDER_TABLE_NAME = "Reminders";
	
	//This string creates the database using an SQL "CREATE TABLE" command
	private static final String REMINDER_TABLE_CREATE = "CREATE TABLE "
			+ REMINDER_TABLE_NAME + " (" + 
			REMINDER_ID + " INTEGER NOT NULL, " 
			+ TYPE_OF_REMINDER + " BOOLEAN, "
			+ CONTACT_NAME + " NVARCHAR(50), " 
			+ FREQUENCY_OF_REMINDER + " BIGINT, "
			+ NEXT_DATE_TIME + " BIGINT, " 
			+ PHONE_NUMBER + " NVARCHAR(22), " 
			+ NUMBER_LEFT + " INTEGER, " 
			+ MESSAGE_CONTENT + " NVARCHAR(500), " 
			+ "PRIMARY KEY(REMINDER_ID) "			
			+ ");";

	SavedDataOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(REMINDER_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
