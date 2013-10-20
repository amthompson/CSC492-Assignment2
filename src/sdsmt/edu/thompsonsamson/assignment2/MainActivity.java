package sdsmt.edu.thompsonsamson.assignment2;

import java.util.List;

import sdsmt.edu.thompsonsamson.assignment2.Model.Contact;
import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MainActivity extends Activity implements IContactControlListener {

	private Model _model;
	private Contact _contact;
	private List<Contact> _contacts;
	private ArrayAdapter<Contact> _adapter;
	
	private FragmentManager _fragmentManager;
	private ViewListFragment _fragmentList;
	private ViewDetailFragment _fragmentDetail;
	
	private final static String FRAGMENT_LIST_TAG = "ContactListTag";
	private final static String FRAGMENT_DETAIL_TAG = "ContatViewTag";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		_fragmentManager = getFragmentManager();
		
		// get fragment list
		_fragmentList = (ViewListFragment) _fragmentManager.findFragmentByTag(FRAGMENT_LIST_TAG);
		if( _fragmentList == null )
		{
			_fragmentList = new ViewListFragment();
		}
		
		// get fragment view
		_fragmentDetail = (ViewDetailFragment) _fragmentManager.findFragmentByTag(FRAGMENT_DETAIL_TAG);
		if( _fragmentDetail == null )
		{
			_fragmentDetail = new ViewDetailFragment();
		}
		
		// check fragment view
		if( savedInstanceState == null )
		{
			_fragmentManager.beginTransaction()
							.replace(R.id.fragmentContainerFrame, _fragmentList, FRAGMENT_LIST_TAG)
							.commit();
		}
		
		// get database model
		_model = Model.getInstance(this);
		
		// refresh the list of contacts
		refreshArrayAdapter();
		
	}

	@Override
	public void selectContact(Contact contact) {
		
		_contact = contact;
		showDetailFragment();
		
	}

	@Override
	public void updateContact(Contact contact) {
		
		_adapter.remove(contact);
		_adapter.add(contact);
		_adapter.sort(contact);
		_adapter.notifyDataSetChanged();
		
		_model.updateContact(contact);
		_fragmentManager.popBackStackImmediate();
		
	}

	@Override
	public void insertContact() {
		
		// instantiate a new empty course object
		_contact = new Contact();
		
		// display the detail fragment
		showDetailFragment();
	}

	@Override
	public void insertContact(Contact contact) {
		
		_adapter.add(contact);
		_adapter.sort(contact);
		_adapter.notifyDataSetChanged();
		
		_model.insertContact(contact);
		_fragmentManager.popBackStackImmediate();
		
	}

	@Override
	public void deleteContact(Contact contact) {
		
		_adapter.remove(contact);
		_adapter.sort(contact);
		_adapter.notifyDataSetChanged();
		
		_model.deleteContact(contact);
		_fragmentManager.popBackStackImmediate();
		
	}

	@Override
	public Contact getContact() {
		return _contact;
	}

	@Override
	public ArrayAdapter<Contact> getContactArrayAdapter() {		
		return _adapter;
	}

	private void refreshArrayAdapter() {
		
		// get an array of the contact objects
		_contacts = Model.getInstance(this).getContacts();

		// display the contacts in the list adapter
		_adapter = new ArrayAdapter<Contact>(this, android.R.layout.simple_list_item_1, _contacts);
		
	}

	public void showDetailFragment()
	{
		_fragmentManager.beginTransaction()
						.replace(R.id.fragmentContainerFrame, _fragmentDetail, FRAGMENT_DETAIL_TAG)
						.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
						.addToBackStack(null)
						.commit();
	}
	
}
