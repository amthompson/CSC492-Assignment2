package sdsmt.edu.thompsonsamson.assignment2;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class ViewDetailFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRetainInstance(true);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		// inflate the ui
		View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
		
		// assign instances of views from layout resource
		
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		
		// use try/catch to assign listener
		
		super.onAttach(activity);
	}

	@Override
	public void onResume() {
		
		super.onResume();
		
		displayContact();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		// get the menu resource
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// switch/case to find out what item was selected
		
		return super.onOptionsItemSelected(item);
	}

	private void displayContact() {
		
		// display the contact
		
	}

}
