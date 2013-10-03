package com.thefourthspecies.ttuner.model.tests;
//package graham.ttuner.model.tests;
//
//
//import static org.junit.Assert.*;
//
//import graham.ttuner.model.data.Note;
//import graham.ttuner.model.exceptions.FrequencyException;
//import graham.ttuner.model.exceptions.ParseException;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Set;
//
//import org.junit.Before;
//import org.junit.Test;
//
//
//
//public class NoteTests {
//	Note a;
//	Note b;
//	Note c;
//	
//	@Before
//	public void setup() throws ParseException {
//		a = new Note("A");
//		b = new Note("B");		
//	}
////		
//	@Test
//	public void testGetName() {
//		assertEquals(a.getName(), "A");
//		assertEquals(b.getName(), "B");
//	}
//	
//	@Test 
//	public void testSetFreq() throws FrequencyException {
//		a.setFreq(415.0);
//		assertEquals(a.getFreq(), (Double) 415.0);
//		assertTrue(b.getFreq() == null);
//	}
//	
//	@Test (expected = FrequencyException.class)
//	public void testSetFreqDifferent() throws FrequencyException {
//		a.setFreq(440.0);
//		a.setFreq(415.0);
//	}
//
//	@Test (expected = FrequencyException.class)
//	public void testSetFreqDifferent2() throws FrequencyException {
//		a.setFreq(415.0);
//		a.setFreq(440.0);
//	}
//	
//	@Test (expected = FrequencyException.class)
//	public void testSetFreqDifferent3() throws FrequencyException {
//		a.setFreq(440.0);
//		a.setFreq(440.1);
//	}
//	
//	
//	
//	@Test 
//	public void testSetFreqClose() throws FrequencyException, ParseException {
//		c = new Note("C");
//		c.setFreq(261.6);
//		c.setFreq(261.6);
//		c.setFreq(261.62);
//		assertEquals(c.getFreq(), (Double) 261.62);
//		
//		c.setFreq(261.6);
//		assertEquals(c.getFreq(), (Double) 261.6);
//	}
//
//	
//
//	
//	@Test (expected = FrequencyException.class)
//	public void testSetFreqNegative() throws FrequencyException {
//		a.setFreq(-415.0);
//	}
//	
//	@Test
//	public void testGetValue() throws ParseException {
//		assertEquals(9, (int) a.getValue());
//		
//		Note x = new Note("F#");
//		assertEquals(6, (int) x.getValue());
//	}
//	
//	
//
//	@Test
//	public void testCompareTo() throws FrequencyException, ParseException {
//		a.setFreq(100);
//		b.setFreq(200);
//				
//		
//		
//		List<Note> expected = new ArrayList<Note>();
//		expected.add(a);
//		expected.add(b);
//	
//		List<Note> result = new ArrayList<Note>(expected); 
//		
//		Collections.sort(result);
//		assertEquals(expected, result);
//		
//		result.clear();
//		result.add(b);
//		result.add(a);
//		
//		assertFalse(expected.equals(result));
//		
//		Collections.sort(result);
//		assertEquals(expected, result);
//	}
//	
//	@Test
//	public void testEraseFreq() throws FrequencyException {
//		a.setFreq(100);
//		a.eraseFreq();
//		assertTrue(a.getFreq() == null);
//	}
//	
//}
