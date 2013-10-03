package com.thefourthspecies.ttuner.model.tests;
//package graham.ttuner.model.tests;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import graham.ttuner.model.data.Frequencies;
//import graham.ttuner.model.data.Note;
//import graham.ttuner.model.data.NotePool;
//import graham.ttuner.model.exceptions.FrequencyException;
//import graham.ttuner.model.exceptions.ParseException;
//import graham.ttuner.model.exceptions.TunerException;
//import graham.ttuner.model.listables.RelationshipsTemperament;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import org.junit.Test;
//
//
//public class FrequenciesTests {
//	final private static double middleC = 261.6;
//	
//	RelationshipsTemperament t;
//	List<Note> n;
//	String s;
//	List<Double> expected;
//	List<Double> resulted;
//	
//	
//	@Test
//	public void testGetAll() throws ParseException, FrequencyException {
//		NotePool np = new NotePool();
//		np.add("A");
//		np.add("B");
//		np.add("C");
//		np.get("A").setFreq(100);
//		np.get("B").setFreq(200);
//		np.get("C").setFreq(200);
//		
//		expected = new ArrayList<Double>(Arrays.asList(100.0, 200.0, 200.0));
//		resulted = Frequencies.getAll(np);
//		Collections.sort(resulted);
//		assertEquals(expected, resulted);
//	}
//
//	
//	@Test
//	public void testMakeScaleFreqEQT() 
//			throws TunerException {
//		s = "A=E-1/12P, B=F#-1/12P, F#=C#-1/12P, E=B-1/12P, D=A-1/12P, "
//			+ "G=D-1/12P, C=G-1/12P, F=C-1/12P, Bb=F-1/12P, Eb=Bb-1/12P, Ab=Eb-1/12P";	
//		t = new RelationshipsTemperament(s);
//		
//		n = Frequencies.makeScale(t, 440);
//				
////		For version where octave begins at middle C
////		expected = Arrays.asList(261.63, 277.18, 293.66, 311.13, 329.63, 349.23, 
////				369.99, 392.00, 415.30, 440.00, 466.16, 493.88);
//
//		expected = Arrays.asList(440.00, 466.16, 493.88, 523.25, 554.37, 587.33, 622.25, 
//				659.26, 698.46, 739.99, 783.99, 830.61);
//
//		
//		resulted = roundList(Frequencies.getAll(n));
//		
//		assertEquals(expected, resulted);
//	}
//
//	
//	@Test
//	public void testAssignFreqMeanT() 
//			throws TunerException  {
//		s = "A = F, Bb = D, B = G, B= E-1/4S, C = E, C# = A, C#= F#-1/4S, D = F#, "
//				+ "D= Bb, Eb = G, E = C, E=A-1/4S, F = A, F# = D, F#=B-1/4S, G = Eb, "
//				+ "G=B, G# = E, Ab = C, D# = B";
//				
//		t = new RelationshipsTemperament(s);
//
//		n = Frequencies.makeScale(t, 415);
//		
////		List<Note> n1 = Arrays.asList(note("C"), note("C#"), note("D"), note("D#"),
////				note("Eb"), note("E"), note("F"), note("F#"), note("G"), note("G#"), 
////				note("Ab"), note("A"), note("Bb"), note("B"));
//		List<Note> n1 = Arrays.asList(note("A"), note("Bb"), note("B"), note("C"), 
//				note("C#"), note("D"), note("D#"), note("Eb"), note("E"), note("F"),
//				note("F#"), note("G"), note("G#"), note("Ab"));
//
//		
//		
//		assertEquals(n1, n);
//
//		
////		expected = Arrays.asList(248.23, 259.38, 277.53, 289.99, 296.95, 310.28, 332.0, 346.91,
////						371.19, 387.86, 397.16, 415.0, 444.04, 463.98);
//		
//		expected = Arrays.asList(415.0, 444.04, 463.98, 496.46, 518.75, 555.05, 
//					579.98, 593.90, 620.57, 664.00, 693.82, 742.37, 775.71, 794.33);
//
//		
//		resulted = roundList(Frequencies.getAll(n));
//		assertEquals(expected, resulted);
//	}
//
//	
//	@Test
//	public void testDegenerate() throws TunerException {
//		s = "D = F, C = G";
//		t = new RelationshipsTemperament(s);
//		n = Frequencies.makeScale(t, 415);
//		NotePool np = new NotePool(n);
//		assertFalse(np.isComplete());
//		assertEquals(4, np.size());
//	}
//	
////	@Test
////	public void testEmpty() throws FrequencyException {
////		t = new Temperament();
////		Frequencies.makeScale(t, 440);		
////		assertEquals(new ArrayList<Note>(), t);
////	}
//	
////This is unnecessary now
////	@Test
////	public void testNoC() 
////			throws RelationshipException, ParseException, TemperamentException, FrequencyException {
////		
////		s = "F=A";
////		t = new Temperament(s);
////		n = Frequencies.makeScale(t, 440);
////		
////		expected = Arrays.asList(352.0, 440.0);
////		resulted = Frequencies.getAll(n);
////		
////		System.out.println(expected);
////		System.out.println(resulted);
////		
//////		assertEquals(expected, resulted);
////	}
//	
//	private static Note note(String s) {
//		try {
//			return new Note(s);
//		} catch (ParseException e) {
//			throw new RuntimeException();
//		} 
//	}
//	
//	private List<Double> roundList(List<Double> l) {
//		List<Double> f = new ArrayList<Double>(l);
//		for (int i=0; i<f.size(); i++) {
//			Double x = f.get(i)*100;
//			x = Math.rint(x);
//			f.set(i, x/100);
//		}
//		return f;
//	}
//	
//}
//
//
////double freq;
////double norm;
//
////	
////	// Testing withins:
////	@Test
////	public void testWithinLow() {
////		freq = MIN;
////		norm = Frequencies.normalize(freq);
////		assertTrue(norm == freq);
////	}
////
////	@Test
////	public void testWithinHigh() {
////		freq = MAX - 0.00000001;
////		norm = Frequencies.normalize(freq);
////		assertTrue(norm == freq);
////	}
////
////	@Test
////	public void testWithin() {
////		freq = MID;
////		norm = Frequencies.normalize(freq);
////		assertTrue(norm == freq);
////	}
////
////	// Testing highs:
////	@Test
////	public void testHighBoundary() {
////		freq = MAX;
////		norm = Frequencies.normalize(freq);
////		assertEquals((Double) MIN, (Double) norm);
////	}
////
////	@Test
////	public void testVeryHigh() {
////		freq = MID*1024;
////		norm = Frequencies.normalize(freq);
////		assertEquals((Double) MID, (Double) norm);
////	}
////	
////	@Test
////	public void testReasonablyHigh() {
////		freq = MID*32;
////		norm = Frequencies.normalize(freq);
////		assertEquals((Double) MID, (Double) norm);
////	}
////
////	// Testing lows:
////	@Test
////	public void testLowBoundary() {
////		freq = MIN - 0.00000001;
////		norm = Frequencies.normalize(freq);
////		assertTrue(norm == freq*2);
////	}
////	@Test
////	public void testVeryLow() {
////		freq = MID/1024;
////		norm = Frequencies.normalize(freq);
////		assertEquals((Double) MID, (Double) norm);
////	}
////	@Test
////	public void testReasonablyLow() {
////		freq = MID/8;
////		norm = Frequencies.normalize(freq);
////		assertEquals((Double) MID, (Double) norm);
////	}
////	
////	
////	//**************************************
////	// Testing temperaments!
////
////	List<Double> f;
////	List<Double> l;
////	
////	@Before
////	public void setUp() throws RelationshipException, ParseException {
////		Note.deleteAll();
////		l = new ArrayList<Double>();
////	}
////	
////
////	
////	@Test
////	public void testGetAllNone() {
////		f = Frequencies.getAll();
////		assertEquals(0, f.size());
////	}
////	
////	@Test (expected = NullPointerException.class)
////	public void testGetAllEmpty() throws FrequencyException {
////		Note.get("A");
////		Note.get("B").setFreq(100.0);
////		f = Frequencies.getAll();
////	}
////	
////	@Test
////	public void testGetAll() throws FrequencyException {
////		Note.get("A").setFreq(100.0);
////		Note.get("B").setFreq(200.0);
////		Note.get("C").setFreq(50.0);
////		Note.get("D").setFreq(300.0);
////		Note.get("E").setFreq(200.0);
////		Note.get("F").setFreq(500.0);
////		Note.get("G").setFreq(600.0);
////		
////		l.add(50.0);
////		l.add(100.0);
////		l.add(200.0);
////		l.add(200.0);
////		l.add(300.0);
////		l.add(500.0);
////		l.add(600.0);
////		
////		assertEquals(l, Frequencies.getAll());	
////	}
////	
////	
////	// ***************************************************************
////	// Testing Temperament stuff!
////
////	Temperament t;
////	String s;
////	
////	Note x;
////		
////	
////	@Test (expected = FrequencyException.class)
////	public void testCalculateUninitilialized() throws FrequencyException, RelationshipException, ParseException {
////		s = "A=E";
////		t = new Temperament(s);
////		Frequencies.calculate(t);
////	}
////	
////	@Test
////	public void testCalculateSimple() throws RelationshipException, ParseException, FrequencyException {
////		s = "A=E";
////		t = new Temperament(s);
////		
////		Note.get("A").setFreq(440);
////		Frequencies.calculate(t);
////		
////		l.add(330.0);
////		l.add(440.0);
////		
////		assertEquals(l, Frequencies.getAll());
////	}
////	
////		
//
//
////	
////	@Test
////	public void stuff() throws FrequencyException {
////		Note a = Note.get("A");
////		a.setFreq(440);
////		System.out.println(a);
////		Note.deleteAll();
////		System.out.println(a);
////		
////	}
////	
//
////	
////}
