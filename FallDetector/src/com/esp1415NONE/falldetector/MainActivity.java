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

		//inizializzazione database
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

		//mi prendo il layout del drawer
		mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

		//imposto come l'ombra del drawer quando lo apro
		mDrawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		initMenu();
		mDrawerToggle = new CustomActionBarDrawerToggle(this, mDrawer);
		mDrawer.setDrawerListener(mDrawerToggle);

	}

	private void initMenu() {
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

		//pulsante impostazioni in alto a dx
		menu.findItem(R.id.action_settings).setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub

				fragmentManager.popBackStack();
				FragmentSettings ls_fragment = new FragmentSettings();
				fragmentTransaction.replace(R.id.frag_show_activity, ls_fragment);
				fragmentTransaction.commit();
				fragmentTransaction = fragmentManager.beginTransaction();
				return false;
			}
		});
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		//se il drawer e' aperto, nascondo il pulsante impostazioni in alto a dx
		boolean drawerOpen = mDrawer.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private class CustomActionBarDrawerToggle extends ActionBarDrawerToggle {

		public CustomActionBarDrawerToggle(Activity mActivity,DrawerLayout mDrawerLayout){
			super(
					mActivity,
					mDrawerLayout, 
					R.drawable.ic_drawer, //icona menu
					R.string.ns_menu_bar,
					R.string.ns_menu_bar);
		}

		@Override
		public void onDrawerClosed(View view) {
			getSupportActionBar().setTitle(getString(R.string.ns_menu_bar));
			ActivityCompat.invalidateOptionsMenu(MainActivity.this); // creato alla chiamata di onPrepareOptionsMenu()
		}

		@Override
		public void onDrawerOpened(View drawerView) {
			getSupportActionBar().setTitle(getString(R.string.ns_menu_bar));
			ActivityCompat.invalidateOptionsMenu(MainActivity.this); // creato alla chiamata di onPrepareOptionsMenu()
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
					FragmentHome ls_fragment = new FragmentHome();
					fragmentTransaction.replace(R.id.frag_show_activity, ls_fragment);
					fragmentTransaction.commit();

					//ricreo l'oggetto per nuova futura Transaction
					fragmentTransaction = fragmentManager.beginTransaction();
					mDrawer.closeDrawer(mDrawerList);

				}
				else if(ChronoService.isPlaying == 1 || ChronoService.isPlaying == -1) {
					fragmentManager.popBackStack();
					FragmentCurrentSession ls_fragment = new FragmentCurrentSession();
					fragmentTransaction.replace(R.id.frag_show_activity, ls_fragment);
					fragmentTransaction.commit();

					//ricreo l'oggetto per nuova futura Transaction
					fragmentTransaction = fragmentManager.beginTransaction();
					mDrawer.closeDrawer(mDrawerList);

				}
				break;
			case 2:

				if(ChronoService.isPlaying == 0) {
					fragmentManager.popBackStack();
					FragmentHome ls_fragment = new FragmentHome();
					fragmentTransaction.replace(R.id.frag_show_activity, ls_fragment);
					fragmentTransaction.commit();

					//ricreo l'oggetto per nuova futura Transaction
					fragmentTransaction = fragmentManager.beginTransaction();
					mDrawer.closeDrawer(mDrawerList);

				}
				else if(ChronoService.isPlaying == 1 || ChronoService.isPlaying == -1) {
					fragmentManager.popBackStack();
					FragmentCurrentSession ls_fragment = new FragmentCurrentSession();
					fragmentTransaction.replace(R.id.frag_show_activity, ls_fragment);
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
					FragmentListView ls_fragment = new FragmentListView();
					fragmentTransaction.replace(R.id.frag_show_activity, ls_fragment);
					fragmentTransaction.commit();
					fragmentTransaction = fragmentManager.beginTransaction();
					mDrawer.closeDrawer(mDrawerList);

				}
				break;

			case 4:

				fragmentManager.popBackStack();
				FragmentSettings ls_fragment = new FragmentSettings();
				fragmentTransaction.replace(R.id.frag_show_activity, ls_fragment);
				fragmentTransaction.commit();
				fragmentTransaction = fragmentManager.beginTransaction();
				mDrawer.closeDrawer(mDrawerList);

				break;

			case 5:

				fragmentManager.popBackStack();
				FragmentCredits ls_fragment1 = new FragmentCredits();
				fragmentTransaction.replace(R.id.frag_show_activity, ls_fragment1);
				fragmentTransaction.commit();
				fragmentTransaction = fragmentManager.beginTransaction();
				mDrawer.closeDrawer(mDrawerList);

				break;	



			default:

				//resetta il drawer di default
				mDrawer.closeDrawer(mDrawerList);

				break;
			}


		}

	}

}
