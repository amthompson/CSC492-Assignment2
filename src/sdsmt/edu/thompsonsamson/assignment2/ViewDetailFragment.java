/**
 * Project Assignment2 Address Book
 */
package sdsmt.edu.thompsonsamson.assignment2;

import sdsmt.edu.thompsonsamson.assignment2.Model.Contact;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * <p>
 * The View Detail Fragment shows the details of a contact.  Contact information
 * is saved to a bundle and here. 
 * <p>
 * 
 * @author Andrew Thopmson
 * @author Scott Samson
 * Date: 10/27/2013
 */
public class ViewDetailFragment extends Fragment 
{
	
	private IContactControlListener _listener;
	private Contact _contact = null;
    private boolean _isOrientationChanging = false;
    private boolean _isEditMode;
		
	private EditText _fieldName;
	private EditText _fieldPhone;
	private EditText _fieldEmail;
	private EditText _fieldAddress;
	private EditText _fieldCity;
	private Button _buttonSave;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		_isEditMode = false;
				
		setRetainInstance(true);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{	
		Log.d("Assignment2","onCreateView");
		
		// inflate the fragment
		View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
		
		// assign instances of views from layout resource
		configureUiObjects(rootView);

		// check if in edit mode or not
		checkEditMode();      
		
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) 
	{	
		// assign listener reference from host activity
		try {
			_listener = (IContactControlListener) activity;
		}
		catch (ClassCastException e) {
			throw new ClassCastException(activity.toString());
		}
		
		super.onAttach(activity);
	}

	@Override
	public void onResume() 
	{	
		Log.d("Assignment2","onResume");
		
		super.onResume();
	
		if( _isOrientationChanging == false ) 
		{
			_contact = _listener.getContact();
		}
		
						
		displayContact();
	}

	@Override
	public void onPause() 
	{
		Log.d("Assignment2","onPause");
		
		_isOrientationChanging = getActivity().isChangingConfigurations();
		super.onPause();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
	{
		// get the menu resource if there is an actual contact being edited
		if( _contact.ID > 0 ) 
		{
			getActivity().getMenuInflater().inflate(R.menu.menu_detail, menu);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// switch/case to find out what item was selected
		switch(item.getItemId())
		{
			case R.id.action_menu_edit:
			{
				enableEditMode();
				return true;
			}
			case R.id.action_menu_delete:
			{
				
				// add the confirmation message
				
				sendDeleteContactAlert();
				return true;
			}
			default:
			{
				return super.onOptionsItemSelected(item);
			}
		}
	}
	
	

	private void configureUiObjects(View v) 
	{	
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
				if (sendEmptyContactAlert())
				{
					return;
				}
				
				// get current values of text fields
				_contact.Name = _fieldName.getText().toString();
				_contact.Phone = _fieldPhone.getText().toString();
				_contact.Email = _fieldEmail.getText().toString();
				_contact.Address = _fieldAddress.getText().toString();
				_contact.City = _fieldCity.getText().toString();
				
				// hide the soft keyboard if open
				hideKeyboard();				
				
				// if there is a contact id, update it. otherwise add it
				if(_contact.ID > 0) {
					_listener.updateContact(_contact);	
				}
				else {
					_listener.insertContact(_contact);
				}
			}		
		});
	}

	private void hideKeyboard()
	{
		InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(getView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}	
	
	private void checkEditMode() 
	{	
		
		Log.d("Assignment2","CheckEditMOde");
		
		// if editing, enable the UI objects, otherwise disable
		if( _isEditMode == true ) {
			enableEditMode();
		}
		else {
			disableEditMode();
		}
	}
	
    private void disableEditMode() 
    {
		_isEditMode = false;
	    _fieldName.setEnabled(false);
	    _fieldPhone.setEnabled(false);
	    _fieldEmail.setEnabled(false);
	    _fieldAddress.setEnabled(false);
	    _fieldCity.setEnabled(false);
        _buttonSave.setVisibility(View.GONE);
    }

    private void enableEditMode() 
    {
		_isEditMode = true;
        _fieldName.setEnabled(true);
        _fieldPhone.setEnabled(true);
        _fieldEmail.setEnabled(true);
        _fieldAddress.setEnabled(true);
        _fieldCity.setEnabled(true);
        _buttonSave.setVisibility(View.VISIBLE);
    }
	
	private void displayContact() 
	{	
		
		// yup - this is where contact is null when restoring from
		// recent activities after pause and causing null exception. 
		// Need to make sure we're getting the contact information 
		// saved. Maybe can clean up the enable/disable stuff
		
		if( _contact.ID > 0 ) {
			_fieldName.setText(_contact.Name);
			_fieldPhone.setText(_contact.Phone);
			_fieldEmail.setText(_contact.Email);
			_fieldAddress.setText(_contact.Address);
			_fieldCity.setText(_contact.City);
		}
		else {
			_fieldName.setText("");
			_fieldPhone.setText("");
			_fieldEmail.setText("");
			_fieldAddress.setText("");
			_fieldCity.setText("");
			
			// set edit mode on
			enableEditMode();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		

		// store the key/pair to the bundle
		outState.putLong("ContactID", _contact.ID);
		outState.putString("ContactName", _contact.Name);
		outState.putString("ContactPhone", _contact.Phone);
		outState.putString("ContactAddress", _contact.Address);
		outState.putString("ContactCity", _contact.City);
	}
	
	private void sendDeleteContactAlert()
	{
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		alertDialog.setTitle(R.string.fragment_detail_alert_delete_title);
		alertDialog.setMessage(R.string.fragment_detail_alert_delete_message);
		alertDialog.setCancelable(true);
		
		
		// listener for confirmation
		DialogInterface.OnClickListener deleteContactConfirmButtonListener = new DialogInterface.OnClickListener() {						
			@Override
			public void onClick(DialogInterface dialog, int which) {
				_listener.deleteContact(_contact);
			}
		};
		
		alertDialog.setPositiveButton(R.string.fragment_detail_alert_Ok, deleteContactConfirmButtonListener);
		alertDialog.setNegativeButton(R.string.fragment_detail_alert_Cancel, null);
		alertDialog.create();
		alertDialog.show();
		
	}
	
	private boolean sendEmptyContactAlert( )
	{
		if (_fieldName.getText().toString().matches("") && _fieldPhone.getText().toString().matches("") && _fieldEmail.getText().toString().matches("") &&
				_fieldAddress.getText().toString().matches("") && _fieldCity.getText().toString().matches(""))
		{
			Log.d("Assigment2", "sendEmptyContactAlert");
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
			alertDialog.setTitle(R.string.fragment_detail_alert_save_title);
			alertDialog.setMessage(R.string.fragement_detail_alert_save_message);
			alertDialog.setPositiveButton(R.string.fragment_detail_alert_Ok, null);
			alertDialog.setCancelable(true);
			alertDialog.create();
			alertDialog.show();
			
			return true;
		}
		return false;
	}
	
}
