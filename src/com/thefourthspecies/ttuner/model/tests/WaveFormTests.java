package com.thefourthspecies.ttuner.model.tests;

import static org.junit.Assert.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.thefourthspecies.ttuner.model.exceptions.ParseException;
import com.thefourthspecies.ttuner.model.exceptions.WaveformException;
import com.thefourthspecies.ttuner.model.listables.Waveform;


public class WaveFormTests {
	double DELTA = 1.0E-8;
	
	Waveform w;
	List<Double> l;
	String s;
	
	@Before
	public void setUp() {
		w = new Waveform();
	}
	
	@Test
	public void testSetHarmonics() throws WaveformException {
		l = Arrays.asList(1.0);
		assertEquals(l, w.getHarmonics());
		
		l = Arrays.asList(8.0, 4.0, 2.0, 1.0, 0.0, 8.0);
		w.setHarmonics(l);
		assertEquals(l, w.getHarmonics());
		assertEquals(23, w.getWeight(), DELTA);
	}
	
	@Test
	public void testSetHarmonicsString() throws WaveformException, ParseException {
		l = Arrays.asList(3.0, 0.0, 1.0);
		w.setHarmonics("3, 0, 1");
		assertEquals(l, w.getHarmonics());
		assertEquals(4, w.getWeight(), DELTA);
	}
	
	@Test (expected = WaveformException.class)
	public void testBadHarmonics() throws WaveformException {
		l = new ArrayList<Double>();
		w.setHarmonics(l);
	}
	
	@Test (expected = WaveformException.class)
	public void testBadHarmonics2() throws WaveformException {
		l = Arrays.asList(0.0, 0.0, 0.0);
		w.setHarmonics(l);
	}
	
	@Test
	public void testSetName() {
		assertEquals("waveform", w.getName().substring(0, 8));
		
		s = "Square";
		w.setName(s);
		assertEquals(s, w.getName());	
	}
	
//	@Test
//	public void testConstructors() throws WaveformException {
//		s = "Fun";
//		l = Arrays.asList(1.0, 2.0, 3.0);
//		w = new Waveform(s, l);
//		assertEquals(s, w.getName());
//		assertEquals(l, w.getHarmonics());
//		
//		w = new Waveform(l);
//		assertEquals("waveform", w.getName().substring(0, 8));
//		assertEquals(l, w.getHarmonics());
//	}
	
	@Test (expected = ParseException.class)
	public void testBadness() throws WaveformException, ParseException {
		s = "happy days";
		w.setHarmonics(s);
	}
	
	@Test
	public void testGetDepth() throws WaveformException, ParseException {
		assertEquals(1, w.getDepth());
		w.setHarmonics("1, 2, 0, 0");
		assertEquals(4, w.getDepth());
	}
	
	@Test
	public void testGet() throws WaveformException, ParseException {
		assertEquals(1.0, w.get(0), DELTA);
		w.setHarmonics("1,2,3");
		assertEquals(3.0, w.get(2), DELTA);
	}
	
	@Test
	public void testGetDetails() throws WaveformException {
		l = Arrays.asList(8.0, 3.5, 1.0, 0.5, 0.0625, 5.12347, 17.0);
		w.setHarmonics(l);
		String s = "8, 3.5, 1, .5, .0625, 5.1235, 17";
		System.out.println(w.getDetails());
		assertEquals(s, w.getDetails());
	}
	
	@Test
	public void testGetHarmonic() throws WaveformException {
		l = Arrays.asList(8.0, 3.5, 1.0, 0.5, 0.0625, 5.12347, 17.0);
		w.setHarmonics(l);
		assertEquals((Double) 8.0, w.getHarmonic(1));
		assertEquals((Double)17.0, w.getHarmonic(7));
		assertEquals((Double) 0.0, w.getHarmonic(8));
		assertEquals((Double) 0.0, w.getHarmonic(100));
		
		w.setHarmonic(1, 42.0);
		assertEquals((Double) 42.0, w.getHarmonic(1));
		
	}
	
	@Test (expected = WaveformException.class)
	public void testSetHarmonic() throws WaveformException {
		System.out.println(w.getHarmonic(1) + " : 10");
		
		
		w.setHarmonic(1, 10.0);
		assertEquals((Double) 10.0, w.getHarmonic(1));
		
		System.out.println(w.getWeight());
		assertTrue(10.0 ==  w.getWeight());
		
		w.setHarmonic(100, 0.0);
		assertEquals((Double) 0.0, w.getHarmonic(100));
		w.setHarmonic(100, 42.0);
		assertEquals((Double) 42.0, w.getHarmonic(100));
		
		assertEquals((Double) 0.0, w.getHarmonic(50));
		
		w.setHarmonic(0, 3.0);
	}

	
	@Test
	public void testFormatWeight() {
		
		assertEquals("1", Waveform.formatWeight(1.0));
		assertEquals("0.0625", Waveform.formatWeight(0.0625));
		assertEquals("0", Waveform.formatWeight(0.0));
		assertEquals("3.5", Waveform.formatWeight(3.5));
	}
	
	@Test
	public void testFormatAll() {
		l = Arrays.asList(8.0, 1.0, 0.0, 0.25, 0.0625);
		assertEquals("8, 1, 0, .25, .0625", Waveform.formatAll(l));
	}
}


