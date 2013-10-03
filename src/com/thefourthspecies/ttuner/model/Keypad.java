package com.thefourthspecies.ttuner.model;

import com.thefourthspecies.ttuner.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
//import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A Keypad for entering Relationships. The calling activity must have a concrete ViewGroup (e.g., a FrameLayout)
 * with id "keypad", which is where the keypad will display itself. Overrides
 * the given TextView's OnFocusListener and OnClickListener.
 * 
 * @author graham
 *
 */
public class Keypad extends View {
	TextView listener; 
	View keypad;
	
	/**
	 * Sets a textview to respond to keypad touches and a parent Activity that hosts both 
	 * the TextView and the Keypad (and contains the keypad ViewGroup in its xml).
	 */
	public Keypad(TextView v, Activity parent) {
		super(parent);
		
		LayoutInflater inflater = parent.getLayoutInflater();
		ViewGroup location = (ViewGroup) parent.findViewById(R.id.keypad);
		keypad = inflater.inflate(R.layout.keypad, location); 
		keypad.setVisibility(View.INVISIBLE);
		hideKeypad();
		
		
		setListener(v);
	}
	
	/**
	 * Sets a textview to activate and respond to keypad entry.
	 * @param v
	 */
	public void setListener(TextView v) {
		listener = v;
		v.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					showKeypad();
				} else {
					hideKeypad();
				}
			}

		});

		v.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showKeypad();
				
			}
			
		});
				
	}

	/**
	 * Hides the keypad so it takes up no space in layout.
	 */
	@SuppressLint("NewApi")
	public void hideKeypad() {
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			keypad.animate().translationY(keypad.getHeight())
				.withEndAction(new Runnable() {

					@Override
					public void run() {
						keypad.setVisibility(View.GONE);
					}
				});

		} else {
			keypad.setVisibility(View.GONE);	
		}
		
	}
	
	/**
	 * Shows the keypad
	 */
	@SuppressLint("NewApi")
	public void showKeypad() {
		if (keypad.getVisibility() == View.VISIBLE) {
			return;
		}
		
		keypad.setVisibility(View.VISIBLE);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			keypad.setTranslationY(keypad.getHeight());
			keypad.animate().translationYBy(-keypad.getHeight()).start();
		} 
	}

	public void handleEvent(View v) {
		
		int id = v.getId();
		switch(id) {

		//	Row 1:
			case R.id.kA:
				listener.append("A"); 
				break;
			case R.id.kB:
				listener.append("B"); 
				break;
			case R.id.kC:
				listener.append("C"); 
				break;
			
			case R.id.k1:
				listener.append("1"); 
				break;
			case R.id.k2:
				listener.append("2"); 
				break;
			case R.id.k3:
				listener.append("3"); 
				break;
			
			case R.id.kSlash:
				listener.append("/"); 
				break;
			
			// Row 2:	
			case R.id.kD:
				listener.append("D"); 
				break;
			case R.id.kE:
				listener.append("E"); 
				break;
			case R.id.kF:
				listener.append("F"); 
				break;
			
			case R.id.k4:
				listener.append("4"); 
				break;
			case R.id.k5:
				listener.append("5"); 
				break;
			case R.id.k6:
				listener.append("6"); 
				break;
			
			// Row 3:
			case R.id.kG:
				listener.append("G"); 
				break;
			case R.id.kSharp:
				listener.append("#"); 
				break;
			case R.id.kFlat:
				listener.append("b"); 
				break;
			
			case R.id.k7:
				listener.append("7"); 
				break;
			case R.id.k8:
				listener.append("8"); 
				break;
			case R.id.k9:
				listener.append("9"); 
				break;
			
	
			// Row 4:
			case R.id.kEquals:
				listener.append("="); 
				break;
			case R.id.kPlus:
				listener.append("+"); 
				break;
			case R.id.kMinus:
				listener.append("-"); 
				break;
			
			case R.id.k0:
				listener.append("0"); 
				break;
			case R.id.kP:
				listener.append("P"); 
				break;
			case R.id.kS:
				listener.append("S"); 
				break;
			
			case R.id.kBack:
				performBack();
				break;
		
		}
		
	}

	private void performBack() {
		String t = listener.getText().toString();
		int length = t.length();
		if (length > 0) {
			t = t.substring(0, length-1);
			listener.setText(t);	
		}
	}

}
