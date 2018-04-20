package com.thefourthspecies.ttuner.model;


import java.util.ArrayList;
import java.util.List;

import com.thefourthspecies.ttuner.model.exceptions.TunerException;
import com.thefourthspecies.ttuner.model.listables.Listable;
import com.thefourthspecies.ttuner.model.listables.Temperament;
import com.thefourthspecies.ttuner.model.listables.TemperamentFactory;
import com.thefourthspecies.ttuner.model.listables.Waveform;

/**
 * Where we keep the registered components.
 * It consists of a bunch of default values that are generally only
 * used if the store is empty.
 * @author graham
 *
 */
public class Registry {
	
	private List<Temperament> temperaments;
	private List<Waveform> waveforms;
	
	private static List<Temperament> defaultTemperaments = new ArrayList<Temperament>();
	private static List<Waveform> defaultWaveforms = new ArrayList<Waveform>();
	
	/**
	 * Now, to Hardcode a bunch of default Temperaments and Waveforms, mostly courtesy
	 * of the original ttuner by Jon-o. I'll put them into special defaults lists
	 * and then they can be retrieved later if a user wants to reset everything.
	 * 
	 * Might have been better to make this non-static, so that I can choose whether or not
	 * to load these (based on, perhaps, whether there is something in the TunerStore,
	 * or whether I indicate "reset"), but, oh well.
	 */
	static {
		addDefaultTemperament("Equal Temperament",
				"A=E-1/12P, B=F#-1/12P, F#=C#-1/12P, E=B-1/12P, D=A-1/12P, G=D-1/12P, " +
				"C=G-1/12P, F=C-1/12P, Bb=F-1/12P, Eb=Bb-1/12P, Ab=Eb-1/12P");
		addDefaultTemperament("Equal Temperament (by cents)",
				"A+0.0, Bb+0.0, B+0.0, C+0.0, C#+0.0, D+0.0, " +
				"Eb+0.0, E+0.0, F+0.0, F#+0.0, G+0.0, G#+0.0");
	
		addDefaultTemperament("Just Intonation, A Major", 
				"A=E, A=C#, E=B, E=G#, D=A, D=F#");
		
		addDefaultTemperament("Kirnberger III",
				"C=E, C=G-1/4S, G=D-1/4S, D=A-1/4S, A=E-1/4S, " +
				"E=B, B=F#, Db=Ab, Ab=Eb, Eb=Bb, Bb=F, F=C");
		
		addDefaultTemperament("Bach/Lehman 1722",
				"C = F-1/6P, G = C-1/6P, D = G-1/6P, A = D-1/6P, " +
				"E = A-1/6P, B = E, F# = B, C# = F#, G# = C#-1/12P, " +
				"D# = G#-1/12P, A# = D#-1/12P");

		addDefaultTemperament("Bach/Lehman Chorale",
				"D = G-1/6P, A = D-1/6P, E = A-1/6P, B = E-1/6P, " +
				"F# = B-1/6P, C# = F#, G# = C#, D# = G#, Bb = Eb-1/12P, " +
				"F = Bb-1/12P, C = F-1/12P");
		
		addDefaultTemperament("Meantone, 1/4 Comma",
				"A = F, Bb = D, B = G, B= E-1/4S, C = E, C# = A, C#= F#-1/4S, D = F#, " +
						"D = Bb, Eb = G, E = C, E=A-1/4S, F = A, F# = D, F#=B-1/4S, G = Eb, " +
				"G=B, G# = E, Ab = C, D# = B");

		addDefaultTemperament("Meantone, 1/5 Comma", 
				"A = E-1/5S, E = B-1/5S, D = A-1/5S, G = D-1/5S, C = G-1/5S, F = C-1/5S, " +
				"Bb = F-1/5S, Eb = Bb-1/5S, Ab = Eb-1/5S, F# = B-1/5S, C# = F#-1/5S");

		addDefaultTemperament("Meantone, 1/6 Comma",
				"A = E-1/6S, E = B-1/6S, D = A-1/6S, G = D-1/6S, C = G-1/6S, " +
				"F = C-1/6S, Bb = F-1/6S, Eb = Bb-1/6S, Ab = Eb-1/6S, " +
				"F# = B-1/6S, C# = F#-1/6S, G# = C#-1/6S");

		addDefaultTemperament("Pythagorean",
				"A=E, E=B, B=F#, F#=C#, C#=G#, D=A, G=D, C=G, F=C, Bb=F, Eb=Bb");
		
		addDefaultTemperament("Sorge 1758",
				"C = G-1/6P, C# = G#, D = A-1/6P, Eb = Bb-1/12P, E = B, F = C, F# = C#-1/12P, " +
				"G = D-1/6P, Ab = Eb-1/12P, A = E-1/12P, B = F#-1/12P, Bb = F-1/12P");

		addDefaultTemperament("Valotti",
				"A = E-1/6P, E = B-1/6P, D = A-1/6P, G = D-1/6P, C = G-1/6P, F = C-1/6P, " +
				"Bb = F, Eb = Bb, Ab = Eb, F# = B, C# = F#");
		addDefaultTemperament("Valotti (by cents)", 
				"A+0.0, Bb+5.9, B-3.9, C+5.9, C#+0.0, D+2.0, " +
				"Eb+3.9, E-2.0, F+7.8, F#-2.0, G+3.9, G#+2.0");
		
		addDefaultTemperament("Werckmeister III",
				"A = E, E = B, B = F#-1/4P, D = A-1/4P, G = D-1/4P, C = G-1/4P, " +
				"F = C, Bb = F, Eb = Bb, Ab = Eb, C# = F#");
		
		addDefaultTemperament("Woods So Wild",
				"Eb = Bb-1/6S, Bb = F-1/6S, F = C-1/4S, C = G-1/4S, G = D, D = A, " +
				"A = E-1/6S, E = B-1/4S, B = F#-1/3S, F# = C#, C# = G#");
		addDefaultTemperament("Woods So Wild (by cents)",
				"A+0.0, Bb+6, B-5, C+0, C#-10, D-2, Eb+8, E-2, F+4, F#-12, G-4, G#-8");
		
		addDefaultTemperament("Young II",
				"C=G-1/6P, G=D-1/6P, D=A-1/6P, A=E-1/6P, E=B-1/6P," +
				"C=F, F=Bb, Bb=Eb, Eb=Ab, Ab=Db, Db=Gb, B=Gb-1/6P");
		

		addDefaultWaveform("Splendid Tone",
				"8, 4, 2, 1,.5,.25,.125,.0625");
		addDefaultWaveform("Sawtooth",
				"1,1,1,1,1,1,1,1,1,1,1,1,1," +
				"1,1,1,1,1,1,1,1,1,1,1,1,1");
		addDefaultWaveform("Simple Sine", "1");
		addDefaultWaveform("Square",
				"8, 0, 4, 0, 2, 0, 1, 0, .5, 0, .25, 0, .125, 0, .0625");
		addDefaultWaveform("Strict Square", 
				"1,0,1,0,1,0,1,0,1,0,1,0,1," +
				"0,1,0,1,0,1,0,1,0,1,0,1,0");
	}
	
	
	public Registry(){
		temperaments = new ArrayList<Temperament>(defaultTemperaments);
		waveforms = new ArrayList<Waveform>(defaultWaveforms); 
	}
	
