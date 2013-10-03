package com.thefourthspecies.ttuner.model.tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.thefourthspecies.ttuner.model.data.Frequencies;
import com.thefourthspecies.ttuner.model.data.Note;
import com.thefourthspecies.ttuner.model.exceptions.TemperamentException;
import com.thefourthspecies.ttuner.model.listables.CentsTemperament;
import com.thefourthspecies.ttuner.model.listables.Temperament;

public class CentsTemperamentTests {

	CentsTemperament eqt;
	CentsTemperament val;

	List<Double> VALDEVS = Arrays.asList(0.0, 5.9, -3.9, 5.9, 0.0, 2.0,
										3.9, -2.0, 7.8, -2.0, 3.9, 2.0);
	String VALDETS = "A+0.0, Bb+5.9, B-3.9, C+5.9, C#+0.0, D+2.0, " +
					"Eb+3.9, E-2.0, F+7.8, F#-2.0, G+3.9, G#+2.0";

	List<Double> EQFREQS = Arrays.asList(440.0, 466.2, 493.9, 523.3, 554.4, 587.3, 
            622.3, 659.3, 698.5, 740.0, 784.0, 830.6);
	List<Double> VALFREQS = Arrays.asList(440.0, 467.8, 492.8, 525.0, 554.4, 588.0,
             623.7, 658.5, 701.6, 739.1, 785.8, 831.6);

//	List<Double> EQFREQS = Arrays.asList(440.00, 466.16, 493.88, 523.25, 554.37, 587.33, 
//			                             622.25, 659.26, 698.46, 739.99, 783.99, 830.61);
//	List<Double> VALFREQS = Arrays.asList(440.00, 467.75, 492.77, 525.03, 554.37, 588.00,
//			                              623.66, 658.51, 701.63, 739.16, 785.77, 831.55);
	double PITCH = 440;

	List<Double> devs;
	String dets; 
	List<Double> freqs;
	List<Note> notes;
	
	@Before
	public void setup() {
		eqt = new CentsTemperament();
		val = new CentsTemperament();
	}
	
	@Test
	public void testGetScale() throws TemperamentException {
		notes = eqt.getScale(PITCH);
		freqs = Frequencies.getAll(notes);
		freqs = roundList(freqs);
		assertEquals(EQFREQS, freqs);
	
		val.setDeviations(VALDEVS);
		assertEquals(VALDEVS, val.getDeviations());
		
		assertEquals(VALFREQS, getFreqs(val));
		
	}
	
	@Test
	public void testSetDeviations() throws TemperamentException {
		val.setDeviations(VALDEVS);
		assertEquals(VALDEVS, val.getDeviations());
		
		assertEquals(VALFREQS, getFreqs(val));	
	}

	@Test
	public void testGetDetails() throws TemperamentException {
		val.setDeviations(VALDEVS);
		assertEquals(VALDETS, val.getDetails());
	}
	
	
	@Test
	public void testSetDetails() throws TemperamentException {
		val.setDetails(VALDETS);
		assertEquals(VALDETS, val.getDetails());
		assertEquals(VALFREQS, getFreqs(val));
	}
	
	@Test
	public void testGetCopy() {
		Temperament bob =  (Temperament) val.getCopy();
		assertEquals(val, bob);
	}
	
	
	
	
	private List<Double> getFreqs(CentsTemperament temp) {
		List<Note> notes = temp.getScale(PITCH);
		List<Double> freqs = Frequencies.getAll(notes);
		return roundList(freqs);
	}
	private List<Double> roundList(List<Double> l) {
		List<Double> f = new ArrayList<Double>(l);
		for (int i=0; i<f.size(); i++) {
			Double x = f.get(i)*10;
			x = Math.rint(x);
			f.set(i, x/10);
		}
		return f;
	}
	
}
