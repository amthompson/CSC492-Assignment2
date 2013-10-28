/**
* Project Assignment2 Address Book - List Fragment
*/
package sdsmt.edu.thompsonsamson.assignment2;

import sdsmt.edu.thompsonsamson.assignment2.Model.Contact;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

/**
 * The ViewListFragment class shows the list of contacts for the user to see.  It has a menu
 * bar to enable adding and deleting a contact.  A detail view can be displayed for displaying 
 * and editing contact information.
 */
public class ViewListFragment extends ListFragment 
{
	private IContactControlListener _listener;		// host activity listener
	
	/**
	 * creates activity and sets initial values
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		// add menu bar to fragment
		setHasOptionsMenu(true);
	}

	/**
	 * Creates the toolbar menu with application name and add contact option from resources.
	 * @param menu list of items in toolbar menu
	 * @param inflater object to add and place menu items
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
	{
		// add menu resource
		getActivity().getMenuInflater().inflate(R.menu.menu_list, menu);
		
		// give inflated menu back to host activity
		super.onCreateOptionsMenu(menu, inflater);
	}

	/**
	 * Attaches to fragment to the main activity 
	 * @param activity the activity to be attached to, should be parent
	 */
	@Override
	public void onAttach(Activity activity) 
	{	
		// assign listener reference from host activity
		try
		{
			_listener = (IContactControlListener) activity;
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException(activity.toString());
		}
		
		// attach to host activity
		super.onAttach(activity);
	}

	/**
	 * Resumes the fragment and refreshes the contact list
	 */
	@Override
	public void onResume() 
	{	
		super.onResume();
		refreshContactList();
	}

	/**
	 * Handles menu item selections.
	 * @param item The item the user selected
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{	
		// switch/case to check the button then call the below return
		switch(item.getItemId())
		{
			// add new contact
			case R.id.action_menu_add:
			{
				_listener.insertContact();				
			}
			default:
			{
				return super.onOptionsItemSelected(item);
			}
		}
	}

	/**
	 * Gets the contact when selecting a list view item.
	 * @param l	The listview where the click happened
	 * @param v The view that was clicked within the list view
	 * @param position The position of the view in the list
	 * @param id Row id of item clicked
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) 
	{	
		// get list adapter and call the listener select contact
		Contact contact = null;
		contact = (Contact) getListAdapter().getItem(position);

		// load clicked contact provided it is not null
		if(contact != null) 
		{
			_listener.selectContact(contact);
		}
		
	}

	/**
	 * Refreshes the contact list with the contact array adapter
	 */
	private void refreshContactList()
	{
		// refresh the contact list
		setListAdapter(_listener.getContactArrayAdapter());
	}
}
