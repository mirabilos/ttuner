package com.thefourthspecies.ttuner.model.tests;
//package graham.ttuner.model.tests;
//
//import static org.junit.Assert.*;
//import graham.ttuner.model.data.Adjustment;
//import graham.ttuner.model.data.Note;
//import graham.ttuner.model.data.Relationship;
//import graham.ttuner.model.exceptions.ParseException;
//import graham.ttuner.model.exceptions.RelationshipException;
//
//import org.junit.Before;
//import org.junit.Test;
//
//
//public class RelationshipTests {
//
//	double DELTA = 0.0000000001;
//
//	Adjustment min4S;
//	Adjustment plus4S;
//
//	Adjustment min6P;
//	Adjustment plus6P;
//
//	Relationship r;
//
//	@Before
//	public void setUp() throws ParseException {
//		min4S = new Adjustment("-1/4S");
//		plus4S = new Adjustment("+1/4S");
//
//		min6P = new Adjustment("-1/6P");
//		plus6P = new Adjustment("+1/6P");
//	}
//
//	// This is just for debugging purposes, so it prints something nice.
//	public String method() {
//		return Thread.currentThread().getStackTrace()[2].getMethodName();
//	}
//
//	@Test
//	public void testEquals() throws RelationshipException, ParseException {
//		String c = "C";
//		String a = "A";
//		r = new Relationship(c, a);
//		Relationship s = new Relationship("A=C");
//		
//		assertEquals(s, r);
//		assertEquals(r, s);
//		assertEquals(r, r);
//	}
//	
//	@Test
//	public void testUnEquals() throws RelationshipException, ParseException {
//		r = new Relationship("A=C-1/4P");
//		Relationship s = new Relationship("A=C");
//		Relationship t = new Relationship("A=E");
//		
//		assertFalse(r.equals(s));
//		assertFalse(t.equals(s));
//	}
//	
//	
//	/*
//	 * There are four cases:
//	 * 
//	 * "G = A" delta = G(7) - A(9) (delta = left - right) * lower upper distance
//	 * 
//	 * -12 < delta < -6 right left delta+12 C(0)-A(9)=-9 A(9) C(0) 3
//	 * 
//	 * -6 <= de lta < 0 left right -delta G(7)-B(11)=-4 G(7) B(11) 4
//	 * 
//	 * 0 <= delta < 6 right left delta G(7)-D(2)=5 D(2) G(7) 5
//	 * 
//	 * 6 <= delta < 12 left right -delta+12 G(7)-C(0)=7 G(7) C(0) 5
//	 */
//
//	@Test
//	public void testCase1() throws RelationshipException, ParseException {
//		String c = "C";
//		String a = "A";
//		r = new Relationship(c, a);
//
//		assertEquals(a, r.getLower());
//		assertEquals(c, r.getUpper());
//		assertEquals(3, r.getDistance());
//	}
//
//	@Test
//	public void testCase2() throws RelationshipException, ParseException {
//		String g = "G";
//		String b = "B";
//		r = new Relationship(g, b);
//
//		assertEquals(g, r.getLower());
//		assertEquals(b, r.getUpper());
//		assertEquals(4, r.getDistance());
//	}
//
//	@Test
//	public void testCase3() throws RelationshipException, ParseException {
//		String g = "G";
//		String d = "D";
//		r = new Relationship(g, d);
//
//		assertEquals(d, r.getLower());
//		assertEquals(g, r.getUpper());
//		assertEquals(5, r.getDistance());
//	}
//
//	@Test
//	public void testCase4() throws RelationshipException, ParseException {
//		String g = "G";
//		String c = "C";
//		r = new Relationship(g, c);
//
//		assertEquals(g, r.getLower());
//		assertEquals(c, r.getUpper());
//		assertEquals(5, r.getDistance());
//	}
//
//	// Originally, I allowed unison relationships. I've eliminated that for now.
//	// @Test
//	// public void testSame() throws RelationshipException, ParseException {
//	// String x = "G";
//	// String y = "G";
//	// r = new Relationship(x, y);
//	//
//	// assertEquals(x, r.getLower());
//	// assertEquals(y, r.getUpper());
//	// assertEquals(x, r.getUpper());
//	// assertEquals(0, r.getDistance());
//	// }
//
//	@Test(expected = RelationshipException.class)
//	public void testBad() throws RelationshipException, ParseException {
//		r = new Relationship("C", "D");
//	}
//
//	@Test(expected = RelationshipException.class)
//	public void testBadtt() throws RelationshipException, ParseException {
//		r = new Relationship("C", "F#");
//	}
//
//	@Test(expected = RelationshipException.class)
//	public void testBadUnison() throws RelationshipException, ParseException {
//		r = new Relationship("C", "B#");
//	}
//
//	@Test
//	public void testIntervalPureMin3() throws RelationshipException, ParseException {
//		r = new Relationship("C", "Eb");
//		double interval = 6 / 5.0;
//
//		System.out.println("\n" + method() + "\ninterval: " + interval
//				+ "\nr.getInterval: " + r.getInterval());
//
//		assertEquals(interval, r.getInterval(), DELTA);
//	}
//
//	@Test
//	public void testIntervalPlusMin3() throws RelationshipException, ParseException {
//		r = new Relationship("F#", "A", plus4S);
//		double interval = plus4S.toDouble() * 6 / 5.0;
//
//		System.out.println("\n" + method() + "\ninterval: " + interval
//				+ "\nr.getInterval: " + r.getInterval());
//
//		assertEquals(interval, r.getInterval(), DELTA);
//	}
//
//	@Test
//	public void testIntervalMinusMin3() throws RelationshipException, ParseException {
//		r = new Relationship("C", "Eb", min4S);
//		double interval = min4S.toDouble() * 6 / 5.0;
//
//		System.out.println("\n" + method() + "\ninterval: " + interval
//				+ "\nr.getInterval: " + r.getInterval());
//
//		assertEquals(interval, r.getInterval(), DELTA);
//	}
//
//	@Test
//	public void testIntervalPureMaj3() throws RelationshipException, ParseException {
//		r = new Relationship("C", "E");
//		double interval = 5 / 4.0;
//
//		System.out.println("\n" + method() + "\ninterval: " + interval
//				+ "\nr.getInterval: " + r.getInterval());
//
//		assertEquals(interval, r.getInterval(), DELTA);
//	}
//
//	@Test
//	public void testIntervalPlusMaj3() throws RelationshipException, ParseException {
//		r = new Relationship("C", "E", plus4S);
//		double interval = plus4S.toDouble() * 5 / 4.0;
//
//		System.out.println("\n" + method() + "\ninterval: " + interval
//				+ "\nr.getInterval: " + r.getInterval());
//
//		assertEquals(interval, r.getInterval(), DELTA);
//	}
//
//	@Test
//	public void testIntervalMinusMaj3() throws RelationshipException, ParseException {
//		r = new Relationship("C", "E", min4S);
//		double interval = min4S.toDouble() * 5 / 4.0;
//
//		System.out.println("\n" + method() + "\ninterval: " + interval
//				+ "\nr.getInterval: " + r.getInterval());
//
//		assertEquals(interval, r.getInterval(), DELTA);
//	}
//
//	@Test
//	public void testIntervalPureP5() throws RelationshipException, ParseException {
//		r = new Relationship("C", "G");
//		double interval = 4 / 3.0;
//
//		System.out.println("\n" + method() + "\ninterval: " + interval
//				+ "\nr.getInterval: " + r.getInterval());
//
//		assertEquals(interval, r.getInterval(), DELTA);
//	}
//
//	@Test
//	public void testIntervalPlusP5() throws RelationshipException, ParseException {
//		r = new Relationship("C", "G", plus6P);
//		double interval = (4 / 3.0) / plus6P.toDouble();
//
//		System.out.println("\n" + method() + "\ninterval: " + interval
//				+ "\nr.getInterval: " + r.getInterval());
//
//		assertEquals(interval, r.getInterval(), DELTA);
//	}
//
//	@Test
//	public void testIntervalMinusP5() throws RelationshipException, ParseException {
//		r = new Relationship("C", "G", min6P);
//		double interval = (4 / 3.0) / min6P.toDouble();
//
//		System.out.println("\n" + method() + "\ninterval: " + interval
//				+ "\nr.getInterval: " + r.getInterval());
//
//		assertEquals(interval, r.getInterval(), DELTA);
//	}
//
//	@Test
//	public void testGetSymbol5() throws RelationshipException, ParseException {
//		r = new Relationship("C", "G", min6P);
//		assertEquals("C = G -1/6P", r.getSymbol());
//	}
//	
//	@Test
//	public void testGetSymbolMaj3() throws RelationshipException, ParseException {
//		r = new Relationship("C#", "A", plus4S);
//		assertEquals("A = C# +1/4S", r.getSymbol());
//	}
//	
//	@Test
//	public void testGetSymbolMin3() throws RelationshipException, ParseException {
//		r = new Relationship("G", "Bb");
//		assertEquals("G = Bb", r.getSymbol());
//	}
//	
//	// *******************************************************************************
//	// Now, to test the String Parsing!:
//	
//	Relationship parsed;
//	Relationship expected;
//	String input;
//	Adjustment adj;
//	double interval;
//	
//	// GOOD TESTS FIRST (later, exceptions):
//
//	@Test
//	public void testPureP5() throws ParseException, RelationshipException {
//		input = "A=E";
//		expected = new Relationship("A", "E");
//		parsed = new Relationship(input);
//		System.out.println(parsed);
//
//		assertEquals(expected, parsed);
//	}		
//
//	@Test
//	public void testPureMaj3() throws ParseException, RelationshipException {
//		input = "B = D#";
//		expected = new Relationship("B", "D#");
//		parsed = new Relationship(input);
//		System.out.println(parsed);
//
//		assertEquals(expected, parsed);	
//	}		
//
//
//	@Test
//	public void testPureMin3() throws ParseException, RelationshipException {
//		input = " A = F# ";
//		expected = new Relationship("F#", "A");
//		parsed = new Relationship(input);
//		System.out.println(parsed);
//
//		assertEquals(expected, parsed);	
//	}
//
//	@Test
//	public void testPlusPyth() throws ParseException, RelationshipException {
//		input = "C=G+1/6P";
//		adj = new Adjustment("+1/6P");
//		expected = new Relationship("C", "G", adj);
//		parsed = new Relationship(input);
//		System.out.println(parsed);
//
//		interval = (4/3.0)/adj.toDouble();
//		assertEquals((Double) interval, (Double) expected.getInterval());
//		assertEquals(expected, parsed);	
//	}
//
//	@Test
//	public void testMinPyth() throws ParseException, RelationshipException {
//		input = "A = D -1/6P";
//		adj = new Adjustment("-1/6P");
//		expected = new Relationship("A", "D", adj);
//		parsed = new Relationship(input);
//		System.out.println(parsed);
//
//		interval = (4/3.0)/adj.toDouble();
//		assertEquals((Double) interval, (Double) expected.getInterval());
//		assertEquals(expected, parsed);	
//	}
//
//	@Test
//	public void testPlusSyn() throws ParseException, RelationshipException {
//		input = "Eb = G +1/4S";
//		adj = new Adjustment("+1/4S");
//		expected = new Relationship("G", "Eb", adj);
//		parsed = new Relationship(input);
//		System.out.println(parsed);
//
//		interval = (5/4.0)*adj.toDouble();
//		assertEquals((Double) interval, (Double) expected.getInterval());
//		assertEquals(expected, parsed);	
//	}
//
//	@Test
//	public void testMinSyn() throws ParseException, RelationshipException {
//		input = "Eb = C -1/7S";
//		adj = new Adjustment("-1/7S");
//		expected = new Relationship("Eb", "C", adj);
//		parsed = new Relationship(input);
//		System.out.println(parsed);
//
//		interval = (6/5.0)*adj.toDouble();
//		assertEquals((Double) interval, (Double) expected.getInterval());
//		assertEquals(expected, parsed);	
//	}
//
//	// Now time for exceptions: ***************************************
//	// ParseException ************************
//
//	@Test (expected = ParseException.class)
//	public void testJunk() throws ParseException, RelationshipException {
//		input = "alkdfj";
//		parsed = new Relationship(input);
//		fail(parsed + "shouldn't exist.");
//	}
//
//	@Test (expected = ParseException.class)
//	public void testFrontJunk() throws ParseException, RelationshipException {
//		input = ",sk A = E";
//		parsed = new Relationship(input);
//		fail(parsed + "shouldn't exist.");
//	}
//
//	@Test (expected = ParseException.class)
//	public void testBadEquals() throws ParseException, RelationshipException {
//		input = "A == E";
//		parsed = new Relationship(input);
//		fail(parsed + "shouldn't exist.");
//	}
//
//	@Test (expected = ParseException.class)
//	public void testBadNotes() throws ParseException, RelationshipException {
//		input = "H = I";
//		parsed = new Relationship(input);
//		fail(parsed + "shouldn't exist.");
//	}
//
//	@Test (expected = ParseException.class)
//	public void testBadAdj() throws ParseException, RelationshipException {
//		input = "F = A =1/6S";
//		parsed = new Relationship(input);
//		fail(parsed + "shouldn't exist.");
//	}
//
//	@Test (expected = ParseException.class)
//	public void testExtraPM() throws ParseException, RelationshipException {
//		input = "G = C+-1/6S";
//		parsed = new Relationship(input);
//		fail(parsed + "shouldn't exist.");
//	}
//
//	@Test (expected = ParseException.class)
//	public void testMissingComma() throws ParseException, RelationshipException {
//		input = "F = C +1/6";
//		parsed = new Relationship(input);
//		fail("SHOULDN'T EXIST!: " + parsed);
//	}
//
//	@Test (expected = ParseException.class)
//	public void testNoFraction() throws ParseException, RelationshipException {
//		input = "A = F -0.25S";
//		parsed = new Relationship(input);
//		fail(parsed + "shouldn't exist.");
//	}
//
//	@Test (expected = ParseException.class)
//	public void testBadFraction() throws ParseException, RelationshipException {
//		input = "A = F +25P/32S";
//		parsed = new Relationship(input);
//		fail(parsed + "shouldn't exist.");
//	}
//
//	@Test (expected = ParseException.class)
//	public void testMissingFraction() throws ParseException, RelationshipException {
//		input = "A = F +P";
//		parsed = new Relationship(input);
//		fail(parsed + "shouldn't exist.");
//	}
//
//	@Test (expected = ParseException.class)
//	public void testOnlyComma() throws ParseException, RelationshipException {
//		input = "A = F S";
//		parsed = new Relationship(input);
//		fail(parsed + "shouldn't exist.");
//	}
//
//	@Test (expected = ParseException.class)
//	public void testDoubleComma() throws ParseException, RelationshipException {
//		input = "A = F +1/6PP";
//		parsed = new Relationship(input);
//		fail(parsed + "shouldn't exist.");
//	}
//
//	@Test (expected = ParseException.class)
//	public void testEndJunk() throws ParseException, RelationshipException {
//		input = "A = F +1/6P,";
//		parsed = new Relationship(input);
//		fail(parsed + "shouldn't exist.");
//	}
//
//	// Now RelationshipException ***********************************
//
//	@Test (expected = RelationshipException.class)
//	public void testBadRel() throws ParseException, RelationshipException {
//		input = "A = Bb";
//		parsed = new Relationship(input);
//		fail(parsed + "shouldn't exist.");
//	}
//
//	@Test (expected = RelationshipException.class)
//	public void testBadRel2() throws ParseException, RelationshipException {
//		input = "A = A +1/6P";
//		parsed = new Relationship(input);
//		fail(parsed + "shouldn't exist.");
//	}
//
//	
//
//
//}
