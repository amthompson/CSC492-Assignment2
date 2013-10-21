package sdsmt.edu.thompsonsamson.assignment2;

import sdsmt.edu.thompsonsamson.assignment2.Model.Contact;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
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
	private Button _buttonSave;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRetainInstance(true);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		// inflate the fragment
		View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

		// assign instances of views from layout resource
		configureUiObjects(rootView);
				
		return rootView;
	}

	private void disableUiObjects() {

		// disable by default
		if( _fieldName != null ) {
			_fieldName.setEnabled(false);
			_fieldPhone.setEnabled(false);
			_fieldEmail.setEnabled(false);
			_fieldAddress.setEnabled(false);
			_fieldCity.setEnabled(false);
		}
		
		if( _buttonSave != null) {
			_buttonSave.setVisibility(View.GONE);
		}
	}

	private void enableUiObjects() {

		// disable by default
		if( _fieldName != null ) {
			_fieldName.setEnabled(true);
			_fieldPhone.setEnabled(true);
			_fieldEmail.setEnabled(true);
			_fieldAddress.setEnabled(true);
			_fieldCity.setEnabled(true);
		}

		if( _buttonSave != null) {
			_buttonSave.setVisibility(View.VISIBLE);
		}
	}

	private void configureUiObjects(View v) {

		// assign the text fields
		_fieldName = (EditText) v.findViewById(R.id.editTextName);
		_fieldPhone = (EditText) v.findViewById(R.id.editTextPhone);
		_fieldEmail = (EditText) v.findViewById(R.id.editTextEmail);
		_fieldAddress = (EditText) v.findViewById(R.id.editTextAddress);
		_fieldCity = (EditText) v.findViewById(R.id.editTextCity);
		
		// assign the save button and add listener
		_buttonSave = (Button) v.findViewById(R.id.buttonSave);
		_buttonSave.setOnClickListener(new Button.OnClickListener() {
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
				// _listener.updateContact(_contact); -- removed
				enableUiObjects();
				
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
		
		// blowing up here because objects don't exist on refresh (screen rotate)
		
		if( _contact.ID > 0 )
		{
			// disable UI objects if viewing a contact
			disableUiObjects();
			
			_fieldName.setText(_contact.Name);
			_fieldPhone.setText(_contact.Phone);
			_fieldEmail.setText(_contact.Email);
			_fieldAddress.setText(_contact.Address);
			_fieldCity.setText(_contact.City);
		}
		else
		{
			_fieldName.setText("");
			_fieldPhone.setText("");
			_fieldEmail.setText("");
			_fieldAddress.setText("");
			_fieldCity.setText("");
		}
			
	}

}
