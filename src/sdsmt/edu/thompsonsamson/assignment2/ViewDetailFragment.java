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
 * The View Detail Fragment shows the details of a contact.  Users can edit contact information and
 * save it with a menu.  Contact information is saved to a bundle and here. 
 * <p>
 * 
 * @author Andrew Thopmson
 * @author Scott Samson
 * Date: 10/27/2013
 */
public class ViewDetailFragment extends Fragment 
{
	private IContactControlListener _listener;		// host activity listener
	private Contact _contact = null;				// current contact
    private boolean _isOrientationChanging = false;	// orientation change flag
    private boolean _isEditMode;					// edit mode flag
		
	private EditText _fieldName;	// field for entering contact name
	private EditText _fieldPhone;	// field for entering contact phone number
	private EditText _fieldEmail;	// field for entering contact email address
	private EditText _fieldAddress;	// field for entering contact street address
	private EditText _fieldCity;	// field for entering contact city/state
	private Button _buttonSave;		// save contact button

	/**
	 * @author Andrew Thompson
	 * @author Scott Samson
	 * Creates the ViewDetailFragment activity
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		// always turn off edit mode on start
		_isEditMode = false;
		
		setRetainInstance(true);
		setHasOptionsMenu(true);
	}

	/**
	 * @author Andrew Thompson
	 * @author Scott Samson
	 * Creates and returns the view associated with the fragment
	 */
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

	/**
	 * @author Andrew Thompson
	 * @author Scott Samson
	 * Attaches the fragment view to host activity
	 */
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

	/**
	 * @author Andrew Thompson
	 * @author Scott Samson
	 * Resumes the fragment view
	 */
	@Override
	public void onResume() 
	{	
		super.onResume();
	
		// if not rotating device, get the contact from host activity
		if( _isOrientationChanging == false ) 
		{
			_contact = _listener.getContact();
		}

		// display the current contact info
		displayContact();
	}

	/**
	 * @author Andrew Thompson
	 * @author Scott Samson
	 * When pause happens, get the orientation change flag
	 */
	@Override
	public void onPause() 
	{
		_isOrientationChanging = getActivity().isChangingConfigurations();
		super.onPause();
	}

	/**
	 * @author Andrew Thompson
	 * @author Scott Samson
	 * If the contact isn't new, inflate the detail menu resource item
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
	{
		if( _contact.ID > 0 ) 
		{
			getActivity().getMenuInflater().inflate(R.menu.menu_detail, menu);
		}
	}

	/**
	 * @author Andrew Thompson
	 * @author Scott Samson
	 * Controls the action of the menu - edit or delete
	 */
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
				_listener.deleteContact(_contact);
				return true;
			}
			default:
			{
				return super.onOptionsItemSelected(item);
			}
		}
	}
	
	
	/**
	 * @author Andrew Thompson
	 * Sets up the GUI items. These include finding the EditText resources and
	 * creating the save button. Also an anonymous inner class is used for the
	 * button click listener.
	 * @param v the current view
	 */
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
			
			/**
			 * 
			 * @param v
			 */
			@Override
			public void onClick(View v)
			{	
				// return if user tried to save empty contact (goes back to edit)
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

	/**
	 * @author Andrew Thompson
	 * Hides the keyboard if open. This is done if they keyboard isn't closed
	 * before hitting save button.
	 */
	private void hideKeyboard()
	{
		InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(getView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	/**
	 * @author Andrew Thompson
	 * Checks the edit mode to enable or disable the GUI objects. This affects
	 * the EditText fields and the save contact button.
	 */
	private void checkEditMode() 
	{	
		// if editing, enable the UI objects, otherwise disable
		if( _isEditMode == true ) {
			enableEditMode();
		}
		else {
			disableEditMode();
		}
	}
	
	/**
	 * @author Andrew Thompson
	 * Disables the EditText fields and hides the save button
	 */
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

    /**
	 * @author Andrew Thompson
	 * Enables the EditText fields and shows the save button
     */
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
	
    /**
	 * @author Andrew Thompson
     * Fill the EditText fields with the current contact information. If the
     * contact is new, enable edit mode also.
     */
	private void displayContact() 
	{
		Log.d("Assignment2","displayContact");
		
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

	/**
	 * @author Scott Samson
	 * Displays an alertDialog to confirm deletion of a contact.  If the user clicks the "OK" button the delete
	 * proceeds, if not, we return to edit.
	 */
	private void sendDeleteContactAlert()
	{
		// create and setup alert dialog
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
		
		// setup buttons
		alertDialog.setPositiveButton(R.string.fragment_detail_alert_Ok, deleteContactConfirmButtonListener);
		alertDialog.setNegativeButton(R.string.fragment_detail_alert_Cancel, null);
		
		// create and show alert
		alertDialog.create();
		alertDialog.show();
		
	}
	
	/**
	 * @author Scott Samson
	 * Checks to see if the user is trying to save a contact with no data in any field.  If they are then 
	 * a warning is displayed.  If not false is returned.
	 * 
	 * @return true returns true if warning is displayed
	 * @return false returns false if warning is not displayed
	 */
	private boolean sendEmptyContactAlert()
	{
		//check if any field has been edited
		if (_fieldName.getText().toString().matches("") && 
			_fieldPhone.getText().toString().matches("") &&
			_fieldEmail.getText().toString().matches("") && 
			_fieldAddress.getText().toString().matches("") &&
			_fieldCity.getText().toString().matches(""))
		{
			// save it to the bundle if it has
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
