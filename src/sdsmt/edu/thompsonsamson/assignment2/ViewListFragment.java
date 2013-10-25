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

public class ViewListFragment extends ListFragment {

	private IContactControlListener _listener;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		// add menu resource
		getActivity().getMenuInflater().inflate(R.menu.menu_list, menu);
		
		// give inflated menu back to host activity
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onAttach(Activity activity) {
		
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

	@Override
	public void onResume() {
		
		super.onResume();
		
		// refresh the list
		refreshContactList();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// switch/case to check the button then call the below return
		switch(item.getItemId())
		{
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

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
		// get list adapter and call the listener select contact
		Contact contact = null;		
		contact = (Contact) getListAdapter().getItem(position);

		if(contact != null) {
			_listener.selectContact(contact);
		}
		
	}

	private void refreshContactList()
	{
		// refresh the contact list
		setListAdapter(_listener.getContactArrayAdapter());
	}
	
	
}
