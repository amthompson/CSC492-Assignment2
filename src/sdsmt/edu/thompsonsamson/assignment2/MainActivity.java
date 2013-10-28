/**
* Project Assignment2 Address Book - Main Activity
*/
package sdsmt.edu.thompsonsamson.assignment2;

import java.util.List;

import sdsmt.edu.thompsonsamson.assignment2.Model.Contact;
import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.widget.ArrayAdapter;

/**
 * <p>
 * Android application to create a list of contacts. The contacts are stored in
 * a SQLite database and controlled by using fragments created by the host
 * activity. A user can add, edit and remove contacts.
 * </p>
 * <p>
 * Main entry to the application. The list will be empty when first opened.
 * Clicking the Add Contact button will bring the user to an empty contact
 * where they can fill out the text fields. When the contact is saved, the
 * view returns to the contact list. When a contact is clicked upon, it will
 * display a view with the details. Here, the user can select to edit or
 * delete the contact. If deleted, they will be returned to the contact list.
 * If edit is selected, the text boxes will be activated and the contact
 * information changed.
 * <p>
 * <a href="https://github.com/amthompson/CSC421-Assignment2">GitHub Repository</a>
 * <p>
 * Timeline:
 * <ul>
 *		<li>10/19/2013:	Initial project creation and repo (AT)</li>
 * 		<li>10/20/2013: Coding session (database/fragments) (AT&SS)</li>
 * 		<li>10/21/2013: Fragment detail work (AT)</li>
 * 		<li>10/23/2013: Fixed orientation bug (AT)</li>
 * 		<li>10/27/2013:	Coding Session - Fixed onResume bug, added alerts, 
 * 						testing and documentation (AT&SS)</li>
 * 		<li>10/28/2013:  Finalize comments and host docs and code</li>
 * </ul>
 * @author Andrew Thompson
 * @author Scott Samson
 */
public class MainActivity extends Activity implements IContactControlListener
{
	// constant names for fragments used
	private final static String FRAGMENT_LIST_TAG = "ContactListTag";
	private final static String FRAGMENT_DETAIL_TAG = "ContatViewTag";

	private FragmentManager _fragmentManager;	// fragment manager object
	private ViewListFragment _fragmentList;		// list view fragment
	private ViewDetailFragment _fragmentDetail;	// detail view fragment
	
	private Model _model;						// data model
	private Contact _contact;					// current contact object
	private List<Contact> _contacts;			// list of contacts
	private ArrayAdapter<Contact> _adapter;		// adapter to control list
	
	/**
	 * Main starting point of application. Creates the main activity by setting
	 * up the fragment manager and object and refreshes the contact list via the
	 * array adapter.
	 * @param savedInstanceState saved instance data
	 * @see #refreshArrayAdapter()
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// initialize fragment manager
		_fragmentManager = getFragmentManager();
		
		// get fragment list
		_fragmentList = (ViewListFragment) _fragmentManager.findFragmentByTag(FRAGMENT_LIST_TAG);
		if( _fragmentList == null )
		{
			_fragmentList = new ViewListFragment();
		}
		
		// get fragment detail
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

	/**
	 * Sets the current contact object and gets the detail fragment
	 * @param contact the current contact object
	 * @see #showDetailFragment()
	 */
	@Override
	public void selectContact(Contact contact) 
	{
		_contact = contact;
		showDetailFragment();
	}

	/**
	 * Updates the current contact in the list via the array adapter and
	 * the underlying data model. The fragment view is then popped off
	 * the back stack and returns to the contact list.
	 * @param contact the current contact object
	 */
	@Override
	public void updateContact(Contact contact) 
	{	
		// update the array adapter
		_adapter.remove(contact);
		_adapter.add(contact);
		_adapter.sort(contact);
		_adapter.notifyDataSetChanged();
		
		// update the data model
		_model.updateContact(contact);
		
		// return to contact list
		_fragmentManager.popBackStackImmediate();
	}

	/**
	 * Generates a new, blank contact and shows the detail page. The user
	 * can then edit the information and save it.
	 * @see #showDetailFragment()
	 */
	@Override
	public void insertContact() 
	{	
		// instantiate a new empty course object
		_contact = new Contact();
		
		// display the detail fragment
		showDetailFragment();
	}

	/**
	 * Inserts the current contact into the list via the array adapter
	 * and the underlying data model.  The fragment view is then popped off
	 * the back stack and returns to the contact list.
	 * @param contact the current contact object
	 */
	@Override
	public void insertContact(Contact contact) 
	{	
		// update the array adapter
		_adapter.add(contact);
		_adapter.sort(contact);
		_adapter.notifyDataSetChanged();

		// update the data model
		_model.insertContact(contact);
		
		// return to the contact list
		_fragmentManager.popBackStackImmediate();
	}

	/**
	 * Deletes the current contact from the list via the array adapter
	 * and the underlying data model. The fragment view is then popped off
	 * the back stack and returns to the contact list.
	 * @param contact the current contact object
	 */
	@Override
	public void deleteContact(Contact contact) 
	{	
		// update the array adapter
		_adapter.remove(contact);
		_adapter.sort(contact);
		_adapter.notifyDataSetChanged();

		// update the data model
		_model.deleteContact(contact);

		// return to the contact list
		_fragmentManager.popBackStackImmediate();	
	}

	/**
	 * Returns the current contact
	 */
	@Override
	public Contact getContact() 
	{
		return _contact;
	}
	
	/**
	 * Returns the current array adapter
	 */
	@Override
	public ArrayAdapter<Contact> getContactArrayAdapter() 
	{
		return _adapter;
	}

	/**
	 * Gets a list of contacts from the data model and refreshes the array
	 * adapter.
	 */
	private void refreshArrayAdapter() 
	{	
		// get an array of the contact objects
		_contacts = Model.getInstance(this).getContacts();

		// display the contacts in the list adapter
		_adapter = new ArrayAdapter<Contact>(this, android.R.layout.simple_list_item_1, _contacts);	
	}

	/**
	 * Shows the contact detail fragment in the main activity
	 */
	public void showDetailFragment() 
	{	
		_fragmentManager.beginTransaction()
						.replace(R.id.fragmentContainerFrame, _fragmentDetail, FRAGMENT_DETAIL_TAG)
						.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
						.addToBackStack(null)
						.commit();
	}

	/**
	 * Sets contact information on state restore.
	 * @param savedInstanceState application bundle for storing data
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) 
	{
		super.onRestoreInstanceState(savedInstanceState);
		
		_contact = new Contact();
		_contact.ID = savedInstanceState.getLong("CONTACT_ID");
		_contact.Name = savedInstanceState.getString("CONTACT_NAME");
		_contact.Phone = savedInstanceState.getString("CONTACT_PHONE");
		_contact.Email = savedInstanceState.getString("CONTACT_EMAIL");
		_contact.Address = savedInstanceState.getString("CONTACT_ADDRESS");
		_contact.City = savedInstanceState.getString("CONTACT_CITY");
	}

	/**
	 * If contact isn't null, save the current information to the bundle.
	 * @param outState application bundle for storing data
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) 
	{
		super.onSaveInstanceState(outState);
		
		if( _contact != null ) 
		{
			outState.putLong("CONTACT_ID", _contact.ID);
			outState.putString("CONTACT_NAME", _contact.Name);
			outState.putString("CONTACT_PHONE", _contact.Phone);
			outState.putString("CONTACT_EMAIL", _contact.Email);
			outState.putString("CONTACT_ADDRESS", _contact.Address);
			outState.putString("CONTACT_CITY", _contact.City);
		}
	}
}