package com.thefourthspecies.ttuner.model.states;


import java.util.List;

import com.thefourthspecies.ttuner.HelpMain;
import com.thefourthspecies.ttuner.WaveformEditor;
import com.thefourthspecies.ttuner.model.listables.Listable;
import com.thefourthspecies.ttuner.model.listables.Waveform;

import android.app.Activity;

/**
 * Provides some concrete implementations unique to WaveformState
 * @author graham
 *
 */
public class WaveformState extends AbstractListableState {
	TunerState state = TunerState.getInstance();
	
	@Override
	public void setListable(int position) {
		state.setWavePos(position);
		state.setWaveform((Waveform) getListable(position));
	}

	@Override
	public int getPos() {
		return state.getWavePos();
	}

	@Override
	public List<Listable> getAll() {
		return state.getRegistry().getWaveforms();
	}

	@Override
	public Class<? extends Activity> getEditor() {
		// From ttuner package.
		return WaveformEditor.class;
	}

	@Override
	public String getType() {
		return "Waveform";
	}

	@Override
	public void delete(int position) {
		state.getRegistry().deleteWave(position);		
	}

	@Override
	public Listable getListable(int position) {
		return state.getRegistry().getWave(position);
		
	}

	@Override
	public void addListable(Listable listable) {
		state.getRegistry().addWave((Waveform) listable);
		
	}

	@Override
	public int getHelp() {
		return HelpMain.WAVEFORMS;
	}
	
}


