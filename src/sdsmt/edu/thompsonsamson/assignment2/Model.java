/**
 * Project Assignment2 Address Book - Model
 */
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

/**
 * Class defining the data model used in this application. Creates the
 * contact table and handles all of the creation, reading, updating and
 * deleting of address book contacts. Logging functions were retained,
 * but in an actual application, exceptions should be thrown and handled
 * if there were problems with the database.
 */
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
    
    // create table sql statement
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
    
    /**
     * Inner class for the contact object. Contains all the information
     * for a contact including: unique identifier, name, phone number,
     * email address, street address and city/state fields.
     */
    public static class Contact implements Comparator<Contact>
    {
    	public long ID;			// identifier for the contact
    	public String Name;		// name of the contact
    	public String Phone;	// phone number of the contact
    	public String Email;	// email address for the contact
    	public String Address;	// address for the contact
    	public String City;		// city and state for the contact
    	
    	/**
    	 * Constructor for the contact object. Sets the ID to -1
    	 * and the rest of the fields as empty.
    	 */
    	public Contact()
    	{
    		ID = -1;
    		Name = "";
    		Phone = "";
    		Email = "";
    		Address = "";
    		City = "";
    	}
    	
    	/**
    	 * Constructor for the contact object that passes the
    	 * ID of an existing contact.
    	 * @param id
    	 */
    	public Contact( long id)
    	{
    		ID = id;
    	}
    	
    	/**
    	 * Returns a string that is the contacts name. This is displayed
    	 * in the list view.
    	 * @return String name of the contact
    	 */
    	@Override
    	public String toString()
    	{
    		return Name;
    	}
    	
    	/**
    	 * Comparator for checking two contact objects by the contact name
    	 * @param lhs first contact to compare
    	 * @param rhs second contact to compare
    	 * @return int value to determine which object is greater or less than
    	 */
		@Override
		public int compare(Contact lhs, Contact rhs) {
			return lhs.Name.compareTo(rhs.Name);
		}
    	
    }
    
    /**
     * Constructor for the database model
     * @param context
     */
	public Model(Context context) 
	{		
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * Creates the contact table in the SQLite database
	 * @param db the current database 
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE_CONTACTS);
	}

	/**
	 * Upgrades the current database to a newer version. Not implemented.
	 * @param db the current database
	 * @param oldVersion old version of the database
	 * @param newVersion new version of the database
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		if( newVersion == 2)
		{
			// no version 2 upgrade process yet
		}
	}
	
	/**
	 * Gets the new instance of the database. Synchronized to avoid thread
	 * interference.
	 * @param context current information about the application environment
	 * @return model of the current database
	 */
	public static synchronized Model getInstance(Context context)
	{
		if(_instance == null)
		{
			_instance = new Model(context);
		}
		
		return _instance;
	}
	
	/**
	 * Inserts the current contact into the database.
	 * @param contact the current contact object
	 * @see #populateContentValues(Contact)
	 */
	public void insertContact(Contact contact)
	{
		// create object to store contact values
		ContentValues values = populateContentValues(contact);
		
		// open database
		openDbConnection();
		
		// insert the record
		long id = _db.insertOrThrow(TABLE_CONTACTS, null, values);
		
		// close database
		closeDbConnection();

		// check the id to make sure it was inserted and log results
		if( id > 0 ) 
		{	
			// update the contact with new id
			contact.ID = id;

			Log.d(TAG, String.format("Contact inserted: %s (%d)", contact.Name, contact.ID));
		}
		else 
		{
			Log.d(TAG, String.format("Contact NOT inserted: %s (%d)", contact.Name, contact.ID));
		}
	}
	
	/**
	 * Updates the current contact in the database
	 * @param contact the current contact object
	 * @see #populateContentValues(Contact)
	 */
	public void updateContact(Contact contact)
	{
		// create object to store contact values
		ContentValues values = populateContentValues(contact);
		
		// open database
		openDbConnection();
		
		// insert the record
		int rowsAffected = _db.update(TABLE_CONTACTS, values, KEY_ID + " = ?", new String[] { String.valueOf(contact.ID) });
				
		// close database
		closeDbConnection();
		
		// log the results
		if( rowsAffected == 0 ) {
			Log.d(TAG, String.format("Contact NOT updated: %s (%d)", contact.Name, contact.ID));
		}
		else {
			Log.d(TAG, String.format("Contact updated: %s (%d)", contact.Name, contact.ID));
		}
		
	}
	
	/**
	 * Deletes the contact from the database
	 * @param contact the current contact object
	 */
	public void deleteContact(Contact contact)
	{
		// open database
		openDbConnection();
		
		// delete the contact
		int rowsAffected = _db.delete(TABLE_CONTACTS, KEY_ID + " = ?", 
									  new String[] {String.valueOf(contact.ID)});
		
		// close database
		closeDbConnection();

		// log the results
		if( rowsAffected == 0 ) 
		{
			Log.d(TAG, String.format("Contact NOT deleted: %s (%d)", contact.Name, contact.ID));
		}
		else 
		{
			Log.d(TAG, String.format("Contact deleted: %s (%d)", contact.Name, contact.ID));
		}
	}
	
	/**
	 * Gets a contact from the database based on the id. Uses a cursor to find
	 * the contact and return the information.
	 * @param id id of the contact to fetch
	 * @return Contact object
	 */
	public Contact getContact(long id)
	{
		Contact contact = null;

		// open database
		openDbConnection();
		
		// search for the contact by id
		Cursor cursor = _db.query(TABLE_CONTACTS, 
								  new String[] { KEY_ID, KEY_NAME, KEY_PHONE, KEY_EMAIL, KEY_ADDRESS, KEY_CITY },
								  KEY_ID + " = " + id, null, null, null,
								  KEY_NAME);

		// 
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

	/**
	 * Gets the list of current contacts in the database ordered by name
	 * @return List of contact objects
	 */
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
	
	/**
	 * Opens a connection to the current database
	 */
	private void openDbConnection()
	{
		_db = getWritableDatabase();
	}
	
	/**
	 * Closes the current database connection if exists and is open
	 */
	private void closeDbConnection()
	{
		if( _db != null && _db.isOpen() )
		{
			_db.close();
		}
	}
	
	/**
	 * Sets contact data from the database cursor to a contact object and returns it.
	 * @param cursor the position/row in the database
	 * @return Contact object for the database information
	 */
	private Contact cursorToContact(Cursor cursor)
	{
		// set the contact with the id of the record at the cursor
		Contact contact = new Contact(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
		
		contact.ID = cursor.getLong(cursor.getColumnIndex(KEY_ID));
		contact.Name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
		contact.Phone = cursor.getString(cursor.getColumnIndex(KEY_PHONE));
		contact.Email = cursor.getString(cursor.getColumnIndex(KEY_EMAIL));
		contact.Address = cursor.getString(cursor.getColumnIndex(KEY_ADDRESS));
		contact.City = cursor.getString(cursor.getColumnIndex(KEY_CITY));
		
		return contact;
	}
	
	/**
	 * Puts contact values into a ContentValues object in key/value pairs.
	 * @param contact the current contact object
	 * @return ContentValues object used to store contact values 
	 */
	private ContentValues populateContentValues(Contact contact)
	{
		// object to hold key/value pairs of contact data
		ContentValues values = new ContentValues();
		
		// insert current contact information and return it
		values.put(KEY_NAME, contact.Name);
		values.put(KEY_PHONE, contact.Phone);
		values.put(KEY_EMAIL, contact.Email);
		values.put(KEY_ADDRESS, contact.Address);
		values.put(KEY_CITY, contact.City);
		
		return values;
	}
}


