package sdsmt.edu.thompsonsamson.assignment2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Model extends SQLiteOpenHelper {

	// database setup
	private static final String TAG = "Assignment2.Database";
    private static final String DATABASE_NAME = "AddressBook.Database.db";
    private static final int DATABASE_VERSION = 1;
    
    // database fields
    private static final String KEY_ID = "ContactID";
    private static final String KEY_NAME = "Name";
    private static final String KEY_PHONE = "Phone";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_ADDRESS = "Address";
    private static final String KEY_CITY = "City";   
    
    // database table    
    private static final String TABLE_CONTACTS = "Contacts";
    
    private static final String TABLE_CREATE_CONTACTS = 
    		"CREATE TABLE " +
    				TABLE_CONTACTS +
    				"(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
    				KEY_NAME + " TEXT, " +
    				KEY_PHONE + " TEXT, " +
    				KEY_EMAIL + " TEXT, " +
    				KEY_ADDRESS + " TEXT, " +
    				KEY_CITY + " TEXT)";
    
    private SQLiteDatabase _db;			// database
    private static Model _instance;		// data model
    
    // inner class
    public static class Contact implements Comparator<Contact>
    {

    	public long ID;
    	public String Name;
    	public String Phone;
    	public String Email;
    	public String Address;
    	public String City;
    	
    	public Contact()
    	{
    		ID = -1;
    		Name = "";
    		Phone = "";
    		Email = "";
    		Address = "";
    		City = "";
    	}
    	
    	public Contact( long id)
    	{
    		ID = id;
    	}
    	
    	@Override
    	public String toString()
    	{
    		return Name;
    	}
    	
		@Override
		public int compare(Contact lhs, Contact rhs) {
			return lhs.Name.compareTo(rhs.Name);
		}
    	
    }
	public Model(Context context) {		
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE_CONTACTS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		if( newVersion == 2)
		{
			// no version 2 upgrade process yet
		}
	}
	
	public static synchronized Model getInstance(Context context)
	{
		if(_instance == null)
		{
			_instance = new Model(context);
		}
		
		return _instance;
	}
	
	public void insertContact(Contact contact)
	{
		ContentValues values = populateContentValues(contact);
		
		// open database
		openDbConnection();
		
		// insert the record
		long id = _db.insertOrThrow(TABLE_CONTACTS, null, values);
		
		// close database
		closeDbConnection();

		// check the id to make sure it was inserted
		if( id > 0 ) {
			
			// update the contact with new id
			contact.ID = id;

			Log.d(TAG, String.format("Contact inserted: %s (%d)", contact.Name, id));
		}
		else {
			Log.d(TAG, String.format("Contact NOT inserted: %s (%d)", contact.Name, contact.ID));
		}
	}
	
	public void updateContact(Contact contact)
	{

		ContentValues values = populateContentValues(contact);
		
		// open database
		openDbConnection();
		
		// insert the record
		int rowsAffected = _db.update(TABLE_CONTACTS, values, KEY_ID + " = ?", new String[] { String.valueOf(contact.ID) });
				
		// close database
		closeDbConnection();
		
		if( rowsAffected == 0 ) {
			Log.d(TAG, String.format("Contact NOT updated: %s (%d)", contact.Name, contact.ID));
		}
		else {
			Log.d(TAG, String.format("Contact updated: %s (%d)", contact.Name, contact.ID));
		}
		
	}
	
	public void deleteContact(Contact contact)
	{
		// open database
		openDbConnection();

		int rowsAffected = _db.delete(TABLE_CONTACTS, KEY_ID + " = ?", 
									  new String[] {String.valueOf(contact.ID)});
		
		// close database
		closeDbConnection();

		if( rowsAffected == 0 ) {
			Log.d(TAG, String.format("Contact NOT deleted: %s (%d)", contact.Name, contact.ID));
		}
		else {
			Log.d(TAG, String.format("Contact deleted: %s (%d)", contact.Name, contact.ID));
		}
	}
	
	public Contact getContact(long id)
	{
		Contact contact = null;

		// open database
		openDbConnection();
		
		Cursor cursor = _db.query(TABLE_CONTACTS, 
								  new String[] { KEY_ID, KEY_NAME, KEY_PHONE, KEY_EMAIL, KEY_ADDRESS, KEY_CITY },
								  KEY_ID + " = " + id, null, null, null,
								  KEY_NAME);

		if( cursor.moveToFirst() )
		{
			contact = cursorToContact(cursor);
			
			Log.d(TAG, String.format("Contact retrieved: %s (%d)", contact.Name, contact.ID));
		}
		
		// close database cursor
		cursor.close();
		
		// close database
		closeDbConnection();
		
		return contact;
	}

	public List<Contact> getContacts()
	{
		List<Contact> contacts = new ArrayList<Contact>();

		// open database
		openDbConnection();
		
		Cursor cursor = _db.query(TABLE_CONTACTS, 
				  new String[] { KEY_ID, KEY_NAME, KEY_PHONE, KEY_EMAIL, KEY_ADDRESS, KEY_CITY  },
				  null, null, null, null,
				  KEY_NAME
				 );
		
		// move to first contact object
		cursor.moveToFirst();
		
		// loop through database getting contact list
		while( cursor.isAfterLast() == false)
		{
			Contact contact = cursorToContact(cursor);
			contacts.add(contact);
			cursor.moveToNext();
		}

		// close the database cursor
		cursor.close();
		
		// close database
		closeDbConnection();
		
		return contacts;
	}
	
	private void openDbConnection()
	{
		_db = getWritableDatabase();
	}
	
	private void closeDbConnection()
	{
		if( _db != null && _db.isOpen() )
		{
			_db.close();
		}
	}
	
	private Contact cursorToContact(Cursor cursor)
	{
		Contact contact = new Contact(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
		
		contact.ID = cursor.getLong(cursor.getColumnIndex(KEY_ID));
		contact.Name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
		contact.Phone = cursor.getString(cursor.getColumnIndex(KEY_PHONE));
		contact.Email = cursor.getString(cursor.getColumnIndex(KEY_EMAIL));
		contact.Address = cursor.getString(cursor.getColumnIndex(KEY_ADDRESS));
		contact.City = cursor.getString(cursor.getColumnIndex(KEY_CITY));
		
		return contact;
	}
	
	private ContentValues populateContentValues(Contact contact)
	{
		ContentValues values = new ContentValues();
				
		values.put(KEY_NAME, contact.Name);
		values.put(KEY_PHONE, contact.Phone);
		values.put(KEY_EMAIL, contact.Email);
		values.put(KEY_ADDRESS, contact.Address);
		values.put(KEY_CITY, contact.City);
		
		return values;
	}
}


