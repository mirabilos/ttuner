package com.thefourthspecies.ttuner;

import com.thefourthspecies.ttuner.R;

import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A multi-fragment activity consisting of several screens of instructions for TTuner.
 * Currently, there are four screens: About, Home, Temperaments, and Waveforms
 * @author graham
 *
 */
public class HelpMain extends FragmentActivity implements ActionBar.TabListener {
	/**
	 * A key for bundling.
	 */
	public final static String ITEM = "graham.ttuner.item";
	
	/**
	 * 
	 */
	public final static int ABOUT = 0;
	public final static int HOME = 1;
	public final static int TEMPERAMENTS = 2;
	public final static int WAVEFORMS = 3;
	
	static String version;
	
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help_main);
		
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// Show the Up button in the action bar.
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		
		// Accepts the number of the appropriate page to visit, from the calling activity.
		Intent intent = getIntent();
		int item = intent.getIntExtra(HelpMain.ITEM, HOME);
		mViewPager.setCurrentItem(item);
		
		try {
	        PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
	        version = pi.versionName;
	    } catch (NameNotFoundException e) {
	        version = "PICKLES!";
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help_main, menu);
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
//			NavUtils.navigateUpFromSameTask(this);
			
			// The parent for this activity is the Home screen. I don't necessarily want to go there;
			// I want to go to the activity that called Help. That's what onBackPressed() does.
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
		
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a HelpFragment (defined as a static inner class
			// below) with the page number as its lone argument to determine its contents.
			Fragment fragment = new HelpFragment();
			Bundle args = new Bundle();
			args.putInt(HelpFragment.ARG_SECTION_NUMBER, position);
			fragment.setArguments(args);
			return fragment;
		}
		
		// There are currently four help screens
		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case ABOUT:
				return getString(R.string.title_section0).toUpperCase(l);
			case HOME:
				return getString(R.string.title_section1).toUpperCase(l);
			case TEMPERAMENTS:
				return getString(R.string.title_section2).toUpperCase(l);
			case WAVEFORMS:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A fragment representing a section of the app, displaying all the appropriate text and images
	 * for this help file. 
	 */
	public static class HelpFragment extends Fragment {
		
		/**
		 * The base layer for this help screen. A vertical LinearLayout (the true root is a Scrollview).
		 * Text and images will be added in order vertically downwards. 
		 */
		LinearLayout root;
		
		Context context;
		
		// Text sizes
		final static float NORMAL = 14;
		final static float LARGE = 20;
		final static float SMALL = 12;
		
		// Text styles
		final static int BOLD = 1;
		final static int ITALIC = 2;
		
		/**
		 * The gravity attribute for centered text.
		 */
		final static int CENTER = 0x11;
		
		/**
		 * The fragment argument representing the screen number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section number";
		
		public HelpFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			context = this.getActivity();
			View help = inflater.inflate(R.layout.fragment_help, container, false);
			root = (LinearLayout) help.findViewById(R.id.root);
			
			int section = getArguments().getInt(ARG_SECTION_NUMBER);
			switch (section) {
				case ABOUT:
					makeAbout();
					break;
				case TEMPERAMENTS:
					makeTempHelp();
					break;
				case WAVEFORMS:
					makeWaveHelp();
					break;
				default:
					makeHomeHelp();
			}
			
			return help;	
		}
		
		/**
		 * The help screen for Waveforms
		 */
		private void makeWaveHelp() {
			addText(R.string.wave_intro);
			addText(R.string.wave_how, LARGE, false, BOLD);
			addText(R.string.wave_info);
			addText(R.string.wave_perf, LARGE, false, BOLD);
			addText(R.string.wave_resources);
			addText(R.string.wave_examples, LARGE, false, BOLD);
			addText(R.string.wave_st, NORMAL, false, BOLD);
			addText(R.string.wave_st_info);
			addText(R.string.wave_saw, NORMAL, false, BOLD);
			addText(R.string.wave_saw_info);
			addText(R.string.wave_sin, NORMAL, false, BOLD);
			addText(R.string.wave_sin_info);
			addText(R.string.wave_sq, NORMAL, false, BOLD);
			addText(R.string.wave_sq_info);
			addText(R.string.wave_ss, NORMAL, false, BOLD);
			addText(R.string.wave_ss_info);
		}

		/**
		 * The help screen for Temperaments
		 */
		private void makeTempHelp() {
			addText(R.string.temp_ways);
			addText(R.string.by_relationships, NORMAL, false, BOLD);
			addText(R.string.temp_rel_summary);
			addText(R.string.by_cents, NORMAL, false, BOLD);
			addText(R.string.temp_cent_summary);
			
			addText(R.string.temp_def_by_rel, LARGE, false, BOLD);
			addText(R.string.temp_rel_info1);
			addText(R.string.temp_rel_info_intervals, NORMAL, true, BOLD);
			addText(R.string.temp_rel_info2);
			addText(R.string.temp_rel_info_ex1, NORMAL, true, BOLD);
			addText(R.string.temp_rel_info3);
			addText(R.string.temp_rel_info_ex2, NORMAL, true, BOLD);
			addText(R.string.temp_rel_info4);
			addText(R.string.temp_rel_info4a, NORMAL, true, ITALIC);
			addText(R.string.temp_rel_info4b);			
			addText(R.string.temp_rel_info_plusminus, NORMAL, true, BOLD);
			addText(R.string.temp_rel_info5);
			addText(R.string.temp_rel_info_ex3, NORMAL, true, BOLD);
			addText(R.string.temp_rel_info6);
			addText(R.string.temp_rel_info_ps, NORMAL, true, BOLD);
			addText(R.string.temp_rel_info7);
			addText(R.string.temp_rel_info_ex4, NORMAL, true, BOLD);
			addText(R.string.temp_rel_info8);
			addText(R.string.temp_rel_info_ex5, NORMAL, true, BOLD);
			addText(R.string.temp_rel_info9);
			addText(R.string.temp_rel_info_ex6, NORMAL, true, BOLD);
			addText(R.string.temp_rel_info10);
			addText(R.string.temp_rel_info_ex7, NORMAL, true, BOLD);
			addText(R.string.temp_rel_info11);
			addText(R.string.temp_rel_info11a, NORMAL, true, ITALIC);	
			addText(R.string.temp_rel_info11b);
			addText(R.string.temp_rel_info_vallotti, NORMAL, true, BOLD);
			addText(R.string.temp_rel_info12);
			addText(R.string.temp_rel_info_enharmonics, NORMAL, false, ITALIC);
			
			addText(R.string.temp_def_by_cent, LARGE, false, BOLD);
			addText(R.string.temp_cent_info1);
			addText(R.string.temp_cent_info_notes, NORMAL, true, BOLD);
			addText(R.string.temp_cent_info2);
			
			addText(R.string.temp_notes, LARGE, false, BOLD);
			addText(R.string.temp_notes_info);
			addText(R.string.temp_notes_lb, NORMAL, false, BOLD);
			addText(R.string.temp_notes_lb_info);
			addLink(R.string.temp_notes_larips, R.string.temp_notes_larips);
			addText(R.string.temp_notes_wsw, NORMAL, false, BOLD);
			addText(R.string.temp_notes_wsw_info);
			addText(R.string.temp_notes_young, NORMAL, false, BOLD);
			addText(R.string.temp_notes_young_info);
			addText(R.string.temp_notes_just, NORMAL, false, BOLD);
			addText(R.string.temp_notes_just_info);
		}

		/**
		 * The help/welcome screen for Home
		 */
		private void makeHomeHelp() {
			addText(R.string.main_welcome, LARGE, false, BOLD);
			addText(R.string.main_explication);
			addText(R.string.main_functions_header, LARGE, false, BOLD);
			addText(R.string.main_functions);
			addText(R.string.main_play, NORMAL, false, BOLD);
			addText(R.string.main_play_info);
			addText(R.string.main_pitch, NORMAL, false, BOLD);
			addText(R.string.main_pitch_info);
			addText(R.string.main_temp, NORMAL, false, BOLD);
			addText(R.string.main_temp_info);
			addText(R.string.main_wave, NORMAL, false, BOLD);
			addText(R.string.main_wave_info);
			addText(R.string.main_note_nav, NORMAL, false, BOLD);
			addText(R.string.main_note_nav_info);
		}

		/**
		 * The About page, full of version history and authors and stuff.
		 */
		private void makeAbout() {
			addText(R.string.app_name, NORMAL, true);
			addText("Version " + version + ", July 2013", NORMAL, true);
			addDrawable(getResources().getDrawable(R.drawable.ttuner_logo));   
			addText(R.string.about_port);
			addLink(R.string.about_redowl_ttuner, R.string.about_visit);
			addText(R.string.about_authors, LARGE, false, BOLD);
			addText(R.string.about_jono);
			addLink(R.string.about_redowl, R.string.about_redowl_pull);
			addText(R.string.about_graham);
			addLink(R.string.about_email, R.string.about_email_pull);
			addText(R.string.about_history, LARGE, false, BOLD);
			addText(R.string.about_history_info);
			addText(R.string.about_g, NORMAL, false, BOLD);
		}
		
		
		/**
		 * Adds the next view to the root: a TextView.
		 * @param text		the text to be displayed
		 * @param size		the size in sp
		 * @param centered	when true, the text is centered; when false, it is left-justified
		 * @param style		can set as italics or bold or 0 (normal);
		 */
		private void addText(String text, float size, boolean centered, int style) {
			TextView tv = new TextView(context);
			tv.setTextIsSelectable(true);
			tv.setText(text);
			tv.setTypeface(null, style);
			tv.setTextColor(getResources().getColor(R.color.black));
			tv.setTextSize(size);
			if (centered) {
				tv.setGravity(CENTER); // code for "center"
			}
			root.addView(tv);		
		}
		private void addText(int textResource, float size, boolean centered, int style) {
			addText(getResources().getString(textResource), size, centered, style);
		}

		
		private void addText(String text, float size, boolean centered) {
			addText(text, size, centered, 0);   
		}
		
		private void addText(int textResource, float size, boolean centered) {
			addText(getResources().getString(textResource), size, centered);
		}
		
		private void addText(String text) {
			addText(text, NORMAL, false);
		}

		/**
		 * Add a TextView to root from the given string resource int. All normal and left-justified.
		 */
		private void addText(int textResource) {
			addText(getResources().getString(textResource));
		}
			
		
		/**
		 * Adds given Drawable as an ImageView centered as the next in the root.
		 * @param pic
		 */
		private void addDrawable(Drawable pic) {
			ImageView v = new ImageView(context);
			v.setImageDrawable(pic);
			root.addView(v);
		}
				
		/**
		 * Adds a centered borderless Button of blue normal text as next view in root. Creates an intent to 
		 * go out on the web to the given uri.
		 * @param uriTextResource		The uri to go to. 
		 * @param displayTextResource	The text to display in the Button.
		 */
		private void addLink(int uriTextResource, int displayTextResource) {
			final String displayText = getResources().getString(displayTextResource);
			final String uriText = getResources().getString(uriTextResource);
			Button b = new Button(context, null, android.R.style.Holo_ButtonBar);
			b.setText(displayText);
			b.setTypeface(null, BOLD);
			b.setTextSize(NORMAL);
			b.setTextColor(getResources().getColor(R.color.blue));
			b.setGravity(CENTER);
			
			b.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Uri uri = Uri.parse(uriText);
					Intent webLink = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(webLink);		
				}
			});
			
			root.addView(b);
			
		}
		
	}
	
	

}
