package sdsmt.edu.thompsonsamson.assignment2;

import java.util.Comparator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Model extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AddressBook.Database.db";
    private static final int DATABASE_VERSION = 1;
	
    public static class Contact implements Comparator<Contact>
    {

    	public Integer Id;
    	public String Name;
    	public String Phone;
    	public String Email;
    	public String Street;
    	public String City;
    	
    	public Contact()
    	{
    		Id = -1;
    		    		
    	}
    	
    	public Contact( Integer id)
    	{
    		Id = id;
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
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}


