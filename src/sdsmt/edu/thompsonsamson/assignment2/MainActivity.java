package sdsmt.edu.thompsonsamson.assignment2;

import java.util.List;

import sdsmt.edu.thompsonsamson.assignment2.Model.Contact;
import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.view.Menu;
import android.widget.ArrayAdapter;

public class MainActivity extends Activity implements IContactControlListener {

	private Model _model;
	private Contact _contact;
	private List<Contact> _contacts;
	private ArrayAdapter<Contact> _adapter;
	
	private FragmentManager _fragementManager;
	private ViewListFragment _fragmentList;
	private ViewDetailFragment _fragmentDetail;
	
	private final static String FRAGMENT_LIST_TAG = "ContactListTag";
	private final static String FRAGMENT_DETAIL_TAG = "ContatViewTag";
	
	@Override
	public void selectContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertContact() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Contact getContact() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayAdapter<Contact> getContactArrayAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.menu_list, menu);
		
		return true;
	}

}
