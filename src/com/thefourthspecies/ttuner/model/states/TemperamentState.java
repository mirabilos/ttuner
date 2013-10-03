package com.thefourthspecies.ttuner.model.states;


import java.util.List;

import com.thefourthspecies.ttuner.HelpMain;
import com.thefourthspecies.ttuner.NewTemperament;
import com.thefourthspecies.ttuner.model.listables.Listable;
import com.thefourthspecies.ttuner.model.listables.Temperament;

import android.app.Activity;

/**
 * Provides concrete methods unique for TemperamentState.
 * @author graham
 *
 */
public class TemperamentState extends AbstractListableState {
	TunerState state = TunerState.getInstance();
	
	@Override
	public void setListable(int position) {
		state.setTempPos(position);
		state.setTemperament((Temperament) getListable(position));

	}

	@Override
	public int getPos() {
		return state.getTempPos();
	}

	@Override
	public List<Listable> getAll() {
		return state.getRegistry().getTemperaments();
	}

	@Override
	public Class<? extends Activity> getEditor() {
		// from graham.ttuner package
		return NewTemperament.class;
	}

	@Override
	public String getType() {
		return "Temperament";
	}

	@Override
	public void delete(int position) {
		state.getRegistry().deleteTemp(position); // .deleteTemp((Temperament) getListable(position));		
	}

	

	@Override
	public Listable getListable(int position) {
		return state.getRegistry().getTemp(position);
	}

	@Override
	public void addListable(Listable l) {
		state.getRegistry().addTemp((Temperament) l);
	}

	@Override
	public int getHelp() {
		return HelpMain.TEMPERAMENTS;
	}
	
}
