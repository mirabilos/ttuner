package com.thefourthspecies.ttuner;

import com.thefourthspecies.ttuner.R;

import java.util.ArrayList;
import java.util.List;

import com.thefourthspecies.ttuner.model.listables.Listable;
import com.thefourthspecies.ttuner.model.states.ListableState;
import com.thefourthspecies.ttuner.model.states.TunerState;

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
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Using Strategy pattern, allows for any kind of Listable to be displayed in a list here, provided it has 
 * an implementation of ListableState.
 * Shows list along with details of all Listables of state's type. Automatically switches TunerState to 
 * the currently selected Listable. Allows copying and (providing a unique name), deletion of selected.
 * (Cannot delete defaults.) Editing and New listable creation is handled by starting the ListableState's appropriate editing activity.
 * 
 * 
 * @author graham
 *
 */
public class ListableSelector extends ListActivity {
	
	/**
	 * Used for Intent to indicate which Listable is selected, and more importantly carries
	 * -1 if it is a new listable to be made.
	 */
	public final static String EXTRA_POSITION = "graham.ttuner.POSITION";
	
	
	/**
	 * The opacity of the undo bar.
	 */
	final static float UNDO_ALPHA = 0.9f;
	
	/**
	 * The time in ms that it takes for animated fading in or out.
	 */
	final static long FADE_TIME = 500;
	
	/**
	 * The time in ms that the undo bar stays visible
	 */
	final static long UNDO_TIME = 2000;
	
	/**
	 * Using Strategy pattern, this state handles all calls unique to the set listable-type.
	 */
	private ListableState state;
	
	/**
	 * The current list of Listables 
	 */
	private List<Listable> list;
	
	/**
	 * The Undo Bar which shows after Delete is pressed.
	 */
	private LinearLayout undoBar;
	
	/**
	 * The most recently-deleted Listable; stored here in case of Undo press.
	 */
	private Listable deletedListable;
	
	/**
	 * The position of the currently-selected Listable
	 */
	int selected;
	
	Toast toast;
	private ArrayAdapter<Listable> adapter;
	
