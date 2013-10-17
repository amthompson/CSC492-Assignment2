package sdsmt.edu.thompsonsamson.assignment2;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class ViewListFragment extends ListFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		// add menu resource
		
	}

	@Override
	public void onAttach(Activity activity) {
		
		// add try catch for listener
		
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
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
		// get list adapter and call the listener select contact
		
	}

	private void refreshContactList()
	{
		// refresh the contact list
	}
	
	
}
