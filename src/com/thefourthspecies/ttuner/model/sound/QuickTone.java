package com.thefourthspecies.ttuner.model.sound;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.thefourthspecies.ttuner.model.exceptions.WaveformException;
import com.thefourthspecies.ttuner.model.listables.Waveform;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * Streams, but with builtin Sine-like function for speed
 * Currently, does not buffer in period-sized chunks, so it stops
 * mid-wave at times, which, I believe causes a click. Future versions
 * will change that. You can change the pitch or waveform mid-play though
 * without any audible distortion.
 * 
 * Creates a huge array (SINE_SIZE) and fills it with the sine values  for a complete cycle, thus
 * corresponding to wave displacements. Then uses a list of the nested class Partial to calculate the 
 * displacement data point for whatever frame we are currently in in the Stream. It uses a trick. 
 * 
 * This is probably the best I can do (best I can thin of, at least)
 * without using the NDK and OpenSL (see http://code.google.com/p/high-performance-audio/)
 * @author graham
 *
 */

public class QuickTone implements Tone {

	// Some Audio constants:
	final static int STREAM_TYPE = AudioManager.STREAM_MUSIC;
	final static int SAMPLE_RATE = 44100;  // In Hz.
	final static int CHANNEL = AudioFormat.CHANNEL_OUT_MONO;
	final static int FORMAT = AudioFormat.ENCODING_PCM_16BIT;
	final static int MODE = AudioTrack.MODE_STREAM;

	// Takes the minimum buffer size and multiplies it by this to make the size in the AudioTrack
	// When starting a new tone, it always fills up the audiotrack before playing, so the bigger this
	// is, the bigger the lag, but the less likely there will be interruptions.
	final static int SIZE_MULTIPLIER = 5;
	final static int MIN_BUFFER = AudioTrack.getMinBufferSize(SAMPLE_RATE, CHANNEL, FORMAT); // In Bytes.
	final static int BUFFER_SIZE = SIZE_MULTIPLIER * MIN_BUFFER;  // Buffer Size is in bytes; Data is in shorts;
	final static int SHORT_BUFFER = MIN_BUFFER * Byte.SIZE/Short.SIZE;
	
	/* The number of divisions in the sine array */
	final private static double SINE_POWER = 20;  // arbitrary 2^17 = 131,072 (probably fine); 2^20 = 1,048,576
	final private static int SINE_SIZE = (int) Math.pow(2.0, SINE_POWER);
	final private static int SINE_MASK = SINE_SIZE - 1;
	
	private static double[] sineArray = new double[SINE_SIZE];
	static {
		final double FACTOR = 2 * Math.PI/SINE_SIZE;
		for (int i = 0; i < SINE_SIZE; i++) {
			sineArray[i] = Math.sin(FACTOR*i);
		}
	}
	
	private Waveform waveform;
	private List<Partial> partials;
	
	private double pitch;
	private AudioTrack audio;

	private short[] data;

		

	public QuickTone() {
		audio = new AudioTrack(STREAM_TYPE, SAMPLE_RATE, CHANNEL, FORMAT, BUFFER_SIZE, MODE);
		data = new short[SHORT_BUFFER];
		pitch = 415.0;
		
		try {
			// Defaults to a sine wave
			setWaveform(new Waveform(Arrays.asList(1.0)));
		} catch (WaveformException e) {
			throw new IllegalStateException("Error Initializing default QuickTone Waveform: " + e.getMessage());
		}
	}

	public QuickTone(Waveform wave) {
		this();
		setWaveform(wave);
	}

	
	@Override
	public void setPitch(double pitch) {
		/* Sets the frequency of each of the partials (harmonics) in the given waveform 
		 * based on the given fundamental frequency (pitch) */
		Iterator<Partial> ps = partials.iterator();
		Partial p;
		double freq;
		
		for (int i = 0; i < waveform.getDepth(); i++) {
			/* only non-zero harmonics have counterparts in partials */
			if (waveform.get(i) != 0) {
				p = ps.next();
				freq = pitch * (i+1);
				p.setFreq(freq);
			}
		}
		this.pitch = pitch;
	}

	@Override
	public void setWaveform(Waveform wave) {
		waveform = wave;
		partials = new ArrayList<Partial>();
		for (double h : waveform.getHarmonics()) {
			/* only harmonics with non-zero weights get assigned a partial */
			if (h != 0) {
				partials.add(new Partial(h));
			}
		}
		setPitch(pitch);
	}

	/**
	 * Returns the phase of the wave to time 0.
	 */
	private void resetPartials() {
		for (Partial p : partials) {
			p.reset();
		}
	}
	
	@Override
	public void play() {
		if (audio.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
			return;
		}
	
		/* Don't want to hog the UI with wave calculations! */
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				resetPartials();
				/* Fills the AudioTrack's buffer initially, which is SIZE_MULTIPLIER bigger than the working buffer */
				for (int i=0; i<SIZE_MULTIPLIER; i++) {
					pushNextData();
				}

				audio.setPositionNotificationPeriod(SHORT_BUFFER);
				audio.setPlaybackPositionUpdateListener(new AudioTrack.OnPlaybackPositionUpdateListener() {

					/*Apparently, this loops around when it gets to the end. Everytime the the playback head
					 * reaches a multiple of our SHORT_BUFFER, it sends notification to calculate and accept some more
					 * data.
					 */
					@Override
					public void onPeriodicNotification(AudioTrack track) {
						pushNextData();
					}

					@Override
					public void onMarkerReached(AudioTrack track) {
						throw new UnsupportedOperationException();							
					}
				});

				audio.play();			
			}
			
		});
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();
	}

	/**
	 * Fills the audio data array and writes it to AudioTrack
	 */
	private void pushNextData() {
		generateData();
		audio.write(data, 0, SHORT_BUFFER);		
	}
	
	/**
	 * Fills in the audio data array with the next period of data points.
	 */
	private void generateData() {
		for (int i = 0; i < data.length; i++) {
			data[i] = nextSample();
		}
	}
	
	/**
	 * Returns the sum of the displacements of all partials at the next point in the wave cycles, normalized
	 * to a short.
	 */
	private short nextSample() {
		double result = 0.0;
		for (Partial p : partials) {
			result += p.next();
		}
		return (short) (Short.MAX_VALUE * result/waveform.getWeight());
	}
	
	@Override
	public void stop() {
		audio.pause();
		audio.flush();
	}

	@Override
	public boolean isPlaying() {
		return (audio.getState() == AudioTrack.PLAYSTATE_PLAYING);
	}
	
	/**
	 * Partials represent the non-zero-weighted harmonics in a Waveform. They allow for quick constant time calculation 
	 * of the displacement at consecutive sample frames. They do not allow random access of frames because they rely on a trick.
	 * 	 * 
	 * They consist of three fields:
	 * 
	 * weight		the relative weight of the partial
	 * frame		the index into the sine array (a ring-array) that corresponds to the current sample frame
	 * frameSize	the number of positions in the sine array to skip over when moving to the next sample frame.
	 * 					This is determined by the frequency of the tone. Higher frequency = bigger frameSize
	 * 
	 * 
	 * Since the Partials are all moving independently through the sine array (through calls to next()) it is imperative
	 * that the sine array is of very high resolution for this to work, so they don't get out of phase. Luckily, since it
	 * is just simple addition to move from one to the next (and some modulo) a bigger array doesn't translate to more time.
	 * 
	 * @author graham
	 *
	 */
	private class Partial {
		double weight;
		int frame;
		int frameSize;
		
		/**
		 * Defaults to the zeroth frame
		 * @param weight
		 */
		Partial(double weight) {
			this.weight = weight;
			frame = 0;
			frameSize = 0;
		}

		/**
		 * Sets to the zeroth frame, the beginning of a wave's cycle.
		 */
		void reset() {
			frame = 0;
		}

		
		/**
		 * Sets this partial to report values for the given frequency.
		 */
		void setFreq(double freq) {
			makeFrameSize(freq);
		}
		/**
		 * Determines the number of sine array frames to skip over for the given frequency and
		 * record that in frameSize
		 */
		private void makeFrameSize(double freq) {
			frameSize = (int) Math.rint((freq * SINE_SIZE)/SAMPLE_RATE);
		}

		/**		
		 * Produce the displacement corresponding to this partial and its weight based for the next sample
		 */
		double next() {
			double result = sineArray[frame];
			advanceFrame();
			return result * weight;
		}
		
		/**
		 * Prepares the frame for the next displacement reading.
		 */
		private void advanceFrame() {
			// since I'm using a power of true, this old trick is the same as % SINE_SIZE , which I believe this gets compiled to anyway.
			frame = (frame + frameSize) & SINE_MASK; 
		}
		
	}
}