	/**
	 * Returns a copy of the list Temperaments, as Listables.
	 */
	public List<Listable> getTemperaments() { 
		return new ArrayList<Listable>(temperaments);
	}
	/**
	 * Adds a Temperament to the Registry.
	 */
	public void addTemp(Temperament t) {
		temperaments.add(t);
//		System.out.println("REGISTERING TEMPERAMENT: " + t.getName() + ", " + t.getRelationships());
	}
	/**
	 * Removes the temperament at position in the list from the registry
	 */
	public void deleteTemp(int position) {
		temperaments.remove(position);
	}
	/**
	 * Returns the ith teperament in the Registry 
	 */
	public Temperament getTemp(int i) {
		return temperaments.get(i);
	}
	/**
	 * Erases all temperaments
	 */
	public void clearTemperaments() {
		temperaments.clear();
	}
	/**
	 * Adds to the list of temperaments in the Registry the contents of another list of temperaments
	 * Throws esception if there are non-temperaments tucked in there
	 */
	public void addTemperaments(List<Listable> temps) {
		for (Listable t : temps) {
			addTemp((Temperament) t);
		}
	}
	
	/**
	 * Returns a copy of the list Waveforms, as Listables.
	 */	
	public List<Listable> getWaveforms() {
		return new ArrayList<Listable>(waveforms);
	}
	/**
	 * Adds a Waveform to the Registry.
	 */
	public void addWave(Waveform w) {
		waveforms.add(w);
//		System.out.println("REGISTERING WAVEFORM: " + w.getName() + ", " + w.getHarmonics());
	}
	
	/**
	 * Removes the waveform at position from Registry
	 */ 
	public void deleteWave(int position) {
		waveforms.remove(position);
	}
	/**
	 * Return the waveform at position i
	 */
	public Waveform getWave(int i) {
		return waveforms.get(i);
		
	/**
	 * Remove all waveforms from registy
	 */
	}public void clearWaveforms() {
		waveforms.clear();
	}
	
	/**
	 * Append this list of waveforms to the registry, returning exception if there are
	 * non-Waveforms in the list
	 */
	public void addWaveforms(List<Listable> waves) {
		for (Listable w : waves) {
			addWave((Waveform) w);
		}
	}
	
	
	/**
	 * Creates and adds a temperament with given name and details (a string of relationships), in default status, to the registry
	 */
	static private void addDefaultTemperament(String name, String details) {
		try {
			Temperament t = TemperamentFactory.createTemperament(name, details);
			t.makeDefault();
			defaultTemperaments.add(t);
		} catch (TunerException e) {
			throw new IllegalStateException("Error initializing Registry: " + e.getMessage());
		}
	}

	/**
	 * Creates and add a Wavefomr with given name and list of weights, in default status, to the registry
	 */
	static private void addDefaultWaveform(String name, String weights) {
		try {
			Waveform w = new Waveform(name, weights);
			w.makeDefault();
			defaultWaveforms.add(w);
		} catch (TunerException e) {
			throw new IllegalStateException("Error initializing Registry: " + e.getMessage());
		}
	}

}
