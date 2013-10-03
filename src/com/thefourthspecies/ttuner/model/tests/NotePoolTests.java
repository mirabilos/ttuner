package com.thefourthspecies.ttuner.model.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.thefourthspecies.ttuner.model.data.Note;
import com.thefourthspecies.ttuner.model.data.NotePool;
import com.thefourthspecies.ttuner.model.exceptions.FrequencyException;
import com.thefourthspecies.ttuner.model.exceptions.ParseException;


public class NotePoolTests {

	NotePool np;
	
	@Before
	public void setUp() {
		np = new NotePool();
	}
		
	@Test
	public void testAdd() throws ParseException {
		assertEquals(0, np.size());
		
		np.add("A");
		assertEquals(1, np.size());
		
		np.add(new Note("B"));
		assertEquals(2, np.size());
		
		np.add("B");
		assertEquals(2, np.size());
	}
	
	@Test
	public void testGet() throws ParseException {
		Note x = new Note("B");
		np.add(x);
		assertEquals(x, np.get("B"));
	}
	
	@Test
	public void testGetAll() throws ParseException {
		np.add("A");
		np.add(new Note("B"));
		Set<Note> notes = new HashSet<Note>(Arrays.asList(new Note("A"), new Note("B")));
		assertEquals(notes, np.getAll());
	}

	@Test
	public void testEmpty() throws ParseException {
		np.add("A");
		np.add("B");
		np.empty();
		assertEquals(0, np.size());
	}
	
	@Test
	public void testIsComplete() throws  ParseException, FrequencyException {
		assertTrue(np.isComplete());
		np.add("A");
		np.add("B");
		assertFalse(np.isComplete());
		
		np.get("A").setFreq(440);
		assertFalse(np.isComplete());

		np.get("B").setFreq(415);
		assertTrue(np.isComplete());
	}
	
	@Test
	public void testIterator() throws ParseException, FrequencyException {
		np.add("A");
		np.add("B");
		assertFalse(np.isComplete());
		
		for (Note n : np) {
			n.setFreq(100);
		}
		assertTrue(np.isComplete());
	}
	
	@Test
	public void testNoNote() throws ParseException {
		np.add("A");
		Note c = np.get("C");
		assertTrue(c == null);
	}
	
	@Test
	public void testClearFreqs() throws ParseException, FrequencyException {
		np.add("A");
		np.add("B");
		np.add("C");
		assertFalse(np.isComplete());
		
		np.get("A").setFreq(100);
		np.get("B").setFreq(200);
		np.get("C").setFreq(300);
		assertTrue(np.isComplete());
		
		np.clearFreqs();
		assertFalse(np.isComplete());
		
	}
}