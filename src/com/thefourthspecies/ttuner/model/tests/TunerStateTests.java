package com.thefourthspecies.ttuner.model.tests;
//package graham.ttuner.model.tests;
//
//import static org.junit.Assert.*;
//
//import graham.ttuner.model.Note;
//import graham.ttuner.model.Temperament;
//import graham.ttuner.model.TunerState;
//import graham.ttuner.model.Waveform;
//import graham.ttuner.model.exceptions.ParseException;
//import graham.ttuner.model.exceptions.RelationshipException;
//import graham.ttuner.model.exceptions.TemperamentException;
//import graham.ttuner.model.exceptions.WaveformException;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import org.junit.Before;
//import org.junit.Test;
//
//
//public class TunerStateTests {
// 
//	double DELTA = 0.01;
//	
//	String mttString = "A = F, Bb = D, B = G, B= E-1/4S, C = E, C# = A, C#= F#-1/4S, D = F#, "
//			+ "D= Bb, Eb = G, E = C, E=A-1/4S, F = A, F# = D, F#=B-1/4S, G = Eb, "
//			+ "G=B, G# = E, Ab = C, D# = B";
//	
//	String eqtString = "A=E-1/12P, B=F#-1/12P, F#=C#-1/12P, E=B-1/12P, D=A-1/12P, "
//			+ "G=D-1/12P, C=G-1/12P, F=C-1/12P, Bb=F-1/12P, Eb=Bb-1/12P, Ab=Eb-1/12P";	
//
//	// at A = 440;
////	double[] eqtFreqs =	{ 
////			261.63, 277.18, 293.66, 311.13, 329.63, 349.23, 
////			369.99, 392.00, 415.30, 440.00, 466.16, 493.88 
////	};
//	double[] eqtFreqs =	{ 
//			440.00, 466.16, 493.88, 523.25, 554.37, 587.33,
//			622.25, 659.26, 698.46, 739.99, 783.99, 830.61
//	};
//
//
//	
//	
//	// at A = 415;
////	double[] mttFreqs = {
////			248.23, 259.38, 277.53, 289.99, 296.95, 310.28, 332.00, 
////			346.91, 371.19, 387.86, 397.16, 415.00, 444.04, 463.98
////	};
//	double[] mttFreqs = {
//			415.0, 444.04, 463.98, 496.46, 518.75, 555.05, 579.98,
//			593.90, 620.57, 664.00, 693.82, 742.37, 775.71, 794.33
//	};
//
//	
//	List<Double> niceWave = Arrays.asList(8.0,4.0,2.0,1.0,.5,.25,.125,.0625);
//	
//	Temperament eqt;
//	Temperament mtt;
//	Waveform wave;
//	
//	TunerState state = TunerState.getInstance();
//
//
//	@Before
//	public void setUp() throws RelationshipException, ParseException, TemperamentException, WaveformException {
//		eqt = new Temperament(eqtString);
//		mtt = new Temperament(mttString);
//		wave = new Waveform(niceWave);
//		
//		state.reset();
//	}
//	
//	@Test
//	public void testGets() throws ParseException {
//		assertEquals(440.0, state.getFrequency(), DELTA);
//		assertEquals(new Note("A"), state.getNote());
//		assertEquals(440.0, state.getPitchA(), DELTA);
//		assertEquals(eqt, state.getTemperament());
//		assertEquals(wave, state.getWaveform());
//	}
//	
//	@Test
//	public void testNoteChangers() throws TemperamentException, ParseException {
//		state.setNote("F#");
//		assertEquals(eqtFreqs[9], state.getFrequency(), DELTA);
//		
//		state.increaseNote();
//		assertEquals(eqtFreqs[10], state.getFrequency(), DELTA);
//		
//		state.decreaseNote();
//		state.decreaseNote();
//		assertEquals(eqtFreqs[8], state.getFrequency(), DELTA);
//		
//		state.increaseOctave();
//		assertEquals(2*eqtFreqs[8], state.getFrequency(), DELTA);
//		
//		state.decreaseOctave();
//		state.decreaseOctave();
//		assertEquals(eqtFreqs[8]/2, state.getFrequency(), DELTA);
//		state.increaseOctave();
//		
//		state.setNote("A");
//		assertEquals(eqtFreqs[0], state.getFrequency(), DELTA);
//		
//		state.decreaseNote();
//		assertEquals(eqtFreqs[11]/2, state.getFrequency(), DELTA);
//		
//		state.increaseNote();
//		assertEquals(eqtFreqs[0], state.getFrequency(), DELTA);
//		
//		state.setNote("Ab");
//		assertEquals(eqtFreqs[11], state.getFrequency(), DELTA);
//		
//		state.increaseNote();
//		assertEquals(2*eqtFreqs[0], state.getFrequency(), DELTA);
//	}
//	
//	@Test
//	public void testSetters() throws TemperamentException, ParseException, WaveformException {
//		state.setNote("A");
//		assertEquals(eqtFreqs[0], state.getFrequency(), DELTA);
//		
//		state.setTemperament(mtt);
//		assertEquals(mtt, state.getTemperament());
//	
//		assertEquals(440.0, state.getFrequency(), DELTA);
//		state.setNote("C");
//		assertNotSame(mttFreqs[3], state.getFrequency());
//		assertNotSame(eqtFreqs[3], state.getFrequency());
//		
//		
//		state.setPitchA(415);
//		assertEquals(mttFreqs[3], state.getFrequency(), DELTA);
//		
//		state.setNote("A");
//		assertEquals(415.0, state.getFrequency(), DELTA);
//		
//		state.setNote("C");
//		state.setPitchA(440);
//		assertNotSame(mttFreqs[3], state.getFrequency());
//		assertNotSame(eqtFreqs[3], state.getFrequency());
//		state.setTemperament(eqt);
//		assertEquals(eqtFreqs[3], state.getFrequency(), DELTA);
//		
//
//		Waveform w = new Waveform(Arrays.asList(1.0,2.0,3.0));
//		state.setWaveform(w);
//		assertEquals(w, state.getWaveform());	
//	}
//	
//	@Test
//	public void testNoNote() throws TemperamentException, ParseException, RelationshipException {
//		state.setTemperament(mtt);
//		state.setNote("D#");
//		assertEquals(new Note("D#"), state.getNote());
//		
//		state.setPitchA(415);
//		state.setTemperament(eqt);
//		assertEquals(new Note("Eb"), state.getNote());
//		
//		state.setTemperament(mtt);
//		assertEquals(new Note("Eb"), state.getNote());
//		state.setNote("G#");
//		state.setTemperament(eqt);
//		assertEquals(new Note("Ab"), state.getNote());
//		
//		state.setNote("E");
//		state.setTemperament(new Temperament("A=C"));
//		assertEquals(new Note("C"), state.getNote());
//	}
//	
//	
//	
//}
