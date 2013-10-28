/**
* Project Assignment2 Address Book - Contact Control Listener
*/
package sdsmt.edu.thompsonsamson.assignment2;
import android.widget.ArrayAdapter;
import sdsmt.edu.thompsonsamson.assignment2.Model.Contact;

/**
 * Interface to control an activity with the database model
 */
public interface IContactControlListener {
	
	/**
	 * Action performed when a contact is selected
	 * @param contact the current contact object
	 */
	public void selectContact(Contact contact);

	/**
	 * Updates the data model with current contact
	 * @param contact the current contact object
	 */
	public void updateContact(Contact contact);
	
	/**
	 * Creates a blank contact
	 */
	public void insertContact();

	/**
	 * 
	 * @param contact the current contact object
	 */
	public void insertContact(Contact contact);

	/**
	 * Deletes a contact from the database,
	 * @param contact the current contact object
	 */
	public void deleteContact(Contact contact);
	
	/**
	 * Returns current contact object
	 * @return the current contact object
	 */
	public Contact getContact();
	
	/**
	 * Gets the list of contacts by using an array adapter to control
	 * inserts, updates and removals
	 * @return ArrayAdapter with a list of contact objects
	 */
	public ArrayAdapter<Contact> getContactArrayAdapter();
	
}
