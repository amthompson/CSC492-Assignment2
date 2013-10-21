package sdsmt.edu.thompsonsamson.assignment2;

import sdsmt.edu.thompsonsamson.assignment2.Model.Contact;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ViewDetailFragment extends Fragment {

	private IContactControlListener _listener;
	private Contact _contact = null;
	
	private EditText _fieldName;
	private EditText _fieldPhone;
	private EditText _fieldEmail;
	private EditText _fieldAddress;
	private EditText _fieldCity;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRetainInstance(true);
		setHasOptionsMenu(true);
				
		displayContact();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		// inflate the fragment
		View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
		
		// assign instances of views from layout resource
		_fieldName = (EditText) rootView.findViewById(R.id.editTextName);
		_fieldPhone = (EditText) rootView.findViewById(R.id.editTextPhone);
		_fieldEmail = (EditText) rootView.findViewById(R.id.editTextEmail);
		_fieldAddress = (EditText) rootView.findViewById(R.id.editTextAddress);
		_fieldCity = (EditText) rootView.findViewById(R.id.editTextCity);
		
		Button buttonSave = (Button) rootView.findViewById(R.id.buttonSave);
		buttonSave.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				// get current values of text fields
				_contact.Name = _fieldName.getText().toString();
				_contact.Phone = _fieldPhone.getText().toString();
				_contact.Email = _fieldEmail.getText().toString();
				_contact.Address = _fieldAddress.getText().toString();
				_contact.City = _fieldCity.getText().toString();
				
				// do something when button is clicked
				if(_contact.ID > 0)
				{
					_listener.updateContact(_contact);	
				}
				else
				{
					_listener.insertContact(_contact);
				}
			}
			
		});
		
		return rootView;
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
		
		super.onAttach(activity);
	}

	@Override
	public void onResume() {
		
		super.onResume();
		
		displayContact();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		// get the menu resource if there is an actual contact being edited
		if( _contact.ID > 0 )
		{
			getActivity().getMenuInflater().inflate(R.menu.menu_detail, menu);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// switch/case to find out what item was selected
		switch(item.getItemId())
		{
			case R.id.action_menu_edit:
			{
				_listener.updateContact(_contact);
				return true;
			}
			case R.id.action_menu_delete:
			{
				_listener.deleteContact(_contact);
				return true;
			}
			default:
			{
				return super.onOptionsItemSelected(item);
			}
		}
		
	}

	private void displayContact() {
		
		// display the contact
		_contact = _listener.getContact();
		
		if( _contact.ID > 0 )
		{
			_fieldName.setText(_contact.Name);
			_fieldPhone.setText(_contact.Phone);
			_fieldEmail.setText(_contact.Email);
			_fieldAddress.setText(_contact.Address);
			_fieldCity.setText(_contact.City);
		}
	}

}