	@SuppressLint("ShowToast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listable_selector);
		// Show the Up button in the action bar.
		setupActionBar();
	
		toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		undoBar = (LinearLayout) findViewById(R.id.undobar);
		
		selected = -1;
		list = new ArrayList<Listable>();
	
		adapter = new ArrayAdapter<Listable>(this, R.layout.row, R.id.name, list) {

			// To increase efficiency, I should make a ViewHolder for Tags and use convertView... TODO

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View row = super.getView(position, convertView, parent);
				Listable entry = getItem(position);
				// True if the row in this position is the selected row.
				boolean selectedRow = (position == selected);
				
				TextView name = (TextView) row.findViewById(R.id.name);
				name.setText(entry.getName());
				int c = entry.isDefault() ? android.R.color.white : R.color.blue;
				name.setTextColor(getResources().getColor(c));
				
				TextView details = (TextView) row.findViewById(R.id.details);
				details.setText(entry.getDetails());
				// By setting it as selecteded, enables the marquee action!
				details.setSelected(selectedRow);

				// This is how we set just one row, the right row, as checked!
				RadioButton button = (RadioButton) row.findViewById(R.id.radio);
				button.setChecked(selectedRow);

				return row;
			}

		};

		
		setListAdapter(adapter);
		state = TunerState.getInstance().getListableState();
	}
	
		
	@Override
	protected void onStart() {
		super.onStart();
		initialize();
	}
	
	private void initialize() {

		setTitle("Select " + state.getType());
		selected = state.getPos();

		adapter.clear();
		adapter.addAll(state.getAll());

		getListView().setSelection(selected);

		state.setListable(selected);

	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if (selected == position) return;

		setSelected(position);
		adapter.notifyDataSetChanged();
	}
		
	/**
	 * Assigns i to as the selected Listable both here and in the ListableState.
	 */
	private void setSelected(int i) {
		selected = i;
		state.setListable(i);
	}

	/**
	 * Called when Edit button pressed.
	 * Moves to appropriate activity for editing current listable, unless selected Listable is a default.
	 */
	public void editListable(View view) {
		toEditor(selected);
	}
	
	/**
	 * Called when New button pressed.
	 * Moves to appropriate activity for editing current listable, but with flag set for new.
	 */
	public void newListable(View view) {
		// On -1, the editor treats it as an empty, unnamed Listable
		toEditor(-1);
	}
	
	/**
	 * Does the actual switching to a new activity.
	 * @param selected
	 */
	private void toEditor(int selected) {
		if (selected >= 0 && adapter.getItem(selected).isDefault()) {
			toast.setText("Cannot edit defaults. Try making a copy first.");
			toast.show();
		} else {
			TunerState.getInstance().stopTone();
			Intent intent = new Intent(this, state.getEditor());
			intent.putExtra(EXTRA_POSITION, selected);
			startActivity(intent);
		}
	}
		
	/**
	 * Called when Copy is pressed
	 * Gets a copy of the selected listable added to the list and registry and updates the display.
	 */
	public void copyListable(View v) {
		int newPos = list.size();
		state.copy(selected);
		adapter.clear();
		adapter.addAll(state.getAll());
		adapter.notifyDataSetChanged();
		setSelected(newPos);
		getListView().setSelectionFromTop(newPos, 0);
	}


	/**
	 * Handles pressing of Delete button
	 * Deletes currently-selected item, if it is not default and more than one exist, otherwise shows Toast error message.
	 * If delete is succesful, shows UndoBar
	 */
	public void deleteListable(View view) {
		Listable l = adapter.getItem(selected);
		if (l.isDefault()) {
			toast.setText("Cannot delete defaults.");
			toast.show();
		} else {
			delete(selected);
			showUndo(l);
			
		}
	}

	/**
	 * Deletes item at given position
	 * @param position
	 */
	private void delete(int position) {
		if (list.size() > 1) {
			deletedListable = adapter.getItem(position);
			list.remove(position);
			state.delete(position);
			setSelected(position-1);
			adapter.notifyDataSetChanged();
		} else {
			toast.setText("There must be at least one.");
			toast.show();
		}
	}

	/**
	 * Show the UndoBar, animated fade if possible
	 */
	private void showUndo(Listable l) {
		System.out.println("Version: " + Build.VERSION.SDK_INT);
		TextView msg = (TextView) findViewById(R.id.undo_message);
		msg.setText("Deleted\n " + l.getName());
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			animateUndoBar();
		} else {
			staticUndoBar();   // TODO : THIS IS UNTESTED.
		}
	}

	/**
	 * The non-animated display of the UndoBar for the UNDO_TIME duration.
	 */
	private void staticUndoBar() {
		undoBar.setVisibility(View.VISIBLE);
		undoBar.setAlpha(UNDO_ALPHA);
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(UNDO_TIME);
					staticHide();
				} catch (InterruptedException e) {
					staticHide();
				}
				
			}
		});
		thread.start();
	}
	
	
	/**
	 * Hides undoBar for APIs that don't have animating.
	 */
	private void staticHide() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				undoBar.setVisibility(View.GONE);
			}
			
		});
	}
	
	/**
	 * Animates show, float, and fade of undoBar.
	 */
	@SuppressLint("NewApi")
	private void animateUndoBar() {
		undoBar.setVisibility(View.VISIBLE);
		animateFade(undoBar, .20f, UNDO_ALPHA, FADE_TIME).withEndAction(new Runnable() {

			@Override
			public void run() {
				animateFade(undoBar, UNDO_ALPHA, 0, FADE_TIME).setStartDelay(UNDO_TIME).withEndAction(new Runnable() {

					@Override
					public void run() {
						undoBar.setVisibility(View.GONE);
					}
				
				});
			}
			
		}).setStartDelay(0);
	}

	/**
	 * Animates a fade from alpha start to alpha end of v that last dur milliseconds.
	 * @param v		the view to animate
	 * @param start alpha value at beginning
	 * @param end	alpha value at end
	 * @param dur   duration in ms of animation
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private ViewPropertyAnimator animateFade(
			final View v, float start, float end, long dur) {
		v.setVisibility(View.VISIBLE);
		v.setAlpha(start);
		return v.animate().alpha(end).setDuration(dur);
	}
	
	/**
	 * Called if undo is pressed
	 * Returns deleted Listable to bottom of the list and updates display, and hides undobar.
	 * @param v
	 */
	public void handleUndo(View v) {
		undoDelete();
		undoBar.setVisibility(View.GONE);
	}
	
	/**
	 * Does actual returning of deleted listable to bottom of list and updating display so it is visible.
	 */
	private void undoDelete() {
		int newPos = state.getAll().size();		
		state.addListable(deletedListable);
		setSelected(newPos);
		adapter.add(deletedListable);
		getListView().setSelectionFromTop(newPos, 0);
	}

	
	
	@Override
	protected void onPause() {
		super.onPause();
		TunerState.getInstance().saveData();
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
		getMenuInflater().inflate(R.menu.listable_selector, menu);
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

//			// The reset button can be enabled by going to the meny layout and uncommenting it
//			case R.id.reset:
//				TunerState.getInstance().resetAll(state);
//				initialize();
//				return true;

			case R.id.help:
				Intent intent = new Intent(this, HelpMain.class);
				intent.putExtra(HelpMain.ITEM, state.getHelp());
				startActivity(intent);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	

	
//	MESSES UP POSITION/SELECTED	
	// A GOOD IDEA, BUT MUST BE HANDLED MORE CAREFULLY.
//	private void sort() {
//
//		adapter.sort(new Comparator<Listable>() {
//
//			@Override
//			public int compare(Listable lhs, Listable rhs) {
//				return lhs.getName().compareTo(rhs.getName());
//			}
//
//		});
//
//	}
}
