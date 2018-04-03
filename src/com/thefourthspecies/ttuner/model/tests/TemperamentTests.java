package com.thefourthspecies.ttuner.model.tests;
//package graham.ttuner.model.tests;
//
//import static org.junit.Assert.*;
//
//import graham.ttuner.model.data.Note;
//import graham.ttuner.model.data.Relationship;
//import graham.ttuner.model.exceptions.ParseException;
//import graham.ttuner.model.exceptions.RelationshipException;
//import graham.ttuner.model.exceptions.TemperamentException;
//import graham.ttuner.model.exceptions.TunerException;
//import graham.ttuner.model.exceptions.WaveformException;
//import graham.ttuner.model.listables.CentsTemperament;
//import graham.ttuner.model.listables.RelationshipsTemperament;
//import graham.ttuner.model.listables.Waveform;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.junit.Before;
//import org.junit.Test;
//
//
//public class TemperamentTests {
//	RelationshipsTemperament t;
//	Relationship a;
//	Relationship a1;
//	Relationship a2;
//	Relationship b;
//	Relationship c;
//	
//	String sa;
//	String sb;
//	String sc;
//	
//	List<Relationship> l;
//	
//	String n;
//	
//	@Before
//	public void setUp() throws RelationshipException, ParseException {
//		t = new RelationshipsTemperament();
//		
//		sa = "C = G -1/6P";
//		sb = "A = E";
//		sc = "C = E";
//		
//		a  = new Relationship(sa);		
//		b  = new Relationship(sb);
//		c  = new Relationship(sc);
//
//		a1 = new Relationship("C = G -1/6P");
//		a2 = new Relationship("C = G");
//		
//		l = new ArrayList<Relationship>();
//		l.add(a);
//		l.add(b);
//	}
//	
//	
//		
//	@Test
//	public void testSetName() {
//		assertEquals("temperament", t.getName().substring(0, 11));		
//		n = "Werckmeister";
//		t.setName(n);
//		assertEquals(n, t.getName());	
//	}
//	
//	@Test
//	public void testAdd() throws RelationshipException, TemperamentException {
//		assertEquals(0, t.size());
//		
//		t.add(a);
//		assertEquals(1, t.size());
//		
//		t.add(b);
//		assertEquals(2, t.size());
//		
//		t.add(a1);
//		assertEquals(2, t.size());
//		
//		t.add(c);
//		assertEquals(3, t.size());
//	}
//
//	@Test
//	public void testAddBad() {
//		try {
//			t.add(a);
//			assertEquals(1, t.size());
//			t.add(a2);
//			assertEquals(1, t.size());
//		} catch (TemperamentException  e) {
//			System.out.println(e.getMessage());
//			assertEquals(1, t.size());
//			return;
//		}
//		fail("Exception wasn't thrown!");
//	}
//	
//		
//	@Test
//	public void testAsList() throws RelationshipException, TemperamentException {
//		assertEquals(new ArrayList<Relationship>(), t.getRelationships());
//		
//		t.add(a);
//		t.add(b);
//		assertEquals(l, t.getRelationships());
//	}
//		
//	@Test
//	public void testRemove() throws RelationshipException, TemperamentException {
//		t.add(a);
//		assertEquals(1, t.size());
//		
//		t.remove(a);
//		assertEquals(0, t.size());
//		
//		t.add(a2);
//		assertEquals(1, t.size());
//		
//		t.remove(b);
//	}
//	
//	@Test
//	public void testConstructorCollection() throws RelationshipException, ParseException, TemperamentException {
//		t = new RelationshipsTemperament(l);
//		assertEquals(2, t.size());
//		assertEquals(l, t.getRelationships());
//	}
//	
//	@Test
//	public void testConstructorStringCollection() throws RelationshipException, ParseException, TemperamentException {
//		t = new RelationshipsTemperament("hello", l);
//		assertEquals("hello", t.getName());
//		assertEquals(l, t.getRelationships());
//	}
//	
//	@Test
//	public void testConstructorStringCommas() throws TunerException {
//		l.add(c);
//		
//		String temp = sa + "," + sb + "," + sc;
//		t = new RelationshipsTemperament(temp);
//		
//		assertEquals(3, t.size());
//		assertEquals(l, t.getRelationships());
//		
//		temp = sa + ", " + sb + ", " + sc;
//		t = new RelationshipsTemperament(temp);
//		assertEquals(l, t.getRelationships());
//	}
//	
//	@Test
//	public void testConstructorStringNewlines() throws TunerException {
//		l.add(c);
//		String temp = sa + "\n" + sb + "\n" + sc;
//		t = new RelationshipsTemperament(temp);
//		
//		assertEquals(3, t.size());
//		assertEquals(l, t.getRelationships());
//	
//		temp = sa + "\n" + sb + " \n " + sc;
//		t = new RelationshipsTemperament(temp);
//		
//		assertEquals(3, t.size());
//		assertEquals(l, t.getRelationships());
//	}
//	
//	@Test
//	public void testConstructorsArray() throws RelationshipException, ParseException, TemperamentException {
//		t = new RelationshipsTemperament(a,b);
//		
//		assertEquals(2, t.size());
//		assertEquals(l, t.getRelationships());
//	}
//	
//
//	
//	@Test
//	public void testEquals() throws RelationshipException, ParseException, TemperamentException {
//		t = new RelationshipsTemperament();
//		RelationshipsTemperament t1 = new RelationshipsTemperament();
//		assertEquals(t, t1);
//		
//		t1 = new RelationshipsTemperament(a,b);
//		t.add(a);
//		t.add(b);
//		assertEquals(t, t1);
//		
//		t = new RelationshipsTemperament(b,a);
//		assertFalse(t.getRelationships().equals(t1.getRelationships()));
//		assertEquals(t, t1);
//		
//		t = new RelationshipsTemperament(a,b,c);
//		assertFalse(t.equals(t1));
//		
//		t = new RelationshipsTemperament();
//		assertFalse(t.equals(t1));
//	}
//	
////	@Test
////	public void testIterator() throws RelationshipException, ParseException, TemperamentException {
////		t = new RelationshipsTemperament(a,b);
////		List<Relationship> l1 = new ArrayList<Relationship>();
////		for (Relationship r : t) {
////			l1.add(r);
////		}
////		assertEquals(l, l1);
////	}
//	
//	@Test
//	public void testGetDetails() throws TemperamentException, RelationshipException, ParseException {
//		assertEquals("", t.getDetails());
//		t = new RelationshipsTemperament(b,c);
//		String expected = "A = E, C = E";
//		String result = t.getDetails();
//		System.out.println(t.getDetails());
//		assertEquals(expected, result);
//		
//	}
//	
//	@Test
//	public void testCents() throws TunerException {
//		String VALDETS = "A+0.0, Bb+5.9, B-3.9, C+5.9, C#+0.0, D+2.0, " +
//				"Eb+3.9, E-2.0, F+7.8, F#-2.0, G+3.9, G#+2.0";
//		t = new RelationshipsTemperament("Vallotti", VALDETS);
//		assertEquals(VALDETS, t.getDetails());
//		
////		assertTrue(t instanceof CentsTemperament); Certainly not, at this point....
//		
//		CentsTemperament c = new CentsTemperament();
//		c.setDetails(VALDETS);
//		assertEquals(t, c);
//		
//	}
//	
//	
////	t = new Temperament();
////	
////	sa = "C = G -1/6P";
////	sb = "A = E";
////	sc = "C = E";
////	
////	a  = new Relationship(sa);		
////	b  = new Relationship(sb);
////	c  = new Relationship(sc);
//
//}
