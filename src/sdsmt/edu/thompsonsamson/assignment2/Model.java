package sdsmt.edu.thompsonsamson.assignment2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Model extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AddressBook.Database.db";
    private static final int DATABASE_VERSION = 1;
	
    
	public Model(Context context) {		
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
