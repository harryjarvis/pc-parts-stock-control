package part;

import java.util.ArrayList;

/**
 * Student ID: 21360548
 * Provides logic and in-memory management for {@link Part} objects
 * Responsible for creating, retrieving and deleting parts within the system
 * Maintains an internal list of parts, ensures that each part is assigned a unique identifier automatically
 */
public class PartMethods {
	
	private ArrayList<Part> parts;
	
	/**
	 * Constructs a new instance
	 * Initialises the internal customer list
	 */
	public PartMethods() {
		parts = new ArrayList<>();
	}
	
	private int idCounter = 1; // Incrementing IDs set automatically
	
	/**
	 * Adds a part that has been loaded from persistent storage
	 * This method preserves the part's existing ID and updates the internal ID counter to ensure future parts retrieve a unique ID
	 @param p part loaded from file
	 */
	public void addPartFromLoad(Part p) {
		parts.add(p);
		
		if (p.getId() >= idCounter) {
			idCounter = p.getId() +1;
		}
	}
	
	/**
	 * Adds a new part to the system
	 * @param p	part to be added
	 */
	public void addPart(Part p) {
		p.setId(idCounter);
		idCounter++;
		parts.add(p);
	}
	
	/**
	 * Retrieves a part by their unique ID
	 * @param id	part ID to search for
	 * @return	the matching {@link Part} or null if not found
	 */
	public Part viewPart(int id) {
		for (Part p : parts) {
			if (p.getId() == id) {
				return p;
			}
		} return null;
	}
	
	/**
	 * @return	list of all parts
	 */
	public ArrayList<Part> viewAllParts() {
		return new ArrayList<>(parts);
	}
	
	/**
	 * Deletes a customer from the system
	 * @param id ID of the part to be deleted
	 * @return true if the part was found and deleted, false otherwise
	 */
	public boolean deletePart(int id) {
		Part p = viewPart(id);
		if (p != null) {
			parts.remove(p);
			return true;
		} return false;
	}
	
}
