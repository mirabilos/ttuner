package com.thefourthspecies.ttuner;


import com.thefourthspecies.ttuner.model.Keypad;
import com.thefourthspecies.ttuner.model.data.Relationship;
import com.thefourthspecies.ttuner.model.exceptions.TunerException;
import com.thefourthspecies.ttuner.model.listables.RelationshipsTemperament;
import com.thefourthspecies.ttuner.model.states.TunerState;

import com.thefourthspecies.ttuner.R;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class RelationshipsEditor extends ListActivity {
	TunerState state = TunerState.getInstance();
	RelationshipsTemperament temperament;
	ArrayAdapter<Relationship> adapter;
	
	EditText name;
	EditText editRel;
	
	Button addButton;
	Button cancelButton;
	Button deleteButton;
	
	Keypad keypad;
	
	/**
	 * The clear and delete buttons, in a vertical, hideable bar.
	 */
	LinearLayout buttons;
	
	/**
	 * True if this is a new temperament, not yet in the Registry.
	 */
	boolean isNew;
	
	int selected;
	
	Toast toast;
	
	@SuppressLint("ShowToast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_relationships_editor);
		// Show the Up button in the action bar.
		setupActionBar();
		
		Intent intent = getIntent();
		int position = intent.getIntExtra(ListableSelector.EXTRA_POSITION, -1);
		
		name = (EditText) findViewById(R.id.edit_name);
		editRel = (EditText) findViewById(R.id.edit_rel);
		addButton = (Button) findViewById(R.id.new_detail);
		cancelButton = (Button) findViewById(R.id.cancel);
		deleteButton = (Button) findViewById(R.id.delete);
		buttons = (LinearLayout) findViewById(R.id.buttons);
		
		keypad = new Keypad(editRel, this);
		
		
		setButtonHeights();
		
		initialize();
		buttons.setVisibility(View.INVISIBLE);
		
		toast = Toast.makeText(this, "", Toast.LENGTH_LONG);
		
		
		if (position == -1) {
			isNew = true;
			temperament = new RelationshipsTemperament();
		} else {
			isNew = false;
			temperament = (RelationshipsTemperament) state.getRegistry().getTemp(position);
			name.setText(temperament.getName());
		}
		
		name.setHint(temperament.getName());						
		
		adapter = new ArrayAdapter<Relationship>(this, R.layout.relationship, temperament.getRelationships());
		getListView().setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
		setListAdapter(adapter);

		getListView().requestFocus();
	}

	/**
	 * Empties edit zone and sets button to edit/add button to Add. Unselects everything
	 */
	private void initialize() {
		selected = -1;
		editRel.setText("");
		addButton.setText(R.string.button_new);
	}

	/**
	 * Shows clear and delete buttons and switchs add to edit. Also sets the current Relationship as the one
	 * that will be edited.
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		selected = position;
//		System.out.println("Selected = " + position);
//		addButton.setText(R.string.button_edit);            // ONLY DO THIS WHEN EDITING, AND NOT JUST ADDING IS ACTUALLY IMPLEMENTED!
		editRel.setText(adapter.getItem(position).getSymbol());
		showButtons();
		editRel.requestFocus();
		keypad.showKeypad();
		l.setSelectionFromTop(position, 0);
	}
	

	/**
	 * Called when Add button is pressed
	 * Attempts to add currently-input symbol as a Relationship. Toasts a warning if there is a problem
	 * @param view
	 */
	public void addDetail(View view) {
//		toggleKeypad();
		
		String symbol = editRel.getText().toString();
		if (symbol.equals("")) {
			initialize();
			return;
		}
		
		try {
			Relationship newRel = new Relationship(symbol);
			temperament.add(newRel);
			editRel.setText("");
			adapter.notifyDataSetChanged();
		} catch (TunerException e) {
			toast.setText(e.getMessage());
			toast.show();
		}
	
	}
	
	/**
	 * Called when Cancel button is pressed
	 * Clears the editzone and hides the buttons.
	 * @param v
	 */
	public void cancelEdit(View v) {
		clearSelection();
	}
	
	/**
	 * Called when Delete button is pressed
	 * Removes the selected detail and updates display.
	 */
	public void deleteDetail(View v) {
		if (adapter.getCount() == 1) {
			toast.setText(R.string.toast_one_relationship);
			toast.show();
		} else if (selected != -1) {
			temperament.remove(selected);	
			adapter.notifyDataSetChanged();
		}
		clearSelection();
	}
	
	
	/**
	 * Empties edit zone and hides buttons.
	 */
	private void clearSelection() {
		initialize();
		hideButtons();
		keypad.hideKeypad();
	}
	
		
	/**
	 * Hides the clear and delete buttons, animating if possible.
	 */
	@SuppressLint("NewApi")
	private void hideButtons() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			animateFade(buttons, 0.5f, 0, 100).withEndAction(new Runnable() {

				@Override
				public void run(){
					buttons.setVisibility(View.INVISIBLE);
				}
				
			});
		} else {
			buttons.setVisibility(Button.INVISIBLE);
		}
	}
	
	/**
	 * Shows clear and delete buttons, animating if possible.
	 */
	private void showButtons() {
		if (buttons.getVisibility() != Button.VISIBLE) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				animateFade(buttons, 0, 1, 500);
			} else {
				buttons.setVisibility(View.VISIBLE);
			}
		}
	}
	
	/**
	 * Animates a fade for the given view
	 * @param v		the given view
	 * @param start	alpha value at beginning
	 * @param end	alpha value at end
	 * @param dur	duration of animation
	 * @return		the animator
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private ViewPropertyAnimator animateFade(
			final View v, float start, float end, int dur) {
		v.setVisibility(View.VISIBLE);
		v.setAlpha(start);
		return v.animate(). alpha(end).setDuration(dur);
	}
			
	/**
	 * Uses math to ensure the buttons are at the same height as the Edit/Add button. Probably superfluous.
	 */
	private void setButtonHeights() {
		int height = editRel.getHeight();
		addButton.setMinHeight(height);
		cancelButton.setMinHeight(height);
		deleteButton.setMinHeight(height);		
	}
	
	
	
	@Override
	protected void onPause() {
		super.onPause();
		addDetail(null);
		
		String newName = name.getText().toString();
		
		if (temperament.size() == 0) {
			if (newName.length() > 0) {
				toast.setText("Aborting creation of new temperament: Must have at least one Relationship.");
				toast.show();
			}
			return;
		}
		
		if (temperament.size() > 0 && isNew ) {
			state.setTempPos(state.getRegistry().getTemperaments().size());
			state.getRegistry().addTemp(temperament);
		}
		if (newName.length() > 0) {
			temperament.setName(newName);
		}
		state.setTemperament(temperament);
		state.saveData();
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.temperament_editor, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;

		case R.id.help:
			Intent intent = new Intent(this, HelpMain.class);
			intent.putExtra(HelpMain.ITEM, HelpMain.TEMPERAMENTS);
			startActivity(intent);
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	
	/**
	 * Handles keypad press events
	 */
	public void onKeypadClick(View v) {
		keypad.handleEvent(v);
	}

	
}
