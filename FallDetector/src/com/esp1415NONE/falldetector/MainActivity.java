package com.esp1415NONE.falldetector;

import com.esp1415NONE.falldetector.classi.DbAdapter;
import com.esp1415NONE.falldetector.classi.NsMenuAdapter;
import com.esp1415NONE.falldetector.classi.NsMenuItemModel;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.app.Activity;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity {

	private ListView mDrawerList;
	private DrawerLayout mDrawer;
	private CustomActionBarDrawerToggle mDrawerToggle;
	private String[] menuItems;

	private FragmentTransaction fragmentTransaction;
	private FragmentManager fragmentManager;
	private DbAdapter dbAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_drawer);

		dbAdapter = new DbAdapter(getApplication());
		fragmentManager = getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		if (savedInstanceState == null)
		{
			//apre in automatico questa activity quando avvio l'app
			FragmentHome ls_fragment = new FragmentHome();
			fragmentTransaction.replace(R.id.frag_show_activity, ls_fragment);

			//mi serve per metterlo nello stack per il pulsante indietro
			fragmentTransaction.commit();
			fragmentTransaction = fragmentManager.beginTransaction();
		}

		/*utilizzo un metodo di supporto di appcompact, e agisco il pulsante 
		  dell'action bar per fare in modo che azioni il drawer*/
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

		//imposto come l'ombra del drawer quando lo apro
		mDrawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		_initMenu();
		mDrawerToggle = new CustomActionBarDrawerToggle(this, mDrawer);
		mDrawer.setDrawerListener(mDrawerToggle);

	}

	private void _initMenu() {
		NsMenuAdapter mAdapter = new NsMenuAdapter(this);

		// Aggiungo l'header
		mAdapter.addHeader(R.string.ns_menu);

		// Aggiungo il primo blocco

		menuItems = getResources().getStringArray(
				R.array.ns_menu_items);
		String[] menuItemsIcon = getResources().getStringArray(
				R.array.ns_menu_items_icon);

		int res = 0;
		for (String item : menuItems) {

			int id_title = getResources().getIdentifier(item, "string",
					this.getPackageName());
			int id_icon = getResources().getIdentifier(menuItemsIcon[res],
					"drawable", this.getPackageName());

			NsMenuItemModel mItem = new NsMenuItemModel(id_title, id_icon);
			mAdapter.addItem(mItem);
			res++;
		}


		mDrawerList = (ListView) findViewById(R.id.drawer);
		if (mDrawerList != null)
			mDrawerList.setAdapter(mAdapter);

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		menu.findItem(R.id.action_settings).setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub

				fragmentManager.popBackStack();
				FragmentSettings ls_fragment4 = new FragmentSettings();
				fragmentTransaction.replace(R.id.frag_show_activity, ls_fragment4);
				fragmentTransaction.commit();
				fragmentTransaction = fragmentManager.beginTransaction();
				return false;
			}
		});
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		// If the nav drawer is open, hide action items related to the content view
		boolean drawerOpen = mDrawer.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * The action bar home/up should open or close the drawer.
		 * ActionBarDrawerToggle will take care of this.
		 */
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		// Handle your other action bar items...
		return super.onOptionsItemSelected(item);
	}

	private class CustomActionBarDrawerToggle extends ActionBarDrawerToggle {

		public CustomActionBarDrawerToggle(Activity mActivity,DrawerLayout mDrawerLayout){
			super(
					mActivity,
					mDrawerLayout, 
					R.drawable.ic_drawer,
					R.string.ns_menu_bar, 
					R.string.ns_menu_bar);
		}

		@Override
		public void onDrawerClosed(View view) {
			getSupportActionBar().setTitle(getString(R.string.ns_menu_bar));
			ActivityCompat.invalidateOptionsMenu(MainActivity.this); // creates call to onPrepareOptionsMenu()
		}

		@Override
		public void onDrawerOpened(View drawerView) {
			getSupportActionBar().setTitle(getString(R.string.ns_menu_bar));
			ActivityCompat.invalidateOptionsMenu(MainActivity.this); // creates call to onPrepareOptionsMenu()
		}
	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener {


		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// Highlight the selected item, update the title, and close the drawer
			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			switch (position) {
			case 1:

				if(ChronoService.isPlaying == 0) {
					fragmentManager.popBackStack();
					FragmentHome ls_fragment1 = new FragmentHome();
					fragmentTransaction.replace(R.id.frag_show_activity, ls_fragment1);
					fragmentTransaction.commit();

					//ricreo l'oggetto per nuova futura Transaction
					fragmentTransaction = fragmentManager.beginTransaction();
					mDrawer.closeDrawer(mDrawerList);
				}
				else if(ChronoService.isPlaying == 1 || ChronoService.isPlaying == -1) {
					fragmentManager.popBackStack();
					FragmentCurrentSession ls_fragment1 = new FragmentCurrentSession();
					fragmentTransaction.replace(R.id.frag_show_activity, ls_fragment1);
					fragmentTransaction.commit();

					//ricreo l'oggetto per nuova futura Transaction
					fragmentTransaction = fragmentManager.beginTransaction();
					mDrawer.closeDrawer(mDrawerList);
				}
				break;
			case 2:

				if(ChronoService.isPlaying == 0) {
					fragmentManager.popBackStack();
					FragmentHome ls_fragment1 = new FragmentHome();
					fragmentTransaction.replace(R.id.frag_show_activity, ls_fragment1);
					fragmentTransaction.commit();

					//ricreo l'oggetto per nuova futura Transaction
					fragmentTransaction = fragmentManager.beginTransaction();
					mDrawer.closeDrawer(mDrawerList);
				}
				else if(ChronoService.isPlaying == 1 || ChronoService.isPlaying == -1) {
					fragmentManager.popBackStack();
					FragmentCurrentSession ls_fragment1 = new FragmentCurrentSession();
					fragmentTransaction.replace(R.id.frag_show_activity, ls_fragment1);
					fragmentTransaction.commit();

					//ricreo l'oggetto per nuova futura Transaction
					fragmentTransaction = fragmentManager.beginTransaction();
					mDrawer.closeDrawer(mDrawerList);
				}
				break;

			case 3:
				if(dbAdapter.getNumberSession() == 0) {
					Toast.makeText(getApplicationContext(), "Nessuna sessione", Toast.LENGTH_SHORT).show();
				}
				else {

					fragmentManager.popBackStack();
					FragmentListView ls_fragment3 = new FragmentListView();
					fragmentTransaction.replace(R.id.frag_show_activity, ls_fragment3);
					fragmentTransaction.commit();
					fragmentTransaction = fragmentManager.beginTransaction();
					mDrawer.closeDrawer(mDrawerList);
				}
				break;

			case 4:

				fragmentManager.popBackStack();
				FragmentSettings ls_fragment4 = new FragmentSettings();
				fragmentTransaction.replace(R.id.frag_show_activity, ls_fragment4);
				fragmentTransaction.commit();
				fragmentTransaction = fragmentManager.beginTransaction();
				mDrawer.closeDrawer(mDrawerList);
				break;

			case 5:

				fragmentManager.popBackStack();
				FragmentCredits ls_fragment5 = new FragmentCredits();
				fragmentTransaction.replace(R.id.frag_show_activity, ls_fragment5);
				fragmentTransaction.commit();
				fragmentTransaction = fragmentManager.beginTransaction();
				mDrawer.closeDrawer(mDrawerList);
				break;	



			default:
				//You should reset item counter

				mDrawer.closeDrawer(mDrawerList);

				break;
			}


		}

	}

}
