package com.thefourthspecies.ttuner.model.tests;
//package graham.ttuner.model.tests;
//
//import static org.junit.Assert.assertEquals;
//import graham.ttuner.model.Registry;
//import graham.ttuner.model.listables.Listable;
//import graham.ttuner.model.listables.RelationshipsTemperament;
//import graham.ttuner.model.listables.Waveform;
//
//import java.util.Arrays;
//import java.util.List;
//
//import org.junit.Before;
//import org.junit.Test;
//
//public class RegistryTests {
//
//	Registry reg;
//	int NUM_TEMPS = 10;
//	int NUM_WAVES = 4;
//	
//	List<Listable> temps;
//	List<Listable> waves;
//	
//	String eqDets = "A = E -1/12P, B = F# -1/12P, F# = C# -1/12P, E = B -1/12P, " +
//			"D = A -1/12P, G = D -1/12P, C = G -1/12P, F = C -1/12P, Bb = F -1/12P, " +
//			"Eb = Bb -1/12P, Ab = Eb -1/12P";
//
//	String mtDets = "F = A, Bb = D, G = B, E = B -1/4S, C = E, A = C#, F# = C# -1/4S, D = F#, "
//				+ "Eb = G, A = E -1/4S, B = F# -1/4S, E = G#, Ab = C, B = D#";
//
//	String fiDets = "A = E -1/5S, E = B -1/5S, D = A -1/5S, G = D -1/5S, C = G -1/5S, F = C -1/5S, "  
//		+ "Bb = F -1/5S, Eb = Bb -1/5S, Ab = Eb -1/5S, B = F# -1/5S, F# = C# -1/5S";
//
//	String splenDets = "8, 4, 2, 1, .5, .25, .125, .0625";
//	String sawDets = "1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1";
//	String sinDets = "1";
//	String sqDets = "8, 0, 4, 0, 2, 0, 1, 0, .5, 0, .25, 0, .125, 0, .0625";
//	
//	@Before
//	public void setUp() {
//		reg = new Registry();
//		temps = reg.getTemperaments();
//		waves = reg.getWaveforms();
//	}
//	
//	@Test
//	public void testConstructor() {
//		assertEquals(NUM_TEMPS, temps.size());
//		assertEquals(NUM_WAVES, waves.size());
//	}
//	
//	@Test
//	public void testDefaultTemperaments() {
//		RelationshipsTemperament t = reg.getTemp(0);
//		String name = "Equal Temperament";
//		String dets = eqDets;
//		assertEquals(name, t.getName());
//		assertEquals(dets, t.getDetails());
//	
//		t = reg.getTemp(2);
//		name = "Meantone, 1/4 Comma";
//		dets = mtDets;
//		assertEquals(name, t.getName());
//		assertEquals(dets, t.getDetails());
//	
//		t = reg.getTemp(3);
//		name = "Meantone, 1/5 Comma";
//		dets = fiDets;
//		assertEquals(name, t.getName());
//		assertEquals(dets, t.getDetails());
//	}
//	
//	@Test
//	public void testDefaultWaves() {
//		Waveform w = reg.getWave(0);
//		String name = "Splendid Tone";
//		String dets = splenDets;
//		assertEquals(name, w.getName());
//		assertEquals(dets, w.getDetails());
//	
//		w = reg.getWave(1);
//		name = "Sawtooth";
//		dets = sawDets;
//		assertEquals(name, w.getName());
//		assertEquals(dets, w.getDetails());
//
//		w = reg.getWave(2);
//		name = "Simple Sine";
//		dets = sinDets;
//		assertEquals(name, w.getName());
//		assertEquals(dets, w.getDetails());
//
//		w = reg.getWave(3);
//		name = "Square";
//		dets = sqDets;
//		assertEquals(name, w.getName());
//		assertEquals(dets, w.getDetails());
//	}
//}
