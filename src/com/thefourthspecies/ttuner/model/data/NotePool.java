package com.thefourthspecies.ttuner.model.data;



import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.thefourthspecies.ttuner.model.exceptions.ParseException;

/**
 * A set of unique Notes and some operations that can be performed on them.
 * @author graham
 */
public class NotePool implements Iterable<Note> {

	private Set<Note> pool;

	public NotePool() {
		pool = new HashSet<Note>();
	}
	
	public NotePool(Collection<Note> notes) {
		pool = new HashSet<Note>(notes);
	}
	
	/**
	 * Returns the note in this NotePool corresponding to the give name. Returns
	 * null if it isn't present.
	 * @param name
	 * @return
	 */
	public Note get(String name) {
		
		for (Note note : pool) {
			if (name.equals(note.getName())) {
				return note;
			}
		}
		return null;
	}

	/**
	 * Adds a note to the pool.
	 */
	public void add(Note n) {
		pool.add(n);
	}
	
	/**
	 * Creates a new note with given Name and adds it to the pool
	 * @throws ParseException if the given name isn't an acceptable note name
	 */
	public void add(String name) throws ParseException {
		pool.add(new Note(name));
	}
	
	/**
	 * Returns the complete, unordered group of notes in this pool
	 */
	public Set<Note> getAll() { return pool; }

	/**
	 * Deletes all Notes.
	 */
	public void empty() {
		pool.clear();
	}
	
	/**
	 * @return  the number of notes in this pool
	 */
	public int size() {
		return pool.size();
	}
	
	/**
	 * Returns true if every note has an assigned frequency. True even if empty.
	 * @return
	 */
	public boolean isComplete() {
		for (Note n : pool) {
			if (n.getFreq() == null) {
				return false;
			}
		}
		return true;
	}
	
	public void clearFreqs() {
		for (Note n : pool) {
			n.eraseFreq();
		}
	}

	@Override
	public Iterator<Note> iterator() {
		return pool.iterator();
	}
}

